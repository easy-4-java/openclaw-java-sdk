package io.github.easy4j.openclaw;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.api.model.ThinkOption;
import io.github.easy4j.openclaw.cli.opts.ThinkingLevel;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import io.github.easy4j.openclaw.ws.protocol.ConnectParams;
import io.github.easy4j.openclaw.ws.protocol.SessionsSendParams;
import io.github.easy4j.openclaw.ws.protocol.params.ChatHistoryParams;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 对照 OpenClaw 2026.7.1-2 HTTP 与 Gateway 协议 Schema，验证 SDK 请求参数不会在建模或序列化时丢失。
 */
class OpenClawRequestParameterCoverageTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldSerializeEveryChatSendParameterSupportedByGatewaySchema() {
        Map<String, Object> attachment = Collections.<String, Object>singletonMap("type", "image");
        ChatSendParams.SystemInputProvenance provenance = ChatSendParams.SystemInputProvenance.builder()
                .kind("external_user")
                .originSessionId("origin-session")
                .sourceSessionKey("agent:source:main")
                .sourceChannel("web")
                .sourceTool("cloud-agents")
                .build();

        Map<String, Object> params = ChatSendParams.builder()
                .sessionKey("agent:ops:main")
                .agentId("ops")
                .sessionId("session-id")
                .message("hello")
                .thinkingLevel(ThinkingLevel.MINIMAL)
                .fastMode("auto")
                .fastAutoOnSeconds(8)
                .deliver(false)
                .originatingChannel("web")
                .originatingTo("user-1")
                .originatingAccountId("account-1")
                .originatingThreadId("thread-1")
                .attachments(Collections.<Object>singletonList(attachment))
                .timeoutMs(30_000)
                .systemInputProvenance(provenance)
                .systemProvenanceReceipt("receipt")
                .suppressCommandInterpretation(true)
                .expectedSessionRoutingContract("contract-v1")
                .idempotencyKey("turn-1")
                .build()
                .toParamsMap();

        assertEquals("agent:ops:main", params.get("sessionKey"));
        assertEquals("ops", params.get("agentId"));
        assertEquals("session-id", params.get("sessionId"));
        assertEquals("hello", params.get("message"));
        assertEquals("minimal", params.get("thinking"));
        assertEquals("auto", params.get("fastMode"));
        assertEquals(8, params.get("fastAutoOnSeconds"));
        assertEquals(false, params.get("deliver"));
        assertEquals("web", params.get("originatingChannel"));
        assertEquals("user-1", params.get("originatingTo"));
        assertEquals("account-1", params.get("originatingAccountId"));
        assertEquals("thread-1", params.get("originatingThreadId"));
        assertEquals(Collections.singletonList(attachment), params.get("attachments"));
        assertEquals(30_000, params.get("timeoutMs"));
        assertEquals("receipt", params.get("systemProvenanceReceipt"));
        assertEquals(true, params.get("suppressCommandInterpretation"));
        assertEquals("contract-v1", params.get("expectedSessionRoutingContract"));
        assertEquals("turn-1", params.get("idempotencyKey"));

        @SuppressWarnings("unchecked")
        Map<String, Object> provenanceMap = (Map<String, Object>) params.get("systemInputProvenance");
        assertEquals("external_user", provenanceMap.get("kind"));
        assertEquals("origin-session", provenanceMap.get("originSessionId"));
        assertEquals("agent:source:main", provenanceMap.get("sourceSessionKey"));
        assertEquals("web", provenanceMap.get("sourceChannel"));
        assertEquals("cloud-agents", provenanceMap.get("sourceTool"));
    }

    @Test
    void shouldSerializeEveryConnectParameterSupportedByGatewaySchema() {
        ConnectParams.DeviceInfo device = new ConnectParams.DeviceInfo(
                "device-1", "public-key", "signature", 1L, "nonce");
        ConnectParams.AuthInfo auth = ConnectParams.AuthInfo.builder()
                .token("token")
                .bootstrapToken("bootstrap")
                .deviceToken("device-token")
                .password("password")
                .approvalRuntimeToken("approval-token")
                .agentRuntimeIdentityToken("runtime-token")
                .build();
        ConnectParams params = ConnectParams.builder()
                .minProtocol(4)
                .maxProtocol(4)
                .client(ConnectParams.ClientInfo.builder()
                        .id("gateway-client")
                        .displayName("OpenClaw Java SDK")
                        .version("1.0.0")
                        .platform("java")
                        .deviceFamily("server")
                        .modelIdentifier("jvm")
                        .mode("backend")
                        .instanceId("instance-1")
                        .build())
                .caps(Collections.singletonList("chat"))
                .commands(Collections.singletonList("chat.send"))
                .permissions(Collections.singletonMap("microphone", true))
                .pathEnv("/usr/bin")
                .role("operator")
                .scopes(Arrays.asList("operator.read", "operator.write"))
                .device(device)
                .auth(auth)
                .locale("zh-CN")
                .userAgent("openclaw-java-sdk/1.0.0")
                .build();

        Map<String, Object> map = params.toParamsMap();
        assertEquals(4, map.get("minProtocol"));
        assertEquals(Collections.singletonList("chat"), map.get("caps"));
        assertEquals(Collections.singletonList("chat.send"), map.get("commands"));
        assertEquals(Collections.singletonMap("microphone", true), map.get("permissions"));
        assertEquals("/usr/bin", map.get("pathEnv"));
        assertEquals("operator", map.get("role"));
        assertEquals(Arrays.asList("operator.read", "operator.write"), map.get("scopes"));
        assertEquals("zh-CN", map.get("locale"));
        assertEquals("openclaw-java-sdk/1.0.0", map.get("userAgent"));

        @SuppressWarnings("unchecked")
        Map<String, Object> client = (Map<String, Object>) map.get("client");
        assertEquals("server", client.get("deviceFamily"));
        assertEquals("jvm", client.get("modelIdentifier"));
        assertEquals("instance-1", client.get("instanceId"));

        @SuppressWarnings("unchecked")
        Map<String, Object> authMap = (Map<String, Object>) map.get("auth");
        assertEquals("bootstrap", authMap.get("bootstrapToken"));
        assertEquals("device-token", authMap.get("deviceToken"));
        assertEquals("approval-token", authMap.get("approvalRuntimeToken"));
        assertEquals("runtime-token", authMap.get("agentRuntimeIdentityToken"));

        @SuppressWarnings("unchecked")
        Map<String, Object> deviceMap = (Map<String, Object>) map.get("device");
        assertEquals("public-key", deviceMap.get("publicKey"));
        assertEquals("nonce", deviceMap.get("nonce"));
    }

    @Test
    void shouldSerializeEverySessionsSendAndChatHistoryParameter() throws Exception {
        Map<String, Object> params = SessionsSendParams.builder()
                .key("agent:ops:main")
                .agentId("ops")
                .message("hello")
                .thinkingLevel(ThinkingLevel.OFF)
                .attachments(Collections.<Object>singletonList("attachment"))
                .timeoutMs(30_000)
                .idempotencyKey("turn-2")
                .build()
                .toParamsMap();

        assertEquals("ops", params.get("agentId"));
        assertEquals("off", params.get("thinking"));
        assertEquals(Collections.singletonList("attachment"), params.get("attachments"));
        assertEquals("turn-2", params.get("idempotencyKey"));

        JsonNode history = mapper.valueToTree(ChatHistoryParams.builder()
                .sessionKey("agent:ops:main")
                .agentId("ops")
                .limit(20)
                .offset(10)
                .maxChars(40_000)
                .build());
        assertEquals("ops", history.path("agentId").asText());
        assertEquals(10, history.path("offset").asInt());
    }

    @Test
    void shouldSerializeResponsesCompatibilityParametersAcceptedByOpenClaw() throws Exception {
        Map<String, String> metadata = Collections.singletonMap("traceId", "trace-1");
        Map<String, Object> reasoning = new LinkedHashMap<String, Object>();
        reasoning.put("effort", "minimal");

        ResponseRequest request = ResponseRequest.builder()
                .agent("openclaw/default")
                .input("hello")
                .maxToolCalls(3)
                .reasoning(reasoning)
                .metadata(metadata)
                .store(false)
                .truncation("auto")
                .build();

        JsonNode json = mapper.valueToTree(request);
        assertEquals(3, json.path("max_tool_calls").asInt());
        assertEquals("minimal", json.path("reasoning").path("effort").asText());
        assertEquals("trace-1", json.path("metadata").path("traceId").asText());
        assertFalse(json.path("store").asBoolean(true));
        assertEquals("auto", json.path("truncation").asText());

        JsonNode image = mapper.valueToTree(ResponseRequest.InputItem.imageBase64("image-data").build());
        assertEquals("image-data", image.path("source").path("data").asText());
        assertFalse(image.path("source").has("url"));

        JsonNode file = mapper.valueToTree(
                ResponseRequest.InputItem.fileBase64("file-data", "text/plain").build());
        assertEquals("file-data", file.path("source").path("data").asText());
        assertEquals("text/plain", file.path("source").path("media_type").asText());
    }

    @Test
    void shouldExposeEveryOpenClawThinkingLevel() {
        assertEquals(
                Arrays.asList("off", "minimal", "low", "medium", "high", "xhigh", "adaptive", "max", "ultra"),
                Arrays.asList(ThinkingLevel.values()).stream()
                        .map(ThinkingLevel::cliValue)
                        .collect(java.util.stream.Collectors.toList()));
        assertEquals("minimal", ThinkOption.ThinkLevel.MINIMAL.toJsonValue());
        assertEquals("adaptive", ThinkOption.ThinkLevel.ADAPTIVE.toJsonValue());
        assertEquals("max", ThinkOption.ThinkLevel.MAX.toJsonValue());
        assertEquals("ultra", ThinkOption.ThinkLevel.ULTRA.toJsonValue());
    }

    @Test
    void shouldOmitUnsetOptionalGatewayParameters() {
        Map<String, Object> params = ChatSendParams.builder()
                .sessionKey("agent:ops:main")
                .message("hello")
                .build()
                .toParamsMap();

        assertFalse(params.containsKey("thinking"));
        assertFalse(params.containsKey("fastMode"));
        assertFalse(params.containsKey("idempotencyKey"));
        assertTrue(params.containsKey("message"));

        assertEquals(Arrays.asList("operator.read", "operator.write"),
                new OpenClawHttpClientConfig().getGatewayScopes());
    }
}
