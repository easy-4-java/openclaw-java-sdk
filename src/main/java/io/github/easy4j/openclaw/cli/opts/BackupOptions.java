package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `backup` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class BackupOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `create` 协议模式；序列化时使用该固定取值。
         */
        CREATE,
        /**
         * 选择 `verify` 协议模式；序列化时使用该固定取值。
         */
        VERIFY
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--output-dir` 选项的内容；为 null 时通常省略。
     */
    private final String outputDir;
    /**
     * 是否向 openclaw 子命令追加 `--dry-run` 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 `--verify-after-create` 开关。
     */
    private final boolean verifyAfterCreate;
    /**
     * 是否向 openclaw 子命令追加 `--no-include-workspace` 开关。
     */
    private final boolean noIncludeWorkspace;
    /**
     * 是否向 openclaw 子命令追加 `--only-config` 开关。
     */
    private final boolean onlyConfig;
    /**
     * 传给 openclaw 子命令 `--verify-archive-path` 选项的内容；为 null 时通常省略。
     */
    private final String verifyArchivePath;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private BackupOptions(Builder b) {
        this.mode = b.mode;
        this.outputDir = b.outputDir;
        this.dryRun = b.dryRun;
        this.json = b.json;
        this.verifyAfterCreate = b.verifyAfterCreate;
        this.noIncludeWorkspace = b.noIncludeWorkspace;
        this.onlyConfig = b.onlyConfig;
        this.verifyArchivePath = b.verifyArchivePath;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `BackupOptions` 字段。
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
        if (mode == Mode.CREATE) {
            out.add("create");
            OpenClawCliArgv.addIfPresent(out, "--output", outputDir);
            OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
            OpenClawCliArgv.addFlag(out, "--json", json);
            OpenClawCliArgv.addFlag(out, "--verify", verifyAfterCreate);
            OpenClawCliArgv.addFlag(out, "--no-include-workspace", noIncludeWorkspace);
            OpenClawCliArgv.addFlag(out, "--only-config", onlyConfig);
        } else {
            out.add("verify");
            if (verifyArchivePath != null && OpenClawStrings.isNotBlank(verifyArchivePath)) {
                out.add(verifyArchivePath.trim());
            }
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 BackupOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 BackupOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.CREATE;
        /**
         * 传给 openclaw 子命令 `--output-dir` 选项的内容；为 null 时通常省略。
         */
        private String outputDir;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 `--verify-after-create` 开关。
         */
        private boolean verifyAfterCreate;
        /**
         * 是否向 openclaw 子命令追加 `--no-include-workspace` 开关。
         */
        private boolean noIncludeWorkspace;
        /**
         * 是否向 openclaw 子命令追加 `--only-config` 开关。
         */
        private boolean onlyConfig;
        /**
         * 传给 openclaw 子命令 `--verify-archive-path` 选项的内容；为 null 时通常省略。
         */
        private String verifyArchivePath;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `create` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder create() {
            this.mode = Mode.CREATE;
            return this;
        }

        /**
         * 设置 `--output` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param outputDirOrFile 写入 `--output` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder output(String outputDirOrFile) {
            this.outputDir = outputDirOrFile;
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
         * 设置 `--verify-after-create` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verify 是否向命令行追加 `--verify-after-create` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verifyAfterCreate(boolean verify) {
            this.verifyAfterCreate = verify;
            return this;
        }

        /**
         * 设置 `--no-include-workspace` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noWorkspace 是否向命令行追加 `--no-include-workspace` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noIncludeWorkspace(boolean noWorkspace) {
            this.noIncludeWorkspace = noWorkspace;
            return this;
        }

        /**
         * 设置 `--only-config` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param onlyConfig 是否向命令行追加 `--only-config` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onlyConfig(boolean onlyConfig) {
            this.onlyConfig = onlyConfig;
            return this;
        }

        /**
         * 设置 `--verify` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param archivePath 写入 `--verify` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verify(String archivePath) {
            this.mode = Mode.VERIFY;
            this.verifyArchivePath = archivePath;
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
         * 校验并复制当前构建器字段，创建独立的 `BackupOptions`。
         *
         * @return 按当前字段创建的 BackupOptions
         */
        public BackupOptions build() {
            return new BackupOptions(this);
        }
    }
}
