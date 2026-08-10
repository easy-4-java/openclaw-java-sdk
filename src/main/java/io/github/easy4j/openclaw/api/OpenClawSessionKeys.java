package io.github.easy4j.openclaw.api;

import io.github.easy4j.openclaw.api.model.HookRequest;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 会话标识工具，生成稳定业务会话键、临时对端会话键和请求关联标识。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawSessionKeys {

    /**
     * 会话键单段校验规则：首字符为字母或数字，总长不超过 128。
     */
    private static final Pattern SAFE_SEGMENT = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]{0,127}$");

    private OpenClawSessionKeys() {
    }

    /**
     * 使用智能体和对端标识创建可重复计算的稳定 Hook 会话键。
     *
     * @param agentId Agent 标识
     * @param peerId 用于关联协议对象的 {@code peerId} 标识
     * @return 格式为 {@code hook:<agentId>:<peerId>} 的稳定会话键
     */
    public static String forStableSession(String agentId, String peerId) {
        return "hook:" + normalizeSegment(agentId, "agentId") + ":"
                + normalizeSegment(peerId, "peerId");
    }

    /**
     * 使用对端和关联标识创建一次调用范围内的 Hook 会话键。
     *
     * @param peerId 用于关联协议对象的 {@code peerId} 标识
     * @param correlationId 用于关联协议对象的 {@code correlationId} 标识
     * @return 格式为 {@code hook:<peerId>:<correlationId>} 的临时会话键
     */
    public static String forEphemeralPeer(String peerId, String correlationId) {
        return "hook:" + normalizeSegment(peerId, "peerId") + ":"
                + normalizeSegment(correlationId, "correlationId");
    }

    /**
     * 使用新生成的关联标识创建临时 Hook 会话键。
     *
     * @param peerId 用于关联协议对象的 {@code peerId} 标识
     * @return 使用新关联标识创建的临时会话键
     */
    public static String forEphemeralPeer(String peerId) {
        return forEphemeralPeer(peerId, newCorrelationId());
    }

    /**
     * 生成不含连字符的小写 UUID，供临时会话键关联请求。
     *
     * @return 可用于关联后续请求的标识
     */
    public static String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 去除会话键分段两端空白并转为小写，同时拒绝空值和保留分隔符 {@code :}。
     *
     * @param value 待规范化的会话键分段
     * @param fieldName 参数名，用于生成校验异常信息
     * @return 可安全拼接进会话键的规范化分段
     * @throws NullPointerException {@code value} 为空时抛出
     * @throws IllegalArgumentException 分段为空白或包含冒号时抛出
     */
    static String normalizeSegment(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        if (normalized.contains(":")) {
            throw new IllegalArgumentException(fieldName + " must not contain ':'");
        }
        if (!SAFE_SEGMENT.matcher(normalized).matches()) {
            throw new IllegalArgumentException(fieldName + " contains illegal characters: " + value);
        }
        return normalized;
    }
}
