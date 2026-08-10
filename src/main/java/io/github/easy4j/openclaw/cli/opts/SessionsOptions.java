package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `sessions` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SessionsOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `cleanup` 协议模式；序列化时使用该固定取值。
         */
        CLEANUP
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 是否向 openclaw 子命令追加 `--all-agents` 开关。
     */
    private final boolean allAgents;
    /**
     * 传给 openclaw 子命令 `--active-minutes` 选项的内容；为 null 时通常省略。
     */
    private final Integer activeMinutes;
    /**
     * 是否向 openclaw 子命令追加 `--verbose` 开关。
     */
    private final boolean verbose;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--store` 选项的内容；为 null 时通常省略。
     */
    private final String store;
    /**
     * 是否向 openclaw 子命令追加 `--cleanup-dry-run` 开关。
     */
    private final boolean cleanupDryRun;
    /**
     * 是否向 openclaw 子命令追加 `--cleanup-enforce` 开关。
     */
    private final boolean cleanupEnforce;
    /**
     * 是否向 openclaw 子命令追加 `--cleanup-fix-missing` 开关。
     */
    private final boolean cleanupFixMissing;
    /**
     * 传给 openclaw 子命令 `--cleanup-active-key` 选项的内容；为 null 时通常省略。
     */
    private final String cleanupActiveKey;
    /**
     * 是否向 openclaw 子命令追加 `--cleanup-json` 开关。
     */
    private final boolean cleanupJson;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `SessionsOptions` 字段。
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
     * 链式构建器，逐项收集 SessionsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SessionsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.LIST;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 是否向 openclaw 子命令追加 `--all-agents` 开关。
         */
        private boolean allAgents;
        /**
         * 传给 openclaw 子命令 `--active-minutes` 选项的内容；为 null 时通常省略。
         */
        private Integer activeMinutes;
        /**
         * 是否向 openclaw 子命令追加 `--verbose` 开关。
         */
        private boolean verbose;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--store` 选项的内容；为 null 时通常省略。
         */
        private String store;
        /**
         * 是否向 openclaw 子命令追加 `--cleanup-dry-run` 开关。
         */
        private boolean cleanupDryRun;
        /**
         * 是否向 openclaw 子命令追加 `--cleanup-enforce` 开关。
         */
        private boolean cleanupEnforce;
        /**
         * 是否向 openclaw 子命令追加 `--cleanup-fix-missing` 开关。
         */
        private boolean cleanupFixMissing;
        /**
         * 传给 openclaw 子命令 `--cleanup-active-key` 选项的内容；为 null 时通常省略。
         */
        private String cleanupActiveKey;
        /**
         * 是否向 openclaw 子命令追加 `--cleanup-json` 开关。
         */
        private boolean cleanupJson;
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
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * 选择 `cleanup` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanup() {
            this.mode = Mode.CLEANUP;
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
         * 设置 `--all-agents` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allAgents 是否向命令行追加 `--all-agents` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allAgents(boolean allAgents) {
            this.allAgents = allAgents;
            return this;
        }

        /**
         * 设置 `--active-minutes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param minutes 写入 `--active-minutes` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder activeMinutes(int minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
         * 设置 `--active-minutes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param minutes 写入 `--active-minutes` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder activeMinutes(Integer minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
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
         * 设置 `--store` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param storePath 写入 `--store` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder store(String storePath) {
            this.store = storePath;
            return this;
        }

        /**
         * 设置 `--cleanup-dry-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 `--cleanup-dry-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupDryRun(boolean dryRun) {
            this.cleanupDryRun = dryRun;
            return this;
        }

        /**
         * 设置 `--cleanup-enforce` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param enforce 是否向命令行追加 `--cleanup-enforce` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupEnforce(boolean enforce) {
            this.cleanupEnforce = enforce;
            return this;
        }

        /**
         * 设置 `--cleanup-fix-missing` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fixMissing 是否向命令行追加 `--cleanup-fix-missing` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupFixMissing(boolean fixMissing) {
            this.cleanupFixMissing = fixMissing;
            return this;
        }

        /**
         * 设置 `--cleanup-active-key` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionKey 会话路由键
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupActiveKey(String sessionKey) {
            this.cleanupActiveKey = sessionKey;
            return this;
        }

        /**
         * 设置 `--cleanup-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cleanupJson(boolean json) {
            this.cleanupJson = json;
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
         * 校验并复制当前构建器字段，创建独立的 `SessionsOptions`。
         *
         * @return 按当前字段创建的 SessionsOptions
         */
        public SessionsOptions build() {
            return new SessionsOptions(this);
        }
    }
}
