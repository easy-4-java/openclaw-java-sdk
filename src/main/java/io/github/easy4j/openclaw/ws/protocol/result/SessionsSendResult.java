package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `SessionsSendResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsSendResult {

    /**
     * 映射 OpenClaw JSON 字段 `runId` 的 关联标识。
     */
    @JsonProperty("runId")
    private String runId;

    /**
     * 映射 OpenClaw JSON 字段 `messageSeq` 的 协议内容。
     */
    @JsonProperty("messageSeq")
    private Integer messageSeq;

    /**
     * 映射 OpenClaw JSON 字段 `interruptedActiveRun` 的 布尔开关。
     */
    @JsonProperty("interruptedActiveRun")
    private Boolean interruptedActiveRun;
}
