package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * OpenClaw SDK 在 Open Claw Http 阶段失败时抛出的异常，并保留可用于诊断的原因信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawHttpException extends OpenClawException {

    /**
     * OpenClaw 协议固定值 {@code 1L}；调用方不应在运行时修改。
     */
    private static final long serialVersionUID = 1L;

    /**
     * `OpenClawHttpException` 生命周期内保存的 `statusCode` 对应状态。
     */
    private final int statusCode;

    /**
     * `OpenClawHttpException` 生命周期内保存的 `responseBody` 对应状态。
     */
    private final String responseBody;

    /**
     * 按给定配置创建 `OpenClawHttpException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param statusCode 写入 `statusCode` 协议字段的内容
     * @param responseBody 写入 `responseBody` 协议字段的内容
     */
    public OpenClawHttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * 按给定配置创建 `OpenClawHttpException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 写入 `cause` 协议字段的内容
     */
    public OpenClawHttpException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.responseBody = null;
    }
}
