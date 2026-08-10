package io.github.easy4j.openclaw.ws;

/**
 * {@code ChatStreamHandler} 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface ChatStreamHandler {

    /**
     * 注册文本增量回调；每个 SSE 文本片段到达时按顺序调用。
     *
     * @param text 本次接收到的文本增量，保持服务端事件顺序
     */
    void onDelta(String text);

    /**
     * 注册或处理流完成事件，并向调用方交付累计文本。
     *
     * @param fullText 流式响应累计得到的完整文本
     */
    void onComplete(String fullText);

    /**
     * 注册或处理流、WebSocket 或回调执行异常。
     *
     * @param error 导致调用失败的异常
     */
    void onError(String error);
}
