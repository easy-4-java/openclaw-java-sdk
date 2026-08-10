package io.github.easy4j.openclaw.ws;

import io.github.easy4j.openclaw.ws.protocol.EventFrame;
import io.github.easy4j.openclaw.ws.protocol.HelloOk;
import io.github.easy4j.openclaw.ws.protocol.ResponseFrame;

/**
 * `OpenClawWsListener` 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface OpenClawWsListener {

    /**
     * 接收并处理 Connected 生命周期事件；实现不会改变事件顺序。
     *
     * @param helloOk 写入 `helloOk` 协议字段的内容
     */
    default void onConnected(HelloOk helloOk) {}

    /**
     * 接收并处理 Disconnected 生命周期事件；实现不会改变事件顺序。
     *
     * @param code 写入 `code` 协议字段的内容
     * @param reason 写入 `reason` 协议字段的内容
     * @param remote 写入 `remote` 协议字段的内容
     */
    default void onDisconnected(int code, String reason, boolean remote) {}

    /**
     * 接收并处理 Error 生命周期事件；实现不会改变事件顺序。
     *
     * @param ex 写入 `ex` 协议字段的内容
     */
    default void onError(Exception ex) {}

    /**
     * 接收并处理 Event 生命周期事件；实现不会改变事件顺序。
     *
     * @param frame 写入 `frame` 协议字段的内容
     */
    default void onEvent(EventFrame frame) {}

    /**
     * 接收并处理 Response 生命周期事件；实现不会改变事件顺序。
     *
     * @param frame 写入 `frame` 协议字段的内容
     */
    default void onResponse(ResponseFrame frame) {}
}
