package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `uninstall` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class UninstallOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--service` 开关。
     */
    private final boolean service;
    /**
     * 是否向 openclaw 子命令追加 `--state` 开关。
     */
    private final boolean state;
    /**
     * 是否向 openclaw 子命令追加 `--workspace` 开关。
     */
    private final boolean workspace;
    /**
     * 是否向 openclaw 子命令追加 `--app` 开关。
     */
    private final boolean app;
    /**
     * 是否向 openclaw 子命令追加 `--all` 开关。
     */
    private final boolean all;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
     */
    private final boolean nonInteractive;
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
    private UninstallOptions(Builder b) {
        this.service = b.service;
        this.state = b.state;
        this.workspace = b.workspace;
        this.app = b.app;
        this.all = b.all;
        this.yes = b.yes;
        this.nonInteractive = b.nonInteractive;
        this.dryRun = b.dryRun;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `UninstallOptions` 字段。
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
        OpenClawCliArgv.addFlag(out, "--service", service);
        OpenClawCliArgv.addFlag(out, "--state", state);
        OpenClawCliArgv.addFlag(out, "--workspace", workspace);
        OpenClawCliArgv.addFlag(out, "--app", app);
        OpenClawCliArgv.addFlag(out, "--all", all);
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addFlag(out, "--non-interactive", nonInteractive);
        OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 UninstallOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 UninstallOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 是否向 openclaw 子命令追加 `--service` 开关。
         */
        private boolean service;
        /**
         * 是否向 openclaw 子命令追加 `--state` 开关。
         */
        private boolean state;
        /**
         * 是否向 openclaw 子命令追加 `--workspace` 开关。
         */
        private boolean workspace;
        /**
         * 是否向 openclaw 子命令追加 `--app` 开关。
         */
        private boolean app;
        /**
         * 是否向 openclaw 子命令追加 `--all` 开关。
         */
        private boolean all;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
         */
        private boolean nonInteractive;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--service` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param service 是否向命令行追加 `--service` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder service(boolean service) {
            this.service = service;
            return this;
        }

        /**
         * 设置 `--state` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param state 是否向命令行追加 `--state` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder state(boolean state) {
            this.state = state;
            return this;
        }

        /**
         * 设置 `--workspace` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param workspace 是否向命令行追加 `--workspace` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder workspace(boolean workspace) {
            this.workspace = workspace;
            return this;
        }

        /**
         * 设置 `--app` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param app 是否向命令行追加 `--app` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder app(boolean app) {
            this.app = app;
            return this;
        }

        /**
         * 设置 `--all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder all(boolean all) {
            this.all = all;
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
         * 设置 `--non-interactive` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 `--non-interactive` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
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
         * 校验并复制当前构建器字段，创建独立的 `UninstallOptions`。
         *
         * @return 按当前字段创建的 UninstallOptions
         */
        public UninstallOptions build() {
            return new UninstallOptions(this);
        }
    }
}
