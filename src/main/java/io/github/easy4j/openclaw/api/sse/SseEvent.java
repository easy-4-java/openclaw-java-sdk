package io.github.easy4j.openclaw.api.sse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenClaw SDK 的 `SseEvent` 类型，封装其公开契约和生命周期边界。
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
     * `SseEvent` 生命周期内保存的 `event` 对应状态。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("event") private String event;
    /**
     * `SseEvent` 生命周期内保存的 `data` 对应状态。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("data") private String data;
    /**
     * `SseEvent` 生命周期内保存的 `done` 对应状态。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("done") private boolean done;
    /**
     * `SseEvent` 生命周期内保存的 `parsed` 对应状态。
     */
    @com.fasterxml.jackson.annotation.JsonProperty("parsed") private Object parsed;

    /**
     * 判断 `terminal` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isTerminal() { return done; }
    /**
     * 创建表示 SSE 流正常结束的终止事件，不携带业务数据。
     *
     * @return 按当前参数创建、查询或解析得到的 SseEvent
     */
    public static SseEvent terminal() { SseEvent e = new SseEvent(); e.done = true; return e; }
    /**
     * 创建携带原始 data 文本和解析对象的普通 SSE 数据事件。
     *
     * @param data 写入 `data` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 SseEvent
     */
    public static SseEvent data(String data) { SseEvent e = new SseEvent(); e.data = data; return e; }
    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `SseEvent`。
     *
     * @param event 写入 `event` 协议字段的内容
     * @param data 写入 `data` 协议字段的内容
     * @return 按当前参数创建、查询或解析得到的 SseEvent
     */
    public static SseEvent of(String event, String data) { SseEvent e = new SseEvent(); e.event = event; e.data = data; return e; }
}
