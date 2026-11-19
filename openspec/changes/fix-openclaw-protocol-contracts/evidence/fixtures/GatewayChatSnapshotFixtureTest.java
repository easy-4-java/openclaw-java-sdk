package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class GatewayChatSnapshotFixtureTest {
    @Test
    void recordsReplacementAndFinalSnapshotForTheRequestedRun() throws Exception {
        URI endpoint = URI.create(Objects.requireNonNull(System.getenv("SDK_PROBE_URL")));
        assertEquals("ws", endpoint.getScheme());
        assertEquals("127.0.0.1", endpoint.getHost());

        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(endpoint.toString().replaceFirst("^ws:", "http:"));
        config.setGatewayAuthToken(Objects.requireNonNull(System.getenv("OPENCLAW_GATEWAY_TOKEN")));
        config.setGatewayClientId("gateway-client");
        config.setGatewayClientMode("backend");
        config.setGatewayScopes(Arrays.asList("operator.read", "operator.write"));

        CountDownLatch finalReceived = new CountDownLatch(1);
        List<Map<?, ?>> chatEvents = new ArrayList<>();
        String runId = "sdk-chat-snapshot-" + UUID.randomUUID();
        String sessionKey = "agent:main:" + runId;
        try (OpenClawGatewayWsClient client = new OpenClawGatewayWsClient(config, endpoint)) {
            client.addListener(new OpenClawWsListener() {
                @Override
                public void onEvent(EventFrame frame) {
                    if (!"chat".equals(frame.getEvent()) || finalReceived.getCount() == 0) {
                        return;
                    }
                    if (!(frame.getPayload() instanceof Map)) {
                        return;
                    }
                    Map<?, ?> payload = (Map<?, ?>) frame.getPayload();
                    if (!runId.equals(payload.get("runId"))) {
                        return;
                    }
                    chatEvents.add(payload);
                    if ("final".equals(payload.get("state"))) {
                        finalReceived.countDown();
                    }
                }
            });
            assertEquals(4, client.connectHandshake().getProtocol());
            client.chatSend(ChatSendParams.builder()
                    .sessionKey(sessionKey)
                    .message("Return the local fixture response.")
                    .idempotencyKey(runId)
                    .timeoutMs(30000)
                    .build(), new ChatStreamHandler() {
                @Override public void onDelta(String text) { }
                @Override public void onComplete(String text) { }
                @Override public void onError(String message) { }
            });
            assertTrue(finalReceived.await(45, TimeUnit.SECONDS), "Expected the selected run's final frame");
        }

        StringBuilder appendText = new StringBuilder();
        List<String> replacements = new ArrayList<>();
        for (Map<?, ?> event : chatEvents) {
            assertEquals(sessionKey, event.get("sessionKey"));
            if ("delta".equals(event.get("state")) && event.get("deltaText") instanceof String) {
                String text = (String) event.get("deltaText");
                if (Boolean.TRUE.equals(event.get("replace"))) {
                    replacements.add(text);
                } else {
                    appendText.append(text);
                }
            }
        }
        Map<?, ?> terminal = chatEvents.get(chatEvents.size() - 1);
        Map<?, ?> message = (Map<?, ?>) terminal.get("message");
        List<?> blocks = (List<?>) message.get("content");
        List<String> finalText = new ArrayList<>();
        for (Object value : blocks) {
            Map<?, ?> block = (Map<?, ?>) value;
            if ("text".equals(block.get("type"))) {
                finalText.add((String) block.get("text"));
            }
        }
        System.out.printf("chat snapshot append=%s replacementCount=%d replacementLengths=%s finalText=%s%n",
                appendText, replacements.size(), replacements.stream().map(String::length)
                        .collect(java.util.stream.Collectors.toList()), finalText);
        assertEquals("Gateway fixture ✓", appendText.toString());
        assertEquals(Arrays.asList("Gateway fixture ✓"), finalText);
        if ("candidate".equals(System.getenv("SDK_FIXTURE_VERSION"))) {
            assertEquals(Arrays.asList(""), replacements);
        } else {
            assertTrue(replacements.isEmpty());
        }
    }
}
