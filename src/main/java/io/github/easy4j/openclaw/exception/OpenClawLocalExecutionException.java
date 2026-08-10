package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * OpenClaw .
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
public class OpenClawLocalExecutionException extends OpenClawException {

    private static final long serialVersionUID = 1L;

 /** processexit code;completion {@link Integer#MIN_VALUE} */
    private final int exitCode;

    public OpenClawLocalExecutionException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public OpenClawLocalExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.exitCode = Integer.MIN_VALUE;
    }
}
