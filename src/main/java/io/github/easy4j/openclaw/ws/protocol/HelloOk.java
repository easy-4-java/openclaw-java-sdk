package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * OpenClaw JSON 协议中的 `HelloOk` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloOk {

    /**
     * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
     */
    @JsonProperty("type")
    private String type;

    /**
     * 映射 OpenClaw JSON 字段 `protocol` 的 协议内容。
     */
    @JsonProperty("protocol")
    private int protocol;

    /**
     * 映射 OpenClaw JSON 字段 `server` 的 协议内容。
     */
    @JsonProperty("server")
    private ServerInfo server;

    /**
     * 映射 OpenClaw JSON 字段 `features` 的 协议内容。
     */
    @JsonProperty("features")
    private FeaturesInfo features;

    /**
     * 映射 OpenClaw JSON 字段 `auth` 的 协议内容。
     */
    @JsonProperty("auth")
    private AuthResult auth;

    /**
     * 映射 OpenClaw JSON 字段 `policy` 的 协议内容。
     */
    @JsonProperty("policy")
    private PolicyInfo policy;

    /**
     * OpenClaw JSON 协议中的 `ServerInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServerInfo {
        /**
         * 映射 OpenClaw JSON 字段 `version` 的 协议内容。
         */
        @JsonProperty("version") private String version;
        /**
         * 映射 OpenClaw JSON 字段 `connId` 的 关联标识。
         */
        @JsonProperty("connId") private String connId;
    }

    /**
     * OpenClaw JSON 协议中的 `FeaturesInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeaturesInfo {
        /**
         * 映射 OpenClaw JSON 字段 `methods` 的 有序数组。
         */
        @JsonProperty("methods") private List<String> methods;
        /**
         * 映射 OpenClaw JSON 字段 `events` 的 有序数组。
         */
        @JsonProperty("events") private List<String> events;
    }

    /**
     * OpenClaw JSON 协议中的 `AuthResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthResult {
        /**
         * 映射 OpenClaw JSON 字段 `role` 的 协议内容。
         */
        @JsonProperty("role") private String role;
        /**
         * 映射 OpenClaw JSON 字段 `scopes` 的 有序数组。
         */
        @JsonProperty("scopes") private List<String> scopes;
    }

    /**
     * OpenClaw JSON 协议中的 `PolicyInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyInfo {
        /**
         * 映射 OpenClaw JSON 字段 `maxPayload` 的 协议内容。
         */
        @JsonProperty("maxPayload") private int maxPayload;
        /**
         * 映射 OpenClaw JSON 字段 `maxBufferedBytes` 的 协议内容。
         */
        @JsonProperty("maxBufferedBytes") private int maxBufferedBytes;
        /**
         * 映射 OpenClaw JSON 字段 `tickIntervalMs` 的 协议内容。
         */
        @JsonProperty("tickIntervalMs") private int tickIntervalMs;
    }
}
