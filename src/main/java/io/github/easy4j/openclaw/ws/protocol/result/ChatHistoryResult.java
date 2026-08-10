package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * chat.history RPC 返回的会话消息、思考等级和模式信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistoryResult {

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    @JsonProperty("sessionKey")
    private String sessionKey;

    /**
     * JSON 属性 {@code sessionId}，表示Gateway 会话标识。
     */
    @JsonProperty("sessionId")
    private String sessionId;

    /**
     * JSON 属性 {@code messages}，表示按对话顺序排列的消息。
     */
    @JsonProperty("messages")
    private List<Object> messages;

    /**
     * JSON 属性 {@code thinkingLevel}，表示思考强度等级。
     */
    @JsonProperty("thinkingLevel")
    private String thinkingLevel;

    /**
     * JSON 属性 {@code fastMode}，表示是否启用快速模式。
     */
    @JsonProperty("fastMode")
    private Boolean fastMode;

    /**
     * JSON 属性 {@code verboseLevel}，表示日志详细级别。
     */
    @JsonProperty("verboseLevel")
    private String verboseLevel;

    /**
     * 返回按对话顺序排列的消息。
     *
     * @return 按对话顺序排列的消息；响应未包含消息时返回空列表
     */
    public List<Object> getMessages() {
        return messages != null ? messages : Collections.emptyList();
    }
}
