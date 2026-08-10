package io.github.easy4j.openclaw.exception;

import lombok.Getter;

/**
 * HTTP 传输、非成功状态或响应解析失败时抛出的异常，可携带状态码和原始响应体。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawHttpException extends OpenClawException {

    /**
     * 异常序列化版本标识。
     */
    private static final long serialVersionUID = 1L;

    /**
     * HTTP 响应状态码，用于区分成功与协议错误。
     */
    private final int statusCode;

    /**
     * 非成功 HTTP 响应的原始响应体，用于诊断服务端错误。
     */
    private final String responseBody;

    /**
     * 按给定配置创建 {@code OpenClawHttpException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param statusCode HTTP 响应状态码
     * @param responseBody 服务端返回的响应体；可能为空
     */
    public OpenClawHttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * 按给定配置创建 {@code OpenClawHttpException}，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param cause 导致当前异常的根本原因
     */
    public OpenClawHttpException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.responseBody = null;
    }
}
