package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** OpenClaw 基于 OkHttp enqueue 的 500 并发回归测试。 */
class OpenClawNonBlockingConcurrencyTest {

    private static final int CONCURRENCY = 500;

    @Test
    void shouldCompleteFiveHundredAsyncRequestsWithBoundedOkHttpDispatcher() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            for (int index = 0; index < CONCURRENCY; index++) {
                server.enqueue(jsonResponse("{\"id\":\"chatcmpl-" + index
                        + "\",\"model\":\"openclaw/test\",\"choices\":[{\"index\":0,"
                        + "\"message\":{\"role\":\"assistant\",\"content\":\"ok\"},"
                        + "\"finish_reason\":\"stop\"}]}"));
            }
            OpenClawHttpClientConfig config = config(server);
            ChatRequest request = request();
            long baselineThreads = countDispatcherThreads();

            try (OpenClawClient client = new OpenClawClient(config)) {
                List<CompletableFuture<?>> futures = new ArrayList<>(CONCURRENCY);
                for (int index = 0; index < CONCURRENCY; index++) {
                    futures.add(client.chat().chatCompletionAsync(request));
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                        .get(30, TimeUnit.SECONDS);

                assertEquals(CONCURRENCY, server.getRequestCount());
                assertTrue(countDispatcherThreads() - baselineThreads <= config.getMaxRequests(),
                        "OkHttp dispatcher exceeded its configured concurrency bound");
            }
        }
    }

    @Test
    void shouldCompleteFiveHundredSseResponsesWithBoundedConsumers() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            for (int index = 0; index < CONCURRENCY; index++) {
                server.enqueue(new MockResponse().setResponseCode(200)
                        .setHeader("Content-Type", "text/event-stream")
                        .setBody("data: [DONE]\n\n"));
            }
            OpenClawHttpClientConfig config = config(server);
            config.setStreamCorePoolSize(8);
            config.setStreamMaxPoolSize(8);
            config.setStreamQueueCapacity(1_024);

            try (OpenClawClient client = new OpenClawClient(config)) {
                List<CompletableFuture<?>> streams = new ArrayList<>(CONCURRENCY);
                for (int index = 0; index < CONCURRENCY; index++) {
                    StreamingChatResponse stream = client.chat().chatCompletionStream(request());
                    streams.add(stream);
                }
                CompletableFuture.allOf(streams.toArray(new CompletableFuture<?>[0]))
                        .get(30, TimeUnit.SECONDS);

                assertEquals(CONCURRENCY, server.getRequestCount());
                assertTrue(countThreads("openclaw-sse-consumer-") <= config.getStreamMaxPoolSize());
            }
        }
    }

    private OpenClawHttpClientConfig config(MockWebServer server) {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(server.url("").toString().replaceAll("/+$", ""));
        config.setStartupCheckEnabled(false);
        config.setMaxRequests(64);
        config.setMaxRequestsPerHost(64);
        return config;
    }

    private ChatRequest request() {
        return ChatRequest.builder().agent("openclaw/test")
                .messages(Collections.singletonList(ChatMessage.ofUser("ping"))).build();
    }

    private MockResponse jsonResponse(String body) {
        return new MockResponse().setResponseCode(200)
                .setHeader("Content-Type", "application/json").setBody(body);
    }

    private long countDispatcherThreads() {
        return Thread.getAllStackTraces().keySet().stream().map(Thread::getName)
                .filter(name -> name.startsWith("openclaw-okhttp-dispatcher-")).count();
    }

    private long countThreads(String prefix) {
        return Thread.getAllStackTraces().keySet().stream().map(Thread::getName)
                .filter(name -> name.startsWith(prefix)).count();
    }
}
