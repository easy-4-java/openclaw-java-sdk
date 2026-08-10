package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenClaw JSON 协议中的 `ChatMessage` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage {

    /**
     * OpenClaw 协议固定值 {@code OpenClawConstants.ROLE_SYSTEM}；调用方不应在运行时修改。
     */
    public static final String ROLE_SYSTEM = OpenClawConstants.ROLE_SYSTEM;
    /**
     * OpenClaw 协议固定值 {@code OpenClawConstants.ROLE_USER}；调用方不应在运行时修改。
     */
    public static final String ROLE_USER = OpenClawConstants.ROLE_USER;
    /**
     * OpenClaw 协议固定值 {@code OpenClawConstants.ROLE_ASSISTANT}；调用方不应在运行时修改。
     */
    public static final String ROLE_ASSISTANT = OpenClawConstants.ROLE_ASSISTANT;
    /**
     * OpenClaw 协议固定值 {@code OpenClawConstants.ROLE_TOOL}；调用方不应在运行时修改。
     */
    public static final String ROLE_TOOL = OpenClawConstants.ROLE_TOOL;

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
    private List<ToolCall> toolCalls;

    /**
     * 映射 OpenClaw JSON 字段 `toolCallId` 的 关联标识。
     */
    @JsonProperty("tool_call_id")
    private String toolCallId;

    // ==================== Factory Methods ====================

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatMessage`。
     *
     * @param content 写入 `content` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
     */
    public static ChatMessage ofSystem(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_SYSTEM);
        msg.setContent(content);
        return msg;
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatMessage`。
     *
     * @param content 写入 `content` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
     */
    public static ChatMessage ofUser(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_USER);
        msg.setContent(content);
        return msg;
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatMessage`。
     *
     * @param content 写入 `content` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
     */
    public static ChatMessage ofAssistant(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        return msg;
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatMessage`。
     *
     * @param content 写入 `content` 协议字段的内容
     * @param toolCalls 写入 `toolCalls` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
     */
    public static ChatMessage ofAssistant(String content, List<ToolCall> toolCalls) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        msg.setToolCalls(toolCalls);
        return msg;
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatMessage`。
     *
     * @param toolCallId 用于关联协议对象的 `toolCallId` 标识
     * @param output 写入 `output` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
     */
    public static ChatMessage ofTool(String toolCallId, String output) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_TOOL);
        msg.setContent(output);
        msg.setToolCallId(toolCallId);
        return msg;
    }

    // ==================== Inner Classes ====================

    /**
     * OpenClaw JSON 协议中的 `ToolCall` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolCall {

        /**
         * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
         */
        private String id;

        /**
         * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
         */
        private String type = "function";

        /**
         * 映射 OpenClaw JSON 字段 `function` 的 协议内容。
         */
        private FunctionCall function;

        /**
         * 根据参数创建符合 OpenClaw 协议约束的 `ToolCall`。
         *
         * @param id 用于关联协议对象的 `id` 标识
         * @param name 写入 `name` 协议字段的内容
         * @param arguments 写入 `arguments` 协议字段的内容
         * @return 按当前参数创建、查询或解析得到的 ToolCall
         */
        public static ToolCall of(String id, String name, String arguments) {
            FunctionCall fc = new FunctionCall();
            fc.setName(name);
            fc.setArguments(arguments);
            ToolCall tc = new ToolCall();
            tc.setId(id);
            tc.setType(OpenClawConstants.TOOL_TYPE_FUNCTION);
            tc.setFunction(fc);
            return tc;
        }
    }

    /**
     * OpenClaw JSON 协议中的 `FunctionCall` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FunctionCall {

        /**
         * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
         */
        private String name;

        /**
         * 映射 OpenClaw JSON 字段 `arguments` 的 协议内容。
         */
        private String arguments;
    }
}
