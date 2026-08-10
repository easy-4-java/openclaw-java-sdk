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
     * OpenClaw 协议固定值 {@code 1}；调用方不应在运行时修改。
     */
    private static final int PROTOCOL_VERSION = 1;

    /**
     * 用于限制等待时间的默认值 {@code 120_000L}，单位由字段名声明。
     */
    private static final long DEFAULT_RPC_TIMEOUT_MS = 120_000L;

    /**
     * SDK 配置。
     */
    private final OpenClawHttpClientConfig config;
    /**
     * JSON 映射器。
     */
    private final ObjectMapper objectMapper;
    /**
     * `OpenClawGatewayWsClient` 生命周期内保存的 `listeners` 对应状态。
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
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final ReentrantLock connectLock = new ReentrantLock();

    /**
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final ReentrantLock writeLock = new ReentrantLock();

    /**
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final AtomicReference<HelloOk> helloOkRef = new AtomicReference<>();

    /**
     * `OpenClawGatewayWsClient` 生命周期内保存的 `connectFuture` 对应状态。
     */
    private volatile CompletableFuture<HelloOk> connectFuture = new CompletableFuture<>();

    /**
     * `OpenClawGatewayWsClient` 生命周期内保存的 `challengeScheduler` 对应状态。
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
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawGatewayWsClient(OpenClawHttpClientConfig config) {
        this(config, buildWsUri(config));
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
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
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final AtomicReference<String> challengeNonce = new AtomicReference<>();

    /**
     * 用于限制等待时间的默认值 {@code 3_000L}，单位由字段名声明。
     */
    private static final long CHALLENGE_TIMEOUT_MS = 3_000L;

    // ============================================================
    // 生命周期
    // ============================================================

    /**
     * 接收并处理 Open 生命周期事件；实现不会改变事件顺序。
     *
     * @param handshake 写入 `handshake` 协议字段的内容
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
                            log.debug("connect.challenge not received within {}ms, sending connect without nonce", CHALLENGE_TIMEOUT_MS);
                        }
                        sendConnectHandshake();
                    }
                }, CHALLENGE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * 接收并处理 Message 生命周期事件；实现不会改变事件顺序。
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
                    log.debug("Received unexpected req frame from Gateway: {}", root);
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
     * 接收并处理 Close 生命周期事件；实现不会改变事件顺序。
     *
     * @param code 写入 `code` 协议字段的内容
     * @param reason 写入 `reason` 协议字段的内容
     * @param remote 写入 `remote` 协议字段的内容
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
     * 接收并处理 Error 生命周期事件；实现不会改变事件顺序。
     *
     * @param ex 写入 `ex` 协议字段的内容
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
 * {@link #connectLock} connect handshake;handshakecompletionskips, onOpen .
     */
    private void sendConnectHandshake() {
        connectLock.lock();
        try {
            if (connectFuture.isDone()) {
                log.debug("Skipping duplicate connect handshake");
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
                log.debug("Sending connect handshake: {}", json);
                sendFrame(json);
            } catch (JsonProcessingException e) {
                failConnectFuture(e);
            }
        } finally {
            connectLock.unlock();
        }
    }

    /**
 * connection:handshake;connectionreset connectFuture.
     *
 * @return handshakeCorresponds to Future( connectLock , onOpen )
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
                log.debug("Closing existing WebSocket before reconnect");
                super.close();
            }
            resetConnectStateUnderLock();
            return connectFuture;
        } finally {
            connectLock.unlock();
        }
    }

    /**
 * handshake Future Creates a new( {@link #connectLock}).
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
 * threadsecurity Gateway JSON .
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
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HelloOk
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
     * 使用 OkHttp/WebSocket 的异步机制发起 `connectHandshake`，调用线程不会等待远程响应。
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
     * 读取当前对象保存的 `helloOk` 对应状态，不触发网络或子进程调用。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HelloOk
     */
    public HelloOk getHelloOk() {
        return helloOkRef.get();
    }

    // ============================================================
    // RPC 调用（类型化 API）
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `sessionsList`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 SessionsListResult
     */
    public SessionsListResult sessionsList(SessionsListParams params) {
        return invokeRpc("sessions.list", params, SessionsListResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `sessionsList`，并按请求标识关联响应。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 SessionsListResult
     */
    public SessionsListResult sessionsList() {
        return sessionsList(SessionsListParams.defaults());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `chatHistory`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatHistoryResult
     */
    public ChatHistoryResult chatHistory(ChatHistoryParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("chat.history", params, ChatHistoryResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `chatHistory`，并按请求标识关联响应。
     *
     * @param sessionKey 会话路由键
     * @param limit 写入 `limit` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatHistoryResult
     */
    public ChatHistoryResult chatHistory(String sessionKey, Integer limit) {
        return chatHistory(ChatHistoryParams.of(sessionKey, limit));
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `chatAbort`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatAbortResult
     */
    public ChatAbortResult chatAbort(ChatAbortParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("chat.abort", params, ChatAbortResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `chatAbort`，并按请求标识关联响应。
     *
     * @param sessionKey 会话路由键
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatAbortResult
     */
    public ChatAbortResult chatAbort(String sessionKey) {
        return chatAbort(ChatAbortParams.abortSession(sessionKey));
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `agentIdentityGet`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 AgentIdentityGetResult
     */
    public AgentIdentityGetResult agentIdentityGet(AgentIdentityGetParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("agent.identity.get", params, AgentIdentityGetResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `agentIdentityGet`，并按请求标识关联响应。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 AgentIdentityGetResult
     */
    public AgentIdentityGetResult agentIdentityGet() {
        return agentIdentityGet(AgentIdentityGetParams.empty());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `cronList`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 CronListResult
     */
    public CronListResult cronList(CronListParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("cron.list", params, CronListResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `cronList`，并按请求标识关联响应。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 CronListResult
     */
    public CronListResult cronList() {
        return cronList(CronListParams.defaults());
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `configGet`，并按请求标识关联响应。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ConfigGetResult
     */
    public ConfigGetResult configGet() {
        return invokeRpc("config.get", Collections.emptyMap(), ConfigGetResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    /**
 * RPC ;{@code ok: false} {@link OpenClawWsRpcException}.
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
 * RPC: {@link ResponseFrame}( payload).
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
     * 通过已完成握手的 WebSocket 控制面调用 `chatSend`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
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
     * 通过已完成握手的 WebSocket 控制面调用 `sessionsSend`，并按请求标识关联响应。
     *
     * @param params 写入 `params` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 SessionsSendResult
     */
    public SessionsSendResult sessionsSend(SessionsSendParams params) {
        Objects.requireNonNull(params, "params");
        return invokeRpc("sessions.send", params, SessionsSendResult.class, DEFAULT_RPC_TIMEOUT_MS);
    }

    // ============================================================
    // 监听器管理
    // ============================================================

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `addListener`，并按请求标识关联响应。
     *
     * @param listener 生命周期监听器
     */
    public void addListener(OpenClawWsListener listener) {
        listeners.add(listener);
    }

    /**
     * 通过已完成握手的 WebSocket 控制面调用 `removeListener`，并按请求标识关联响应。
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
         * `PendingRpc` 生命周期内保存的 `id` 对应状态。
         */
        final String id;
        /**
         * `PendingRpc` 生命周期内保存的 `method` 对应状态。
         */
        final String method;
        /**
         * `PendingRpc` 生命周期内保存的 `timestamp` 对应状态。
         */
        final long timestamp;
        /**
         * `PendingRpc` 生命周期内保存的 `future` 对应状态。
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
         * `ChatStreamCollector` 生命周期内保存的 `reqId` 对应状态。
         */
        final String reqId;
        /**
         * 事件处理器。
         */
        final ChatStreamHandler handler;
        /**
         * `ChatStreamCollector` 生命周期内保存的 `textBuilder` 对应状态。
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
    /**
     * <p>
 * Gateway connect event, nonce ts.
 * nonce connect handshake( nonce).
     * </p>
     *
 * @param root event JSON
 * @return true ( connect.challenge),false
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
                log.debug("Received connect.challenge with nonce, sending connect handshake");
            } else {
                log.debug("Received connect.challenge without nonce");
            }
            // 收到挑战后立即发送 connect 握手
            sendConnectHandshake();
        } catch (Exception e) {
            log.warn("Failed to handle connect.challenge: {}", e.getMessage());
        }
        return true;
    }
}
