package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `migrate` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class MigrateOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `default` 协议模式；序列化时使用该固定取值。
         */
        DEFAULT,
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `plan` 协议模式；序列化时使用该固定取值。
         */
        PLAN,
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
     * 传给 openclaw 子命令 `--provider` 选项的内容；为 null 时通常省略。
     */
    private final String provider;
    /**
     * 传给 openclaw 子命令 `--from` 选项的内容；为 null 时通常省略。
     */
    private final String from;
    /**
     * 是否向 openclaw 子命令追加 `--include-secrets` 开关。
     */
    private final boolean includeSecrets;
    /**
     * 是否向 openclaw 子命令追加 `--no-auth-credentials` 开关。
     */
    private final boolean noAuthCredentials;
    /**
     * 是否向 openclaw 子命令追加 `--overwrite` 开关。
     */
    private final boolean overwrite;
    /**
     * 是否向 openclaw 子命令追加 `--dry-run` 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 传给 openclaw 子命令 `--skills` 选项的内容；为 null 时通常省略。
     */
    private final List<String> skills;
    /**
     * 传给 openclaw 子命令 `--plugins` 选项的内容；为 null 时通常省略。
     */
    private final List<String> plugins;
    /**
     * 传给 openclaw 子命令 `--backup-output` 选项的内容；为 null 时通常省略。
     */
    private final String backupOutput;
    /**
     * 是否向 openclaw 子命令追加 `--no-backup` 开关。
     */
    private final boolean noBackup;
    /**
     * 是否向 openclaw 子命令追加 `--force` 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 `--verify-plugin-apps` 开关。
     */
    private final boolean verifyPluginApps;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
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
     * 创建空白构建器，供调用方链式设置 `MigrateOptions` 字段。
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
     * 链式构建器，逐项收集 MigrateOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 MigrateOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.DEFAULT;
        /**
         * 传给 openclaw 子命令 `--provider` 选项的内容；为 null 时通常省略。
         */
        private String provider;
        /**
         * 传给 openclaw 子命令 `--from` 选项的内容；为 null 时通常省略。
         */
        private String from;
        /**
         * 是否向 openclaw 子命令追加 `--include-secrets` 开关。
         */
        private boolean includeSecrets;
        /**
         * 是否向 openclaw 子命令追加 `--no-auth-credentials` 开关。
         */
        private boolean noAuthCredentials;
        /**
         * 是否向 openclaw 子命令追加 `--overwrite` 开关。
         */
        private boolean overwrite;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 传给 openclaw 子命令 `--skills` 选项的内容；为 null 时通常省略。
         */
        private List<String> skills;
        /**
         * 传给 openclaw 子命令 `--plugins` 选项的内容；为 null 时通常省略。
         */
        private List<String> plugins;
        /**
         * 传给 openclaw 子命令 `--backup-output` 选项的内容；为 null 时通常省略。
         */
        private String backupOutput;
        /**
         * 是否向 openclaw 子命令追加 `--no-backup` 开关。
         */
        private boolean noBackup;
        /**
         * 是否向 openclaw 子命令追加 `--force` 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 `--verify-plugin-apps` 开关。
         */
        private boolean verifyPluginApps;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() { this.mode = Mode.LIST; return this; }
        /**
         * 设置 `--plan` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--plan` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plan(String provider) { this.mode = Mode.PLAN; this.provider = provider; return this; }
        /**
         * 设置 `--apply` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--apply` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apply(String provider) { this.mode = Mode.APPLY; this.provider = provider; return this; }
        /**
         * 设置 `--default-action` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--default-action` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultAction(String provider) { this.mode = Mode.DEFAULT; this.provider = provider; return this; }
        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 `--provider` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--provider` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder provider(String provider) { this.provider = provider; return this; }
        /**
         * 设置 `--from` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param from 写入 `--from` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder from(String from) { this.from = from; return this; }
        /**
         * 设置 `--include-secrets` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param includeSecrets 是否向命令行追加 `--include-secrets` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder includeSecrets(boolean includeSecrets) { this.includeSecrets = includeSecrets; return this; }
        /**
         * 设置 `--no-auth-credentials` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noAuthCredentials 是否向命令行追加 `--no-auth-credentials` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noAuthCredentials(boolean noAuthCredentials) { this.noAuthCredentials = noAuthCredentials; return this; }
        /**
         * 设置 `--overwrite` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param overwrite 是否向命令行追加 `--overwrite` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder overwrite(boolean overwrite) { this.overwrite = overwrite; return this; }
        /**
         * 设置 `--dry-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 `--dry-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dryRun(boolean dryRun) { this.dryRun = dryRun; return this; }
        /**
         * 设置 `--yes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 `--yes` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) { this.yes = yes; return this; }
        /**
         * 设置 `--skills` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skills 写入 `--skills` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skills(List<String> skills) { this.skills = skills; return this; }
        /**
         * 设置 `--plugins` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plugins 写入 `--plugins` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plugins(List<String> plugins) { this.plugins = plugins; return this; }
        /**
         * 设置 `--backup-output` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param backupOutput 写入 `--backup-output` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder backupOutput(String backupOutput) { this.backupOutput = backupOutput; return this; }
        /**
         * 设置 `--no-backup` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noBackup 是否向命令行追加 `--no-backup` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noBackup(boolean noBackup) { this.noBackup = noBackup; return this; }
        /**
         * 设置 `--force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) { this.force = force; return this; }
        /**
         * 设置 `--verify-plugin-apps` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verifyPluginApps 是否向命令行追加 `--verify-plugin-apps` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verifyPluginApps(boolean verifyPluginApps) { this.verifyPluginApps = verifyPluginApps; return this; }
        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `MigrateOptions`。
         *
         * @return 按当前字段创建的 MigrateOptions
         */
        public MigrateOptions build() {
            return new MigrateOptions(this);
        }
    }
}
