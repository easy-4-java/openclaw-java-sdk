package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * OpenClaw JSON 协议中的 `SessionsListResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsListResult {

    /**
     * 映射 OpenClaw JSON 字段 `ts` 的 协议内容。
     */
    @JsonProperty("ts")
    private long ts;

    /**
     * 映射 OpenClaw JSON 字段 `path` 的 协议内容。
     */
    @JsonProperty("path")
    private String path;

    /**
     * 映射 OpenClaw JSON 字段 `count` 的 协议内容。
     */
    @JsonProperty("count")
    private int count;

    /**
     * 映射 OpenClaw JSON 字段 `totalCount` 的 协议内容。
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * 映射 OpenClaw JSON 字段 `limitApplied` 的 协议内容。
     */
    @JsonProperty("limitApplied")
    private Integer limitApplied;

    /**
     * 映射 OpenClaw JSON 字段 `hasMore` 的 布尔开关。
     */
    @JsonProperty("hasMore")
    private Boolean hasMore;

    /**
     * 映射 OpenClaw JSON 字段 `defaults` 的 协议内容。
     */
    @JsonProperty("defaults")
    private GatewaySessionsDefaults defaults;

    /**
     * 映射 OpenClaw JSON 字段 `sessions` 的 有序数组。
     */
    @JsonProperty("sessions")
    private List<GatewaySessionRow> sessions;

    /**
     * 读取当前对象保存的 `sessions` 对应状态，不触发网络或子进程调用。
     *
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public List<GatewaySessionRow> getSessions() {
        return sessions != null ? sessions : Collections.emptyList();
    }
}
