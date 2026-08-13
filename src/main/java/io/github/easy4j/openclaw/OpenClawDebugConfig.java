package io.github.easy4j.openclaw;

import lombok.Data;
import okhttp3.extension.logging.HttpLogLevel;

/**
 * OpenClaw SDK 统一调试配置，用于控制生命周期、请求头和正文日志。
 *
 * <p>调试默认关闭。正文日志始终受长度限制，认证头和敏感令牌仍由客户端脱敏。</p>
 *
 * <p>{@link #level} 使用 {@link HttpLogLevel} 表示日志级别。可选值：
 * <ul>
 *     <li>{@code "NONE"} — 不输出</li>
 *     <li>{@code "BASIC"} — 仅方法/路径/状态/耗时</li>
 *     <li>{@code "HEADERS"} — BASIC + 请求/响应头</li>
 *     <li>{@code "BODY"} — HEADERS + 请求/响应正文（受 {@link #maxContentLength} 限制）</li>
 * </ul>
 * 该类型继续作为 SDK 的公共 API，避免升级 SDK 后破坏既有调用方的源码和二进制兼容性。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawDebugConfig {

    /** 是否允许 SDK 输出调试诊断信息。 */
    private boolean enabled;

    /** 启用调试后的详细程度。 */
    private HttpLogLevel level = HttpLogLevel.BASIC;

    /** BODY 级别单项正文允许记录的最大字符数。 */
    private int maxContentLength = 2_000;

    /**
     * 判断指定级别的日志是否允许输出。
     * @param required 待输出信息要求的最低级别
     * @return 调试已启用且当前级别满足要求时返回 {@code true}
     */
    public boolean allows(HttpLogLevel required) {
        return enabled && level != null && required != null && level.allows(required);
    }

    /**
     * 兼容短期使用字符串级别的调用方。
     *
     * @param required 待输出信息要求的最低级别名称
     * @return 调试已启用且当前级别满足要求时返回 {@code true}
     */
    public boolean allows(String required) {
        return required != null && allows(resolveLevel(required));
    }

    /**
     * 设置日志级别，保持旧版 SDK 公共 API 的源码与二进制兼容性。
     *
     * @param level 日志级别
     */
    public void setLevel(HttpLogLevel level) {
        this.level = level;
    }

    /**
     * 兼容 Spring 配置及短期使用字符串级别的调用方。
     *
     * @param level 日志级别名称
     */
    public void setLevel(String level) {
        this.level = resolveLevel(level);
    }

    /**
     * 返回经过下限保护的正文日志长度。
     *
     * @return 至少为 1 的最大正文字符数
     */
    public int resolveMaxContentLength() {
        return Math.max(1, maxContentLength);
    }

    private static HttpLogLevel resolveLevel(String level) {
        if (level == null) {
            return HttpLogLevel.BASIC;
        }
        try {
            return HttpLogLevel.valueOf(level.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return HttpLogLevel.BASIC;
        }
    }
}
