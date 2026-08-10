package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * OpenClaw SDK 在 Open Claw Local Execution 阶段失败时抛出的异常，并保留可用于诊断的原因信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawLocalExecutionException extends OpenClawException {

    /**
     * OpenClaw 协议固定值 {@code 1L}；调用方不应在运行时修改。
     */
    private static final long serialVersionUID = 1L;

    /**
     * `OpenClawLocalExecutionException` 生命周期内保存的 `exitCode` 对应状态。
     */
    private final int exitCode;

    /**
     * 按给定配置创建 `OpenClawLocalExecutionException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param exitCode 写入 `exitCode` 协议字段的内容
     */
    public OpenClawLocalExecutionException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    /**
     * 按给定配置创建 `OpenClawLocalExecutionException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 写入 `cause` 协议字段的内容
     */
    public OpenClawLocalExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.exitCode = Integer.MIN_VALUE;
    }
}
