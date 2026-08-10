package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * OpenAI Chat Completions API request body.
 * <p>
 * Corresponds to {@code POST /v1/chat/completions} JSON.
 * </p>
 *
 * <h3>field</h3>
 * <ul>
 * <li>{@code agent} - Agent ( {@code "openclaw/default"})
 * <li>{@code model} - <b> LLM </b>( {@code "gpt-4o"})
 * </ul>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * // 1: Builder
 * ChatRequest request = ChatRequest.builder()
 *     .agent("openclaw/default")
 *     .model("gpt-4o")
 *     .messages(List.of(ChatMessage.ofUser("Hello")))
 *     .build();
 *
 * // 2: setter
 * ChatRequest request = new ChatRequest();
 * request.setAgent("openclaw/default");
 * request.setModel("gpt-4o");
 * request.setMessages(List.of(ChatMessage.ofUser("Hello")));
 * }</pre>
 *
 * <h3>session</h3>
 * <p>(session key). {@code user} characters,
 * Gateway session key, agent session.</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    /**
 * Agent .
 * <p> {@code "openclaw"},{@code "openclaw/default"} {@code "openclaw/<agentId>"}.
 * {@code "openclaw:<agentId>"} {@code "agent:<agentId>"} .</p>
     */
    private String agent;

    /**
 * LLM .
 * <p>Corresponds to OpenAI model field, {@code "gpt-4o"},{@code "claude-3-opus"} .
 * , Agent .</p>
     */
    private String model;

    /**
 * messagearray. OpenAI , {@code system},{@code user},{@code assistant},{@code tool} .
     */
    private List<ChatMessage> messages;

    /**
 * Whether to enable SSE streaming.
 * <p> {@code true} , Content-Type {@code text/event-stream},
 * {@code data: <json>}, {@code data: [DONE]} .</p>
     */
    private Boolean stream;

    /**
 * streaming. {@code stream} {@code true} .
 * <p> {@code include_usage} {@code true}, {@code [DONE]} usage .</p>
     */
    @JsonProperty("stream_options")
    private Map<String, Object> streamOptions;

    /**
 * array.
 * <p>:{@code { "type": "function", "function": { "name": "...", "description": "...", "parameters": {...} } }}</p>
     */
    private List<Map<String, Object>> tools;

    /**
 * tool choice.
 * <p>:{@code "auto"},{@code "none"},{@code "required"},
 * {@code { "type": "function", "function": { "name": "..." } }}.</p>
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
 * (Used for session key).
 * <p> {@code conv:<conversationId>} ,
 * thread user value agent session.</p>
     */
    private String user;

    /**
 * completion token ( token).
 * <p> {@code maxTokens}. agent stream-param channel.</p>
     */
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
 * token (field, {@code maxCompletionTokens} ).
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
 * (0-2). provider.
     */
    private Double temperature;

    /**
 * nucleus (0-1). provider.
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
 * (-2.0 2.0). {@code 400 invalid_request_error}.
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
 * (-2.0 2.0). {@code 400 invalid_request_error}.
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
 * . provider.
     */
    private Integer seed;

    /**
 * .
 * <p> {@link ResponseFormat} ,:
 * {@link ResponseFormat#jsonObject} JSON ,
 * {@link ResponseFormatType#JSON_SCHEMA} ,
 * {@link ResponseFormatType#TEXT} .</p>
     *
     * @see ResponseFormat
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
 * (characters 4 charactersarray).
 * <p> 4 characters/ {@code 400 invalid_request_error}.</p>
     */
    private Object stop;
}
