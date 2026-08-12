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
     * 负责 SSE 连接、解析和取消传播的客户端。
     */
    private final OpenClawSseClient sseClient;
    /**
     * 标记 SSE 客户端是否由当前聊天客户端创建；仅自建实例会随当前对象关闭。
     */
    private final boolean ownsSseClient;

    /**
     * 使用默认映射器创建 Chat Completions 客户端，并复用调用方提供的 OkHttp 连接资源。
     *
     * @param config SDK 配置
     */
    public OpenClawChatClient(OpenClawHttpClientConfig config) {
        super(config);
        this.sseClient = new OpenClawSseClient(config, objectMapper, httpClient);
        this.ownsSseClient = true;
    }

    /**
     * 创建 Chat Completions 客户端，并复用调用方提供的映射器和 OkHttp 连接资源。
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
     * 创建 Chat Completions 客户端，并使用指定 SSE 执行器读取流式响应。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     * @param sseClient 负责建立和管理 SSE 流的客户端
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
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 包含回复消息、结束原因和 Token 用量的完整响应
     */
    public ChatResponse chatCompletion(ChatRequest request) {
        return chatCompletion(request, (Map<String, String>) null);
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headers 附加 HTTP 请求头
     * @return 包含回复消息、结束原因和 Token 用量的完整响应
     */
    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers) {
        return chatCompletion(request, headers, null);
    }

    /**
     * 向 Chat Completions 端点发送非流式请求，并解析完整响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 包含回复消息、结束原因和 Token 用量的完整响应
     */
    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers,
                                       HttpCallCancellation cancellation) {
        return awaitFuture(chatCompletionAsync(request, headers, cancellation));
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code chatCompletion}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
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
        debug("HEADERS", "Headers to send: {}", headers);

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
     * 通过 OkHttp Dispatcher 异步执行 {@code chatCompletion}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request) {
        return chatCompletionAsync(request, null, null);
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @return 可注册增量回调和取消底层调用的流式结果句柄
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatCompletionStream(request, (Map<String, String>) null);
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headers 附加 HTTP 请求头
     * @return 已应用回调构建器配置的可取消流式结果句柄
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, Map<String, String> headers) {
        return chatCompletionStream(request, headers, StreamingChatResponse.builder());
    }

    /**
     * 向 Chat Completions 端点发送流式请求，并返回可取消的聚合句柄。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param callbackBuilder 用于注册流式增量、工具调用、完成和失败回调的构建器
     * @return 携带自定义请求头的可取消流式结果句柄
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        return chatCompletionStream(request, null, callbackBuilder);
    }

    /**
     * 携带自定义请求头发起流式请求，并在订阅启动前完成回调绑定。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headers 附加 HTTP 请求头
     * @param callbackBuilder 用于注册流式增量、工具调用、完成和失败回调的构建器
     * @return 已在订阅启动前绑定请求头和回调的可取消流式结果句柄
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request, Map<String, String> headers,
                                                       StreamingChatResponse.Builder callbackBuilder) {
        Objects.requireNonNull(callbackBuilder, "callbackBuilder");
        StreamingChatResponse response = callbackBuilder.build();
        subscribeStream(request, headers, response);
        return response;
    }

    // ============================================================
    // Models
    // ============================================================

    /**
     * 读取 Gateway 当前可用模型列表。
     *
     * @return Gateway 当前公开的模型资源列表
     */
    public ModelsResponse listModels() {
        return awaitFuture(listModelsAsync());
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code listModels}，调用线程不等待远端响应。
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
     * 返回模型标识。
     *
     * @param modelId 模型标识
     * @return 模型标识匹配的数据；未找到时返回 null
     */
    public ModelsResponse.ModelData getModel(String modelId) {
        return awaitFuture(getModelAsync(modelId));
    }

    /**
     * 异步读取模型列表；网络 I/O 由 OkHttp Dispatcher 执行。
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
