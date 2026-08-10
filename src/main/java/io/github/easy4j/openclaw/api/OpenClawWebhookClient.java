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
 * 访问 Agent、Wake 和自定义 Hook Webhook 端点的客户端。
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
     * 仅用于解析 Webhook 响应 JSON 的共享 ObjectMapper。
     */
    private static final ObjectMapper RESPONSE_MAPPER = new ObjectMapper();

    /**
     * 构造端点客户端并复用认证、JSON 映射和 OkHttp 连接资源；外部注入的客户端不随当前对象关闭。
     *
     * @param config SDK 配置
     * @param mapper 用于 JSON 序列化与反序列化的映射器
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper) {
        super(config, mapper, null);
    }

    /**
     * 构造端点客户端并复用认证、JSON 映射和 OkHttp 连接资源；外部注入的客户端不随当前对象关闭。
     *
     * @param config SDK 配置
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    /**
     * 构造端点客户端并复用认证、JSON 映射和 OkHttp 连接资源；外部注入的客户端不随当前对象关闭。
     *
     * @param config SDK 配置
     * @param mapper 用于 JSON 序列化与反序列化的映射器
     * @param httpClient 复用连接池和 Dispatcher 的 OkHttpClient
     */
    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper, OkHttpClient httpClient) {
        super(config, mapper, httpClient);
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 要校验、序列化并发送的 {@code HookRequest}
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 HookResponse
     */
    public HookResponse postHooksAgent(HookRequest request) {
        return awaitFuture(postHooksAgentAsync(request));
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code postHooksAgent}，调用线程不等待远端响应。
     *
     * @param request 要校验、序列化并发送的 {@code HookRequest}
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
     * @param text 发送给默认智能体的唤醒文本
     * @param mode Wake Hook 使用的唤醒模式；未指定时可为 {@code null}
     * @return Wake Webhook 返回的响应正文
     */
    public String postHooksWake(String text, String mode) {
        return awaitFuture(postHooksWakeAsync(text, mode));
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code postHooksWake}，调用线程不等待远端响应。
     *
     * @param text 发送给默认智能体的唤醒文本
     * @param mode Wake Hook 使用的唤醒模式；未指定时可为 {@code null}
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
     * @param hookName 目标 Hook 的注册名称
     * @param payload 序列化为目标 Hook 请求体的键值负载
     * @return 指定 Webhook 返回的响应正文
     */
    public String postMappedHook(String hookName, Map<String, Object> payload) {
        return awaitFuture(postMappedHookAsync(hookName, payload));
    }

    /**
     * 通过 OkHttp Dispatcher 异步执行 {@code postMappedHook}，调用线程不等待远端响应。
     *
     * @param hookName 目标 Hook 的注册名称
     * @param payload 序列化为目标 Hook 请求体的键值负载
     * @return 在远程响应、取消或失败时完成的 CompletableFuture
     */
    public CompletableFuture<String> postMappedHookAsync(String hookName, Map<String, Object> payload) {
        String normalized = normalizeHookName(hookName);
        Map<String, Object> body = payload != null ? payload : Collections.emptyMap();
        return postWebhookAsync(resolveHooksSubPath(normalized), body).thenApply(HttpResult::getBody);
    }

    /**
     * 根据智能体标识、消息和扩展负载构造 Agent Hook 请求体。
     *
     * @param request 要校验、序列化并发送的 {@code HookRequest}
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
     * 去除 Hook 名称两端斜杠并拒绝空名称，确保可安全拼接端点路径。
     *
     * @param hookName 目标 Hook 的注册名称
     * @return 去除首尾斜杠的 Hook 路径段
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
     * @param body Hook 返回的原始 JSON 响应正文
     * @return 响应 JSON 中 {@code ok} 为布尔真时返回 {@code true}
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
     * @param body Hook 返回的原始 JSON 响应正文
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
     * Webhook 响应在底层 Response 关闭后保留的状态码与响应体快照。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static final class HttpResult {
        /**
         * Webhook HTTP 请求返回的状态码。
         */
        private final int status;
        /**
         * 已完整读取的 Hook HTTP 响应正文；响应体为空时为空字符串。
         */
        private final String body;
        private HttpResult(int status, String body) { this.status = status; this.body = body; }
        int getStatus() { return status; }
        String getBody() { return body; }
    }
}
