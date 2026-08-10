package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityChecker;
import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityReport;
import io.github.easy4j.openclaw.cli.support.SubprocessExecutionSupport;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 本地 openclaw 进程执行器，负责全局参数组装、工作目录和超时解析，并把进程退出、超时和启动失败归一化为 OpenClawCliResult。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Slf4j
public class OpenClawCliExecutor {

    /**
     * 客户端使用的不可变配置引用。
     */
    private final OpenClawCliConfig config;

    /**
     * 按给定配置创建 {@code OpenClawCliExecutor}，构造过程不隐式执行远程业务请求。
     *
     * @param config SDK 配置
     */
    public OpenClawCliExecutor(OpenClawCliConfig config) {
        this.config = Objects.requireNonNull(config, "config");
        SubprocessExecutionSupport.configureMaxConcurrentExecutions(config.getMaxConcurrentExecutions());
    }

    /**
     * 构造并发送 HTTP 请求，读取并关闭响应体，将传输失败或非成功状态映射为 SDK 异常。
     *
     * @param request 要校验、序列化并发送的 {@code OpenClawCliRequest}
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execute(OpenClawCliRequest request) {
        Objects.requireNonNull(request, "request");
        CommandLine cmd = toCommandLine(request);
        long timeoutMs = resolveTimeoutMillis(request);

        File workingDirectory = resolveWorkingDirectory();
        SubprocessExecutionSupport.ExecutionRequest execRequest =
                new SubprocessExecutionSupport.ExecutionRequest(cmd, workingDirectory, null, timeoutMs);

        try {
            SubprocessExecutionSupport.RunSession session = executeSubprocess(execRequest);
            String stdout = new String(session.getStdout().toByteArray(), StandardCharsets.UTF_8);
            String stderr = new String(session.getStderr().toByteArray(), StandardCharsets.UTF_8);

            if (session.timedOut()) {
                log.warn("openclaw timed out after {} ms: {}", timeoutMs, cmd);
                stderr = appendLine(stderr, "openclaw CLI timed out after " + timeoutMs + " ms");
                return new OpenClawCliResult(-1, stdout, stderr);
            }

            DefaultExecuteResultHandler handler = session.getHandler();
            Exception asyncFailure = handler.getException();
            if (asyncFailure != null) {
                log.warn("openclaw async failure: {}", asyncFailure.getMessage());
                if (asyncFailure instanceof ExecuteException) {
                    ExecuteException ex = (ExecuteException) asyncFailure;
                    return new OpenClawCliResult(ex.getExitValue(), stdout, stderr);
                }
                stderr = appendLine(stderr, asyncFailure.getMessage());
                return new OpenClawCliResult(-1, stdout, stderr);
            }

            try {
                int exit = handler.getExitValue();
                return new OpenClawCliResult(exit, stdout, stderr);
            } catch (IllegalStateException e) {
                log.warn("openclaw completed without exit code: {}", e.getMessage());
                stderr = appendLine(stderr, e.getMessage());
                return new OpenClawCliResult(-1, stdout, stderr);
            }
        } catch (Exception e) {
            log.warn("openclaw execution failed: {}", e.getMessage());
            return new OpenClawCliResult(-1, "", e.getMessage());
        }
    }

    /**
 * subprocess(See,inject).
     */
    SubprocessExecutionSupport.RunSession executeSubprocess(SubprocessExecutionSupport.ExecutionRequest request)
            throws Exception {
        return SubprocessExecutionSupport.execute(request);
    }

    private static String appendLine(String base, String line) {
        if (OpenClawStrings.isNotBlank(base)) {
            return base + "\n" + line;
        }
        return line;
    }

    private File resolveWorkingDirectory() {
        String wdProperty = config.getWorkingDirectory();
        if (OpenClawStrings.isBlank(wdProperty)) {
            return null;
        }
        File wd = new File(wdProperty.trim());
        if (!wd.isDirectory()) {
            throw new IllegalArgumentException(
                    "openclaw.local-working-directory is not an existing directory: " + wd.getAbsolutePath());
        }
        return wd;
    }

    private long resolveTimeoutMillis(OpenClawCliRequest request) {
        Integer t = request.getTimeoutSeconds();
        if (t != null && t > 0) {
            return t * 1000L;
        }
        return Math.max(1L, config.getTimeout()) * 1000L;
    }

    /**
     * 把请求中的全局开关和子命令参数转换为 Commons Exec CommandLine，参数按原顺序保留。
     *
     * @param request 要校验、序列化并发送的 {@code OpenClawCliRequest}
     * @return 已按原顺序转义并组装全局参数和子命令参数的命令行
     */
    public CommandLine toCommandLine(OpenClawCliRequest request) {
        CommandLine cmd = new CommandLine(config.getExecutable());
        if (request.isDev()) {
            cmd.addArgument("--dev");
        }
        if (request.getProfile() != null && !request.getProfile().isEmpty()) {
            cmd.addArgument("--profile");
            cmd.addArgument(request.getProfile());
        }
        if (request.getContainer() != null && !request.getContainer().isEmpty()) {
            cmd.addArgument("--container");
            cmd.addArgument(request.getContainer());
        }
        if (request.isNoColor()) {
            cmd.addArgument("--no-color");
        }
        for (String a : request.getArguments()) {
            cmd.addArgument(a);
        }
        return cmd;
    }

    /**
     * 执行轻量级 {@code openclaw --version} 探测并返回可用性、版本和失败原因。
     *
     * @return 从 Gateway、SSE 或本地进程响应解析得到的 OpenClawCliAvailabilityReport
     */
    public OpenClawCliAvailabilityReport probe() {
        return new OpenClawCliAvailabilityChecker().check(this.config);
    }
}
