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
 * OpenClaw JSON 协议中的 `ChatChunk` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    private String id;

    /**
     * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
     */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION_CHUNK;

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
    private List<DeltaChoice> choices;

    /**
     * OpenClaw JSON 协议中的 `DeltaChoice` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
         * 映射 OpenClaw JSON 字段 `index` 的 协议内容。
         */
        private Integer index;

        /**
         * 映射 OpenClaw JSON 字段 `delta` 的 协议内容。
         */
        private DeltaMessage delta;

        /**
         * 映射 OpenClaw JSON 字段 `finishReason` 的 协议内容。
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 判断 `toolCalls` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }
    }

    /**
     * OpenClaw JSON 协议中的 `DeltaMessage` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
         * 映射 OpenClaw JSON 字段 `role` 的 协议内容。
         */
        private String role;
        /**
         * 映射 OpenClaw JSON 字段 `content` 的 协议内容。
         */
        private String content;
        /**
         * 映射 OpenClaw JSON 字段 `toolCalls` 的 有序数组。
         */
        @JsonProperty("tool_calls")
        private List<ChatMessage.ToolCall> toolCalls;
    }
}
