package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `approvals` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ApprovalsOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `get` 协议模式；序列化时使用该固定取值。
         */
        GET,
        /**
         * 选择 `set` 协议模式；序列化时使用该固定取值。
         */
        SET,
        /**
         * 选择 `allowlist_add` 协议模式；序列化时使用该固定取值。
         */
        ALLOWLIST_ADD,
        /**
         * 选择 `allowlist_remove` 协议模式；序列化时使用该固定取值。
         */
        ALLOWLIST_REMOVE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--node` 选项的内容；为 null 时通常省略。
     */
    private final String node;
    /**
     * 是否向 openclaw 子命令追加 `--gateway` 开关。
     */
    private final boolean gateway;
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
     * 传给 openclaw 子命令 `--file` 选项的内容；为 null 时通常省略。
     */
    private final String file;
    /**
     * 是否向 openclaw 子命令追加 `--stdin` 开关。
     */
    private final boolean stdin;
    /**
     * 传给 openclaw 子命令 `--allowlist-pattern` 选项的内容；为 null 时通常省略。
     */
    private final String allowlistPattern;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ApprovalsOptions(Builder b) {
        this.verb = b.verb;
        this.node = b.node;
        this.gateway = b.gateway;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.json = b.json;
        this.file = b.file;
        this.stdin = b.stdin;
        this.allowlistPattern = b.allowlistPattern;
        this.agent = b.agent;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `ApprovalsOptions` 字段。
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
            case GET:
                out.add("get");
                break;
            case SET:
                out.add("set");
                OpenClawCliArgv.addIfPresent(out, "--file", file);
                if (stdin) {
                    out.add("--stdin");
                }
                break;
            case ALLOWLIST_ADD:
                out.add("allowlist");
                out.add("add");
                if (allowlistPattern != null && OpenClawStrings.isNotBlank(allowlistPattern)) {
                    out.add(allowlistPattern.trim());
                }
                break;
            case ALLOWLIST_REMOVE:
                out.add("allowlist");
                out.add("remove");
                if (allowlistPattern != null && OpenClawStrings.isNotBlank(allowlistPattern)) {
                    out.add(allowlistPattern.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--node", node);
        OpenClawCliArgv.addFlag(out, "--gateway", gateway);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 ApprovalsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ApprovalsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.GET;
        /**
         * 传给 openclaw 子命令 `--node` 选项的内容；为 null 时通常省略。
         */
        private String node;
        /**
         * 是否向 openclaw 子命令追加 `--gateway` 开关。
         */
        private boolean gateway;
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
         * 传给 openclaw 子命令 `--file` 选项的内容；为 null 时通常省略。
         */
        private String file;
        /**
         * 是否向 openclaw 子命令追加 `--stdin` 开关。
         */
        private boolean stdin;
        /**
         * 传给 openclaw 子命令 `--allowlist-pattern` 选项的内容；为 null 时通常省略。
         */
        private String allowlistPattern;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `get` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder get() {
            this.verb = Verb.GET;
            return this;
        }

        /**
         * 选择 `set` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() {
            this.verb = Verb.SET;
            return this;
        }

        /**
         * 设置 `--file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder file(String path) {
            this.file = path;
            return this;
        }

        /**
         * 设置 `--stdin` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param stdin 是否向命令行追加 `--stdin` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stdin(boolean stdin) {
            this.stdin = stdin;
            return this;
        }

        /**
         * 设置 `--allowlist-add` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 写入 `--allowlist-add` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistAdd(String pattern) {
            this.verb = Verb.ALLOWLIST_ADD;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
         * 设置 `--allowlist-remove` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 写入 `--allowlist-remove` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistRemove(String pattern) {
            this.verb = Verb.ALLOWLIST_REMOVE;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
         * 设置 `--node` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param node 写入 `--node` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder node(String node) {
            this.node = node;
            return this;
        }

        /**
         * 设置 `--gateway` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gateway 是否向命令行追加 `--gateway` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gateway(boolean gateway) {
            this.gateway = gateway;
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
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
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
         * 校验并复制当前构建器字段，创建独立的 `ApprovalsOptions`。
         *
         * @return 按当前字段创建的 ApprovalsOptions
         */
        public ApprovalsOptions build() {
            return new ApprovalsOptions(this);
        }
    }
}
