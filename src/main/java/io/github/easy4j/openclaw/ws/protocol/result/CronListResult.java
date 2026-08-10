package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `CronListResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronListResult {

    /**
     * 映射 OpenClaw JSON 字段 `jobs` 的 有序数组。
     */
    @JsonProperty("jobs")
    private List<CronJobSummary> jobs;

    /**
     * 映射 OpenClaw JSON 字段 `items` 的 有序数组。
     */
    @JsonProperty("items")
    private List<CronJobSummary> items;

    /**
     * 映射 OpenClaw JSON 字段 `total` 的 协议内容。
     */
    @JsonProperty("total")
    private Integer total;

    /**
     * 映射 OpenClaw JSON 字段 `deliveryPreviews` 的 键值对象。
     */
    @JsonProperty("deliveryPreviews")
    private Map<String, Object> deliveryPreviews;

    /**
     * 读取当前对象保存的 `jobs` 对应状态，不触发网络或子进程调用。
     *
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
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
