package io.github.easy4j.openclaw.ws;

/**
 * `ChatStreamHandler` 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface ChatStreamHandler {

    /**
     * 接收并处理 Delta 生命周期事件；实现不会改变事件顺序。
     *
     * @param text 写入 `text` 协议字段的内容
     */
    void onDelta(String text);

    /**
     * 接收并处理 Complete 生命周期事件；实现不会改变事件顺序。
     *
     * @param fullText 写入 `fullText` 协议字段的内容
     */
    void onComplete(String fullText);

    /**
     * 接收并处理 Error 生命周期事件；实现不会改变事件顺序。
     *
     * @param error 导致调用失败的异常
     */
    void onError(String error);
}
