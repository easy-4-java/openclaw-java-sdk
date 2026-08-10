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
     * OpenClaw 协议固定值 {@code Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]{0,127}$")}；调用方不应在运行时修改。
     */
    private static final Pattern SAFE_SEGMENT = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]{0,127}$");

    private OpenClawSessionKeys() {
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawSessionKeys`。
     *
     * @param agentId Agent 标识
     * @param peerId 用于关联协议对象的 `peerId` 标识
     * @return 服务返回或流式累积得到的文本
     */
    public static String forStableSession(String agentId, String peerId) {
        return "hook:" + normalizeSegment(agentId, "agentId") + ":"
                + normalizeSegment(peerId, "peerId");
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawSessionKeys`。
     *
     * @param peerId 用于关联协议对象的 `peerId` 标识
     * @param correlationId 用于关联协议对象的 `correlationId` 标识
     * @return 服务返回或流式累积得到的文本
     */
    public static String forEphemeralPeer(String peerId, String correlationId) {
        return "hook:" + normalizeSegment(peerId, "peerId") + ":"
                + normalizeSegment(correlationId, "correlationId");
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawSessionKeys`。
     *
     * @param peerId 用于关联协议对象的 `peerId` 标识
     * @return 服务返回或流式累积得到的文本
     */
    public static String forEphemeralPeer(String peerId) {
        return forEphemeralPeer(peerId, newCorrelationId());
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawSessionKeys`。
     *
     * @return 可用于关联后续请求的标识
     */
    public static String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
 * Normalizes hook session :trim, {@code :} characters.
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
