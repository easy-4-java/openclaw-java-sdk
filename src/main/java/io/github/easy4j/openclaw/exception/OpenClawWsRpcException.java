package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.ws.protocol.ErrorShape;
import lombok.Getter;

/**
 * Gateway WebSocket RPC {@code ok: false} .
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public class OpenClawWsRpcException extends OpenClawException {

    private static final long serialVersionUID = 1L;

 /** RPC , {@code sessions.list} */
    private final String method;

 /** Gateway error structure */
    private final ErrorShape error;

    public OpenClawWsRpcException(String method, ErrorShape error) {
        super(buildMessage(method, error));
        this.method = method;
        this.error = error;
    }

    private static String buildMessage(String method, ErrorShape error) {
        if (error != null && error.getMessage() != null) {
            return "Gateway RPC failed: " + method + " — " + error.getMessage();
        }
        return "Gateway RPC failed: " + method;
    }
}
