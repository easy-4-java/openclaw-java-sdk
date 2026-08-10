package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.EmbeddingsRequest;
import io.github.easy4j.openclaw.api.model.EmbeddingsResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * OpenClaw SDK 的 `OpenClawEmbeddingsClient` 类型，封装其公开契约和生命周期边界。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawEmbeddingsClient extends OpenClawHttpClient {

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawEmbeddingsClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawEmbeddingsClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawEmbeddingsClient`。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 EmbeddingsResponse
     */
    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        return awaitFuture(createEmbeddingsAsync(request));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `createEmbeddings`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<EmbeddingsResponse> createEmbeddingsAsync(EmbeddingsRequest request) {
        debug("=== Embeddings Request ===");
        debug("agent: {}", request.getAgent());
        debug("model: {}", request.getModel());
        debug("input: {}", request.getInput());

        // 验证请求
        validateRequest(request);

        Map<String, String> headers = buildHeaders(request);
        String bodyModel = resolveModel(request);

        debug("Resolved body model: {}", bodyModel);

        EmbeddingsRequest normalized = EmbeddingsRequest.builder()
                .model(bodyModel)
                .input(request.getInput())
                .build();

        return postJsonAsync(OpenClawConstants.ENDPOINT_EMBEDDINGS, normalized, headers, null)
                .thenApply(json -> {
                    debug("Embeddings response received");
                    return parse(json, EmbeddingsResponse.class, "embeddings");
                });
    }

    private void validateRequest(EmbeddingsRequest request) {
        if (OpenClawStrings.isBlank(request.getAgent()) && OpenClawStrings.isBlank(request.getModel())) {
            String msg = "Embeddings request requires either 'agent' or 'model' field. " +
                    "Use 'agent' for OpenClaw routing (e.g., 'openclaw/default'), " +
                    "or 'model' for direct backend model (e.g., 'text-embedding-3-small').";
            warn(msg);
            throw new IllegalArgumentException(msg);
        }

        if (request.getInput() == null) {
            warn("Embeddings request has no input");
            throw new IllegalArgumentException("Embeddings request requires input");
        }
    }

    private Map<String, String> buildHeaders(EmbeddingsRequest request) {
        Map<String, String> headers = new HashMap<>();

        if (request.getModel() != null && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
            debug("Added {} header: {}", OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }

        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(EmbeddingsRequest request) {
        if (request.getAgent() != null) {
            debug("Using agent as model: {}", request.getAgent());
            return request.getAgent();
        }
        debug("Using model: {}", request.getModel());
        return request.getModel();
    }
}
