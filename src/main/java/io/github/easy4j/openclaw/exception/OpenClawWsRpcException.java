package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.ws.protocol.ErrorShape;
import lombok.Getter;

/**
 * Gateway WebSocket RPC 返回错误帧时抛出的异常，保留方法名和结构化错误信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawWsRpcException extends OpenClawException {

    /**
     * 异常序列化版本标识。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 失败或待完成的 Gateway RPC 方法名，用于关联响应和诊断。
     */
    private final String method;

    /**
     * 导致调用失败的异常。
     */
    private final ErrorShape error;

    /**
     * 根据失败的 RPC 方法和 Gateway 错误负载创建异常消息，并保留两者供调用方诊断。
     *
     * @param method Gateway RPC 方法名称
     * @param error Gateway 响应中的结构化错误；为空时异常消息仅包含方法名
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
