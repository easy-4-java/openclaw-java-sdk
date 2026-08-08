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
 * Facade:<b>HTTP Webhooks</b>({@code /hooks/*})+ <b>OpenAI API</b>({@code /v1/*})+ <b>Tools Invoke</b>({@code /tools/invoke})+ <b>WebSocket control plane</b> + CLI({@link #cli}).
 * <p>
 * channel,per their respective sub-config {@code enabled} determines whether to createCorresponds to:
 * </p>
 * <ul>
 * <li>{@link OpenClawHttpClientConfig#isEnabled} = false → HTTP / WS {@code null}</li>
 * <li>{@link OpenClawCliConfig#isEnabled} = false → CLI {@code null}</li>
 * </ul>
 *
 * <h3></h3>
 * <p>Provides multiple:</p>
 * <ul>
 * <li>only HTTP / only CLI:,system</li>
 * <li>HTTP + CLI:,systemaccording to their respective {@code enabled} </li>
 * <li>Composes: {@link OpenClawClientConfig},</li>
 * </ul>
 * <p>" ObjectMapper/OkHttpClient""inject".
 * injectversion {@code ObjectMapper}/{@code OkHttpClient} {@code requireNonNull} ,
 * for easyshared connection pool.</p>
 *
 * <h3></h3>
 * <p>Primary constructorsystem {@code startupCheckEnabled} {@code failFastOnUnavailable}
 * performs health probe(HTTP:{@code GET /v1/models};CLI:{@code openclaw --version}).
 * probe failed fail-fast only warning,does not interrupt construction; fail-fast {@link IllegalStateException}.</p>
 *
 * @see OpenClawGatewayWsClient
 * @see OpenClawChatClient
 * @see OpenClawEmbeddingsClient
 * @see OpenClawResponsesClient
 * @see OpenClawToolInvokeClient
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
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
 * only HTTP system(CLI ).auto-creates default {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig) {
        this(httpConfig, new OpenClawCliConfig(), new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
 * only HTTP system,uses caller-managed {@link OkHttpClient}.
 * <p>Applicable toinject Spring okhttp3-extension/starter .</p>
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OkHttpClient httpClient) {
        this(httpConfig, new ObjectMapper(), httpClient);
    }

    /**
 * only HTTP system(CLI ),inject {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(httpConfig, new OpenClawCliConfig(), objectMapper, httpClient, false);
    }

    /**
 * only CLI system(HTTP ).auto-creates default {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawCliConfig cliConfig) {
        this(new OpenClawHttpClientConfig(), cliConfig, new ObjectMapper(), new OkHttpClient(), true);
    }

    /**
 * only CLI system(HTTP ),inject {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawCliConfig cliConfig, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(new OpenClawHttpClientConfig(), cliConfig, objectMapper, httpClient, false);
    }

    /**
 * HTTP + CLI system.auto-creates default {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawHttpClientConfig httpConfig, OpenClawCliConfig cliConfig) {
        this(httpConfig, cliConfig, new ObjectMapper(), OpenClawOkHttpClientFactory.create(httpConfig), true);
    }

    /**
 * HTTP + CLI system,inject {@link ObjectMapper} {@link OkHttpClient}.
     * <p>
 * <b>Primary constructor</b>:All parameters {@code requireNonNull};HTTP/CLI according to their respective
 * {@code enabled} determines whether to create(when disabled, is {@code null});After construction completes
 * {@code startupCheckEnabled} {@code failFastOnUnavailable} Executes startup self-check.
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
 * Composes,auto-creates default {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawClientConfig config) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                new ObjectMapper(),
                OpenClawOkHttpClientFactory.create(config.getHttp()),
                true);
    }

    /**
 * Composes {@link OkHttpClient}.
 * <p>SDK ,connection pooldispatcher.</p>
     */
    public OpenClawClient(OpenClawClientConfig config, OkHttpClient httpClient) {
        this(config, new ObjectMapper(), httpClient);
    }

    /**
 * Composes,inject {@link ObjectMapper} {@link OkHttpClient}.
     */
    public OpenClawClient(OpenClawClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(Objects.requireNonNull(config, "config").getHttp(),
                config.getCli(),
                objectMapper,
                httpClient,
                false);
    }

    /**
 * inject(Used for).
 * <p><b></b>;HTTP/CLI fielddetermined by caller.</p>
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

 /** HTTP systemWhether to enable({@code enabled=true} HTTP null). */
    public boolean isHttpEnabled() {
        return chatClient != null;
    }

 /** CLI systemWhether to enable({@code enabled=true} CLI null). */
    public boolean isCliEnabled() {
        return cli != null;
    }

    // ============================================================
    // HTTP Webhook
    // ============================================================


    /**
 * injectsystemevent.
 * <p>Corresponds to {@code POST /hooks/wake}.</p>
     *
 * @param text event
 * @param mode wake( {@code "now"})
 * @return
     */
    public String wake(String text, String mode) {
        return gatewayHttpClient.postHooksWake(text, mode);
    }

    /**
 * map webhook.
 * <p>Corresponds to {@code POST /hooks/<name>}.</p>
     *
 * @param hookName webhook
 * @param payload
 * @return
     */
    public String hook(String hookName, Map<String, Object> payload) {
        return gatewayHttpClient.postMappedHook(hookName, payload);
    }

    /**
 * agent.
 * <p>Corresponds to {@code POST /hooks/agent}.</p>
     *
 * @param request request body
 * @return agent
     */
    public HookResponse hook(HookRequest request) {
        return gatewayHttpClient.postHooksAgent(request);
    }

    // ============================================================
    // WebSocket 控制面
    // ============================================================

    /**
 * WebSocket .
 * <p> WS :streaming({@code chat.send}),session,cron ,.</p>
     *
     * <pre>{@code
     * OpenClawGatewayWsClient ws = client.ws();
     * ws.addListener(new OpenClawWsListener() { ... });
     * ws.connectBlocking();
 * ws.chatSend(ChatSendParams.builder.message("").build, handler);
     * }</pre>
     *
 * @return WebSocket
     */
    public OpenClawGatewayWsClient ws() {
        return wsClient;
    }

    /**
 * connection WebSocket handshakecompletion.
     *
 * @return handshake
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
 * connection WebSocket.
     */
    public CompletableFuture<HelloOk> connectAsync() {
        return wsClient.connectHandshakeAsync();
    }

    /**
 * WebSocket streaming.
     *
 * @param message message
 * @param handler streaming
     */
    public void chatSend(String message, ChatStreamHandler handler) {
        wsClient.chatSend(ChatSendParams.builder().message(message).build(), handler);
    }

    /**
 * WebSocket streaming(session).
     *
 * @param sessionKey sessionkey
 * @param message message
 * @param handler streaming
     */
    public void chatSend(String sessionKey, String message, ChatStreamHandler handler) {
        wsClient.chatSend(
                ChatSendParams.builder().sessionKey(sessionKey).message(message).build(),
                handler);
    }

    /**
 * WebSocket sessionmessage(non-streaming RPC).
     */
    public SessionsSendResult sessionsSend(String sessionKey, String message) {
        return wsClient.sessionsSend(
                SessionsSendParams.builder().key(sessionKey).message(message).build());
    }

    /**
 * WebSocket event listener.
     */
    public OpenClawClient addWsListener(OpenClawWsListener listener) {
        wsClient.addListener(listener);
        return this;
    }

    // ============================================================
    // OpenAI 兼容 HTTP API（/v1/*）
    // ============================================================

    /**
 * Chat Completions .
     */
    public OpenClawChatClient chat() {
        return chatClient;
    }

    /**
 * HTTP system {@link OkHttpClient}.
 * <p>inject,lifecyclemanaged by caller.</p>
     *
 * @return HTTP system OkHttpClient;HTTP system {@code null}
     */
    public OkHttpClient getOkHttpClient() {
        return Objects.nonNull(chatClient) ? chatClient.getHttpClient() : null;
    }

    /**
 * Embeddings .
     */
    public OpenClawEmbeddingsClient embeddings() {
        return embeddingsClient;
    }

    /**
 * Responses .
     */
    public OpenClawResponsesClient responses() {
        return responsesClient;
    }

    // ----------------------------------------------------------------
    // Chat Completions 快捷方法
    // ----------------------------------------------------------------

    /**
 * Chat Completions (non-streaming).
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatClient.chatCompletion(request);
    }

    /** 发送支持调用方取消的 Chat Completions 请求。 */
    public ChatResponse chatCompletion(ChatRequest request, HttpCallCancellation cancellation) {
        return chatClient.chatCompletion(request, null, cancellation);
    }

    /**
 * Chat Completions ,request header.
     */
    public ChatResponse chatCompletion(ChatRequest request, OpenClawHeaders.Builder headersBuilder) {
        Map<String, String> headers = headersBuilder != null ? headersBuilder.build() : null;
        return chatClient.chatCompletion(request, headers);
    }

    // ----------------------------------------------------------------
    // Convenience methods（便捷方法）
    // ----------------------------------------------------------------

    /**
 * chat completion .
     * <p>
 * example:
     * <pre>{@code
     * client.chatCompletion("openclaw/default", List.of(ChatMessage.ofUser("Hello")));
     * }</pre>
     * </p>
     *
 * @param agent Agent ( {@code "openclaw/default"})
 * @param messages message
 * @return Chat Completions
     */
    public ChatResponse chatCompletion(String agent, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
 * chat completion ( Agent ).
     * <p>
 * example:
     * <pre>{@code
     * client.chatCompletion("openclaw/default", "gpt-4o", List.of(ChatMessage.ofUser("Hello")));
     * }</pre>
     * </p>
     *
 * @param agent Agent ( {@code "openclaw/default"})
 * @param model LLM ( {@code "gpt-4o"})
 * @param messages message
 * @return Chat Completions
     */
    public ChatResponse chatCompletion(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
 * chat completion ( Agent).
     *
 * @param agent Agent
 * @param model LLM
 * @param user (Used for session)
 * @param messages message
 * @return Chat Completions
     */
    public ChatResponse chatCompletion(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletion(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    /**
 * streaming chat completion ( Agent).
     *
 * @param agent Agent
 * @param messages message
 * @return streaming
     */
    public StreamingChatResponse chatCompletionStream(String agent, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).messages(messages).build());
    }

    /**
 * streaming chat completion ( Agent ).
     *
 * @param agent Agent
 * @param model LLM
 * @param messages message
 * @return streaming
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).messages(messages).build());
    }

    /**
 * streaming chat completion ( Agent).
     *
 * @param agent Agent
 * @param model LLM
 * @param user (Used for session)
 * @param messages message
 * @return streaming
     */
    public StreamingChatResponse chatCompletionStream(String agent, String model, String user, List<ChatMessage> messages) {
        return this.chatCompletionStream(ChatRequest.builder().agent(agent).model(model).user(user).messages(messages).build());
    }

    // ----------------------------------------------------------------
    // Streaming（对齐 Hermes chatCompletionStream）
    // ----------------------------------------------------------------

    /**
 * streaming chat completion, {@link StreamingChatResponse}.
     *
     * <pre>{@code
     * client.chatCompletionStream(request)
     *     .onDelta(delta -> System.out.print(delta))
 * .onComplete(text -> System.out.println("\ncompletion"))
     *     .onError(error -> error.printStackTrace());
     * }</pre>
     *
 * @param request request body( stream=true)
 * @return streaming,
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatClient.chatCompletionStream(request);
    }

    /**
 * streaming chat completion, Builder .
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        return chatClient.chatCompletionStream(request, callbackBuilder);
    }

    /**
 * streaming chat completion, sessionKey .
     */
    public StreamingChatResponse chatCompletionStreamWithSession(ChatRequest request, String sessionKey) {
        Map<String, String> headers = OpenClawHeaders.builder().sessionKey(sessionKey).build();
        return chatClient.chatCompletionStream(request, headers);
    }

    // ----------------------------------------------------------------
    // Models
    // ----------------------------------------------------------------

    /**
 * /agent .
     */
    public ModelsResponse listModels() {
        return chatClient.listModels();
    }

    // ----------------------------------------------------------------
    // Embeddings
    // ----------------------------------------------------------------

    /**
 * embedding vector.
     */
    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        return embeddingsClient.createEmbeddings(request);
    }

    // ----------------------------------------------------------------
    // Responses
    // ----------------------------------------------------------------

    /**
 * OpenResponses .
     */
    public ResponseResult createResponse(ResponseRequest request) {
        return responsesClient.createResponse(request);
    }

    // ============================================================
    // Tools Invoke（/tools/invoke）
    // ============================================================

    /**
 * Tools Invoke .
     */
    public OpenClawToolInvokeClient toolsInvoke() {
        return toolsInvokeClient;
    }

    /**
 * .
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
 * top-level CLI command facade.
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
