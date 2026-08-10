package io.github.easy4j.openclaw.api.sse;

import io.github.easy4j.openclaw.api.model.ChatChunk;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 流式对话结果句柄，同时是 CompletableFuture 和 SSE 事件处理器。它累积 ChatChunk，并分发文本、工具调用、完成和失败回调。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class StreamingChatResponse extends CompletableFuture<ChatChunk>
        implements SseEventHandler {

    /**
     * `StreamingChatResponse` 生命周期内保存的 `accumulator` 对应状态。
     */
    private final SseEventAccumulator accumulator = new SseEventAccumulator();

    /**
     * `StreamingChatResponse` 生命周期内保存的 `deltaConsumer` 对应状态。
     */
    @Setter
    private Consumer<String> deltaConsumer;

    /**
     * `StreamingChatResponse` 生命周期内保存的 `chunkConsumer` 对应状态。
     */
    private Consumer<ChatChunk> chunkConsumer;
    /**
     * `StreamingChatResponse` 生命周期内保存的 `toolCallConsumer` 对应状态。
     */
    private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
    /**
     * `StreamingChatResponse` 生命周期内保存的 `completeConsumer` 对应状态。
     */
    private Consumer<String> completeConsumer;
    /**
     * `StreamingChatResponse` 生命周期内保存的 `errorConsumer` 对应状态。
     */
    private Consumer<Throwable> errorConsumer;
    /**
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final AtomicReference<Runnable> cancellation = new AtomicReference<>();

    /**
     * 创建空白构建器，供调用方链式设置 `StreamingChatResponse` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== 回调注册 ====================

    /**
     * 接收并处理 Delta 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onDelta(Consumer<String> callback) {
        this.deltaConsumer = callback;
        return this;
    }

    /**
     * 接收并处理 Chunk 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onChunk(Consumer<ChatChunk> callback) {
        this.chunkConsumer = callback;
        return this;
    }

    /**
     * 接收并处理 Tool Call 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
        this.toolCallConsumer = callback;
        return this;
    }

    /**
     * 接收并处理 Complete 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onComplete(Consumer<String> callback) {
        this.completeConsumer = callback;
        return this;
    }

    /**
     * 接收并处理 Error 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onError(Consumer<Throwable> callback) {
        this.errorConsumer = callback;
        return this;
    }

    /**
     * 接收并处理 Cancel 生命周期事件；实现不会改变事件顺序。
     *
     * @param action 写入 `action` 协议字段的内容
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onCancel(Runnable action) {
        cancellation.set(Objects.requireNonNull(action, "action"));
        if (isCancelled()) {
            action.run();
        }
        return this;
    }

    /**
     * 把取消信号传播到底层网络调用或 Future，并以幂等方式结束当前任务。
     *
     * @param mayInterruptIfRunning 是否允许中断正在执行的任务
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        Runnable action = cancellation.getAndSet(null);
        if (Objects.nonNull(action)) {
            action.run();
        }
        return super.cancel(mayInterruptIfRunning);
    }

    // ==================== SseEventHandler 实现 ====================

    /**
     * 接收并处理 Event 生命周期事件；实现不会改变事件顺序。
     *
     * @param event 写入 `event` 协议字段的内容
     */
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

        // 原始 chunk 先交给观察者，再进入累加器，兼顾逐帧追踪与最终聚合结果。
        if (chunkConsumer != null) {
            chunkConsumer.accept(chunk);
        }

        // 按 choice/tool-call 索引合并增量，工具参数被拆帧时仍保持顺序。
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

    /**
     * 接收并处理 Complete 生命周期事件；实现不会改变事件顺序。
     */
    @Override
    public void onComplete() {
        // Future 的终值始终是完整 ChatChunk；便捷文本回调在其后接收最终文本。
        ChatChunk result = accumulator.getAccumulated();
        complete(result);
        if (completeConsumer != null) {
            String fullText = result != null && result.getChoices() != null && !result.getChoices().isEmpty()
                ? result.getChoices().get(0).getDelta().getContent()
                : "";
            completeConsumer.accept(fullText);
        }
    }

    /**
     * 接收并处理 Error 生命周期事件；实现不会改变事件顺序。
     *
     * @param error 导致调用失败的异常
     */
    @Override
    public void onError(Throwable error) {
        completeExceptionally(error);
        if (errorConsumer != null) {
            errorConsumer.accept(error);
        }
    }

    // ==================== Builder ====================

    /**
     * 链式构建器，逐项收集 StreamingChatResponse 的字段；build() 会复制当前快照，后续修改不会影响已构造的 StreamingChatResponse。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * `Builder` 生命周期内保存的 `deltaConsumer` 对应状态。
         */
        private Consumer<String> deltaConsumer;
        /**
         * `Builder` 生命周期内保存的 `chunkConsumer` 对应状态。
         */
        private Consumer<ChatChunk> chunkConsumer;
        /**
         * `Builder` 生命周期内保存的 `toolCallConsumer` 对应状态。
         */
        private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
        /**
         * `Builder` 生命周期内保存的 `completeConsumer` 对应状态。
         */
        private Consumer<String> completeConsumer;
        /**
         * `Builder` 生命周期内保存的 `errorConsumer` 对应状态。
         */
        private Consumer<Throwable> errorConsumer;

        /**
         * 设置 `--on-delta` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onDelta(Consumer<String> callback) {
            this.deltaConsumer = callback;
            return this;
        }

        /**
         * 设置 `--on-chunk` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onChunk(Consumer<ChatChunk> callback) {
            this.chunkConsumer = callback;
            return this;
        }

        /**
         * 设置 `--on-tool-call` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
            this.toolCallConsumer = callback;
            return this;
        }

        /**
         * 设置 `--on-complete` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onComplete(Consumer<String> callback) {
            this.completeConsumer = callback;
            return this;
        }

        /**
         * 设置 `--on-error` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onError(Consumer<Throwable> callback) {
            this.errorConsumer = callback;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `StreamingChatResponse`。
         *
         * @return 按当前字段创建的 StreamingChatResponse
         */
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
