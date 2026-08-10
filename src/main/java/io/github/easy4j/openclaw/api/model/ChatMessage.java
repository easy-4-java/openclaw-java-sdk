package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenAI Chat Completions API messageobject.
 * <p>
 * Corresponds to OpenAI {@code /v1/chat/completions} {@code messages} array.
 * :{@code system},{@code user},{@code assistant},{@code tool}.
 * </p>
 *
 * <p> {@code finish_reason} {@code tool_calls} ,
 * {@code tool_calls} field agent .</p>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * // message
 * ChatMessage msg = ChatMessage.ofUser("Hello");
 * ChatMessage msg = ChatMessage.ofSystem("You are a helpful assistant");
 * ChatMessage msg = ChatMessage.ofAssistant("I can help with that.");
 *
 * // tool call
 * ChatMessage toolResult = ChatMessage.ofTool("call_abc123", "{\"result\": \"done\"}");
 *
 * // tool callmessage
 * ChatMessage assistantMsg = ChatMessage.ofAssistant(null,
 *     List.of(ToolCall.of("call_abc", "get_weather", "{\"city\": \"Beijing\"}")));
 * }</pre>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage {

 /** message */
    public static final String ROLE_SYSTEM = OpenClawConstants.ROLE_SYSTEM;
    public static final String ROLE_USER = OpenClawConstants.ROLE_USER;
    public static final String ROLE_ASSISTANT = OpenClawConstants.ROLE_ASSISTANT;
    public static final String ROLE_TOOL = OpenClawConstants.ROLE_TOOL;

 /** message */
    private String role;

 /** message */
    private String content;

 /** tool call */
    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;

 /** tool call ID(tool ) */
    @JsonProperty("tool_call_id")
    private String toolCallId;

    // ==================== Factory Methods ====================

    /**
 * systemmessage.
     */
    public static ChatMessage ofSystem(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_SYSTEM);
        msg.setContent(content);
        return msg;
    }

    /**
 * message.
     */
    public static ChatMessage ofUser(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_USER);
        msg.setContent(content);
        return msg;
    }

    /**
 * message.
     */
    public static ChatMessage ofAssistant(String content) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        return msg;
    }

    /**
 * message(tool call).
     *
 * @param content message
 * @param toolCalls tool call
     */
    public static ChatMessage ofAssistant(String content, List<ToolCall> toolCalls) {
        ChatMessage msg = new ChatMessage();
        msg.setRole(ROLE_ASSISTANT);
        msg.setContent(content);
        msg.setToolCalls(toolCalls);
        return msg;
    }

    /**
 * message.
     *
 * @param toolCallId Corresponds totool call ID
 * @param output (JSON characters)
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
 * tool callobject.
 * <p>tool call ID,.</p>
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolCall {

 /** tool call, {@code call_abc123} */
        private String id;

 /** , {@code "function"} */
        private String type = "function";

 /** details */
        private FunctionCall function;

        /**
 * tool call.
         *
 * @param id tool call ID
 * @param name
 * @param arguments (JSON characters)
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
 * details.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FunctionCall {

 /** */
        private String name;

        /**
 * (JSON characters).
 * <p> JSON charactersobject.</p>
         */
        private String arguments;
    }
}
