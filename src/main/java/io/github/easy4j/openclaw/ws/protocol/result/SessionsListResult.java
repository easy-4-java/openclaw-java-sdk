package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * {@code sessions.list} RPC response body.
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsListResult {

    @JsonProperty("ts")
    private long ts;

    @JsonProperty("path")
    private String path;

    @JsonProperty("count")
    private int count;

    @JsonProperty("totalCount")
    private Integer totalCount;

    @JsonProperty("limitApplied")
    private Integer limitApplied;

    @JsonProperty("hasMore")
    private Boolean hasMore;

    @JsonProperty("defaults")
    private GatewaySessionsDefaults defaults;

    @JsonProperty("sessions")
    private List<GatewaySessionRow> sessions;

    /**
 * @return null session
     */
    public List<GatewaySessionRow> getSessions() {
        return sessions != null ? sessions : Collections.emptyList();
    }
}
