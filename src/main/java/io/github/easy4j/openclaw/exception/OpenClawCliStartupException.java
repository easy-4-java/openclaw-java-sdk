package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityReport;
import lombok.Getter;

/**
 * OpenClaw CLI fail-fast .
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Getter
public class OpenClawCliStartupException extends RuntimeException {

    private final OpenClawCliAvailabilityReport availabilityReport;

    /**
 * @param message diagnostic
 * @param report probe report
     */
    public OpenClawCliStartupException(String message, OpenClawCliAvailabilityReport report) {
        super(message);
        this.availabilityReport = report;
    }
}
