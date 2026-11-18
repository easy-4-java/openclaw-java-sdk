package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.exception.OpenClawWsRpcException;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class RemovedGatewayMethodFixtureTest {
    private static final String METHOD = "tasks.list";
    private static final Pattern RPC_METHOD = Pattern.compile("\"method\"\\s*:\\s*\"([^\"]+)\"");

    @Test
    void reportsRemovedReadMethodWithoutRetryOrFallback() throws Exception {
        String target = Objects.requireNonNull(System.getenv("SDK_PROBE_TARGET"));
        assertTrue("locked".equals(target) || "candidate".equals(target));
        boolean advertised = "locked".equals(target);
        URI endpoint = URI.create(Objects.requireNonNull(System.getenv("SDK_PROBE_URL")));
        assertEquals("ws", endpoint.getScheme());
        assertEquals("127.0.0.1", endpoint.getHost());

        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        config.setBaseUrl(endpoint.toString().replaceFirst("^ws:", "http:"));
        config.setGatewayClientId("gateway-client");
        config.setGatewayClientMode("backend");
        config.setGatewayAuthToken(Objects.requireNonNull(System.getenv("OPENCLAW_GATEWAY_TOKEN")));
        config.setGatewayScopes(Collections.singletonList("operator.read"));

        try (RecordingClient client = new RecordingClient(config, endpoint)) {
            HelloOk hello = client.connectHandshake();
            assertEquals(4, hello.getProtocol());
            assertEquals(advertised, hello.getFeatures().getMethods().contains(METHOD));

            Method invoke = OpenClawGatewayWsClient.class.getDeclaredMethod("invokeRpc",
                    String.class, Object.class, Class.class, long.class);
            invoke.setAccessible(true);
            if (advertised) {
                Object result = invoke.invoke(client, METHOD, Collections.emptyMap(), Map.class, 5000L);
                assertNotNull(result);
            } else {
                InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                        () -> invoke.invoke(client, METHOD, Collections.emptyMap(), Map.class, 5000L));
                assertTrue(thrown.getCause() instanceof OpenClawWsRpcException);
                OpenClawWsRpcException error = (OpenClawWsRpcException) thrown.getCause();
                assertEquals(METHOD, error.getMethod());
                assertEquals("FORBIDDEN", error.getError().getCode());
                assertEquals("missing scope: operator.admin", error.getError().getMessage());
            }

            assertNotNull(client.sessionsList());
            assertEquals(Arrays.asList("connect", METHOD, "sessions.list"), new ArrayList<>(client.methods));
        }
    }

    private static final class RecordingClient extends OpenClawGatewayWsClient {
        private final List<String> methods = new CopyOnWriteArrayList<>();

        RecordingClient(OpenClawHttpClientConfig config, URI endpoint) {
            super(config, endpoint);
        }

        @Override
        public void send(String text) {
            Matcher match = RPC_METHOD.matcher(text);
            if (match.find()) {
                methods.add(match.group(1));
            }
            super.send(text);
        }
    }
}
