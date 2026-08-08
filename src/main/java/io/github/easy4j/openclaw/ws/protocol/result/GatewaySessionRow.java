package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * {@code sessions.list} session.
 * <p>aligned {@code GatewaySessionRow}({@code src/gateway/session-utils.types.ts})field.</p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionRow {

    @JsonProperty("key")
    private String key;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("label")
    private String label;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("derivedTitle")
    private String derivedTitle;

    @JsonProperty("lastMessagePreview")
    private String lastMessagePreview;

    @JsonProperty("updatedAt")
    private Long updatedAt;

    @JsonProperty("modelProvider")
    private String modelProvider;

    @JsonProperty("model")
    private String model;

    @JsonProperty("hasActiveRun")
    private Boolean hasActiveRun;

    @JsonProperty("status")
    private String status;
}
