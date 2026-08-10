package io.github.easy4j.openclaw;

/**
 * HTTP 响应消费模式，区分一次性读取完整响应、持续消费流以及由客户端按端点自动选择。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum HttpResponseMode {

    /**
     * 选择 `blocking` 协议模式；序列化时使用该固定取值。
     */
    BLOCKING,

    /**
     * 选择 `stream` 协议模式；序列化时使用该固定取值。
     */
    STREAM,

    /**
     * 选择 `auto` 协议模式；序列化时使用该固定取值。
     */
    AUTO
}
