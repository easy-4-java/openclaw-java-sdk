package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Gateway WebSocket 帧基类，通过 type 字段区分请求、响应和事件。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RequestFrame.class, name = "req"),
        @JsonSubTypes.Type(value = ResponseFrame.class, name = "res"),
        @JsonSubTypes.Type(value = EventFrame.class, name = "event"),
})
public abstract class GatewayFrame {

    /**
     * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
     */
    private final String type;

    /**
     * 初始化所有 Gateway 帧共有的 type 判别字段。
     *
     * @param type 协议帧、事件或响应的类型标识
     */
    protected GatewayFrame(String type) {
        this.type = type;
    }

    /**
     * 返回 Gateway 协议帧类型。
     *
     * @return 服务端帧的 {@code type} 协议值
     */
    public String getType() {
        return type;
    }
}
