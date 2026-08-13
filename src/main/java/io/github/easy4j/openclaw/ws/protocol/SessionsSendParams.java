package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.cli.opts.ThinkingLevel;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenClaw Gateway {@code sessions.send} RPC 的完整请求参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@lombok.Builder(builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionsSendParams {

    /** Gateway 会话路由键。 */
    private final String key;
    /** 可选智能体标识。 */
    private final String agentId;
    /** 发送到目标会话的消息正文。 */
    private final String message;
    /** 本轮思考强度。 */
    private final String thinking;
    /** OpenClaw 接受的原始附件对象列表。 */
    private final List<Object> attachments;
    /** 请求超时毫秒数。 */
    private final Integer timeoutMs;
    /** 可选幂等键。 */
    private final String idempotencyKey;

    /**
     * 为构建器提供类型安全的思考等级重载。
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
         * 设置 OpenClaw 标准思考等级。
         *
         * @param thinking 思考等级
         * @return 当前构建器
         */
        public Builder thinkingLevel(ThinkingLevel thinking) {
            this.thinking = thinking == null ? null : thinking.cliValue();
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
        putIfNonNull(params, "key", key);
        putIfNonNull(params, "agentId", agentId);
        putIfNonNull(params, "message", message);
        putIfNonNull(params, "thinking", thinking);
        putIfNonNull(params, "attachments", attachments);
        putIfNonNull(params, "timeoutMs", timeoutMs);
        putIfNonNull(params, "idempotencyKey", idempotencyKey);
        return params;
    }

    private static void putIfNonNull(Map<String, Object> params, String key, Object value) {
        if (value != null) {
            params.put(key, value);
        }
    }
}
