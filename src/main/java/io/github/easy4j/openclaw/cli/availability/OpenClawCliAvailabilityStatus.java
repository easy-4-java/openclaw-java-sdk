package io.github.easy4j.openclaw.cli.availability;

/**
 * 定义CLI 可用性探测状态允许的固定取值及其 CLI/JSON 序列化拼写。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum OpenClawCliAvailabilityStatus {

    /**
     * 表示CLI 可用性探测状态的 {@code available} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    AVAILABLE,

    /**
     * 表示CLI 可用性探测状态的 {@code executable_not_configured} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    EXECUTABLE_NOT_CONFIGURED,

    /**
     * 表示CLI 可用性探测状态的 {@code executable_not_found} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    EXECUTABLE_NOT_FOUND,

    /**
     * 表示CLI 可用性探测状态的 {@code executable_not_executable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    EXECUTABLE_NOT_EXECUTABLE,

    /**
     * 表示CLI 可用性探测状态的 {@code spawn_failed} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    SPAWN_FAILED,

    /**
     * 表示CLI 可用性探测状态的 {@code non_zero_exit} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    NON_ZERO_EXIT,

    /**
     * 表示CLI 可用性探测状态的 {@code timeout} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    TIMEOUT,

    /**
     * 表示CLI 可用性探测状态的 {@code failed} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    FAILED
}
