package io.github.easy4j.openclaw.cli;

import lombok.Getter;

import java.util.Objects;

/**
 * 本地 {@code openclaw} 子进程的不可变执行结果，完整保留退出码、标准输出和标准错误。
 * 空输出在构造时归一化为空字符串，便于调用方直接判断成功状态和读取诊断信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public final class OpenClawCliResult {

    /**
     * 子进程退出码；零表示命令正常结束。
     */
    private final int exitCode;
    /**
     * 子进程标准输出的完整捕获文本。
     */
    private final String stdout;
    /**
     * 子进程标准错误的完整捕获文本。
     */
    private final String stderr;

    /**
     * 创建一次 CLI 执行结果，并把 {@code null} 输出归一化为空字符串。
     *
     * @param exitCode 本地子进程退出码；非零通常表示执行失败
     * @param stdout 子进程标准输出文本
     * @param stderr 子进程标准错误文本
     */
    public OpenClawCliResult(int exitCode, String stdout, String stderr) {
        this.exitCode = exitCode;
        this.stdout = stdout != null ? stdout : "";
        this.stderr = stderr != null ? stderr : "";
    }

    /**
     * 判断 CLI 子进程是否以退出码 0 结束。
     *
     * @return 子进程退出码为零时返回 {@code true}
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * 生成包含退出码、标准输出和标准错误的诊断文本，便于日志记录和测试断言。
     *
     * @return 不包含完整输出内容、仅包含长度与退出状态的诊断文本
     */
    @Override
    public String toString() {
        return "OpenClawCliResult{exitCode=" + exitCode + ", stdout.len=" + stdout.length()
                + ", stderr.len=" + stderr.length() + '}';
    }

    /**
     * 按退出码、标准输出和标准错误比较两个 CLI 执行结果。
     *
     * @param o 与当前结果比较的对象
     * @return 退出码、标准输出和标准错误均相同时返回 {@code true}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenClawCliResult)) {
            return false;
        }
        OpenClawCliResult that = (OpenClawCliResult) o;
        return exitCode == that.exitCode && Objects.equals(stdout, that.stdout) && Objects.equals(stderr, that.stderr);
    }

    /**
     * 按退出码、标准输出和标准错误计算哈希值。
     *
     * @return 由退出码、标准输出和标准错误计算的哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(exitCode, stdout, stderr);
    }
}
