package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenClaw JSON 协议中的 `EmbeddingsResponse` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddingsResponse {

    /**
     * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
     */
    private String object;

    /**
     * 映射 OpenClaw JSON 字段 `data` 的 有序数组。
     */
    private List<EmbeddingData> data;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `usage` 的 协议内容。
     */
    private Usage usage;

    /**
     * OpenClaw JSON 协议中的 `EmbeddingData` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbeddingData {
        /**
         * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
         */
        private String object;
        /**
         * 映射 OpenClaw JSON 字段 `embedding` 的 有序数组。
         */
        private List<Double> embedding;
        /**
         * 映射 OpenClaw JSON 字段 `index` 的 协议内容。
         */
        private Integer index;
    }

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
         * 映射 OpenClaw JSON 字段 `promptTokens` 的 协议内容。
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        /**
         * 映射 OpenClaw JSON 字段 `totalTokens` 的 协议内容。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
