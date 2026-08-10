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
 * OpenAI Chat Completions API non-streaming.
 * <p>
 * Corresponds to {@code POST /v1/chat/completions}({@code stream: false}) JSON.
 * agent ,{@code choices[0].finish_reason} {@code "tool_calls"},
 * {@code choices[0].message.toolCalls} tool call.
 * </p>
 *
 * <h3></h3>
 * <p>tool call,Corresponds to,:</p>
 * <ul>
 * <li> assistant tool callmessage</li>
 * <li> {@code role: "tool"} message, {@code toolCallId}</li>
 * </ul>
 * <p> Gateway agent .</p>
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

 /** . */
    private String id;

 /** object, {@code "chat.completion"}. */
    private String object = OpenClawConstants.OBJECT_CHAT_COMPLETION;

 /** (Unix epoch seconds). */
    private Long created;

 /** agent . */
    private String model;

 /** . */
    private List<Choice> choices;

 /** Token . */
    private Usage usage;

    /**
 * .
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {

 /** array. */
        private Integer index;

        /**
 * agent message.
 * <p> {@code finishReason} {@code "tool_calls"} ,
 * message {@code toolCalls} tool call,
 * {@code content} characters( agent ).</p>
         */
        private ChatMessage message;

        /**
 * completion.
         * <ul>
 * <li>{@code "stop"} - completion</li>
 * <li>{@code "tool_calls"} - agent </li>
 * <li>{@code "length"} - token </li>
         * </ul>
         */
        @JsonProperty("finish_reason")
        private String finishReason;

 /** completion */
        public boolean isStop() {
            return OpenClawConstants.FINISH_REASON_STOP.equals(finishReason);
        }

 /** tool call */
        public boolean isToolCalls() {
            return OpenClawConstants.FINISH_REASON_TOOL_CALLS.equals(finishReason);
        }

 /** */
        public boolean isLength() {
            return OpenClawConstants.FINISH_REASON_LENGTH.equals(finishReason);
        }
    }

    /**
 * Token .
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
 /** token ( prompt token). */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
 /** token ( completion token). */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
 /** token . */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
