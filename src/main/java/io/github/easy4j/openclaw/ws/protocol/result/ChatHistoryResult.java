package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * OpenClaw JSON 协议中的 `ChatHistoryResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistoryResult {

    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    @JsonProperty("sessionKey")
    private String sessionKey;

    /**
     * 映射 OpenClaw JSON 字段 `sessionId` 的 关联标识。
     */
    @JsonProperty("sessionId")
    private String sessionId;

    /**
     * 映射 OpenClaw JSON 字段 `messages` 的 有序数组。
     */
    @JsonProperty("messages")
    private List<Object> messages;

    /**
     * 映射 OpenClaw JSON 字段 `thinkingLevel` 的 协议内容。
     */
    @JsonProperty("thinkingLevel")
    private String thinkingLevel;

    /**
     * 映射 OpenClaw JSON 字段 `fastMode` 的 布尔开关。
     */
    @JsonProperty("fastMode")
    private Boolean fastMode;

    /**
     * 映射 OpenClaw JSON 字段 `verboseLevel` 的 协议内容。
     */
    @JsonProperty("verboseLevel")
    private String verboseLevel;

    /**
     * 读取当前对象保存的 按对话顺序排列的消息，不触发网络或子进程调用。
     *
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public List<Object> getMessages() {
        return messages != null ? messages : Collections.emptyList();
    }
}
