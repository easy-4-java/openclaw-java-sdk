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
 * OpenAI tool call.
 * <p>
 * Provides,.
 * </p>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * //
 * Map<String, Object> getWeatherTool = Tools.function("get_weather", "Get weather info")
 *     .param("city", "string", "City name")
 *     .param("country", "string", "Country code", true)
 *     .build();
 *
 * // tool call
 * ToolCall call = response.getChoices().get(0).getMessage().getToolCalls().get(0);
 * Map<String, Object> args = Tools.parseArgs(call, Map.class);
 * String city = (String) args.get("city")
 *
 * // message
 * String result = executeTool(call, args);
 * ChatMessage resultMsg = Tools.toolResult(call.getId(), result);
 * }</pre>
 *
 * @see ChatMessage.ToolCall
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#chat-tool-contract">Chat tool contract</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class Tools {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Tools() {}

    /**
 * .
     *
 * @param name
 * @param description
     */
    public static FunctionBuilder function(String name, String description) {
        return new FunctionBuilder(name, description);
    }

    /**
 * messagetool call.
     */
    public static boolean hasToolCalls(ChatMessage message) {
        return message != null
            && message.getToolCalls() != null
            && !message.getToolCalls().isEmpty();
    }

    /**
 * chunk tool callcompletion.
     */
    public static boolean isToolCallFinish(String finishReason) {
        return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
    }

    /**
 * tool call.
     *
 * @param toolCall tool call
 * @param clazz ( Map.class )
 * @return object
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
 * tool call Map.
     */
    public static Map<String, Object> parseArgsAsMap(ToolCall toolCall) {
        return parseArgs(toolCall, Map.class);
    }

    /**
 * message.
     *
 * @param toolCallId Corresponds totool call ID
 * @param output (object, JSON)
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
 * messagetool call.
     */
    public static List<ToolCall> extractToolCalls(ChatMessage message) {
        if (!hasToolCalls(message)) {
            return Collections.emptyList();
        }
        return message.getToolCalls();
    }

    /**
 * builder.
     */
    public static class FunctionBuilder {
        private final String name;
        private final String description;
        private final Map<String, Parameter> parameters = new java.util.LinkedHashMap<>();
        private boolean required = false;

        FunctionBuilder(String name, String description) {
            this.name = Objects.requireNonNull(name, "name");
            this.description = Objects.requireNonNull(description, "description");
        }

        /**
 * Optional.
         */
        public FunctionBuilder param(String name, String type, String description) {
            return param(name, type, description, false);
        }

        /**
 * .
         *
 * @param name
 * @param type :string, number, integer, boolean, array, object
 * @param description
 * @param required Required
         */
        public FunctionBuilder param(String name, String type, String description, boolean required) {
            parameters.put(name, new Parameter(name, type, description, required));
            if (required) {
                this.required = true;
            }
            return this;
        }

        /**
 * Map(Used for HTTP ).
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

        private static class Parameter {
            final String name;
            final String type;
            final String description;
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
