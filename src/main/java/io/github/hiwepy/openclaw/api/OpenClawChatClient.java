package io.github.hiwepy.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hiwepy.openclaw.OpenClawHttpClientConfig;
import io.github.hiwepy.openclaw.exception.OpenClawHttpException;
import io.github.hiwepy.openclaw.util.OpenClawStrings;
import io.github.hiwepy.openclaw.api.model.*;
import io.github.hiwepy.openclaw.api.sse.SseStreamReader;
import io.github.hiwepy.openclaw.api.sse.StreamingChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Chat Completions API 客户端。
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
 */
@Slf4j
public class OpenClawChatClient extends OpenClawHttpClient {

    public OpenClawChatClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    // ============================================================
    // Chat Completions
    // ============================================================

    public ChatResponse chatCompletion(ChatRequest request) {
        return chatCompletion(request, (Map<String, String>) null);
    }

    public ChatResponse chatCompletion(ChatRequest request, Map<String, String> headers) {
        Objects.requireNonNull(request, "request");

        headers = buildHeaders(request, headers);
        String bodyModel = resolveModel(request);

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
                .build();

        String json = postJson(OpenClawConstants.ENDPOINT_CHAT_COMPLETIONS, normalized, headers);
        return parse(json, ChatResponse.class, "chat completion");
    }

    /**
     * 流式 chat completion。
     *
     * <pre>{@code
     * client.chatCompletionStream(request)
     *     .onDelta(delta -> System.out.print(delta))
     *     .onComplete(text -> System.out.println("\\n完成"))
     *     .onError(error -> error.printStackTrace());
     * }</pre>
     */
    public StreamingChatResponse chatCompletionStream(ChatRequest request) {
        return chatCompletionStream(request, (Map<String, String>) null);
    }

    public StreamingChatResponse chatCompletionStream(ChatRequest request, Map<String, String> headers) {
        StreamingChatResponse response = new StreamingChatResponse();
        Response httpResponse = chatCompletionStreamRaw(request, headers);
        startStreamConsumer(httpResponse, response);
        return response;
    }

    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        StreamingChatResponse response = callbackBuilder.build();
        Response httpResponse = chatCompletionStreamRaw(request, null);
        startStreamConsumer(httpResponse, response);
        return response;
    }

    /**
     * 获取流式响应的原始 OkHttp Response（高级用法）。
     */
    public Response chatCompletionStreamRaw(ChatRequest request) {
        return chatCompletionStreamRaw(request, (Map<String, String>) null);
    }

    public Response chatCompletionStreamRaw(ChatRequest request, Map<String, String> headers) {
        Objects.requireNonNull(request, "request");
        request.setStream(true);

        headers = buildHeaders(request, headers);
        String bodyModel = resolveModel(request);

        ChatRequest normalized = ChatRequest.builder()
                .model(bodyModel)
                .messages(request.getMessages())
                .stream(true)
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
                .build();

        Request.Builder builder = authedBuilder(resolveUrl(OpenClawConstants.ENDPOINT_CHAT_COMPLETIONS), headers)
                .header("Accept", "text/event-stream");

        try {
            Request req = builder.post(RequestBody.create(objectMapper.writeValueAsString(normalized), JSON)).build();
            Response response = httpClient.newCall(req).execute();
            if (!response.isSuccessful()) {
                String body = response.body() != null ? response.body().string() : "";
                response.close();
                throw new OpenClawHttpException("Stream returned status " + response.code(), response.code(), body);
            }
            return response;
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenClawHttpException("Stream request failed: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // Models
    // ============================================================

    public ModelsResponse listModels() {
        String json = getJson(OpenClawConstants.ENDPOINT_MODELS);
        return parse(json, ModelsResponse.class, "models");
    }

    public ModelsResponse.ModelData getModel(String modelId) {
        Objects.requireNonNull(modelId, "modelId");
        String encodedId;
        try {
            encodedId = URLEncoder.encode(modelId, StandardCharsets.UTF_8.name()).replace("+", "%20");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String json = getJson(OpenClawConstants.ENDPOINT_MODELS + "/" + encodedId);
        return parse(json, ModelsResponse.ModelData.class, "model");
    }

    // ============================================================
    // Private helpers
    // ============================================================

    private Map<String, String> buildHeaders(ChatRequest request, Map<String, String> existingHeaders) {
        Map<String, String> headers = existingHeaders != null ? new HashMap<>(existingHeaders) : new HashMap<>();
        if (request.getModel() != null && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }
        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(ChatRequest request) {
        return request.getAgent() != null ? request.getAgent() : request.getModel();
    }

    private void startStreamConsumer(Response httpResponse, StreamingChatResponse response) {
        SseStreamReader reader = new SseStreamReader(objectMapper);
        CompletableFuture.runAsync(() -> {
            try {
                reader.readChatCompletionStream(Objects.requireNonNull(httpResponse.body(), "Response body is null").byteStream(), response);
            } finally {
                httpResponse.close();
            }
        });
    }
}
