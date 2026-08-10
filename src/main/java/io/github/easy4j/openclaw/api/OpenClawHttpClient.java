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

/**
 * HTTP client base class.
 * <p>
 * Wraps OkHttp ObjectMapper ,Provides HTTP .
 * </p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Slf4j
public abstract class OpenClawHttpClient implements AutoCloseable {

    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final AtomicLong REQUEST_SEQUENCE = new AtomicLong();

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
        debug("OpenClaw HTTP client initialized: baseUrl={}, connectTimeoutMs={}, readTimeoutMs={}, "
                        + "callTimeoutMs={}, retryOnConnectionFailure={}, detailedLoggingEnabled={}",
                config.getBaseUrl(), config.getConnectTimeoutMillis(), config.getReadTimeoutMillis(),
                config.getCallTimeoutMillis(), config.isRetryOnConnectionFailure(),
                config.isDetailedLoggingEnabled());
    }

    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // ============================================================
    // HTTP primitives
    // ============================================================

    /**
 * Builds an authenticated.
     */
    protected Request.Builder authedBuilder(String url) {
        return authedBuilder(url, null);
    }

    /**
 * Builds an authenticated,extra headers.
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
 * POST JSON .
     */
    protected String postJson(String path, Object body) {
        return postJson(path, body, null);
    }

    /**
 * POST JSON ,extra headers.
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
            if (config.isDetailedLoggingEnabled()) {
                debug("Request body: {}", truncate(json));
            }

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
 * GET JSON .
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
 * .
     */
    protected String execute(Request request, String url) throws IOException {
        return execute(request, url, null);
    }

    /** 执行支持协作式取消的请求。 */
    protected String execute(Request request, String url,
                             HttpCallCancellation cancellation) throws IOException {
        long requestId = REQUEST_SEQUENCE.incrementAndGet();
        long startedAt = System.nanoTime();
        debug("HTTP request started: requestId={}, method={}, url={}", requestId, request.method(), request.url());
        if (config.isDetailedLoggingEnabled()) {
            debug("HTTP request details: requestId={}, headers={}", requestId, redactHeaders(request.headers()));
        }

        Call call = httpClient.newCall(request);
        AutoCloseable registration = cancellation != null ? cancellation.onCancel(call::cancel) : null;
        try (Response response = call.execute()) {
            int status = response.code();
            String respBody = response.body() != null ? response.body().string() : "";

            long elapsedMs = elapsedMillis(startedAt);
            debug("HTTP request completed: requestId={}, method={}, url={}, status={}, bodyLength={}, elapsedMs={}",
                    requestId, request.method(), request.url(), status, respBody.length(), elapsedMs);
            if (config.isDetailedLoggingEnabled()) {
                debug("HTTP response body: requestId={}, body={}", requestId, truncate(respBody));
            }

            if (!response.isSuccessful()) {
                throw new OpenClawHttpException("Request returned status " + status, status, respBody);
            }
            return respBody;
        } catch (IOException | RuntimeException error) {
            log.warn("HTTP request failed: requestId={}, method={}, url={}, elapsedMs={}, error={}",
                    requestId, request.method(), request.url(), elapsedMillis(startedAt), error.getMessage());
            throw error;
        } finally {
            closeRegistration(registration);
        }
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
 * JSON .
     */
    protected <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    /**
 * JSON ,.
     */
    protected <T> T parse(String json, Class<T> type, String label) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new OpenClawHttpException("Failed to parse " + label + " response: " + e.getMessage(), e);
        }
    }

    /**
 * Resolves the URL.
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
 * Probes the Gateway HTTP .
     * <p>
 * {@code GET /v1/models} ; HTTP 2xx .
 * {@link OpenClawHttpException} probe failed( 2xx).
     * </p>
     *
 * @throws OpenClawHttpException 2xx
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
