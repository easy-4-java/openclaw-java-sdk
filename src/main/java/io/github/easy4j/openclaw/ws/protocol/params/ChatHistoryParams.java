package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * OpenClaw JSON 协议中的 `ChatHistoryParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatHistoryParams {

    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    private final String sessionKey;
    /**
     * 映射 OpenClaw JSON 字段 `limit` 的 协议内容。
     */
    private final Integer limit;
    /**
     * 映射 OpenClaw JSON 字段 `maxChars` 的 协议内容。
     */
    private final Integer maxChars;

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `ChatHistoryParams`。
     *
     * @param sessionKey 会话路由键
     * @param limit 写入 `limit` 协议字段的内容
     * @return 按方法参数填充的 ChatHistoryParams
     */
    public static ChatHistoryParams of(String sessionKey, Integer limit) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatHistoryParams.builder().sessionKey(sessionKey).limit(limit).build();
    }
}
