package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Cron 任务的标识、名称、调度和启用状态摘要。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronJobSummary {

    /**
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    @JsonProperty("id")
    private String id;

    /**
     * 计划任务的显示名称。
     */
    @JsonProperty("name")
    private String name;

    /**
     * JSON 属性 {@code enabled}，表示是否启用。
     */
    @JsonProperty("enabled")
    private Boolean enabled;

    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    @JsonProperty("agentId")
    private String agentId;

    /**
     * JSON 属性 {@code updatedAtMs}，表示最后更新时间戳，单位为毫秒。
     */
    @JsonProperty("updatedAtMs")
    private Long updatedAtMs;

    /**
     * JSON 属性 {@code nextRunAtMs}，表示下次运行时间戳，单位为毫秒。
     */
    @JsonProperty("nextRunAtMs")
    private Long nextRunAtMs;
}
