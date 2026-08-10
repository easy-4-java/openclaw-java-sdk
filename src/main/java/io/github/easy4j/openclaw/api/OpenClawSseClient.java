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
 * SSE 传输客户端。OkHttp Dispatcher 仅接收响应，持续流读取转交有界执行器，并通过 SseSubscription 管理取消和关闭。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawSseClient extends OpenClawHttpClient {

    /**
     * 执行 SSE 读取任务的线程池；由客户端关闭时主动终止。
     */
    private final ExecutorService streamExecutor;
    /**
     * 仍在读取的 SSE 订阅集合，供 close() 批量取消且避免保留已终止订阅。
     */
    private final Set<SseSubscription> activeSubscriptions = ConcurrentHashMap.newKeySet();

    /**
     * 构造 SSE 客户端并创建有界流读取执行器；外部注入的 OkHttpClient 不随当前对象关闭。
     *
     * @param config SDK 配置
     */
    public OpenClawSseClient(OpenClawHttpClientConfig config) {
        super(config);
        this.streamExecutor = createStreamExecutor(config);
        logInitialization(config);
    }

    /**
     * 构造 SSE 客户端并创建有界流读取执行器；外部注入的 OkHttpClient 不随当前对象关闭。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawSseClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper,
                             OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
        this.streamExecutor = createStreamExecutor(config);
        logInitialization(config);
    }

    /**
     * 启动 SSE 请求并登记活动订阅；返回句柄可取消 Call 并释放响应资源。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param handler 事件处理器
     * @return 已登记且可主动取消的 SSE 订阅句柄
     */
    public SseSubscription subscribeChat(ChatRequest request, SseEventHandler handler) {
        return subscribeChat(request, null, handler);
    }

    /**
     * 启动 SSE 请求并登记活动订阅；返回句柄可取消 Call 并释放响应资源。
     *
     * @param request 要校验、序列化并发送的 {@code ChatRequest}
     * @param headers 附加 HTTP 请求头
     * @param handler 事件处理器
     * @return 已登记且可主动取消的 SSE 订阅句柄
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
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
        // 取消动作同时终止网络调用并从活动集合移除，确保 close() 只遍历仍存活的订阅。
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
     * 统计尚未取消且读取任务未结束的 SSE 订阅。
     *
     * @return 当前仍处于活动状态的 SSE 订阅数量
     */
    public int activeSubscriptionCount() {
        return activeSubscriptions.size();
    }

    private void enqueue(Call call, SseSubscription subscription, SseEventHandler handler) {
        // OkHttp 回调只接收响应；持续读取转交专用有界执行器，避免阻塞 Dispatcher。
        call.enqueue(new Callback() {
            /**
             * 处理 OkHttp 传输失败，关闭注册资源并完成异常结果。
             *
             * @param ignored OkHttp 回调关联的调用对象；当前回调无需读取它
             * @param error 导致调用失败的异常
             */
            @Override
            public void onFailure(Call ignored, IOException error) {
                if (subscription.isActive()) {
                    handler.onError(error);
                }
                subscription.close();
            }

            /**
             * 处理 HTTP 或 Gateway 响应，并完成对应异步请求。
             *
             * @param ignored OkHttp 回调关联的调用对象；当前回调无需读取它
             * @param response 待消费并关闭的 HTTP 响应
             */
            @Override
            public void onResponse(Call ignored, Response response) {
                if (!subscription.isActive()) {
                    response.close();
                    return;
                }
                try {
                    streamExecutor.execute(() -> consume(response, subscription, handler));
                } catch (RejectedExecutionException error) {
                    // 执行器满载时快速失败，防止无界排队；当前分支仍负责关闭响应。
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
        // consume 获得 Response 所有权，所有成功、失败和取消路径最终都终止订阅。
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

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
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
