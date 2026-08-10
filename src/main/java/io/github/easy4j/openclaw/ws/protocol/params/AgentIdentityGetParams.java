package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * {@code agent.identity.get} RPC .
 * <p>aligned {@code AgentIdentityParamsSchema}({@code src/gateway/protocol/schema/agent.ts}).</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentIdentityGetParams {

    private final String agentId;
    private final String sessionKey;

    public static AgentIdentityGetParams forAgent(String agentId) {
        return AgentIdentityGetParams.builder().agentId(agentId).build();
    }

    public static AgentIdentityGetParams forSession(String sessionKey) {
        return AgentIdentityGetParams.builder().sessionKey(sessionKey).build();
    }

    public static AgentIdentityGetParams empty() {
        return AgentIdentityGetParams.builder().build();
    }
}
