package io.github.easy4j.openclaw.cli.availability;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import io.github.easy4j.openclaw.cli.OpenClawCli;
import io.github.easy4j.openclaw.cli.OpenClawCliExecutor;
import io.github.easy4j.openclaw.cli.OpenClawCliResult;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * 本地 openclaw CLI 的 {@code OpenClawCliAvailabilityChecker} 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class OpenClawCliAvailabilityChecker {

    /**
     * 在短超时内执行 {@code openclaw --version}，区分文件缺失、超时、非零退出和正常版本输出。
     *
     * @param config SDK 配置
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 OpenClawCliAvailabilityReport
     */
    public OpenClawCliAvailabilityReport check(OpenClawCliConfig config) {
        Objects.requireNonNull(config, "config");
        String configured = config.getExecutable();
        if (OpenClawStrings.isBlank(configured)) {
            return unavailable(
                    OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_CONFIGURED,
                    configured,
                    null,
                    "openclaw.local.executable is blank",
                    null);
        }
        String trimmed = configured.trim();
        Optional<String> resolved = resolveExecutablePath(trimmed);
        if (!resolved.isPresent()) {
            if (looksLikePath(trimmed)) {
                File file = new File(trimmed);
                if (!file.exists()) {
                    return unavailable(
                            OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_FOUND,
                            trimmed,
                            null,
                            "executable file does not exist: " + file.getAbsolutePath(),
                            null);
                }
                return unavailable(
                        OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_EXECUTABLE,
                        trimmed,
                        file.getAbsolutePath(),
                        "executable exists but is not executable: " + file.getAbsolutePath(),
                        null);
            }
            return unavailable(
                    OpenClawCliAvailabilityStatus.EXECUTABLE_NOT_FOUND,
                    trimmed,
                    null,
                    "executable not found on PATH: " + trimmed,
                    null);
        }

        OpenClawCliConfig probeConfig = copyForProbe(config);
        OpenClawCliExecutor probeExecutor = new OpenClawCliExecutor(probeConfig);
        OpenClawCliResult result = new OpenClawCli(probeExecutor).version();
        if (result.isSuccess()) {
            return OpenClawCliAvailabilityReport.builder()
                    .status(OpenClawCliAvailabilityStatus.AVAILABLE)
                    .available(true)
                    .configuredExecutable(trimmed)
                    .resolvedExecutablePath(resolved.get())
                    .message("openclaw --version succeeded")
                    .probeResult(result)
                    .build();
        }
        if (result.getExitCode() == -1 && containsTimeoutHint(result)) {
            return unavailable(
                    OpenClawCliAvailabilityStatus.TIMEOUT,
                    trimmed,
                    resolved.get(),
                    "openclaw --version timed out",
                    result);
        }
        if (result.getExitCode() == -1 && isSpawnFailure(result)) {
            return unavailable(
                    OpenClawCliAvailabilityStatus.SPAWN_FAILED,
                    trimmed,
                    resolved.get(),
                    firstLine(result.getStderr()),
                    result);
        }
        return unavailable(
                OpenClawCliAvailabilityStatus.NON_ZERO_EXIT,
                trimmed,
                resolved.get(),
                "openclaw --version exitCode=" + result.getExitCode(),
                result);
    }

    /**
 * executable:/; {@code PATH} .
     */
    static Optional<String> resolveExecutablePath(String executable) {
        if (OpenClawStrings.isBlank(executable)) {
            return Optional.empty();
        }
        String trimmed = executable.trim();
        File direct = new File(trimmed);
        if (looksLikePath(trimmed)) {
            if (direct.isFile() && direct.canExecute()) {
                return Optional.of(direct.getAbsolutePath());
            }
            return Optional.empty();
        }
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            return Optional.empty();
        }
        for (String dir : pathEnv.split(File.pathSeparator)) {
            if (OpenClawStrings.isBlank(dir)) {
                continue;
            }
            File candidate = new File(dir.trim(), trimmed);
            if (candidate.isFile() && candidate.canExecute()) {
                return Optional.of(candidate.getAbsolutePath());
            }
        }
        return Optional.empty();
    }

    private static OpenClawCliConfig copyForProbe(OpenClawCliConfig source) {
        OpenClawCliConfig copy = new OpenClawCliConfig();
        copy.setExecutable(source.getExecutable());
        copy.setWorkingDirectory(source.getWorkingDirectory());
        copy.setMaxConcurrentExecutions(source.getMaxConcurrentExecutions());
        int probeSec = source.getProbeTimeoutSeconds();
        if (probeSec <= 0) {
            probeSec = 5;
        }
        copy.setTimeout(probeSec);
        copy.setProbeTimeoutSeconds(probeSec);
        return copy;
    }

    private static boolean looksLikePath(String executable) {
        return executable.contains("/") || executable.contains("\\") || new File(executable).isAbsolute();
    }

    private static boolean containsTimeoutHint(OpenClawCliResult result) {
        String combined = result.getStderr() + result.getStdout();
        return combined.toLowerCase().contains("timed out");
    }

    private static boolean isSpawnFailure(OpenClawCliResult result) {
        String stderr = result.getStderr();
        return stderr != null
                && (stderr.contains("could not be started")
                        || stderr.contains("No such file")
                        || stderr.contains("spawn"));
    }

    private static String firstLine(String text) {
        if (OpenClawStrings.isBlank(text)) {
            return "openclaw execution failed";
        }
        int idx = text.indexOf('\n');
        return idx >= 0 ? text.substring(0, idx) : text;
    }

    private static OpenClawCliAvailabilityReport unavailable(
            OpenClawCliAvailabilityStatus status,
            String configured,
            String resolved,
            String message,
            OpenClawCliResult partial) {
        return OpenClawCliAvailabilityReport.builder()
                .status(status)
                .available(false)
                .configuredExecutable(configured)
                .resolvedExecutablePath(resolved)
                .message(message)
                .probeResult(partial)
                .build();
    }
}
