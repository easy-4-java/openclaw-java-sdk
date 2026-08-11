package io.github.easy4j.openclaw.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.exception.OpenClawWsRpcException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.ws.protocol.*;
import io.github.easy4j.openclaw.ws.protocol.params.*;
import io.github.easy4j.openclaw.ws.protocol.result.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.extension.logging.HttpLogLevel;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Gateway WebSocket 控制面客户端。它实现 challenge 握手、请求与响应关联、RPC 超时、chat 事件路由以及断线时的等待者清理。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawGatewayWsClient extends WebSocketClient implements AutoCloseable {

    /**
     * 当前客户端声明支持的 Gateway WebSocket 协议版本。
     */
    private static final int PROTOCOL_VERSION = 1;

    /**
     * 网络、握手或进程等待的默认超时为 {@code 120_000L}，单位为字段声明的计量单位。
     */
    private static final long DEFAULT_RPC_TIMEOUT_MS = 120_000L;

    /**
     * 客户端使用的不可变配置引用。
     */
    private final OpenClawHttpClientConfig config;
    /**
     * 负责协议 JSON 序列化与反序列化的映射器。
     */
    private final ObjectMapper objectMapper;
    /**
     * 按注册顺序保存的 WebSocket 事件监听器；并发分发时使用快照语义。
     */
    private final List<OpenClawWsListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 请求标识到待完成 RPC 的并发表；响应、超时和断线都必须移除对应项。
     */
    private final Map<String, PendingRpc> pendingRpcs = new ConcurrentHashMap<>();

    /**
     * 请求或运行标识到活动 chat 流的并发表；完成、失败和断线时清理。
     */
    private final Map<String, ChatStreamCollector> activeChatStreams = new ConcurrentHashMap<>();

    /**
     * 串行化连接、握手 Future 创建与关闭清理，避免并发 connect 重复初始化。
     */
    private final ReentrantLock connectLock = new ReentrantLock();

    /**
     * 串行化 WebSocket 帧写入，避免多个业务线程交叉发送。
     */
    private final ReentrantLock writeLock = new ReentrantLock();

    /**
     * 跨线程发布最近一次成功握手结果；断线时清空。
     */
    private final AtomicReference<HelloOk> helloOkRef = new AtomicReference<>();

    /**
     * 当前 WebSocket 连接与握手的完成信号；成功或失败时只完成一次。
     */
    private volatile CompletableFuture<HelloOk> connectFuture = new CompletableFuture<>();

    /**
     * 负责 Gateway 挑战超时检查的调度器；连接结束时关闭。
     */
    private final ScheduledExecutorService challengeScheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "openclaw-ws-challenge");
                t.setDaemon(true);
                return t;
            });

    // ============================================================
    // 构造
    // ============================================================

    /**
     * 根据配置解析 Gateway WebSocket 地址，并初始化挑战握手、RPC 关联和断线清理状态。
     *
     * @param config SDK 配置
     */
    public OpenClawGatewayWsClient(OpenClawHttpClientConfig config) {
        this(config, buildWsUri(config));
    }

    /**
     * 根据配置解析 Gateway WebSocket 地址，并初始化挑战握手、RPC 关联和断线清理状态。
     *
     * @param config SDK 配置
     * @param serverUri 目标服务地址
     */
    public OpenClawGatewayWsClient(OpenClawHttpClientConfig config, URI serverUri) {
        super(serverUri);
        this.config = config;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setConnectionLostTimeout(30);
    }

    private static URI buildWsUri(OpenClawHttpClientConfig config) {
        String base = config.getBaseUrl();
        if (OpenClawStrings.isBlank(base)) {
            throw new IllegalArgumentException("gatewayBaseUrl is required for WebSocket connection");
        }
        String wsUrl = base
                .replaceFirst("^https://", "wss://")
                .replaceFirst("^http://", "ws://")
                .replaceAll("/+$", "");
        return URI.create(wsUrl);
    }

    /**
     * 跨 WebSocket 回调与挑战超时任务共享的一次性 nonce。
     */
    private final AtomicReference<String> challengeNonce = new AtomicReference<>();

    /**
     * 网络、握手或进程等待的默认超时为 {@code 3_000L}，单位为字段声明的计量单位。
     */
    private static final long CHALLENGE_TIMEOUT_MS = 3_000L;

    // ============================================================
    // 生命周期
    // ============================================================

    /**
     * WebSocket 建立后等待 Gateway challenge，再发起认证握手。
     *
     * @param handshake WebSocket 服务端握手响应
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("WebSocket connected to {}, waiting for connect.challenge or timeout", getURI());
        challengeNonce.set(null);
        // 新版 Gateway 先发 challenge；超时后无 nonce 握手兼容旧协议，connectFuture 防止重复发送。
        challengeScheduler.schedule(() -> {
                    if (!connectFuture.isDone()) {
                        String nonce = challengeNonce.get();
                        if (nonce == null) {
                            debug(HttpLogLevel.BASIC, "connect.challenge not received within {}ms, sending connect without nonce", CHALLENGE_TIMEOUT_MS);
                        }
                        sendConnectHandshake();
                    }
                }, CHALLENGE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * 解析 WebSocket 文本帧，并按帧类型分发事件或完成待处理 RPC。
     *
     * @param message 消息正文
     */
    @Override
    public void onMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            String type = root.path("type").asText("");

            switch (type) {
                case "res":
                    handleResponse(root);
                    break;
                case "event":
                    // 先检查是否为 connect.challenge（预连接挑战）
                    if (handleConnectChallenge(root)) {
                        break;
                    }
                    handleEvent(root);
                    break;
                case "req":
                    debug(HttpLogLevel.BODY, "Received unexpected req frame from Gateway: {}", root);
                    break;
                default:
                    log.warn("Unknown frame type: {}", type);
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to parse WebSocket message: {}", message, e);
        }
    }

    /**
     * 连接关闭时停止挑战调度、失败所有待处理 RPC 并通知监听器。
     *
     * @param code HTTP、WebSocket 关闭或进程退出状态码
     * @param reason 连接关闭、结束或失败原因
     * @param remote 关闭事件是否由远端发起
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket closed: code={}, reason={}, remote={}", code, reason, remote);
        failConnectFuture(new RuntimeException("WebSocket closed: " + reason));
        helloOkRef.set(null);
        // 断线后响应不可能再到达，必须失败并清空所有等待 Future，避免永久悬挂。
        for (Map.Entry<String, PendingRpc> entry : pendingRpcs.entrySet()) {
            entry.getValue().future.completeExceptionally(
                    new RuntimeException("WebSocket closed: " + reason));
        }
        pendingRpcs.clear();
        // 活动流也以错误结束，保证每个 handler 都观察到明确终态。
        for (Map.Entry<String, ChatStreamCollector> entry : activeChatStreams.entrySet()) {
            entry.getValue().handler.onError("WebSocket closed: " + reason);
        }
        activeChatStreams.clear();
        // 通知监听器
        listeners.forEach(l -> l.onDisconnected(code, reason, remote));
    }

    /**
     * 注册或处理流、WebSocket 或回调执行异常。
     *
     * @param ex WebSocket 处理过程中报告的异常
     */
    @Override
    public void onError(Exception ex) {
        log.error("WebSocket error", ex);
        listeners.forEach(l -> l.onError(ex));
    }

    // ============================================================
    // 握手
    // ============================================================

    /**
     * 在连接锁内构造并发送 connect 请求；握手 Future 已完成时跳过重复发送。
     */
    private void sendConnectHandshake() {
        connectLock.lock();
        try {
            if (connectFuture.isDone()) {
                debug(HttpLogLevel.BASIC, "Skipping duplicate connect handshake");
                return;
            }

            ConnectParams.AuthInfo auth = null;
            String token = config.getGatewayAuthToken();
            String password = config.getGatewayAuthPassword();
            if (OpenClawStrings.isNotBlank(token)) {
                auth = ConnectParams.AuthInfo.token(token);
            } else if (OpenClawStrings.isNotBlank(password)) {
                auth = ConnectParams.AuthInfo.password(password);
            }

            ConnectParams params = new ConnectParams(
                    PROTOCOL_VERSION, PROTOCOL_VERSION,
                    new ConnectParams.ClientInfo(
                            "openclaw-java-sdk",
                            "OpenClaw Java SDK",
                            "1.0.0",
                            "java",
                            "operator"
                    ),
                    auth
            );

            String reqId = generateId();
            RequestFrame req = new RequestFrame(reqId, "connect", params.toParamsMap());

            PendingRpc pending = new PendingRpc(reqId, "connect", System.currentTimeMillis());
            pendingRpcs.put(reqId, pending);

            try {
                String json = objectMapper.writeValueAsString(req);
                debug(HttpLogLevel.BODY, "Sending connect handshake: {}", json);
                sendFrame(json);
            } catch (JsonProcessingException e) {
                failConnectFuture(e);
            }
        } finally {
            connectLock.unlock();
        }
    }

    /**
     * 开始一次新的连接尝试；已有连接会先关闭，再重置握手状态。
     *
     * @return 由 {@link #onOpen(ServerHandshake)} 后续握手完成的 Future
     */
    private CompletableFuture<HelloOk> beginConnectAttempt() {
        connectLock.lock();
        try {
            HelloOk existing = helloOkRef.get();
            CompletableFuture<HelloOk> current = connectFuture;
            if (isOpen() && existing != null && current.isDone() && !current.isCompletedExceptionally()) {
                return current;
            }
            if (isOpen()) {
                debug(HttpLogLevel.BASIC, "Closing existing WebSocket before reconnect");
                super.close();
            }
            resetConnectStateUnderLock();
            return connectFuture;
        } finally {
            connectLock.unlock();
        }
    }

    /**
     * 在持有 {@link #connectLock} 时取消旧握手，并创建新的握手 Future。
     */
    private void resetConnectStateUnderLock() {
        CompletableFuture<HelloOk> previous = connectFuture;
        if (!previous.isDone()) {
            previous.completeExceptionally(new CancellationException("Superseded by new connect attempt"));
        }
        helloOkRef.set(null);
        connectFuture = new CompletableFuture<>();
    }

    private void completeConnectFuture(HelloOk helloOk) {
        connectLock.lock();
        try {
            helloOkRef.set(helloOk);
            CompletableFuture<HelloOk> handshakeFuture = connectFuture;
            if (!handshakeFuture.isDone()) {
                handshakeFuture.complete(helloOk);
            }
        } finally {
            connectLock.unlock();
        }
    }

    private void failConnectFuture(Throwable cause) {
        connectLock.lock();
        try {
            CompletableFuture<HelloOk> handshakeFuture = connectFuture;
            if (!handshakeFuture.isDone()) {
                handshakeFuture.completeExceptionally(cause);
            }
        } finally {
            connectLock.unlock();
        }
    }

    /**
     * 在写锁保护下发送 Gateway JSON 帧，避免并发调用交叉写入 WebSocket。
     */
    private void sendFrame(String json) {
        writeLock.lock();
        try {
            send(json);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 建立 WebSocket 连接，完成 challenge/connect 握手，并在超时或断线时失败所有等待者。
     *
     * @return 握手成功后 Gateway 返回的版本、能力和服务信息
     * @throws InterruptedException 等待线程被中断时抛出，并恢复中断标记
     * @throws RuntimeException 远程响应、协议解析或本地执行失败时抛出
     */
    public HelloOk connectHandshake() throws InterruptedException {
        CompletableFuture<HelloOk> handshakeFuture = beginConnectAttempt();
        if (handshakeFuture.isDone() && !handshakeFuture.isCompletedExceptionally()) {
            HelloOk cached = handshakeFuture.getNow(null);
            if (cached != null) {
                return cached;
            }
        }
        if (!isOpen()) {
            super.connectBlocking();
        }
        try {
            return handshakeFuture.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            throw new RuntimeException("Gateway WS connect handshake failed", e);
        }
    }

    /**
     * 通过 WebSocket 异步执行 {@code connectHandshake}，调用线程不等待远端响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<HelloOk> connectHandshakeAsync() {
        CompletableFuture<HelloOk> handshakeFuture = beginConnectAttempt();
        if (handshakeFuture.isDone() && !handshakeFuture.isCompletedExceptionally()) {
            return handshakeFuture;
        }
        if (!isOpen()) {
            super.connect();
        }
        return handshakeFuture;
    }

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
    @Override
    public void close() {
        // 安全关闭调度器（防重复关闭）
        try {
            challengeScheduler.shutdownNow();
        } catch (Exception ignored) {
        }
        failConnectFuture(new CancellationException("Closed by client"));
        helloOkRef.set(null);
        super.close();
    }

    /**
     * 返回最近一次成功握手的 Gateway 信息；握手完成前为空。
     *
     * @return 最近一次成功握手的 Gateway 信息；尚未完成握手时为 {@code null}
     */
    public HelloOk getHelloOk() {
        return helloOkRef.get();
    }

    // ============================================================
    // RPC 调用（类型化 API）
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code sessionsList}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return 符合过滤条件的会话列表及默认会话配置
     */
    public SessionsListResult sessionsList(SessionsListParams params) {
        return invokeRpc("sessions.list", params, SessionsListResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code sessionsList}，并按请求标识关联响应。
     *
     * @return 使用 Gateway 默认过滤条件查询到的会话列表
     */
    public SessionsListResult sessionsList() {
        return sessionsList(SessionsListParams.defaults());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code chatHistory}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return 指定会话的历史消息和分页信息
     */
    public ChatHistoryResult chatHistory(ChatHistoryParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("chat.history", params, ChatHistoryResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code chatHistory}，并按请求标识关联响应。
     *
     * @param sessionKey 会话路由键
     * @param limit 最多返回的记录数；为空时使用 Gateway 默认限制
     * @return 指定会话的历史消息和分页信息
     */
    public ChatHistoryResult chatHistory(String sessionKey, Integer limit) {
        return chatHistory(ChatHistoryParams.of(sessionKey, limit));
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code chatAbort}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return Gateway 对取消请求的确认结果
     */
    public ChatAbortResult chatAbort(ChatAbortParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("chat.abort", params, ChatAbortResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code chatAbort}，并按请求标识关联响应。
     *
     * @param sessionKey 会话路由键
     * @return Gateway 对指定会话取消请求的确认结果
     */
    public ChatAbortResult chatAbort(String sessionKey) {
        return chatAbort(ChatAbortParams.abortSession(sessionKey));
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code agentIdentityGet}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return 智能体身份标识及其显示信息
     */
    public AgentIdentityGetResult agentIdentityGet(AgentIdentityGetParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("agent.identity.get", params, AgentIdentityGetResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code agentIdentityGet}，并按请求标识关联响应。
     *
     * @return 默认智能体的身份标识及其显示信息
     */
    public AgentIdentityGetResult agentIdentityGet() {
        return agentIdentityGet(AgentIdentityGetParams.empty());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code cronList}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return 符合过滤条件的定时任务摘要列表
     */
    public CronListResult cronList(CronListParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("cron.list", params, CronListResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code cronList}，并按请求标识关联响应。
     *
     * @return 使用默认过滤条件查询到的定时任务摘要列表
     */
    public CronListResult cronList() {
        return cronList(CronListParams.defaults());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code configGet}，并按请求标识关联响应。
     *
     * @return Gateway 当前配置及其版本哈希
     */
    public ConfigGetResult configGet() {
        return invokeRpc("config.get", Collections.emptyMap(), ConfigGetResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 同步执行 Gateway RPC，把成功帧 payload 转换为目标类型，并把错误帧映射为 {@link OpenClawWsRpcException}。
     *
     * @param method Gateway RPC 方法名
     * @param params 可序列化的 RPC 参数对象
     * @param responseType 成功 payload 的目标类型
     * @param timeoutMs 等待响应的最长毫秒数
     * @param <T> 成功响应类型
     * @return 转换后的 payload；响应没有 payload 时返回 null
     */
    private <T> T invokeRpc(String method, Object params, Class<T> responseType, long timeoutMs) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(responseType, "responseType");
        ResponseFrame frame = executeRpc(method, params, timeoutMs);
        if (!frame.isOk()) {
            throw new OpenClawWsRpcException(method, frame.getError());
        }
        Object payload = frame.getPayload();
        if (payload == null) {
            return null;
        }
        return objectMapper.convertValue(payload, responseType);
    }

    /**
     * 登记待处理请求、发送 req 帧并在超时范围内等待对应 res 帧。
     *
     * @param method Gateway RPC 方法名
     * @param params 可序列化的 RPC 参数对象
     * @param timeoutMs 等待响应的最长毫秒数
     * @return 与请求标识匹配的响应帧
     */
    private ResponseFrame executeRpc(String method, Object params, long timeoutMs) {
        requireHandshakeComplete(method);
        String reqId = generateId();
        PendingRpc pending = new PendingRpc(reqId, method, System.currentTimeMillis());
        // 先登记再发送，避免极快响应在映射建立前到达而无法关联。
        pendingRpcs.put(reqId, pending);

        try {
            Map<String, Object> paramsMap = serializeRpcParams(params);
            RequestFrame req = new RequestFrame(reqId, method, paramsMap);
            String json = objectMapper.writeValueAsString(req);
            sendFrame(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize RPC request: " + method, e);
        }

        try {
            return pending.future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("RPC interrupted: " + method, e);
        } catch (ExecutionException e) {
            throw new RuntimeException("RPC failed: " + method, e.getCause());
        } catch (TimeoutException e) {
            pendingRpcs.remove(reqId);
            throw new RuntimeException("RPC timeout: " + method + " (" + timeoutMs + "ms)");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> serializeRpcParams(Object params) throws JsonProcessingException {
        if (params == null) {
            return Collections.emptyMap();
        }
        if (params instanceof ChatSendParams) {
            return ((ChatSendParams) params).toParamsMap();
        }
        if (params instanceof SessionsSendParams) {
            return ((SessionsSendParams) params).toParamsMap();
        }
        if (params instanceof ConnectParams) {
            return ((ConnectParams) params).toParamsMap();
        }
        if (params instanceof Map) {
            return (Map<String, Object>) params;
        }
        return objectMapper.convertValue(params, Map.class);
    }

    private void requireHandshakeComplete(String method) {
        if (helloOkRef.get() == null) {
            throw new IllegalStateException(
                    "Gateway WS handshake not complete; call connectHandshake() before " + method);
        }
    }

    // ============================================================
    // chat.send（流式对话）
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code chatSend}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @param handler 事件处理器
     */
    public void chatSend(ChatSendParams params, ChatStreamHandler handler) {
        String reqId = generateId();
        ChatStreamCollector collector = new ChatStreamCollector(reqId, handler);
        activeChatStreams.put(reqId, collector);

        Map<String, Object> paramsMap = params.toParamsMap();
        // idempotencyKey: Gateway chat.send 要求
        paramsMap.put("idempotencyKey", reqId);

        try {
            RequestFrame req = new RequestFrame(reqId, "chat.send", paramsMap);
            String json = objectMapper.writeValueAsString(req);
            sendFrame(json);
        } catch (JsonProcessingException e) {
            activeChatStreams.remove(reqId);
            handler.onError("Failed to serialize chat.send request: " + e.getMessage());
        }
    }

    // ============================================================
    // sessions.send
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code sessionsSend}，并按请求标识关联响应。
     *
     * @param params 随 Gateway RPC 请求发送的参数对象
     * @return Gateway 对会话消息投递的确认结果
     */
    public SessionsSendResult sessionsSend(SessionsSendParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("sessions.send", params, SessionsSendResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    // ============================================================
    // 监听器管理
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code addListener}，并按请求标识关联响应。
     *
     * @param listener 生命周期监听器
     */
    public void addListener(OpenClawWsListener listener) {
        listeners.add(listener);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 {@code removeListener}，并按请求标识关联响应。
     *
     * @param listener 生命周期监听器
     */
    public void removeListener(OpenClawWsListener listener) {
        listeners.remove(listener);
    }

    // ============================================================
    // 内部帧处理
    // ============================================================

    private void handleResponse(JsonNode root) {
        String id = root.path("id").asText("");
        boolean ok = root.path("ok").asBoolean(false);
        Object payload = null;
        ErrorShape error = null;

        if (root.has("payload") && !root.path("payload").isNull()) {
            payload = objectMapper.convertValue(root.path("payload"), Object.class);
        }
        if (root.has("error") && !root.path("error").isNull()) {
            try {
                error = objectMapper.treeToValue(root.path("error"), ErrorShape.class);
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse error shape", e);
            }
        }

        ResponseFrame frame = new ResponseFrame("res", id, ok, payload, error);

        // 1. 检查是否为 connect 握手响应
        PendingRpc connectPending = pendingRpcs.remove(id);
        if (connectPending != null) {
            if ("connect".equals(connectPending.method) && ok && payload != null) {
                try {
                    JsonNode payloadNode = objectMapper.valueToTree(payload);
                    HelloOk helloOk = objectMapper.treeToValue(payloadNode, HelloOk.class);
                    completeConnectFuture(helloOk);
                    listeners.forEach(l -> l.onConnected(helloOk));
                } catch (Exception e) {
                    failConnectFuture(e);
                }
            } else if ("connect".equals(connectPending.method)) {
                failConnectFuture(
                        new RuntimeException("Connect failed: " + (error != null ? error : "unknown")));
            } else {
                // 非 connect 的普通 RPC
                connectPending.future.complete(frame);
            }
            return;
        }

        // 2. 检查是否为 chat.send 的 RPC 确认
        // chat.send 的实际回复通过 event 帧推送，但 RPC 响应也需消费
        if (activeChatStreams.containsKey(id)) {
            if (!ok && error != null) {
                ChatStreamCollector collector = activeChatStreams.remove(id);
                if (collector != null) {
                    collector.handler.onError(error.getMessage());
                }
            }
            // ok=true 时 chat.send 的内容通过 event 帧推送，无需在此处理
            return;
        }

        // 3. 普通监听器
        listeners.forEach(l -> l.onResponse(frame));
    }

    @SuppressWarnings("unchecked")
    private void handleEvent(JsonNode root) {
        String event = root.path("event").asText("");
        Object payload = null;
        Integer seq = root.has("seq") && !root.path("seq").isNull() ? root.path("seq").asInt() : null;

        if (root.has("payload") && !root.path("payload").isNull()) {
            payload = objectMapper.convertValue(root.path("payload"), Object.class);
        }

        EventFrame frame = new EventFrame("event", event, payload, seq);

        // chat 事件：处理流式回复
        if ("chat".equals(event) && payload != null) {
            handleChatEvent(payload);
        }

        // 通知监听器
        listeners.forEach(l -> l.onEvent(frame));
    }

    @SuppressWarnings("unchecked")
    private void handleChatEvent(Object payload) {
        Map<String, Object> p;
        if (payload instanceof Map) {
            p = (Map<String, Object>) payload;
        } else {
            try {
                p = objectMapper.convertValue(payload, Map.class);
            } catch (Exception e) {
                return;
            }
        }

        String runId = (String) p.get("runId");
        boolean done = Boolean.TRUE.equals(p.get("done"));
        String delta = (String) p.get("delta");

        // 匹配到活跃的 chat stream
        ChatStreamCollector collector = null;
        if (runId != null) {
            collector = activeChatStreams.get(runId);
        }
        // 旧版 Gateway 可能省略 runId；仅作兼容性回退，优先最近登记的活动流。
        if (collector == null && !activeChatStreams.isEmpty()) {
            collector = activeChatStreams.values().stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
        }

        if (collector == null) {
            return;
        }

        if (delta != null) {
            collector.textBuilder.append(delta);
            collector.handler.onDelta(delta);
        }

        if (done) {
            String fullText = collector.textBuilder.toString();
            activeChatStreams.remove(collector.reqId);
            collector.handler.onComplete(fullText);
        }
    }

    // ============================================================
    // 内部类
    // ============================================================

    /**
     * 尚未完成的 WebSocket RPC 上下文，记录请求标识、方法、开始时间以及等待响应的 Future。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static class PendingRpc {
        /**
         * 请求帧标识，用于把异步响应精确关联到当前 RPC。
         */
        final String id;
        /**
         * 失败或待完成的 Gateway RPC 方法名，用于关联响应和诊断。
         */
        final String method;
        /**
         * 创建待处理 RPC 时记录的时间戳，用于超时清理和耗时诊断。
         */
        final long timestamp;
        /**
         * 等待对应 RPC 响应的异步结果；响应、超时或断连时完成。
         */
        final CompletableFuture<ResponseFrame> future = new CompletableFuture<>();

        PendingRpc(String id, String method, long timestamp) {
            this.id = id;
            this.method = method;
            this.timestamp = timestamp;
        }
    }

    /**
     * 单次 chat.send 的流式聚合状态，保存回调、已接收文本和用于清理活动流的请求标识。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static class ChatStreamCollector {
        /**
         * 当前聊天流的请求标识，用于过滤其他请求的事件。
         */
        final String reqId;
        /**
         * 接收聊天流增量、完成和异常通知的处理器。
         */
        final ChatStreamHandler handler;
        /**
         * 仅由事件回调线程追加的聊天文本缓冲区。
         */
        final StringBuilder textBuilder = new StringBuilder();

        ChatStreamCollector(String reqId, ChatStreamHandler handler) {
            this.reqId = reqId;
            this.handler = handler;
        }
    }

    private static String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private void debug(HttpLogLevel level, String message, Object... arguments) {
        if (config.getDebug().allows(level)) {
            log.debug(message, arguments);
        }
    }

    /**
     * 识别 Gateway 的 connect.challenge 事件并保存 nonce，供后续认证握手使用。
     *
     * @param root 已解析的 Gateway event 帧 JSON
     * @return 识别并处理 challenge 时返回 {@code true}，其他事件返回 {@code false}
     */
    private boolean handleConnectChallenge(JsonNode root) {
        String event = root.path("event").asText("");
        if (!"connect.challenge".equals(event)) {
            return false;
        }
        try {
            JsonNode payload = root.path("payload");
            String nonce = payload.path("nonce").asText(null);
            if (nonce != null) {
                challengeNonce.set(nonce);
                debug(HttpLogLevel.BASIC, "Received connect.challenge with nonce, sending connect handshake");
            } else {
                debug(HttpLogLevel.BASIC, "Received connect.challenge without nonce");
            }
            // 收到挑战后立即发送 connect 握手
            sendConnectHandshake();
        } catch (Exception e) {
            log.warn("Failed to handle connect.challenge: {}", e.getMessage());
        }
        return true;
    }
}
