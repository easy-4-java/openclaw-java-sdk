package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * sessions.list 返回的单条会话摘要及模型、用量和活动状态。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionRow {

    /**
     * JSON 属性 {@code key}，表示属性或会话键。
     */
    @JsonProperty("key")
    private String key;

    /**
     * JSON 属性 {@code sessionId}，表示Gateway 会话标识。
     */
    @JsonProperty("sessionId")
    private String sessionId;

    /**
     * JSON 属性 {@code kind}，表示资源类别。
     */
    @JsonProperty("kind")
    private String kind;

    /**
     * JSON 属性 {@code label}，表示展示标签。
     */
    @JsonProperty("label")
    private String label;

    /**
     * JSON 属性 {@code displayName}，表示展示名称。
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * JSON 属性 {@code derivedTitle}，表示自动生成的会话标题。
     */
    @JsonProperty("derivedTitle")
    private String derivedTitle;

    /**
     * JSON 属性 {@code lastMessagePreview}，表示最后一条消息预览。
     */
    @JsonProperty("lastMessagePreview")
    private String lastMessagePreview;

    /**
     * JSON 属性 {@code updatedAt}，表示最后更新时间。
     */
    @JsonProperty("updatedAt")
    private Long updatedAt;

    /**
     * JSON 属性 {@code modelProvider}，表示模型提供方标识。
     */
    @JsonProperty("modelProvider")
    private String modelProvider;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    @JsonProperty("model")
    private String model;

    /**
     * JSON 属性 {@code hasActiveRun}，表示会话是否存在活动运行。
     */
    @JsonProperty("hasActiveRun")
    private Boolean hasActiveRun;

    /**
     * Gateway 报告的会话当前状态。
     */
    @JsonProperty("status")
    private String status;
}
