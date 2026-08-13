package io.github.easy4j.openclaw.api;

import tools.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.ToolInvokeRequest;
import io.github.easy4j.openclaw.api.model.ToolInvokeResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 通过 HTTP 调用 OpenClaw 工具并解析成功或错误结果的客户端。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawToolInvokeClient extends OpenClawHttpClient {

    /**
     * 使用默认映射器创建工具调用客户端，并复用调用方提供的 OkHttp 连接资源。
     *
     * @param config SDK 配置
     */
    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 创建工具调用客户端，并复用调用方提供的映射器和 OkHttp 连接资源。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    /**
     * 向工具调用端点发送请求，并按同步或异步入口解析结果。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @return 工具执行成功输出或 Gateway 返回的结构化错误
     */
    public ToolInvokeResult invoke(ToolInvokeRequest request) {
        return invoke(request, null);
    }

    /**
     * 向工具调用端点发送请求，并按同步或异步入口解析结果。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @param cancellation 可选调用取消令牌
     * @return 工具执行成功输出或 Gateway 返回的结构化错误
     */
    public ToolInvokeResult invoke(ToolInvokeRequest request, HttpCallCancellation cancellation) {
        return awaitFuture(invokeAsync(request, cancellation));
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code invoke}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @param cancellation 可选调用取消令牌
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     * @throws OpenClawHttpException 远程响应、协议解析或本地执行失败时抛出
     */
    public CompletableFuture<ToolInvokeResult> invokeAsync(ToolInvokeRequest request,
                                                           HttpCallCancellation cancellation) {
        Objects.requireNonNull(request, "request");

        debug("=== Tool Invoke Request ===");
        debug("tool: {}", request.getTool());
        debug("action: {}", request.getAction());
        debug("BODY", "args: {}", request.getArgs());

        if (OpenClawStrings.isBlank(request.getTool())) {
            String msg = "Tool name is required";
            warn(msg);
            throw new IllegalArgumentException(msg);
        }

        try {
            Request.Builder builder = authedBuilder(resolveUrl(OpenClawConstants.ENDPOINT_TOOLS_INVOKE));
            Request httpRequest = builder.post(RequestBody.create(objectMapper.writeValueAsString(request), JSON)).build();

            debug("Sending tool invoke request...");
            return executeResponseAsync(httpRequest, cancellation).thenApply(response -> {
                int status = response.getStatusCode();
                String respBody = response.getBody();
                debug("Tool invoke response status: {}", status);
                debug("BODY", "Tool invoke response body: {}", truncate(respBody));

                if (status == 404) {
                    String msg = "Tool not available: " + request.getTool();
                    warn(msg);
                    ToolInvokeResult result = new ToolInvokeResult();
                    result.setOk(false);
                    ToolInvokeResult.ErrorDetail error = new ToolInvokeResult.ErrorDetail();
                    error.setType(ToolInvokeResult.ERROR_TYPE_NOT_FOUND);
                    error.setMessage(msg);
                    result.setError(error);
                    return result;
                }

                if (status < 200 || status >= 300) {
                    throw new OpenClawHttpException("POST /tools/invoke returned status " + status, status, respBody);
                }

                ToolInvokeResult result = parse(respBody, ToolInvokeResult.class);
                debug("Tool invoke success, ok: {}", result.getOk());
                return result;
            });
        } catch (Exception e) {
            CompletableFuture<ToolInvokeResult> failed = new CompletableFuture<>();
            failed.completeExceptionally(e instanceof OpenClawHttpException ? e
                    : new OpenClawHttpException("POST /tools/invoke failed: " + e.getMessage(), e));
            return failed;
        }
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code invoke}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code ToolInvokeRequest}
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ToolInvokeResult> invokeAsync(ToolInvokeRequest request) {
        return invokeAsync(request, null);
    }
}
