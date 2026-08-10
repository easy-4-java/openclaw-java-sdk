package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `CronJobSummary` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronJobSummary {

    /**
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    @JsonProperty("id")
    private String id;

    /**
     * 映射 OpenClaw JSON 字段 `name` 的 协议内容。
     */
    @JsonProperty("name")
    private String name;

    /**
     * 映射 OpenClaw JSON 字段 `enabled` 的 布尔开关。
     */
    @JsonProperty("enabled")
    private Boolean enabled;

    /**
     * 映射 OpenClaw JSON 字段 `agentId` 的 关联标识。
     */
    @JsonProperty("agentId")
    private String agentId;

    /**
     * 映射 OpenClaw JSON 字段 `updatedAtMs` 的 协议内容。
     */
    @JsonProperty("updatedAtMs")
    private Long updatedAtMs;

    /**
     * 映射 OpenClaw JSON 字段 `nextRunAtMs` 的 协议内容。
     */
    @JsonProperty("nextRunAtMs")
    private Long nextRunAtMs;
}
