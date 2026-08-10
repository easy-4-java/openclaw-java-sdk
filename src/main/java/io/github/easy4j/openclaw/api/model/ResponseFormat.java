package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * OpenClaw JSON 协议中的 `ResponseFormat` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormat {

    /**
     * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
     */
    @JsonProperty("type")
    private ResponseFormatType type;

    /**
     * 映射 OpenClaw JSON 字段 `jsonSchema` 的 协议内容。
     */
    @JsonProperty("json_schema")
    private ResponseFormatJsonSchema jsonSchema;

    // ---- 便捷工厂方法 ----

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `ResponseFormat` 中的 `jsonObject` 数据。
     *
     * @return 按当前参数创建、查询或解析得到的 ResponseFormat
     */
    public static ResponseFormat jsonObject() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON_OBJECT)
                .build();
    }

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `ResponseFormat` 中的 `text` 数据。
     *
     * @return 按当前参数创建、查询或解析得到的 ResponseFormat
     */
    public static ResponseFormat text() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.TEXT)
                .build();
    }
}
