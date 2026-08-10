package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 向量嵌入端点响应，包含模型、向量数组和 Token 用量。
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
     * JSON 属性 {@code object}，表示响应资源类型。
     */
    private String object;

    /**
     * JSON 属性 {@code data}，表示响应数据条目。
     */
    private List<EmbeddingData> data;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code usage}，表示Token 用量统计。
     */
    private Usage usage;

    /**
     * 单条浮点向量及其在响应数组中的索引。
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
         * JSON 属性 {@code object}，表示响应资源类型。
         */
        private String object;
        /**
         * JSON 属性 {@code embedding}，表示向量元素。
         */
        private List<Double> embedding;
        /**
         * JSON 属性 {@code index}，表示片段或工具调用序号。
         */
        private Integer index;
    }

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
         * JSON 属性 {@code promptTokens}，表示输入 Token 数。
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        /**
         * JSON 属性 {@code totalTokens}，表示总 Token 数。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
