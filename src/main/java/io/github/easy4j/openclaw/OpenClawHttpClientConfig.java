package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import lombok.Data;

/**
 * OpenClaw HTTP/Gateway client configuration.
 * <p>
 * Covers Gateway base URL,Webhook authentication,control planecredentials,TLS,HTTP timeout.
 * </p>
 *
 * <p><b>credentials( OpenClaw Gateway documentationaligned):</b></p>
 * <ul>
 * <li>{@link #hooksToken}:<b>only</b>Used for {@code POST /hooks/*}(Webhooks)authentication;
 * documentation {@code Authorization: Bearer &lt;hooks.token&gt;} <b></b>
 * {@code x-openclaw-token: &lt;token&gt;}(mutually exclusive, {@link #hooksUseXOpenclawTokenHeader} ),
 * Corresponds to {@code hooks.token},<b></b> {@code gateway.auth.token} .</li>
 * <li>{@link #gatewayAuthToken} / {@link #gatewayAuthPassword}:Corresponds tocontrol planecredentials(
 * {@code gateway.auth.token},{@code OPENCLAW_GATEWAY_TOKEN} ), CLI /
 * WebSocket control plane / OpenAI API / Tools Invoke .</li>
 * </ul>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/protocol">Gateway Protocol</a>
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#authentication">OpenAI HTTP API Authentication</a>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawHttpClientConfig {

    /** 对话响应模式，默认保持兼容的完整响应模式。 */
    private HttpResponseMode mode = HttpResponseMode.BLOCKING;

    /**
 * Whether to enable HTTP/Gateway system.
 * <p>When false,skips HTTP .</p>
     */
    private boolean enabled = true;

    /**
 * At startupProbes the Gateway HTTP ({@code GET /v1/models}).
     */
    private boolean startupCheckEnabled = false;

    /**
 * Gateway HTTP Whether to fail fast(interrupts construction).
 * <p>Defaults to false only logs a warning;productionwhen true.</p>
     */
    private boolean failFastOnUnavailable = false;

    /**
 * Gateway HTTP root URL(Webhooks HTTP ), {@code http://localhost:18789}.
     */
    private String baseUrl = "http://localhost:18789";

    /**
 * Gateway control planetoken( {@code gateway.auth.token} {@code OPENCLAW_GATEWAY_TOKEN}).
     */
    private String gatewayAuthToken;

    /**
 * Gateway control plane({@code gateway.auth.password} ); {@link #gatewayAuthToken} mutually exclusive context.
     */
    private String gatewayAuthPassword;

    /**
 * Whether to verify HTTPS ;When false,(only recommended for)
     */
    private boolean verifySsl = true;

 /** connectiontimeout(milliseconds) */
    private int connectTimeoutMillis = 2_000;

 /** timeout(milliseconds) */
    private int readTimeoutMillis = 120_000;

 /** timeout(milliseconds) */
    private int writeTimeoutMillis = 10_000;

 /** timeout(milliseconds);0 ,timeout */
    private int callTimeoutMillis;

 /** connection poolmaximum idle connections */
    private int maxIdleConnections = 32;

 /** idleconnectionkeep-alive(milliseconds) */
    private long keepAliveDurationMillis = 300_000L;

 /** maximum concurrency */
    private int maxRequests = 128;

 /** maximum concurrency */
    private int maxRequestsPerHost = 128;

    /** 流式响应消费线程池核心线程数。 */
    private int streamCorePoolSize = 16;

    /** SSE 响应消费线程池最大线程数 */
    private int streamMaxPoolSize = 16;

    /** SSE 响应消费线程池有界队列容量 */
    private int streamQueueCapacity = 1_024;

    /** SSE 响应消费线程空闲保活时间（毫秒） */
    private long streamKeepAliveMillis = 60_000L;

    /** 遇到失效连接等传输故障时是否允许 OkHttp 自动恢复 */
    private boolean retryOnConnectionFailure = true;

    /**
     * 是否输出请求头、请求体及响应体等详细诊断信息。
     * <p>默认关闭，避免业务内容或凭证进入日志；基础请求追踪仍以 DEBUG 级别输出。</p>
     */
    private boolean detailedLoggingEnabled = false;

    /** 详细日志中请求体、响应体的最大字符数。 */
    private int maxLoggedBodyLength = 2_000;

    /**
 * Gateway HTTP Webhooks base path,Corresponds to {@code hooks.path}, {@code /hooks}.
     */
    private String hooksPath = "/hooks";

    /**
 * Webhook authentication token,Corresponds to Gateway {@code hooks.token};
 * {@code /hooks/*} Bearer <b>preferred</b>value.
     */
    private String hooksToken;

    /**
 * {@code true} {@code x-openclaw-token} hook token; {@code false}
 * {@code Authorization: Bearer …}.consistent with official Gateway Webhook documentation,.
     */
    private boolean hooksUseXOpenclawTokenHeader = false;

    /**
 * Resolves the {@code /hooks/*} HTTP Webhook Bearer token.
     *
 * @return {@link #hooksToken} ,characters
     */
    public String resolveHooksBearerToken() {
        if (OpenClawStrings.isNotBlank(hooksToken)) {
            return hooksToken.trim();
        }
        return "";
    }

    /**
 * Resolves the Gateway <b>control plane</b> HTTP API({@code /v1/*},{@code /tools/*}) Bearer token.
     * <p>
 * Priority:{@link #gatewayAuthToken} → {@link #gatewayAuthPassword} .
     * </p>
     *
 * @return control plane Bearer token,characters
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
 * Normalizes {@link #hooksPath}, {@code /} {@code /} .
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
