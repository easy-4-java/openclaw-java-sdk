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
     * 订阅活动标记；取消线程与流读取线程通过原子变量协调结束。
     */
    private final AtomicBoolean active = new AtomicBoolean(true);
    /**
     * 可选调用取消令牌。
     */
    private final Runnable cancellation;

    /**
     * 创建订阅句柄，并保存关闭时传播到底层网络调用的取消动作。
     *
     * @param cancellation 关闭或取消订阅时执行的底层调用取消动作，不能为 {@code null}
     */
    public SseSubscription(Runnable cancellation) {
        this.cancellation = Objects.requireNonNull(cancellation, "cancellation");
    }

    /**
     * 把取消信号传播到底层网络调用或 Future，并以幂等方式结束当前任务。
     *
     * @return 本次调用成功把订阅从活动状态切换为已取消时返回 {@code true}
     */
    public boolean cancel() {
        if (!active.compareAndSet(true, false)) {
            return false;
        }
        cancellation.run();
        return true;
    }

    /**
     * 判断 SSE 订阅尚未取消且读取任务尚未结束。
     *
     * @return 订阅尚未取消且底层调用仍可继续接收事件时返回 {@code true}
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
