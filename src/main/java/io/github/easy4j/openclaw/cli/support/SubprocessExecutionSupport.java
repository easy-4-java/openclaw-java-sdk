package io.github.easy4j.openclaw.cli.support;

import lombok.Getter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CLI 子进程生命周期支撑。信号量限制并发进程数，Commons Exec Watchdog 终止超时进程，流泵收集 stdout 与 stderr。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SubprocessExecutionSupport {

    /**
     * 用于限制等待时间的默认值 {@code 5_000L}，单位由字段名声明。
     */
    public static final long WAIT_GRACE_MILLIS = 5_000L;

    /**
     * OpenClaw 协议固定值 {@code Math.max(2, Runtime.getRuntime().availableProcessors())}；调用方不应在运行时修改。
     */
    private static final int DEFAULT_MAX_CONCURRENT = Math.max(2, Runtime.getRuntime().availableProcessors());

    /**
     * OpenClaw 协议固定值 {@code new AtomicReference<>(new Semaphore(DEFAULT_MAX_CONCURRENT))}；调用方不应在运行时修改。
     */
    private static final AtomicReference<Semaphore> CONCURRENCY_LIMIT =
            new AtomicReference<>(new Semaphore(DEFAULT_MAX_CONCURRENT));

    private SubprocessExecutionSupport() {
    }

    /**
     * 原子替换 CLI 子进程并发信号量；非正数恢复为按 CPU 核心数计算的默认上限。
     *
     * @param maxConcurrent 写入 `maxConcurrent` 协议字段的内容
     */
    public static void configureMaxConcurrentExecutions(int maxConcurrent) {
        if (maxConcurrent <= 0) {
            CONCURRENCY_LIMIT.set(new Semaphore(DEFAULT_MAX_CONCURRENT));
            return;
        }
        CONCURRENCY_LIMIT.set(new Semaphore(maxConcurrent));
    }

    /**
     * 返回未显式配置时使用的 CLI 子进程并发上限，该值至少为 2。
     *
     * @return 当前计数、状态码、可空配置或毫秒级时间值
     */
    public static int defaultMaxConcurrentExecutions() {
        return DEFAULT_MAX_CONCURRENT;
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 请求对象
     * @return 保存退出处理器、输出缓冲区和超时状态的子进程会话
     * @throws IOException 网络、流或子进程 I/O 失败时抛出
     * @throws InterruptedException 等待线程被中断时抛出，并恢复中断标记
     */
    public static RunSession execute(ExecutionRequest request) throws IOException, InterruptedException {
        Objects.requireNonNull(request, "request");
        Semaphore limit = CONCURRENCY_LIMIT.get();
        // CLI 是显式本地阻塞通道；信号量限制并发子进程数，HTTP/SSE 不经过此处。
        limit.acquire();
        try {
            return executeWithinLimit(request);
        } finally {
            limit.release();
        }
    }

    private static RunSession executeWithinLimit(ExecutionRequest request) throws IOException, InterruptedException {
        long timeoutMs = Math.max(1L, request.getTimeoutMillis());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        DefaultExecutor.Builder builder = DefaultExecutor.builder();
        if (request.getWorkingDirectory() != null) {
            builder.setWorkingDirectory(request.getWorkingDirectory());
        }
        DefaultExecutor executor = builder.get();
        executor.setStreamHandler(new PumpStreamHandler(out, err));

        ExecuteWatchdog watchdog =
                ExecuteWatchdog.builder().setTimeout(Duration.ofMillis(timeoutMs)).get();
        executor.setWatchdog(watchdog);

        DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
        Map<String, String> environment = request.getEnvironment();
        if (environment != null) {
            executor.execute(request.getCommandLine(), environment, handler);
        } else {
            executor.execute(request.getCommandLine(), handler);
        }

        boolean finished = awaitResult(handler, timeoutMs + WAIT_GRACE_MILLIS);
        boolean waitTimedOut = !finished;
        if (waitTimedOut) {
            // 超时后先销毁进程，再给予流泵短暂宽限期以收集剩余 stdout/stderr。
            watchdog.destroyProcess();
            awaitResult(handler, WAIT_GRACE_MILLIS);
        }

        return new RunSession(out, err, handler, watchdog, timeoutMs, waitTimedOut);
    }

    private static boolean awaitResult(DefaultExecuteResultHandler handler, long timeoutMillis)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + Math.max(1L, timeoutMillis);
        // Commons Exec 没有带超时的 waitFor；短轮询只发生在受并发上限保护的 CLI 调用线程。
        while (!handler.hasResult()) {
            if (System.currentTimeMillis() >= deadline) {
                return false;
            }
            Thread.sleep(Math.min(50L, deadline - System.currentTimeMillis()));
        }
        return true;
    }

    /**
     * 一次 CLI 子进程执行请求，包含命令行、工作目录、环境变量和毫秒级超时。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    public static final class ExecutionRequest {

        /**
         * `ExecutionRequest` 生命周期内保存的 `commandLine` 对应状态。
         */
        private final CommandLine commandLine;
        /**
         * `ExecutionRequest` 生命周期内保存的 `workingDirectory` 对应状态。
         */
        private final File workingDirectory;
        /**
         * `ExecutionRequest` 生命周期内保存的 `environment` 对应状态。
         */
        private final Map<String, String> environment;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private final long timeoutMillis;

        /**
         * 按给定配置创建 `ExecutionRequest`，构造过程不隐式执行远程业务请求。
         *
         * @param commandLine 写入 `commandLine` 协议字段的内容
         * @param workingDirectory 写入 `workingDirectory` 协议字段的内容
         * @param environment 写入 `environment` 协议字段的内容
         * @param timeoutMillis 超时时间，单位为毫秒
         */
        public ExecutionRequest(
                CommandLine commandLine,
                File workingDirectory,
                Map<String, String> environment,
                long timeoutMillis) {
            this.commandLine = Objects.requireNonNull(commandLine, "commandLine");
            this.workingDirectory = workingDirectory;
            this.environment = environment;
            this.timeoutMillis = timeoutMillis;
        }
    }

    /**
     * 一次已启动 CLI 子进程的执行会话，暴露输出缓冲区、异步退出处理器、Watchdog 和超时判定。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    public static final class RunSession {

        /**
         * `RunSession` 生命周期内保存的 `stdout` 对应状态。
         */
        private final ByteArrayOutputStream stdout;
        /**
         * `RunSession` 生命周期内保存的 `stderr` 对应状态。
         */
        private final ByteArrayOutputStream stderr;
        /**
         * 事件处理器。
         */
        private final DefaultExecuteResultHandler handler;
        /**
         * `RunSession` 生命周期内保存的 `watchdog` 对应状态。
         */
        private final ExecuteWatchdog watchdog;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private final long timeoutMillis;
        /**
         * `RunSession` 生命周期内保存的 `waitTimedOut` 对应状态。
         */
        private final boolean waitTimedOut;

        RunSession(
                ByteArrayOutputStream stdout,
                ByteArrayOutputStream stderr,
                DefaultExecuteResultHandler handler,
                ExecuteWatchdog watchdog,
                long timeoutMillis,
                boolean waitTimedOut) {
            this.stdout = stdout;
            this.stderr = stderr;
            this.handler = handler;
            this.watchdog = watchdog;
            this.timeoutMillis = timeoutMillis;
            this.waitTimedOut = waitTimedOut;
        }

        /**
         * 同时检查等待宽限期和 Watchdog 状态，判断子进程是否因超时被终止。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean timedOut() {
            return waitTimedOut || watchdog.killedProcess();
        }
    }
}
