package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * {@code sessions.list} /.
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionsDefaults {

    @JsonProperty("modelProvider")
    private String modelProvider;

    @JsonProperty("model")
    private String model;

    @JsonProperty("contextTokens")
    private Integer contextTokens;

    @JsonProperty("thinkingDefault")
    private String thinkingDefault;
}
