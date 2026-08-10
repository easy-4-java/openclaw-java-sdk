package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.sse.SseEventHandler;
import io.github.easy4j.openclaw.api.sse.SseStreamReader;
import io.github.easy4j.openclaw.api.sse.SseSubscription;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * OpenClaw SSE 客户端，负责流式连接、解析、取消和资源回收。
 *
 * <p>网络请求使用 OkHttp 异步回调；响应体解析使用共享有界线程池，禁止为每个订阅
 * 创建独占消费线程。</p>
 */
@Slf4j
public class OpenClawSseClient extends OpenClawHttpClient {

    private final ExecutorService streamExecutor;
    private final Set<SseSubscription> activeSubscriptions = ConcurrentHashMap.newKeySet();

    /** 使用 SDK 自建 OkHttpClient 创建 SSE 客户端。 */
    public OpenClawSseClient(OpenClawHttpClientConfig config) {
        super(config);
        this.streamExecutor = createStreamExecutor(config);
        logInitialization(config);
    }

    /** 使用调用方提供的共享组件创建 SSE 客户端。 */
    public OpenClawSseClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper,
                             OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
        this.streamExecutor = createStreamExecutor(config);
        logInitialization(config);
    }

    /**
     * 订阅 Chat Completion SSE。
     *
     * @param request Chat 请求
     * @param handler SSE 事件处理器
     * @return 可取消订阅句柄
     */
    public SseSubscription subscribeChat(ChatRequest request, SseEventHandler handler) {
        return subscribeChat(request, null, handler);
    }

    /**
     * 订阅带自定义请求头的 Chat Completion SSE。
     *
     * @param request Chat 请求
     * @param headers 附加请求头
     * @param handler SSE 事件处理器
     * @return 可取消订阅句柄
     */
    public SseSubscription subscribeChat(ChatRequest request, Map<String, String> headers,
                                         SseEventHandler handler) {
        Objects.requireNonNull(handler, "handler");
        final Request httpRequest;
        try {
            httpRequest = buildChatRequest(request, headers);
        } catch (Exception error) {
            throw new OpenClawHttpException("Stream request build failed: " + error.getMessage(), error);
        }

        Call call = httpClient.newCall(httpRequest);
        AtomicReference<SseSubscription> subscriptionRef = new AtomicReference<>();
        SseSubscription subscription = new SseSubscription(() -> {
            call.cancel();
            SseSubscription current = subscriptionRef.get();
            if (Objects.nonNull(current)) {
                activeSubscriptions.remove(current);
            }
        });
        subscriptionRef.set(subscription);
        activeSubscriptions.add(subscription);
        enqueue(call, subscription, handler);
        return subscription;
    }

    /**
     * 返回当前活动订阅数量。
     *
     * @return 活动订阅数
     */
    public int activeSubscriptionCount() {
        return activeSubscriptions.size();
    }

    private void enqueue(Call call, SseSubscription subscription, SseEventHandler handler) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call ignored, IOException error) {
                if (subscription.isActive()) {
                    handler.onError(error);
                }
                subscription.close();
            }

            @Override
            public void onResponse(Call ignored, Response response) {
                if (!subscription.isActive()) {
                    response.close();
                    return;
                }
                try {
                    streamExecutor.execute(() -> consume(response, subscription, handler));
                } catch (RejectedExecutionException error) {
                    response.close();
                    if (subscription.isActive()) {
                        handler.onError(new OpenClawHttpException(
                                "OpenClaw SSE executor is full", error));
                    }
                    subscription.close();
                }
            }
        });
    }

    private void consume(Response response, SseSubscription subscription, SseEventHandler handler) {
        try (Response completed = response) {
            if (!subscription.isActive()) {
                return;
            }
            if (!completed.isSuccessful()) {
                String body = Objects.nonNull(completed.body()) ? completed.body().string() : "";
                handler.onError(new OpenClawHttpException(
                        "Stream returned status " + completed.code(), completed.code(), body));
                return;
            }
            if (Objects.isNull(completed.body())) {
                handler.onError(new OpenClawHttpException("SSE response body is null", null));
                return;
            }
            new SseStreamReader(objectMapper)
                    .readChatCompletionStream(completed.body().byteStream(), handler);
        } catch (Exception error) {
            if (subscription.isActive()) {
                handler.onError(error);
            }
        } finally {
            subscription.close();
        }
    }

    private Request buildChatRequest(ChatRequest request, Map<String, String> headers) throws IOException {
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

    private void validateRequest(ChatRequest request) {
        Objects.requireNonNull(request, "request");
        if (OpenClawStrings.isBlank(request.getAgent())
                && OpenClawStrings.isBlank(request.getModel())) {
            throw new IllegalArgumentException(
                    "Chat request requires either 'agent' or 'model' field");
        }
        if (Objects.isNull(request.getMessages()) || request.getMessages().isEmpty()) {
            throw new IllegalArgumentException("Chat request requires at least one message");
        }
    }

    private Map<String, String> buildHeaders(ChatRequest request,
                                             Map<String, String> existingHeaders) {
        Map<String, String> headers = Objects.nonNull(existingHeaders)
                ? new HashMap<>(existingHeaders) : new HashMap<>();
        if (Objects.nonNull(request.getModel())
                && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }
        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(ChatRequest request) {
        return Objects.nonNull(request.getAgent()) ? request.getAgent() : request.getModel();
    }

    private static ExecutorService createStreamExecutor(OpenClawHttpClientConfig config) {
        int corePoolSize = Math.max(1, config.getStreamCorePoolSize());
        int maxPoolSize = Math.max(corePoolSize, config.getStreamMaxPoolSize());
        AtomicInteger threadIndex = new AtomicInteger();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                Math.max(1L, config.getStreamKeepAliveMillis()), TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Math.max(1, config.getStreamQueueCapacity())), runnable -> {
                    Thread thread = new Thread(runnable,
                            "openclaw-sse-consumer-" + threadIndex.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());
    }

    private void logInitialization(OpenClawHttpClientConfig config) {
        log.debug("OpenClaw SSE client initialized: baseUrl={}, maxRequests={}, "
                        + "maxRequestsPerHost={}, eventQueueCapacity={}, reconnectPolicy=none, "
                        + "detailedLoggingEnabled={}",
                config.getBaseUrl(), config.getMaxRequests(), config.getMaxRequestsPerHost(),
                config.getStreamQueueCapacity(), config.isDetailedLoggingEnabled());
    }

    /** 取消全部订阅并释放 SSE 客户端自有资源。 */
    @Override
    public void close() {
        for (SseSubscription subscription : activeSubscriptions) {
            subscription.close();
        }
        activeSubscriptions.clear();
        streamExecutor.shutdownNow();
        super.close();
    }
}
