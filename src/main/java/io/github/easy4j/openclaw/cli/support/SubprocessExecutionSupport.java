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
     * 网络、握手或进程等待的默认超时为 {@code 5_000L}，单位为字段声明的计量单位。
     */
    public static final long WAIT_GRACE_MILLIS = 5_000L;

    /**
     * 默认并发子进程上限，至少为 2，并随可用处理器数量增长。
     */
    private static final int DEFAULT_MAX_CONCURRENT = Math.max(2, Runtime.getRuntime().availableProcessors());

    /**
     * 可原子替换的子进程并发信号量；所有 CLI 执行共享该限制。
     */
    private static final AtomicReference<Semaphore> CONCURRENCY_LIMIT =
            new AtomicReference<>(new Semaphore(DEFAULT_MAX_CONCURRENT));

    private SubprocessExecutionSupport() {
    }

    /**
     * 原子替换 CLI 子进程并发信号量；非正数恢复为按 CPU 核心数计算的默认上限。
     *
     * @param maxConcurrent 允许同时运行的 CLI 子进程上限
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
     * @return 默认允许同时运行的 CLI 子进程数量
     */
    public static int defaultMaxConcurrentExecutions() {
        return DEFAULT_MAX_CONCURRENT;
    }

    /**
     * 启动异步子进程，捕获标准输出和标准错误，并返回负责等待与超时判定的执行会话。
     *
     * @param request 命令行、工作目录、环境变量和毫秒级超时配置
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
         * 即将执行的 Commons Exec 命令行快照。
         */
        private final CommandLine commandLine;
        /**
         * 子进程工作目录；为空时继承当前进程目录。
         */
        private final File workingDirectory;
        /**
         * 传递给子进程的环境变量副本。
         */
        private final Map<String, String> environment;
        /**
         * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
         */
        private final long timeoutMillis;

        /**
         * 创建不可变执行请求；环境变量映射由执行支持组件在启动子进程时读取。
         *
         * @param commandLine 包含可执行文件和参数的 Commons Exec 命令行
         * @param workingDirectory 子进程工作目录；为空时继承当前进程目录
         * @param environment 传递给子进程的环境变量；为空时继承当前进程环境
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
         * 并发收集子进程标准输出字节的内存缓冲区。
         */
        private final ByteArrayOutputStream stdout;
        /**
         * 并发收集子进程标准错误字节的内存缓冲区。
         */
        private final ByteArrayOutputStream stderr;
        /**
         * 接收聊天流增量、完成和异常通知的处理器。
         */
        private final DefaultExecuteResultHandler handler;
        /**
         * 负责在超时后终止子进程的 Commons Exec Watchdog。
         */
        private final ExecuteWatchdog watchdog;
        /**
         * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
         */
        private final long timeoutMillis;
        /**
         * 等待线程是否先于进程完成而超时，用于区分主动终止。
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
         * @return 子进程是否因等待超时而结束
         */
        public boolean timedOut() {
            return waitTimedOut || watchdog.killedProcess();
        }
    }
}
