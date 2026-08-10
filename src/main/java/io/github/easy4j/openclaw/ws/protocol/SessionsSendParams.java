package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `SessionsSendParams` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionsSendParams {

    /**
     * 映射 OpenClaw JSON 字段 `key` 的 协议内容。
     */
    private final String key;
    /**
     * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
     */
    private final String message;
    /**
     * 映射 OpenClaw JSON 字段 `thinking` 的 协议内容。
     */
    private final String thinking;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final Integer timeoutMs;

    private SessionsSendParams(Builder b) {
        this.key = b.key;
        this.message = b.message;
        this.thinking = b.thinking;
        this.timeoutMs = b.timeoutMs;
    }

    /**
     * 读取当前对象保存的 `key` 对应状态，不触发网络或子进程调用。
     *
     * @return 可用于关联后续请求的标识
     */
    public String getKey() { return key; }
    /**
     * 读取当前对象保存的 消息正文，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getMessage() { return message; }
    /**
     * 读取当前对象保存的 `thinking` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getThinking() { return thinking; }
    /**
     * 读取当前对象保存的 超时时间，单位为毫秒，不触发网络或子进程调用。
     *
     * @return 当前计数、状态码、可空配置或毫秒级时间值
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
     * 创建空白构建器，供调用方链式设置 `SessionsSendParams` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() { return new Builder(); }

    /**
     * 链式构建器，逐项收集 SessionsSendParams 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SessionsSendParams。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * 映射 OpenClaw JSON 字段 `key` 的 协议内容。
         */
        private String key;
        /**
         * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
         */
        private String message;
        /**
         * 映射 OpenClaw JSON 字段 `thinking` 的 协议内容。
         */
        private String thinking;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private Integer timeoutMs;

        /**
         * 设置 `--key` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param key 写入 `--key` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder key(String key) { this.key = key; return this; }
        /**
         * 设置 `--message` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { this.message = message; return this; }
        /**
         * 设置 `--thinking` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 写入 `--thinking` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) { this.thinking = thinking; return this; }
        /**
         * 设置 `--timeout-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ms 写入 `--timeout-ms` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer ms) { this.timeoutMs = ms; return this; }
        /**
         * 校验并复制当前构建器字段，创建独立的 `SessionsSendParams`。
         *
         * @return 按当前字段创建的 SessionsSendParams
         */
        public SessionsSendParams build() { return new SessionsSendParams(this); }
    }
}
