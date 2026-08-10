package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * chat.abort RPC 参数，可中止整个会话或指定运行。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatAbortParams {

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private final String sessionKey;
    /**
     * JSON 属性 {@code runId}，表示一次智能体运行的标识。
     */
    private final String runId;

    /**
     * 创建中止指定会话全部运行的请求参数。
     *
     * @param sessionKey 会话路由键
     * @return 按方法参数填充的 ChatAbortParams
     */
    public static ChatAbortParams abortSession(String sessionKey) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatAbortParams.builder().sessionKey(sessionKey).build();
    }

    /**
     * 创建仅中止指定会话中某次运行的请求参数。
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
