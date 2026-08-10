package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * agent.identity.get RPC 返回的智能体展示身份。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentIdentityGetResult {

    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    @JsonProperty("agentId")
    private String agentId;

    /**
     * Gateway 返回的智能体显示名称。
     */
    @JsonProperty("name")
    private String name;

    /**
     * JSON 属性 {@code avatar}，表示头像地址或数据。
     */
    @JsonProperty("avatar")
    private String avatar;

    /**
     * JSON 属性 {@code avatarSource}，表示头像来源。
     */
    @JsonProperty("avatarSource")
    private String avatarSource;

    /**
     * JSON 属性 {@code avatarStatus}，表示头像解析状态。
     */
    @JsonProperty("avatarStatus")
    private String avatarStatus;

    /**
     * JSON 属性 {@code avatarReason}，表示头像不可用原因。
     */
    @JsonProperty("avatarReason")
    private String avatarReason;

    /**
     * JSON 属性 {@code emoji}，表示展示图标。
     */
    @JsonProperty("emoji")
    private String emoji;
}
