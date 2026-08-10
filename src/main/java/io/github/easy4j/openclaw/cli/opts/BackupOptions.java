package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code backup} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class BackupOptions implements CliSubArgs {

    /**
     * 定义备份管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示备份管理动作的 {@code create} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CREATE,
        /**
         * 表示备份管理动作的 {@code verify} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        VERIFY
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 生成文件的输出目录；未设置时命令行不包含 {@code --output-dir}。
     */
    private final String outputDir;
    /**
     * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 {@code --verify-after-create} 开关。
     */
    private final boolean verifyAfterCreate;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-include-workspace} 开关。
     */
    private final boolean noIncludeWorkspace;
    /**
     * 是否向 openclaw 子命令追加 {@code --only-config} 开关。
     */
    private final boolean onlyConfig;
    /**
     * 待验证的归档文件路径；未设置时命令行不包含 {@code --verify-archive-path}。
     */
    private final String verifyArchivePath;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code BackupOptions} 字段。
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
     * {@code BackupOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.CREATE;
        /**
         * 生成文件的输出目录；未设置时命令行不包含 {@code --output-dir}。
         */
        private String outputDir;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 {@code --verify-after-create} 开关。
         */
        private boolean verifyAfterCreate;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-include-workspace} 开关。
         */
        private boolean noIncludeWorkspace;
        /**
         * 是否向 openclaw 子命令追加 {@code --only-config} 开关。
         */
        private boolean onlyConfig;
        /**
         * 待验证的归档文件路径；未设置时命令行不包含 {@code --verify-archive-path}。
         */
        private String verifyArchivePath;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code create} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder create() {
            this.mode = Mode.CREATE;
            return this;
        }

        /**
         * 设置 {@code --output} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param outputDirOrFile 生成内容的输出目录或文件；作为 {@code --output} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder output(String outputDirOrFile) {
            this.outputDir = outputDirOrFile;
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
         * 设置 {@code --verify-after-create} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verify 是否向命令行追加 {@code --verify-after-create} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verifyAfterCreate(boolean verify) {
            this.verifyAfterCreate = verify;
            return this;
        }

        /**
         * 设置 {@code --no-include-workspace} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noWorkspace 是否向命令行追加 {@code --no-include-workspace} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noIncludeWorkspace(boolean noWorkspace) {
            this.noIncludeWorkspace = noWorkspace;
            return this;
        }

        /**
         * 设置 {@code --only-config} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param onlyConfig 是否向命令行追加 {@code --only-config} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder onlyConfig(boolean onlyConfig) {
            this.onlyConfig = onlyConfig;
            return this;
        }

        /**
         * 设置 {@code --verify} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param archivePath 待校验的归档文件路径；作为 {@code --verify} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verify(String archivePath) {
            this.mode = Mode.VERIFY;
            this.verifyArchivePath = archivePath;
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
         * 校验并复制当前构建器字段，创建独立的 {@code BackupOptions}。
         *
         * @return 按当前字段创建的 BackupOptions
         */
        public BackupOptions build() {
            return new BackupOptions(this);
        }
    }
}
