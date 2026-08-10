package io.github.easy4j.openclaw.util;

import io.github.easy4j.openclaw.api.OpenClawConstants;

import java.util.Map;
import java.util.Objects;

/**
 * SDK 内部使用的空白字符串、默认值和智能体目标判断工具。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawStrings {

    private OpenClawStrings() {
    }

    /**
     * 判断字符串是否为 null、空串或仅包含空白字符。
     *
     * @param value 待检查或标准化的字符串；可为空
     * @return 输入为 {@code null}、空串或仅含空白时返回 {@code true}
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 判断字符串是否包含至少一个非空白字符。
     *
     * @param value 待检查或标准化的字符串；可为空
     * @return 输入包含至少一个非空白字符时返回 {@code true}
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * 移除字符串首尾空白；空白结果转换为 null。
     *
     * @param value 待检查或标准化的字符串；可为空
     * @return 去除首尾空白的内容；输入为空或仅含空白时返回 {@code null}
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
     * @param value 待检查或标准化的字符串；可为空
     * @param defaultValue 输入为空时使用的默认值
     * @return 去除首尾空白的输入，或输入为空白时的默认值
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        String trimmed = trimToNull(value);
        return trimmed != null ? trimmed : Objects.requireNonNull(defaultValue, "defaultValue");
    }

    /**
     * 把 null 规范化为空字符串，非 null 内容保持不变。
     *
     * @param value 待检查或标准化的字符串；可为空
     * @return 原字符串，或输入为 {@code null} 时的空字符串
     */
    public static String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    /**
     * 仅在值包含非空白内容时写入目标映射。
     *
     * @param target 接收解析结果或命令参数的目标集合
     * @param key 会话、请求或协议属性键
     * @param value 候选属性值；仅非空白时写入目标映射
     */
    public static void putIfNotBlank(Map<String, Object> target, String key, String value) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(key, "key");
        if (isNotBlank(value)) {
            target.put(key, value.trim());
        }
    }

    /**
     * 判断字符串是否符合 SDK 接受的 OpenClaw 智能体目标形式。
     *
     * @param value 待判断是否为智能体目标的字符串
     * @return 输入使用 {@code agent:} 前缀表示智能体目标时返回 {@code true}
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
