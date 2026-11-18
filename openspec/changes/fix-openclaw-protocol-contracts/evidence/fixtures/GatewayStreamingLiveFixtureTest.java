package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class GatewayStreamingLiveFixtureTest {
    @Test
    void reconstructsStreamingTextAndCompletesOnce() throws Exception {
        URI endpoint = URI.create(Objects.requireNonNull(System.getenv("SDK_PROBE_URL")));
        assertEquals("ws", endpoint.getScheme());
        assertEquals("127.0.0.1", endpoint.getHost());
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(endpoint.toString().replaceFirst("^ws:", "http:"));
        config.setGatewayAuthToken(Objects.requireNonNull(System.getenv("OPENCLAW_GATEWAY_TOKEN")));
        config.setGatewayClientId("gateway-client");
        config.setGatewayClientMode("backend");
        config.setGatewayScopes(Arrays.asList("operator.read", "operator.write"));

        CountDownLatch terminal = new CountDownLatch(1);
        StringBuffer deltas = new StringBuffer();
        AtomicInteger fragments = new AtomicInteger();
        AtomicInteger foreignDeltasBeforeTerminal = new AtomicInteger();
        AtomicInteger completions = new AtomicInteger();
        AtomicReference<String> finalText = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();
        String turn = "sdk-stream-fixture-" + UUID.randomUUID();
        try (OpenClawGatewayWsClient client = new OpenClawGatewayWsClient(config, endpoint)) {
            client.addListener(new OpenClawWsListener() {
                @Override
                public void onEvent(EventFrame frame) {
                    if ("chat".equals(frame.getEvent()) && frame.getPayload() instanceof Map) {
                        Map<?, ?> payload = (Map<?, ?>) frame.getPayload();
                        if ("delta".equals(payload.get("state"))) {
                            if (!turn.equals(payload.get("runId")) && terminal.getCount() > 0) {
                                foreignDeltasBeforeTerminal.incrementAndGet();
                            }
                        }
                    }
                }
            });
            assertEquals(4, client.connectHandshake().getProtocol());
            client.chatSend(ChatSendParams.builder()
                    .sessionKey("agent:main:" + turn)
                    .message("Return the local fixture response.")
                    .idempotencyKey(turn)
                    .timeoutMs(30000)
                    .build(), new ChatStreamHandler() {
                @Override public void onDelta(String text) {
                    deltas.append(text);
                    fragments.incrementAndGet();
                }

                @Override public void onComplete(String text) {
                    finalText.set(text);
                    completions.incrementAndGet();
                    terminal.countDown();
                }

                @Override public void onError(String message) {
                    error.set(message);
                    terminal.countDown();
                }
            });

            assertTrue(terminal.await(45, TimeUnit.SECONDS), "Gateway chat did not reach a terminal state");
            assertNull(error.get(), error.get());
            assertEquals("Gateway fixture ✓", finalText.get());
            assertEquals("Gateway fixture ✓", deltas.toString());
            assertTrue(fragments.get() >= 1);
            assertTrue(foreignDeltasBeforeTerminal.get() > 0, "Expected an overlapping foreign run");
            assertEquals(1, completions.get());
        }
    }
}
