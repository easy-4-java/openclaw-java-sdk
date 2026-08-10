package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * OpenClaw JSON 协议中的 `GatewayFrame` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
     */
    private final String type;

    /**
     * 按协议字段创建 `GatewayFrame`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param type 写入 `type` 协议字段的内容
     */
    protected GatewayFrame(String type) {
        this.type = type;
    }

    /**
     * 读取当前对象保存的 `type` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getType() {
        return type;
    }
}
