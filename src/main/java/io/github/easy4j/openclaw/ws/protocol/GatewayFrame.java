package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Gateway WebSocket protocol frame base type.
 * <p>Frame types: {@code req} (client to Gateway), {@code res} (Gateway to client), {@code event} (Gateway push).</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see RequestFrame
 * @see ResponseFrame
 * @see EventFrame
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RequestFrame.class, name = "req"),
        @JsonSubTypes.Type(value = ResponseFrame.class, name = "res"),
        @JsonSubTypes.Type(value = EventFrame.class, name = "event"),
})
public abstract class GatewayFrame {

    private final String type;

    protected GatewayFrame(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
