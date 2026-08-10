package io.github.easy4j.openclaw.util;

import io.github.easy4j.openclaw.api.OpenClawConstants;

import java.util.Map;
import java.util.Objects;

/**
 * OpenClaw SDK 的 `OpenClawStrings` 类型，封装其公开契约和生命周期边界。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawStrings {

    private OpenClawStrings() {
    }

    /**
     * 判断 `blank` 对应状态 是否满足协议或生命周期条件。
     *
     * @param value 写入 `value` 协议字段的内容
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 判断 `notBlank` 对应状态 是否满足协议或生命周期条件。
     *
     * @param value 写入 `value` 协议字段的内容
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * 移除字符串首尾空白；空白结果转换为 null。
     *
     * @param value 写入 `value` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 源字符串为空白时使用指定默认值，否则保留源字符串。
     *
     * @param value 写入 `value` 协议字段的内容
     * @param defaultValue 写入 `defaultValue` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        String trimmed = trimToNull(value);
        return trimmed != null ? trimmed : Objects.requireNonNull(defaultValue, "defaultValue");
    }

    /**
     * 把 null 规范化为空字符串，非 null 内容保持不变。
     *
     * @param value 写入 `value` 协议字段的内容
     * @return 服务返回或流式累积得到的文本
     */
    public static String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    /**
     * 仅在值包含非空白内容时写入目标映射。
     *
     * @param target 写入 `target` 协议字段的内容
     * @param key 写入 `key` 协议字段的内容
     * @param value 写入 `value` 协议字段的内容
     */
    public static void putIfNotBlank(Map<String, Object> target, String key, String value) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(key, "key");
        if (isNotBlank(value)) {
            target.put(key, value.trim());
        }
    }

    /**
     * 判断 `agentTarget` 对应状态 是否满足协议或生命周期条件。
     *
     * @param value 写入 `value` 协议字段的内容
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isAgentTarget(String value) {
        if (value == null) {
            return false;
        }
        return value.startsWith(OpenClawConstants.AGENT_PREFIX_OPENCLAW) ||
                value.startsWith(OpenClawConstants.AGENT_PREFIX_AGENT_COLON) ||
                value.startsWith(OpenClawConstants.AGENT_PREFIX_OPENCLAW_COLON);
    }
}
