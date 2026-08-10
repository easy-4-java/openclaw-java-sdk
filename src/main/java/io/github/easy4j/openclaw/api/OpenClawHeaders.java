package io.github.easy4j.openclaw.api;

import io.github.easy4j.openclaw.util.OpenClawStrings;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenClaw 扩展请求头的不可变集合，用于覆盖模型、Agent、会话路由、消息通道和权限范围。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawHeaders {

    /**
     * HTTP 请求头 `OpenClawConstants.HEADER_X_OPENCLAW_MODEL` 的规范名称。
     */
    public static final String X_OPENCLAW_MODEL = OpenClawConstants.HEADER_X_OPENCLAW_MODEL;

    /**
     * HTTP 请求头 `OpenClawConstants.HEADER_X_OPENCLAW_AGENT_ID` 的规范名称。
     */
    public static final String X_OPENCLAW_AGENT_ID = OpenClawConstants.HEADER_X_OPENCLAW_AGENT_ID;

    /**
     * HTTP 请求头 `OpenClawConstants.HEADER_X_OPENCLAW_SESSION_KEY` 的规范名称。
     */
    public static final String X_OPENCLAW_SESSION_KEY = OpenClawConstants.HEADER_X_OPENCLAW_SESSION_KEY;

    /**
     * HTTP 请求头 `OpenClawConstants.HEADER_X_OPENCLAW_MESSAGE_CHANNEL` 的规范名称。
     */
    public static final String X_OPENCLAW_MESSAGE_CHANNEL = OpenClawConstants.HEADER_X_OPENCLAW_MESSAGE_CHANNEL;

    /**
     * HTTP 请求头 `OpenClawConstants.HEADER_X_OPENCLAW_SCOPES` 的规范名称。
     */
    public static final String X_OPENCLAW_SCOPES = OpenClawConstants.HEADER_X_OPENCLAW_SCOPES;

    private OpenClawHeaders() {}

    /**
     * 创建空白构建器，供调用方链式设置 `OpenClawHeaders` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 链式构建器，逐项收集 OpenClawHeaders 的字段；build() 会复制当前快照，后续修改不会影响已构造的 OpenClawHeaders。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 模型标识。
         */
        private String model;
        /**
         * Agent 标识。
         */
        private String agentId;
        /**
         * 会话路由键。
         */
        private String sessionKey;
        /**
         * `Builder` 生命周期内保存的 `messageChannel` 对应状态。
         */
        private String messageChannel;
        /**
         * `Builder` 生命周期内保存的 `scopes` 对应状态。
         */
        private String scopes;

        /**
         * 设置 `--model` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param model 模型标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置 `--agent-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agentId Agent 标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agentId(String agentId) {
            this.agentId = agentId;
            return this;
        }

        /**
         * 设置 `--session-key` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionKey 会话路由键
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
            return this;
        }

        /**
         * 设置 `--message-channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param messageChannel 写入 `--message-channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder messageChannel(String messageChannel) {
            this.messageChannel = messageChannel;
            return this;
        }

        /**
         * 设置 `--scopes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param scopes 写入 `--scopes` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scopes(String scopes) {
            this.scopes = scopes;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `OpenClawHeaders`。
         *
         * @return 按当前字段创建的 OpenClawHeaders
         */
        public Map<String, String> build() {
            Map<String, String> headers = new LinkedHashMap<>();
            if (OpenClawStrings.isNotBlank(model)) {
                headers.put(X_OPENCLAW_MODEL, model);
            }
            if (OpenClawStrings.isNotBlank(agentId)) {
                headers.put(X_OPENCLAW_AGENT_ID, agentId);
            }
            if (OpenClawStrings.isNotBlank(sessionKey)) {
                headers.put(X_OPENCLAW_SESSION_KEY, sessionKey);
            }
            if (OpenClawStrings.isNotBlank(messageChannel)) {
                headers.put(X_OPENCLAW_MESSAGE_CHANNEL, messageChannel);
            }
            if (OpenClawStrings.isNotBlank(scopes)) {
                headers.put(X_OPENCLAW_SCOPES, scopes);
            }
            return Collections.unmodifiableMap(headers);
        }
    }
}
