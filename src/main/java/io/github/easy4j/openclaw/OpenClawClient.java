package io.github.easy4j.openclaw;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.*;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import io.github.easy4j.openclaw.cli.OpenClawCli;
import io.github.easy4j.openclaw.cli.OpenClawCliExecutor;
import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityReport;
import io.github.easy4j.openclaw.api.model.*;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.api.model.ResponseResult;
import io.github.easy4j.openclaw.api.model.ToolInvokeRequest;
import io.github.easy4j.openclaw.api.model.ToolInvokeResult;
import io.github.easy4j.openclaw.exception.OpenClawException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.ws.ChatStreamHandler;
import io.github.easy4j.openclaw.ws.OpenClawGatewayWsClient;
import io.github.easy4j.openclaw.ws.OpenClawWsListener;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import io.github.easy4j.openclaw.ws.protocol.SessionsSendParams;
import io.github.easy4j.openclaw.ws.protocol.result.SessionsSendResult;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 门面：<b>HTTP Webhooks</b>（{@code /hooks/*}）+ <b>OpenAI 兼容 API</b>（{@code /v1/*}）+ <b>Tools Invoke</b>（{@code /tools/invoke}）+ <b>WebSocket 控制面</b> + 通用本地 CLI（{@link #cli()}）。
 * <p>
 * 五条通信通道相互独立，按各自子配置的 {@code enabled} 决定是否创建对应客户端：
 * </p>
 * <ul>
 *     <li>{@link OpenClawHttpClientConfig#isEnabled()} = false → HTTP / WS 子客户端为 {@code null}</li>
 *     <li>{@link OpenClawCliConfig#isEnabled()} = false → CLI 子客户端为 {@code null}</li>
 * </ul>
 *
 * <h3>构造器选择</h3>
 * <p>提供多种构造器覆盖三类场景：</p>
 * <ul>
 *     <li>仅 HTTP / 仅 CLI：传入单个子配置，禁用另一子系统</li>
 *     <li>HTTP + CLI：传入两个子配置，子系统都按各自 {@code enabled} 决定</li>
 *     <li>组合配置：传入 {@link OpenClawClientConfig}，内部拆分为两个子配置</li>
 * </ul>
 * <p>每种场景再分「自动 ObjectMapper/OkHttpClient」与「强制注入」两个变体。
 * 强制注入的版本对 {@code ObjectMapper}/{@code OkHttpClient} 进行 {@code requireNonNull} 校验，
 * 便于在多实例间共享连接池。</p>
 *
 * <h3>启动自检</h3>
 * <p>主构造器在子系统初始化后按 {@code startupCheckEnabled} 与 {@code failFastOnUnavailable}
 * 执行健康探测（HTTP：{@code GET /v1/models}；CLI：{@code openclaw --version}）。
 * 探测失败但未开启 fail-fast 时仅 WARN，不中断构造；开启 fail-fast 时抛 {@link IllegalStateException}。</p>
 *
 * @see OpenClawGatewayWsClient
 * @see OpenClawChatClient
 * @see OpenClawEmbeddingsClient
 * @see OpenClawResponsesClient
 * @see OpenClawToolInvokeClient
 */
@Slf4j
public class OpenClawClient implements AutoCloseable {

    private final OpenClawWebhookClient gatewayHttpClient;
    private final OpenClawChatClient chatClient;
    private final OpenClawEmbeddingsClient embeddingsClient;
    private final OpenClawResponsesClient responsesClient;
    private final OpenClawToolInvokeClient toolsInvokeClient;
    private final OpenClawCli cli;
    private final OpenClawGatewayWsClient wsClient;
    private final OkHttpClient ownedHttpClient;

    // ============================================================
    // 构造器（单配置 / 双配置 / 组合配置 × 自动或强制）
    // ============================================================

    /**
     * 仅 HTTP 子系统（CLI 禁用）。自动创建默认 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig) {
        this(httpConfig, new OpenClawCliConfig(), new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * 仅 HTTP 子系统，使用调用方管理的共享 {@link OkHttpClient}。
     * <p>适用于直接注入 Spring 容器中由 okhttp3-extension/starter 配置的客户端。</p>
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OkHttpClient httpClient) {
        this(httpConfig, new ObjectMapper(), httpClient);
    }

    /**
     * 仅 HTTP 子系统（CLI 禁用），强制注入共享 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(httpConfig, new OpenClawCliConfig(), objectMapper, httpClient, false);
    }

    /**
     * 仅 CLI 子系统（HTTP 禁用）。自动创建默认 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawCliConfig cliConfig) {
        this(new OpenClawHttpClientConfig(), cliConfig, new ObjectMapper(), new OkHttpClient(), true);
    }

    /**
     * 仅 CLI 子系统（HTTP 禁用），强制注入共享 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawCliConfig cliConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(new OpenClawHttpClientConfig(), cliConfig, objectMapper, httpClient, false);
    }

    /**
     * HTTP + CLI 子系统。自动创建默认 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        this(httpConfig, cliConfig, new ObjectMapper(), OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * HTTP + CLI 子系统，强制注入共享 {@link ObjectMapper} 与 {@link OkHttpClient}。
     * <p>
     * <b>主构造器</b>：所有参数 {@code requireNonNull}；HTTP/CLI 子客户端按各自
     * {@code enabled} 决定是否创建（禁用时为 {@code null}）；构造完成后按
     * {@code startupCheckEnabled} 与 {@code failFastOnUnavailable} 执行启动自检。
     * </p>
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig,
                          ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(httpConfig, cliConfig, objectMapper, httpClient, false);
    }

    private OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig,
                           ObjectMapper objectMapper, OkHttpClient httpClient, boolean ownsHttpClient) {
        Objects.requireNonNull(httpConfig, "httpConfig");
        Objects.requireNonNull(cliConfig, "cliConfig");
        Objects.requireNonNull(objectMapper, "objectMapper");
        Objects.requireNonNull(httpClient, "httpClient");
        this.ownedHttpClient = ownsHttpClient ? httpClient : null;

        boolean httpEnabled = httpConfig.isEnabled();
        boolean cliEnabled = cliConfig.isEnabled();

        // HTTP 子系统初始化（enabled=false 时不创建，字段为 null）
        if (httpEnabled) {
            this.gatewayHttpClient = new OpenClawWebhookClient(httpConfig, objectMapper, httpClient);
            this.chatClient = new OpenClawChatClient(httpConfig, objectMapper, httpClient);
            this.embeddingsClient = new OpenClawEmbeddingsClient(httpConfig, objectMapper, httpClient);
            this.responsesClient = new OpenClawResponsesClient(httpConfig, objectMapper, httpClient);
            this.toolsInvokeClient = new OpenClawToolInvokeClient(httpConfig, objectMapper, httpClient);
            this.wsClient = new OpenClawGatewayWsClient(httpConfig);
        } else {
            this.gatewayHttpClient = null;
            this.chatClient = null;
            this.embeddingsClient = null;
            this.responsesClient = null;
            this.toolsInvokeClient = null;
            this.wsClient = null;
        }

        // CLI 子系统初始化（enabled=false 时不创建，字段为 null）
        if (cliEnabled) {
            OpenClawCliExecutor exec = new OpenClawCliExecutor(cliConfig);
            this.cli = new OpenClawCli(exec);
        } else {
            this.cli = null;
        }

        // 启动自检（在 enabled 的子系统上执行）
        runStartupChecks(httpConfig, cliConfig);
    }

    /**
     * 组合配置，自动创建默认 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawClientConfig config) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(config.getHttp()),
                true);
    }

    /**
     * 使用组合配置和调用方管理的共享 {@link OkHttpClient}。
     * <p>SDK 关闭时不会关闭、清空或重建该客户端的连接池和调度器。</p>
     */
    public OpenClawClient(OpenClawClientConfig config, OkHttpClient httpClient) {
        this(config, new ObjectMapper(), httpClient);
    }

    /**
     * 组合配置，强制注入共享 {@link ObjectMapper} 与 {@link OkHttpClient}。
     */
    public OpenClawClient(OpenClawClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                objectMapper,
                httpClient,
                false);
    }

    /**
     * 全量依赖注入（用于测试或自定义组件）。
     * <p>使用此构造器<b>不会</b>执行任何启动自检；HTTP/CLI 字段由调用方决定。</p>
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig,
                          OpenClawCliConfig cliConfig,
                          OpenClawWebhookClient gatewayHttpClient,
                          OpenClawChatClient chatClient,
                          OpenClawEmbeddingsClient embeddingsClient,
                          OpenClawResponsesClient responsesClient,
                          OpenClawToolInvokeClient toolsInvokeClient,
                          OpenClawCli cli,
                          OpenClawGatewayWsClient wsClient) {
        this.gatewayHttpClient = gatewayHttpClient;
        this.chatClient = chatClient;
        this.embeddingsClient = embeddingsClient;
        this.responsesClient = responsesClient;
        this.toolsInvokeClient = toolsInvokeClient;
        this.cli = cli;
        this.wsClient = wsClient;
        this.ownedHttpClient = null;
    }

    /**
     * 在构造阶段执行启动自检；按各自子配置的 fail-fast 决定是否抛异常。
     * <p>
     * HTTP 探测在 {@code gatewayBaseUrl} 为空或 {@code enabled=false} 时跳过；
     * CLI 探测在 {@code enabled=false} 时跳过。
     * </p>
     */
    private void runStartupChecks(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        if (httpConfig.isEnabled() && httpConfig.isStartupCheckEnabled()
                && OpenClawStrings.isNotBlank(httpConfig.getGatewayBaseUrl())) {
            try {
                chatClient.health();
                log.info("OpenClaw HTTP health check passed: {}", httpConfig.getGatewayBaseUrl());
            } catch (Exception e) {
                if (httpConfig.isFailFastOnUnavailable()) {
                    throw new IllegalStateException(
                            "OpenClaw HTTP service is not available: " + e.getMessage()
                                    + ". Set OpenClawHttpClientConfig.enabled=false or startupCheckEnabled=false to disable.",
                            e);
                }
                log.warn("OpenClaw HTTP service is not available (continuing without strict check): {}", e.getMessage());
            }
        }

        if (cliConfig.isEnabled() && cliConfig.isStartupCheckEnabled()) {
            OpenClawCliAvailabilityReport report = new OpenClawCliExecutor(cliConfig).probe();
            if (!report.isAvailable()) {
                if (cliConfig.isFailFastOnUnavailable()) {
                    throw new IllegalStateException(
                            "OpenClaw CLI is not available: " + report.toDiagnosticMessage()
                                    + ". Set OpenClawCliConfig.enabled=false or startupCheckEnabled=false to disable.");
                }
                log.warn("OpenClaw CLI startup check failed (fail-fast disabled): {}",
                        report.toDiagnosticMessage());
            } else {
                log.info("OpenClaw CLI ready: {}", report.toDiagnosticMessage());
            }
        }
    }

    // ============================================================
    // 子系统启用状态查询
    // ============================================================

    /** HTTP 子系统是否启用（{@code enabled=true} 且 HTTP 客户端非 null）。 */
    public boolean isHttpEnabled() {
        return chatClient != null;
    }

    /** CLI 子系统是否启用（{@code enabled=true} 且 CLI 非 null）。 */
    public boolean isCliEnabled() {
        return cli != null;
    }

    // ============================================================
    // HTTP Webhook
    // ============================================================


    /**
     * 注入系统事件。
     * <p>对应 {@code POST /hooks/wake}。</p>
     *
     * @param text 事件文本
     * @param mode 唤醒模式（如 {@code "now"}）
     * @return 响应结果
     */
    public String wake(String text, String mode) {
        return gatewayHttpClient.postHooksWake(text, mode);
    }

    /**
     * 调用映射 webhook。
     * <p>对应 {@code POST /hooks/<name>}。</p>
     *
     * @param hookName webhook 名称
     * @param payload 请求数据
     * @return 响应结果
     */
    public String hook(String hookName, Map<String, Object> payload) {
        return gatewayHttpClient.postMappedHook(hookName, payload);
    }

    /**
     * 触发智能体。
     * <p>对应 {@code POST /hooks/agent}。</p>
     *
     * @param request 请求体
     * @return 智能体响应
     */
    public HookResponse hook(HookRequest request) {
        return gatewayHttpClient.postHooksAgent(request);
    }

    // ============================================================
    // WebSocket 控制面
    // ============================================================

    /**
     * 获取 WebSocket 客户端实例。
     * <p>通过 WS 客户端可实现：流式对话（{@code chat.send}）、会话管理、cron 管理、配置管理等。</p>
     *
     * <pre>{@code
     * OpenClawGatewayWsClient ws = client.ws();
     * ws.addListener(new OpenClawWsListener() { ... });
     * ws.connectBlocking();
     * ws.chatSend(ChatSendParams.builder().message("你好").build(), handler);
     * }</pre>
     *
     * @return WebSocket 客户端
     */
    public OpenClawGatewayWsClient ws() {
        return wsClient;
    }

    /**
     * 连接 WebSocket 并阻塞等待握手完成。
     *
     * @return 握手结果
     */
    public HelloOk connect() {
        try {
            return wsClient.connectHandshake();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenClawException("WS connect interrupted", e);
        }
    }

    /**
     * 异步连接 WebSocket。
     */
    public CompletableFuture<HelloOk> connectAsync() {
        return wsClient.connectHandshakeAsync();
    }

    /**
     * 通过 WebSocket 发送流式聊天。
     *
     * @param message 消息文本
     * @param handler 流式处理器
     */
    public void chatSend(String message, ChatStreamHandler handler) {
        wsClient.chatSend(ChatSendParams.builder().message(message).build(), handler);
    }

    /**
     * 通过 WebSocket 发送流式聊天（指定会话）。
     *
     * @param sessionKey 会话键
     * @param message    消息文本
     * @param handler    流式处理器
     */
    public void chatSend(String sessionKey, String message, ChatStreamHandler handler) {
        wsClient.chatSend(
                ChatSendParams.builder().sessionKey(sessionKey).message(message).build(),
                handler);
    }

    /**
     * 通过 WebSocket 向指定会话发消息（非流式 RPC）。
     */
    public SessionsSendResult sessionsSend(String sessionKey, String message) {
        return wsClient.sessionsSend(
                SessionsSendParams.builder().key(sessionKey).message(message).build());
    }

    /**
     * 添加 WebSocket 事件监听器。
     */
    public OpenClawClient addWsListener(OpenClawWsListener listener) {
        wsClient.addListener(listener);
        return this;
    }

    // ============================================================
    // OpenAI 兼容 HTTP API（/v1/*）
    // ============================================================

    /**
     * 获取 Chat Completions 客户端。
     */
    public OpenClawChatClient chat() {
        return chatClient;
    }

    /**
     * 获取 HTTP 子系统实际使用的 {@link OkHttpClient}。
     * <p>通过注入构造器传入时返回同一个实例，其生命周期仍由调用方管理。</p>
     *
     * @return HTTP 子系统使用的 OkHttpClient；HTTP 子系统禁用时返回 {@code null}
     */
    public OkHttpClient getOkHttpClient() {
        return Objects.nonNull(chatClient) ? chatClient.getHttpClient() : null;
    }

    /**
     * 获取 Embeddings 客户端。
     */
    public OpenClawEmbeddingsClient embeddings() {
        return embeddingsClient;
    }

    /**
     * 获取 Responses 客户端。
     */
    public OpenClawResponsesClient responses() {
        return responsesClient;
    }

    // ----------------------------------------------------------------
    // Chat Completions 快捷方法
    // ----------------------------------------------------------------

    /**
     * 发送 Chat Completions 请求（非流式）。
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatClient.chatCompletion(request);
    }

    /** 发送支持调用方取消的 Chat Completions 请求。 */
    public ChatResponse chatCompletion(ChatRequest request, HttpCallCancellation cancellation) {
        return chatClient.chatCompletion(request, null, cancellation);
    }

    /**
     * 发送 Chat Completions 请求，携带自定义请求头。
     */
    public ChatResponse chatCompletion(ChatRequest request, OpenClawHeaders.Builder headersBuilder) {
        Map<String, String> headers = headersBuilder != null ? headersBuilder.build() : null;
        return chatClient.chatCompletion(request, headers);
    }

    // ----------------------------------------------------------------
    // Convenience methods（便捷方法）
    // ----------------------------------------------------------------

    /**
     * 发送 chat completion 请求（简洁写法）。
     * <p>
     * 示例：
     * <pre>{@code
     * client.chatCompletion("openclaw/default", List.of(ChatMessage.ofUser("Hello")));
     * }</pre>
     * </p>
     *
     * @param agent    Agent 目标（如 {@code "openclaw/default"}）
     * @param messages 消息列表
     * @return Chat Completions 响应
     */
    public ChatResponse chatCompletion(String agent, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 发送 chat completion 请求（指定 Agent 和后端模型）。
     * <p>
     * 示例：
     * <pre>{@code
     * client.chatCompletion("openclaw/default", "gpt-4o", List.of(ChatMessage.ofUser("Hello")));
     * }</pre>
     * </p>
     *
     * @param agent    Agent 目标（如 {@code "openclaw/default"}）
     * @param model    后端 LLM 模型（如 {@code "gpt-4o"}）
     * @param messages 消息列表
     * @return Chat Completions 响应
     */
    public ChatResponse chatCompletion(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 发送 chat completion 请求（指定 Agent、模型和用户标识）。
     *
     * @param agent    Agent 目标
     * @param model    后端 LLM 模型
     * @param user     用户标识（用于派生稳定 session）
     * @param messages 消息列表
     * @return Chat Completions 响应
     */
    public ChatResponse chatCompletion(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    /**
     * 发送流式 chat completion 请求（指定 Agent）。
     *
     * @param agent    Agent 目标
     * @param messages 消息列表
     * @return 流式响应
     */
    public StreamingChatResponse chatCompletionStream(String agent, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 发送流式 chat completion 请求（指定 Agent 和后端模型）。
     *
     * @param agent    Agent 目标
     * @param model    后端 LLM 模型
     * @param messages 消息列表
     * @return 流式响应
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 发送流式 chat completion 请求（指定 Agent、模型和用户标识）。
     *
     * @param agent    Agent 目标
     * @param model    后端 LLM 模型
     * @param user     用户标识（用于派生稳定 session）
     * @param messages 消息列表
     * @return 流式响应
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    // ----------------------------------------------------------------
    // Streaming（对齐 Hermes chatCompletionStream）
    // ----------------------------------------------------------------

    /**
     * 流式 chat completion，返回 {@link StreamingChatResponse}。
     *
     * <pre>{@code
     * client.chatCompletionStream(request)
     *     .onDelta(delta -> System.out.print(delta))
     *     .onComplete(text -> System.out.println("\n完成"))
     *     .onError(error -> error.printStackTrace());
     * }</pre>
     *
     * @param request 请求体（自动设 stream=true）
     * @return 流式响应，支持链式回调
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatClient.chatCompletionStream(request);
    }

    /**
     * 流式 chat completion，使用 Builder 模式注册回调。
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        return chatClient.chatCompletionStream(request, callbackBuilder);
    }

    /**
     * 流式 chat completion，按 sessionKey 路由。
     */
    public StreamingChatResponse chatCompletionStreamWithSession(ChatRequest request, String sessionKey) {
        Map<String, String> headers = OpenClawHeaders.builder().sessionKey(sessionKey).build();
        return chatClient.chatCompletionStream(request, headers);
    }

    // ----------------------------------------------------------------
    // Models
    // ----------------------------------------------------------------

    /**
     * 获取可用模型/agent 目标列表。
     */
    public ModelsResponse listModels() {
        return chatClient.listModels();
    }

    // ----------------------------------------------------------------
    // Embeddings
    // ----------------------------------------------------------------

    /**
     * 创建嵌入向量。
     */
    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddings(request);
    }

    // ----------------------------------------------------------------
    // Responses
    // ----------------------------------------------------------------

    /**
     * 发送 OpenResponses 请求。
     */
    public ResponseResult createResponse(ResponseRequest request) {
        return responsesClient.createResponse(request);
    }

    // ============================================================
    // Tools Invoke（/tools/invoke）
    // ============================================================

    /**
     * 获取 Tools Invoke 客户端实例。
     */
    public OpenClawToolInvokeClient toolsInvoke() {
        return toolsInvokeClient;
    }

    /**
     * 调用单个工具。
     */
    public ToolInvokeResult toolInvoke(ToolInvokeRequest request) {
        return toolsInvokeClient.invoke(request);
    }

    public ToolInvokeResult toolInvoke(ToolInvokeRequest request, HttpCallCancellation cancellation) {
        return toolsInvokeClient.invoke(request, cancellation);
    }

    // ============================================================
    // CLI
    // ============================================================

    /**
     * 官方 CLI 顶层命令封装。
     */
    public OpenClawCli cli() {
        return cli;
    }

    // ============================================================
    // 生命周期
    // ============================================================

    @Override
    public void close() {
        // 逐一释放所有子客户端资源，任一失败不影响其他
        closeQuietly(gatewayHttpClient);
        closeQuietly(chatClient);
        closeQuietly(embeddingsClient);
        closeQuietly(responsesClient);
        closeQuietly(toolsInvokeClient);
        closeQuietly(wsClient);
        OpenClawOkHttpClientFactory.shutdown(ownedHttpClient);
    }

    /**
     * 安全关闭资源（失败不抛异常、不影响后续释放）。
     */
    private static void closeQuietly(AutoCloseable resource) {
        if (resource == null) {
            return;
        }
        try {
            resource.close();
        } catch (Exception e) {
            log.warn("Failed to close {}: {}", resource.getClass().getSimpleName(), e.getMessage());
        }
    }
}
