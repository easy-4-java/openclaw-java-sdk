package io.github.easy4j.openclaw.cli;

import lombok.Getter;

import java.util.Objects;

/**
 * {@code openclaw} process execution result(;{@code --json} {@link #stdout}).
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
public final class OpenClawCliResult {

    private final int exitCode;
    private final String stdout;
    private final String stderr;

    public OpenClawCliResult(int exitCode, String stdout, String stderr) {
        this.exitCode = exitCode;
        this.stdout = stdout != null ? stdout : "";
        this.stderr = stderr != null ? stderr : "";
    }

    /**
 * @return process 0
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    @Override
    public String toString() {
        return "OpenClawCliResult{exitCode=" + exitCode + ", stdout.len=" + stdout.length()
                + ", stderr.len=" + stderr.length() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenClawCliResult)) {
            return false;
        }
        OpenClawCliResult that = (OpenClawCliResult) o;
        return exitCode == that.exitCode && Objects.equals(stdout, that.stdout) && Objects.equals(stderr, that.stderr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exitCode, stdout, stderr);
    }
}
