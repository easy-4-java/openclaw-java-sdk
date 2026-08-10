package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * chat.history RPC 查询参数，限定会话、条数和最大字符数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatHistoryParams {

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private final String sessionKey;
    /**
     * JSON 属性 {@code limit}，表示最大返回条数。
     */
    private final Integer limit;
    /**
     * JSON 属性 {@code maxChars}，表示最大字符数。
     */
    private final Integer maxChars;

    /**
     * 创建指定会话的历史查询参数，并可限制返回消息数量。
     *
     * @param sessionKey 会话路由键
     * @param limit 最多返回的记录数；为空时使用 Gateway 默认限制
     * @return 按方法参数填充的 ChatHistoryParams
     */
    public static ChatHistoryParams of(String sessionKey, Integer limit) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatHistoryParams.builder().sessionKey(sessionKey).limit(limit).build();
    }
}
