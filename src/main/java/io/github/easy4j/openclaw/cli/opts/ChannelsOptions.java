package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `channels` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ChannelsOptions implements CliSubArgs {

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
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS,
        /**
         * 选择 `capabilities` 协议模式；序列化时使用该固定取值。
         */
        CAPABILITIES,
        /**
         * 选择 `resolve` 协议模式；序列化时使用该固定取值。
         */
        RESOLVE,
        /**
         * 选择 `logs` 协议模式；序列化时使用该固定取值。
         */
        LOGS,
        /**
         * 选择 `add` 协议模式；序列化时使用该固定取值。
         */
        ADD,
        /**
         * 选择 `remove` 协议模式；序列化时使用该固定取值。
         */
        REMOVE,
        /**
         * 选择 `login` 协议模式；序列化时使用该固定取值。
         */
        LOGIN,
        /**
         * 选择 `logout` 协议模式；序列化时使用该固定取值。
         */
        LOGOUT
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 `--status-probe` 开关。
     */
    private final boolean statusProbe;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
     */
    private final String account;
    /**
     * 传给 openclaw 子命令 `--target` 选项的内容；为 null 时通常省略。
     */
    private final String target;
    /**
     * 传给 openclaw 子命令 `--kind` 选项的内容；为 null 时通常省略。
     */
    private final String kind;
    /**
     * 传给 openclaw 子命令 `--resolve-positional` 选项的内容；为 null 时通常省略。
     */
    private final List<String> resolvePositional;
    /**
     * 传给 openclaw 子命令 `--log-lines` 选项的内容；为 null 时通常省略。
     */
    private final Integer logLines;
    /**
     * 是否向 openclaw 子命令追加 `--remove-delete` 开关。
     */
    private final boolean removeDelete;
    /**
     * 是否向 openclaw 子命令追加 `--login-verbose` 开关。
     */
    private final boolean loginVerbose;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ChannelsOptions(Builder b) {
        this.verb = b.verb;
        this.statusProbe = b.statusProbe;
        this.timeout = b.timeout;
        this.json = b.json;
        this.channel = b.channel;
        this.account = b.account;
        this.target = b.target;
        this.kind = b.kind;
        this.resolvePositional = b.resolvePositional == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.resolvePositional);
        this.logLines = b.logLines;
        this.removeDelete = b.removeDelete;
        this.loginVerbose = b.loginVerbose;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `ChannelsOptions` 字段。
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
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--probe", statusProbe);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case CAPABILITIES:
                out.add("capabilities");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--account", account);
                OpenClawCliArgv.addIfPresent(out, "--target", target);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case RESOLVE:
                out.add("resolve");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--account", account);
                OpenClawCliArgv.addIfPresent(out, "--kind", kind);
                OpenClawCliArgv.addFlag(out, "--json", json);
                out.addAll(resolvePositional);
                break;
            case LOGS:
                out.add("logs");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfNotNull(out, "--lines", logLines);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case ADD:
                out.add("add");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                break;
            case REMOVE:
                out.add("remove");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addFlag(out, "--delete", removeDelete);
                break;
            case LOGIN:
                out.add("login");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addFlag(out, "--verbose", loginVerbose);
                break;
            case LOGOUT:
                out.add("logout");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 ChannelsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ChannelsOptions。
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
         * 是否向 openclaw 子命令追加 `--status-probe` 开关。
         */
        private boolean statusProbe;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
         */
        private String account;
        /**
         * 传给 openclaw 子命令 `--target` 选项的内容；为 null 时通常省略。
         */
        private String target;
        /**
         * 传给 openclaw 子命令 `--kind` 选项的内容；为 null 时通常省略。
         */
        private String kind;
        /**
         * 传给 openclaw 子命令 `--resolve-positional` 选项的内容；为 null 时通常省略。
         */
        private List<String> resolvePositional = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--log-lines` 选项的内容；为 null 时通常省略。
         */
        private Integer logLines;
        /**
         * 是否向 openclaw 子命令追加 `--remove-delete` 开关。
         */
        private boolean removeDelete;
        /**
         * 是否向 openclaw 子命令追加 `--login-verbose` 开关。
         */
        private boolean loginVerbose;
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
         * 选择 `status` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * 设置 `--status-probe` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probe 是否向命令行追加 `--status-probe` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusProbe(boolean probe) {
            this.statusProbe = probe;
            return this;
        }

        /**
         * 选择 `capabilities` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder capabilities() {
            this.verb = Verb.CAPABILITIES;
            return this;
        }

        /**
         * 设置 `--resolve` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param positionalNames 写入 `--resolve` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder resolve(String... positionalNames) {
            this.verb = Verb.RESOLVE;
            this.resolvePositional = new ArrayList<>();
            if (positionalNames != null) {
                for (String p : positionalNames) {
                    if (p != null && OpenClawStrings.isNotBlank(p)) {
                        resolvePositional.add(p.trim());
                    }
                }
            }
            return this;
        }

        /**
         * 选择 `logs` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logs() {
            this.verb = Verb.LOGS;
            return this;
        }

        /**
         * 选择 `add` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
         * 选择 `remove` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove() {
            this.verb = Verb.REMOVE;
            return this;
        }

        /**
         * 选择 `login` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder login() {
            this.verb = Verb.LOGIN;
            return this;
        }

        /**
         * 选择 `logout` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logout() {
            this.verb = Verb.LOGOUT;
            return this;
        }

        /**
         * 设置 `--channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 写入 `--channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 `--account` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 写入 `--account` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        /**
         * 设置 `--target` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param target 写入 `--target` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder target(String target) {
            this.target = target;
            return this;
        }

        /**
         * 设置 `--kind` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param kind 写入 `--kind` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder kind(String kind) {
            this.kind = kind;
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
         * 设置 `--log-lines` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lines 写入 `--log-lines` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logLines(int lines) {
            this.logLines = lines;
            return this;
        }

        /**
         * 设置 `--remove-delete` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param delete 是否向命令行追加 `--remove-delete` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder removeDelete(boolean delete) {
            this.removeDelete = delete;
            return this;
        }

        /**
         * 设置 `--login-verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--login-verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder loginVerbose(boolean verbose) {
            this.loginVerbose = verbose;
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
         * 校验并复制当前构建器字段，创建独立的 `ChannelsOptions`。
         *
         * @return 按当前字段创建的 ChannelsOptions
         */
        public ChannelsOptions build() {
            return new ChannelsOptions(this);
        }
    }
}
