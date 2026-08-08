package io.github.easy4j.openclaw.exception;

/**
 * root exception:OpenClaw SDK .
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public class OpenClawException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OpenClawException(String message) {
        super(message);
    }

    public OpenClawException(String message, Throwable cause) {
        super(message, cause);
    }
}
