package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.api.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * OpenClaw SDK 的 `OpenClawResponsesClient` 类型，封装其公开契约和生命周期边界。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawResponsesClient extends OpenClawHttpClient {

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawResponsesClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawResponsesClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawResponsesClient`。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ResponseResult
     */
    public ResponseResult createResponse(ResponseRequest request) {
        return awaitFuture(createResponseAsync(request));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `createResponse`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ResponseResult> createResponseAsync(ResponseRequest request) {
        debug("=== Response Request ===");
        debug("agent: {}", request.getAgent());
        debug("model: {}", request.getModel());
        debug("input type: {}", request.getInput() != null ? request.getInput().getClass().getSimpleName() : "null");
        debug("stream: {}", request.getStream());

        // 验证请求
        validateRequest(request);

        Map<String, String> headers = buildHeaders(request);
        String bodyModel = resolveModel(request);

        debug("Resolved body model: {}", bodyModel);

        ResponseRequest normalized = ResponseRequest.builder()
                .model(bodyModel)
                .input(request.getInput())
                .instructions(request.getInstructions())
                .tools(request.getTools())
                .toolChoice(request.getToolChoice())
                .stream(request.getStream())
                .maxOutputTokens(request.getMaxOutputTokens())
                .temperature(request.getTemperature())
                .topP(request.getTopP())
                .user(request.getUser())
                .previousResponseId(request.getPreviousResponseId())
                .build();

        return postJsonAsync(OpenClawConstants.ENDPOINT_RESPONSES, normalized, headers, null)
                .thenApply(json -> {
                    debug("Response API response received");
                    return parse(json, ResponseResult.class, "response");
                });
    }

    private void validateRequest(ResponseRequest request) {
        if (OpenClawStrings.isBlank(request.getAgent()) && OpenClawStrings.isBlank(request.getModel())) {
            String msg = "Response request requires either 'agent' or 'model' field. " +
                    "Use 'agent' for OpenClaw routing (e.g., 'openclaw/default'), " +
                    "or 'model' for direct backend model.";
            warn(msg);
            throw new IllegalArgumentException(msg);
        }

        if (request.getInput() == null) {
            warn("Response request has no input");
            throw new IllegalArgumentException("Response request requires input");
        }
    }

    private Map<String, String> buildHeaders(ResponseRequest request) {
        Map<String, String> headers = new HashMap<>();

        if (request.getModel() != null && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
            debug("Added {} header: {}", OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }

        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(ResponseRequest request) {
        if (request.getAgent() != null) {
            debug("Using agent as model: {}", request.getAgent());
            return request.getAgent();
        }
        debug("Using model: {}", request.getModel());
        return request.getModel();
    }
}
