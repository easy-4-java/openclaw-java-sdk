package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * chat.send RPC 参数，包含会话、消息、投递来源和超时。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatSendParams {

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private final String sessionKey;
    /**
     * 发送到目标聊天会话的消息正文。
     */
    private final String message;
    /**
     * JSON 属性 {@code thinking}，表示思考强度选项。
     */
    private final String thinking;
    /**
     * JSON 属性 {@code deliver}，表示是否向外部通道投递消息。
     */
    private final Boolean deliver;
    /**
     * JSON 属性 {@code originatingChannel}，表示来源通道名称。
     */
    private final String originatingChannel;
    /**
     * JSON 属性 {@code originatingTo}，表示来源通道目标。
     */
    private final String originatingTo;
    /**
     * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
     */
    private final Integer timeoutMs;

    private ChatSendParams(Builder b) {
        this.sessionKey = b.sessionKey;
        this.message = b.message;
        this.thinking = b.thinking;
        this.deliver = b.deliver;
        this.originatingChannel = b.originatingChannel;
        this.originatingTo = b.originatingTo;
        this.timeoutMs = b.timeoutMs;
    }

    /**
     * 返回会话路由键。
     *
     * @return 可用于关联后续请求的标识
     */
    public String getSessionKey() { return sessionKey; }
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
     * 返回是否将消息投递到外部通道。
     *
     * @return 是否投递到外部通道；未设置时返回 {@code null}
     */
    public Boolean getDeliver() { return deliver; }
    /**
     * 返回触发本次消息的来源通道。
     *
     * @return 触发消息的来源通道；未设置时为 {@code null}
     */
    public String getOriginatingChannel() { return originatingChannel; }
    /**
     * 返回来源通道中的目标地址。
     *
     * @return 来源通道中的目标地址；未设置时为 {@code null}
     */
    public String getOriginatingTo() { return originatingTo; }
    /**
     * 返回聊天消息发送超时，单位为毫秒。
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
        if (sessionKey != null) m.put("sessionKey", sessionKey);
        m.put("message", message);
        if (thinking != null) m.put("thinking", thinking);
        if (deliver != null) m.put("deliver", deliver);
        if (originatingChannel != null) m.put("originatingChannel", originatingChannel);
        if (originatingTo != null) m.put("originatingTo", originatingTo);
        if (timeoutMs != null) m.put("timeoutMs", timeoutMs);
        return m;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ChatSendParams} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() { return new Builder(); }

    /**
     * {@code ChatSendParams} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
         */
        private String sessionKey;
        /**
         * 构建中的聊天消息正文。
         */
        private String message;
        /**
         * JSON 属性 {@code thinking}，表示思考强度选项。
         */
        private String thinking;
        /**
         * JSON 属性 {@code deliver}，表示是否向外部通道投递消息。
         */
        private Boolean deliver;
        /**
         * JSON 属性 {@code originatingChannel}，表示来源通道名称。
         */
        private String originatingChannel;
        /**
         * JSON 属性 {@code originatingTo}，表示来源通道目标。
         */
        private String originatingTo;
        /**
         * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
         */
        private Integer timeoutMs;

        /**
         * 设置 {@code --session-key} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionKey 会话路由键
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionKey(String sessionKey) { this.sessionKey = sessionKey; return this; }
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
         * 设置 {@code --deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否把消息投递至外部通道；作为 {@code --deliver} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(Boolean deliver) { this.deliver = deliver; return this; }
        /**
         * 设置 {@code --originating-channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ch 触发消息的来源通道；作为 {@code --originating-channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder originatingChannel(String ch) { this.originatingChannel = ch; return this; }
        /**
         * 设置 {@code --originating-to} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param to 消息投递目标；作为 {@code --originating-to} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder originatingTo(String to) { this.originatingTo = to; return this; }
        /**
         * 设置 {@code --timeout-ms} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ms 超时时长，单位为毫秒；作为 {@code --timeout-ms} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer ms) { this.timeoutMs = ms; return this; }
        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ChatSendParams}。
         *
         * @return 按当前字段创建的 ChatSendParams
         */
        public ChatSendParams build() { return new ChatSendParams(this); }
    }
}
