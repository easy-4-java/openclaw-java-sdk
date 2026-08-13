package io.github.easy4j.openclaw.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.exception.OpenClawWsRpcException;
import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import io.github.easy4j.openclaw.ws.protocol.ResponseFrame;
import io.github.easy4j.openclaw.ws.protocol.ChatSendParams;
import io.github.easy4j.openclaw.ws.protocol.SessionsSendParams;
import io.github.easy4j.openclaw.ws.protocol.params.ChatAbortParams;
import io.github.easy4j.openclaw.ws.protocol.params.ChatHistoryParams;
import io.github.easy4j.openclaw.ws.protocol.params.CronListParams;
import io.github.easy4j.openclaw.ws.protocol.params.SessionsListParams;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenClawGatewayWsClientIntegrationTest {

    private TestGateway gateway;
    private OpenClawGatewayWsClient client;

    @BeforeEach
    void setUp() throws Exception {
        gateway = new TestGateway();
        gateway.start();
        assertTrue(gateway.started.await(3, TimeUnit.SECONDS));
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl("http://127.0.0.1:" + gateway.getPort());
        config.setGatewayAuthToken("token");
        client = new OpenClawGatewayWsClient(config, URI.create("ws://127.0.0.1:" + gateway.getPort()));
    }

    @AfterEach
    void tearDown() throws Exception {
        if (client != null) {
            client.close();
        }
        if (gateway != null) {
            gateway.stop(1_000);
        }
    }

    @Test
    void shouldHandshakeInvokeAllTypedRpcsAndReuseConnection() throws Exception {
        RecordingListener listener = new RecordingListener();
        client.addListener(listener);
        HelloOk hello = client.connectHandshake();
        assertEquals(4, hello.getProtocol());
        assertNotNull(client.getHelloOk());
        assertEquals(hello, client.connectHandshake());
        assertEquals(hello, client.connectHandshakeAsync().get(1, TimeUnit.SECONDS));
        assertTrue(listener.connected.await(1, TimeUnit.SECONDS));
        assertEquals("token", gateway.connectRequest.get().path("params").path("auth").path("token").asText());

        assertNotNull(client.sessionsList());
        assertNotNull(client.sessionsList(SessionsListParams.defaults()));
        assertNotNull(client.chatHistory("session", 10));
        assertNotNull(client.chatHistory(ChatHistoryParams.of("session", null)));
        assertNotNull(client.chatAbort("session"));
        assertNotNull(client.chatAbort(ChatAbortParams.abortSession("session")));
        assertNotNull(client.agentIdentityGet());
        assertNotNull(client.cronList());
        assertNotNull(client.cronList(CronListParams.defaults()));
        assertNotNull(client.configGet());
        assertNotNull(client.sessionsSend(SessionsSendParams.builder().key("session").message("hello").build()));

        gateway.errorNext.set(true);
        assertThrows(OpenClawWsRpcException.class, client::sessionsList);

        gateway.sendRaw("{\"type\":\"res\",\"id\":\"unknown\",\"ok\":true,\"payload\":{}}");
        gateway.sendRaw("{\"type\":\"event\",\"event\":\"tick\",\"payload\":{\"now\":1},\"seq\":2}");
        assertTrue(listener.response.await(1, TimeUnit.SECONDS));
        assertTrue(listener.event.await(1, TimeUnit.SECONDS));
        client.removeListener(listener);
    }

    @Test
    void shouldApplyConfiguredConnectOptionsAndSignCurrentChallenge() throws Exception {
        client.close();
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl("http://127.0.0.1:" + gateway.getPort());
        config.setGatewayAuthToken("token");
        config.setGatewayAuthBootstrapToken("bootstrap-token");
        config.setGatewayAuthDeviceToken("device-token");
        config.setGatewayApprovalRuntimeToken("approval-token");
        config.setGatewayAgentRuntimeIdentityToken("runtime-token");
        config.setGatewayClientDisplayName("Configured SDK");
        config.setGatewayClientPlatform("linux");
        config.setGatewayClientDeviceFamily("server");
        config.setGatewayClientModelIdentifier("x86_64");
        config.setGatewayClientInstanceId("instance-1");
        config.setGatewayCapabilities(Collections.singletonList("chat"));
        config.setGatewayCommands(Collections.singletonList("chat.send"));
        config.setGatewayPermissions(Collections.singletonMap("notifications", true));
        config.setGatewayPathEnv("/usr/local/bin:/usr/bin");
        config.setGatewayLocale("zh-CN");
        config.setGatewayUserAgent("cloud-agents/2.0");
        AtomicReference<String> signedPayload = new AtomicReference<String>();
        config.setGatewayDeviceIdentity(new OpenClawGatewayDeviceIdentity() {
            @Override public String getDeviceId() { return "device-1"; }
            @Override public String getPublicKey() { return "public-key"; }
            @Override public String sign(String payload) {
                signedPayload.set(payload);
                return "signature";
            }
        });
        client = new OpenClawGatewayWsClient(config,
                URI.create("ws://127.0.0.1:" + gateway.getPort()));

        client.connectHandshake();

        JsonNode params = gateway.connectRequest.get().path("params");
        assertEquals("bootstrap-token", params.path("auth").path("bootstrapToken").asText());
        assertEquals("device-token", params.path("auth").path("deviceToken").asText());
        assertEquals("approval-token", params.path("auth").path("approvalRuntimeToken").asText());
        assertEquals("runtime-token", params.path("auth").path("agentRuntimeIdentityToken").asText());
        assertEquals("Configured SDK", params.path("client").path("displayName").asText());
        assertEquals("server", params.path("client").path("deviceFamily").asText());
        assertEquals("x86_64", params.path("client").path("modelIdentifier").asText());
        assertEquals("instance-1", params.path("client").path("instanceId").asText());
        assertEquals("chat", params.path("caps").path(0).asText());
        assertEquals("chat.send", params.path("commands").path(0).asText());
        assertTrue(params.path("permissions").path("notifications").asBoolean());
        assertEquals("/usr/local/bin:/usr/bin", params.path("pathEnv").asText());
        assertEquals("zh-CN", params.path("locale").asText());
        assertEquals("cloud-agents/2.0", params.path("userAgent").asText());
        assertEquals("device-1", params.path("device").path("id").asText());
        assertEquals("public-key", params.path("device").path("publicKey").asText());
        assertEquals("signature", params.path("device").path("signature").asText());
        assertEquals("nonce-1", params.path("device").path("nonce").asText());
        assertTrue(signedPayload.get().startsWith("v3|device-1|gateway-client|backend|operator|"));
        assertTrue(signedPayload.get().contains("|token|nonce-1|linux|server"));

        String redacted = client.redactConnectHandshake(gateway.connectRequest.get().toString());
        assertFalse(redacted.contains("bootstrap-token"));
        assertFalse(redacted.contains("device-token"));
        assertFalse(redacted.contains("approval-token"));
        assertFalse(redacted.contains("runtime-token"));
        assertFalse(redacted.contains("\"signature\":\"signature\""));
        assertTrue(redacted.contains("<redacted>"));
    }

    @Test
    void shouldRejectDeviceTokenWithoutDeviceIdentity() {
        client.close();
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl("http://127.0.0.1:" + gateway.getPort());
        config.setGatewayAuthDeviceToken("device-token");
        client = new OpenClawGatewayWsClient(config,
                URI.create("ws://127.0.0.1:" + gateway.getPort()));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> client.connectHandshake());

        assertTrue(rootCause(error).getMessage().contains("gatewayDeviceIdentity"));
    }

    private static Throwable rootCause(Throwable error) {
        Throwable current = error;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }

    @Test
    void shouldStreamChatDispatchProtocolFramesAndNotifyErrors() throws Exception {
        RecordingListener listener = new RecordingListener();
        client.addListener(listener);
        client.connectHandshake();
        CountDownLatch complete = new CountDownLatch(1);
        AtomicReference<String> delta = new AtomicReference<>();
        AtomicReference<String> full = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        client.chatSend(ChatSendParams.builder().sessionKey("session").message("hello").build(),
                new ChatStreamHandler() {
                    @Override public void onDelta(String text) { delta.set(text); }
                    @Override public void onComplete(String fullText) { full.set(fullText); complete.countDown(); }
                    @Override public void onError(String message) { error.set(message); complete.countDown(); }
                });
        assertTrue(complete.await(2, TimeUnit.SECONDS));
        assertEquals("hello", delta.get());
        assertEquals("hello", full.get());

        client.onMessage("{\"type\":\"req\",\"id\":\"server-request\"}");
        client.onMessage("{\"type\":\"unknown\"}");
        client.onMessage("not-json");
        client.onMessage("{\"type\":\"event\",\"event\":\"chat\",\"payload\":null}");
        client.onMessage("{\"type\":\"event\",\"event\":\"connect.challenge\",\"payload\":{}}");
        client.onError(new IllegalStateException("simulated"));
        assertTrue(listener.error.await(1, TimeUnit.SECONDS));
        assertEquals(1, listener.errors.get());
        assertNull(error.get());
    }

    @Test
    void shouldPreserveChatParametersAndFollowGatewayRunId() throws Exception {
        client.connectHandshake();
        CountDownLatch complete = new CountDownLatch(1);
        AtomicReference<String> full = new AtomicReference<String>();

        client.chatSend(ChatSendParams.builder()
                        .sessionKey("agent:ops:main")
                        .message("modern-event")
                        .fastMode("auto")
                        .fastAutoOnSeconds(8)
                        .suppressCommandInterpretation(true)
                        .idempotencyKey("business-turn-1")
                        .build(),
                new ChatStreamHandler() {
                    @Override public void onDelta(String text) { }
                    @Override public void onComplete(String fullText) { full.set(fullText); complete.countDown(); }
                    @Override public void onError(String message) { complete.countDown(); }
                });

        assertTrue(complete.await(2, TimeUnit.SECONDS));
        JsonNode request = gateway.chatSendRequest.get();
        assertEquals("business-turn-1", request.path("params").path("idempotencyKey").asText());
        assertEquals("auto", request.path("params").path("fastMode").asText());
        assertEquals(8, request.path("params").path("fastAutoOnSeconds").asInt());
        assertTrue(request.path("params").path("suppressCommandInterpretation").asBoolean());
        assertEquals("modern-event", full.get());
    }

    @Test
    void shouldRejectRpcBeforeHandshakeAndFailPendingWorkOnClose() throws Exception {
        assertThrows(IllegalStateException.class, client::sessionsList);
        RecordingListener listener = new RecordingListener();
        client.addListener(listener);
        client.connectHandshake();
        client.onClose(1006, "network lost", true);
        assertTrue(listener.disconnected.await(1, TimeUnit.SECONDS));
        assertEquals(1006, listener.closeCode.get());
        assertNull(client.getHelloOk());
    }

    private static final class RecordingListener implements OpenClawWsListener {
        private final CountDownLatch connected = new CountDownLatch(1);
        private final CountDownLatch disconnected = new CountDownLatch(1);
        private final CountDownLatch response = new CountDownLatch(1);
        private final CountDownLatch event = new CountDownLatch(1);
        private final CountDownLatch error = new CountDownLatch(1);
        private final AtomicInteger errors = new AtomicInteger();
        private final AtomicInteger closeCode = new AtomicInteger();
        @Override public void onConnected(HelloOk helloOk) { connected.countDown(); }
        @Override public void onDisconnected(int code, String reason, boolean remote) { closeCode.set(code); disconnected.countDown(); }
        @Override public void onError(Exception ex) { errors.incrementAndGet(); error.countDown(); }
        @Override public void onEvent(EventFrame frame) { event.countDown(); }
        @Override public void onResponse(ResponseFrame frame) { response.countDown(); }
    }

    private static final class TestGateway extends WebSocketServer {
        private final ObjectMapper mapper = new ObjectMapper();
        private final CountDownLatch started = new CountDownLatch(1);
        private final AtomicReference<JsonNode> connectRequest = new AtomicReference<>();
        private final AtomicReference<JsonNode> chatSendRequest = new AtomicReference<JsonNode>();
        private final AtomicReference<WebSocket> connection = new AtomicReference<>();
        private final AtomicBoolean errorNext = new AtomicBoolean();

        private TestGateway() {
            super(new InetSocketAddress("127.0.0.1", 0));
        }

        @Override public void onOpen(WebSocket conn, ClientHandshake handshake) {
            connection.set(conn);
            conn.send("{\"type\":\"event\",\"event\":\"connect.challenge\",\"payload\":{\"nonce\":\"nonce-1\"}}");
        }

        @Override public void onMessage(WebSocket conn, String message) {
            try {
                JsonNode request = mapper.readTree(message);
                String id = request.path("id").asText();
                String method = request.path("method").asText();
                if (method.equals("connect")) {
                    connectRequest.set(request);
                    conn.send(response(id, true, buildHelloOkMap()));
                    return;
                }
                if (method.equals("chat.send")) {
                    chatSendRequest.set(request);
                    if ("modern-event".equals(request.path("params").path("message").asText())) {
                        conn.send(response(id, true, Collections.singletonMap("runId", "gateway-run-1")));
                        conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"gateway-run-1\",\"state\":\"delta\",\"deltaText\":\"modern-event\",\"seq\":1}}");
                        conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"gateway-run-1\",\"state\":\"final\",\"seq\":2}}");
                        return;
                    }
                    conn.send(response(id, true, Collections.singletonMap("runId", id)));
                    conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"" + id + "\",\"delta\":\"hello\"}}");
                    conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"" + id + "\",\"done\":true}}");
                    return;
                }
                if (errorNext.compareAndSet(true, false)) {
                    conn.send("{\"type\":\"res\",\"id\":\"" + id + "\",\"ok\":false,\"error\":{\"code\":\"FAILED\",\"message\":\"failed\"}}");
                } else {
                    conn.send(response(id, true, Collections.emptyMap()));
                }
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        private String response(String id, boolean ok, Object payload) throws Exception {
            Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
            responseMap.put("type", "res");
            responseMap.put("id", id);
            responseMap.put("ok", ok);
            responseMap.put("payload", payload);
            return mapper.writeValueAsString(responseMap);
        }

        private Map<String, Object> buildHelloOkMap() {
            Map<String, Object> server = new LinkedHashMap<String, Object>();
            server.put("version", "test");
            server.put("connId", "connection");
            Map<String, Object> features = new LinkedHashMap<String, Object>();
            features.put("methods", Arrays.asList("chat.send"));
            features.put("events", Arrays.asList("chat"));
            Map<String, Object> auth = new LinkedHashMap<String, Object>();
            auth.put("role", "operator");
            auth.put("scopes", Arrays.asList("operator.read"));
            Map<String, Object> policy = new LinkedHashMap<String, Object>();
            policy.put("maxPayload", 1024);
            policy.put("maxBufferedBytes", 1024);
            policy.put("tickIntervalMs", 1000);
            Map<String, Object> helloOk = new LinkedHashMap<String, Object>();
            helloOk.put("type", "hello-ok");
            helloOk.put("protocol", 4);
            helloOk.put("server", server);
            helloOk.put("features", features);
            helloOk.put("auth", auth);
            helloOk.put("policy", policy);
            return helloOk;
        }

        private void sendRaw(String message) {
            connection.get().send(message);
        }

        @Override public void onClose(WebSocket conn, int code, String reason, boolean remote) { }
        @Override public void onError(WebSocket conn, Exception ex) { }
        @Override public void onStart() { started.countDown(); }
    }
}
