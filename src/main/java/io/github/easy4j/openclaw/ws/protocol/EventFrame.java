package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Gateway WebSocket event 帧，携带事件名、负载和可选序列号。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class EventFrame extends GatewayFrame {

    /**
     * JSON 属性 {@code event}，表示事件名称。
     */
    private final String event;
    /**
     * JSON 属性 {@code payload}，表示事件或响应负载。
     */
    private final Object payload;
    /**
     * JSON 属性 {@code seq}，表示事件序列号。
     */
    private final Integer seq;

    /**
     * 从 Gateway event 帧恢复事件名称、负载和可选序列号。
     *
     * @param type 协议帧、事件或响应的类型标识
     * @param event 待分发或累积的 SSE/WebSocket 事件
     * @param payload 事件携带的结构化业务数据；无负载时可为 {@code null}
     * @param seq 可空的事件序列号，用于检测顺序或缺帧
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
