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
 * OpenClaw Gateway HTTP Webhooks client({@code /hooks/*}).
 * <p> OkHttp, {@link OkHttpClient}.</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Slf4j
public class OpenClawWebhookClient extends OpenClawHttpClient {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final ObjectMapper RESPONSE_MAPPER = new ObjectMapper();

    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper) {
        super(config, mapper, null);
    }

    public OpenClawWebhookClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    public OpenClawWebhookClient(OpenClawHttpClientConfig config, ObjectMapper mapper, OkHttpClient httpClient) {
        super(config, mapper, httpClient);
    }

    public HookResponse postHooksAgent(HookRequest request) {
        return awaitFuture(postHooksAgentAsync(request));
    }

    /** 异步触发 Agent webhook。 */
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

    public String postHooksWake(String text, String mode) {
        return awaitFuture(postHooksWakeAsync(text, mode));
    }

    /** 异步触发 wake webhook。 */
    public CompletableFuture<String> postHooksWakeAsync(String text, String mode) {
        if (OpenClawStrings.isBlank(text)) {
            throw new IllegalArgumentException("webhooks wake: text is required");
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("text", text);
        body.put("mode", OpenClawStrings.isBlank(mode) ? "now" : mode);
        return postWebhookAsync(resolveHooksSubPath("wake"), body).thenApply(HttpResult::getBody);
    }

    public String postMappedHook(String hookName, Map<String, Object> payload) {
        return awaitFuture(postMappedHookAsync(hookName, payload));
    }

    /** 异步触发映射 webhook。 */
    public CompletableFuture<String> postMappedHookAsync(String hookName, Map<String, Object> payload) {
        String normalized = normalizeHookName(hookName);
        Map<String, Object> body = payload != null ? payload : Collections.emptyMap();
        return postWebhookAsync(resolveHooksSubPath(normalized), body).thenApply(HttpResult::getBody);
    }

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

    public static String normalizeHookName(String hookName) {
        if (OpenClawStrings.isBlank(hookName)) throw new IllegalArgumentException("hookName is required");
        String normalized = hookName.trim();
        if (normalized.startsWith("/")) normalized = normalized.substring(1);
        if (normalized.startsWith("hooks/")) normalized = normalized.substring("hooks/".length());
        if (!normalized.matches("[A-Za-z0-9._-]+"))
            throw new IllegalArgumentException("hookName contains illegal characters: " + hookName);
        return normalized;
    }

    public static boolean parseOk(String body) {
        if (body == null || body.isEmpty()) return false;
        try {
            JsonNode root = RESPONSE_MAPPER.readTree(body);
            if (root.has("ok")) return root.get("ok").asBoolean(false);
        } catch (Exception ignored) {}
        return body.contains("\"ok\":true");
    }

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

    private static final class HttpResult {
        private final int status;
        private final String body;
        private HttpResult(int status, String body) { this.status = status; this.body = body; }
        int getStatus() { return status; }
        String getBody() { return body; }
    }
}
