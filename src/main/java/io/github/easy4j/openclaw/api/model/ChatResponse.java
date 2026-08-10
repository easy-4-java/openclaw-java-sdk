package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenClaw JSON 协议中的 `ChatResponse` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

    /**
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    private String id;

    /**
     * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
     */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION;

    /**
     * 映射 OpenClaw JSON 字段 `created` 的 协议内容。
     */
    private Long created;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `choices` 的 有序数组。
     */
    private List<Choice> choices;

    /**
     * 映射 OpenClaw JSON 字段 `usage` 的 协议内容。
     */
    private Usage usage;

    /**
     * OpenClaw JSON 协议中的 `Choice` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {

        /**
         * 映射 OpenClaw JSON 字段 `index` 的 协议内容。
         */
        private Integer index;

        /**
         * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
         */
        private ChatMessage message;

        /**
         * 映射 OpenClaw JSON 字段 `finishReason` 的 协议内容。
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 判断 `stop` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isStop() {
            return OpenClawConstants.FINISH_REASON_STOP.equals(finishReason);
        }

        /**
         * 判断 `toolCalls` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }

        /**
         * 判断 `length` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isLength() {
            return OpenClawConstants.FINISH_REASON_LENGTH.equals(finishReason);
        }
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
         * 映射 OpenClaw JSON 字段 `completionTokens` 的 协议内容。
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        /**
         * 映射 OpenClaw JSON 字段 `totalTokens` 的 协议内容。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
