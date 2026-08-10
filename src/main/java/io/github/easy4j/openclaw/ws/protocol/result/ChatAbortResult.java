package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * {@code chat.abort} RPC response body.
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatAbortResult {

    @JsonProperty("ok")
    private boolean ok;

    @JsonProperty("aborted")
    private boolean aborted;

    @JsonProperty("runIds")
    private List<String> runIds;

    @JsonProperty("abortedRunId")
    private String abortedRunId;

    @JsonProperty("status")
    private String status;

    public List<String> getRunIds() {
        return runIds != null ? runIds : Collections.emptyList();
    }
}
