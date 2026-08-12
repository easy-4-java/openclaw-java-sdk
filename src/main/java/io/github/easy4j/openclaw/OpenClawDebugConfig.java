package io.github.easy4j.openclaw;

import lombok.Data;

/**
 * OpenClaw SDK 统一调试配置，用于控制生命周期、请求头和正文日志。
 *
 * <p>调试默认关闭。正文日志始终受长度限制，认证头和敏感令牌仍由客户端脱敏。</p>
 *
 * <p>{@link #level} 字段使用字符串表示日志级别。可选值：
 * <ul>
 *     <li>{@code "NONE"} — 不输出</li>
 *     <li>{@code "BASIC"} — 仅方法/路径/状态/耗时</li>
 *     <li>{@code "HEADERS"} — BASIC + 请求/响应头</li>
 *     <li>{@code "BODY"} — HEADERS + 请求/响应正文（受 {@link #maxContentLength} 限制）</li>
 * </ul>
 * 当 classpath 上有 {@code io.github.easy4j:okhttp3-extension} 时，{@link #level} 会按相同语义传递给底层日志拦截器；否则仅用于 SDK 自身 DEBUG 输出。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawDebugConfig {

    /** 是否允许 SDK 输出调试诊断信息。 */
    private boolean enabled;

    /**
     * 启用调试后的详细程度；字符串常量，对齐 {@code okhttp3.extension.logging.HttpLogLevel}。
     */
    private String level = "BASIC";

    /** BODY 级别单项正文允许记录的最大字符数。 */
    private int maxContentLength = 2_000;

    /**
     * 判断指定级别的日志是否允许输出。
     * <p>简单的字符串排序：{@code NONE &lt; BASIC &lt; HEADERS &lt; BODY}。</p>
     *
     * @param required 待输出信息要求的最低级别（字符串）
     * @return 调试已启用且当前级别满足要求时返回 {@code true}
     */
    public boolean allows(String required) {
        if (!enabled || level == null || required == null) {
            return false;
        }
        return rank(level) >= rank(required);
    }

    /**
     * 返回经过下限保护的正文日志长度。
     *
     * @return 至少为 1 的最大正文字符数
     */
    public int resolveMaxContentLength() {
        return Math.max(1, maxContentLength);
    }

    private static int rank(String level) {
        switch (level.toUpperCase()) {
            case "BODY":    return 3;
            case "HEADERS": return 2;
            case "BASIC":   return 1;
            case "NONE":    return 0;
            default:        return 1;
        }
    }
}