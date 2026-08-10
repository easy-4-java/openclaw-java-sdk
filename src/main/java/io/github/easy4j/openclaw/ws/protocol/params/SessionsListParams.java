package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * sessions.list RPC 的会话范围、展示字段、过滤和分页参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionsListParams {

    /**
     * JSON 属性 {@code limit}，表示最大返回条数。
     */
    private final Integer limit;
    /**
     * JSON 属性 {@code activeMinutes}，表示最近活跃时间窗口，单位为分钟。
     */
    private final Integer activeMinutes;
    /**
     * JSON 属性 {@code includeGlobal}，表示是否包含全局会话。
     */
    private final Boolean includeGlobal;
    /**
     * JSON 属性 {@code includeUnknown}，表示是否包含未知来源会话。
     */
    private final Boolean includeUnknown;
    /**
     * JSON 属性 {@code configuredAgentsOnly}，表示是否只返回已配置智能体的会话。
     */
    private final Boolean configuredAgentsOnly;
    /**
     * JSON 属性 {@code includeDerivedTitles}，表示是否包含自动生成标题。
     */
    private final Boolean includeDerivedTitles;
    /**
     * JSON 属性 {@code includeLastMessage}，表示是否包含最后一条消息预览。
     */
    private final Boolean includeLastMessage;
    /**
     * JSON 属性 {@code label}，表示展示标签。
     */
    private final String label;
    /**
     * JSON 属性 {@code spawnedBy}，表示创建当前会话的父级标识。
     */
    private final String spawnedBy;
    /**
     * JSON 属性 {@code agentId}，表示智能体标识。
     */
    private final String agentId;
    /**
     * JSON 属性 {@code search}，表示搜索关键字。
     */
    private final String search;

    /**
     * 创建使用 Gateway 默认过滤条件的查询参数。
     *
     * @return 按方法参数填充的 SessionsListParams
     */
    public static SessionsListParams defaults() {
        return SessionsListParams.builder().build();
    }
}
