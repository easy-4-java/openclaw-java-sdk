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
     * 注册取消时执行的回调，并返回用于注销该回调的句柄。
     *
     * @param callback 首次取消时执行的回调；注册到已取消令牌时立即执行
     * @return 请求终止后用于注销取消回调的句柄
     */
    AutoCloseable onCancel(Runnable callback);
}
