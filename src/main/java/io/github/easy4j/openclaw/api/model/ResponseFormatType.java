package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * OpenClaw JSON 协议中的 `ResponseFormatType` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum ResponseFormatType {

    /**
     * 选择 `text` 协议模式；序列化时使用该固定取值。
     */
    TEXT("text"),
    /**
     * 选择 `json_object` 协议模式；序列化时使用该固定取值。
     */
    JSON_OBJECT("json_object"),
    /**
     * 选择 `json_schema` 协议模式；序列化时使用该固定取值。
     */
    JSON_SCHEMA("json_schema");

    /**
     * 映射 OpenClaw JSON 字段 `value` 的 协议内容。
     */
    private final String value;

    ResponseFormatType(String value) {
        this.value = value;
    }

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `ResponseFormatType` 中的 `value` 数据。
     *
     * @return 服务返回或流式累积得到的文本
     */
    @JsonValue
    public String value() {
        return value;
    }
}
