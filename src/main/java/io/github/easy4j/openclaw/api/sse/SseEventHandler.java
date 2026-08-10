package io.github.easy4j.openclaw.api.sse;

import io.github.easy4j.openclaw.api.model.ChatChunk;

/**
 * {@code SseEventHandler} 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface SseEventHandler {

    /**
     * 处理一个 SSE 或 Gateway 事件并更新累计状态。
     *
     * @param event 待分发或累积的 SSE/WebSocket 事件
     */
    void onEvent(SseEvent event);

    /**
     * 注册或处理流完成事件，并向调用方交付累计文本。
     */
    void onComplete();

    /**
     * 注册或处理流、WebSocket 或回调执行异常。
     *
     * @param error 导致调用失败的异常
     */
    void onError(Throwable error);
}
