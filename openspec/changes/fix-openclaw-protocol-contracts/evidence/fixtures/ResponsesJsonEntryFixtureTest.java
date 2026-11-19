package io.github.easy4j.openclaw;

import com.sun.net.httpserver.HttpServer;
import io.github.easy4j.openclaw.api.OpenClawResponsesClient;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponsesJsonEntryFixtureTest {

    @Test
    void existingJsonEntrySendsStreamRequestAndAttemptsJsonParsing() throws IOException {
        AtomicInteger requests = new AtomicInteger();
        AtomicInteger streamRequests = new AtomicInteger();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/v1/responses", exchange -> {
            try (Scanner body = new Scanner(exchange.getRequestBody(), "UTF-8")) {
                requests.incrementAndGet();
                String json = body.useDelimiter("\\A").next();
                boolean stream = json.contains("\"stream\":true");
                if (stream) {
                    streamRequests.incrementAndGet();
                }
                String payload = stream
                        ? "event: response.completed\ndata: {\"type\":\"response.completed\","
                                + "\"response\":{\"id\":\"resp_fixture\",\"status\":\"completed\"}}\n\ndata: [DONE]\n\n"
                        : "{\"id\":\"resp_fixture\",\"status\":\"completed\",\"output\":[]}";
                byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", stream ? "text/event-stream" : "application/json");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream output = exchange.getResponseBody()) {
                    output.write(bytes);
                }
            } finally {
                exchange.close();
            }
        });
        server.start();
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl("http://127.0.0.1:" + server.getAddress().getPort());
        try (OpenClawResponsesClient client = new OpenClawResponsesClient(config)) {
            assertEquals("resp_fixture", client.createResponse(
                    ResponseRequest.builder().model("openclaw").input("fixture").build()).getId());
            OpenClawHttpException failure = assertThrows(OpenClawHttpException.class, () -> client.createResponse(
                    ResponseRequest.builder().model("openclaw").input("fixture").stream(true).build()));
            assertTrue(failure.getMessage().contains("parse"), failure.getMessage());
            assertEquals(2, requests.get());
            assertEquals(1, streamRequests.get());
        } finally {
            server.stop(0);
        }
    }
}
