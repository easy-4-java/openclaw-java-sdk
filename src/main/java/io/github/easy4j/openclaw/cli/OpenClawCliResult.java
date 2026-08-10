package io.github.easy4j.openclaw.cli;

import lombok.Getter;

import java.util.Objects;

/**
 * 本地 openclaw CLI 的 `OpenClawCliResult` 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public final class OpenClawCliResult {

    /**
     * `OpenClawCliResult` 生命周期内保存的 `exitCode` 对应状态。
     */
    private final int exitCode;
    /**
     * `OpenClawCliResult` 生命周期内保存的 `stdout` 对应状态。
     */
    private final String stdout;
    /**
     * `OpenClawCliResult` 生命周期内保存的 `stderr` 对应状态。
     */
    private final String stderr;

    /**
     * 按给定配置创建 `OpenClawCliResult`，构造过程不隐式执行远程业务请求。
     *
     * @param exitCode 写入 `exitCode` 协议字段的内容
     * @param stdout 写入 `stdout` 协议字段的内容
     * @param stderr 写入 `stderr` 协议字段的内容
     */
    public OpenClawCliResult(int exitCode, String stdout, String stderr) {
        this.exitCode = exitCode;
        this.stdout = stdout != null ? stdout : "";
        this.stderr = stderr != null ? stderr : "";
    }

    /**
     * 判断 `success` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * 生成包含退出码、标准输出和标准错误的诊断文本，便于日志记录和测试断言。
     *
     * @return 服务返回或流式累积得到的文本
     */
    @Override
    public String toString() {
        return "OpenClawCliResult{exitCode=" + exitCode + ", stdout.len=" + stdout.length()
                + ", stderr.len=" + stderr.length() + '}';
    }

    /**
     * 按退出码、标准输出和标准错误比较两个 CLI 执行结果。
     *
     * @param o 写入 `o` 协议字段的内容
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
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
     * 判断 `hCode` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public int hashCode() {
        return Objects.hash(exitCode, stdout, stderr);
    }
}
