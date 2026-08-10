package io.github.easy4j.openclaw.ws;

/**
 * {@code chat.send} streaming reply handler.
 * <p>Gateway {@code chat.send} {@code event: "chat"} event,
 * {@code delta: true} delta text,{@code done: true} completion.</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/protocol">Gateway Protocol</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public interface ChatStreamHandler {

    /**
 * delta text.
     *
 * @param text delta text
     */
    void onDelta(String text);

    /**
 * completion.
     *
 * @param fullText
     */
    void onComplete(String fullText);

    /**
 * .
     *
 * @param error error message
     */
    void onError(String error);
}
