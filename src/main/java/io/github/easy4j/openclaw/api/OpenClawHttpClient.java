package io.github.easy4j.openclaw.api;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;
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
     * 客户端使用的不可变配置引用。
     */
    protected final OpenClawHttpClientConfig config;
    /**
     * 负责协议 JSON 序列化与反序列化的映射器。
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
     * 使用默认 Jackson 映射器创建 HTTP 基类，认证与超时从配置读取，OkHttp 连接资源由调用方管理。
     *
     * @param config SDK 配置
     */
    protected OpenClawHttpClient(OpenClawHttpClientConfig config) {
        this(config, null, OpenClawOkHttpClientFactory.create(config), true);
    }

    /**
     * 使用调用方提供的 Jackson 映射器创建 HTTP 基类，认证与超时从配置读取，OkHttp 连接资源由调用方管理。
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
        debug("BASIC", "OpenClaw HTTP client initialized: baseUrl={}, connectTimeoutMs={}, readTimeoutMs={}, "
                        + "callTimeoutMs={}, retryOnConnectionFailure={}, debugLevel={}",
                config.getBaseUrl(), config.getConnectTimeoutMillis(), config.getReadTimeoutMillis(),
                config.getCallTimeoutMillis(), config.isRetryOnConnectionFailure(),
                config.getDebug().getLevel());
    }

    /**
     * 创建忽略未知响应字段的 ObjectMapper，使旧版 SDK 能读取 Gateway 新增字段。
     *
     * @return 已关闭未知字段失败检查的 ObjectMapper
     */
    protected ObjectMapper createObjectMapper() {
        return JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();
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
                    debug("HEADERS", "Added header: {}={}", k, redactHeader(k, v));
                }
            });
        }

        return builder;
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body 将由 Jackson 序列化的请求对象
     * @return 服务端成功响应的完整正文
     */
    protected String postJson(String path, Object body) {
        return postJson(path, body, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body 将由 Jackson 序列化的请求对象
     * @param headers 附加 HTTP 请求头
     * @return 服务端成功响应的完整正文
     */
    protected String postJson(String path, Object body, Map<String, String> headers) {
        return postJson(path, body, headers, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body 将由 Jackson 序列化的请求对象
     * @param headers 附加 HTTP 请求头
     * @param cancellation 可选调用取消令牌
     * @return 服务端成功响应的完整正文
     */
    protected String postJson(String path, Object body, Map<String, String> headers,
                              HttpCallCancellation cancellation) {
        return await(postJsonAsync(path, body, headers, cancellation));
    }

    /**
     * 将请求体序列化为 JSON 后交给 OkHttp Dispatcher 异步发送，并传播取消和协议异常。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @param body 将由 Jackson 序列化的请求对象
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
            debug("BODY", "Request body: {}", truncate(json));

            Request request = authedBuilder(url, headers)
                    .post(RequestBody.create(json, JSON))
                    .build();

            return executeAsync(request, url, cancellation);
        } catch (OpenClawHttpException e) {
            return failedFuture(e);
        } catch (JacksonException e) {
            return failedFuture(new OpenClawHttpException("POST " + url + " failed: " + e.getMessage(), e));
        }
    }

    /**
     * 同步等待 GET 请求完成；需要避免占用业务线程时应调用 {@link #getJsonAsync(String)}。
     *
     * @param path 相对于 Gateway 根地址的端点路径
     * @return 服务端成功响应的完整正文
     */
    protected String getJson(String path) {
        return await(getJsonAsync(path));
    }

    /**
     * 异步发送 GET 请求并返回响应 JSON；网络 I/O 由 OkHttp Dispatcher 执行。
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
     * @param request 要校验、序列化并发送的 {@code Request}
     * @param url 完整目标 URL
     * @return 服务端成功响应的完整正文
     * @throws IOException 网络、流或子进程 I/O 失败时抛出
     */
    protected String execute(Request request, String url) throws IOException {
        return execute(request, url, null);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 要校验、序列化并发送的 {@code Request}
     * @param url 完整目标 URL
     * @param cancellation 可选调用取消令牌
     * @return 服务端成功响应的完整正文
     * @throws IOException 网络、流或子进程 I/O 失败时抛出
     */
    protected String execute(Request request, String url,
                             HttpCallCancellation cancellation) throws IOException {
        return await(executeAsync(request, url, cancellation));
    }

    /**
     * 通过 OkHttp Dispatcher 异步发送请求，并在响应体读取、关闭或失败后完成结果。
     *
     * @param request 要校验、序列化并发送的 {@code Request}
     * @param url 完整目标 URL
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected CompletableFuture<String> executeAsync(Request request, String url,
                                                     HttpCallCancellation cancellation) {
        long requestId = REQUEST_SEQUENCE.incrementAndGet();
        long startedAt = System.nanoTime();
        debug("BASIC", "HTTP request started: requestId={}, method={}, url={}",
                requestId, request.method(), request.url());
        debug("HEADERS", "HTTP request headers: requestId={}, headers={}",
                requestId, redactHeaders(request.headers()));

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
            debug("BASIC", "HTTP request completed: requestId={}, method={}, url={}, bodyLength={}, elapsedMs={}",
                    requestId, request.method(), request.url(), respBody.length(), elapsedMillis(startedAt));
            debug("BODY", "HTTP response body: requestId={}, body={}", requestId, truncate(respBody));
        });
    }

    /**
     * 异步发送请求并保留 HTTP 状态码和响应体，供上层决定是否映射为异常。
     *
     * @param request 要校验、序列化并发送的 {@code Request}
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
             * 将 OkHttp 传输失败写入异步结果，并注销业务取消监听器。
             *
             * @param ignored OkHttp 回调关联的调用对象；当前回调无需读取它
             * @param error 导致调用失败的异常
             */
            @Override
            public void onFailure(Call ignored, IOException error) {
                closeRegistration(registration);
                result.completeExceptionally(error);
            }

            /**
             * 在回调线程读取并关闭响应体，然后以状态码和文本完成异步结果。
             *
             * @param ignored OkHttp 回调关联的调用对象；当前回调无需读取它
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
     * OkHttp 响应的轻量快照，在底层 {@link Response} 关闭后保留状态码和响应文本。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    protected static final class HttpResponseData {
        /**
         * HTTP 响应状态码，用于区分成功与协议错误。
         */
        private final int statusCode;
        /**
         * 已完整读取的 HTTP 响应正文；响应体为空时为空字符串。
         */
        private final String body;

        private HttpResponseData(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        /**
         * 返回 HTTP 响应状态码。
         *
         * @return HTTP 状态码
         */
        protected int getStatusCode() {
            return statusCode;
        }

        /**
         * 返回已读取并缓存的 HTTP 响应正文。
         *
         * @return 响应正文；响应体为空时为空字符串
         */
        protected String getBody() {
            return body;
        }
    }

    private String await(CompletableFuture<String> future) {
        return awaitFuture(future);
    }

    /**
     * 将异步 API 桥接为同步调用，并把 CompletionException 中的 SDK 异常恢复为原始类型。
     *
     * @param <T> 方法使用的泛型类型
     * @param future 待同步等待的异步结果
     * @return 异步调用完成的值
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

    protected String truncate(String value) {
        if (Objects.isNull(value)) {
            return "";
        }
        int limit = config.getDebug().resolveMaxContentLength();
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
            return "<redacted>";
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
     * @param type JSON 反序列化目标类
     * @return 按目标类反序列化得到的对象
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JacksonException e) {
            throw new OpenClawHttpException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param <T> 方法使用的泛型类型
     * @param json JSON 文本
     * @param type JSON 反序列化目标类
     * @param label 日志或诊断信息中使用的字段标签
     * @return 按目标类反序列化得到的对象
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    protected <T> T parse(String json, Class<T> type, String label) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JacksonException e) {
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
     * 向 Gateway 健康检查端点发送请求；非成功状态映射为 HTTP 异常。
     */
    public void health() {
        awaitFuture(healthAsync());
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code health}，调用线程不等待远端响应。
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
     * 在 DEBUG 级别启用时记录格式化日志。
     *
     * @param msg 包含 SLF4J 占位符的日志消息模板
     * @param args 填充消息模板占位符的参数
     */
    protected void debug(String msg, Object... args) {
        debug("BASIC", msg, args);
    }

    /**
     * 在统一调试配置允许指定级别且 SLF4J 开启 DEBUG 时记录日志。
     *
     * @param level 信息要求的最低 HTTP 日志级别
     * @param msg 包含 SLF4J 占位符的日志消息模板
     * @param args 填充消息模板占位符的参数
     */
    void debug(String level, String msg, Object... args) {
        if (config.getDebug().allows(level) && log.isDebugEnabled()) {
            log.debug(msg, args);
        }
    }

    /**
     * 在 INFO 级别启用时记录格式化日志。
     *
     * @param msg 包含 SLF4J 占位符的日志消息模板
     * @param args 填充消息模板占位符的参数
     */
    protected void info(String msg, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(msg, args);
        }
    }

    /**
     * 记录 WARN 级别格式化日志。
     *
     * @param msg 包含 SLF4J 占位符的日志消息模板
     * @param args 填充消息模板占位符的参数
     */
    protected void warn(String msg, Object... args) {
        log.warn(msg, args);
    }

    /**
     * 记录 ERROR 级别格式化日志。
     *
     * @param msg 包含 SLF4J 占位符的日志消息模板
     * @param args 填充消息模板占位符的参数
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
