package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.Data;

/**
 * OpenClaw HTTP/Gateway 客户端配置。
 * <p>
 * 涵盖 Gateway 基础地址、Webhook 鉴权、控制面凭证、TLS、HTTP 超时等所有网络相关设置。
 * </p>
 *
 * <p><b>凭证语义（与 OpenClaw Gateway 文档对齐）：</b></p>
 * <ul>
 *     <li>{@link #hooksToken}：<b>仅</b>用于 {@code POST /hooks/*}（Webhooks）鉴权；
 *         文档允许 {@code Authorization: Bearer &lt;hooks.token&gt;} <b>或</b>
 *         {@code x-openclaw-token: &lt;token&gt;}（二选一，由 {@link #hooksUseXOpenclawTokenHeader} 选择），
 *         对应 {@code hooks.token}，<b>不得</b>与 {@code gateway.auth.token} 混用。</li>
 *     <li>{@link #gatewayAuthToken} / {@link #gatewayAuthPassword}：对应控制面凭证（如
 *         {@code gateway.auth.token}、{@code OPENCLAW_GATEWAY_TOKEN} 或密码模式），供 CLI /
 *         WebSocket 控制面 / OpenAI 兼容 API / Tools Invoke 使用。</li>
 * </ul>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/protocol">Gateway Protocol</a>
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#authentication">OpenAI HTTP API Authentication</a>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Data
public class OpenClawHttpClientConfig {

    /**
     * 是否启用 HTTP/Gateway 子系统。
     * <p>为 false 时跳过 HTTP 客户端初始化和检查。</p>
     */
    private boolean enabled = true;

    /**
     * 启动时是否探测 Gateway HTTP 可用性（{@code GET /v1/models}）。
     */
    private boolean startupCheckEnabled = false;

    /**
     * Gateway HTTP 不可用时是否快速失败（中断构造）。
     * <p>默认 false 仅打 WARN；生产环境建议设为 true。</p>
     */
    private boolean failFastOnUnavailable = false;

    /**
     * Gateway HTTP 根地址（Webhooks 与部分 HTTP 面共用主机），例如 {@code http://localhost:18789}。
     */
    private String gatewayBaseUrl = "http://localhost:18789";

    /**
     * 网关控制面共享令牌（如 {@code gateway.auth.token} 或环境变量 {@code OPENCLAW_GATEWAY_TOKEN}）。
     */
    private String gatewayAuthToken;

    /**
     * 网关控制面密码（{@code gateway.auth.password} 模式时）；与 {@link #gatewayAuthToken} 二选一语境。
     */
    private String gatewayAuthPassword;

    /**
     * 是否校验 HTTPS 证书；为 false 时关闭校验（仅建议开发环境）
     */
    private boolean verifySsl = true;

    /** 连接超时（毫秒） */
    private int connectTimeoutMillis = 2_000;

    /** 读取超时（毫秒） */
    private int readTimeoutMillis = 120_000;

    /** 写入超时（毫秒） */
    private int writeTimeoutMillis = 10_000;

    /** 整个调用超时（毫秒）；0 表示不额外限制，由读取超时控制 */
    private int callTimeoutMillis;

    /** 连接池最大空闲连接数 */
    private int maxIdleConnections = 32;

    /** 空闲连接保活时间（毫秒） */
    private long keepAliveDurationMillis = 300_000L;

    /** 异步请求最大并发数 */
    private int maxRequests = 128;

    /** 单主机异步请求最大并发数 */
    private int maxRequestsPerHost = 64;

    /** SSE 响应消费线程池核心线程数 */
    private int sseCorePoolSize = 16;

    /** SSE 响应消费线程池最大线程数 */
    private int sseMaxPoolSize = 16;

    /** SSE 响应消费线程池有界队列容量 */
    private int sseQueueCapacity = 128;

    /** SSE 响应消费线程空闲保活时间（毫秒） */
    private long sseKeepAliveMillis = 60_000L;

    /** 遇到失效连接等传输故障时是否允许 OkHttp 自动恢复 */
    private boolean retryOnConnectionFailure = true;

    /**
     * Gateway HTTP Webhooks 基础路径，对应 {@code hooks.path}，默认 {@code /hooks}。
     */
    private String hooksPath = "/hooks";

    /**
     * Webhook 鉴权令牌，对应 Gateway {@code hooks.token}；
     * 作为 {@code /hooks/*} 请求的 Bearer 时的<b>首选</b>值。
     */
    private String hooksToken;

    /**
     * 为 {@code true} 时使用 {@code x-openclaw-token} 传递 hook 令牌；为 {@code false}（默认）时使用
     * {@code Authorization: Bearer …}。与官方 Gateway Webhook 文档一致，两种头不要同时发送。
     */
    private boolean hooksUseXOpenclawTokenHeader = false;

    /**
     * 解析用于 {@code /hooks/*} HTTP Webhook 请求的 Bearer 令牌。
     *
     * @return {@link #hooksToken} 非空则用之，否则空字符串
     */
    public String resolveHooksBearerToken() {
        if (OpenClawStrings.isNotBlank(hooksToken)) {
            return hooksToken.trim();
        }
        return "";
    }

    /**
     * 解析用于 Gateway <b>控制面</b> HTTP API（{@code /v1/*}、{@code /tools/*}）的 Bearer 令牌。
     * <p>
     * 优先级：{@link #gatewayAuthToken} → {@link #gatewayAuthPassword} 。
     * </p>
     *
     * @return 控制面 Bearer 令牌，均为空则空字符串
     * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#authentication">OpenAI HTTP API Authentication</a>
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
     * 规范化 {@link #hooksPath}，保证以 {@code /} 开头且不以 {@code /} 结尾。
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
