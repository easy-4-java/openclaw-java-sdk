package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gateway WebSocket connection({@code connect.challenge} event).
 * <p>
 * Gateway v4,Gateway {@code connect} ,
 * {@code connect.challenge} event,:
 * <ul>
 * <li>{@code nonce} - , connect </li>
 * <li>{@code ts} - (Unix epoch milliseconds)</li>
 * </ul>
 * </p>
 *
 * <h3>handshakestream</h3>
 * <ol>
 * <li> WebSocket connection</li>
 * <li>Gateway {@code connect.challenge} event( {@code nonce} {@code ts})</li>
 * <li> {@code nonce} device(Optional), {@code connect} </li>
 * <li>Gateway {@code hello-ok} </li>
 * </ol>
 *
 * <h3></h3>
 * <p>version v3, {@code platform} {@code deviceFamily}.
 * v2 Used for.</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/protocol">Gateway Protocol</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectChallenge {

    /**
 * .
 * <p> {@code connect} {@code device.nonce} fieldvalue.</p>
 * <p> nonce , {@code DEVICE_AUTH_NONCE_MISMATCH} .</p>
     */
    private String nonce;

    /**
 * (Unix epoch milliseconds).
 * <p>Used for., {@code DEVICE_AUTH_SIGNATURE_EXPIRED} .</p>
     */
    private Long ts;
}
