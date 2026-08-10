package io.github.easy4j.openclaw.api.model;

import io.github.easy4j.openclaw.api.OpenClawConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Chat Completions SSE 片段，包含候选增量及结束原因。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatChunk {

    /**
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    private String id;

    /**
     * JSON 属性 {@code object}，表示响应资源类型。
     */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION_CHUNK;

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
    private List<DeltaChoice> choices;

    /**
     * 单个流式候选的序号、消息增量和结束原因。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeltaChoice {
        /**
         * JSON 属性 {@code index}，表示片段或工具调用序号。
         */
        private Integer index;

        /**
         * JSON 属性 {@code delta}，表示流式文本增量。
         */
        private DeltaMessage delta;

        /**
         * JSON 属性 {@code finishReason}，表示生成结束原因。
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 判断响应是否包含工具调用或以工具调用原因结束。
         *
         * @return 当前增量包含工具调用时返回 {@code true}
         */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }
    }

    /**
     * 流式候选携带的角色、文本增量和工具调用片段。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeltaMessage {
        /**
         * JSON 属性 {@code role}，表示聊天消息角色。
         */
        private String role;
        /**
         * JSON 属性 {@code content}，表示消息或输出正文。
         */
        private String content;
        /**
         * JSON 属性 {@code toolCalls}，表示工具调用。
         */
        @JsonProperty("tool_calls")
        private List<ChatMessage.ToolCall> toolCalls;
    }
}
