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
 * OpenClaw JSON 协议中的 `ResponseResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    private String id;

    /**
     * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
     */
    private String object;

    /**
     * 映射 OpenClaw JSON 字段 `status` 的 协议内容。
     */
    private String status;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `output` 的 有序数组。
     */
    private List<Map<String, Object>> output;

    /**
     * 映射 OpenClaw JSON 字段 `usage` 的 协议内容。
     */
    private Usage usage;

    /**
     * OpenClaw JSON 协议中的 `Usage` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
         * 映射 OpenClaw JSON 字段 `inputTokens` 的 协议内容。
         */
        @JsonProperty("input_tokens")
        private Integer inputTokens;
        /**
         * 映射 OpenClaw JSON 字段 `outputTokens` 的 协议内容。
         */
        @JsonProperty("output_tokens")
        private Integer outputTokens;
        /**
         * 映射 OpenClaw JSON 字段 `totalTokens` 的 协议内容。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
