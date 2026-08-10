package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * OpenClaw SDK 的 `OpenClawToolInvokeClient` 类型，封装其公开契约和生命周期边界。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawToolInvokeClient extends OpenClawHttpClient {

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param objectMapper JSON 映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    /**
     * 调用 OpenClaw 的 `invoke` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ToolInvokeResult
     */
    public ToolInvokeResult invoke(ToolInvokeRequest request) {
        return invoke(request, null);
    }

    /**
     * 调用 OpenClaw 的 `invoke` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @param cancellation 可选调用取消令牌
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 ToolInvokeResult
     */
    public ToolInvokeResult invoke(ToolInvokeRequest request, HttpCallCancellation cancellation) {
        return awaitFuture(invokeAsync(request, cancellation));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `invoke`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
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
        debug("args: {}", request.getArgs());

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
                debug("Tool invoke response body: {}", respBody);

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
     * 使用 OkHttp/WebSocket 的异步机制发起 `invoke`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<ToolInvokeResult> invokeAsync(ToolInvokeRequest request) {
        return invokeAsync(request, null);
    }
}
