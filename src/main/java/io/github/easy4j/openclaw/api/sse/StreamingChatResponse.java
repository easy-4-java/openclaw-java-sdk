package io.github.easy4j.openclaw.api.sse;

import io.github.easy4j.openclaw.api.model.ChatChunk;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * SSE streaming response wrapper.
 * <p>
 * Provides a fluent API Used fordelta,:
 * </p>
 * <ul>
 * <li>{@code onDelta} - delta( Spring AI {@code Flux<ChatResponse>})</li>
 * <li>{@code onChunk} - chunk( SSE )</li>
 * <li>{@code onToolCall} - tool calldelta</li>
 * <li>{@code onComplete} - stream</li>
 * <li>{@code onError} - </li>
 * </ul>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * // 1:
 * StreamingChatResponse stream = client.chatCompletionStream(request);
 * stream.onDelta(delta -> System.out.print(delta))
 * .onComplete(fullText -> System.out.println("\\ncompletion: " + fullText))
 *       .onError(error -> error.printStackTrace());
 *
 * //
 * ChatChunk result = stream.get();
 *
 * // 2:Builder
 * StreamingChatResponse stream2 = client.chatCompletionStream(request,
 *     StreamingChatResponse.builder()
 *         .onDelta(delta -> System.out.print(delta))
 *         .onChunk(chunk -> accumulate(chunk))
 *         .onComplete(accumulator::get)
 *         .onError(System.err::println)
 *         .build()
 * );
 * }</pre>
 *
 * @see SseEventHandler
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#streaming-sse">Streaming SSE</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
public class StreamingChatResponse extends CompletableFuture<ChatChunk>
        implements SseEventHandler {

    private final SseEventAccumulator accumulator = new SseEventAccumulator();

    @Setter
    private Consumer<String> deltaConsumer;

    private Consumer<ChatChunk> chunkConsumer;
    private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
    private Consumer<String> completeConsumer;
    private Consumer<Throwable> errorConsumer;

    /**
 * Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== 回调注册 ====================

    /**
 * delta.
     * <p>
 * delta content .
     * </p>
     *
 * @param callback ,delta text
     * @return this
     */
    public StreamingChatResponse onDelta(Consumer<String> callback) {
        this.deltaConsumer = callback;
        return this;
    }

    /**
 * chunk .
     * <p>
 * SSE ,field.
     * </p>
     *
 * @param callback , {@link ChatChunk}
     * @return this
     */
    public StreamingChatResponse onChunk(Consumer<ChatChunk> callback) {
        this.chunkConsumer = callback;
        return this;
    }

    /**
 * tool call.
     * <p>
 * streamtool call,.
     * </p>
     *
 * @param callback ,tool call
     * @return this
     */
    public StreamingChatResponse onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
        this.toolCallConsumer = callback;
        return this;
    }

    /**
 * streamcompletion.
     *
 * @param callback ,
     * @return this
     */
    public StreamingChatResponse onComplete(Consumer<String> callback) {
        this.completeConsumer = callback;
        return this;
    }

    /**
 * .
     *
 * @param callback
     * @return this
     */
    public StreamingChatResponse onError(Consumer<Throwable> callback) {
        this.errorConsumer = callback;
        return this;
    }

    // ==================== SseEventHandler 实现 ====================

    @Override
    public void onEvent(SseEvent event) {
        if (event.isTerminal()) {
            return;
        }
        Object p = event.getParsed();
        if (!(p instanceof ChatChunk)) {
            return;
        }
        final ChatChunk chunk = (ChatChunk) p;

        // 触发 chunk 回调
        if (chunkConsumer != null) {
            chunkConsumer.accept(chunk);
        }

        // 合并到累加器
        accumulator.merge(chunk);

        // 提取 delta
        if (chunk.getChoices() != null && !chunk.getChoices().isEmpty()) {
            ChatChunk.DeltaChoice choice = chunk.getChoices().get(0);
            ChatChunk.DeltaMessage delta = choice.getDelta();

            if (delta != null) {
                // 文本增量
                if (delta.getContent() != null && deltaConsumer != null) {
                    deltaConsumer.accept(delta.getContent());
                }

                // 工具调用增量
                if (delta.getToolCalls() != null && !delta.getToolCalls().isEmpty() && toolCallConsumer != null) {
                    // 直接传递原始 toolCalls
                    toolCallConsumer.accept(delta.getToolCalls());
                }
            }
        }
    }

    @Override
    public void onComplete() {
        ChatChunk result = accumulator.getAccumulated();
        complete(result);
        if (completeConsumer != null) {
            String fullText = result != null && result.getChoices() != null && !result.getChoices().isEmpty()
                ? result.getChoices().get(0).getDelta().getContent()
                : "";
            completeConsumer.accept(fullText);
        }
    }

    @Override
    public void onError(Throwable error) {
        completeExceptionally(error);
        if (errorConsumer != null) {
            errorConsumer.accept(error);
        }
    }

    // ==================== Builder ====================

    public static class Builder {
        private Consumer<String> deltaConsumer;
        private Consumer<ChatChunk> chunkConsumer;
        private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
        private Consumer<String> completeConsumer;
        private Consumer<Throwable> errorConsumer;

        public Builder onDelta(Consumer<String> callback) {
            this.deltaConsumer = callback;
            return this;
        }

        public Builder onChunk(Consumer<ChatChunk> callback) {
            this.chunkConsumer = callback;
            return this;
        }

        public Builder onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
            this.toolCallConsumer = callback;
            return this;
        }

        public Builder onComplete(Consumer<String> callback) {
            this.completeConsumer = callback;
            return this;
        }

        public Builder onError(Consumer<Throwable> callback) {
            this.errorConsumer = callback;
            return this;
        }

        public StreamingChatResponse build() {
            StreamingChatResponse response = new StreamingChatResponse();
            response.deltaConsumer = this.deltaConsumer;
            response.chunkConsumer = this.chunkConsumer;
            response.toolCallConsumer = this.toolCallConsumer;
            response.completeConsumer = this.completeConsumer;
            response.errorConsumer = this.errorConsumer;
            return response;
        }
    }
}
