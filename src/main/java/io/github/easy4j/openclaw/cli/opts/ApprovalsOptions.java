package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code approvals} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ApprovalsOptions implements CliSubArgs {

    /**
     * 定义执行审批动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示执行审批动作的 {@code get} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        GET,
        /**
         * 表示执行审批动作的 {@code set} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET,
        /**
         * 表示执行审批动作的 {@code allowlist_add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ALLOWLIST_ADD,
        /**
         * 表示执行审批动作的 {@code allowlist_remove} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ALLOWLIST_REMOVE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 目标节点标识；未设置时命令行不包含 {@code --node}。
     */
    private final String node;
    /**
     * 是否向 openclaw 子命令追加 {@code --gateway} 开关。
     */
    private final boolean gateway;
    /**
     * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
     */
    private final String url;
    /**
     * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
     */
    private final String token;
    /**
     * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
     */
    private final String password;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 审批策略文件路径；未设置时命令行不包含 {@code --file}。
     */
    private final String file;
    /**
     * 是否向 openclaw 子命令追加 {@code --stdin} 开关。
     */
    private final boolean stdin;
    /**
     * 待加入或移出审批白名单的匹配模式；未设置时命令行不包含 {@code --allowlist-pattern}。
     */
    private final String allowlistPattern;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code ApprovalsOptions} 字段。
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
     * {@code ApprovalsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.GET;
        /**
         * 目标节点标识；未设置时命令行不包含 {@code --node}。
         */
        private String node;
        /**
         * 是否向 openclaw 子命令追加 {@code --gateway} 开关。
         */
        private boolean gateway;
        /**
         * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
         */
        private String url;
        /**
         * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
         */
        private String token;
        /**
         * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
         */
        private String password;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 审批策略文件路径；未设置时命令行不包含 {@code --file}。
         */
        private String file;
        /**
         * 是否向 openclaw 子命令追加 {@code --stdin} 开关。
         */
        private boolean stdin;
        /**
         * 待加入或移出审批白名单的匹配模式；未设置时命令行不包含 {@code --allowlist-pattern}。
         */
        private String allowlistPattern;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code get} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder get() {
            this.verb = Verb.GET;
            return this;
        }

        /**
         * 选择 {@code set} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() {
            this.verb = Verb.SET;
            return this;
        }

        /**
         * 设置 {@code --file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder file(String path) {
            this.file = path;
            return this;
        }

        /**
         * 设置 {@code --stdin} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param stdin 是否向命令行追加 {@code --stdin} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stdin(boolean stdin) {
            this.stdin = stdin;
            return this;
        }

        /**
         * 设置 {@code --allowlist-add} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 审批白名单匹配模式；作为 {@code --allowlist-add} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistAdd(String pattern) {
            this.verb = Verb.ALLOWLIST_ADD;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
         * 设置 {@code --allowlist-remove} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 审批白名单匹配模式；作为 {@code --allowlist-remove} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistRemove(String pattern) {
            this.verb = Verb.ALLOWLIST_REMOVE;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
         * 设置 {@code --node} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param node 目标节点标识；作为 {@code --node} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder node(String node) {
            this.node = node;
            return this;
        }

        /**
         * 设置 {@code --gateway} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gateway 是否向命令行追加 {@code --gateway} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gateway(boolean gateway) {
            this.gateway = gateway;
            return this;
        }

        /**
         * 设置 {@code --url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 {@code --token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 {@code --password} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password Gateway 或远程服务密码；作为 {@code --password} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
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
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
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
         * 校验并复制当前构建器字段，创建独立的 {@code ApprovalsOptions}。
         *
         * @return 按当前字段创建的 ApprovalsOptions
         */
        public ApprovalsOptions build() {
            return new ApprovalsOptions(this);
        }
    }
}
