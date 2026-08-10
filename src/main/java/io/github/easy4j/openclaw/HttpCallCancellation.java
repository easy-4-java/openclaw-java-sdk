package io.github.easy4j.openclaw;

/**
 * HTTP 取消信号接口。实现方注册 Call.cancel 回调，并返回请求结束时用于注销该回调的句柄。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface HttpCallCancellation {

    /**
     * 接收并处理 Cancel 生命周期事件；实现不会改变事件顺序。
     *
     * @param callback 取消或事件回调
     * @return 请求终止后用于注销取消回调的句柄
     */
    AutoCloseable onCancel(Runnable callback);
}
