package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

/**
 * Gateway WebSocket req 帧，携带请求标识、RPC 方法名和参数映射。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class RequestFrame extends GatewayFrame {

    /**
     * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
     */
    private final String id;
    /**
     * JSON 属性 {@code method}，表示RPC 方法名。
     */
    private final String method;
    /**
     * JSON 属性 {@code params}，表示RPC 参数。
     */
    private final Map<String, Object> params;

    /**
     * 构造 Gateway req 帧并携带方法名和参数映射。
     *
     * @param type 协议帧、事件或响应的类型标识
     * @param id 用于关联协议对象的 {@code id} 标识
     * @param method Gateway RPC 方法名称
     * @param params 随 Gateway RPC 请求发送的参数对象
     */
    @JsonCreator
    public RequestFrame(
            @JsonProperty("type") String type,
            @JsonProperty("id") String id,
            @JsonProperty("method") String method,
            @JsonProperty("params") Map<String, Object> params) {
        super("req");
        this.id = id;
        this.method = method;
        this.params = params;
    }

    /**
     * 构造 Gateway req 帧并携带方法名和参数映射。
     *
     * @param id 用于关联协议对象的 {@code id} 标识
     * @param method Gateway RPC 方法名称
     * @param params 随 Gateway RPC 请求发送的参数对象
     */
    public RequestFrame(String id, String method, Map<String, Object> params) {
        super("req");
        this.id = id;
        this.method = method;
        this.params = params;
    }
}
