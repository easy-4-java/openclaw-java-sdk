package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.OpenClawClient;
import io.github.easy4j.openclaw.api.OpenClawSessionKeys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenClaw Gateway {@code POST /hooks/agent} request body,
 * <a href="https://docs.openclaw.ai/gateway/configuration-reference">Gateway Hooks documentation</a>.
 * <p>
 * {@code sessionKey} onlyGateway {@code hooks.allowRequestSessionKey}
 * {@code hooks.allowedSessionKeyPrefixes} ;Gateway.
 * </p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HookRequest {

 /** Required: agent / */
    private String message;

 /** agent */
    private String agentId;

 /** agent , "Hook" */
    private String name = "Generation";

 /** wake:"now" "next-heartbeat" */
    private String wakeMode = "now";

 /** timeoutseconds, 300 */
    private int timeoutSeconds = 300;

    /**
 * sessionkey;Gateway {@code hooks.allowRequestSessionKey=true} .
     * <p>
 * {@link OpenClawSessionKeys} {@link OpenClawClient#agentWithStableSession} /
 * {@link OpenClawClient#agentOneShotForPeer} / {@link OpenClawClient#agentOneShot} ,characters.
     * </p>
     */
    private String sessionKey;

    /**
 * {@code true} channel;{@code null} JSON field(Gateway).
     */
    private Boolean deliver;

 /** channel, {@link #deliver} ;documentation {@code last} */
    private String channel;

 /** ,documentationfield {@code to} */
    private String to;

 /** , {@code openai/gpt-5.5} Gateway ref */
    private String model;

 /** ,documentationexample {@code off} */
    private String thinking;

    /**
 * key(Optional).
 * <p>Used for {@code tool_call_id} .</p>
     */
    private String idempotencyKey;

    /**
 * @param agentId agent
 * @param message agent /
     */
    public HookRequest(String agentId, String message) {
        this.agentId = agentId;
        this.message = message;
    }
}
