package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code exec-approvals} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ExecApprovalsOptions implements CliSubArgs {

    /**
     * 定义执行审批策略动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示执行审批策略动作的 {@code get} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        GET,
        /**
         * 表示执行审批策略动作的 {@code set} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET,
        /**
         * 表示执行审批策略动作的 {@code allowlist_add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ALLOWLIST_ADD,
        /**
         * 表示执行审批策略动作的 {@code allowlist_remove} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ALLOWLIST_REMOVE,
        /**
         * 表示执行审批策略动作的 {@code default} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DEFAULT
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 审批白名单匹配模式；未设置时命令行不包含 {@code --pattern}。
     */
    private final String pattern;
    /**
     * 目标节点标识；未设置时命令行不包含 {@code --node}。
     */
    private final String node;
    /**
     * 是否向 openclaw 子命令追加 {@code --gateway} 开关。
     */
    private final boolean gateway;
    /**
     * 审批策略文件路径；未设置时命令行不包含 {@code --file}。
     */
    private final String file;
    /**
     * 是否向 openclaw 子命令追加 {@code --stdin} 开关。
     */
    private final boolean stdin;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;

    private ExecApprovalsOptions(Builder b) {
        this.mode = b.mode;
        this.pattern = b.pattern;
        this.node = b.node;
        this.gateway = b.gateway;
        this.file = b.file;
        this.stdin = b.stdin;
        this.agent = b.agent;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ExecApprovalsOptions} 字段。
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
        switch (mode) {
            case GET:
                out.add("get");
                break;
            case SET:
                out.add("set");
                break;
            case ALLOWLIST_ADD:
                out.add("allowlist");
                out.add("add");
                if (pattern != null && !pattern.isEmpty()) {
                    out.add(pattern);
                }
                break;
            case ALLOWLIST_REMOVE:
                out.add("allowlist");
                out.add("remove");
                if (pattern != null && !pattern.isEmpty()) {
                    out.add(pattern);
                }
                break;
            case DEFAULT:
            default:
                // 父命令默认动作：不输出子命令 token
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--node", node);
        OpenClawCliArgv.addFlag(out, "--gateway", gateway);
        OpenClawCliArgv.addIfPresent(out, "--file", file);
        OpenClawCliArgv.addFlag(out, "--stdin", stdin);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code ExecApprovalsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.DEFAULT;
        /**
         * 审批白名单匹配模式；未设置时命令行不包含 {@code --pattern}。
         */
        private String pattern;
        /**
         * 目标节点标识；未设置时命令行不包含 {@code --node}。
         */
        private String node;
        /**
         * 是否向 openclaw 子命令追加 {@code --gateway} 开关。
         */
        private boolean gateway;
        /**
         * 审批策略文件路径；未设置时命令行不包含 {@code --file}。
         */
        private String file;
        /**
         * 是否向 openclaw 子命令追加 {@code --stdin} 开关。
         */
        private boolean stdin;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;

        /**
         * 选择 {@code get} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder get() { this.mode = Mode.GET; return this; }
        /**
         * 选择 {@code set} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() { this.mode = Mode.SET; return this; }
        /**
         * 设置 {@code --allowlist-add} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 审批白名单匹配模式；作为 {@code --allowlist-add} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistAdd(String pattern) { this.mode = Mode.ALLOWLIST_ADD; this.pattern = pattern; return this; }
        /**
         * 设置 {@code --allowlist-remove} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 审批白名单匹配模式；作为 {@code --allowlist-remove} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistRemove(String pattern) { this.mode = Mode.ALLOWLIST_REMOVE; this.pattern = pattern; return this; }
        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 {@code --node} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param node 目标节点标识；作为 {@code --node} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder node(String node) { this.node = node; return this; }
        /**
         * 设置 {@code --gateway} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gateway 是否向命令行追加 {@code --gateway} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gateway(boolean gateway) { this.gateway = gateway; return this; }
        /**
         * 设置 {@code --file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param file 审批策略文件路径；作为 {@code --file} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder file(String file) { this.file = file; return this; }
        /**
         * 设置 {@code --stdin} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param stdin 是否向命令行追加 {@code --stdin} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stdin(boolean stdin) { this.stdin = stdin; return this; }
        /**
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) { this.agent = agent; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ExecApprovalsOptions}。
         *
         * @return 按当前字段创建的 ExecApprovalsOptions
         */
        public ExecApprovalsOptions build() {
            return new ExecApprovalsOptions(this);
        }
    }
}
