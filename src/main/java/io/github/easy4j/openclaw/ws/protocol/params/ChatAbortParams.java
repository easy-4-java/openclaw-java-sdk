package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * OpenClaw JSON 协议中的 `ChatAbortParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatAbortParams {

    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    private final String sessionKey;
    /**
     * 映射 OpenClaw JSON 字段 `runId` 的 关联标识。
     */
    private final String runId;

    /**
     * 根据参数构造或读取 `ChatAbortParams` 的 `abortSession` 协议字段。
     *
     * @param sessionKey 会话路由键
     * @return 按方法参数填充的 ChatAbortParams
     */
    public static ChatAbortParams abortSession(String sessionKey) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatAbortParams.builder().sessionKey(sessionKey).build();
    }

    /**
     * 根据参数构造或读取 `ChatAbortParams` 的 `abortRun` 协议字段。
     *
     * @param sessionKey 会话路由键
     * @param runId 运行标识
     * @return 按方法参数填充的 ChatAbortParams
     */
    public static ChatAbortParams abortRun(String sessionKey, String runId) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        Objects.requireNonNull(runId, "runId");
        return ChatAbortParams.builder().sessionKey(sessionKey).runId(runId).build();
    }
}
