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
     * 复用统一配置访问 Gateway 通用 HTTP 端点的客户端。
     */
    private final OpenClawWebhookClient gatewayHttpClient;
    /**
     * 处理同步聊天与流式聊天入口的客户端。
     */
    private final OpenClawChatClient chatClient;
    /**
     * 负责 SSE 连接、解析和取消传播的客户端。
     */
    private final OpenClawSseClient sseClient;
    /**
     * 访问向量嵌入端点的客户端。
     */
    private final OpenClawEmbeddingsClient embeddingsClient;
    /**
     * 访问 Responses API 的客户端。
     */
    private final OpenClawResponsesClient responsesClient;
    /**
     * 访问工具调用端点的客户端。
     */
    private final OpenClawToolInvokeClient toolsInvokeClient;
    /**
     * 在本机执行 OpenClaw 子命令的门面；不经过 HTTP 或 WebSocket。
     */
    private final OpenClawCli cli;
    /**
     * Gateway WebSocket 控制面客户端；仅在配置 WebSocket 地址时创建。
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
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig) {
        this(httpConfig, new OpenClawCliConfig(), new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OkHttpClient httpClient) {
        this(httpConfig, new ObjectMapper(), httpClient);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(httpConfig, new OpenClawCliConfig(), objectMapper, httpClient, false);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param cliConfig CLI 配置
     */
    public OpenClawClient(OpenClawCliConfig cliConfig) {
        this(new OpenClawHttpClientConfig(), cliConfig, new ObjectMapper(), new OkHttpClient(), true);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param cliConfig CLI 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawCliConfig cliConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(new OpenClawHttpClientConfig(), cliConfig, objectMapper, httpClient, false);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param cliConfig CLI 配置
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        this(httpConfig, cliConfig, new ObjectMapper(), OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
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
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
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
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param config SDK 配置
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawClient(OpenClawClientConfig config, OkHttpClient httpClient) {
        this(config, new ObjectMapper(), httpClient);
    }

    /**
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
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
     * 构造统一 SDK 门面，并按传入配置装配 HTTP、SSE、WebSocket 与 CLI 子客户端；外部依赖保持调用方所有权。
     *
     * @param httpConfig HTTP 与 WebSocket 配置
     * @param cliConfig CLI 配置
     * @param gatewayHttpClient Gateway 通用 HTTP 接口客户端
     * @param chatClient 聊天补全接口客户端
     * @param sseClient 负责建立和管理 SSE 流的客户端
     * @param embeddingsClient 向量嵌入接口客户端
     * @param responsesClient Responses API 客户端
     * @param toolsInvokeClient 工具调用接口客户端
     * @param cli 本地 OpenClaw CLI 门面
     * @param wsClient Gateway WebSocket 控制面客户端
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
     * 构造阶段按子配置执行 HTTP 健康检查和 CLI 可用性检查；任一启用通道失败都会立即终止初始化。
     * HTTP 或 CLI 通道显式禁用时跳过对应检查。
     *
     * @param httpConfig HTTP 启用状态、地址和启动检查配置
     * @param cliConfig CLI 启用状态和启动检查配置
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
     * 判断是否配置了可用的 Gateway HTTP 地址。
     *
     * @return 当前聚合客户端是否已配置 HTTP 传输
     */
    public boolean isHttpEnabled() {
        return chatClient != null;
    }

    /**
     * 判断是否配置并创建了本地 CLI 通道。
     *
     * @return 当前聚合客户端是否已配置 CLI 传输
     */
    public boolean isCliEnabled() {
        return cli != null;
    }

    // ============================================================
    // HTTP Webhook
    // ============================================================


    /**
     * 通过 Wake Webhook 唤醒默认智能体，并返回服务端响应文本。
     *
     * @param text 发送给默认智能体的唤醒文本
     * @param mode Wake Hook 使用的唤醒模式；未指定时可为 {@code null}
     * @return Wake Webhook 返回的响应正文
     */
    public String wake(String text, String mode) {
        return gatewayHttpClient.postHooksWake(text, mode);
    }

    /**
     * 调用指定名称的 Webhook，并传递结构化负载。
     *
     * @param hookName 目标 Hook 的注册名称
     * @param payload 序列化为目标 Hook 请求体的键值负载
     * @return 指定 Webhook 返回的响应正文
     */
    public String hook(String hookName, Map<String, Object> payload) {
        return gatewayHttpClient.postMappedHook(hookName, payload);
    }

    /**
     * 调用指定名称的 Webhook，并传递结构化负载。
     *
     * @param request 要校验、序列化并发送的 {@code HookRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HookResponse
     */
    public HookResponse hook(HookRequest request) {
        return gatewayHttpClient.postHooksAgent(request);
    }

    // ============================================================
    // WebSocket 控制面
    // ============================================================

    /**
     * 返回已配置的 Gateway WebSocket 客户端；未启用 WebSocket 时为空。
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
     * 通过 OkHttp Dispatcher 异步执行 {@code connect}，调用线程不等待远端响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<HelloOk> connectAsync() {
        return wsClient.connectHandshakeAsync();
    }

    /**
     * 通过 Gateway WebSocket 发送聊天消息，并把增量事件交给处理器。
     *
     * @param message 消息正文
     * @param handler 事件处理器
     */
    public void chatSend(String message, ChatStreamHandler handler) {
        wsClient.chatSend(ChatSendParams.builder().message(message).build(), handler);
    }

    /**
     * 通过 Gateway WebSocket 发送聊天消息，并把增量事件交给处理器。
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
     * 通过 Gateway WebSocket 向会话发送消息并等待结构化结果。
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
     * 向 Gateway WebSocket 客户端注册生命周期和协议事件监听器。
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
     * 返回 Chat Completions 客户端。
     *
     * @return 当前门面持有的 OpenClawChatClient；对应通道未启用时不可调用
     */
    public OpenClawChatClient chat() {
        return chatClient;
    }

    /**
     * 返回负责流式响应读取与取消的 SSE 客户端。
     *
     * @return 当前门面持有的 OpenClawSseClient；对应通道未启用时不可调用
     */
    public OpenClawSseClient sse() {
        return sseClient;
    }

    /**
     * 返回当前门面复用的 OkHttpClient；调用方不得通过此引用提前关闭共享资源。
     *
     * @return 当前门面持有的 OkHttpClient；对应通道未启用时不可调用
     */
    public OkHttpClient getOkHttpClient() {
        return Objects.nonNull(chatClient) ? chatClient.getHttpClient() : null;
    }

    /**
     * 返回 Embeddings API 客户端。
     *
     * @return 当前门面持有的 OpenClawEmbeddingsClient；对应通道未启用时不可调用
     */
    public OpenClawEmbeddingsClient embeddings() {
        return embeddingsClient;
    }

    /**
     * 返回 Responses API 客户端。
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
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatClient.chatCompletion(request);
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code chatCompletion}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request) {
        return chatClient.chatCompletionAsync(request);
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param cancellation 可选调用取消令牌
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request, HttpCallCancellation cancellation) {
        return chatClient.chatCompletion(request, null, cancellation);
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headersBuilder 用于追加会话、通道和作用域请求头的构建器
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
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param agent 目标智能体的名称或标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param agent 目标智能体的名称或标识
     * @param model 模型标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param agent 目标智能体的名称或标识
     * @param model 模型标识
     * @param user 认证签名中的用户标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param agent 目标智能体的名称或标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(String agent, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param agent 目标智能体的名称或标识
     * @param model 模型标识
     * @param messages 按对话顺序排列的消息
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param agent 目标智能体的名称或标识
     * @param model 模型标识
     * @param user 认证签名中的用户标识
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
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatClient.chatCompletionStream(request);
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param callbackBuilder 用于注册流式增量、工具调用、完成和失败回调的构建器
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        return chatClient.chatCompletionStream(request, callbackBuilder);
    }

    /**
     * 携带会话键发起流式聊天，使后续请求可复用同一会话。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
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
     * 读取 Gateway 当前可用模型列表。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ModelsResponse
     */
    public ModelsResponse listModels() {
        return chatClient.listModels();
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code listModels}，调用线程不等待远端响应。
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
     * 通过已配置的 HTTP 客户端同步调用 Embeddings API。
     *
     * @param request 要校验、序列化并发送的 {@code EmbeddingsRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 EmbeddingsResponse
     */
    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddings(request);
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code createEmbeddings}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code EmbeddingsRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<EmbeddingsResponse> createEmbeddingsAsync(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddingsAsync(request);
    }

    // ----------------------------------------------------------------
    // Responses
    // ----------------------------------------------------------------

    /**
     * 通过已配置的 HTTP 客户端同步调用 Responses API。
     *
     * @param request 要校验、序列化并发送的 {@code ResponseRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ResponseResult
     */
    public ResponseResult createResponse(ResponseRequest request) {
        return responsesClient.createResponse(request);
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code createResponse}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ResponseRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ResponseResult> createResponseAsync(ResponseRequest request) {
        return responsesClient.createResponseAsync(request);
    }

    // ============================================================
    // Tools Invoke（/tools/invoke）
    // ============================================================

    /**
     * 通过工具调用客户端执行指定工具请求。
     *
     * @return 当前门面持有的 OpenClawToolInvokeClient；对应通道未启用时不可调用
     */
    public OpenClawToolInvokeClient toolsInvoke() {
        return toolsInvokeClient;
    }

    /**
     * 执行工具调用，并返回成功结果或结构化错误。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ToolInvokeResult
     */
    public ToolInvokeResult toolInvoke(ToolInvokeRequest request) {
        return toolsInvokeClient.invoke(request);
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code toolInvoke}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ToolInvokeResult> toolInvokeAsync(ToolInvokeRequest request) {
        return toolsInvokeClient.invokeAsync(request);
    }

    /**
     * 执行工具调用，并返回成功结果或结构化错误。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
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
     * 返回本地 CLI 门面；CLI 通道未启用时为空。
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
     * 关闭可空资源；关闭失败只记录警告，不中断其余子客户端的清理。
     *
     * @param resource 待关闭资源；可为空
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
