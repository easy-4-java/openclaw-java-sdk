package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * sessions.send RPC 参数，包含会话键、消息、思考等级和超时。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionsSendParams {

    /**
     * JSON 属性 {@code key}，表示属性或会话键。
     */
    private final String key;
    /**
     * 发送到目标会话的消息正文。
     */
    private final String message;
    /**
     * JSON 属性 {@code thinking}，表示思考强度选项。
     */
    private final String thinking;
    /**
     * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
     */
    private final Integer timeoutMs;

    private SessionsSendParams(Builder b) {
        this.key = b.key;
        this.message = b.message;
        this.thinking = b.thinking;
        this.timeoutMs = b.timeoutMs;
    }

    /**
     * 返回目标会话键。
     *
     * @return Gateway 会话路由键
     */
    public String getKey() { return key; }
    /**
     * 返回消息正文。
     *
     * @return 发送给会话的消息正文
     */
    public String getMessage() { return message; }
    /**
     * 返回本次请求使用的思考强度选项。
     *
     * @return 本次请求的思考强度；未设置时为 {@code null}
     */
    public String getThinking() { return thinking; }
    /**
     * 返回会话消息发送超时，单位为毫秒。
     *
     * @return 请求超时毫秒数；未设置时为空
     */
    public Integer getTimeoutMs() { return timeoutMs; }

    /**
     * 把当前协议对象编码为 Gateway WebSocket RPC 接受的键值参数。
     *
     * @return 键名与 OpenClaw JSON/CLI 协议一致的映射
     */
    public Map<String, Object> toParamsMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("key", key);
        m.put("message", message);
        if (thinking != null) m.put("thinking", thinking);
        if (timeoutMs != null) m.put("timeoutMs", timeoutMs);
        return m;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code SessionsSendParams} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() { return new Builder(); }

    /**
     * {@code SessionsSendParams} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * JSON 属性 {@code key}，表示属性或会话键。
         */
        private String key;
        /**
         * 构建中的会话消息正文。
         */
        private String message;
        /**
         * JSON 属性 {@code thinking}，表示思考强度选项。
         */
        private String thinking;
        /**
         * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
         */
        private Integer timeoutMs;

        /**
         * 设置 {@code --key} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param key 请求幂等键或目标键名；作为 {@code --key} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder key(String key) { this.key = key; return this; }
        /**
         * 设置 {@code --message} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { this.message = message; return this; }
        /**
         * 设置 {@code --thinking} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 模型思考强度；作为 {@code --thinking} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) { this.thinking = thinking; return this; }
        /**
         * 设置 {@code --timeout-ms} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ms 超时时长，单位为毫秒；作为 {@code --timeout-ms} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer ms) { this.timeoutMs = ms; return this; }
        /**
         * 校验并复制当前构建器字段，创建独立的 {@code SessionsSendParams}。
         *
         * @return 按当前字段创建的 SessionsSendParams
         */
        public SessionsSendParams build() { return new SessionsSendParams(this); }
    }
}
