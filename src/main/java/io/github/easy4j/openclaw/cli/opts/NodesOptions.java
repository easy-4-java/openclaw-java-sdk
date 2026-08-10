package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `nodes` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class NodesOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `pending` 协议模式；序列化时使用该固定取值。
         */
        PENDING,
        /**
         * 选择 `approve` 协议模式；序列化时使用该固定取值。
         */
        APPROVE,
        /**
         * 选择 `reject` 协议模式；序列化时使用该固定取值。
         */
        REJECT,
        /**
         * 选择 `rename` 协议模式；序列化时使用该固定取值。
         */
        RENAME,
        /**
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS,
        /**
         * 选择 `invoke` 协议模式；序列化时使用该固定取值。
         */
        INVOKE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 `--list-connected` 开关。
     */
    private final boolean listConnected;
    /**
     * 传给 openclaw 子命令 `--last-connected` 选项的内容；为 null 时通常省略。
     */
    private final String lastConnected;
    /**
     * 传给 openclaw 子命令 `--request-id` 选项的内容；为 null 时通常省略。
     */
    private final String requestId;
    /**
     * 传给 openclaw 子命令 `--node-ref` 选项的内容；为 null 时通常省略。
     */
    private final String nodeRef;
    /**
     * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
     */
    private final String name;
    /**
     * 传给 openclaw 子命令 `--command` 选项的内容；为 null 时通常省略。
     */
    private final String command;
    /**
     * 传给 openclaw 子命令 `--params-json` 选项的内容；为 null 时通常省略。
     */
    private final String paramsJson;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String invokeTimeout;
    /**
     * 传给 openclaw 子命令 `--idempotency-key` 选项的内容；为 null 时通常省略。
     */
    private final String idempotencyKey;
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
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private NodesOptions(Builder b) {
        this.verb = b.verb;
        this.listConnected = b.listConnected;
        this.lastConnected = b.lastConnected;
        this.requestId = b.requestId;
        this.nodeRef = b.nodeRef;
        this.name = b.name;
        this.command = b.command;
        this.paramsJson = b.paramsJson;
        this.invokeTimeout = b.invokeTimeout;
        this.idempotencyKey = b.idempotencyKey;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `NodesOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (verb) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case PENDING:
                out.add("pending");
                break;
            case APPROVE:
                out.add("approve");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case REJECT:
                out.add("reject");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case RENAME:
                out.add("rename");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--name", name);
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case INVOKE:
                out.add("invoke");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--command", command);
                OpenClawCliArgv.addIfPresent(out, "--params", paramsJson);
                OpenClawCliArgv.addIfPresent(out, "--invoke-timeout", invokeTimeout);
                OpenClawCliArgv.addIfPresent(out, "--idempotency-key", idempotencyKey);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 NodesOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 NodesOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.LIST;
        /**
         * 是否向 openclaw 子命令追加 `--list-connected` 开关。
         */
        private boolean listConnected;
        /**
         * 传给 openclaw 子命令 `--last-connected` 选项的内容；为 null 时通常省略。
         */
        private String lastConnected;
        /**
         * 传给 openclaw 子命令 `--request-id` 选项的内容；为 null 时通常省略。
         */
        private String requestId;
        /**
         * 传给 openclaw 子命令 `--node-ref` 选项的内容；为 null 时通常省略。
         */
        private String nodeRef;
        /**
         * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
         */
        private String name;
        /**
         * 传给 openclaw 子命令 `--command` 选项的内容；为 null 时通常省略。
         */
        private String command;
        /**
         * 传给 openclaw 子命令 `--params-json` 选项的内容；为 null 时通常省略。
         */
        private String paramsJson;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String invokeTimeout;
        /**
         * 传给 openclaw 子命令 `--idempotency-key` 选项的内容；为 null 时通常省略。
         */
        private String idempotencyKey;
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
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 `--list-connected` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param connected 是否向命令行追加 `--list-connected` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listConnected(boolean connected) {
            this.listConnected = connected;
            return this;
        }

        /**
         * 设置 `--last-connected` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param duration 写入 `--last-connected` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder lastConnected(String duration) {
            this.lastConnected = duration;
            return this;
        }

        /**
         * 选择 `pending` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pending() {
            this.verb = Verb.PENDING;
            return this;
        }

        /**
         * 设置 `--approve` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requestId 请求标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve(String requestId) {
            this.verb = Verb.APPROVE;
            this.requestId = requestId;
            return this;
        }

        /**
         * 设置 `--reject` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requestId 请求标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder reject(String requestId) {
            this.verb = Verb.REJECT;
            this.requestId = requestId;
            return this;
        }

        /**
         * 设置 `--rename` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeRef 写入 `--rename` 选项的内容
         * @param displayName 写入 `--rename` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rename(String nodeRef, String displayName) {
            this.verb = Verb.RENAME;
            this.nodeRef = nodeRef;
            this.name = displayName;
            return this;
        }

        /**
         * 选择 `status` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * 设置 `--invoke` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeRef 写入 `--invoke` 选项的内容
         * @param command 写入 `--invoke` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder invoke(String nodeRef, String command) {
            this.verb = Verb.INVOKE;
            this.nodeRef = nodeRef;
            this.command = command;
            return this;
        }

        /**
         * 设置 `--params-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder paramsJson(String json) {
            this.paramsJson = json;
            return this;
        }

        /**
         * 设置 `--invoke-timeout` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ms 写入 `--invoke-timeout` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder invokeTimeout(String ms) {
            this.invokeTimeout = ms;
            return this;
        }

        /**
         * 设置 `--idempotency-key` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param key 写入 `--idempotency-key` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder idempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

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
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `NodesOptions`。
         *
         * @return 按当前字段创建的 NodesOptions
         */
        public NodesOptions build() {
            return new NodesOptions(this);
        }
    }
}
