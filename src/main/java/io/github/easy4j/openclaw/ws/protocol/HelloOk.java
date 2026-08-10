package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Gateway connect 握手成功结果，包含协议版本、服务端能力、认证和策略。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloOk {

    /**
     * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
     */
    @JsonProperty("type")
    private String type;

    /**
     * JSON 属性 {@code protocol}，表示协议版本。
     */
    @JsonProperty("protocol")
    private int protocol;

    /**
     * JSON 属性 {@code server}，表示服务端信息。
     */
    @JsonProperty("server")
    private ServerInfo server;

    /**
     * JSON 属性 {@code features}，表示服务端能力集合。
     */
    @JsonProperty("features")
    private FeaturesInfo features;

    /**
     * JSON 属性 {@code auth}，表示Gateway 认证信息。
     */
    @JsonProperty("auth")
    private AuthResult auth;

    /**
     * JSON 属性 {@code policy}，表示审批或执行策略。
     */
    @JsonProperty("policy")
    private PolicyInfo policy;

    /**
     * Gateway 服务端版本、平台及连接标识。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServerInfo {
        /**
         * JSON 属性 {@code version}，表示版本标识。
         */
        @JsonProperty("version") private String version;
        /**
         * JSON 属性 {@code connId}，表示WebSocket 连接标识。
         */
        @JsonProperty("connId") private String connId;
    }

    /**
     * Gateway 声明支持的 RPC 方法、事件和权限作用域。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeaturesInfo {
        /**
         * JSON 属性 {@code methods}，表示服务端支持的 RPC 方法。
         */
        @JsonProperty("methods") private List<String> methods;
        /**
         * JSON 属性 {@code events}，表示服务端支持的事件名称。
         */
        @JsonProperty("events") private List<String> events;
    }

    /**
     * Gateway 握手认证结果及授权信息。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthResult {
        /**
         * JSON 属性 {@code role}，表示聊天消息角色。
         */
        @JsonProperty("role") private String role;
        /**
         * JSON 属性 {@code scopes}，表示服务端授予的权限作用域。
         */
        @JsonProperty("scopes") private List<String> scopes;
    }

    /**
     * Gateway 返回的连接或工具执行策略。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyInfo {
        /**
         * JSON 属性 {@code maxPayload}，表示最大负载大小。
         */
        @JsonProperty("maxPayload") private int maxPayload;
        /**
         * JSON 属性 {@code maxBufferedBytes}，表示最大缓冲字节数。
         */
        @JsonProperty("maxBufferedBytes") private int maxBufferedBytes;
        /**
         * JSON 属性 {@code tickIntervalMs}，表示轮询间隔，单位为毫秒。
         */
        @JsonProperty("tickIntervalMs") private int tickIntervalMs;
    }
}
