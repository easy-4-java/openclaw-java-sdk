package io.github.easy4j.openclaw;

import lombok.Data;

/**
 * OpenClaw local CLI client configuration.
 * <p>
 * Covers {@code openclaw} executable,timeout,concurrency,working directory CLI .
 * </p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Data
public class OpenClawCliConfig {

    /**
 * Whether to enable CLI system.
 * <p>When false,skips CLI .</p>
     */
    private boolean enabled = true;

    /**
 * At startupWhether to probe {@code openclaw --version}.
     */
    private boolean startupCheckEnabled = false;

    /**
 * CLI Whether to fail fast(interrupts construction).
 * <p>Defaults to false only logs a warning;productionwhen true.</p>
     */
    private boolean failFastOnUnavailable = false;

    /**
 * executable name
     */
    private String executable = "openclaw";

    /**
 * agent timeout(seconds)
     */
    private int timeout = 300;

    /**
 * CLI subprocessworking directory;When empty, JVM directory.
     */
    private String workingDirectory;

    /**
 * CLI subprocessmaximum concurrency; 0 CPU 2 value.
     */
    private int maxConcurrentExecutions = 0;

    /**
 * timeout(seconds)
     */
    private int probeTimeoutSeconds = 5;

}
