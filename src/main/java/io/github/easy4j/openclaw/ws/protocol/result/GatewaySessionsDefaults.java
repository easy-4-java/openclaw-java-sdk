package io.github.easy4j.openclaw.ws.protocol.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * sessions.list 返回的默认模型和思考等级。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewaySessionsDefaults {

    /**
     * JSON 属性 {@code modelProvider}，表示模型提供方标识。
     */
    @JsonProperty("modelProvider")
    private String modelProvider;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    @JsonProperty("model")
    private String model;

    /**
     * JSON 属性 {@code contextTokens}，表示上下文 Token 容量。
     */
    @JsonProperty("contextTokens")
    private Integer contextTokens;

    /**
     * JSON 属性 {@code thinkingDefault}，表示默认思考强度。
     */
    @JsonProperty("thinkingDefault")
    private String thinkingDefault;
}
