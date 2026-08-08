package io.github.easy4j.openclaw;

/**
 * 将调用方取消信号绑定到底层 HTTP Call。
 */
@FunctionalInterface
public interface HttpCallCancellation {

    /**
     * 注册取消动作。
     *
     * @param callback 取消时执行的动作
     * @return 请求结束后用于注销动作的句柄
     */
    AutoCloseable onCancel(Runnable callback);
}
