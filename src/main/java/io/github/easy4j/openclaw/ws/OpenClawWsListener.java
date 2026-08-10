package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import io.github.easy4j.openclaw.ws.protocol.ResponseFrame;

/**
 * {@code OpenClawWsListener} 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface OpenClawWsListener {

    /**
     * 在 Gateway 握手成功后通知监听器协商结果。
     *
     * @param helloOk Gateway 握手成功后返回的能力与版本信息
     */
    default void onConnected(HelloOk helloOk) {}

    /**
     * 在 WebSocket 关闭后通知监听器状态码、原因和发起方。
     *
     * @param code HTTP、WebSocket 关闭或进程退出状态码
     * @param reason 连接关闭、结束或失败原因
     * @param remote 关闭事件是否由远端发起
     */
    default void onDisconnected(int code, String reason, boolean remote) {}

    /**
     * 注册或处理流、WebSocket 或回调执行异常。
     *
     * @param ex WebSocket 处理过程中报告的异常
     */
    default void onError(Exception ex) {}

    /**
     * 处理一个 SSE 或 Gateway 事件并更新累计状态。
     *
     * @param frame 待处理的 Gateway 协议帧
     */
    default void onEvent(EventFrame frame) {}

    /**
     * 处理 HTTP 或 Gateway 响应，并完成对应异步请求。
     *
     * @param frame 待处理的 Gateway 协议帧
     */
    default void onResponse(ResponseFrame frame) {}
}
