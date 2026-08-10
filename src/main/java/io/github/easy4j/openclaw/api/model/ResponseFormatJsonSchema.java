package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * JSON Schema 响应格式的名称、Schema、严格模式和说明。
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
     * 服务端用于标识该响应 JSON Schema 的名称。
     */
    @JsonProperty("name")
    private String name;

    /**
     * JSON 属性 {@code schema}，表示响应 JSON Schema。
     */
    @JsonProperty("schema")
    private Map<String, Object> schema;

    /**
     * JSON 属性 {@code strict}，表示是否严格校验 JSON Schema。
     */
    @JsonProperty("strict")
    private Boolean strict;

    /**
     * 向模型说明预期结构用途的文本。
     */
    @JsonProperty("description")
    private String description;
}
