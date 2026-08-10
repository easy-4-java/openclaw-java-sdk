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
     * 创建 CLI 执行器，并把配置的最大并发数同步到共享子进程执行支持组件。
     *
     * @param config 可执行文件、工作目录、默认超时和最大并发数配置
     */
    public OpenClawCliExecutor(OpenClawCliConfig config) {
        this.config = Objects.requireNonNull(config, "config");
        SubprocessExecutionSupport.configureMaxConcurrentExecutions(config.getMaxConcurrentExecutions());
    }

    /**
     * 启动 CLI 子进程并等待异步结果，将超时、启动异常和非零退出统一转换为执行结果。
     *
     * @param request 包含全局开关、子命令参数和可选超时的 CLI 请求
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
     * 启动底层子进程会话；该隔离点允许测试替换实际进程执行。
     *
     * @param request 已解析命令行、工作目录、环境和超时的执行请求
     * @return 已启动且包含输出缓冲区与退出处理器的会话
     * @throws Exception 子进程无法创建或执行支持组件初始化失败时抛出
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
     * @param request 包含全局开关和保持原顺序子命令参数的 CLI 请求
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
     * @return 包含可执行文件解析和 {@code --version} 探测结果的报告
     */
    public OpenClawCliAvailabilityReport probe() {
        return new OpenClawCliAvailabilityChecker().check(this.config);
    }
}
