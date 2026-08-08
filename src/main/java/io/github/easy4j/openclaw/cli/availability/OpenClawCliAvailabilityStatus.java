package io.github.easy4j.openclaw.cli.availability;

/**
 * OpenClaw CLI .
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public enum OpenClawCliAvailabilityStatus {

 /** {@code openclaw --version} . */
    AVAILABLE,

 /** executable. */
    EXECUTABLE_NOT_CONFIGURED,

 /** PATH . */
    EXECUTABLE_NOT_FOUND,

 /** . */
    EXECUTABLE_NOT_EXECUTABLE,

 /** process. */
    SPAWN_FAILED,

 /** . */
    NON_ZERO_EXIT,

 /** timeout. */
    TIMEOUT,

 /** . */
    FAILED
}
