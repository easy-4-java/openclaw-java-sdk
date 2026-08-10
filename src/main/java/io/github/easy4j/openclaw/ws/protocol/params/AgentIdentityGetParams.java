package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `AgentIdentityGetParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentIdentityGetParams {

    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    private final String agentId;
    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    private final String sessionKey;

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `AgentIdentityGetParams`。
     *
     * @param agentId Agent 标识
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams forAgent(String agentId) {
        return AgentIdentityGetParams.builder().agentId(agentId).build();
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `AgentIdentityGetParams`。
     *
     * @param sessionKey 会话路由键
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams forSession(String sessionKey) {
        return AgentIdentityGetParams.builder().sessionKey(sessionKey).build();
    }

    /**
     * 根据参数构造或读取 `AgentIdentityGetParams` 的 `empty` 协议字段。
     *
     * @return 按方法参数填充的 AgentIdentityGetParams
     */
    public static AgentIdentityGetParams empty() {
        return AgentIdentityGetParams.builder().build();
    }
}
