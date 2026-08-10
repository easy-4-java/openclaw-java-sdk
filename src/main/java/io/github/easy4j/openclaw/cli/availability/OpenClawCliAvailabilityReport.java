package io.github.easy4j.openclaw.cli.availability;

import io.github.easy4j.openclaw.cli.OpenClawCliResult;
import lombok.Builder;
import lombok.Getter;

/**
 * OpenClaw CLI /.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
public class OpenClawCliAvailabilityReport {

    private final OpenClawCliAvailabilityStatus status;
    private final boolean available;
    private final String configuredExecutable;
    private final String resolvedExecutablePath;
    private final String message;
    private final OpenClawCliResult probeResult;

    /**
 * @return security {@code openclaw}
     */
    public boolean isAvailable() {
        return available;
    }

    /**
 * /diagnostic.
     *
 * @return characters
     */
    public String toDiagnosticMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenClaw CLI ");
        sb.append(available ? "ready" : "unavailable");
        sb.append(" [").append(status).append(']');
        if (configuredExecutable != null) {
            sb.append(" executable=").append(configuredExecutable);
        }
        if (resolvedExecutablePath != null) {
            sb.append(" resolved=").append(resolvedExecutablePath);
        }
        if (message != null && !message.isEmpty()) {
            sb.append(" — ").append(message);
        }
        return sb.toString();
    }
}
