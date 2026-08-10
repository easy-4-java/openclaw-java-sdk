package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * 本地 CLI 子进程无法启动或以失败状态结束时抛出的异常，保留退出码或根本原因。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawLocalExecutionException extends OpenClawException {

    /**
     * 异常序列化版本标识。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 子进程退出码；零表示命令正常结束。
     */
    private final int exitCode;

    /**
     * 按给定配置创建 {@code OpenClawLocalExecutionException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param exitCode 本地子进程退出码；非零通常表示执行失败
     */
    public OpenClawLocalExecutionException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    /**
     * 按给定配置创建 {@code OpenClawLocalExecutionException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 导致当前异常的根本原因
     */
    public OpenClawLocalExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.exitCode = Integer.MIN_VALUE;
    }
}
