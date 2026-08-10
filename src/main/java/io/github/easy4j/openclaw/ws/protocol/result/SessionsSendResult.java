package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * {@code sessions.send} RPC response body( {@code chat.send} ).
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsSendResult {

    @JsonProperty("runId")
    private String runId;

    @JsonProperty("messageSeq")
    private Integer messageSeq;

    @JsonProperty("interruptedActiveRun")
    private Boolean interruptedActiveRun;
}
