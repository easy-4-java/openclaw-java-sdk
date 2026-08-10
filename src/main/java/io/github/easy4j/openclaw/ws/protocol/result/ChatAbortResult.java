package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * chat.abort RPC 的中止状态及受影响运行标识。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatAbortResult {

    /**
     * JSON 属性 {@code ok}，表示请求是否成功。
     */
    @JsonProperty("ok")
    private boolean ok;

    /**
     * JSON 属性 {@code aborted}，表示是否成功中止目标运行。
     */
    @JsonProperty("aborted")
    private boolean aborted;

    /**
     * JSON 属性 {@code runIds}，表示被中止的运行标识。
     */
    @JsonProperty("runIds")
    private List<String> runIds;

    /**
     * JSON 属性 {@code abortedRunId}，表示实际被中止的运行标识。
     */
    @JsonProperty("abortedRunId")
    private String abortedRunId;

    /**
     * Gateway 对中止请求的处理状态。
     */
    @JsonProperty("status")
    private String status;

    /**
     * 返回被中止的运行标识列表。
     *
     * @return 服务端确认中止的运行标识；响应未包含标识时返回空列表
     */
    public List<String> getRunIds() {
        return runIds != null ? runIds : Collections.emptyList();
    }
}
