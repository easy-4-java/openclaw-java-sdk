package io.github.easy4j.openclaw.api.sse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SSE event/data 字段、结束标记及可选解析对象的传输模型。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SseEvent {
    /**
     * SSE event 字段；为空时表示默认消息事件。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("event") private String event;
    /**
     * SSE data 字段拼接后的原始文本。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("data") private String data;
    /**
     * 是否收到流结束标记。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("done") private boolean done;
    /**
     * 按目标响应类型反序列化得到的事件对象；解析前可为空。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("parsed") private Object parsed;

    /**
     * 判断该 SSE 事件是否为流结束标记。
     *
     * @return 当前事件是流终止标记时返回 {@code true}
     */
    public boolean isTerminal() { return done; }
    /**
     * 创建表示 SSE 流正常结束的终止事件，不携带业务数据。
     *
     * @return 仅设置结束标记的 SSE 事件
     */
    public static SseEvent terminal() { SseEvent e = new SseEvent(); e.done = true; return e; }
    /**
     * 创建携带原始 data 文本和解析对象的普通 SSE 数据事件。
     *
     * @param data SSE 事件或协议帧携带的原始数据
     * @return 仅携带 data 字段的 SSE 事件
     */
    public static SseEvent data(String data) { SseEvent e = new SseEvent(); e.data = data; return e; }
    /**
     * 创建同时携带事件名称和原始数据文本的 SSE 事件。
     *
     * @param event SSE 的 {@code event} 字段；未指定时可为 {@code null}
     * @param data SSE 的原始 {@code data} 文本；未指定时可为 {@code null}
     * @return 携带指定事件名称和原始数据的非终止事件
     */
    public static SseEvent of(String event, String data) { SseEvent e = new SseEvent(); e.event = event; e.data = data; return e; }
}
