package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code migrate} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class MigrateOptions implements CliSubArgs {

    /**
     * 定义迁移动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示迁移动作的 {@code default} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DEFAULT,
        /**
         * 表示迁移动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示迁移动作的 {@code plan} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PLAN,
        /**
         * 表示迁移动作的 {@code apply} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        APPLY
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标模型或密钥提供方；未设置时命令行不包含 {@code --provider}。
     */
    private final String provider;
    /**
     * 消息发送方标识；未设置时命令行不包含 {@code --from}。
     */
    private final String from;
    /**
     * 是否向 openclaw 子命令追加 {@code --include-secrets} 开关。
     */
    private final boolean includeSecrets;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-auth-credentials} 开关。
     */
    private final boolean noAuthCredentials;
    /**
     * 是否向 openclaw 子命令追加 {@code --overwrite} 开关。
     */
    private final boolean overwrite;
    /**
     * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 要安装或启用的技能集合；未设置时命令行不包含 {@code --skills}。
     */
    private final List<String> skills;
    /**
     * 要启用或处理的插件集合；未设置时命令行不包含 {@code --plugins}。
     */
    private final List<String> plugins;
    /**
     * 备份文件输出路径；未设置时命令行不包含 {@code --backup-output}。
     */
    private final String backupOutput;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-backup} 开关。
     */
    private final boolean noBackup;
    /**
     * 是否向 openclaw 子命令追加 {@code --force} 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 {@code --verify-plugin-apps} 开关。
     */
    private final boolean verifyPluginApps;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;

    private MigrateOptions(Builder b) {
        this.mode = b.mode;
        this.provider = b.provider;
        this.from = b.from;
        this.includeSecrets = b.includeSecrets;
        this.noAuthCredentials = b.noAuthCredentials;
        this.overwrite = b.overwrite;
        this.dryRun = b.dryRun;
        this.yes = b.yes;
        this.skills = OpenClawLists.copyOf(b.skills);
        this.plugins = OpenClawLists.copyOf(b.plugins);
        this.backupOutput = b.backupOutput;
        this.noBackup = b.noBackup;
        this.force = b.force;
        this.verifyPluginApps = b.verifyPluginApps;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code MigrateOptions} 字段。
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
            case LIST:
                out.add("list");
                break;
            case PLAN:
                out.add("plan");
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
            case APPLY:
                out.add("apply");
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
            case DEFAULT:
            default:
                // 默认动作：可选后接 provider 位置参数
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--from", from);
        OpenClawCliArgv.addFlag(out, "--include-secrets", includeSecrets);
        OpenClawCliArgv.addFlag(out, "--no-auth-credentials", noAuthCredentials);
        OpenClawCliArgv.addFlag(out, "--overwrite", overwrite);
        // --dry-run / --yes / --backup-output / --no-backup / --force 仅在 DEFAULT 与 APPLY 下生效
        if (mode == Mode.DEFAULT || mode == Mode.APPLY) {
            OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
            OpenClawCliArgv.addFlag(out, "--yes", yes);
            OpenClawCliArgv.addIfPresent(out, "--backup-output", backupOutput);
            OpenClawCliArgv.addFlag(out, "--no-backup", noBackup);
            OpenClawCliArgv.addFlag(out, "--force", force);
        }
        OpenClawCliArgv.addRepeatable(out, "--skill", skills);
        OpenClawCliArgv.addRepeatable(out, "--plugin", plugins);
        OpenClawCliArgv.addFlag(out, "--verify-plugin-apps", verifyPluginApps);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code MigrateOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 目标模型或密钥提供方；未设置时命令行不包含 {@code --provider}。
         */
        private String provider;
        /**
         * 消息发送方标识；未设置时命令行不包含 {@code --from}。
         */
        private String from;
        /**
         * 是否向 openclaw 子命令追加 {@code --include-secrets} 开关。
         */
        private boolean includeSecrets;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-auth-credentials} 开关。
         */
        private boolean noAuthCredentials;
        /**
         * 是否向 openclaw 子命令追加 {@code --overwrite} 开关。
         */
        private boolean overwrite;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 要安装或启用的技能集合；未设置时命令行不包含 {@code --skills}。
         */
        private List<String> skills;
        /**
         * 要启用或处理的插件集合；未设置时命令行不包含 {@code --plugins}。
         */
        private List<String> plugins;
        /**
         * 备份文件输出路径；未设置时命令行不包含 {@code --backup-output}。
         */
        private String backupOutput;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-backup} 开关。
         */
        private boolean noBackup;
        /**
         * 是否向 openclaw 子命令追加 {@code --force} 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 {@code --verify-plugin-apps} 开关。
         */
        private boolean verifyPluginApps;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() { this.mode = Mode.LIST; return this; }
        /**
         * 设置 {@code --plan} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --plan} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plan(String provider) { this.mode = Mode.PLAN; this.provider = provider; return this; }
        /**
         * 设置 {@code --apply} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --apply} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apply(String provider) { this.mode = Mode.APPLY; this.provider = provider; return this; }
        /**
         * 设置 {@code --default-action} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --default-action} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultAction(String provider) { this.mode = Mode.DEFAULT; this.provider = provider; return this; }
        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 {@code --provider} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --provider} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder provider(String provider) { this.provider = provider; return this; }
        /**
         * 设置 {@code --from} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param from 消息发送方标识；作为 {@code --from} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder from(String from) { this.from = from; return this; }
        /**
         * 设置 {@code --include-secrets} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param includeSecrets 是否向命令行追加 {@code --include-secrets} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder includeSecrets(boolean includeSecrets) { this.includeSecrets = includeSecrets; return this; }
        /**
         * 设置 {@code --no-auth-credentials} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noAuthCredentials 是否向命令行追加 {@code --no-auth-credentials} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noAuthCredentials(boolean noAuthCredentials) { this.noAuthCredentials = noAuthCredentials; return this; }
        /**
         * 设置 {@code --overwrite} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param overwrite 是否向命令行追加 {@code --overwrite} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder overwrite(boolean overwrite) { this.overwrite = overwrite; return this; }
        /**
         * 设置 {@code --dry-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 {@code --dry-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dryRun(boolean dryRun) { this.dryRun = dryRun; return this; }
        /**
         * 设置 {@code --yes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 {@code --yes} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) { this.yes = yes; return this; }
        /**
         * 设置 {@code --skills} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skills 要安装或启用的技能集合；作为 {@code --skills} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skills(List<String> skills) { this.skills = skills; return this; }
        /**
         * 设置 {@code --plugins} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plugins 要启用或处理的插件集合；作为 {@code --plugins} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plugins(List<String> plugins) { this.plugins = plugins; return this; }
        /**
         * 设置 {@code --backup-output} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param backupOutput 备份文件输出路径；作为 {@code --backup-output} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder backupOutput(String backupOutput) { this.backupOutput = backupOutput; return this; }
        /**
         * 设置 {@code --no-backup} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noBackup 是否向命令行追加 {@code --no-backup} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noBackup(boolean noBackup) { this.noBackup = noBackup; return this; }
        /**
         * 设置 {@code --force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 {@code --force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) { this.force = force; return this; }
        /**
         * 设置 {@code --verify-plugin-apps} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verifyPluginApps 是否向命令行追加 {@code --verify-plugin-apps} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verifyPluginApps(boolean verifyPluginApps) { this.verifyPluginApps = verifyPluginApps; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code MigrateOptions}。
         *
         * @return 按当前字段创建的 MigrateOptions
         */
        public MigrateOptions build() {
            return new MigrateOptions(this);
        }
    }
}
