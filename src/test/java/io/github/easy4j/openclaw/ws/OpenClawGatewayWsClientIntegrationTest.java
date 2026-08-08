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
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        config.setGatewayBaseUrl("http://127.0.0.1:" + gateway.getPort());
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
        assertEquals(1, hello.getProtocol());
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
                    conn.send(response(id, true, Map.of(
                            "type", "hello-ok", "protocol", 1,
                            "server", Map.of("version", "test", "connId", "connection"),
                            "features", Map.of("methods", java.util.List.of("chat.send"), "events", java.util.List.of("chat")),
                            "auth", Map.of("role", "operator", "scopes", java.util.List.of("operator.read")),
                            "policy", Map.of("maxPayload", 1024, "maxBufferedBytes", 1024, "tickIntervalMs", 1000))));
                    return;
                }
                if (method.equals("chat.send")) {
                    conn.send(response(id, true, Map.of("runId", id)));
                    conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"" + id + "\",\"delta\":\"hello\"}}");
                    conn.send("{\"type\":\"event\",\"event\":\"chat\",\"payload\":{\"runId\":\"" + id + "\",\"done\":true}}");
                    return;
                }
                if (errorNext.compareAndSet(true, false)) {
                    conn.send("{\"type\":\"res\",\"id\":\"" + id + "\",\"ok\":false,\"error\":{\"code\":\"FAILED\",\"message\":\"failed\"}}");
                } else {
                    conn.send(response(id, true, Map.of()));
                }
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        private String response(String id, boolean ok, Object payload) throws Exception {
            return mapper.writeValueAsString(Map.of("type", "res", "id", id, "ok", ok, "payload", payload));
        }

        private void sendRaw(String message) {
            connection.get().send(message);
        }

        @Override public void onClose(WebSocket conn, int code, String reason, boolean remote) { }
        @Override public void onError(WebSocket conn, Exception ex) { }
        @Override public void onStart() { started.countDown(); }
    }
}
