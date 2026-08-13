package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.cli.opts.ThinkingLevel;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 本机真实 OpenClaw Gateway 协议验收测试；仅在显式开启系统属性时执行，默认构建不会调用模型。
 */
class OpenClawGatewayRealIntegrationTest {

    @Test
    void shouldSendAllTurnOverridesToRealOpenClawGateway() throws Exception {
        Assumptions.assumeTrue(Boolean.getBoolean("openclaw.realIntegration"));
        String gatewayUrl = System.getProperty("openclaw.gatewayUrl", "ws://127.0.0.1:18789");
        String agentId = System.getProperty("openclaw.agentId", "ops");

        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(gatewayUrl.replaceFirst("^ws", "http"));
        config.setGatewayScopes(Arrays.asList("operator.read", "operator.write", "operator.admin"));
        CountDownLatch terminal = new CountDownLatch(1);
        AtomicReference<String> response = new AtomicReference<String>("");
        AtomicReference<String> error = new AtomicReference<String>();

        try (OpenClawGatewayWsClient client = new OpenClawGatewayWsClient(config, URI.create(gatewayUrl))) {
            client.connectHandshake();
            String turnId = "sdk-real-" + UUID.randomUUID().toString();
            client.chatSend(ChatSendParams.builder()
                            .sessionKey("agent:" + agentId + ":" + turnId)
                            .agentId(agentId)
                            .message("只回复 OK")
                            .thinkingLevel(ThinkingLevel.MINIMAL)
                            .fastMode("auto")
                            .fastAutoOnSeconds(10)
                            .suppressCommandInterpretation(true)
                            .idempotencyKey(turnId)
                            .timeoutMs(60_000)
                            .build(),
                    new ChatStreamHandler() {
                        @Override public void onDelta(String text) { response.set(response.get() + text); }
                        @Override public void onComplete(String fullText) { response.set(fullText); terminal.countDown(); }
                        @Override public void onError(String message) { error.set(message); terminal.countDown(); }
                    });

            assertTrue(terminal.await(90, TimeUnit.SECONDS), "real OpenClaw chat did not reach terminal state");
            assertNull(error.get(), error.get());
            assertFalse(response.get().trim().isEmpty(), "real OpenClaw returned an empty response");
        }
    }
}
