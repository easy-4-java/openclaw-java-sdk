package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Gateway WebSocket res 帧，关联请求标识并携带成功负载或错误。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class ResponseFrame extends GatewayFrame {

    /**
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    private final String id;
    /**
     * JSON 属性 {@code ok}，表示请求是否成功。
     */
    private final boolean ok;
    /**
     * JSON 属性 {@code payload}，表示事件或响应负载。
     */
    private final Object payload;
    /**
     * JSON 属性 {@code error}，表示错误详情。
     */
    private final ErrorShape error;

    /**
     * 从 Gateway res 帧恢复关联标识、成功负载或错误信息。
     *
     * @param type 协议帧、事件或响应的类型标识
     * @param id 用于关联协议对象的 {@code id} 标识
     * @param ok RPC 或探测是否成功
     * @param payload RPC 成功响应携带的数据；失败或无数据时可为 {@code null}
     * @param error 导致调用失败的异常
     */
    @JsonCreator
    public ResponseFrame(
            @JsonProperty("type") String type,
            @JsonProperty("id") String id,
            @JsonProperty("ok") boolean ok,
            @JsonProperty("payload") Object payload,
            @JsonProperty("error") ErrorShape error) {
        super("res");
        this.id = id;
        this.ok = ok;
        this.payload = payload;
        this.error = error;
    }
}
