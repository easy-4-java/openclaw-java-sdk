package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Gateway→event:{@code { type: "event", event, payload, seq }}.
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public class EventFrame extends GatewayFrame {

    private final String event;
    private final Object payload;
    private final Integer seq;

    @JsonCreator
    public EventFrame(
            @JsonProperty("type") String type,
            @JsonProperty("event") String event,
            @JsonProperty("payload") Object payload,
            @JsonProperty("seq") Integer seq) {
        super("event");
        this.event = event;
        this.payload = payload;
        this.seq = seq;
    }
}
