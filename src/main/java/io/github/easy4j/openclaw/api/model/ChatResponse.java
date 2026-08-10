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
 * 非流式 Chat Completions 响应，包含候选消息和 Token 用量。
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
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    private String id;

    /**
     * JSON 属性 {@code object}，表示响应资源类型。
     */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION;

    /**
     * JSON 属性 {@code created}，表示创建时间戳。
     */
    private Long created;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code choices}，表示候选响应。
     */
    private List<Choice> choices;

    /**
     * JSON 属性 {@code usage}，表示Token 用量统计。
     */
    private Usage usage;

    /**
     * 单个聊天候选消息及结束原因。
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
         * JSON 属性 {@code index}，表示片段或工具调用序号。
         */
        private Integer index;

        /**
         * 当前候选项生成的助手消息。
         */
        private ChatMessage message;

        /**
         * JSON 属性 {@code finishReason}，表示生成结束原因。
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 判断聊天响应是否因正常 stop 原因结束。
         *
         * @return 结束原因等于 {@code stop} 时返回 {@code true}
         */
        public boolean isStop() {
            return OpenClawConstants.FINISH_REASON_STOP.equals(finishReason);
        }

        /**
         * 判断响应是否包含工具调用或以工具调用原因结束。
         *
         * @return 结束原因等于 {@code tool_calls} 时返回 {@code true}
         */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }

        /**
         * 判断聊天响应是否因达到长度限制结束。
         *
         * @return 结束原因等于 {@code length} 时返回 {@code true}
         */
        public boolean isLength() {
            return OpenClawConstants.FINISH_REASON_LENGTH.equals(finishReason);
        }
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
         * JSON 属性 {@code completionTokens}，表示补全 Token 数。
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        /**
         * JSON 属性 {@code totalTokens}，表示总 Token 数。
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
