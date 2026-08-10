package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.ws.protocol.ErrorShape;
import lombok.Getter;

/**
 * OpenClaw SDK 在 Open Claw Ws Rpc 阶段失败时抛出的异常，并保留可用于诊断的原因信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawWsRpcException extends OpenClawException {

    /**
     * OpenClaw 协议固定值 {@code 1L}；调用方不应在运行时修改。
     */
    private static final long serialVersionUID = 1L;

    /**
     * `OpenClawWsRpcException` 生命周期内保存的 `method` 对应状态。
     */
    private final String method;

    /**
     * 导致调用失败的异常。
     */
    private final ErrorShape error;

    /**
     * 按给定配置创建 `OpenClawWsRpcException`，构造过程不隐式执行远程业务请求。
     *
     * @param method 写入 `method` 协议字段的内容
     * @param error 导致调用失败的异常
     */
    public OpenClawWsRpcException(String method, ErrorShape error) {
        super(buildMessage(method, error));
        this.method = method;
        this.error = error;
    }

    private static String buildMessage(String method, ErrorShape error) {
        if (error != null && error.getMessage() != null) {
            return "Gateway RPC failed: " + method + " — " + error.getMessage();
        }
        return "Gateway RPC failed: " + method;
    }
}
