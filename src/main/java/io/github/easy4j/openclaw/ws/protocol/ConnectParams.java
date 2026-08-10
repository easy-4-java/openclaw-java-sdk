package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `ConnectParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectParams {

    /**
     * 映射 OpenClaw JSON 字段 `minProtocol` 的 协议内容。
     */
    private final int minProtocol;
    /**
     * 映射 OpenClaw JSON 字段 `maxProtocol` 的 协议内容。
     */
    private final int maxProtocol;
    /**
     * 映射 OpenClaw JSON 字段 `client` 的 协议内容。
     */
    private final ClientInfo client;
    /**
     * 映射 OpenClaw JSON 字段 `auth` 的 协议内容。
     */
    private final AuthInfo auth;
    /**
     * 映射 OpenClaw JSON 字段 `device` 的 协议内容。
     */
    private final DeviceInfo device;
    /**
     * 映射 OpenClaw JSON 字段 `role` 的 协议内容。
     */
    private final String role;

    /**
     * 按协议字段创建 `ConnectParams`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param minProtocol 写入 `minProtocol` 协议字段的内容
     * @param maxProtocol 写入 `maxProtocol` 协议字段的内容
     * @param client 写入 `client` 协议字段的内容
     * @param auth 写入 `auth` 协议字段的内容
     */
    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth) {
        this(minProtocol, maxProtocol, client, auth, null, null);
    }

    /**
     * 按协议字段创建 `ConnectParams`，供 Jackson 序列化、反序列化或调用方读取。
     *
     * @param minProtocol 写入 `minProtocol` 协议字段的内容
     * @param maxProtocol 写入 `maxProtocol` 协议字段的内容
     * @param client 写入 `client` 协议字段的内容
     * @param auth 写入 `auth` 协议字段的内容
     * @param device 写入 `device` 协议字段的内容
     * @param role 写入 `role` 协议字段的内容
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
     * OpenClaw JSON 协议中的 `ClientInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientInfo {
        /**
         * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
         */
        private final String id, displayName, version, platform, mode;

        /**
         * 按协议字段创建 `ClientInfo`，供 Jackson 序列化、反序列化或调用方读取。
         *
         * @param id 用于关联协议对象的 `id` 标识
         * @param displayName 写入 `displayName` 协议字段的内容
         * @param version 写入 `version` 协议字段的内容
         * @param platform 写入 `platform` 协议字段的内容
         * @param mode 写入 `mode` 协议字段的内容
         */
        public ClientInfo(String id, String displayName, String version, String platform, String mode) {
            this.id = id;
            this.displayName = displayName;
            this.version = version;
            this.platform = platform;
            this.mode = mode;
        }
        /**
         * 读取当前对象保存的 `id` 对应状态，不触发网络或子进程调用。
         *
         * @return 可用于关联后续请求的标识
         */
        public String getId() { return id; }
        /**
         * 读取当前对象保存的 `displayName` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getDisplayName() { return displayName; }
        /**
         * 读取当前对象保存的 `version` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getVersion() { return version; }
        /**
         * 读取当前对象保存的 `platform` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getPlatform() { return platform; }
        /**
         * 读取当前对象保存的 `mode` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getMode() { return mode; }
    }

    /**
     * OpenClaw JSON 协议中的 `AuthInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthInfo {
        /**
         * 映射 OpenClaw JSON 字段 `token` 的 协议内容。
         */
        private final String token;
        /**
         * 映射 OpenClaw JSON 字段 `password` 的 协议内容。
         */
        private final String password;

        private AuthInfo(String token, String password) {
            this.token = token;
            this.password = password;
        }

        /**
         * 根据参数构造或读取 `AuthInfo` 的 `token` 协议字段。
         *
         * @param token 认证令牌；日志中必须脱敏
         * @return 按方法参数填充的 AuthInfo
         */
        public static AuthInfo token(String token) { return new AuthInfo(token, null); }
        /**
         * 根据参数构造或读取 `AuthInfo` 的 `password` 协议字段。
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
     * OpenClaw JSON 协议中的 `DeviceInfo` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class DeviceInfo {
        /**
         * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
         */
        private final String id;
        /**
         * 映射 OpenClaw JSON 字段 `publicKey` 的 协议内容。
         */
        private final String publicKey;
        /**
         * 映射 OpenClaw JSON 字段 `signature` 的 协议内容。
         */
        private final String signature;
        /**
         * 映射 OpenClaw JSON 字段 `signedAt` 的 协议内容。
         */
        private final Long signedAt;
        /**
         * 映射 OpenClaw JSON 字段 `nonce` 的 协议内容。
         */
        private final String nonce;

        /**
         * 按协议字段创建 `DeviceInfo`，供 Jackson 序列化、反序列化或调用方读取。
         *
         * @param id 用于关联协议对象的 `id` 标识
         * @param publicKey 写入 `publicKey` 协议字段的内容
         * @param signature 写入 `signature` 协议字段的内容
         * @param signedAt 写入 `signedAt` 协议字段的内容
         * @param nonce 写入 `nonce` 协议字段的内容
         */
        public DeviceInfo(String id, String publicKey, String signature, Long signedAt, String nonce) {
            this.id = id;
            this.publicKey = publicKey;
            this.signature = signature;
            this.signedAt = signedAt;
            this.nonce = nonce;
        }
    }
