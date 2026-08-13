package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * config.get RPC 结果，返回配置快照、哈希和校验状态。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigGetResult {

    /**
     * JSON 属性 {@code hash}，表示配置或内容哈希。
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * JSON 属性 {@code valid}，表示当前配置是否通过校验。
     */
    @JsonProperty("valid")
    private Boolean valid;

    /**
     * JSON 属性 {@code config}，表示配置快照。
     */
    @JsonProperty("config")
    private JsonNode config;

    /**
     * JSON 属性 {@code uiHints}，表示界面展示提示。
     */
    @JsonProperty("uiHints")
    private JsonNode uiHints;

    /**
     * JSON 属性 {@code path}，表示资源路径。
     */
    @JsonProperty("path")
    private String path;
}
