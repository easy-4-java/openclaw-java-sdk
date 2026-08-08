package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * Gateway HTTP OpenClaw .
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public class OpenClawHttpException extends OpenClawException {

    private static final long serialVersionUID = 1L;

 /** HTTP status code; -1 */
    private final int statusCode;

 /** response body, null */
    private final String responseBody;

    public OpenClawHttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public OpenClawHttpException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.responseBody = null;
    }
}
