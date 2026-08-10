package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.api.model.HookRequest;
import io.github.easy4j.openclaw.api.model.HookResponse;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * OpenClaw SDK 的 `OpenClawWebhookClient` 类型，封装其公开契约和生命周期边界。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class OpenClawWebhookClient extends OpenClawHttpClient {

    /**
     * HTTP JSON 负载使用的媒体类型定义。
     */
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    /**
     * OpenClaw 协议固定值 {@code new ObjectMapper()}；调用方不应在运行时修改。
     */
    private static final ObjectMapper RESPONSE_MAPPER = new ObjectMapper();

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param mapper 写入 `mapper` 协议字段的内容
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper) {
        super(config, mapper, null);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 创建客户端并保存传入依赖；外部注入的 OkHttpClient 与 ObjectMapper 仍由调用方管理。
     *
     * @param config SDK 配置
     * @param mapper 写入 `mapper` 协议字段的内容
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper, OkHttpClient httpClient) {
        super(config, mapper, httpClient);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 请求对象
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HookResponse
     */
    public HookResponse postHooksAgent(HookRequest request) {
        return awaitFuture(postHooksAgentAsync(request));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `postHooksAgent`，调用线程不会等待远程响应。
     *
     * @param request 请求对象
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<HookResponse> postHooksAgentAsync(HookRequest request) {
        Objects.requireNonNull(request, "request");
        Map<String, Object> body = buildHooksAgentBody(request);
        return postWebhookAsync(resolveHooksSubPath("agent"), body).thenApply(response -> {
            HookResponse result = new HookResponse();
            result.setHttpStatus(response.getStatus());
            result.setRawBody(response.getBody());
            result.setLocalInvocation(false);
            result.setSuccess(parseOk(response.getBody()));
            result.setRunId(parseRunId(response.getBody()));
            return result;
        });
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param text 写入 `text` 协议字段的内容
     * @param mode 写入 `mode` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public String postHooksWake(String text, String mode) {
        return awaitFuture(postHooksWakeAsync(text, mode));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `postHooksWake`，调用线程不会等待远程响应。
     *
     * @param text 写入 `text` 协议字段的内容
     * @param mode 写入 `mode` 协议字段的内容
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     */
    public CompletableFuture<String> postHooksWakeAsync(String text, String mode) {
        if (OpenClawStrings.isBlank(text)) {
            throw new IllegalArgumentException("webhooks wake: text is required");
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("text", text);
        body.put("mode", OpenClawStrings.isBlank(mode) ? "now" : mode);
        return postWebhookAsync(resolveHooksSubPath("wake"), body).thenApply(HttpResult::getBody);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param hookName 写入 `hookName` 协议字段的内容
     * @param payload 写入 `payload` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public String postMappedHook(String hookName, Map<String, Object> payload) {
        return awaitFuture(postMappedHookAsync(hookName, payload));
    }

    /**
     * 使用 OkHttp/WebSocket 的异步机制发起 `postMappedHook`，调用线程不会等待远程响应。
     *
     * @param hookName 写入 `hookName` 协议字段的内容
     * @param payload 写入 `payload` 协议字段的内容
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<String> postMappedHookAsync(String hookName, Map<String, Object> payload) {
        String normalized = normalizeHookName(hookName);
        Map<String, Object> body = payload != null ? payload : Collections.emptyMap();
        return postWebhookAsync(resolveHooksSubPath(normalized), body).thenApply(HttpResult::getBody);
    }

    /**
     * 调用 OpenClaw 的 `buildHooksAgentBody` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param request 请求对象
     * @return 键名与 OpenClaw JSON/CLI 协议一致的映射
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     */
    public static Map<String, Object> buildHooksAgentBody(HookRequest request) {
        Objects.requireNonNull(request, "request");
        if (OpenClawStrings.isBlank(request.getMessage())) {
            throw new IllegalArgumentException("hooks/agent: message is required");
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", request.getMessage());
        if (OpenClawStrings.isNotBlank(request.getAgentId())) {
            body.put("agentId", request.getAgentId());
        }
        body.put("name", OpenClawStrings.isNotBlank(request.getName()) ? request.getName() : "Generation");
        body.put("wakeMode", OpenClawStrings.isNotBlank(request.getWakeMode()) ? request.getWakeMode() : "now");
        body.put("timeoutSeconds", request.getTimeoutSeconds());
        if (OpenClawStrings.isNotBlank(request.getSessionKey())) body.put("sessionKey", request.getSessionKey());
        if (request.getDeliver() != null) body.put("deliver", request.getDeliver());
        if (OpenClawStrings.isNotBlank(request.getChannel())) body.put("channel", request.getChannel());
        if (OpenClawStrings.isNotBlank(request.getTo())) body.put("to", request.getTo());
        if (OpenClawStrings.isNotBlank(request.getModel())) body.put("model", request.getModel());
        if (OpenClawStrings.isNotBlank(request.getThinking())) body.put("thinking", request.getThinking());
        return body;
    }

    private String resolveHooksSubPath(String subPath) {
        String base = config.resolveHooksPath();
        String child = subPath != null ? subPath.trim() : "";
        if (child.startsWith("/")) child = child.substring(1);
        return child.isEmpty() ? base : base + "/" + child;
    }

    private CompletableFuture<HttpResult> postWebhookAsync(String hookPath, Map<String, Object> body) {
        String base = config.getBaseUrl();
        if (OpenClawStrings.isBlank(base)) throw new OpenClawHttpException("OpenClaw gatewayBaseUrl is empty", null);
        String url = base.replaceAll("/+$", "") + hookPath;
        String token = config.resolveHooksBearerToken();
        try {
            Request.Builder builder = new Request.Builder().url(url).header("Content-Type", "application/json");
            if (OpenClawStrings.isNotBlank(token)) {
                if (config.isHooksUseXOpenclawTokenHeader()) {
                    builder.header(OpenClawConstants.HEADER_X_OPENCLAW_TOKEN, token);
                } else {
                    builder.header("Authorization", "Bearer " + token);
                }
            }
            Request request = builder.post(RequestBody.create(objectMapper.writeValueAsString(body), JSON)).build();
            return executeResponseAsync(request, null).thenApply(response -> {
                int status = response.getStatusCode();
                String respBody = response.getBody();
                if (status < 200 || status >= 300) {
                    throw new OpenClawHttpException("OpenClaw webhook returned status " + status, status, respBody);
                }
                return new HttpResult(status, respBody);
            });
        } catch (Exception e) {
            CompletableFuture<HttpResult> failed = new CompletableFuture<>();
            failed.completeExceptionally(e instanceof OpenClawHttpException ? e
                    : new OpenClawHttpException("OpenClaw webhook invoke failed: " + e.getMessage(), e));
            return failed;
        }
    }

    /**
     * 调用 OpenClaw 的 `normalizeHookName` API，并复用统一认证、序列化、取消和异常处理。
     *
     * @param hookName 写入 `hookName` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     */
    public static String normalizeHookName(String hookName) {
        if (OpenClawStrings.isBlank(hookName)) throw new IllegalArgumentException("hookName is required");
        String normalized = hookName.trim();
        if (normalized.startsWith("/")) normalized = normalized.substring(1);
        if (normalized.startsWith("hooks/")) normalized = normalized.substring("hooks/".length());
        if (!normalized.matches("[A-Za-z0-9._-]+"))
            throw new IllegalArgumentException("hookName contains illegal characters: " + hookName);
        return normalized;
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param body JSON 请求体或响应体文本
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean parseOk(String body) {
        if (body == null || body.isEmpty()) return false;
        try {
            JsonNode root = RESPONSE_MAPPER.readTree(body);
            if (root.has("ok")) return root.get("ok").asBoolean(false);
        } catch (Exception ignored) {}
        return body.contains("\"ok\":true");
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param body JSON 请求体或响应体文本
     * @return 可用于关联后续请求的标识
     */
    public static String parseRunId(String body) {
        if (body == null || body.isEmpty()) return null;
        try {
            JsonNode root = RESPONSE_MAPPER.readTree(body);
            if (root.hasNonNull("runId")) {
                JsonNode runId = root.get("runId");
                return runId.isNull() ? null : runId.asText();
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * OpenClaw SDK 的 `HttpResult` 类型，封装其公开契约和生命周期边界。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static final class HttpResult {
        /**
         * `HttpResult` 生命周期内保存的 `status` 对应状态。
         */
        private final int status;
        /**
         * JSON 请求体或响应体文本。
         */
        private final String body;
        private HttpResult(int status, String body) { this.status = status; this.body = body; }
        int getStatus() { return status; }
        String getBody() { return body; }
    }
}
