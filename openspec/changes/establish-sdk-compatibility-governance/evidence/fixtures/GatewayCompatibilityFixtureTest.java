package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.exception.OpenClawWsRpcException;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class GatewayCompatibilityFixtureTest {

    @Test
    void handshakeNegotiatesProtocolFour() throws Exception {
        try (OpenClawGatewayWsClient client = client(requireEnv("OPENCLAW_GATEWAY_TOKEN"),
                Collections.singletonList("operator.read"))) {
            HelloOk hello = client.connectHandshake();
            assertEquals(4, hello.getProtocol());
            assertNotNull(hello.getServer());
        }
    }

    @Test
    void handshakeRejectsInvalidToken() {
        try (OpenClawGatewayWsClient client = client("invalid-" + requireEnv("OPENCLAW_GATEWAY_TOKEN"),
                Collections.singletonList("operator.read"))) {
            assertThrows(RuntimeException.class, client::connectHandshake);
        }
    }

    @Test
    void sessionsListAllowsOperatorRead() throws Exception {
        try (OpenClawGatewayWsClient client = client(requireEnv("OPENCLAW_GATEWAY_TOKEN"),
                Collections.singletonList("operator.read"))) {
            assertEquals(4, client.connectHandshake().getProtocol());
            assertNotNull(client.sessionsList());
        }
    }

    @Test
    void sessionsListRejectsMissingReadScope() throws Exception {
        try (OpenClawGatewayWsClient client = client(requireEnv("OPENCLAW_GATEWAY_TOKEN"),
                Collections.emptyList())) {
            assertEquals(4, client.connectHandshake().getProtocol());
            OpenClawWsRpcException failure = assertThrows(OpenClawWsRpcException.class, client::sessionsList);
            assertEquals("sessions.list", failure.getMethod());
            assertTrue(failure.getError().getMessage().contains("missing scope: operator.read"));
        }
    }

    private OpenClawGatewayWsClient client(String token, List<String> scopes) {
        URI gateway = URI.create(requireEnv("SDK_PROBE_URL"));
        assertEquals("ws", gateway.getScheme());
        assertEquals("127.0.0.1", gateway.getHost());

        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(gateway.toString().replaceFirst("^ws:", "http:"));
        config.setGatewayClientId("gateway-client");
        config.setGatewayClientMode("backend");
        config.setGatewayAuthToken(token);
        config.setGatewayScopes(scopes);
        return new OpenClawGatewayWsClient(config, gateway);
    }

    private static String requireEnv(String name) {
        String value = Objects.requireNonNull(System.getenv(name), name + " is required");
        assertTrue(!value.isEmpty(), name + " must not be empty");
        return value;
    }
}
