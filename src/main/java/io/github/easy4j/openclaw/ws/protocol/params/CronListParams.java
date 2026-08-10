package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * cron.list RPC 的过滤、排序和分页参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CronListParams {

    /**
     * JSON 属性 {@code includeDisabled}，表示是否包含已禁用 Cron 任务。
     */
    private final Boolean includeDisabled;
    /**
     * JSON 属性 {@code limit}，表示最大返回条数。
     */
    private final Integer limit;
    /**
     * JSON 属性 {@code offset}，表示分页起始偏移量。
     */
    private final Integer offset;
    /**
     * JSON 属性 {@code query}，表示查询条件。
     */
    private final String query;
    /**
     * JSON 属性 {@code enabled}，表示是否启用。
     */
    private final String enabled;
    /**
     * JSON 属性 {@code sortBy}，表示排序字段。
     */
    private final String sortBy;
    /**
     * JSON 属性 {@code sortDir}，表示排序方向。
     */
    private final String sortDir;
    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    private final String agentId;

    /**
     * 创建使用 Gateway 默认过滤条件的查询参数。
     *
     * @return 按方法参数填充的 CronListParams
     */
    public static CronListParams defaults() {
        return CronListParams.builder().build();
    }
}
