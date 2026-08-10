package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * {@code config.get} RPC response body(field).
 * <p>field; {@link #getSnapshot} .</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigGetResult {

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("config")
    private JsonNode config;

    @JsonProperty("uiHints")
    private JsonNode uiHints;

    @JsonProperty("path")
    private String path;
}
