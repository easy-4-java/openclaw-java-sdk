package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Responses API 接受的文本、JSON 对象或 JSON Schema 格式枚举。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum ResponseFormatType {

    /**
     * 表示模型响应格式的 {@code text} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    TEXT("text"),
    /**
     * 表示模型响应格式的 {@code json_object} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    JSON_OBJECT("json_object"),
    /**
     * 表示模型响应格式的 {@code json_schema} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    JSON_SCHEMA("json_schema");

    /**
     * JSON 属性 {@code value}，表示协议标量值。
     */
    private final String value;

    ResponseFormatType(String value) {
        this.value = value;
    }

    /**
     * 返回序列化到 JSON 的响应格式类型字符串。
     *
     * @return 写入响应格式 {@code type} 字段的协议值
     */
    @JsonValue
    public String value() {
        return value;
    }
}
