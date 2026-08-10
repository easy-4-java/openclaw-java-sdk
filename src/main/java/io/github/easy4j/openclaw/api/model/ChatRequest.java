package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `ChatRequest` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    /**
     * 映射 OpenClaw JSON 字段 `agent` 的 协议内容。
     */
    private String agent;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `messages` 的 有序数组。
     */
    private List<ChatMessage> messages;

    /**
     * 映射 OpenClaw JSON 字段 `stream` 的 布尔开关。
     */
    private Boolean stream;

    /**
     * 映射 OpenClaw JSON 字段 `streamOptions` 的 键值对象。
     */
    @JsonProperty("stream_options")
    private Map<String, Object> streamOptions;

    /**
     * 映射 OpenClaw JSON 字段 `tools` 的 有序数组。
     */
    private List<Map<String, Object>> tools;

    /**
     * 映射 OpenClaw JSON 字段 `toolChoice` 的 协议内容。
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * 映射 OpenClaw JSON 字段 `user` 的 协议内容。
     */
    private String user;

    /**
     * 映射 OpenClaw JSON 字段 `maxCompletionTokens` 的 协议内容。
     */
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
     * 映射 OpenClaw JSON 字段 `maxTokens` 的 协议内容。
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 映射 OpenClaw JSON 字段 `temperature` 的 协议内容。
     */
    private Double temperature;

    /**
     * 映射 OpenClaw JSON 字段 `topP` 的 协议内容。
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 映射 OpenClaw JSON 字段 `frequencyPenalty` 的 协议内容。
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 映射 OpenClaw JSON 字段 `presencePenalty` 的 协议内容。
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * 映射 OpenClaw JSON 字段 `seed` 的 协议内容。
     */
    private Integer seed;

    /**
     * 映射 OpenClaw JSON 字段 `responseFormat` 的 协议内容。
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
     * 映射 OpenClaw JSON 字段 `stop` 的 协议内容。
     */
    private Object stop;
}
