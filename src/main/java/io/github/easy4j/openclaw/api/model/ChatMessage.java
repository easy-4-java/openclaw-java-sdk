package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 聊天消息模型，支持 system、user、assistant 和 tool 角色。
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
     * 聊天消息 role 字段使用的 {@code system} 角色值。
     */
    public static final String ROLE_SYSTEM = OpenClawConstants.ROLE_SYSTEM;
    /**
     * 聊天消息 role 字段使用的 {@code user} 角色值。
     */
    public static final String ROLE_USER = OpenClawConstants.ROLE_USER;
    /**
     * 聊天消息 role 字段使用的 {@code assistant} 角色值。
     */
    public static final String ROLE_ASSISTANT = OpenClawConstants.ROLE_ASSISTANT;
    /**
     * 聊天消息 role 字段使用的 {@code tool} 角色值。
     */
    public static final String ROLE_TOOL = OpenClawConstants.ROLE_TOOL;

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
    private List<ToolCall> toolCalls;

    /**
     * JSON 属性 {@code toolCallId}，表示被回复的工具调用标识。
     */
    @JsonProperty("tool_call_id")
    private String toolCallId;

    // ==================== Factory Methods ====================

    /**
     * 创建 {@code system} 角色消息。
     *
     * @param content 消息正文或流式增量内容
     * @return role 为 system 的聊天消息
     */
    public static ChatMessage ofSystem(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_SYSTEM);
        msg.setContent(content);
        return msg;
    }

    /**
     * 创建 {@code user} 角色消息。
     *
     * @param content 消息正文或流式增量内容
     * @return role 为 user 的聊天消息
     */
    public static ChatMessage ofUser(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_USER);
        msg.setContent(content);
        return msg;
    }

    /**
     * 创建不包含工具调用的 {@code assistant} 角色消息。
     *
     * @param content 消息正文或流式增量内容
     * @return role 为 assistant 的聊天消息
     */
    public static ChatMessage ofAssistant(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        return msg;
    }

    /**
     * 创建携带工具调用列表的 {@code assistant} 角色消息。
     *
     * @param content 消息正文或流式增量内容
     * @param toolCalls 本次响应累计得到的工具调用列表
     * @return role 为 assistant 的聊天消息
     */
    public static ChatMessage ofAssistant(String content, List<ToolCall> toolCalls) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        msg.setToolCalls(toolCalls);
        return msg;
    }

    /**
     * 创建与指定工具调用关联的 {@code tool} 角色结果消息。
     *
     * @param toolCallId 用于关联协议对象的 {@code toolCallId} 标识
     * @param output 工具执行返回的文本内容
     * @return role 为 tool 的聊天消息
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
     * assistant 消息中的工具调用标识、类型和函数调用。
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
         * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
         */
        private String id;

        /**
         * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
         */
        private String type = "function";

        /**
         * JSON 属性 {@code function}，表示函数工具定义。
         */
        private FunctionCall function;

        /**
         * 创建函数类型的工具调用，并保留原始参数 JSON。
         *
         * @param id 用于关联协议对象的 {@code id} 标识
         * @param name 被调用的工具函数名称
         * @param arguments 按原始顺序传递给 CLI 的参数列表
         * @return 关联指定调用标识、函数名称和参数 JSON 的工具调用对象
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
     * 函数工具调用名称和参数 JSON。
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
         * 模型请求调用的函数名称。
         */
        private String name;

        /**
         * JSON 属性 {@code arguments}，表示函数参数 JSON。
         */
        private String arguments;
    }
}
