package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `SessionsListParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionsListParams {

    /**
     * 映射 OpenClaw JSON 字段 `limit` 的 协议内容。
     */
    private final Integer limit;
    /**
     * 映射 OpenClaw JSON 字段 `activeMinutes` 的 协议内容。
     */
    private final Integer activeMinutes;
    /**
     * 映射 OpenClaw JSON 字段 `includeGlobal` 的 布尔开关。
     */
    private final Boolean includeGlobal;
    /**
     * 映射 OpenClaw JSON 字段 `includeUnknown` 的 布尔开关。
     */
    private final Boolean includeUnknown;
    /**
     * 映射 OpenClaw JSON 字段 `configuredAgentsOnly` 的 布尔开关。
     */
    private final Boolean configuredAgentsOnly;
    /**
     * 映射 OpenClaw JSON 字段 `includeDerivedTitles` 的 布尔开关。
     */
    private final Boolean includeDerivedTitles;
    /**
     * 映射 OpenClaw JSON 字段 `includeLastMessage` 的 布尔开关。
     */
    private final Boolean includeLastMessage;
    /**
     * 映射 OpenClaw JSON 字段 `label` 的 协议内容。
     */
    private final String label;
    /**
     * 映射 OpenClaw JSON 字段 `spawnedBy` 的 协议内容。
     */
    private final String spawnedBy;
    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    private final String agentId;
    /**
     * 映射 OpenClaw JSON 字段 `search` 的 协议内容。
     */
    private final String search;

    /**
     * 根据参数构造或读取 `SessionsListParams` 的 `defaults` 协议字段。
     *
     * @return 按方法参数填充的 SessionsListParams
     */
    public static SessionsListParams defaults() {
        return SessionsListParams.builder().build();
    }
}
