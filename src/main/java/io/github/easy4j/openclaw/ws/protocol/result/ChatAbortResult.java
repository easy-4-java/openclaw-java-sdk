package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * OpenClaw JSON 协议中的 `ChatAbortResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatAbortResult {

    /**
     * 映射 OpenClaw JSON 字段 `ok` 的 布尔开关。
     */
    @JsonProperty("ok")
    private boolean ok;

    /**
     * 映射 OpenClaw JSON 字段 `aborted` 的 布尔开关。
     */
    @JsonProperty("aborted")
    private boolean aborted;

    /**
     * 映射 OpenClaw JSON 字段 `runIds` 的 有序数组。
     */
    @JsonProperty("runIds")
    private List<String> runIds;

    /**
     * 映射 OpenClaw JSON 字段 `abortedRunId` 的 关联标识。
     */
    @JsonProperty("abortedRunId")
    private String abortedRunId;

    /**
     * 映射 OpenClaw JSON 字段 `status` 的 协议内容。
     */
    @JsonProperty("status")
    private String status;

    /**
     * 读取当前对象保存的 `runIds` 对应状态，不触发网络或子进程调用。
     *
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public List<String> getRunIds() {
        return runIds != null ? runIds : Collections.emptyList();
    }
}
