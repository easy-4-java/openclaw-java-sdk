package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code channels} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ChannelsOptions implements CliSubArgs {

    /**
     * 定义消息通道管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示消息通道管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示消息通道管理动作的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS,
        /**
         * 表示消息通道管理动作的 {@code capabilities} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CAPABILITIES,
        /**
         * 表示消息通道管理动作的 {@code resolve} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RESOLVE,
        /**
         * 表示消息通道管理动作的 {@code logs} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LOGS,
        /**
         * 表示消息通道管理动作的 {@code add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ADD,
        /**
         * 表示消息通道管理动作的 {@code remove} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REMOVE,
        /**
         * 表示消息通道管理动作的 {@code login} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LOGIN,
        /**
         * 表示消息通道管理动作的 {@code logout} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LOGOUT
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 {@code --status-probe} 开关。
     */
    private final boolean statusProbe;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
     */
    private final String account;
    /**
     * 消息或操作的目标地址；未设置时命令行不包含 {@code --target}。
     */
    private final String target;
    /**
     * 目标目录实体类型；未设置时命令行不包含 {@code --kind}。
     */
    private final String kind;
    /**
     * 待解析的联系人或群组名称列表；未设置时命令行不包含 {@code --resolve-positional}。
     */
    private final List<String> resolvePositional;
    /**
     * 读取日志的最大行数；未设置时命令行不包含 {@code --log-lines}。
     */
    private final Integer logLines;
    /**
     * 是否向 openclaw 子命令追加 {@code --remove-delete} 开关。
     */
    private final boolean removeDelete;
    /**
     * 是否向 openclaw 子命令追加 {@code --login-verbose} 开关。
     */
    private final boolean loginVerbose;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code ChannelsOptions} 字段。
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
     * {@code ChannelsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.LIST;
        /**
         * 是否向 openclaw 子命令追加 {@code --status-probe} 开关。
         */
        private boolean statusProbe;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
         */
        private String account;
        /**
         * 消息或操作的目标地址；未设置时命令行不包含 {@code --target}。
         */
        private String target;
        /**
         * 目标目录实体类型；未设置时命令行不包含 {@code --kind}。
         */
        private String kind;
        /**
         * 待解析的联系人或群组名称列表；未设置时命令行不包含 {@code --resolve-positional}。
         */
        private List<String> resolvePositional = new ArrayList<>();
        /**
         * 读取日志的最大行数；未设置时命令行不包含 {@code --log-lines}。
         */
        private Integer logLines;
        /**
         * 是否向 openclaw 子命令追加 {@code --remove-delete} 开关。
         */
        private boolean removeDelete;
        /**
         * 是否向 openclaw 子命令追加 {@code --login-verbose} 开关。
         */
        private boolean loginVerbose;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 选择 {@code status} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * 设置 {@code --status-probe} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probe 是否向命令行追加 {@code --status-probe} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusProbe(boolean probe) {
            this.statusProbe = probe;
            return this;
        }

        /**
         * 选择 {@code capabilities} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder capabilities() {
            this.verb = Verb.CAPABILITIES;
            return this;
        }

        /**
         * 设置 {@code --resolve} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param positionalNames 待解析的联系人或群组名称；作为 {@code --resolve} 的参数
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
         * 选择 {@code logs} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logs() {
            this.verb = Verb.LOGS;
            return this;
        }

        /**
         * 选择 {@code add} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
         * 选择 {@code remove} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove() {
            this.verb = Verb.REMOVE;
            return this;
        }

        /**
         * 选择 {@code login} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder login() {
            this.verb = Verb.LOGIN;
            return this;
        }

        /**
         * 选择 {@code logout} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logout() {
            this.verb = Verb.LOGOUT;
            return this;
        }

        /**
         * 设置 {@code --channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 目标消息通道；作为 {@code --channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 {@code --account} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 目标通道账户标识；作为 {@code --account} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        /**
         * 设置 {@code --target} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param target 消息或操作的目标地址；作为 {@code --target} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder target(String target) {
            this.target = target;
            return this;
        }

        /**
         * 设置 {@code --kind} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param kind 目标目录实体类型；作为 {@code --kind} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        /**
         * 设置 {@code --timeout} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout CLI 接受的超时配置；作为 {@code --timeout} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 {@code --log-lines} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lines 读取日志的最大行数；作为 {@code --log-lines} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder logLines(int lines) {
            this.logLines = lines;
            return this;
        }

        /**
         * 设置 {@code --remove-delete} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param delete 是否向命令行追加 {@code --remove-delete} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder removeDelete(boolean delete) {
            this.removeDelete = delete;
            return this;
        }

        /**
         * 设置 {@code --login-verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --login-verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder loginVerbose(boolean verbose) {
            this.loginVerbose = verbose;
            return this;
        }

        /**
         * 设置 {@code --extra} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 原样追加到生成参数末尾的 CLI 参数列表；作为 {@code --extra} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ChannelsOptions}。
         *
         * @return 按当前字段创建的 ChannelsOptions
         */
        public ChannelsOptions build() {
            return new ChannelsOptions(this);
        }
    }
}
