package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `ResponseFormatJsonSchema` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormatJsonSchema {

    /**
     * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
     */
    @JsonProperty("name")
    private String name;

    /**
     * 映射 OpenClaw JSON 字段 `schema` 的 键值对象。
     */
    @JsonProperty("schema")
    private Map<String, Object> schema;

    /**
     * 映射 OpenClaw JSON 字段 `strict` 的 布尔开关。
     */
    @JsonProperty("strict")
    private Boolean strict;

    /**
     * 映射 OpenClaw JSON 字段 `description` 的 协议内容。
     */
    @JsonProperty("description")
    private String description;
}
