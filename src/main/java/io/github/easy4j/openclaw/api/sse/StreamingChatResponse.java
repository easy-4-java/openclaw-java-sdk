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
     * 将多个 SSE 片段合并为完整消息和工具调用的累加器。
     */
    private final SseEventAccumulator accumulator = new SseEventAccumulator();

    /**
     * 接收每个文本增量的可选回调。
     */
    @Setter
    private Consumer<String> deltaConsumer;

    /**
     * 接收每个反序列化响应片段的可选回调。
     */
    private Consumer<ChatChunk> chunkConsumer;
    /**
     * 接收已组装工具调用的可选回调。
     */
    private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
    /**
     * 流正常结束后接收完整文本的可选回调。
     */
    private Consumer<String> completeConsumer;
    /**
     * 流读取或回调处理失败时接收异常的可选回调。
     */
    private Consumer<Throwable> errorConsumer;
    /**
     * 保存当前流的取消动作；原子替换保证并发取消最多生效一次。
     */
    private final AtomicReference<Runnable> cancellation = new AtomicReference<>();

    /**
     * 创建空白构建器，供调用方链式设置 {@code StreamingChatResponse} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== 回调注册 ====================

    /**
     * 注册文本增量回调；每个 SSE 文本片段到达时按顺序调用。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onDelta(Consumer<String> callback) {
        this.deltaConsumer = callback;
        return this;
    }

    /**
     * 注册原始聊天片段回调；每次反序列化成功后调用。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onChunk(Consumer<ChatChunk> callback) {
        this.chunkConsumer = callback;
        return this;
    }

    /**
     * 注册工具调用回调；工具参数跨片段组装完成后调用。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
        this.toolCallConsumer = callback;
        return this;
    }

    /**
     * 注册或处理流完成事件，并向调用方交付累计文本。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onComplete(Consumer<String> callback) {
        this.completeConsumer = callback;
        return this;
    }

    /**
     * 注册或处理流、WebSocket 或回调执行异常。
     *
     * @param callback 取消或事件回调
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 StreamingChatResponse
     */
    public StreamingChatResponse onError(Consumer<Throwable> callback) {
        this.errorConsumer = callback;
        return this;
    }

    /**
     * 注册取消时执行的回调，并返回用于注销该回调的句柄。
     *
     * @param action 待执行的 Hook、工具或命令动作
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
     * @return 底层异步任务接受取消请求时返回 {@code true}
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
     * 处理一个 SSE 或 Gateway 事件并更新累计状态。
     *
     * @param event 待分发或累积的 SSE/WebSocket 事件
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
     * 注册或处理流完成事件，并向调用方交付累计文本。
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
     * 注册或处理流、WebSocket 或回调执行异常。
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
     * {@code StreamingChatResponse} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * 接收每个文本增量的可选回调。
         */
        private Consumer<String> deltaConsumer;
        /**
         * 接收每个反序列化响应片段的可选回调。
         */
        private Consumer<ChatChunk> chunkConsumer;
        /**
         * 接收已组装工具调用的可选回调。
         */
        private Consumer<List<ChatMessage.ToolCall>> toolCallConsumer;
        /**
         * 流正常结束后接收完整文本的可选回调。
         */
        private Consumer<String> completeConsumer;
        /**
         * 流读取或回调处理失败时接收异常的可选回调。
         */
        private Consumer<Throwable> errorConsumer;

        /**
         * 设置 {@code --on-delta} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onDelta(Consumer<String> callback) {
            this.deltaConsumer = callback;
            return this;
        }

        /**
         * 设置 {@code --on-chunk} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onChunk(Consumer<ChatChunk> callback) {
            this.chunkConsumer = callback;
            return this;
        }

        /**
         * 设置 {@code --on-tool-call} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onToolCall(Consumer<List<ChatMessage.ToolCall>> callback) {
            this.toolCallConsumer = callback;
            return this;
        }

        /**
         * 设置 {@code --on-complete} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onComplete(Consumer<String> callback) {
            this.completeConsumer = callback;
            return this;
        }

        /**
         * 设置 {@code --on-error} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param callback 取消或事件回调
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onError(Consumer<Throwable> callback) {
            this.errorConsumer = callback;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code StreamingChatResponse}。
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
