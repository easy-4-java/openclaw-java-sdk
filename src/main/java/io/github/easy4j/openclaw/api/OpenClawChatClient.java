package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.*;
import io.github.easy4j.openclaw.api.sse.SseSubscription;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Chat Completions API 客户端，提供同步桥接、CompletableFuture 异步调用以及 SSE 流式对话入口。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawChatClient extends OpenClawHttpClient {

    /**
     * `OpenClawChatClient` 生命周期内保存的 `sseClient` 对应状态。
     */
    private final OpenClawSseClient sseClient;
    /**
     * `OpenClawChatClient` 生命周期内保存的 `ownsSseClient` 对应状态。
     */
    private final boolean ownsSseClient;

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawChatClient(OpenClawHttpClientConfig config) {
        super(config);
        this.sseClient = new OpenClawSseClient(config, objectMapper, httpClient);
        this.ownsSseClient = true;
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
        this.sseClient = new OpenClawSseClient(config, this.objectMapper, this.httpClient);
        this.ownsSseClient = true;
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     * @param sseClient 写入 `sseClient` 协议字段的内容
     */
    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper,
                              OkHttpClient httpClient, OpenClawSseClient sseClient) {
        super(config, objectMapper, httpClient);
        this.sseClient = Objects.requireNonNull(sseClient, "sseClient");
        this.ownsSseClient = false;
    }

    // ============================================================
    // Chat Completions
    // ============================================================

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatCompletion(request, (Map<String, String>) null);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param headers 附加 HTTP 请求头
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers) {
        return chatCompletion(request, headers, null);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletion` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ChatResponse
     */
    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers,
                                       HttpCallCancellation cancellation) {
        return awaitFuture(chatCompletionAsync(request, headers, cancellation));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `chatCompletion`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request, Map<String, String> headers,
                                                               HttpCallCancellation cancellation) {
        Objects.requireNonNull(request, "request");

        debug("=== Chat Completion Request ===");
        debug("agent: {}", request.getAgent());
        debug("model: {}", request.getModel());
        debug("messages count: {}", request.getMessages() != null ? request.getMessages().size() : 0);
        debug("stream: {}", request.getStream());
        debug("tools: {}", request.getTools() != null ? request.getTools().size() : 0);

        // 验证请求
        validateRequest(request);

        headers = buildHeaders(request, headers);
        String bodyModel = resolveModel(request);

        debug("Resolved body model (agent routing): {}", bodyModel);
        debug("Headers to send: {}", headers);

        ChatRequest normalized = ChatRequest.builder()
                .model(bodyModel)
                .messages(request.getMessages())
                .stream(request.getStream())
                .streamOptions(request.getStreamOptions())
                .tools(request.getTools())
                .toolChoice(request.getToolChoice())
                .user(request.getUser())
                .maxCompletionTokens(request.getMaxCompletionTokens())
                .maxTokens(request.getMaxTokens())
                .temperature(request.getTemperature())
                .topP(request.getTopP())
                .frequencyPenalty(request.getFrequencyPenalty())
                .presencePenalty(request.getPresencePenalty())
                .seed(request.getSeed())
                .stop(request.getStop())
                .responseFormat(request.getResponseFormat())
                .build();

        return postJsonAsync(OpenClawConstants.ENDPOINT_CHAT_COMPLETIONS, normalized, headers, cancellation)
                .thenApply(json -> {
                    debug("Response received, parsing...");
                    ChatResponse response = parse(json, ChatResponse.class, "chat completion");
                    debug("Chat completion success: id={}", response.getId());
                    return response;
                });
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `chatCompletion`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request) {
        return chatCompletionAsync(request, null, null);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatCompletionStream(request, (Map<String, String>) null);
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param headers 附加 HTTP 请求头
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, Map<String, String> headers) {
        StreamingChatResponse response = new StreamingChatResponse();
        subscribeStream(request, headers, response);
        return response;
    }

    /**
     * 调用 OpenClaw 的 `chatCompletionStream` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param callbackBuilder 写入 `callbackBuilder` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        StreamingChatResponse response = callbackBuilder.build();
        subscribeStream(request, null, response);
        return response;
    }

    // ============================================================
    // Models
    // ============================================================

    /**
     * 调用 OpenClaw 的 `listModels` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ModelsResponse
     */
    public ModelsResponse listModels() {
        return awaitFuture(listModelsAsync());
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `listModels`，调用线程不会等待远程响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ModelsResponse> listModelsAsync() {
        debug("=== List Models ===");
        return getJsonAsync(OpenClawConstants.ENDPOINT_MODELS).thenApply(json -> {
            ModelsResponse response = parse(json, ModelsResponse.class, "models");
            debug("Models count: {}", response.getData() != null ? response.getData().size() : 0);
            return response;
        });
    }

    /**
     * 读取当前对象保存的 模型标识，不触发网络或子进程调用。
     *
     * @param modelId 模型标识
     * @return 按当前参数创建、查询或解析得到的 ModelsResponse.ModelData
     */
    public ModelsResponse.ModelData getModel(String modelId) {
        return awaitFuture(getModelAsync(modelId));
    }

    /**
     * 读取当前对象保存的 `modelAsync` 对应状态，不触发网络或子进程调用。
     *
     * @param modelId 模型标识
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     * @throws RuntimeException 远程响应、协议解析或本地执行失败时抛出
     */
    public CompletableFuture<ModelsResponse.ModelData> getModelAsync(String modelId) {
        Objects.requireNonNull(modelId, "modelId");
        debug("=== Get Model: {} ===", modelId);

        String encodedId;
        try {
            encodedId = URLEncoder.encode(modelId, "UTF-8").replace("+", "%20");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return getJsonAsync(OpenClawConstants.ENDPOINT_MODELS + "/" + encodedId)
                .thenApply(json -> parse(json, ModelsResponse.ModelData.class, "model"));
    }

    // ============================================================
    // Private helpers
    // ============================================================

    private void validateRequest(ChatRequest request) {
        // 检查 agent 或 model 必须有一个
        if (OpenClawStrings.isBlank(request.getAgent()) && OpenClawStrings.isBlank(request.getModel())) {
            String msg = "Chat request requires either 'agent' or 'model' field. " +
                    "Use 'agent' for OpenClaw routing (e.g., 'openclaw/default'), " +
                    "or 'model' for direct backend model (e.g., 'gpt-4o').";
            warn(msg);
            throw new IllegalArgumentException(msg);
        }

        // 检查 messages
        if (request.getMessages() == null || request.getMessages().isEmpty()) {
            warn("Chat request has no messages");
            throw new IllegalArgumentException("Chat request requires at least one message");
        }
    }

    private Map<String, String> buildHeaders(ChatRequest request, Map<String, String> existingHeaders) {
        Map<String, String> headers = existingHeaders != null ? new HashMap<>(existingHeaders) : new HashMap<>();

        // 如果有独立的 model 字段（非 agent 路由），设置 x-openclaw-model header
        if (request.getModel() != null && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
            debug("Added {} header: {}", OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }

        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(ChatRequest request) {
        // 优先使用 agent 字段（用于 OpenClaw 路由）
        if (request.getAgent() != null) {
            debug("Using agent as model: {}", request.getAgent());
            return request.getAgent();
        }
        // 否则使用 model 字段（用于直接调用后端模型）
        debug("Using model: {}", request.getModel());
        return request.getModel();
    }

    private void subscribeStream(ChatRequest request, Map<String, String> headers,
                                 StreamingChatResponse response) {
        SseSubscription subscription = sseClient.subscribeChat(request, headers, response);
        response.onCancel(subscription::cancel);
        response.whenComplete((value, error) -> subscription.close());
    }

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
    @Override
    public void close() {
        if (ownsSseClient) {
            sseClient.close();
        }
        super.close();
    }

}
