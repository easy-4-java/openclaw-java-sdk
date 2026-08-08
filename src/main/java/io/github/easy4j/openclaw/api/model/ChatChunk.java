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
 * OpenAI Chat Completions API streaming.
 * <p>
 * {@code stream: true} ,Gateway SSE object.
 * SSE event {@code data: <json>},stream {@code data: [DONE]} .
 * </p>
 *
 * <h3>tool callstreaming</h3>
 * <p> agent ,streaming:</p>
 * <ol>
 * <li> assistant delta</li>
 * <li>Optional assistant delta</li>
 * <li> {@code delta.toolCalls} ,argument fragment</li>
 * <li>,{@code finishReason} {@code "tool_calls"}</li>
 *   <li>{@code data: [DONE]}</li>
 * </ol>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatChunk {

 /** . */
    private String id;

 /** object, {@code "chat.completion.chunk"}. */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION_CHUNK;

 /** (Unix epoch seconds). */
    private Long created;

 /** agent . */
    private String model;

 /** . */
    private List<DeltaChoice> choices;

    /**
 * streaming.
 * <p>:streamingmessage {@code delta} field {@code message} field.</p>
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeltaChoice {
        private Integer index;

        /**
 * deltamessage.
 * <p>,tool calldelta.</p>
         */
        private DeltaMessage delta;

        /**
 * completion(only null).
         * <ul>
 * <li>{@code "stop"} - completion</li>
 * <li>{@code "tool_calls"} - agent </li>
         * </ul>
         */
        @JsonProperty("finish_reason")
        private String finishReason;

 /** tool callcompletion */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }
    }

    /**
 * deltamessage.
 * <p>,tool calldeltafield.</p>
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeltaMessage {
 /** message(only). */
        private String role;
 /** delta text. */
        private String content;
 /** tool calldelta(Used forstreamingargument fragment). */
        @JsonProperty("tool_calls")
        private List<ChatMessage.ToolCall> toolCalls;
    }
}
