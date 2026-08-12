package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.api.OpenClawChatClient;
import io.github.easy4j.openclaw.api.OpenClawSseClient;
import io.github.easy4j.openclaw.api.sse.SseSubscription;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 验证 OpenClaw 统一 SSE 对象拓扑和生命周期契约。 */
class OpenClawSseApiShapeTest {

    @Test
    void shouldExposeOnlyTheUnifiedSseTopology() throws Exception {
        Method sseAccessor = OpenClawClient.class.getMethod("sse");
        assertEquals(OpenClawSseClient.class, sseAccessor.getReturnType());

        assertTrue(hasMethod(OpenClawSseClient.class, "subscribeChat"));
        assertTrue(hasMethod(OpenClawSseClient.class, "activeSubscriptionCount"));
        assertFalse(hasMethod(OpenClawSseClient.class, "stop"));
        assertFalse(hasMethod(OpenClawChatClient.class, "events"));
        assertFalse(hasMethod(OpenClawChatClient.class, "chatCompletionStreamRaw"));
        assertNotNull(OpenClawChatClient.class.getMethod(
                "chatCompletionStream",
                io.github.easy4j.openclaw.api.model.ChatRequest.class,
                java.util.Map.class,
                io.github.easy4j.openclaw.api.sse.StreamingChatResponse.Builder.class));

        for (Field field : OpenClawChatClient.class.getDeclaredFields()) {
            assertFalse("streamExecutor".equals(field.getName()));
            assertFalse("activeStreams".equals(field.getName()));
            assertFalse("eventClient".equals(field.getName()));
        }
    }

    @Test
    void shouldCancelSubscriptionIdempotently() {
        AtomicInteger cancellations = new AtomicInteger();
        SseSubscription subscription = new SseSubscription(cancellations::incrementAndGet);

        assertTrue(subscription.isActive());
        assertTrue(subscription.cancel());
        subscription.close();
        assertFalse(subscription.isActive());
        assertEquals(1, cancellations.get());
    }

    @Test
    void shouldExposeSseClientFromRootFacade() {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setStartupCheckEnabled(false);
        try (OpenClawClient client = new OpenClawClient(config)) {
            assertNotNull(client.chat());
            assertNotNull(client.sse());
        }
    }

    private boolean hasMethod(Class<?> type, String name) {
        return Arrays.stream(type.getMethods()).map(Method::getName)
                .anyMatch(name::equals);
    }
}
