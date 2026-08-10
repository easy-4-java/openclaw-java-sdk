package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Gateway WS {@code connect} handshake({@code hello-ok}).
 * <p> {@code src/gateway/protocol/schema/frames.ts} {@code HelloOkSchema} aligned.</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloOk {

    @JsonProperty("type")
    private String type;

    @JsonProperty("protocol")
    private int protocol;

    @JsonProperty("server")
    private ServerInfo server;

    @JsonProperty("features")
    private FeaturesInfo features;

    @JsonProperty("auth")
    private AuthResult auth;

    @JsonProperty("policy")
    private PolicyInfo policy;

 /** versionconnection ID. */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServerInfo {
        @JsonProperty("version") private String version;
        @JsonProperty("connId") private String connId;
    }

 /** method event . */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeaturesInfo {
        @JsonProperty("methods") private List<String> methods;
        @JsonProperty("events") private List<String> events;
    }

 /** authentication:. */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthResult {
        @JsonProperty("role") private String role;
        @JsonProperty("scopes") private List<String> scopes;
    }

 /** connection. */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyInfo {
        @JsonProperty("maxPayload") private int maxPayload;
        @JsonProperty("maxBufferedBytes") private int maxBufferedBytes;
        @JsonProperty("tickIntervalMs") private int tickIntervalMs;
    }
}
