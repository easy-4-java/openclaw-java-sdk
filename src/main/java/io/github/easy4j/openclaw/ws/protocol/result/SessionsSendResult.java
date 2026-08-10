package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * sessions.send RPC 结果，记录运行标识和是否中断已有运行。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsSendResult {

    /**
     * JSON 属性 {@code runId}，表示一次智能体运行的标识。
     */
    @JsonProperty("runId")
    private String runId;

    /**
     * JSON 属性 {@code messageSeq}，表示消息序列号。
     */
    @JsonProperty("messageSeq")
    private Integer messageSeq;

    /**
     * JSON 属性 {@code interruptedActiveRun}，表示发送消息时是否中断了已有运行。
     */
    @JsonProperty("interruptedActiveRun")
    private Boolean interruptedActiveRun;
}
