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

/**
 * HTTP 客户端基类。
 * <p>
 * 封装 OkHttp 和 ObjectMapper 的配置，提供通用的 HTTP 请求方法。
 * </p>
 */
@Getter
@Slf4j
public abstract class OpenClawHttpClient implements AutoCloseable {

    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    protected final OpenClawHttpClientConfig config;
    protected final ObjectMapper objectMapper;
    protected final OkHttpClient httpClient;
    private final boolean ownsHttpClient;

    protected OpenClawHttpClient(OpenClawHttpClientConfig config) {
        this(config, null, OpenClawOkHttpClientFactory.create(config), true);
    }

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
    }

    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // ============================================================
    // HTTP primitives
    // ============================================================

    /**
     * 构建带认证的请求。
     */
    protected Request.Builder authedBuilder(String url) {
        return authedBuilder(url, null);
    }

    /**
     * 构建带认证的请求，追加额外请求头。
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
                    debug("Added header: {}={}", k, v);
                }
            });
        }

        return builder;
    }

    /**
     * POST JSON 请求。
     */
    protected String postJson(String path, Object body) {
        return postJson(path, body, null);
    }

    /**
     * POST JSON 请求，带额外请求头。
     */
    protected String postJson(String path, Object body, Map<String, String> headers) {
        return postJson(path, body, headers, null);
    }

    /** POST JSON 请求，并将调用方取消信号绑定到底层 Call。 */
    protected String postJson(String path, Object body, Map<String, String> headers,
                              HttpCallCancellation cancellation) {
        String url = resolveUrl(path);
        debug("POST JSON: path={}, url={}", path, url);

        try {
            String json = objectMapper.writeValueAsString(body);
            debug("Request body: {}", json);

            Request request = authedBuilder(url, headers)
                    .post(RequestBody.create(json, JSON))
                    .build();

            return execute(request, url, cancellation);
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (IOException e) {
            throw new OpenClawHttpException("POST " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * GET JSON 请求。
     */
    protected String getJson(String path) {
        String url = resolveUrl(path);
        debug("GET JSON: path={}, url={}", path, url);

        try {
            Request request = authedBuilder(url).get().build();
            return execute(request, url);
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (IOException e) {
            throw new OpenClawHttpException("GET " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * 执行请求。
     */
    protected String execute(Request request, String url) throws IOException {
        return execute(request, url, null);
    }

    /** 执行支持协作式取消的请求。 */
    protected String execute(Request request, String url,
                             HttpCallCancellation cancellation) throws IOException {
        debug("Executing request: {} {}", request.method(), request.url());
        debug("Request headers: {}", request.headers());

        Call call = httpClient.newCall(request);
        AutoCloseable registration = cancellation != null ? cancellation.onCancel(call::cancel) : null;
        try (Response response = call.execute()) {
            int status = response.code();
            String respBody = response.body() != null ? response.body().string() : "";

            debug("Response status: {}, body length: {}", status, respBody.length());
            if (status >= 300) {
                debug("Response body (error): {}", respBody);
            } else if (respBody.length() < 500) {
                debug("Response body: {}", respBody);
            } else {
                debug("Response body (truncated): {}...", respBody.substring(0, 500));
            }

            if (!response.isSuccessful()) {
                throw new OpenClawHttpException("Request returned status " + status, status, respBody);
            }
            return respBody;
        } finally {
            closeRegistration(registration);
        }
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
     * 解析 JSON 响应。
     */
    protected <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    /**
     * 解析 JSON 响应，带标签。
     */
    protected <T> T parse(String json, Class<T> type, String label) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse " + label + " response: " + e.getMessage(), e);
        }
    }

    /**
     * 解析 URL。
     */
    protected String resolveUrl(String path) {
        String base = config.getGatewayBaseUrl();
        if (OpenClawStrings.isBlank(base)) {
            throw new OpenClawHttpException("gatewayBaseUrl is empty", null);
        }
        return base.replaceAll("/+$", "") + path;
    }

    // ============================================================
    // Health probe
    // ============================================================

    /**
     * 探测 Gateway HTTP 服务可用性。
     * <p>
     * 通过 {@code GET /v1/models} 触发；任何 HTTP 2xx 都视为健康。
     * 抛出 {@link OpenClawHttpException} 时表示探测失败（网络错误或非 2xx）。
     * </p>
     *
     * @throws OpenClawHttpException 网络失败或响应非 2xx
     */
    public void health() {
        debug("=== Health probe: {} ===", OpenClawConstants.ENDPOINT_MODELS);
        getJson(OpenClawConstants.ENDPOINT_MODELS);
        debug("Health probe OK");
    }

    // ============================================================
    // Logging helpers
    // ============================================================

    protected void debug(String msg, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(msg, args);
        }
    }

    protected void info(String msg, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(msg, args);
        }
    }

    protected void warn(String msg, Object... args) {
        log.warn(msg, args);
    }

    protected void error(String msg, Object... args) {
        log.error(msg, args);
    }

    @Override
    public void close() {
        if (ownsHttpClient) {
            OpenClawOkHttpClientFactory.shutdown(httpClient);
        }
    }
}
