package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Gateway→ RPC :{@code { type: "res", id, ok, payload, error }}.
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public class ResponseFrame extends GatewayFrame {

    private final String id;
    private final boolean ok;
    private final Object payload;
    private final ErrorShape error;

    @JsonCreator
    public ResponseFrame(
            @JsonProperty("type") String type,
            @JsonProperty("id") String id,
            @JsonProperty("ok") boolean ok,
            @JsonProperty("payload") Object payload,
            @JsonProperty("error") ErrorShape error) {
        super("res");
        this.id = id;
        this.ok = ok;
        this.payload = payload;
        this.error = error;
    }
}
