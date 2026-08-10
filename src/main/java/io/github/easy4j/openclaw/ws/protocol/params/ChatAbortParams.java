package io.github.easy4j.openclaw.ws.protocol.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * {@code chat.abort} RPC .
 * <p>aligned {@code ChatAbortParamsSchema}({@code src/gateway/protocol/schema/logs-chat.ts}).</p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatAbortParams {

    private final String sessionKey;
    private final String runId;

    /**
 * session run.
     */
    public static ChatAbortParams abortSession(String sessionKey) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        return ChatAbortParams.builder().sessionKey(sessionKey).build();
    }

    /**
 * run.
     */
    public static ChatAbortParams abortRun(String sessionKey, String runId) {
        Objects.requireNonNull(sessionKey, "sessionKey");
        Objects.requireNonNull(runId, "runId");
        return ChatAbortParams.builder().sessionKey(sessionKey).runId(runId).build();
    }
}
