package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.OpenClawClient;
import io.github.easy4j.openclaw.api.OpenClawSessionKeys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenClaw JSON 协议中的 `HookRequest` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
     */
    private String message;

    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    private String agentId;

    /**
     * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
     */
    private String name = "Generation";

    /**
     * 映射 OpenClaw JSON 字段 `wakeMode` 的 协议内容。
     */
    private String wakeMode = "now";

    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private int timeoutSeconds = 300;

    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    private String sessionKey;

    /**
     * 映射 OpenClaw JSON 字段 `deliver` 的 布尔开关。
     */
    private Boolean deliver;

    /**
     * 映射 OpenClaw JSON 字段 `channel` 的 协议内容。
     */
    private String channel;

    /**
     * 映射 OpenClaw JSON 字段 `to` 的 协议内容。
     */
    private String to;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `thinking` 的 协议内容。
     */
    private String thinking;

    /**
     * 映射 OpenClaw JSON 字段 `idempotencyKey` 的 关联标识。
     */
    private String idempotencyKey;

    /**
     * 按协议字段创建 `HookRequest`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param agentId Agent 标识
     * @param message 消息正文
     */
    public HookRequest(String agentId, String message) {
        this.agentId = agentId;
        this.message = message;
    }
}
