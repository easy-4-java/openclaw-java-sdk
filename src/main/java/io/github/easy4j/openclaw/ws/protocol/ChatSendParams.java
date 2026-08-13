package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.cli.opts.ThinkingLevel;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenClaw Gateway {@code chat.send} RPC 的完整请求参数。
 * <p>字段对齐 OpenClaw 2026.7.1-2 {@code ChatSendParamsSchema}；可选字段为空时不会写入请求。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@lombok.Builder(builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatSendParams {

    /** Gateway 会话路由键。 */
    private final String sessionKey;
    /** 可选智能体标识。 */
    private final String agentId;
    /** 可选物理会话标识。 */
    private final String sessionId;
    /** 发送到目标会话的消息正文。 */
    private final String message;
    /** 本轮思考强度。 */
    private final String thinking;
    /** 本轮快速模式，可为 {@link Boolean} 或字符串 {@code auto}。 */
    private final Object fastMode;
    /** {@code fastMode=auto} 时保持快速模式的秒数。 */
    private final Integer fastAutoOnSeconds;
    /** 是否把回复投递到外部通道。 */
    private final Boolean deliver;
    /** 来源通道名称。 */
    private final String originatingChannel;
    /** 来源通道目标。 */
    private final String originatingTo;
    /** 来源通道账号标识。 */
    private final String originatingAccountId;
    /** 来源线程标识。 */
    private final String originatingThreadId;
    /** OpenClaw 接受的原始附件对象列表。 */
    private final List<Object> attachments;
    /** 请求超时毫秒数。 */
    private final Integer timeoutMs;
    /** 可信系统输入来源。 */
    private final SystemInputProvenance systemInputProvenance;
    /** 系统来源回执。 */
    private final String systemProvenanceReceipt;
    /** 是否禁止把消息中的斜杠指令解释为控制命令。 */
    private final Boolean suppressCommandInterpretation;
    /** 调用方期望的会话路由契约。 */
    private final String expectedSessionRoutingContract;
    /** 幂等键；相同业务请求重试时应复用同一个值。 */
    private final String idempotencyKey;

    /**
     * {@code ChatSendParams} 构建器，保留字符串协议值并提供类型安全重载。
     */
    public static class Builder {
        /**
         * 设置原始字符串思考等级，供前向兼容新等级。
         *
         * @param thinking OpenClaw 思考等级
         * @return 当前构建器
         */
        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        /**
         * 类型安全地设置 OpenClaw 标准思考等级。
         *
         * @param thinking 思考等级
         * @return 当前构建器
         */
        public Builder thinkingLevel(ThinkingLevel thinking) {
            this.thinking = thinking == null ? null : thinking.cliValue();
            return this;
        }

        /**
         * 使用布尔值设置快速模式。
         *
         * @param fastMode 是否启用快速模式
         * @return 当前构建器
         */
        public Builder fastMode(Boolean fastMode) {
            this.fastMode = fastMode;
            return this;
        }

        /**
         * 使用协议字符串设置快速模式；当前 OpenClaw 支持 {@code auto}。
         *
         * @param fastMode 快速模式值
         * @return 当前构建器
         */
        public Builder fastMode(String fastMode) {
            this.fastMode = fastMode;
            return this;
        }
    }

    /**
     * 把当前对象编码为 Gateway WebSocket RPC 接受的参数映射。
     *
     * @return 仅包含已设置字段的参数映射
     */
    public Map<String, Object> toParamsMap() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        putIfNonNull(params, "sessionKey", sessionKey);
        putIfNonNull(params, "agentId", agentId);
        putIfNonNull(params, "sessionId", sessionId);
        putIfNonNull(params, "message", message);
        putIfNonNull(params, "thinking", thinking);
        putIfNonNull(params, "fastMode", fastMode);
        putIfNonNull(params, "fastAutoOnSeconds", fastAutoOnSeconds);
        putIfNonNull(params, "deliver", deliver);
        putIfNonNull(params, "originatingChannel", originatingChannel);
        putIfNonNull(params, "originatingTo", originatingTo);
        putIfNonNull(params, "originatingAccountId", originatingAccountId);
        putIfNonNull(params, "originatingThreadId", originatingThreadId);
        putIfNonNull(params, "attachments", attachments);
        putIfNonNull(params, "timeoutMs", timeoutMs);
        if (systemInputProvenance != null) {
            params.put("systemInputProvenance", systemInputProvenance.toParamsMap());
        }
        putIfNonNull(params, "systemProvenanceReceipt", systemProvenanceReceipt);
        putIfNonNull(params, "suppressCommandInterpretation", suppressCommandInterpretation);
        putIfNonNull(params, "expectedSessionRoutingContract", expectedSessionRoutingContract);
        putIfNonNull(params, "idempotencyKey", idempotencyKey);
        return params;
    }

    private static void putIfNonNull(Map<String, Object> params, String key, Object value) {
        if (value != null) {
            params.put(key, value);
        }
    }

    /**
     * 系统输入的来源证明，供 Gateway 识别可信的跨会话或工具生成输入。
     */
    @Getter
    @lombok.Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SystemInputProvenance {
        /** 来源类型。 */
        private final String kind;
        /** 原始会话标识。 */
        private final String originSessionId;
        /** 来源会话键。 */
        private final String sourceSessionKey;
        /** 来源通道。 */
        private final String sourceChannel;
        /** 来源工具。 */
        private final String sourceTool;

        /**
         * 编码为 Gateway Schema 对应的参数映射。
         *
         * @return 仅包含已设置字段的来源映射
         */
        public Map<String, Object> toParamsMap() {
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            putIfNonNull(params, "kind", kind);
            putIfNonNull(params, "originSessionId", originSessionId);
            putIfNonNull(params, "sourceSessionKey", sourceSessionKey);
            putIfNonNull(params, "sourceChannel", sourceChannel);
            putIfNonNull(params, "sourceTool", sourceTool);
            return params;
        }
    }
}
