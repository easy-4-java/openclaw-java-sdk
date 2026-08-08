package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * OpenResponses API non-streaming.
 * <p>
 * Corresponds to {@code POST /v1/responses}({@code stream: false}) JSON.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openresponses-http-api">OpenResponses API</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResult {

 /** . */
    private String id;

 /** object, {@code "response"}. */
    private String object;

    /**
 * .
     * <ul>
 * <li>{@code "completed"} - completion</li>
 * <li>{@code "failed"} - </li>
 * <li>{@code "in_progress"} - (streaming)</li>
     * </ul>
     */
    private String status;

 /** agent . */
    private String model;

    /**
 * .
 * <p>messageobject, {@code type},{@code role},{@code content} field.</p>
     */
    private List<Map<String, Object>> output;

    /**
 * Token .
     */
    private Usage usage;

    /**
 * Token .
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        @JsonProperty("input_tokens")
        private Integer inputTokens;
        @JsonProperty("output_tokens")
        private Integer outputTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
