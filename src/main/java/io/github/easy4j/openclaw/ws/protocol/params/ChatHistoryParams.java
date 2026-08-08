package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * {@code chat.history} RPC .
 * <p>aligned {@code ChatHistoryParamsSchema}({@code src/gateway/protocol/schema/logs-chat.ts}).</p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatHistoryParams {

    private final String sessionKey;
    private final Integer limit;
    private final Integer maxChars;

    /**
 * @param sessionKey sessionkey(Required)
 * @param limit message(Optional,Gateway 200, 1000)
     */
    public static ChatHistoryParams of(String sessionKey, Integer limit) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatHistoryParams.builder().sessionKey(sessionKey).limit(limit).build();
    }
}
