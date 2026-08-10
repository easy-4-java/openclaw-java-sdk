package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `RequestFrame` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class RequestFrame extends GatewayFrame {

    /**
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    private final String id;
    /**
     * 映射 OpenClaw JSON 字段 `method` 的 协议内容。
     */
    private final String method;
    /**
     * 映射 OpenClaw JSON 字段 `params` 的 键值对象。
     */
    private final Map<String, Object> params;

    /**
     * 按协议字段创建 `RequestFrame`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param type 写入 `type` 协议字段的内容
     * @param id 用于关联协议对象的 `id` 标识
     * @param method 写入 `method` 协议字段的内容
     * @param params 写入 `params` 协议字段的内容
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
     * 按协议字段创建 `RequestFrame`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param id 用于关联协议对象的 `id` 标识
     * @param method 写入 `method` 协议字段的内容
     * @param params 写入 `params` 协议字段的内容
     */
    public RequestFrame(String id, String method, Map<String, Object> params) {
        super("req");
        this.id = id;
        this.method = method;
        this.params = params;
    }
}
