package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * agent.identity.get RPC 参数，可按智能体或会话定位身份。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentIdentityGetParams {

    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    private final String agentId;
    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private final String sessionKey;

    /**
     * 创建按智能体标识查询身份的参数。
     *
     * @param agentId Agent 标识
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams forAgent(String agentId) {
        return AgentIdentityGetParams.builder().agentId(agentId).build();
    }

    /**
     * 创建按会话键查询关联智能体身份的参数。
     *
     * @param sessionKey 会话路由键
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams forSession(String sessionKey) {
        return AgentIdentityGetParams.builder().sessionKey(sessionKey).build();
    }

    /**
     * 创建不限定智能体标识的身份查询参数。
     *
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams empty() {
        return AgentIdentityGetParams.builder().build();
    }
}
