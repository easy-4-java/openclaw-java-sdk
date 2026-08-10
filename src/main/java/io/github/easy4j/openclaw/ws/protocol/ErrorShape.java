package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * OpenClaw JSON 协议中的 `ErrorShape` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@ToString(of = {"code", "message"})
public class ErrorShape {

    /**
     * 映射 OpenClaw JSON 字段 `code` 的 协议内容。
     */
    private final String code;
    /**
     * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
     */
    private final String message;
    /**
     * 映射 OpenClaw JSON 字段 `details` 的 协议内容。
     */
    private final Object details;
    /**
     * 映射 OpenClaw JSON 字段 `retryable` 的 布尔开关。
     */
    private final Boolean retryable;
    /**
     * 映射 OpenClaw JSON 字段 `retryAfterMs` 的 协议内容。
     */
    private final Integer retryAfterMs;

    /**
     * 按协议字段创建 `ErrorShape`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param code 写入 `code` 协议字段的内容
     * @param message 消息正文
     * @param details 写入 `details` 协议字段的内容
     * @param retryable 写入 `retryable` 协议字段的内容
     * @param retryAfterMs 写入 `retryAfterMs` 协议字段的内容
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
