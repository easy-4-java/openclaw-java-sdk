package io.github.easy4j.openclaw.api.sse;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可取消的 SSE 订阅句柄。
 *
 * <p>取消和关闭均为幂等操作；句柄不会向调用方暴露 OkHttp {@code Call}。</p>
 */
public final class SseSubscription implements AutoCloseable {

    private final AtomicBoolean active = new AtomicBoolean(true);
    private final Runnable cancellation;

    /**
     * 创建订阅句柄。
     *
     * @param cancellation 底层取消动作
     */
    public SseSubscription(Runnable cancellation) {
        this.cancellation = Objects.requireNonNull(cancellation, "cancellation");
    }

    /**
     * 取消订阅。
     *
     * @return 本次调用是否实际执行了取消动作
     */
    public boolean cancel() {
        if (!active.compareAndSet(true, false)) {
            return false;
        }
        cancellation.run();
        return true;
    }

    /**
     * 判断订阅是否仍处于活动状态。
     *
     * @return 活动状态
     */
    public boolean isActive() {
        return active.get();
    }

    /** 关闭订阅，语义等同于取消。 */
    @Override
    public void close() {
        cancel();
    }
}
