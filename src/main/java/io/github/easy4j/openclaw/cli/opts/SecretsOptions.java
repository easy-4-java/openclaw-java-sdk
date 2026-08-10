package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code secrets} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SecretsOptions implements CliSubArgs {

    /**
     * 定义密钥管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示密钥管理动作的 {@code reload} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RELOAD,
        /**
         * 表示密钥管理动作的 {@code audit} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUDIT,
        /**
         * 表示密钥管理动作的 {@code configure} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CONFIGURE,
        /**
         * 表示密钥管理动作的 {@code apply} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        APPLY
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * Gateway 服务地址；未设置时命令行不包含 {@code --gateway-url}。
     */
    private final String gatewayUrl;
    /**
     * Gateway Bearer Token；未设置时命令行不包含 {@code --gateway-token}。
     */
    private final String gatewayToken;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 {@code --audit-check} 开关。
     */
    private final boolean auditCheck;
    /**
     * 是否向 openclaw 子命令追加 {@code --allow-exec} 开关。
     */
    private final boolean allowExec;
    /**
     * 密钥变更计划的输出文件；未设置时命令行不包含 {@code --plan-out}。
     */
    private final String planOut;
    /**
     * 是否向 openclaw 子命令追加 {@code --configure-apply} 开关。
     */
    private final boolean configureApply;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 {@code --providers-only} 开关。
     */
    private final boolean providersOnly;
    /**
     * 是否向 openclaw 子命令追加 {@code --skip-provider-setup} 开关。
     */
    private final boolean skipProviderSetup;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 待应用的密钥计划文件；未设置时命令行不包含 {@code --apply-from}。
     */
    private final String applyFrom;
    /**
     * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
     */
    private final boolean dryRun;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code SecretsOptions} 字段。
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
     * {@code SecretsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.AUDIT;
        /**
         * Gateway 服务地址；未设置时命令行不包含 {@code --gateway-url}。
         */
        private String gatewayUrl;
        /**
         * Gateway Bearer Token；未设置时命令行不包含 {@code --gateway-token}。
         */
        private String gatewayToken;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 {@code --audit-check} 开关。
         */
        private boolean auditCheck;
        /**
         * 是否向 openclaw 子命令追加 {@code --allow-exec} 开关。
         */
        private boolean allowExec;
        /**
         * 密钥变更计划的输出文件；未设置时命令行不包含 {@code --plan-out}。
         */
        private String planOut;
        /**
         * 是否向 openclaw 子命令追加 {@code --configure-apply} 开关。
         */
        private boolean configureApply;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 {@code --providers-only} 开关。
         */
        private boolean providersOnly;
        /**
         * 是否向 openclaw 子命令追加 {@code --skip-provider-setup} 开关。
         */
        private boolean skipProviderSetup;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 待应用的密钥计划文件；未设置时命令行不包含 {@code --apply-from}。
         */
        private String applyFrom;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code reload} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder reload() {
            this.mode = Mode.RELOAD;
            return this;
        }

        /**
         * 设置 {@code --gateway-url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * 设置 {@code --gateway-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --gateway-token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
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
         * 选择 {@code audit} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder audit() {
            this.mode = Mode.AUDIT;
            return this;
        }

        /**
         * 设置 {@code --audit-check} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param check 是否向命令行追加 {@code --audit-check} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder auditCheck(boolean check) {
            this.auditCheck = check;
            return this;
        }

        /**
         * 选择 {@code configure} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder configure() {
            this.mode = Mode.CONFIGURE;
            return this;
        }

        /**
         * 设置 {@code --plan-out} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder planOut(String path) {
            this.planOut = path;
            return this;
        }

        /**
         * 设置 {@code --configure-apply} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apply 是否向命令行追加 {@code --configure-apply} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder configureApply(boolean apply) {
            this.configureApply = apply;
            return this;
        }

        /**
         * 设置 {@code --yes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 {@code --yes} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
         * 设置 {@code --providers-only} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param providersOnly 是否向命令行追加 {@code --providers-only} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder providersOnly(boolean providersOnly) {
            this.providersOnly = providersOnly;
            return this;
        }

        /**
         * 设置 {@code --skip-provider-setup} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skip 是否向命令行追加 {@code --skip-provider-setup} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skipProviderSetup(boolean skip) {
            this.skipProviderSetup = skip;
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
         * 设置 {@code --apply} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param planPath 待应用的变更计划文件路径；作为 {@code --apply} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apply(String planPath) {
            this.mode = Mode.APPLY;
            this.applyFrom = planPath;
            return this;
        }

        /**
         * 设置 {@code --dry-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 {@code --dry-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        /**
         * 设置 {@code --allow-exec} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allowExec 是否向命令行追加 {@code --allow-exec} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowExec(boolean allowExec) {
            this.allowExec = allowExec;
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
         * 校验并复制当前构建器字段，创建独立的 {@code SecretsOptions}。
         *
         * @return 按当前字段创建的 SecretsOptions
         */
        public SecretsOptions build() {
            return new SecretsOptions(this);
        }
    }
}
