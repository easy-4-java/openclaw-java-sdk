package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.OpenClawClient;
import io.github.easy4j.openclaw.api.OpenClawSessionKeys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Agent Hook 请求，包含消息、智能体、会话、投递和幂等信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HookRequest {

    /**
     * 发送给 Hook 目标智能体的消息正文。
     */
    private String message;

    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    private String agentId;

    /**
     * 目标 Hook 的注册名称。
     */
    private String name = "Generation";

    /**
     * JSON 属性 {@code wakeMode}，表示唤醒执行模式。
     */
    private String wakeMode = "now";

    /**
     * 该请求或进程允许等待的最长时间，单位为秒；超时后主动取消对应任务。
     */
    private int timeoutSeconds = 300;

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private String sessionKey;

    /**
     * JSON 属性 {@code deliver}，表示是否向外部通道投递消息。
     */
    private Boolean deliver;

    /**
     * JSON 属性 {@code channel}，表示消息通道。
     */
    private String channel;

    /**
     * JSON 属性 {@code to}，表示消息投递目标。
     */
    private String to;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code thinking}，表示思考强度选项。
     */
    private String thinking;

    /**
     * JSON 属性 {@code idempotencyKey}，表示请求幂等键。
     */
    private String idempotencyKey;

    /**
     * 构造向 Agent Hook 发送的智能体标识和消息正文。
     *
     * @param agentId Agent 标识
     * @param message 消息正文
     */
    public HookRequest(String agentId, String message) {
        this.agentId = agentId;
        this.message = message;
    }
}
