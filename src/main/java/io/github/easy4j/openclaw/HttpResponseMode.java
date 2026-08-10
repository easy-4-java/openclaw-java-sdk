package io.github.easy4j.openclaw;

/**
 * HTTP 响应消费模式，区分一次性读取完整响应、持续消费流以及由客户端按端点自动选择。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum HttpResponseMode {

    /**
     * 表示HTTP 响应交付方式的 {@code blocking} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    BLOCKING,

    /**
     * 表示HTTP 响应交付方式的 {@code stream} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    STREAM,

    /**
     * 表示HTTP 响应交付方式的 {@code auto} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    AUTO
}
