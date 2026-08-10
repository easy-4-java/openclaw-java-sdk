package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * openclaw `gateway-rpc` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class GatewayRpcOptions {

    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 `--expect-final` 开关。
     */
    private final boolean expectFinal;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;

    /**
 * {@link Builder} ; {@link #builder}.
     *
 * @param b null builder
     */
    private GatewayRpcOptions(Builder b) {
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.expectFinal = b.expectFinal;
        this.json = b.json;
    }

    /**
     * 读取当前对象保存的 完整目标 URL，不触发网络或子进程调用。
     *
     * @return 规范化后的目标地址或路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 读取当前对象保存的 `token` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getToken() {
        return token;
    }

    /**
     * 读取当前对象保存的 `password` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getPassword() {
        return password;
    }

    /**
     * 读取当前对象保存的 `timeout` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * 判断 `expectFinal` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isExpectFinal() {
        return expectFinal;
    }

    /**
     * 判断 JSON 文本 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isJson() {
        return json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `GatewayRpcOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 `gateway-rpc` 子命令的 `appendSharedFlags` 行为，并保留未设置选项的省略语义。
     *
     * @param args 写入 `args` 协议字段的内容
     */
    public void appendSharedFlags(List<String> args) {
        Objects.requireNonNull(args, "args");
        if (url != null && !url.isEmpty()) {
            args.add("--url");
            args.add(url);
        }
        if (token != null && !token.isEmpty()) {
            args.add("--token");
            args.add(token);
        }
        if (password != null && !password.isEmpty()) {
            args.add("--password");
            args.add(password);
        }
        if (timeout != null && !timeout.isEmpty()) {
            args.add("--timeout");
            args.add(timeout);
        }
        if (expectFinal) {
            args.add("--expect-final");
        }
        if (json) {
            args.add("--json");
        }
    }

    /**
     * 选择或编码 `gateway-rpc` 子命令的 `toFlagList` 行为，并保留未设置选项的省略语义。
     *
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public List<String> toFlagList() {
        List<String> args = new ArrayList<>();
        appendSharedFlags(args);
        return args;
    }

    /**
     * 链式构建器，逐项收集 GatewayRpcOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 GatewayRpcOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 `--expect-final` 开关。
         */
        private boolean expectFinal;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

        /**
         * 设置 `--url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置 `--timeout` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout 写入 `--timeout` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置 `--expect-final` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expectFinal 是否向命令行追加 `--expect-final` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder expectFinal(boolean expectFinal) {
            this.expectFinal = expectFinal;
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `GatewayRpcOptions`。
         *
         * @return 按当前字段创建的 GatewayRpcOptions
         */
        public GatewayRpcOptions build() {
            return new GatewayRpcOptions(this);
        }
    }
}
