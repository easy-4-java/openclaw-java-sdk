package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.*;
import io.github.easy4j.openclaw.api.sse.SseChunkDecoder;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Chat Completions API client.
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Slf4j
public class OpenClawChatClient extends OpenClawHttpClient {

    private final ExecutorService streamExecutor;
    private final Set<CompletableFuture<Void>> activeStreams = ConcurrentHashMap.newKeySet();

    public OpenClawChatClient(OpenClawHttpClientConfig config) {
        super(config);
        this.streamExecutor = createStreamExecutor(config);
    }

    public OpenClawChatClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
        this.streamExecutor = createStreamExecutor(config);
    }

    private static ExecutorService createStreamExecutor(OpenClawHttpClientConfig config) {
        int corePoolSize = Math.max(1, config.getStreamCorePoolSize());
        int maxPoolSize = Math.max(corePoolSize, config.getStreamMaxPoolSize());
        int queueCapacity = Math.max(1, config.getStreamQueueCapacity());
        long keepAliveMillis = Math.max(1L, config.getStreamKeepAliveMillis());
        AtomicInteger threadIndex = new AtomicInteger();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveMillis, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity), runnable -> {
                    Thread thread = new Thread(runnable,
                            "openclaw-sse-consumer-" + threadIndex.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());
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
     * <p>网络等待由 Netty event-loop 承担，适用于 300–500 并发调用。</p>
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
        enqueueStream(request, headers, response);
        return response;
    }

    public StreamingChatResponse chatCompletionStream(ChatRequest request, StreamingChatResponse.Builder callbackBuilder) {
        StreamingChatResponse response = callbackBuilder.build();
        enqueueStream(request, null, response);
        return response;
    }

    /**
 * streaming OkHttp Response(usage).
     */
    public Response chatCompletionStreamRaw(ChatRequest request) {
        return chatCompletionStreamRaw(request, null);
    }

    public Response chatCompletionStreamRaw(ChatRequest request, Map<String, String> headers) {
        return awaitFuture(chatCompletionStreamRawAsync(request, headers));
    }

    /**
     * 异步返回原始流式响应。调用方负责关闭 Response。
     *
     * @deprecated 优先使用 {@link #chatCompletionStream(ChatRequest)}，避免调用方阻塞读取响应体。
     */
    @Deprecated
    public CompletableFuture<Response> chatCompletionStreamRawAsync(ChatRequest request, Map<String, String> headers) {
        Objects.requireNonNull(request, "request");
        CompletableFuture<Response> result = new CompletableFuture<>();
        try {
            Request req = buildStreamRequest(request, headers);
            httpClient.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException error) {
                    result.completeExceptionally(new OpenClawHttpException(
                            "Stream request failed: " + error.getMessage(), error));
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        result.complete(response);
                        return;
                    }
                    try (Response failed = response) {
                        String body = failed.body() != null ? failed.body().string() : "";
                        result.completeExceptionally(new OpenClawHttpException(
                                "Stream returned status " + failed.code(), failed.code(), body));
                    } catch (Exception error) {
                        result.completeExceptionally(error);
                    }
                }
            });
        } catch (Exception e) {
            result.completeExceptionally(new OpenClawHttpException("Stream request failed: " + e.getMessage(), e));
        }
        return result;
    }

    // ============================================================
    // Models
    // ============================================================

    public ModelsResponse listModels() {
        debug("=== List Models ===");
        String json = getJson(OpenClawConstants.ENDPOINT_MODELS);
        ModelsResponse response = parse(json, ModelsResponse.class, "models");
        debug("Models count: {}", response.getData() != null ? response.getData().size() : 0);
        return response;
    }

    public ModelsResponse.ModelData getModel(String modelId) {
        Objects.requireNonNull(modelId, "modelId");
        debug("=== Get Model: {} ===", modelId);

        String encodedId;
        try {
            encodedId = URLEncoder.encode(modelId, "UTF-8").replace("+", "%20");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String json = getJson(OpenClawConstants.ENDPOINT_MODELS + "/" + encodedId);
        return parse(json, ModelsResponse.ModelData.class, "model");
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

    private void enqueueStream(ChatRequest request, Map<String, String> headers, StreamingChatResponse streamResponse) {
        final Request httpRequest;
        try {
            httpRequest = buildStreamRequest(request, headers);
        } catch (Exception e) {
            streamResponse.onError(new OpenClawHttpException("Stream request build failed: " + e.getMessage(), e));
            return;
        }
        if (config.isLegacyInjectedOkHttpTransportEnabled()) {
            enqueueLegacyStream(httpRequest, streamResponse);
            return;
        }
        SseChunkDecoder decoder = new SseChunkDecoder(objectMapper, streamResponse);
        SerialExecutor serialExecutor = new SerialExecutor(streamExecutor);
        CompletableFuture<Void> stream = asyncTransport.executeStream(httpRequest, null,
                bytes -> serialExecutor.execute(() -> decoder.accept(bytes)));
        activeStreams.add(stream);
        stream.whenComplete((ignored, error) -> serialExecutor.execute(() -> {
            activeStreams.remove(stream);
            if (Objects.nonNull(error)) {
                decoder.fail(error);
            } else {
                decoder.complete();
            }
        }));
    }

    private void enqueueLegacyStream(Request request, StreamingChatResponse streamResponse) {
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call ignored, java.io.IOException error) {
                streamResponse.onError(error);
            }

            @Override
            public void onResponse(Call ignored, Response response) {
                try {
                    streamExecutor.execute(() -> {
                        try (Response completed = response) {
                            if (!completed.isSuccessful()) {
                                String body = Objects.nonNull(completed.body()) ? completed.body().string() : "";
                                streamResponse.onError(new OpenClawHttpException(
                                        "Stream returned status " + completed.code(), completed.code(), body));
                                return;
                            }
                            if (Objects.isNull(completed.body())) {
                                streamResponse.onError(new OpenClawHttpException("SSE response body is null", null));
                                return;
                            }
                            new io.github.easy4j.openclaw.api.sse.SseStreamReader(objectMapper)
                                    .readChatCompletionStream(completed.body().byteStream(), streamResponse);
                        } catch (Exception error) {
                            streamResponse.onError(error);
                        }
                    });
                } catch (RejectedExecutionException error) {
                    response.close();
                    streamResponse.onError(error);
                }
            }
        });
    }

    private Request buildStreamRequest(ChatRequest request, Map<String, String> headers) throws Exception {
        Objects.requireNonNull(request, "request");
        validateRequest(request);
        Map<String, String> resolvedHeaders = buildHeaders(request, headers);
        ChatRequest normalized = ChatRequest.builder()
                .model(resolveModel(request))
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
                .responseFormat(request.getResponseFormat())
                .build();
        return authedBuilder(resolveUrl(OpenClawConstants.ENDPOINT_CHAT_COMPLETIONS), resolvedHeaders)
                .header("Accept", "text/event-stream")
                .post(RequestBody.create(objectMapper.writeValueAsString(normalized), JSON))
                .build();
    }

    @Override
    public void close() {
        for (CompletableFuture<Void> stream : activeStreams) {
            stream.cancel(true);
        }
        activeStreams.clear();
        streamExecutor.shutdownNow();
        super.close();
    }

    /** 在共享线程池上保持单条 SSE 的事件顺序，不长期占用线程。 */
    private static final class SerialExecutor implements java.util.concurrent.Executor {
        private final Queue<Runnable> tasks = new ArrayDeque<>();
        private final ExecutorService executor;
        private Runnable active;

        private SerialExecutor(ExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public synchronized void execute(Runnable command) {
            tasks.offer(() -> {
                try {
                    command.run();
                } finally {
                    scheduleNext();
                }
            });
            if (Objects.isNull(active)) {
                scheduleNext();
            }
        }

        private synchronized void scheduleNext() {
            active = tasks.poll();
            if (Objects.nonNull(active)) {
                try {
                    executor.execute(active);
                } catch (RejectedExecutionException error) {
                    active = null;
                    tasks.clear();
                    throw error;
                }
            }
        }
    }
}
