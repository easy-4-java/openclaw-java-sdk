package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Gateway 错误帧结构，包含错误码、详情、可重试标记和等待时间。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@ToString(of = {"code", "message"})
public class ErrorShape {

    /**
     * JSON 属性 {@code code}，表示机器可读错误码。
     */
    private final String code;
    /**
     * 服务端返回的可读错误说明。
     */
    private final String message;
    /**
     * JSON 属性 {@code details}，表示结构化详情。
     */
    private final Object details;
    /**
     * JSON 属性 {@code retryable}，表示错误是否允许重试。
     */
    private final Boolean retryable;
    /**
     * JSON 属性 {@code retryAfterMs}，表示建议重试等待毫秒数。
     */
    private final Integer retryAfterMs;

    /**
     * 从 Gateway 错误帧恢复错误码、详情、可重试标记和重试等待时间。
     *
     * @param code Gateway 返回的机器可读错误码
     * @param message 消息正文
     * @param details 错误或探测结果的结构化详情
     * @param retryable 该失败是否允许调用方重试
     * @param retryAfterMs 建议重试等待时间，单位为毫秒
     */
    @JsonCreator
    public ErrorShape(
            @JsonProperty("code") String code,
            @JsonProperty("message") String message,
            @JsonProperty("details") Object details,
            @JsonProperty("retryable") Boolean retryable,
            @JsonProperty("retryAfterMs") Integer retryAfterMs) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.retryable = retryable;
        this.retryAfterMs = retryAfterMs;
    }
}
