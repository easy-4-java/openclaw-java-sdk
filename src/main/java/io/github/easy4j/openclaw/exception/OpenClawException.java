package io.github.easy4j.openclaw.exception;

/**
 * OpenClaw SDK 所有通道异常的基础类型，可保留底层传输或执行失败原因。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class OpenClawException extends RuntimeException {

    /**
     * 异常序列化版本标识。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 按给定配置创建 {@code OpenClawException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     */
    public OpenClawException(String message) {
        super(message);
    }

    /**
     * 按给定配置创建 {@code OpenClawException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 导致当前异常的根本原因
     */
    public OpenClawException(String message, Throwable cause) {
        super(message, cause);
    }
}
