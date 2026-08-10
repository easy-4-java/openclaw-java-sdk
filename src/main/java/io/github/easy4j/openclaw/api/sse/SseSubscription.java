package io.github.easy4j.openclaw.api.sse;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SSE 订阅的幂等生命周期句柄。关闭句柄会取消底层 Call，并从客户端活动订阅集合中移除。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SseSubscription implements AutoCloseable {

    /**
     * 跨线程生命周期协调状态，保证并发更新的可见性、互斥或容量上限。
     */
    private final AtomicBoolean active = new AtomicBoolean(true);
    /**
     * 可选调用取消令牌。
     */
    private final Runnable cancellation;

    /**
     * 按给定配置创建 `SseSubscription`，构造过程不隐式执行远程业务请求。
     *
     * @param cancellation 可选调用取消令牌
     */
    public SseSubscription(Runnable cancellation) {
        this.cancellation = Objects.requireNonNull(cancellation, "cancellation");
    }

    /**
     * 把取消信号传播到底层网络调用或 Future，并以幂等方式结束当前任务。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean cancel() {
        if (!active.compareAndSet(true, false)) {
            return false;
        }
        cancellation.run();
        return true;
    }

    /**
     * 判断 `active` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * 结束当前生命周期：取消仍在运行的调用，并释放当前对象拥有的连接、执行器或订阅；重复关闭保持安全。
     */
    @Override
    public void close() {
        cancel();
    }
}
