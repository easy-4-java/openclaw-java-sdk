package io.github.easy4j.openclaw.cli.availability;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import io.github.easy4j.openclaw.cli.support.MockOpenClawCli;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link OpenClawCliAvailabilityChecker} 单元测试。
 */
class OpenClawCliAvailabilityCheckerTest {

    @TempDir
    Path tempDir;

    @Test
    void checkShouldSucceedWithMockExecutable() throws Exception {
        MockOpenClawCli mock = MockOpenClawCli.install();
        OpenClawCliAvailabilityReport report = new OpenClawCliAvailabilityChecker().check(mock.newConfig());

        assertTrue(report.isAvailable());
        assertEquals(OpenClawCliAvailabilityStatus.AVAILABLE, report.getStatus());
    }

    @Test
    void checkShouldFailWhenExecutableMissing() {
        OpenClawCliConfig config = new OpenClawCliConfig();
        config.setExecutable("/nonexistent/openclaw-startup-test");
        config.setProbeTimeoutSeconds(3);

        OpenClawCliAvailabilityReport report = new OpenClawCliAvailabilityChecker().check(config);

        assertFalse(report.isAvailable());
        assertEquals(OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_FOUND, report.getStatus());
    }

    @Test
    void checkShouldClassifyBlankNonExecutablePathAndNonZeroExit() throws Exception {
        OpenClawCliAvailabilityChecker checker = new OpenClawCliAvailabilityChecker();
        OpenClawCliConfig config = new OpenClawCliConfig();
        config.setExecutable(" ");
        assertEquals(OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_CONFIGURED, checker.check(config).getStatus());

        Path nonExecutable = tempDir.resolve("not-executable");
        Files.write(nonExecutable, "#!/bin/sh\nexit 0\n".getBytes(StandardCharsets.UTF_8));
        nonExecutable.toFile().setExecutable(false);
        config.setExecutable(nonExecutable.toString());
        OpenClawCliAvailabilityReport nonExecutableReport = checker.check(config);
        assertEquals(OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_EXECUTABLE, nonExecutableReport.getStatus());
        assertTrue(nonExecutableReport.toDiagnosticMessage().contains("unavailable"));

        Path failed = tempDir.resolve("failed");
        Files.write(failed, "#!/bin/sh\necho failure >&2\nexit 3\n".getBytes(StandardCharsets.UTF_8));
        failed.toFile().setExecutable(true);
        config.setExecutable(failed.toString());
        config.setProbeTimeoutSeconds(0);
        OpenClawCliAvailabilityReport failedReport = checker.check(config);
        assertEquals(OpenClawCliAvailabilityStatus.NON_ZERO_EXIT, failedReport.getStatus());
        assertFalse(failedReport.isAvailable());

        config.setExecutable("definitely-not-an-openclaw-command");
        assertEquals(OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_FOUND, checker.check(config).getStatus());
    }
}
