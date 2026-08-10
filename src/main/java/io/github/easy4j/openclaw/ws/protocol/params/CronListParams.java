package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `CronListParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CronListParams {

    /**
     * 映射 OpenClaw JSON 字段 `includeDisabled` 的 布尔开关。
     */
    private final Boolean includeDisabled;
    /**
     * 映射 OpenClaw JSON 字段 `limit` 的 协议内容。
     */
    private final Integer limit;
    /**
     * 映射 OpenClaw JSON 字段 `offset` 的 协议内容。
     */
    private final Integer offset;
    /**
     * 映射 OpenClaw JSON 字段 `query` 的 协议内容。
     */
    private final String query;
    /**
     * 映射 OpenClaw JSON 字段 `enabled` 的 协议内容。
     */
    private final String enabled;
    /**
     * 映射 OpenClaw JSON 字段 `sortBy` 的 协议内容。
     */
    private final String sortBy;
    /**
     * 映射 OpenClaw JSON 字段 `sortDir` 的 协议内容。
     */
    private final String sortDir;
    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    private final String agentId;

    /**
     * 根据参数构造或读取 `CronListParams` 的 `defaults` 协议字段。
     *
     * @return 按方法参数填充的 CronListParams
     */
    public static CronListParams defaults() {
        return CronListParams.builder().build();
    }
}
