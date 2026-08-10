package io.github.easy4j.openclaw;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.OpenClawChatClient;
import io.github.easy4j.openclaw.api.OpenClawEmbeddingsClient;
import io.github.easy4j.openclaw.api.OpenClawHeaders;
import io.github.easy4j.openclaw.api.OpenClawResponsesClient;
import io.github.easy4j.openclaw.api.OpenClawSessionKeys;
import io.github.easy4j.openclaw.api.OpenClawToolInvokeClient;
import io.github.easy4j.openclaw.api.OpenClawWebhookClient;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.model.EmbeddingsRequest;
import io.github.easy4j.openclaw.api.model.HookRequest;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.api.model.ToolInvokeRequest;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenClawHttpApiCoverageTest {

    private final AtomicInteger status = new AtomicInteger(200);
    private final AtomicBoolean transportFailure = new AtomicBoolean();
    private final List<Request> requests = new CopyOnWriteArrayList<>();
    private OpenClawHttpClientConfig config;
    private OkHttpClient client;

    @BeforeEach
    void setUp() {
        config = new OpenClawHttpClientConfig();
        config.setBaseUrl("http://localhost:18789");
        config.setGatewayAuthToken("gateway-token");
        config.setHooksToken("hook-token");
        config.setLegacyInjectedOkHttpTransportEnabled(true);
        client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();
            requests.add(request);
            if (transportFailure.get()) throw new IOException("transport down");
            int code = status.get();
            String body = responseBody(request);
            return new Response.Builder().request(request).protocol(Protocol.HTTP_1_1)
                    .code(code).message(code < 300 ? "OK" : "Failure")
                    .body(ResponseBody.create(body, MediaType.get(
                            request.url().encodedPath().contains("chat/completions") && Boolean.TRUE.equals(request.tag(Boolean.class))
                                    ? "text/event-stream" : "application/json")))
                    .build();
        }).build();
    }

    @AfterEach
    void tearDown() {
        OpenClawOkHttpClientFactory.shutdown(client);
    }

    @Test
    void shouldCoverChatModelsStreamingAndValidation() throws Exception {
        try (OpenClawChatClient chat = new OpenClawChatClient(config, new ObjectMapper(), client)) {
            ChatRequest agentRequest = ChatRequest.builder().agent("openclaw/default")
                    .messages(List.of(ChatMessage.ofUser("hello"))).build();
            ChatRequest modelRequest = ChatRequest.builder().model("gpt-5.4")
                    .messages(List.of(ChatMessage.ofUser("hello"))).build();

            assertEquals("chat-id", chat.chatCompletion(agentRequest).getId());
            assertEquals("chat-id", chat.chatCompletion(modelRequest, Map.of("X-Custom", "value")).getId());
            AtomicBoolean cancellationRegistered = new AtomicBoolean();
            assertThrows(OpenClawHttpException.class, () -> chat.chatCompletion(agentRequest, null, callback -> {
                cancellationRegistered.set(true);
                callback.run();
                return () -> { };
            }));
            assertTrue(cancellationRegistered.get());
            assertNotNull(chat.listModels());
            assertNotNull(chat.getModel("model with space"));
            chat.health();
            assertTrue(requests.stream().anyMatch(request -> "Bearer gateway-token".equals(request.header("Authorization"))));
            assertTrue(requests.stream().anyMatch(request -> "gpt-5.4".equals(request.header("x-openclaw-model"))));

            assertThrows(NullPointerException.class, () -> chat.chatCompletion(null));
            assertThrows(IllegalArgumentException.class, () -> chat.chatCompletion(ChatRequest.builder()
                    .messages(List.of(ChatMessage.ofUser("hello"))).build()));
            assertThrows(IllegalArgumentException.class, () -> chat.chatCompletion(ChatRequest.builder()
                    .agent("openclaw/default").messages(List.of()).build()));

            StreamingChatResponse stream = chat.chatCompletionStream(agentRequest);
            assertEquals("hello", stream.get(3, TimeUnit.SECONDS).getChoices().get(0).getDelta().getContent());
            StreamingChatResponse callbackStream = chat.chatCompletionStream(agentRequest,
                    StreamingChatResponse.builder().onDelta(ignored -> { }).onChunk(ignored -> { })
                            .onToolCall(ignored -> { }).onComplete(ignored -> { }).onError(ignored -> { }));
            assertEquals("hello", callbackStream.get(3, TimeUnit.SECONDS).getChoices().get(0).getDelta().getContent());
            try (Response raw = chat.chatCompletionStreamRaw(agentRequest, Map.of("X-Raw", "yes"))) {
                assertEquals(200, raw.code());
            }

            status.set(500);
            assertThrows(OpenClawHttpException.class, () -> chat.chatCompletion(agentRequest));
            assertThrows(OpenClawHttpException.class, () -> chat.chatCompletionStreamRaw(agentRequest));
        }
    }

    @Test
    void shouldCoverResponsesEmbeddingsToolsAndBaseHttpErrors() {
        try (OpenClawResponsesClient responses = new OpenClawResponsesClient(config, null, client);
             OpenClawEmbeddingsClient embeddings = new OpenClawEmbeddingsClient(config, null, client);
             OpenClawToolInvokeClient tools = new OpenClawToolInvokeClient(config, null, client)) {
            assertNotNull(responses.createResponse(ResponseRequest.builder().agent("openclaw/default").input("hello").build()));
            assertNotNull(responses.createResponse(ResponseRequest.builder().model("gpt-5.4").input(List.of("hello")).build()));
            assertThrows(IllegalArgumentException.class, () -> responses.createResponse(ResponseRequest.builder().input("hello").build()));
            assertThrows(IllegalArgumentException.class, () -> responses.createResponse(ResponseRequest.builder().agent("a").build()));

            assertNotNull(embeddings.createEmbeddings(EmbeddingsRequest.builder().agent("openclaw/default").input("hello").build()));
            assertNotNull(embeddings.createEmbeddings(EmbeddingsRequest.builder().model("embed-model").input(List.of("hello")).build()));
            assertThrows(IllegalArgumentException.class, () -> embeddings.createEmbeddings(EmbeddingsRequest.builder().input("hello").build()));
            assertThrows(IllegalArgumentException.class, () -> embeddings.createEmbeddings(EmbeddingsRequest.builder().agent("a").build()));

            ToolInvokeRequest request = new ToolInvokeRequest();
            request.setTool("browser");
            request.setAction("open");
            request.setArgs(Map.of("url", "http://localhost"));
            assertTrue(tools.invoke(request).getOk());
            assertThrows(NullPointerException.class, () -> tools.invoke(null));
            assertThrows(IllegalArgumentException.class, () -> tools.invoke(new ToolInvokeRequest()));
            status.set(404);
            assertFalse(tools.invoke(request).getOk());
            status.set(503);
            OpenClawHttpException statusError = assertThrows(OpenClawHttpException.class, () -> tools.invoke(request));
            assertEquals(503, statusError.getStatusCode());

            transportFailure.set(true);
            assertThrows(OpenClawHttpException.class, () -> embeddings.createEmbeddings(
                    EmbeddingsRequest.builder().agent("a").input("hello").build()));
        }

        OpenClawHttpClientConfig empty = new OpenClawHttpClientConfig();
        empty.setBaseUrl(" ");
        try (OpenClawChatClient chat = new OpenClawChatClient(empty, null, client)) {
            assertThrows(OpenClawHttpException.class, chat::health);
        }
    }

    @Test
    void shouldCoverWebhookBodiesAuthenticationParsingAndErrors() {
        HookRequest request = new HookRequest();
        request.setMessage("run task");
        request.setAgentId("agent");
        request.setName("name");
        request.setWakeMode("later");
        request.setSessionKey("hook:agent:user");
        request.setDeliver(true);
        request.setChannel("web");
        request.setTo("user");
        request.setModel("model");
        request.setThinking("high");

        try (OpenClawWebhookClient webhooks = new OpenClawWebhookClient(config, null, client)) {
            assertTrue(webhooks.postHooksAgent(request).isSuccess());
            assertEquals("run-1", webhooks.postHooksAgent(request).getRunId());
            assertTrue(webhooks.postHooksWake("wake", null).contains("runId"));
            assertTrue(webhooks.postHooksWake("wake", "later").contains("runId"));
            assertTrue(webhooks.postMappedHook("/hooks/custom", null).contains("runId"));
            assertThrows(IllegalArgumentException.class, () -> webhooks.postHooksWake(" ", "now"));
            assertThrows(IllegalArgumentException.class, () -> webhooks.postMappedHook("bad/name", Map.of()));
            assertThrows(IllegalArgumentException.class, () -> OpenClawWebhookClient.buildHooksAgentBody(new HookRequest()));
            assertEquals("custom", OpenClawWebhookClient.normalizeHookName(" /hooks/custom "));
            assertTrue(OpenClawWebhookClient.parseOk("{\"ok\":true}"));
            assertTrue(OpenClawWebhookClient.parseOk("prefix \"ok\":true suffix"));
            assertFalse(OpenClawWebhookClient.parseOk(null));
            assertFalse(OpenClawWebhookClient.parseOk(""));
            assertEquals("run-1", OpenClawWebhookClient.parseRunId("{\"runId\":\"run-1\"}"));
            assertNull(OpenClawWebhookClient.parseRunId("invalid"));

            status.set(500);
            assertThrows(OpenClawHttpException.class, () -> webhooks.postMappedHook("custom", Map.of()));
            transportFailure.set(true);
            assertThrows(OpenClawHttpException.class, () -> webhooks.postMappedHook("custom", Map.of()));
        }
    }

    @Test
    void shouldBuildHeadersSessionKeysAndMessageFactories() {
        Map<String, String> headers = OpenClawHeaders.builder().model("m").agentId("a")
                .sessionKey("s").messageChannel("web").scopes("read,write").build();
        assertEquals(5, headers.size());
        assertTrue(OpenClawHeaders.builder().build().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> headers.put("x", "y"));

        assertEquals("hook:agent:user", OpenClawSessionKeys.forStableSession(" Agent ", "User"));
        assertEquals("hook:user:correlation", OpenClawSessionKeys.forEphemeralPeer("User", "Correlation"));
        assertTrue(OpenClawSessionKeys.forEphemeralPeer("user").startsWith("hook:user:"));
        assertNotNull(OpenClawSessionKeys.newCorrelationId());
        assertThrows(NullPointerException.class, () -> OpenClawSessionKeys.forStableSession(null, "user"));
        assertThrows(IllegalArgumentException.class, () -> OpenClawSessionKeys.forStableSession(" ", "user"));
        assertThrows(IllegalArgumentException.class, () -> OpenClawSessionKeys.forStableSession("bad:value", "user"));
        assertThrows(IllegalArgumentException.class, () -> OpenClawSessionKeys.forStableSession("bad value", "user"));

        ChatMessage.ToolCall toolCall = ChatMessage.ToolCall.of("id", "fn", "{}");
        assertEquals("system", ChatMessage.ofSystem("s").getRole());
        assertEquals("user", ChatMessage.ofUser("u").getRole());
        assertEquals("assistant", ChatMessage.ofAssistant("a").getRole());
        assertEquals(toolCall, ChatMessage.ofAssistant(null, List.of(toolCall)).getToolCalls().get(0));
        assertEquals("tool", ChatMessage.ofTool("id", "result").getRole());
    }

    private String responseBody(Request request) {
        String path = request.url().encodedPath();
        if (path.equals("/v1/chat/completions")) {
            try {
                okio.Buffer buffer = new okio.Buffer();
                if (request.body() != null) request.body().writeTo(buffer);
                if (buffer.readUtf8().contains("\"stream\":true")) {
                    return "data: {\"id\":\"chunk\",\"model\":\"m\",\"choices\":[{\"index\":0,\"delta\":{\"role\":\"assistant\",\"content\":\"hello\"}}]}\n\n"
                            + "data: [DONE]\n\n";
                }
            } catch (IOException ignored) { }
            return "{\"id\":\"chat-id\",\"choices\":[]}";
        }
        if (path.equals("/v1/models")) return "{\"data\":[]}";
        if (path.startsWith("/v1/models/")) return "{}";
        if (path.equals("/tools/invoke")) return "{\"ok\":true}";
        if (path.startsWith("/hooks/")) return "{\"ok\":true,\"runId\":\"run-1\"}";
        return "{}";
    }
}
