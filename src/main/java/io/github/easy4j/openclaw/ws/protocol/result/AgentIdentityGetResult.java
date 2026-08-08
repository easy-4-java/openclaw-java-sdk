package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * {@code agent.identity.get} RPC response body.
 * <p>aligned {@code AgentIdentityResultSchema}.</p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentIdentityGetResult {

    @JsonProperty("agentId")
    private String agentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("avatarSource")
    private String avatarSource;

    @JsonProperty("avatarStatus")
    private String avatarStatus;

    @JsonProperty("avatarReason")
    private String avatarReason;

    @JsonProperty("emoji")
    private String emoji;
}
