package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
     * JSON 属性 {@code role}，表示 Gateway 客户端角色；当前支持 {@code operator} 和 {@code node}。
     */
    private final String role;
    /** 客户端能力列表。 */
    private final List<String> caps;
    /** 客户端支持的命令列表。 */
    private final List<String> commands;
    /** 客户端权限快照。 */
    private final Map<String, Boolean> permissions;
    /** 客户端进程 PATH。 */
    private final String pathEnv;
    /** 请求的操作权限范围。 */
    private final List<String> scopes;
    /** 客户端区域设置。 */
    private final String locale;
    /** 客户端 User-Agent。 */
    private final String userAgent;

    /**
     * 构造 Gateway 握手参数，声明协议范围、客户端身份、认证与可选设备签名。
     *
     * @param minProtocol 客户端支持的最低 Gateway 协议版本
     * @param maxProtocol 客户端支持的最高 Gateway 协议版本
     * @param client 参与 Gateway 握手的客户端身份、版本与平台信息
     * @param auth 握手或请求使用的认证参数
     */
    public ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth) {
        this(minProtocol, maxProtocol, client, auth, null, null,
                null, null, null, null, null, null, null);
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
        this(minProtocol, maxProtocol, client, auth, device, role,
                null, null, null, null, null, null, null);
    }

    @Builder
    private ConnectParams(int minProtocol, int maxProtocol, ClientInfo client, AuthInfo auth,
                          DeviceInfo device, String role, List<String> caps, List<String> commands,
                          Map<String, Boolean> permissions, String pathEnv, List<String> scopes,
                          String locale, String userAgent) {
        this.minProtocol = minProtocol;
        this.maxProtocol = maxProtocol;
        this.client = client;
        this.auth = auth;
        this.device = device;
        this.role = role;
        this.caps = caps;
        this.commands = commands;
        this.permissions = permissions;
        this.pathEnv = pathEnv;
        this.scopes = scopes;
        this.locale = locale;
        this.userAgent = userAgent;
    }

    /**
     * 握手中的客户端标识、显示名、版本、平台和运行模式。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientInfo {
        /**
         * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
         */
        private final String id, displayName, version, platform, mode;
        /** 可选设备家族。 */
        private final String deviceFamily;
        /** 可选设备型号标识。 */
        private final String modelIdentifier;
        /** 可选客户端实例标识。 */
        private final String instanceId;

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
            this(id, displayName, version, platform, mode, null, null, null);
        }

        private ClientInfo(String id, String displayName, String version, String platform, String mode,
                           String deviceFamily, String modelIdentifier, String instanceId) {
            this.id = id;
            this.displayName = displayName;
            this.version = version;
            this.platform = platform;
            this.mode = mode;
            this.deviceFamily = deviceFamily;
            this.modelIdentifier = modelIdentifier;
            this.instanceId = instanceId;
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
     * Gateway 握手认证信息，支持共享 Token、密码、设备令牌、引导令牌和本地运行时令牌。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Builder
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
        /** 一次性引导令牌。 */
        private final String bootstrapToken;
        /** 已配对设备令牌。 */
        private final String deviceToken;
        /** 审批运行时令牌。 */
        private final String approvalRuntimeToken;
        /** Agent Runtime 身份令牌。 */
        private final String agentRuntimeIdentityToken;

        private AuthInfo(String token, String password, String bootstrapToken, String deviceToken,
                         String approvalRuntimeToken, String agentRuntimeIdentityToken) {
            this.token = token;
            this.password = password;
            this.bootstrapToken = bootstrapToken;
            this.deviceToken = deviceToken;
            this.approvalRuntimeToken = approvalRuntimeToken;
            this.agentRuntimeIdentityToken = agentRuntimeIdentityToken;
        }

        /**
         * 创建仅使用 Bearer Token 的 Gateway 认证参数。
         *
         * @param token 认证令牌；日志中必须脱敏
         * @return 按方法参数填充的 AuthInfo
         */
        public static AuthInfo token(String token) { return new AuthInfo(token, null, null, null, null, null); }
        /**
         * 创建仅使用密码的 Gateway 认证参数。
         *
         * @param password 认证密码；日志中必须脱敏
         * @return 按方法参数填充的 AuthInfo
         */
        public static AuthInfo password(String password) { return new AuthInfo(null, password, null, null, null, null); }
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
        if (Objects.nonNull(client.getDisplayName())) {
            clientMap.put("displayName", client.getDisplayName());
        }
        if (Objects.nonNull(client.getDeviceFamily())) clientMap.put("deviceFamily", client.getDeviceFamily());
        if (Objects.nonNull(client.getModelIdentifier())) clientMap.put("modelIdentifier", client.getModelIdentifier());
        if (Objects.nonNull(client.getInstanceId())) clientMap.put("instanceId", client.getInstanceId());
        m.put("client", clientMap);
        if (Objects.nonNull(caps)) m.put("caps", caps);
        if (Objects.nonNull(commands)) m.put("commands", commands);
        if (Objects.nonNull(permissions)) m.put("permissions", permissions);
        if (Objects.nonNull(pathEnv)) m.put("pathEnv", pathEnv);
        if (Objects.nonNull(role)) m.put("role", role);
        if (Objects.nonNull(scopes)) m.put("scopes", scopes);
        if (Objects.nonNull(device)) {
            Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
            deviceMap.put("id", device.getId());
            deviceMap.put("publicKey", device.getPublicKey());
            deviceMap.put("signature", device.getSignature());
            deviceMap.put("signedAt", device.getSignedAt());
            deviceMap.put("nonce", device.getNonce());
            m.put("device", deviceMap);
        }
        if (Objects.nonNull(auth)) {
            Map<String, Object> authMap = new LinkedHashMap<>();
            if (Objects.nonNull(auth.getToken())) authMap.put("token", auth.getToken());
            if (Objects.nonNull(auth.getPassword())) authMap.put("password", auth.getPassword());
            if (Objects.nonNull(auth.getBootstrapToken())) authMap.put("bootstrapToken", auth.getBootstrapToken());
            if (Objects.nonNull(auth.getDeviceToken())) authMap.put("deviceToken", auth.getDeviceToken());
            if (Objects.nonNull(auth.getApprovalRuntimeToken())) authMap.put("approvalRuntimeToken", auth.getApprovalRuntimeToken());
            if (Objects.nonNull(auth.getAgentRuntimeIdentityToken())) authMap.put("agentRuntimeIdentityToken", auth.getAgentRuntimeIdentityToken());
            m.put("auth", authMap);
        }
        if (Objects.nonNull(locale)) m.put("locale", locale);
        if (Objects.nonNull(userAgent)) m.put("userAgent", userAgent);
        return m;
    }

    /**
     * Gateway 挑战响应使用的设备公钥、签名、时间戳和 nonce。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DeviceInfo {
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
}
