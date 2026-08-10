package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `secrets` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SecretsOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `reload` 协议模式；序列化时使用该固定取值。
         */
        RELOAD,
        /**
         * 选择 `audit` 协议模式；序列化时使用该固定取值。
         */
        AUDIT,
        /**
         * 选择 `configure` 协议模式；序列化时使用该固定取值。
         */
        CONFIGURE,
        /**
         * 选择 `apply` 协议模式；序列化时使用该固定取值。
         */
        APPLY
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--gateway-url` 选项的内容；为 null 时通常省略。
     */
    private final String gatewayUrl;
    /**
     * 传给 openclaw 子命令 `--gateway-token` 选项的内容；为 null 时通常省略。
     */
    private final String gatewayToken;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 `--audit-check` 开关。
     */
    private final boolean auditCheck;
    /**
     * 是否向 openclaw 子命令追加 `--allow-exec` 开关。
     */
    private final boolean allowExec;
    /**
     * 传给 openclaw 子命令 `--plan-out` 选项的内容；为 null 时通常省略。
     */
    private final String planOut;
    /**
     * 是否向 openclaw 子命令追加 `--configure-apply` 开关。
     */
    private final boolean configureApply;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 `--providers-only` 开关。
     */
    private final boolean providersOnly;
    /**
     * 是否向 openclaw 子命令追加 `--skip-provider-setup` 开关。
     */
    private final boolean skipProviderSetup;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 传给 openclaw 子命令 `--apply-from` 选项的内容；为 null 时通常省略。
     */
    private final String applyFrom;
    /**
     * 是否向 openclaw 子命令追加 `--dry-run` 开关。
     */
    private final boolean dryRun;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SecretsOptions(Builder b) {
        this.mode = b.mode;
        this.gatewayUrl = b.gatewayUrl;
        this.gatewayToken = b.gatewayToken;
        this.timeout = b.timeout;
        this.json = b.json;
        this.auditCheck = b.auditCheck;
        this.allowExec = b.allowExec;
        this.planOut = b.planOut;
        this.configureApply = b.configureApply;
        this.yes = b.yes;
        this.providersOnly = b.providersOnly;
        this.skipProviderSetup = b.skipProviderSetup;
        this.agent = b.agent;
        this.applyFrom = b.applyFrom;
        this.dryRun = b.dryRun;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `SecretsOptions` 字段。
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
            case RELOAD:
                out.add("reload");
                OpenClawCliArgv.addIfPresent(out, "--url", gatewayUrl);
                OpenClawCliArgv.addIfPresent(out, "--token", gatewayToken);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case AUDIT:
                out.add("audit");
                OpenClawCliArgv.addFlag(out, "--check", auditCheck);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                break;
            case CONFIGURE:
                out.add("configure");
                OpenClawCliArgv.addIfPresent(out, "--plan-out", planOut);
                OpenClawCliArgv.addFlag(out, "--apply", configureApply);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                OpenClawCliArgv.addFlag(out, "--providers-only", providersOnly);
                OpenClawCliArgv.addFlag(out, "--skip-provider-setup", skipProviderSetup);
                OpenClawCliArgv.addIfPresent(out, "--agent", agent);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                break;
            case APPLY:
                out.add("apply");
                OpenClawCliArgv.addIfPresent(out, "--from", applyFrom);
                OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 SecretsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SecretsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.AUDIT;
        /**
         * 传给 openclaw 子命令 `--gateway-url` 选项的内容；为 null 时通常省略。
         */
        private String gatewayUrl;
        /**
         * 传给 openclaw 子命令 `--gateway-token` 选项的内容；为 null 时通常省略。
         */
        private String gatewayToken;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 `--audit-check` 开关。
         */
        private boolean auditCheck;
        /**
         * 是否向 openclaw 子命令追加 `--allow-exec` 开关。
         */
        private boolean allowExec;
        /**
         * 传给 openclaw 子命令 `--plan-out` 选项的内容；为 null 时通常省略。
         */
        private String planOut;
        /**
         * 是否向 openclaw 子命令追加 `--configure-apply` 开关。
         */
        private boolean configureApply;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 `--providers-only` 开关。
         */
        private boolean providersOnly;
        /**
         * 是否向 openclaw 子命令追加 `--skip-provider-setup` 开关。
         */
        private boolean skipProviderSetup;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 传给 openclaw 子命令 `--apply-from` 选项的内容；为 null 时通常省略。
         */
        private String applyFrom;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `reload` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder reload() {
            this.mode = Mode.RELOAD;
            return this;
        }

        /**
         * 设置 `--gateway-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * 设置 `--gateway-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--gateway-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
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
         * 选择 `audit` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder audit() {
            this.mode = Mode.AUDIT;
            return this;
        }

        /**
         * 设置 `--audit-check` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param check 是否向命令行追加 `--audit-check` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder auditCheck(boolean check) {
            this.auditCheck = check;
            return this;
        }

        /**
         * 选择 `configure` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder configure() {
            this.mode = Mode.CONFIGURE;
            return this;
        }

        /**
         * 设置 `--plan-out` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder planOut(String path) {
            this.planOut = path;
            return this;
        }

        /**
         * 设置 `--configure-apply` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apply 是否向命令行追加 `--configure-apply` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder configureApply(boolean apply) {
            this.configureApply = apply;
            return this;
        }

        /**
         * 设置 `--yes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 `--yes` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
         * 设置 `--providers-only` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param providersOnly 是否向命令行追加 `--providers-only` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder providersOnly(boolean providersOnly) {
            this.providersOnly = providersOnly;
            return this;
        }

        /**
         * 设置 `--skip-provider-setup` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skip 是否向命令行追加 `--skip-provider-setup` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skipProviderSetup(boolean skip) {
            this.skipProviderSetup = skip;
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
         * 设置 `--apply` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param planPath 写入 `--apply` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apply(String planPath) {
            this.mode = Mode.APPLY;
            this.applyFrom = planPath;
            return this;
        }

        /**
         * 设置 `--dry-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 `--dry-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        /**
         * 设置 `--allow-exec` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allowExec 是否向命令行追加 `--allow-exec` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowExec(boolean allowExec) {
            this.allowExec = allowExec;
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
         * 校验并复制当前构建器字段，创建独立的 `SecretsOptions`。
         *
         * @return 按当前字段创建的 SecretsOptions
         */
        public SecretsOptions build() {
            return new SecretsOptions(this);
        }
    }
}
