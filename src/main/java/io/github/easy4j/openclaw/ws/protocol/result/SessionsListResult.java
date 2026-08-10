package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * sessions.list RPC 返回的会话页、默认配置和分页信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsListResult {

    /**
     * JSON 属性 {@code ts}，表示事件时间戳。
     */
    @JsonProperty("ts")
    private long ts;

    /**
     * JSON 属性 {@code path}，表示资源路径。
     */
    @JsonProperty("path")
    private String path;

    /**
     * JSON 属性 {@code count}，表示记录数量。
     */
    @JsonProperty("count")
    private int count;

    /**
     * JSON 属性 {@code totalCount}，表示未分页前的总记录数。
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * JSON 属性 {@code limitApplied}，表示服务端实际采用的条数限制。
     */
    @JsonProperty("limitApplied")
    private Integer limitApplied;

    /**
     * JSON 属性 {@code hasMore}，表示是否还有下一页会话。
     */
    @JsonProperty("hasMore")
    private Boolean hasMore;

    /**
     * JSON 属性 {@code defaults}，表示Gateway 默认参数。
     */
    @JsonProperty("defaults")
    private GatewaySessionsDefaults defaults;

    /**
     * JSON 属性 {@code sessions}，表示Gateway 会话。
     */
    @JsonProperty("sessions")
    private List<GatewaySessionRow> sessions;

    /**
     * 返回 Gateway 会话列表。
     *
     * @return 当前页的会话行；响应未包含会话时返回空列表
     */
    public List<GatewaySessionRow> getSessions() {
        return sessions != null ? sessions : Collections.emptyList();
    }
}
