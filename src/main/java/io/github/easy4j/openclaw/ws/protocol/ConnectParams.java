package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gateway connect 握手请求，声明协议范围、客户端身份和认证信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectParams {

    /**
     * JSON 属性 {@code minProtocol}，表示最低协议版本。
     */
    private final int minProtocol;
    /**
     * JSON 属性 {@code maxProtocol}，表示最高协议版本。
     */
    private final int maxProtocol;
    /**
     * JSON 属性 {@code client}，表示客户端身份信息。
     */
    private final ClientInfo client;
    /**
     * JSON 属性 {@code auth}，表示Gateway 认证信息。
     */
    private final AuthInfo auth;
    /**
     * JSON 属性 {@code device}，表示设备身份信息。
     */
    private final DeviceInfo device;
    /**
     * JSON 属性 {@code role}，表示聊天消息角色。
     */
    private final String role;

    /**
     * 构造 Gateway 握手参数，声明协议范围、客户端身份、认证与可选设备签名。
     *
     * @param minProtocol 客户端支持的最低 Gateway 协议版本
     * @param maxProtocol 客户端支持的最高 Gateway 协议版本
     * @param client 参与 Gateway 握手的客户端身份、版本与平台信息
     * @param auth 握手或请求使用的认证参数
     */
    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth) {
        this(minProtocol, maxProtocol, client, auth, null, null);
    }

    /**
     * 构造 Gateway 握手参数，声明协议范围、客户端身份、认证与可选设备签名。
     *
     * @param minProtocol 客户端支持的最低 Gateway 协议版本
     * @param maxProtocol 客户端支持的最高 Gateway 协议版本
     * @param client 参与 Gateway 握手的客户端身份、版本与平台信息
     * @param auth 握手或请求使用的认证参数
     * @param device 发起握手的设备身份信息
     * @param role Gateway 握手角色；未指定时可为 {@code null}
     */
    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth,
                         DeviceInfo device, String role) {
        this.minProtocol = minProtocol;
        this.maxProtocol = maxProtocol;
        this.client = client;
        this.auth = auth;
        this.device = device;
        this.role = role;
    }

    /**
     * 握手中的客户端标识、显示名、版本、平台和运行模式。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientInfo {
        /**
         * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
         */
        private final String id, displayName, version, platform, mode;

        /**
         * 构造参与握手的客户端身份、版本、平台和运行模式。
         *
         * @param id 用于关联协议对象的 {@code id} 标识
         * @param displayName 面向用户展示的客户端名称
         * @param version 客户端或协议版本字符串
         * @param platform 客户端运行平台标识
         * @param mode 客户端参与 Gateway 握手时声明的运行模式
         */
        public ClientInfo(String id, String displayName, String version, String platform, String mode) {
            this.id = id;
            this.displayName = displayName;
            this.version = version;
            this.platform = platform;
            this.mode = mode;
        }
        /**
         * 返回客户端或协议对象标识。
         *
         * @return 设备身份标识
         */
        public String getId() { return id; }
        /**
         * 返回面向用户展示的客户端名称。
         *
         * @return 握手时上报的客户端显示名称
         */
        public String getDisplayName() { return displayName; }
        /**
         * 返回客户端或协议版本。
         *
         * @return 握手时上报的 SDK 版本
         */
        public String getVersion() { return version; }
        /**
         * 返回客户端运行平台标识。
         *
         * @return 握手时上报的运行平台
         */
        public String getPlatform() { return platform; }
        /**
         * 返回连接或执行模式。
         *
         * @return 握手时声明的客户端模式
         */
        public String getMode() { return mode; }
    }

    /**
     * Gateway 握手认证信息，支持 Token 或密码二选一。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthInfo {
        /**
         * JSON 属性 {@code token}，表示Bearer Token。
         */
        private final String token;
        /**
         * JSON 属性 {@code password}，表示Gateway 认证密码。
         */
        private final String password;

        private AuthInfo(String token, String password) {
            this.token = token;
            this.password = password;
        }

        /**
         * 创建仅使用 Bearer Token 的 Gateway 认证参数。
         *
         * @param token 认证令牌；日志中必须脱敏
         * @return 按方法参数填充的 AuthInfo
         */
        public static AuthInfo token(String token) { return new AuthInfo(token, null); }
        /**
         * 创建仅使用密码的 Gateway 认证参数。
         *
         * @param password 认证密码；日志中必须脱敏
         * @return 按方法参数填充的 AuthInfo
         */
        public static AuthInfo password(String password) { return new AuthInfo(null, password); }
    }

    /**
     * 把当前协议对象编码为 Gateway WebSocket RPC 接受的键值参数。
     *
     * @return 键名与 OpenClaw JSON/CLI 协议一致的映射
     */
    public Map<String, Object> toParamsMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("minProtocol", minProtocol);
        m.put("maxProtocol", maxProtocol);
        Map<String, Object> clientMap = new LinkedHashMap<>();
        clientMap.put("id", client.getId());
        clientMap.put("version", client.getVersion());
        clientMap.put("platform", client.getPlatform());
        clientMap.put("mode", client.getMode());
        if (client.getDisplayName() != null) {
            clientMap.put("displayName", client.getDisplayName());
        }
        m.put("client", clientMap);
        if (auth != null) {
            Map<String, Object> authMap = new LinkedHashMap<>();
            if (auth.getToken() != null) authMap.put("token", auth.getToken());
            if (auth.getPassword() != null) authMap.put("password", auth.getPassword());
            m.put("auth", authMap);
        }
        return m;
    }
}
    /**
     * Gateway 挑战响应使用的设备公钥、签名、时间戳和 nonce。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class DeviceInfo {
        /**
         * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
         */
        private final String id;
        /**
         * JSON 属性 {@code publicKey}，表示设备签名公钥。
         */
        private final String publicKey;
        /**
         * JSON 属性 {@code signature}，表示设备挑战签名。
         */
        private final String signature;
        /**
         * JSON 属性 {@code signedAt}，表示设备签名时间戳。
         */
        private final Long signedAt;
        /**
         * JSON 属性 {@code nonce}，表示握手挑战随机值。
         */
        private final String nonce;

        /**
         * 构造设备签名信息，用于 Gateway 校验挑战响应。
         *
         * @param id 用于关联协议对象的 {@code id} 标识
         * @param publicKey 设备签名使用的公钥
         * @param signature 对握手载荷生成的签名
         * @param signedAt 参与设备签名校验的时间戳；单位遵循 Gateway 协议
         * @param nonce 服务端挑战提供的一次性随机值
         */
        public DeviceInfo(String id, String publicKey, String signature, Long signedAt, String nonce) {
            this.id = id;
            this.publicKey = publicKey;
            this.signature = signature;
            this.signedAt = signedAt;
            this.nonce = nonce;
        }
    }
