package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * OpenClaw JSON 协议中的 `ConfigGetResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigGetResult {

    /**
     * 映射 OpenClaw JSON 字段 `hash` 的 协议内容。
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * 映射 OpenClaw JSON 字段 `valid` 的 布尔开关。
     */
    @JsonProperty("valid")
    private Boolean valid;

    /**
     * 映射 OpenClaw JSON 字段 `config` 的 协议内容。
     */
    @JsonProperty("config")
    private JsonNode config;

    /**
     * 映射 OpenClaw JSON 字段 `uiHints` 的 协议内容。
     */
    @JsonProperty("uiHints")
    private JsonNode uiHints;

    /**
     * 映射 OpenClaw JSON 字段 `path` 的 协议内容。
     */
    @JsonProperty("path")
    private String path;
}
