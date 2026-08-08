package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * {@code chat.history} RPC response body.
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistoryResult {

    @JsonProperty("sessionKey")
    private String sessionKey;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("messages")
    private List<Object> messages;

    @JsonProperty("thinkingLevel")
    private String thinkingLevel;

    @JsonProperty("fastMode")
    private Boolean fastMode;

    @JsonProperty("verboseLevel")
    private String verboseLevel;

    /**
 * @return null message( Gateway , Jackson )
     */
    public List<Object> getMessages() {
        return messages != null ? messages : Collections.emptyList();
    }
}
