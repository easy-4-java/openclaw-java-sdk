package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * OpenAI/OpenClaw .
 *
 * <h3></h3>
 * <table>
 * <tr><th>type</th><th></th><th>json_schema?</th></tr>
 * <tr><td>{@code "text"}</td><td></td><td></td></tr>
 * <tr><td>{@code "json_object"}</td><td> JSON</td><td></td></tr>
 * <tr><td>{@code "json_schema"}</td><td>( JSON Schema)</td><td><b></b></td></tr>
 * </table>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * // JSON
 * ResponseFormat.jsonObject()
 *
 * // (JSON Schema)
 * ResponseFormat.builder()
 *     .type(ResponseFormatType.JSON_SCHEMA)
 *     .jsonSchema(ResponseFormatJsonSchema.builder()
 *         .name("article")
 *         .strict(true)
 *         .schema(Map.of("type", "object", "properties", ...))
 *         .build())
 *     .build();
 * }</pre>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
 * @see <a href="https://platform.openai.com/docs/guides/structured-outputs">Structured Outputs</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormat {

    /**
 * .
     */
    @JsonProperty("type")
    private ResponseFormatType type;

    /**
 * only {@code type = "json_schema"} .
 * <p> name,schema,strict field.</p>
     */
    @JsonProperty("json_schema")
    private ResponseFormatJsonSchema jsonSchema;

    // ---- 便捷工厂方法 ----

    /**
 * JSON .
     */
    public static ResponseFormat jsonObject() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON_OBJECT)
                .build();
    }

    /**
 * .
     */
    public static ResponseFormat text() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.TEXT)
                .build();
    }
}
