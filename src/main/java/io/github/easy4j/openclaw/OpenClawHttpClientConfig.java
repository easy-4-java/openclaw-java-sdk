package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.Data;

/**
 * HTTP、SSE 与 WebSocket 通道配置，定义 Gateway 地址、认证优先级、连接池、并发、超时、重试和日志开关。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawHttpClientConfig {

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
    private String gatewayAuthToken;

    /**
     * Gateway 密码认证值；仅在未配置 token 时使用且日志必须脱敏。
     */
    private String gatewayAuthPassword;

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
     * 是否输出脱敏后的请求头和截断响应体；默认关闭以控制日志量和泄露风险。
     */
    private boolean detailedLoggingEnabled = false;

    /**
     * 详细日志中响应体允许记录的最大字符数，超出部分截断。
     */
    private int maxLoggedBodyLength = 2_000;

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
     * @return 服务返回或流式累积得到的文本
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
     * @return 服务返回或流式累积得到的文本
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
