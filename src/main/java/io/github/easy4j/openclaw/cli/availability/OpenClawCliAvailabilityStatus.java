package io.github.easy4j.openclaw.cli.availability;

/**
 * `OpenClawCliAvailabilityStatus` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum OpenClawCliAvailabilityStatus {

    /**
     * 选择 `available` 协议模式；序列化时使用该固定取值。
     */
    AVAILABLE,

    /**
     * 选择 `executable_not_configured` 协议模式；序列化时使用该固定取值。
     */
    EXECUTABLE_NOT_CONFIGURED,

    /**
     * 选择 `executable_not_found` 协议模式；序列化时使用该固定取值。
     */
    EXECUTABLE_NOT_FOUND,

    /**
     * 选择 `executable_not_executable` 协议模式；序列化时使用该固定取值。
     */
    EXECUTABLE_NOT_EXECUTABLE,

    /**
     * 选择 `spawn_failed` 协议模式；序列化时使用该固定取值。
     */
    SPAWN_FAILED,

    /**
     * 选择 `non_zero_exit` 协议模式；序列化时使用该固定取值。
     */
    NON_ZERO_EXIT,

    /**
     * 选择 `timeout` 协议模式；序列化时使用该固定取值。
     */
    TIMEOUT,

    /**
     * 选择 `failed` 协议模式；序列化时使用该固定取值。
     */
    FAILED
}
