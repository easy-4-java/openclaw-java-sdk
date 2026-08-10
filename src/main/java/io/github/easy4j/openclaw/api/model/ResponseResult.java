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
 * Responses API 完整结果，包含状态、输出项和 Token 用量。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResult {

    /**
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    private String id;

    /**
     * JSON 属性 {@code object}，表示响应资源类型。
     */
    private String object;

    /**
     * Responses API 返回的生成任务状态。
     */
    private String status;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code output}，按协议顺序保存模型或工具输出列表。
     */
    private List<Map<String, Object>> output;

    /**
     * JSON 属性 {@code usage}，表示Token 用量统计。
     */
    private Usage usage;

    /**
     * 模型请求的输入、输出和总 Token 用量统计。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        /**
         * JSON 属性 {@code inputTokens}，表示输入 Token 数。
         */
        @JsonProperty("input_tokens")
        private Integer inputTokens;
        /**
         * JSON 属性 {@code outputTokens}，表示输出 Token 数。
         */
        @JsonProperty("output_tokens")
        private Integer outputTokens;
        /**
         * JSON 属性 {@code totalTokens}，表示总 Token 数。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
