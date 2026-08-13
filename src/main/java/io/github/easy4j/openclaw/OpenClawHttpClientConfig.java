package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.ws.OpenClawGatewayDeviceIdentity;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HTTP、SSE 与 WebSocket 通道配置，定义 Gateway 地址、认证优先级、连接池、并发、超时、重试和日志开关。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawHttpClientConfig {

    /** HTTP、SSE 与 WebSocket 通道共享的调试配置。 */
    private final OpenClawDebugConfig debug;

    /**
     * 使用默认关闭的调试配置创建 HTTP 配置。
     */
    public OpenClawHttpClientConfig() {
        this(new OpenClawDebugConfig());
    }

    /**
     * 使用客户端级共享调试配置创建 HTTP 配置。
     *
     * @param debug 客户端级调试配置
     */
    public OpenClawHttpClientConfig(OpenClawDebugConfig debug) {
        this.debug = Objects.requireNonNull(debug, "debug");
    }

    /**
     * 同步、流式或自动选择的 HTTP 响应消费模式。
     */
    private HttpResponseMode mode = HttpResponseMode.BLOCKING;

    /**
     * 是否创建并开放对应通信通道；关闭后门面不会初始化该子系统。
     */
    private boolean enabled = true;

    /**
     * 是否在客户端构造阶段执行可用性探测；默认关闭以避免启动阻塞。
     */
    private boolean startupCheckEnabled = false;

    /**
     * 启动探测失败时是否中断客户端构造；关闭时仅记录警告。
     */
    private boolean failFastOnUnavailable = false;

    /**
     * OpenClaw Gateway HTTP 根地址；HTTP 形式会在 WebSocket 通道转换为 ws/wss。
     */
    private String baseUrl = "http://localhost:18789";

    /**
     * Gateway Bearer 认证令牌；解析认证头时优先于密码且日志必须脱敏。
     */
    @ToString.Exclude
    private String gatewayAuthToken;

    /**
     * Gateway 密码认证值；仅在未配置 token 时使用且日志必须脱敏。
     */
    @ToString.Exclude
    private String gatewayAuthPassword;

    /**
     * 设备首次受控接入使用的一次性引导令牌；必须配合 {@link #gatewayDeviceIdentity} 使用。
     */
    @ToString.Exclude
    private String gatewayAuthBootstrapToken;

    /**
     * 已配对设备的认证令牌；必须配合 {@link #gatewayDeviceIdentity} 使用。
     */
    @ToString.Exclude
    private String gatewayAuthDeviceToken;

    /**
     * 本地审批运行时令牌，仅用于受信任的 backend gateway-client 和审批权限范围。
     */
    @ToString.Exclude
    private String gatewayApprovalRuntimeToken;

    /**
     * 本地 Agent Runtime 身份令牌，仅允许受信任的 backend gateway-client 使用。
     */
    @ToString.Exclude
    private String gatewayAgentRuntimeIdentityToken;

    /** Gateway connect 握手角色；OpenClaw 当前支持 {@code operator} 和 {@code node}。 */
    private String gatewayRole = "operator";

    /**
     * WebSocket connect 握手请求的操作权限范围；Gateway 最终仍会按认证身份和服务端策略裁剪。
     */
    private List<String> gatewayScopes = Arrays.asList("operator.read", "operator.write");

    /** Gateway 协议识别的客户端 ID。 */
    private String gatewayClientId = "gateway-client";

    /** Gateway presence 和审计记录中展示的客户端名称。 */
    private String gatewayClientDisplayName = "OpenClaw Java SDK";

    /** Gateway presence 和兼容性判断使用的客户端版本。 */
    private String gatewayClientVersion = "1.0.0";

    /** Gateway 记录和设备签名使用的客户端平台。 */
    private String gatewayClientPlatform = "java";

    /** Gateway 客户端运行模式。 */
    private String gatewayClientMode = "backend";

    /** 可选设备家族，会参与 v3 设备认证签名。 */
    private String gatewayClientDeviceFamily;

    /** 可选设备型号标识，用于客户端 presence 元数据。 */
    private String gatewayClientModelIdentifier;

    /** 可选客户端实例标识，用于区分同一程序的不同运行实例。 */
    private String gatewayClientInstanceId;

    /** 客户端声明的 Gateway 能力列表。 */
    private List<String> gatewayCapabilities = Collections.emptyList();

    /** node 类客户端可由 Gateway 调用的命令列表。 */
    private List<String> gatewayCommands;

    /** 客户端上报的宿主权限快照。 */
    private Map<String, Boolean> gatewayPermissions;

    /** node 类客户端执行命令时使用的 PATH 环境快照。 */
    private String gatewayPathEnv;

    /** connect 握手上报的区域设置；为空时使用 JVM 默认区域。 */
    private String gatewayLocale;

    /** connect 握手上报的 User-Agent；为空时使用 SDK 默认值。 */
    private String gatewayUserAgent;

    /**
     * 可选 Gateway 设备身份签名器；配置后 SDK 会针对每次 challenge 动态生成签名。
     */
    @ToString.Exclude
    private OpenClawGatewayDeviceIdentity gatewayDeviceIdentity;

    /**
     * HTTPS/WSS 是否校验证书和主机名；生产环境应保持开启。
     */
    private boolean verifySsl = true;

    /**
     * 建立 TCP/TLS 连接的最长等待时间，单位为毫秒。
     */
    private int connectTimeoutMillis = 2_000;

    /**
     * 普通 HTTP 响应两次读取之间的最长等待时间，单位为毫秒。
     */
    private int readTimeoutMillis = 120_000;

    /**
     * 写出请求体的最长等待时间，单位为毫秒。
     */
    private int writeTimeoutMillis = 10_000;

    /**
     * 一次完整 HTTP 调用的总超时，单位为毫秒；0 表示由阶段超时控制。
     */
    private int callTimeoutMillis;

    /**
     * 连接池允许保留的最大空闲连接数。
     */
    private int maxIdleConnections = 32;

    /**
     * 空闲连接在连接池中的保留时间，单位为毫秒。
     */
    private long keepAliveDurationMillis = 300_000L;

    /**
     * OkHttp Dispatcher 允许同时执行的最大请求数。
     */
    private int maxRequests = 128;

    /**
     * OkHttp Dispatcher 对单个主机允许的最大并发请求数。
     */
    private int maxRequestsPerHost = 128;

    /**
     * SSE 持续读取执行器常驻工作线程数。
     */
    private int streamCorePoolSize = 16;

    /**
     * SSE 持续读取执行器允许扩展到的最大线程数。
     */
    private int streamMaxPoolSize = 16;

    /**
     * SSE 读取任务有界队列容量；满载时新订阅快速失败，避免内存无界增长。
     */
    private int streamQueueCapacity = 1_024;

    /**
     * SSE 执行器非核心线程的空闲存活时间，单位为毫秒。
     */
    private long streamKeepAliveMillis = 60_000L;

    /**
     * 连接建立失败时是否允许 OkHttp 执行自身安全重试，不包含业务请求重放。
     */
    private boolean retryOnConnectionFailure = true;

    /**
     * Webhook 端点的基础路径，具体 Hook 名称在其后追加。
     */
    private String hooksPath = "/hooks";

    /**
     * Webhook 专用认证令牌；未配置 Gateway token/password 时也可作为回退令牌。
     */
    private String hooksToken;

    /**
     * Webhook 是否通过 x-openclaw-token 请求头传递令牌，而不是 Bearer 头。
     */
    private boolean hooksUseXOpenclawTokenHeader = false;

    /**
     * 根据显式参数和配置默认值解析本次请求使用的 Hooks Bearer Token。
     *
     * @return 去除首尾空白的 Hooks Token；未配置时为空字符串
     */
    public String resolveHooksBearerToken() {
        if (OpenClawStrings.isNotBlank(hooksToken)) {
            return hooksToken.trim();
        }
        return "";
    }

    /**
     * 根据显式参数和配置默认值解析本次请求使用的 Gateway Bearer Token。
     *
     * @return 去除首尾空白的 Gateway Token；未配置时为空字符串
     */
    public String resolveGatewayBearerToken() {
        if (OpenClawStrings.isNotBlank(gatewayAuthToken)) {
            return gatewayAuthToken.trim();
        }
        if (OpenClawStrings.isNotBlank(gatewayAuthPassword)) {
            return gatewayAuthPassword.trim();
        }
        return "";
    }

    /**
     * 根据显式参数和配置默认值解析本次请求使用的 Hooks Path。
     *
     * @return 规范化后的目标地址或路径
     * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
     */
    public String resolveHooksPath() {
        String raw = OpenClawStrings.defaultIfBlank(hooksPath, "/hooks");
        if (!raw.startsWith("/")) {
            raw = "/" + raw;
        }
        while (raw.endsWith("/") && raw.length() > 1) {
            raw = raw.substring(0, raw.length() - 1);
        }
        if ("/".equals(raw)) {
            throw new IllegalArgumentException("hooks.path must be a dedicated subpath (e.g. /hooks); root path '/' is rejected by Gateway");
        }
        return raw;
    }

}
