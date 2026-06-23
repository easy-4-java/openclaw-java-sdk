package io.github.hiwepy.openclaw.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hiwepy.openclaw.OpenClawHttpClientConfig;
import io.github.hiwepy.openclaw.exception.OpenClawHttpException;
import io.github.hiwepy.openclaw.util.OpenClawStrings;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 客户端基类。
 * <p>
 * 封装 OkHttp 和 ObjectMapper 的配置，提供通用的 HTTP 请求方法。
 * </p>
 */
@Getter
public abstract class OpenClawHttpClient implements AutoCloseable {

    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    protected final OpenClawHttpClientConfig config;
    protected final ObjectMapper objectMapper;
    protected final OkHttpClient httpClient;

    protected OpenClawHttpClient(OpenClawHttpClientConfig config) {
        this(config, null, null);
    }

    protected OpenClawHttpClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        this.config = Objects.requireNonNull(config, "config");
        this.objectMapper = objectMapper != null ? objectMapper : createObjectMapper();
        this.httpClient = httpClient != null ? httpClient : buildOkHttpClient(config);
    }

    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected OkHttpClient buildOkHttpClient(OpenClawHttpClientConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS);
        if (!config.isVerifySsl()) {
            builder.hostnameVerifier((hostname, session) -> true);
        }
        return builder.build();
    }

    // ============================================================
    // HTTP primitives
    // ============================================================

    /**
     * 构建带认证的请求。
     */
    protected Request.Builder authedBuilder(String url) {
        Request.Builder builder = new Request.Builder().url(url)
                .header("Content-Type", "application/json");
        String token = config.resolveGatewayBearerToken();
        if (OpenClawStrings.isNotBlank(token)) {
            builder.header("Authorization", "Bearer " + token);
        }
        return builder;
    }

    /**
     * 构建带认证的请求，追加额外请求头。
     */
    protected Request.Builder authedBuilder(String url, Map<String, String> headers) {
        Request.Builder builder = authedBuilder(url);
        if (headers != null) {
            headers.forEach((k, v) -> {
                if (k != null && v != null) {
                    builder.header(k, v);
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
        String url = resolveUrl(path);
        try {
            String json = objectMapper.writeValueAsString(body);
            Request request = authedBuilder(url, headers)
                    .post(RequestBody.create(json, JSON))
                    .build();
            return execute(request);
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
        try {
            Request request = authedBuilder(url).get().build();
            return execute(request);
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (IOException e) {
            throw new OpenClawHttpException("GET " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * 执行请求。
     */
    protected String execute(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            String respBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new OpenClawHttpException("Request returned status " + response.code(), response.code(), respBody);
            }
            return respBody;
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

    @Override
    public void close() {
        // 外部传入的 OkHttpClient 不关闭，由创建者管理
    }
}
