package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * cron.list RPC 的任务列表、总数和投递预览。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronListResult {

    /**
     * JSON 属性 {@code jobs}，表示Cron 任务摘要。
     */
    @JsonProperty("jobs")
    private List<CronJobSummary> jobs;

    /**
     * JSON 属性 {@code items}，表示Cron 任务条目。
     */
    @JsonProperty("items")
    private List<CronJobSummary> items;

    /**
     * JSON 属性 {@code total}，表示总记录数。
     */
    @JsonProperty("total")
    private Integer total;

    /**
     * JSON 属性 {@code deliveryPreviews}，表示任务投递预览映射。
     */
    @JsonProperty("deliveryPreviews")
    private Map<String, Object> deliveryPreviews;

    /**
     * 返回 Cron 任务摘要列表。
     *
     * @return 服务端返回的计划任务摘要；响应未包含任务时返回空列表
     */
    public List<CronJobSummary> getJobs() {
        if (jobs != null && !jobs.isEmpty()) {
            return jobs;
        }
        if (items != null && !items.isEmpty()) {
            return items;
        }
        return Collections.emptyList();
    }
}
