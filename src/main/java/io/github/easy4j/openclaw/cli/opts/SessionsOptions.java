package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code sessions} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SessionsOptions implements CliSubArgs {

    /**
     * 定义会话管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示会话管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示会话管理动作的 {@code cleanup} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CLEANUP
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 是否向 openclaw 子命令追加 {@code --all-agents} 开关。
     */
    private final boolean allAgents;
    /**
     * 判断会话活跃状态的分钟窗口；未设置时命令行不包含 {@code --active-minutes}。
     */
    private final Integer activeMinutes;
    /**
     * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
     */
    private final boolean verbose;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 审批策略存储文件路径；未设置时命令行不包含 {@code --store}。
     */
    private final String store;
    /**
     * 是否向 openclaw 子命令追加 {@code --cleanup-dry-run} 开关。
     */
    private final boolean cleanupDryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --cleanup-enforce} 开关。
     */
    private final boolean cleanupEnforce;
    /**
     * 是否向 openclaw 子命令追加 {@code --cleanup-fix-missing} 开关。
     */
    private final boolean cleanupFixMissing;
    /**
     * 清理操作使用的活跃会话键；未设置时命令行不包含 {@code --cleanup-active-key}。
     */
    private final String cleanupActiveKey;
    /**
     * 是否向 openclaw 子命令追加 {@code --cleanup-json} 开关。
     */
    private final boolean cleanupJson;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SessionsOptions(Builder b) {
        this.mode = b.mode;
        this.agent = b.agent;
        this.allAgents = b.allAgents;
        this.activeMinutes = b.activeMinutes;
        this.verbose = b.verbose;
        this.json = b.json;
        this.store = b.store;
        this.cleanupDryRun = b.cleanupDryRun;
        this.cleanupEnforce = b.cleanupEnforce;
        this.cleanupFixMissing = b.cleanupFixMissing;
        this.cleanupActiveKey = b.cleanupActiveKey;
        this.cleanupJson = b.cleanupJson;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code SessionsOptions} 字段。
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
        if (mode == Mode.CLEANUP) {
            out.add("cleanup");
            OpenClawCliArgv.addFlag(out, "--dry-run", cleanupDryRun);
            OpenClawCliArgv.addFlag(out, "--enforce", cleanupEnforce);
            OpenClawCliArgv.addFlag(out, "--fix-missing", cleanupFixMissing);
            OpenClawCliArgv.addIfPresent(out, "--active-key", cleanupActiveKey);
            OpenClawCliArgv.addIfPresent(out, "--agent", agent);
            OpenClawCliArgv.addFlag(out, "--all-agents", allAgents);
            OpenClawCliArgv.addIfPresent(out, "--store", store);
            OpenClawCliArgv.addFlag(out, "--json", cleanupJson);
        } else {
            OpenClawCliArgv.addIfPresent(out, "--agent", agent);
            OpenClawCliArgv.addFlag(out, "--all-agents", allAgents);
            OpenClawCliArgv.addIfNotNull(out, "--active", activeMinutes);
            OpenClawCliArgv.addFlag(out, "--verbose", verbose);
            OpenClawCliArgv.addFlag(out, "--json", json);
            OpenClawCliArgv.addIfPresent(out, "--store", store);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code SessionsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.LIST;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 是否向 openclaw 子命令追加 {@code --all-agents} 开关。
         */
        private boolean allAgents;
        /**
         * 判断会话活跃状态的分钟窗口；未设置时命令行不包含 {@code --active-minutes}。
         */
        private Integer activeMinutes;
        /**
         * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
         */
        private boolean verbose;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 审批策略存储文件路径；未设置时命令行不包含 {@code --store}。
         */
        private String store;
        /**
         * 是否向 openclaw 子命令追加 {@code --cleanup-dry-run} 开关。
         */
        private boolean cleanupDryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --cleanup-enforce} 开关。
         */
        private boolean cleanupEnforce;
        /**
         * 是否向 openclaw 子命令追加 {@code --cleanup-fix-missing} 开关。
         */
        private boolean cleanupFixMissing;
        /**
         * 清理操作使用的活跃会话键；未设置时命令行不包含 {@code --cleanup-active-key}。
         */
        private String cleanupActiveKey;
        /**
         * 是否向 openclaw 子命令追加 {@code --cleanup-json} 开关。
         */
        private boolean cleanupJson;
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
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * 选择 {@code cleanup} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanup() {
            this.mode = Mode.CLEANUP;
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
         * 设置 {@code --all-agents} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allAgents 是否向命令行追加 {@code --all-agents} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allAgents(boolean allAgents) {
            this.allAgents = allAgents;
            return this;
        }

        /**
         * 设置 {@code --active-minutes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param minutes 活跃时间窗口，单位为分钟；作为 {@code --active-minutes} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder activeMinutes(int minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
         * 设置 {@code --active-minutes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param minutes 活跃时间窗口，单位为分钟；作为 {@code --active-minutes} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder activeMinutes(Integer minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
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
         * 设置 {@code --store} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param storePath 密钥存储文件路径；作为 {@code --store} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder store(String storePath) {
            this.store = storePath;
            return this;
        }

        /**
         * 设置 {@code --cleanup-dry-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 {@code --cleanup-dry-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupDryRun(boolean dryRun) {
            this.cleanupDryRun = dryRun;
            return this;
        }

        /**
         * 设置 {@code --cleanup-enforce} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param enforce 是否向命令行追加 {@code --cleanup-enforce} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupEnforce(boolean enforce) {
            this.cleanupEnforce = enforce;
            return this;
        }

        /**
         * 设置 {@code --cleanup-fix-missing} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fixMissing 是否向命令行追加 {@code --cleanup-fix-missing} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupFixMissing(boolean fixMissing) {
            this.cleanupFixMissing = fixMissing;
            return this;
        }

        /**
         * 设置 {@code --cleanup-active-key} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionKey 会话路由键
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupActiveKey(String sessionKey) {
            this.cleanupActiveKey = sessionKey;
            return this;
        }

        /**
         * 设置 {@code --cleanup-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupJson(boolean json) {
            this.cleanupJson = json;
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
         * 校验并复制当前构建器字段，创建独立的 {@code SessionsOptions}。
         *
         * @return 按当前字段创建的 SessionsOptions
         */
        public SessionsOptions build() {
            return new SessionsOptions(this);
        }
    }
}
