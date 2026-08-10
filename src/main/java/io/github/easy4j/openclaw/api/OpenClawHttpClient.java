package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.OpenClawOkHttpClientFactory;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * OkHttp 客户端抽象基类。它统一完成 URL 与鉴权头构造、JSON 读写、异步取消传播、响应体关闭、HTTP 异常映射以及脱敏追踪日志。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Slf4j
public abstract class OpenClawHttpClient implements AutoCloseable {

    /**
     * HTTP JSON 负载使用的媒体类型定义。
     */
    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    /**
     * 跨线程共享的原子请求序号，用于关联同一进程内的追踪日志。
     */
    private static final AtomicLong REQUEST_SEQUENCE = new AtomicLong();

    /**
     * SDK 配置。
     */
    protected final OpenClawHttpClientConfig config;
    /**
     * JSON 映射器。
     */
    protected final ObjectMapper objectMapper;
    /**
     * 复用连接池和 Dispatcher 的 OkHttpClient。
     */
    protected final OkHttpClient httpClient;
    /**
     * 是否由当前对象创建 OkHttpClient；仅为 {@code true} 时 close() 才关闭 Dispatcher 与连接池，外部注入客户端不会被关闭。
     */
    private final boolean ownsHttpClient;

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    protected OpenClawHttpClient(OpenClawHttpClientConfig config) {
        this(config, null, OpenClawOkHttpClientFactory.create(config), true);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    protected OpenClawHttpClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this(config, objectMapper,
                Objects.isNull(httpClient) ? OpenClawOkHttpClientFactory.create(config) : httpClient,
                Objects.isNull(httpClient));
    }

    private OpenClawHttpClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper,
                               OkHttpClient httpClient, boolean ownsHttpClient) {
        this.config = Objects.requireNonNull(config, "config");
        this.objectMapper = objectMapper != null ? objectMapper : createObjectMapper();
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.ownsHttpClient = ownsHttpClient;
        debug("OpenClaw HTTP client initialized: baseUrl={}, connectTimeoutMs={}, readTimeoutMs={}, "
                        + "callTimeoutMs={}, retryOnConnectionFailure={}, detailedLoggingEnabled={}",
                config.getBaseUrl(), config.getConnectTimeoutMillis(), config.getReadTimeoutMillis(),
                config.getCallTimeoutMillis(), config.isRetryOnConnectionFailure(),
                config.isDetailedLoggingEnabled());
    }

    /**
     * 创建忽略未知响应字段的 ObjectMapper，使旧版 SDK 能读取 Gateway 新增字段。
     *
     * @return 已关闭未知字段失败检查的 ObjectMapper
     */
    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // ============================================================
    // HTTP primitives
    // ============================================================

    /**
     * 创建指向目标 URL 的 Request.Builder，写入 JSON Content-Type、解析后的认证头和已脱敏追踪信息。
     *
     * @param url 完整目标 URL
     * @return 已写入目标 URL、Content-Type、认证头和附加请求头的构建器
     */
    protected Request.Builder authedBuilder(String url) {
        return authedBuilder(url, null);
    }

    /**
     * 创建指向目标 URL 的 Request.Builder，写入 JSON Content-Type、解析后的认证头和已脱敏追踪信息。
     *
     * @param url 完整目标 URL
     * @param headers 附加 HTTP 请求头
     * @return 已写入目标 URL、Content-Type、认证头和附加请求头的构建器
     */
    protected Request.Builder authedBuilder(String url, Map<String, String> headers) {
        debug("Building request: url={}", url);

        Request.Builder builder = new Request.Builder().url(url)
                .header("Content-Type", "application/json");

        String token = config.resolveGatewayBearerToken();
        if (OpenClawStrings.isNotBlank(token)) {
            builder.header("Authorization", "Bearer " + token);
            debug("Added Authorization header");
        } else {
            warn("No gateway bearer token configured");
        }

        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> {
                if (k != null && v != null) {
                    builder.header(k, v);
                    if (config.isDetailedLoggingEnabled()) {
                        debug("Added header: {}={}", k, redactHeader(k, v));
                    }
                }
            });
        }

        return builder;
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body JSON 请求体或响应体文本
     * @return 服务返回或流式累积得到的文本
     */
    protected String postJson(String path, Object body) {
        return postJson(path, body, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body JSON 请求体或响应体文本
     * @param headers 附加 HTTP 请求头
     * @return 服务返回或流式累积得到的文本
     */
    protected String postJson(String path, Object body, Map<String, String> headers) {
        return postJson(path, body, headers, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body JSON 请求体或响应体文本
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 服务返回或流式累积得到的文本
     */
    protected String postJson(String path, Object body, Map<String, String> headers,
                              HttpCallCancellation cancellation) {
        return await(postJsonAsync(path, body, headers, cancellation));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `postJson`，调用线程不会等待远程响应。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body JSON 请求体或响应体文本
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    protected CompletableFuture<String> postJsonAsync(String path, Object body, Map<String, String> headers,
                                                      HttpCallCancellation cancellation) {
        String url = resolveUrl(path);
        debug("POST JSON: path={}, url={}", path, url);

        try {
            String json = objectMapper.writeValueAsString(body);
            if (config.isDetailedLoggingEnabled()) {
                debug("Request body: {}", truncate(json));
            }

            Request request = authedBuilder(url, headers)
                    .post(RequestBody.create(json, JSON))
                    .build();

            return executeAsync(request, url, cancellation);
        } catch (OpenClawHttpException e) {
            return failedFuture(e);
        } catch (IOException e) {
            return failedFuture(new OpenClawHttpException("POST " + url + " failed: " + e.getMessage(), e));
        }
    }

    /**
     * 读取当前对象保存的 JSON 文本，不触发网络或子进程调用。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @return 服务返回或流式累积得到的文本
     */
    protected String getJson(String path) {
        return await(getJsonAsync(path));
    }

    /**
     * 读取当前对象保存的 `jsonAsync` 对应状态，不触发网络或子进程调用。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    protected CompletableFuture<String> getJsonAsync(String path) {
        String url = resolveUrl(path);
        debug("GET JSON: path={}, url={}", path, url);

        try {
            Request request = authedBuilder(url).get().build();
            return executeAsync(request, url, null);
        } catch (OpenClawHttpException e) {
            return failedFuture(e);
        }
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 请求对象
     * @param url 完整目标 URL
     * @return 服务返回或流式累积得到的文本
     * @throws IOException 网络、流或子进程 I/O 失败时抛出
     */
    protected String execute(Request request, String url) throws IOException {
        return execute(request, url, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 请求对象
     * @param url 完整目标 URL
     * @param cancellation 可选调用取消令牌
     * @return 服务返回或流式累积得到的文本
     * @throws IOException 网络、流或子进程 I/O 失败时抛出
     */
    protected String execute(Request request, String url,
                             HttpCallCancellation cancellation) throws IOException {
        return await(executeAsync(request, url, cancellation));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `execute`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @param url 完整目标 URL
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected CompletableFuture<String> executeAsync(Request request, String url,
                                                     HttpCallCancellation cancellation) {
        long requestId = REQUEST_SEQUENCE.incrementAndGet();
        long startedAt = System.nanoTime();
        debug("HTTP request started: requestId={}, method={}, url={}", requestId, request.method(), request.url());
        if (config.isDetailedLoggingEnabled()) {
            debug("HTTP request details: requestId={}, headers={}", requestId, redactHeaders(request.headers()));
        }

        // 传输层只读取状态码和响应体；此处统一把非 2xx 响应转换为携带诊断信息的 SDK 异常。
        CompletableFuture<String> result = executeResponseAsync(request, cancellation).thenApply(response -> {
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new OpenClawHttpException("Request returned status " + response.getStatusCode(),
                        response.getStatusCode(), response.getBody());
            }
            return response.getBody();
        });
        return result.whenComplete((respBody, error) -> {
            if (Objects.nonNull(error)) {
                log.warn("HTTP request failed: requestId={}, method={}, url={}, elapsedMs={}, error={}",
                        requestId, request.method(), request.url(), elapsedMillis(startedAt), unwrap(error).getMessage());
                return;
            }
            debug("HTTP request completed: requestId={}, method={}, url={}, bodyLength={}, elapsedMs={}",
                    requestId, request.method(), request.url(), respBody.length(), elapsedMillis(startedAt));
            if (config.isDetailedLoggingEnabled()) {
                debug("HTTP response body: requestId={}, body={}", requestId, truncate(respBody));
            }
        });
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `executeResponse`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    protected CompletableFuture<HttpResponseData> executeResponseAsync(Request request,
                                                                       HttpCallCancellation cancellation) {
        return executeOkHttpResponseAsync(request, cancellation);
    }

    private CompletableFuture<HttpResponseData> executeOkHttpResponseAsync(Request request,
                                                                           HttpCallCancellation cancellation) {
        CompletableFuture<HttpResponseData> result = new CompletableFuture<>();
        Call call = httpClient.newCall(request);
        // 将业务取消令牌绑定到本次 Call；请求结束后注销，避免长生命周期令牌持有已完成调用。
        AutoCloseable registration = Objects.nonNull(cancellation)
                ? cancellation.onCancel(call::cancel) : null;
        // enqueue 使用 OkHttp Dispatcher 异步执行，不占用调用方线程等待网络 I/O。
        call.enqueue(new Callback() {
            /**
             * 接收并处理 Failure 生命周期事件；实现不会改变事件顺序。
             *
             * @param ignored 写入 `ignored` 协议字段的内容
             * @param error 导致调用失败的异常
             */
            @Override
            public void onFailure(Call ignored, IOException error) {
                closeRegistration(registration);
                result.completeExceptionally(error);
            }

            /**
             * 接收并处理 Response 生命周期事件；实现不会改变事件顺序。
             *
             * @param ignored 写入 `ignored` 协议字段的内容
             * @param response 待消费并关闭的 HTTP 响应
             */
            @Override
            public void onResponse(Call ignored, Response response) {
                // ResponseBody 是一次性资源，必须在回调线程读取并随 Response 一同关闭。
                try (Response completed = response) {
                    String body = Objects.nonNull(completed.body()) ? completed.body().string() : "";
                    result.complete(new HttpResponseData(completed.code(), body));
                } catch (Exception error) {
                    result.completeExceptionally(error);
                } finally {
                    closeRegistration(registration);
                }
            }
        });
        // CompletableFuture 被调用方取消时反向取消 OkHttp Call，使两套生命周期保持一致。
        result.whenComplete((value, error) -> {
            if (result.isCancelled()) {
                call.cancel();
            }
        });
        return result;
    }

    /**
     * OpenClaw SDK 的 `HttpResponseData` 类型，封装其公开契约和生命周期边界。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    protected static final class HttpResponseData {
        /**
         * `HttpResponseData` 生命周期内保存的 `statusCode` 对应状态。
         */
        private final int statusCode;
        /**
         * JSON 请求体或响应体文本。
         */
        private final String body;

        private HttpResponseData(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        /**
         * 读取当前对象保存的 `statusCode` 对应状态，不触发网络或子进程调用。
         *
         * @return 当前计数、状态码、可空配置或毫秒级时间值
         */
        protected int getStatusCode() {
            return statusCode;
        }

        /**
         * 读取当前对象保存的 JSON 请求体或响应体文本，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        protected String getBody() {
            return body;
        }
    }

    private String await(CompletableFuture<String> future) {
        return awaitFuture(future);
    }

    /**
     * 调用 OpenClaw 的 `awaitFuture` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param <T> 方法使用的泛型类型
     * @param future 写入 `future` 协议字段的内容
     * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected <T> T awaitFuture(CompletableFuture<T> future) {
        try {
            return future.join();
        } catch (CompletionException error) {
            Throwable cause = unwrap(error);
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new OpenClawHttpException("Async HTTP request failed: " + cause.getMessage(), cause);
        }
    }

    private Throwable unwrap(Throwable error) {
        return error instanceof CompletionException && Objects.nonNull(error.getCause()) ? error.getCause() : error;
    }

    private <T> CompletableFuture<T> failedFuture(Throwable error) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    private String truncate(String value) {
        if (Objects.isNull(value)) {
            return "";
        }
        int limit = Math.max(0, config.getMaxLoggedBodyLength());
        return value.length() <= limit ? value : value.substring(0, limit) + "...<truncated>";
    }

    private Headers redactHeaders(Headers headers) {
        Headers.Builder safe = headers.newBuilder();
        for (String name : headers.names()) {
            safe.set(name, redactHeader(name, headers.get(name)));
        }
        return safe.build();
    }

    private String redactHeader(String name, String value) {
        if ("authorization".equalsIgnoreCase(name) || name.toLowerCase().contains("token")
                || name.toLowerCase().contains("key")) {
            return "██";
        }
        return value;
    }

    private void closeRegistration(AutoCloseable registration) {
        if (registration == null) {
            return;
        }
        try {
            registration.close();
        } catch (Exception error) {
            debug("Failed to unregister HTTP cancellation callback: {}", error.getMessage());
        }
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param <T> 方法使用的泛型类型
     * @param json JSON 文本
     * @param type 写入 `type` 协议字段的内容
     * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param <T> 方法使用的泛型类型
     * @param json JSON 文本
     * @param type 写入 `type` 协议字段的内容
     * @param label 写入 `label` 协议字段的内容
     * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected <T> T parse(String json, Class<T> type, String label) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse " + label + " response: " + e.getMessage(), e);
        }
    }

    /**
     * 根据显式参数和配置默认值解析本次请求使用的 Url。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @return 规范化后的目标地址或路径
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected String resolveUrl(String path) {
        String base = config.getBaseUrl();
        if (OpenClawStrings.isBlank(base)) {
            throw new OpenClawHttpException("gatewayBaseUrl is empty", null);
        }
        return base.replaceAll("/+$", "") + path;
    }

    // ============================================================
    // Health probe
    // ============================================================

    /**
     * 调用 OpenClaw 的 `health` API，并复用统一认证、序列化、取消和异常处理。
     */
    public void health() {
        awaitFuture(healthAsync());
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `health`，调用线程不会等待远程响应。
     *
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<Void> healthAsync() {
        debug("=== Health probe: {} ===", OpenClawConstants.ENDPOINT_MODELS);
        return getJsonAsync(OpenClawConstants.ENDPOINT_MODELS).thenAccept(ignored -> debug("Health probe OK"));
    }

    // ============================================================
    // Logging helpers
    // ============================================================

    /**
     * 调用 OpenClaw 的 `debug` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param msg 写入 `msg` 协议字段的内容
     * @param args 写入 `args` 协议字段的内容
     */
    protected void debug(String msg, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(msg, args);
        }
    }

    /**
     * 调用 OpenClaw 的 `info` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param msg 写入 `msg` 协议字段的内容
     * @param args 写入 `args` 协议字段的内容
     */
    protected void info(String msg, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(msg, args);
        }
    }

    /**
     * 调用 OpenClaw 的 `warn` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param msg 写入 `msg` 协议字段的内容
     * @param args 写入 `args` 协议字段的内容
     */
    protected void warn(String msg, Object... args) {
        log.warn(msg, args);
    }

    /**
     * 调用 OpenClaw 的 `error` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param msg 写入 `msg` 协议字段的内容
     * @param args 写入 `args` 协议字段的内容
     */
    protected void error(String msg, Object... args) {
        log.error(msg, args);
    }

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
    @Override
    public void close() {
        if (ownsHttpClient) {
            OpenClawOkHttpClientFactory.shutdown(httpClient);
        }
    }
}
