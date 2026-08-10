package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gateway WS {@code connect} handshake.
 * <p> {@code src/gateway/protocol/schema/frames.ts} {@code ConnectParamsSchema} aligned.</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectParams {

    private final int minProtocol;
    private final int maxProtocol;
    private final ClientInfo client;
    private final AuthInfo auth;
    private final DeviceInfo device;
    private final String role;

    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth) {
        this(minProtocol, maxProtocol, client, auth, null, null);
    }

    /**
 * (device).
     *
 * @param minProtocol version
 * @param maxProtocol version
 * @param client
 * @param auth authentication
 * @param device device(Optional,Used for device token stream)
 * @param role connection(Optional, {@code "operator"} {@code "node"})
     */
    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth,
                         DeviceInfo device, String role) {
        this.minProtocol = minProtocol;
        this.maxProtocol = maxProtocol;
        this.client = client;
        this.auth = auth;
        this.device = device;
        this.role = role;
    }

    /**
 * .
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientInfo {
        private final String id, displayName, version, platform, mode;

        public ClientInfo(String id, String displayName, String version, String platform, String mode) {
            this.id = id;
            this.displayName = displayName;
            this.version = version;
            this.platform = platform;
            this.mode = mode;
        }
        // Explicit getters (Lombok @Getter not processed in Maven build)
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getVersion() { return version; }
        public String getPlatform() { return platform; }
        public String getMode() { return mode; }
    }

    /**
 * authentication(token password mutually exclusive).
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthInfo {
        private final String token;
        private final String password;

        private AuthInfo(String token, String password) {
            this.token = token;
            this.password = password;
        }

        public static AuthInfo token(String token) { return new AuthInfo(token, null); }
        public static AuthInfo password(String password) { return new AuthInfo(null, password); }
    }

    /**
 * Builds as RPC params Map.
     */
    public Map<String, Object> toParamsMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("minProtocol", minProtocol);
        m.put("maxProtocol", maxProtocol);
        Map<String, Object> clientMap = new LinkedHashMap<>();
        clientMap.put("id", client.getId());
        clientMap.put("version", client.getVersion());
        clientMap.put("platform", client.getPlatform());
        clientMap.put("mode", client.getMode());
        if (client.getDisplayName() != null) {
            clientMap.put("displayName", client.getDisplayName());
        }
        m.put("client", clientMap);
        if (auth != null) {
            Map<String, Object> authMap = new LinkedHashMap<>();
            if (auth.getToken() != null) authMap.put("token", auth.getToken());
            if (auth.getPassword() != null) authMap.put("password", auth.getPassword());
            m.put("auth", authMap);
        }
        return m;
    }
}
    /**
 * device.
     * <p>
 * Used for Gateway deviceauthentication pairing stream.
 * device, nonce.
     * </p>
     *
 * <h3></h3>
 * <p> v3 , {@code platform} {@code deviceFamily}.
 * v2 Used for.</p>
     *
 * <h3>stream</h3>
     * <ol>
 * <li>Gateway {@code connect.challenge} event( {@code nonce} {@code ts})</li>
 * <li> {@code nonce} device</li>
 * <li> connect {@code nonce}</li>
     * </ol>
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class DeviceInfo {
 /** device. */
        private final String id;
 /** device. */
        private final String publicKey;
 /** device. */
        private final String signature;
 /** (Unix epoch milliseconds). */
        private final Long signedAt;
        /**
 * nonce( {@code connect.challenge} event).
 * <p>Gateway v4+ value. nonce
 * {@code DEVICE_AUTH_NONCE_MISMATCH} .</p>
         */
        private final String nonce;

        public DeviceInfo(String id, String publicKey, String signature, Long signedAt, String nonce) {
            this.id = id;
            this.publicKey = publicKey;
            this.signature = signature;
            this.signedAt = signedAt;
            this.nonce = nonce;
        }
    }
