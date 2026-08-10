package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `GatewaySessionRow` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionRow {

    /**
     * 映射 OpenClaw JSON 字段 `key` 的 协议内容。
     */
    @JsonProperty("key")
    private String key;

    /**
     * 映射 OpenClaw JSON 字段 `sessionId` 的 关联标识。
     */
    @JsonProperty("sessionId")
    private String sessionId;

    /**
     * 映射 OpenClaw JSON 字段 `kind` 的 协议内容。
     */
    @JsonProperty("kind")
    private String kind;

    /**
     * 映射 OpenClaw JSON 字段 `label` 的 协议内容。
     */
    @JsonProperty("label")
    private String label;

    /**
     * 映射 OpenClaw JSON 字段 `displayName` 的 协议内容。
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * 映射 OpenClaw JSON 字段 `derivedTitle` 的 协议内容。
     */
    @JsonProperty("derivedTitle")
    private String derivedTitle;

    /**
     * 映射 OpenClaw JSON 字段 `lastMessagePreview` 的 协议内容。
     */
    @JsonProperty("lastMessagePreview")
    private String lastMessagePreview;

    /**
     * 映射 OpenClaw JSON 字段 `updatedAt` 的 协议内容。
     */
    @JsonProperty("updatedAt")
    private Long updatedAt;

    /**
     * 映射 OpenClaw JSON 字段 `modelProvider` 的 关联标识。
     */
    @JsonProperty("modelProvider")
    private String modelProvider;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    @JsonProperty("model")
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `hasActiveRun` 的 布尔开关。
     */
    @JsonProperty("hasActiveRun")
    private Boolean hasActiveRun;

    /**
     * 映射 OpenClaw JSON 字段 `status` 的 协议内容。
     */
    @JsonProperty("status")
    private String status;
}
