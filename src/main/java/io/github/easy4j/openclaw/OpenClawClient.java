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
 * OpenClaw SDK 统一门面。它按配置装配 HTTP、SSE、WebSocket 与本地 CLI 通道，执行可选启动检查，并只关闭由门面自身创建的资源。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawClient implements AutoCloseable {

    /**
     * `OpenClawClient` 生命周期内保存的 `gatewayHttpClient` 对应状态。
     */
    private final OpenClawWebhookClient gatewayHttpClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `chatClient` 对应状态。
     */
    private final OpenClawChatClient chatClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `sseClient` 对应状态。
     */
    private final OpenClawSseClient sseClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `embeddingsClient` 对应状态。
     */
    private final OpenClawEmbeddingsClient embeddingsClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `responsesClient` 对应状态。
     */
    private final OpenClawResponsesClient responsesClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `toolsInvokeClient` 对应状态。
     */
    private final OpenClawToolInvokeClient toolsInvokeClient;
    /**
     * `OpenClawClient` 生命周期内保存的 `cli` 对应状态。
     */
    private final OpenClawCli cli;
    /**
     * `OpenClawClient` 生命周期内保存的 `wsClient` 对应状态。
     */
    private final OpenClawGatewayWsClient wsClient;
    /**
     * 门面内部创建且由门面负责关闭的 OkHttpClient；外部注入时为 {@code null}。
     */
    private final OkHttpClient ownedHttpClient;

    // ============================================================
    // 构造器（单配置 / 双配置 / 组合配置 × 自动或强制）
    // ============================================================

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig) {
        this(httpConfig, new OpenClawCliConfig(), new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OkHttpClient httpClient) {
        this(httpConfig, new ObjectMapper(), httpClient);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(httpConfig, new OpenClawCliConfig(), objectMapper, httpClient, false);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param cliConfig CLI 配置
     */
    public OpenClawClient(OpenClawCliConfig cliConfig) {
        this(new OpenClawHttpClientConfig(), cliConfig, new ObjectMapper(), new OkHttpClient(), true);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param cliConfig CLI 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawCliConfig cliConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(new OpenClawHttpClientConfig(), cliConfig, objectMapper, httpClient, false);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param cliConfig CLI 配置
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        this(httpConfig, cliConfig, new ObjectMapper(), OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param cliConfig CLI 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
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
            this.sseClient = new OpenClawSseClient(httpConfig, objectMapper, httpClient);
            this.chatClient = new OpenClawChatClient(
                    httpConfig, objectMapper, httpClient, sseClient);
            this.embeddingsClient = new OpenClawEmbeddingsClient(httpConfig, objectMapper, httpClient);
            this.responsesClient = new OpenClawResponsesClient(httpConfig, objectMapper, httpClient);
            this.toolsInvokeClient = new OpenClawToolInvokeClient(httpConfig, objectMapper, httpClient);
            this.wsClient = new OpenClawGatewayWsClient(httpConfig);
        } else {
            this.gatewayHttpClient = null;
            this.chatClient = null;
            this.sseClient = null;
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
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawClient(OpenClawClientConfig config) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(config.getHttp()),
                true);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawClientConfig config, OkHttpClient httpClient) {
        this(config, new ObjectMapper(), httpClient);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                objectMapper,
                httpClient,
                false);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param cliConfig CLI 配置
     * @param gatewayHttpClient 写入 `gatewayHttpClient` 协议字段的内容
     * @param chatClient 写入 `chatClient` 协议字段的内容
     * @param sseClient 写入 `sseClient` 协议字段的内容
     * @param embeddingsClient 写入 `embeddingsClient` 协议字段的内容
     * @param responsesClient 写入 `responsesClient` 协议字段的内容
     * @param toolsInvokeClient 写入 `toolsInvokeClient` 协议字段的内容
     * @param cli 写入 `cli` 协议字段的内容
     * @param wsClient 写入 `wsClient` 协议字段的内容
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig,
                          OpenClawCliConfig cliConfig,
                          OpenClawWebhookClient gatewayHttpClient,
                          OpenClawChatClient chatClient,
                          OpenClawSseClient sseClient,
                          OpenClawEmbeddingsClient embeddingsClient,
                          OpenClawResponsesClient responsesClient,
                          OpenClawToolInvokeClient toolsInvokeClient,
                          OpenClawCli cli,
                          OpenClawGatewayWsClient wsClient) {
        this.gatewayHttpClient = gatewayHttpClient;
        this.chatClient = chatClient;
        this.sseClient = sseClient;
        this.embeddingsClient = embeddingsClient;
        this.responsesClient = responsesClient;
        this.toolsInvokeClient = toolsInvokeClient;
        this.cli = cli;
        this.wsClient = wsClient;
        this.ownedHttpClient = null;
    }

    /**
 * Executes during construction phase;per their respective sub-config fail-fast .
     * <p>
 * HTTP {@code gatewayBaseUrl} {@code enabled=false} skips;
 * CLI {@code enabled=false} skips.
     * </p>
     */
    private void runStartupChecks(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        if (httpConfig.isEnabled() && httpConfig.isStartupCheckEnabled()
                && OpenClawStrings.isNotBlank(httpConfig.getBaseUrl())) {
            try {
                chatClient.health();
                log.info("OpenClaw HTTP health check passed: {}", httpConfig.getBaseUrl());
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

    /**
     * 判断 `httpEnabled` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isHttpEnabled() {
        return chatClient != null;
    }

    /**
     * 判断 `cliEnabled` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isCliEnabled() {
        return cli != null;
    }

    // ============================================================
    // HTTP Webhook
    // ============================================================


    /**
     * 调用 OpenClaw 的 `wake` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param text 写入 `text` 协议字段的内容
     * @param mode 写入 `mode` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public String wake(String text, String mode) {
        return gatewayHttpClient.postHooksWake(text, mode);
    }

    /**
     * 调用 OpenClaw 的 `hook` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param hookName 写入 `hookName` 协议字段的内容
     * @param payload 写入 `payload` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public String hook(String hookName, Map<String, Object> payload) {
        return gatewayHttpClient.postMappedHook(hookName, payload);
    }

    /**
     * 调用 OpenClaw 的 `hook` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HookResponse
     */
    public HookResponse hook(HookRequest request) {
        return gatewayHttpClient.postHooksAgent(request);
    }

    // ============================================================
    // WebSocket 控制面
    // ============================================================

    /**
     * 调用 OpenClaw 的 `ws` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawGatewayWsClient；对应通道未启用时不可调用
     */
    public OpenClawGatewayWsClient ws() {
        return wsClient;
    }

    /**
     * 建立 WebSocket 连接，完成 challenge/connect 握手，并在超时或断线时失败所有等待者。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HelloOk
     * @throws OpenClawException 远程响应、协议解析或本地执行失败时抛出
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
     * 使用 OkHttp/WebSocket 的异步机制发起 `connect`，调用线程不会等待远程响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<HelloOk> connectAsync() {
        return wsClient.connectHandshakeAsync();
    }

    /**
     * 调用 OpenClaw 的 `chatSend` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param message 消息正文
     * @param handler 事件处理器
     */
    public void chatSend(String message, ChatStreamHandler handler) {
        wsClient.chatSend(ChatSendParams.builder().message(message).build(), handler);
    }

    /**
     * 调用 OpenClaw 的 `chatSend` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param sessionKey 会话路由键
     * @param message 消息正文
     * @param handler 事件处理器
     */
    public void chatSend(String sessionKey, String message, ChatStreamHandler handler) {
        wsClient.chatSend(
                ChatSendParams.builder().sessionKey(sessionKey).message(message).build(),
                handler);
    }

    /**
     * 调用 OpenClaw 的 `sessionsSend` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param sessionKey 会话路由键
     * @param message 消息正文
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 SessionsSendResult
     */
    public SessionsSendResult sessionsSend(String sessionKey, String message) {
        return wsClient.sessionsSend(
                SessionsSendParams.builder().key(sessionKey).message(message).build());
    }

    /**
     * 调用 OpenClaw 的 `addWsListener` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param listener 生命周期监听器
     * @return 当前门面持有的 OpenClawClient；对应通道未启用时不可调用
     */
    public OpenClawClient addWsListener(OpenClawWsListener listener) {
        wsClient.addListener(listener);
        return this;
    }

    // ============================================================
    // OpenAI 兼容 HTTP API（/v1/*）
    // ============================================================

    /**
     * 调用 OpenClaw 的 `chat` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawChatClient；对应通道未启用时不可调用
     */
    public OpenClawChatClient chat() {
        return chatClient;
    }

    /**
     * 调用 OpenClaw 的 `sse` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawSseClient；对应通道未启用时不可调用
     */
    public OpenClawSseClient sse() {
        return sseClient;
    }

    /**
     * 读取当前对象保存的 `okHttpClient` 对应状态，不触发网络或子进程调用。
     *
     * @return 当前门面持有的 OkHttpClient；对应通道未启用时不可调用
     */
    public OkHttpClient getOkHttpClient() {
        return Objects.nonNull(chatClient) ? chatClient.getHttpClient() : null;
    }

    /**
     * 调用 OpenClaw 的 `embeddings` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawEmbeddingsClient；对应通道未启用时不可调用
     */
    public OpenClawEmbeddingsClient embeddings() {
        return embeddingsClient;
    }

    /**
     * 调用 OpenClaw 的 `responses` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawResponsesClient；对应通道未启用时不可调用
     */
    public OpenClawResponsesClient responses() {
        return responsesClient;
    }

    // ----------------------------------------------------------------
    // Chat Completions 快捷方法
    // ----------------------------------------------------------------

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatClient.chatCompletion(request);
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `chatCompletion`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request) {
        return chatClient.chatCompletionAsync(request);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param cancellation 可选调用取消令牌
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request, HttpCallCancellation cancellation) {
        return chatClient.chatCompletion(request, null, cancellation);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param headersBuilder 写入 `headersBuilder` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request, OpenClawHeaders.Builder headersBuilder) {
        Map<String, String> headers = headersBuilder != null ? headersBuilder.build() : null;
        return chatClient.chatCompletion(request, headers);
    }

    // ----------------------------------------------------------------
    // Convenience methods（便捷方法）
    // ----------------------------------------------------------------

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param model 模型标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param model 模型标识
     * @param user 写入 `user` 协议字段的内容
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(String agent, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param model 模型标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param agent 写入 `agent` 协议字段的内容
     * @param model 模型标识
     * @param user 写入 `user` 协议字段的内容
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    // ----------------------------------------------------------------
    // Streaming（对齐 Hermes chatCompletionStream）
    // ----------------------------------------------------------------

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatClient.chatCompletionStream(request);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param callbackBuilder 写入 `callbackBuilder` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        return chatClient.chatCompletionStream(request, callbackBuilder);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStreamWithSession` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param sessionKey 会话路由键
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStreamWithSession(ChatRequest request, String sessionKey) {
        Map<String, String> headers = OpenClawHeaders.builder().sessionKey(sessionKey).build();
        return chatClient.chatCompletionStream(request, headers);
    }

    // ----------------------------------------------------------------
    // Models
    // ----------------------------------------------------------------

    /**
     * 调用 OpenClaw 的 `listModels` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ModelsResponse
     */
    public ModelsResponse listModels() {
        return chatClient.listModels();
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `listModels`，调用线程不会等待远程响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ModelsResponse> listModelsAsync() {
        return chatClient.listModelsAsync();
    }

    // ----------------------------------------------------------------
    // Embeddings
    // ----------------------------------------------------------------

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawClient`。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 EmbeddingsResponse
     */
    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddings(request);
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `createEmbeddings`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<EmbeddingsResponse> createEmbeddingsAsync(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddingsAsync(request);
    }

    // ----------------------------------------------------------------
    // Responses
    // ----------------------------------------------------------------

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawClient`。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ResponseResult
     */
    public ResponseResult createResponse(ResponseRequest request) {
        return responsesClient.createResponse(request);
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `createResponse`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ResponseResult> createResponseAsync(ResponseRequest request) {
        return responsesClient.createResponseAsync(request);
    }

    // ============================================================
    // Tools Invoke（/tools/invoke）
    // ============================================================

    /**
     * 调用 OpenClaw 的 `toolsInvoke` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 当前门面持有的 OpenClawToolInvokeClient；对应通道未启用时不可调用
     */
    public OpenClawToolInvokeClient toolsInvoke() {
        return toolsInvokeClient;
    }

    /**
     * 调用 OpenClaw 的 `toolInvoke` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ToolInvokeResult
     */
    public ToolInvokeResult toolInvoke(ToolInvokeRequest request) {
        return toolsInvokeClient.invoke(request);
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `toolInvoke`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ToolInvokeResult> toolInvokeAsync(ToolInvokeRequest request) {
        return toolsInvokeClient.invokeAsync(request);
    }

    /**
     * 调用 OpenClaw 的 `toolInvoke` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param cancellation 可选调用取消令牌
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ToolInvokeResult
     */
    public ToolInvokeResult toolInvoke(ToolInvokeRequest request, HttpCallCancellation cancellation) {
        return toolsInvokeClient.invoke(request, cancellation);
    }

    // ============================================================
    // CLI
    // ============================================================

    /**
     * 调用 OpenClaw 的 `cli` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 使用当前 CLI 配置和执行器创建的命令门面
     */
    public OpenClawCli cli() {
        return cli;
    }

    // ============================================================
    // 生命周期
    // ============================================================

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
    @Override
    public void close() {
        // 逐一释放所有子客户端资源，任一失败不影响其他
        closeQuietly(sseClient);
        closeQuietly(gatewayHttpClient);
        closeQuietly(chatClient);
        closeQuietly(embeddingsClient);
        closeQuietly(responsesClient);
        closeQuietly(toolsInvokeClient);
        closeQuietly(wsClient);
        OpenClawOkHttpClientFactory.shutdown(ownedHttpClient);
    }

    /**
 * Quietly closes().
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
