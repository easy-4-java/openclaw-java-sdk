package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@code cron.list} RPC response body.
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronListResult {

    @JsonProperty("jobs")
    private List<CronJobSummary> jobs;

    @JsonProperty("items")
    private List<CronJobSummary> items;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("deliveryPreviews")
    private Map<String, Object> deliveryPreviews;

    /**
 * Gateway version {@code jobs} {@code items} field.
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
