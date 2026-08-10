package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `GatewaySessionsDefaults` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionsDefaults {

    /**
     * 映射 OpenClaw JSON 字段 `modelProvider` 的 关联标识。
     */
    @JsonProperty("modelProvider")
    private String modelProvider;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    @JsonProperty("model")
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `contextTokens` 的 协议内容。
     */
    @JsonProperty("contextTokens")
    private Integer contextTokens;

    /**
     * 映射 OpenClaw JSON 字段 `thinkingDefault` 的 协议内容。
     */
    @JsonProperty("thinkingDefault")
    private String thinkingDefault;
}
