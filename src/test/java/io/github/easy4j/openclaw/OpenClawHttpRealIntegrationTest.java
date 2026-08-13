package io.github.easy4j.openclaw;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.model.ChatChunk;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.model.ChatResponse;
import io.github.easy4j.openclaw.api.model.ResponseFormat;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 本机真实 OpenClaw HTTP 协议验收测试，直接验证 SDK 的 Blocking 与 SSE 实现。
 *
 * <p>只有显式传入 {@code -Dopenclaw.realIntegration=true} 时才调用本机 Gateway 和实际模型，
 * 默认构建不会产生真实模型请求。</p>
 */
@Slf4j
class OpenClawHttpRealIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void shouldUseJsonObjectThroughRealBlockingHttp() throws Exception {
        requireRealIntegration();
        long startedAt = System.nanoTime();

        try (OpenClawClient client = createClient()) {
            ChatResponse response = client.chatCompletion(ChatRequest.builder()
                    .agent(resolveAgentTarget())
                    .messages(Arrays.asList(ChatMessage.ofUser(
                            "只返回一个JSON对象，不要输出额外文字。字段ok必须为true，transport必须为blocking。")))
                    .responseFormat(ResponseFormat.jsonObject())
                    .maxTokens(256)
                    .build());

            assertNotNull(response);
            assertNotNull(response.getChoices());
            assertFalse(response.getChoices().isEmpty(), "真实 Blocking HTTP 必须返回 choices");
            assertNotNull(response.getChoices().get(0).getMessage());
            String content = response.getChoices().get(0).getMessage().getContent();
            assertNotNull(content);
            JsonNode json = OBJECT_MAPPER.readTree(content);
            assertTrue(json.path("ok").asBoolean(), content);
            assertEquals("blocking", json.path("transport").asText(), content);
        }

        log.info("真实 OpenClaw Blocking HTTP SDK 验收通过，latencyMs={}", elapsedMillis(startedAt));
    }

    @Test
    void shouldReceiveTextDeltasThroughRealHttpSse() throws Exception {
        requireRealIntegration();
        long startedAt = System.nanoTime();
        List<String> deltas = new CopyOnWriteArrayList<String>();
        AtomicReference<String> completedContent = new AtomicReference<String>();
        AtomicReference<Throwable> streamError = new AtomicReference<Throwable>();
        CountDownLatch terminal = new CountDownLatch(1);

        try (OpenClawClient client = createClient()) {
            StreamingChatResponse stream = client.chatCompletionStream(
                    ChatRequest.builder()
                            .agent(resolveAgentTarget())
                            .messages(Arrays.asList(ChatMessage.ofUser("请只回复：HTTP SSE OK")))
                            .maxTokens(128)
                            .build(),
                    StreamingChatResponse.builder()
                            .onDelta(deltas::add)
                            .onComplete(content -> {
                                completedContent.set(content);
                                terminal.countDown();
                            })
                            .onError(error -> {
                                streamError.set(error);
                                terminal.countDown();
                            }));

            ChatChunk chunk = stream.get(90, TimeUnit.SECONDS);
            assertTrue(terminal.await(3, TimeUnit.SECONDS), "真实 HTTP SSE 未触发终态回调");
            assertNull(streamError.get(), String.valueOf(streamError.get()));
            assertNotNull(chunk);
            assertNotNull(chunk.getChoices());
            assertFalse(chunk.getChoices().isEmpty(), "真实 HTTP SSE 必须返回 choices");
            assertNotNull(chunk.getChoices().get(0).getDelta());
            assertFalse(deltas.isEmpty(), "真实 HTTP SSE 必须至少产生一个 delta");
            String deltaContent = join(deltas);
            assertTrue(deltaContent.contains("HTTP SSE OK"), deltaContent);
            assertEquals(deltaContent, chunk.getChoices().get(0).getDelta().getContent());
            assertEquals(deltaContent, completedContent.get());
        }

        log.info("真实 OpenClaw HTTP SSE SDK 验收通过，latencyMs={}", elapsedMillis(startedAt));
    }

    private static OpenClawClient createClient() {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(System.getProperty("openclaw.baseUrl", "http://127.0.0.1:18789"));
        config.setStartupCheckEnabled(false);
        config.setConnectTimeoutMillis(5_000);
        config.setReadTimeoutMillis(120_000);
        config.setCallTimeoutMillis(120_000);
        return new OpenClawClient(config);
    }

    private static String resolveAgentTarget() {
        String agentId = System.getProperty("openclaw.agentId", "ops").trim();
        return agentId.startsWith("openclaw/") ? agentId : "openclaw/" + agentId;
    }

    private static void requireRealIntegration() {
        Assumptions.assumeTrue(Boolean.getBoolean("openclaw.realIntegration"));
    }

    private static String join(List<String> values) {
        StringBuilder result = new StringBuilder();
        for (String value : values) {
            result.append(value);
        }
        return result.toString();
    }

    private static long elapsedMillis(long startedAt) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
    }
}
