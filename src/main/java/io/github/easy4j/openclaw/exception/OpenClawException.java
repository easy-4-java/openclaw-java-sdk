package io.github.easy4j.openclaw.exception;

/**
 * root exception:OpenClaw SDK .
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
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
