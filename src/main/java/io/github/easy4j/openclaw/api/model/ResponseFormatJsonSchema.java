package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * JSON Schema (only {@code type = "json_schema"} ).
 *
 * <h3>field</h3>
 * <ul>
 * <li>{@code name} — Schema (Required)</li>
 * <li>{@code schema} — JSON Schema (Required)</li>
 * <li>{@code strict} — Whether to enable(Optional,Defaults to false)</li>
 * <li>{@code description} — Schema (Optional)</li>
 * </ul>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * ResponseFormatJsonSchema.builder()
 *     .name("article")
 *     .strict(true)
 *     .schema(Map.of("type", "object",
 *         "properties", Map.of("title", Map.of("type", "string"))))
 *     .build();
 * }</pre>
 *
 * @see <a href="https://platform.openai.com/docs/guides/structured-outputs">OpenAI Structured Outputs</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormatJsonSchema {

    /**
 * Schema (Required).
     */
    @JsonProperty("name")
    private String name;

    /**
 * JSON Schema (Required).
 * <p>:{@code { "type": "object", "properties": { ... }, "required": [...] }}</p>
     */
    @JsonProperty("schema")
    private Map<String, Object> schema;

    /**
 * Whether to enable(Optional).
 * <p> Schema,.Defaults to false.</p>
     */
    @JsonProperty("strict")
    private Boolean strict;

    /**
 * Schema (Optional).
     */
    @JsonProperty("description")
    private String description;
}
