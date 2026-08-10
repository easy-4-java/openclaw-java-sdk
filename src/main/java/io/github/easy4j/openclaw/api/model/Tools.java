package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import io.github.easy4j.openclaw.api.model.ChatMessage.ToolCall;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 函数工具定义、工具参数解析和工具结果消息的辅助入口。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class Tools {

    /**
     * 解析工具调用参数和编码工具结果的共享 ObjectMapper。
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Tools() {}

    /**
     * 创建函数工具定义构建器，并预填充函数名和说明。
     *
     * @param name 模型调用工具时使用的函数名称
     * @param description 向模型说明函数用途的文本
     * @return 已设置函数名称和说明的工具构建器
     */
    public static FunctionBuilder function(String name, String description) {
        return new FunctionBuilder(name, description);
    }

    /**
     * 判断消息是否包含非空工具调用列表。
     *
     * @param message 消息正文
     * @return 消息包含至少一个工具调用时返回 {@code true}
     */
    public static boolean hasToolCalls(ChatMessage message) {
        return message != null
            && message.getToolCalls() != null
            && !message.getToolCalls().isEmpty();
    }

    /**
     * 判断结束原因是否表示服务端正在请求工具调用。
     *
     * @param finishReason 流式响应结束原因
     * @return 结束原因等于 {@code tool_calls} 时返回 {@code true}
     */
    public static boolean isToolCallFinish(String finishReason) {
        return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param <T> 方法使用的泛型类型
     * @param toolCall 流式响应中解析出的工具调用
     * @param clazz JSON 反序列化目标类
     * @return 由工具参数 JSON 反序列化得到的 {@code clazz} 类型实例
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     */
    public static <T> T parseArgs(ToolCall toolCall, Class<T> clazz) {
        Objects.requireNonNull(toolCall, "toolCall");
        Objects.requireNonNull(toolCall.getFunction(), "toolCall.function");
        String args = toolCall.getFunction().getArguments();
        if (args == null || args.isEmpty()) {
            if (clazz == Map.class) {
                @SuppressWarnings("unchecked")
                T emptyMap = (T) new java.util.LinkedHashMap<String, Object>();
                return emptyMap;
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot instantiate " + clazz.getName(), e);
            }
        }
        try {
            return MAPPER.readValue(args, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse tool arguments: " + args, e);
        }
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param toolCall 流式响应中解析出的工具调用
     * @return 键名与 OpenClaw JSON/CLI 协议一致的映射
     */
    public static Map<String, Object> parseArgsAsMap(ToolCall toolCall) {
        return parseArgs(toolCall, Map.class);
    }

    /**
     * 把工具执行输出编码为与指定调用标识关联的 tool 消息。
     *
     * @param toolCallId 用于关联协议对象的 {@code toolCallId} 标识
     * @param output 待编码为工具结果消息的执行输出
     * @return role 为 tool 且关联指定工具调用的聊天消息
     */
    public static ChatMessage toolResult(String toolCallId, Object output) {
        String content;
        if (output instanceof String) {
            content = (String) output;
        } else {
            try {
                content = MAPPER.writeValueAsString(output);
            } catch (JsonProcessingException e) {
                content = String.valueOf(output);
            }
        }
        return ChatMessage.ofTool(toolCallId, content);
    }

    /**
     * 从聊天消息中提取工具调用；消息为空或没有工具调用时返回空列表。
     *
     * @param message 消息正文
     * @return 消息中的工具调用列表；消息为空或无工具调用时返回空列表
     */
    public static List<ToolCall> extractToolCalls(ChatMessage message) {
        if (!hasToolCalls(message)) {
            return Collections.emptyList();
        }
        return message.getToolCalls();
    }

    /**
     * {@code Tools} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class FunctionBuilder {
        /**
         * 模型调用工具时使用的函数名称。
         */
        private final String name;
        /**
         * 向模型说明函数用途的文本。
         */
        private final String description;
        /**
         * JSON 属性 {@code parameters}，表示函数工具参数的 JSON Schema。
         */
        private final Map<String, Parameter> parameters = new java.util.LinkedHashMap<>();
        /**
         * JSON 属性 {@code required}，表示工具参数是否必填。
         */
        private boolean required = false;

        FunctionBuilder(String name, String description) {
            this.name = Objects.requireNonNull(name, "name");
            this.description = Objects.requireNonNull(description, "description");
        }

        /**
         * 向函数工具的 JSON Schema 追加参数定义，并按需加入 required 列表。
         *
         * @param name 函数参数名称
         * @param type 工具参数的 JSON Schema 类型
         * @param description 向模型说明该参数用途的文本
         * @return 已追加参数定义的当前构建器
         */
        public FunctionBuilder param(String name, String type, String description) {
            return param(name, type, description, false);
        }

        /**
         * 向函数工具的 JSON Schema 追加参数定义，并按需加入 required 列表。
         *
         * @param name 函数参数名称
         * @param type 工具参数的 JSON Schema 类型
         * @param description 向模型说明该参数用途的文本
         * @param required 是否把该参数加入 JSON Schema 的 {@code required} 列表
         * @return 已追加参数定义的当前构建器
         */
        public FunctionBuilder param(String name, String type, String description, boolean required) {
            parameters.put(name, new Parameter(name, type, description, required));
            if (required) {
                this.required = true;
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code Tools}。
         *
         * @return 按当前字段创建的 Tools
         */
        @SuppressWarnings("unchecked")
        public Map<String, Object> build() {
            Map<String, Object> properties = new java.util.LinkedHashMap<>();
            List<String> requiredList = new java.util.ArrayList<>();
            for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
                Map<String, Object> param = new java.util.LinkedHashMap<>();
                param.put("type", entry.getValue().type);
                param.put("description", entry.getValue().description);
                properties.put(entry.getKey(), param);
                if (entry.getValue().required) {
                    requiredList.add(entry.getKey());
                }
            }
            Map<String, Object> function = new java.util.LinkedHashMap<>();
            function.put("name", name);
            function.put("description", description);
            Map<String, Object> params = new java.util.LinkedHashMap<>();
            params.put("type", "object");
            params.put("properties", properties);
            if (!requiredList.isEmpty()) {
                params.put("required", requiredList);
            }
            function.put("parameters", params);
            Map<String, Object> tool = new java.util.LinkedHashMap<>();
            tool.put("type", OpenClawConstants.TOOL_TYPE_FUNCTION);
            tool.put("function", function);
            return tool;
        }

        /**
         * 函数工具 JSON Schema 中的单个参数定义。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        private static class Parameter {
            /**
             * 函数参数名称。
             */
            final String name;
            /**
             * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
             */
            final String type;
            /**
             * 向模型说明该参数用途的文本。
             */
            final String description;
            /**
             * JSON 属性 {@code required}，表示工具参数是否必填。
             */
            final boolean required;

            Parameter(String name, String type, String description, boolean required) {
                this.name = name;
                this.type = type;
                this.description = description;
                this.required = required;
            }
        }
    }
}
