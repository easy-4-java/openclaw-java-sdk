package io.github.easy4j.openclaw.api.sse;

import io.github.easy4j.openclaw.api.model.ChatChunk;

/**
 * SSE streaming event handler.
 * <p>
 * Used to process Gateway OpenAI OpenResponses SSE streaming.
 * </p>
 *
 * <h3>Chat Completions streamingusage</h3>
 * <pre>{@code
 * client.chatCompletionStream(request, headers, new SseEventHandler() {
 *     public void onEvent(SseEvent event) {
 *         ChatCompletionChunk chunk = (ChatCompletionChunk) event.getParsed();
 *         if (chunk != null && chunk.getChoices() != null) {
 *             chunk.getChoices().forEach(c -> {
 *                 if (c.getDelta() != null && c.getDelta().getContent() != null) {
 *                     System.out.print(c.getDelta().getContent());
 *                 }
 *             });
 *         }
 *     }
 * public void onComplete { System.out.println("\n[completion]"); }
 *     public void onError(Throwable error) { error.printStackTrace(); }
 * });
 * }</pre>
 *
 * <h3>OpenResponses streamingusage</h3>
 * <pre>{@code
 * client.createResponseStream(request, headers, new SseEventHandler() {
 *     public void onEvent(SseEvent event) {
 *         System.out.println("[" + event.getEvent() + "] " + event.getData());
 *     }
 * public void onComplete { System.out.println("[completion]"); }
 *     public void onError(Throwable error) { error.printStackTrace(); }
 * });
 * }</pre>
 *
 * @see SseEvent
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
 * @see <a href="https://docs.openclaw.ai/gateway/openresponses-http-api">OpenResponses API</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public interface SseEventHandler {

    /**
 * SSE event.
     * <p>
 * Chat Completions stream,{@code event.getParsed} {@link ChatChunk}.
 * OpenResponses stream,{@code event.getEvent} event( {@code response.output_text.delta}).
     * </p>
     *
 * @param event SSE event( null,{@code isDone} When true)
     */
    void onEvent(SseEvent event);

    /**
 * stream( {@code data: [DONE]}).
     */
    void onComplete();

    /**
 * stream.
     *
 * @param error
     */
    void onError(Throwable error);
}
