package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `AgentIdentityGetResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentIdentityGetResult {

    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    @JsonProperty("agentId")
    private String agentId;

    /**
     * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
     */
    @JsonProperty("name")
    private String name;

    /**
     * 映射 OpenClaw JSON 字段 `avatar` 的 协议内容。
     */
    @JsonProperty("avatar")
    private String avatar;

    /**
     * 映射 OpenClaw JSON 字段 `avatarSource` 的 协议内容。
     */
    @JsonProperty("avatarSource")
    private String avatarSource;

    /**
     * 映射 OpenClaw JSON 字段 `avatarStatus` 的 协议内容。
     */
    @JsonProperty("avatarStatus")
    private String avatarStatus;

    /**
     * 映射 OpenClaw JSON 字段 `avatarReason` 的 协议内容。
     */
    @JsonProperty("avatarReason")
    private String avatarReason;

    /**
     * 映射 OpenClaw JSON 字段 `emoji` 的 协议内容。
     */
    @JsonProperty("emoji")
    private String emoji;
}
