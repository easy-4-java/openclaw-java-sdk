package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `ResponseFrame` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class ResponseFrame extends GatewayFrame {

    /**
     * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
     */
    private final String id;
    /**
     * 映射 OpenClaw JSON 字段 `ok` 的 布尔开关。
     */
    private final boolean ok;
    /**
     * 映射 OpenClaw JSON 字段 `payload` 的 协议内容。
     */
    private final Object payload;
    /**
     * 映射 OpenClaw JSON 字段 `error` 的 协议内容。
     */
    private final ErrorShape error;

    /**
     * 按协议字段创建 `ResponseFrame`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param type 写入 `type` 协议字段的内容
     * @param id 用于关联协议对象的 `id` 标识
     * @param ok 写入 `ok` 协议字段的内容
     * @param payload 写入 `payload` 协议字段的内容
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
