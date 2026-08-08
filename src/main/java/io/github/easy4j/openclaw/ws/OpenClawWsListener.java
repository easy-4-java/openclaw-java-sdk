package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import io.github.easy4j.openclaw.ws.protocol.ResponseFrame;

/**
 * Gateway WebSocket event listener.
 * <p> Gateway event.</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/protocol">Gateway Protocol</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public interface OpenClawWsListener {

    /**
 * WS connectioncompletion {@code connect} handshake.
     *
 * @param helloOk Gateway handshake
     */
    default void onConnected(HelloOk helloOk) {}

    /**
 * WS connection.
     *
 * @param code
 * @param reason
 * @param remote
     */
    default void onDisconnected(int code, String reason, boolean remote) {}

    /**
 * WS connection.
     *
 * @param ex
     */
    default void onError(Exception ex) {}

    /**
 * Gateway event.
 * <p>Seeevent:{@code chat}(agent),{@code agent}(agent),
 * {@code tick}(heartbeat),{@code shutdown}(Gateway ).</p>
     *
 * @param frame event
     */
    default void onEvent(EventFrame frame) {}

    /**
 * Gateway RPC ( RPC ).
     *
 * @param frame
     */
    default void onResponse(ResponseFrame frame) {}
}
