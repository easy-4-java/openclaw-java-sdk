package io.github.easy4j.openclaw.exception;

/**
 * OpenClaw SDK 在 Open Claw 阶段失败时抛出的异常，并保留可用于诊断的原因信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class OpenClawException extends RuntimeException {

    /**
     * OpenClaw 协议固定值 {@code 1L}；调用方不应在运行时修改。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 按给定配置创建 `OpenClawException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     */
    public OpenClawException(String message) {
        super(message);
    }

    /**
     * 按给定配置创建 `OpenClawException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 写入 `cause` 协议字段的内容
     */
    public OpenClawException(String message, Throwable cause) {
        super(message, cause);
    }
}
