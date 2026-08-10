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
 * Chat Completions API client.
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Slf4j
public class OpenClawChatClient extends OpenClawHttpClient {

    private final OpenClawSseClient sseClient;
    private final boolean ownsSseClient;

    public OpenClawChatClient(OpenClawHttpClientConfig config) {
        super(config);
        this.sseClient = new OpenClawSseClient(config, objectMapper, httpClient);
        this.ownsSseClient = true;
    }

    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
        this.sseClient = new OpenClawSseClient(config, this.objectMapper, this.httpClient);
        this.ownsSseClient = true;
    }

    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper,
                              OkHttpClient httpClient, OpenClawSseClient sseClient) {
        super(config, objectMapper, httpClient);
        this.sseClient = Objects.requireNonNull(sseClient, "sseClient");
        this.ownsSseClient = false;
    }

    // ============================================================
    // Chat Completions
    // ============================================================

    public ChatResponse chatCompletion(ChatRequest request) {
        return chatCompletion(request, (Map<String, String>) null);
    }

    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers) {
        return chatCompletion(request, headers, null);
    }

    /** 发送支持调用方取消的 Chat Completion。 */
    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers,
                                       HttpCallCancellation cancellation) {
        return awaitFuture(chatCompletionAsync(request, headers, cancellation));
    }

    /**
     * 异步发送 Chat Completion 请求。
     *
     * <p>网络等待由 OkHttp Dispatcher 异步调度，不阻塞调用方线程。</p>
     *
     * @param request 对话请求
     * @param headers 附加请求头
     * @param cancellation 取消信号
     * @return 异步对话响应
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

    /** 异步发送 Chat Completion 请求。 */
    public CompletableFuture<ChatResponse> chatCompletionAsync(ChatRequest request) {
        return chatCompletionAsync(request, null, null);
    }

    /**
 * streaming chat completion.
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatCompletionStream(request, (Map<String, String>) null);
    }

    public StreamingChatResponse chatCompletionStream(ChatRequest request, Map<String, String> headers) {
        StreamingChatResponse response = new StreamingChatResponse();
        subscribeStream(request, headers, response);
        return response;
    }

    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        StreamingChatResponse response = callbackBuilder.build();
        subscribeStream(request, null, response);
        return response;
    }

    // ============================================================
    // Models
    // ============================================================

    public ModelsResponse listModels() {
        return awaitFuture(listModelsAsync());
    }

    /** 异步列出模型。 */
    public CompletableFuture<ModelsResponse> listModelsAsync() {
        debug("=== List Models ===");
        return getJsonAsync(OpenClawConstants.ENDPOINT_MODELS).thenApply(json -> {
            ModelsResponse response = parse(json, ModelsResponse.class, "models");
            debug("Models count: {}", response.getData() != null ? response.getData().size() : 0);
            return response;
        });
    }

    public ModelsResponse.ModelData getModel(String modelId) {
        return awaitFuture(getModelAsync(modelId));
    }

    /** 异步获取指定模型。 */
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

    @Override
    public void close() {
        if (ownsSseClient) {
            sseClient.close();
        }
        super.close();
    }

}
