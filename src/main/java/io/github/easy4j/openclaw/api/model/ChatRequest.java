package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Chat Completions 请求，包含消息、模型、采样、工具和流式选项。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    /**
     * JSON 属性 {@code agent}，表示智能体标识。
     */
    private String agent;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code messages}，表示按对话顺序排列的消息。
     */
    private List<ChatMessage> messages;

    /**
     * JSON 属性 {@code stream}，表示是否启用流式响应。
     */
    private Boolean stream;

    /**
     * JSON 属性 {@code streamOptions}，表示流式响应附加选项。
     */
    @JsonProperty("stream_options")
    private Map<String, Object> streamOptions;

    /**
     * JSON 属性 {@code tools}，表示可供模型调用的工具定义。
     */
    private List<Map<String, Object>> tools;

    /**
     * JSON 属性 {@code toolChoice}，表示工具选择策略。
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * JSON 属性 {@code user}，表示终端用户标识。
     */
    private String user;

    /**
     * JSON 属性 {@code maxCompletionTokens}，表示最大补全 Token 数。
     */
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
     * JSON 属性 {@code maxTokens}，表示最大生成 Token 数。
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * JSON 属性 {@code temperature}，表示采样温度。
     */
    private Double temperature;

    /**
     * JSON 属性 {@code topP}，表示核采样概率阈值。
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * JSON 属性 {@code frequencyPenalty}，表示词频惩罚系数。
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * JSON 属性 {@code presencePenalty}，表示重复主题惩罚系数。
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * JSON 属性 {@code seed}，表示采样随机种子。
     */
    private Integer seed;

    /**
     * JSON 属性 {@code responseFormat}，表示模型响应格式约束。
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
     * JSON 属性 {@code stop}，表示停止序列。
     */
    private Object stop;
}
