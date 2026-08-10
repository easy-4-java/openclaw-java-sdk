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
 * OpenClaw JSON 协议中的 `Tools` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class Tools {

    /**
     * OpenClaw 协议固定值 {@code new ObjectMapper()}；调用方不应在运行时修改。
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Tools() {}

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `Tools` 中的 `function` 数据。
     *
     * @param name 写入 `name` 协议字段的内容
     * @param description 写入 `description` 协议字段的内容
     * @return 预填充当前工厂方法字段、可继续链式补充内容的 FunctionBuilder
     */
    public static FunctionBuilder function(String name, String description) {
        return new FunctionBuilder(name, description);
    }

    /**
     * 判断 `toolCalls` 对应状态 是否满足协议或生命周期条件。
     *
     * @param message 消息正文
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean hasToolCalls(ChatMessage message) {
        return message != null
            && message.getToolCalls() != null
            && !message.getToolCalls().isEmpty();
    }

    /**
     * 判断 `toolCallFinish` 对应状态 是否满足协议或生命周期条件。
     *
     * @param finishReason 写入 `finishReason` 协议字段的内容
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isToolCallFinish(String finishReason) {
        return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
    }

    /**
     * 使用受控 ObjectMapper 把输入解析为目标类型，解析失败时保留原始异常原因。
     *
     * @param <T> 方法使用的泛型类型
     * @param toolCall 写入 `toolCall` 协议字段的内容
     * @param clazz 写入 `clazz` 协议字段的内容
     * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
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
     * @param toolCall 写入 `toolCall` 协议字段的内容
     * @return 键名与 OpenClaw JSON/CLI 协议一致的映射
     */
    public static Map<String, Object> parseArgsAsMap(ToolCall toolCall) {
        return parseArgs(toolCall, Map.class);
    }

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `Tools` 中的 `toolResult` 数据。
     *
     * @param toolCallId 用于关联协议对象的 `toolCallId` 标识
     * @param output 写入 `output` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 ChatMessage
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
     * 根据 OpenClaw JSON 语义构造、提取或更新 `Tools` 中的 `extractToolCalls` 数据。
     *
     * @param message 消息正文
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public static List<ToolCall> extractToolCalls(ChatMessage message) {
        if (!hasToolCalls(message)) {
            return Collections.emptyList();
        }
        return message.getToolCalls();
    }

    /**
     * 链式构建器，逐项收集 Tools 的字段；build() 会复制当前快照，后续修改不会影响已构造的 Tools。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class FunctionBuilder {
        /**
         * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
         */
        private final String name;
        /**
         * 映射 OpenClaw JSON 字段 `description` 的 协议内容。
         */
        private final String description;
        /**
         * 映射 OpenClaw JSON 字段 `parameters` 的 键值对象。
         */
        private final Map<String, Parameter> parameters = new java.util.LinkedHashMap<>();
        /**
         * 映射 OpenClaw JSON 字段 `required` 的 布尔开关。
         */
        private boolean required = false;

        FunctionBuilder(String name, String description) {
            this.name = Objects.requireNonNull(name, "name");
            this.description = Objects.requireNonNull(description, "description");
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `FunctionBuilder` 中的 `param` 数据。
         *
         * @param name 写入 `name` 协议字段的内容
         * @param type 写入 `type` 协议字段的内容
         * @param description 写入 `description` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 FunctionBuilder
         */
        public FunctionBuilder param(String name, String type, String description) {
            return param(name, type, description, false);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `FunctionBuilder` 中的 `param` 数据。
         *
         * @param name 写入 `name` 协议字段的内容
         * @param type 写入 `type` 协议字段的内容
         * @param description 写入 `description` 协议字段的内容
         * @param required 写入 `required` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 FunctionBuilder
         */
        public FunctionBuilder param(String name, String type, String description, boolean required) {
            parameters.put(name, new Parameter(name, type, description, required));
            if (required) {
                this.required = true;
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `Tools`。
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
         * OpenClaw JSON 协议中的 `Parameter` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        private static class Parameter {
            /**
             * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
             */
            final String name;
            /**
             * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
             */
            final String type;
            /**
             * 映射 OpenClaw JSON 字段 `description` 的 协议内容。
             */
            final String description;
            /**
             * 映射 OpenClaw JSON 字段 `required` 的 布尔开关。
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
