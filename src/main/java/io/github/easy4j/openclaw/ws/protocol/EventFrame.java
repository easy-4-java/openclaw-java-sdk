package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `EventFrame` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class EventFrame extends GatewayFrame {

    /**
     * 映射 OpenClaw JSON 字段 `event` 的 协议内容。
     */
    private final String event;
    /**
     * 映射 OpenClaw JSON 字段 `payload` 的 协议内容。
     */
    private final Object payload;
    /**
     * 映射 OpenClaw JSON 字段 `seq` 的 协议内容。
     */
    private final Integer seq;

    /**
     * 按协议字段创建 `EventFrame`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param type 写入 `type` 协议字段的内容
     * @param event 写入 `event` 协议字段的内容
     * @param payload 写入 `payload` 协议字段的内容
     * @param seq 写入 `seq` 协议字段的内容
     */
    @JsonCreator
    public EventFrame(
            @JsonProperty("type") String type,
            @JsonProperty("event") String event,
            @JsonProperty("payload") Object payload,
            @JsonProperty("seq") Integer seq) {
        super("event");
        this.event = event;
        this.payload = payload;
        this.seq = seq;
    }
}
