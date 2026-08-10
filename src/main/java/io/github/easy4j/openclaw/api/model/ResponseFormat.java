package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 模型响应格式约束，可选择文本、JSON 对象或 JSON Schema。
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
     * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
     */
    @JsonProperty("type")
    private ResponseFormatType type;

    /**
     * JSON 属性 {@code jsonSchema}，表示JSON Schema 响应约束。
     */
    @JsonProperty("json_schema")
    private ResponseFormatJsonSchema jsonSchema;

    // ---- 便捷工厂方法 ----

    /**
     * 创建要求服务返回 JSON 对象的响应格式。
     *
     * @return 要求服务返回 JSON 对象的响应格式
     */
    public static ResponseFormat jsonObject() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON_OBJECT)
                .build();
    }

    /**
     * 创建要求服务返回普通文本的响应格式。
     *
     * @return 要求服务返回普通文本的响应格式
     */
    public static ResponseFormat text() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.TEXT)
                .build();
    }
}
