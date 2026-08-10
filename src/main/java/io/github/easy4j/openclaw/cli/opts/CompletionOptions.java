package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `completion` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CompletionOptions implements CliSubArgs {

    /**
     * `Shell` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Shell {
        /**
         * 选择 `zsh` 协议模式；序列化时使用该固定取值。
         */
        ZSH("zsh"),
        /**
         * 选择 `bash` 协议模式；序列化时使用该固定取值。
         */
        BASH("bash"),
        /**
         * 选择 `powershell` 协议模式；序列化时使用该固定取值。
         */
        POWERSHELL("powershell"),
        /**
         * 选择 `fish` 协议模式；序列化时使用该固定取值。
         */
        FISH("fish");

        /**
         * 传给 openclaw 子命令 `--cli-value` 选项的内容；为 null 时通常省略。
         */
        private final String cliValue;

        /**
 * @param cliValue null shell
         */
        Shell(String cliValue) {
            this.cliValue = cliValue;
        }

        /**
 * @return CLI shell token
         */
        String cliValue() {
            return cliValue;
        }
    }

    /**
     * 传给 openclaw 子命令 `--shell` 选项的内容；为 null 时通常省略。
     */
    private final Shell shell;
    /**
     * 是否向 openclaw 子命令追加 `--install` 开关。
     */
    private final boolean install;
    /**
     * 是否向 openclaw 子命令追加 `--write-state` 开关。
     */
    private final boolean writeState;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private CompletionOptions(Builder b) {
        this.shell = b.shell;
        this.install = b.install;
        this.writeState = b.writeState;
        this.yes = b.yes;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `CompletionOptions` 字段。
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
        if (shell != null) {
            out.add("--shell");
            out.add(shell.cliValue());
        }
        OpenClawCliArgv.addFlag(out, "--install", install);
        OpenClawCliArgv.addFlag(out, "--write-state", writeState);
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 CompletionOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 CompletionOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--shell` 选项的内容；为 null 时通常省略。
         */
        private Shell shell;
        /**
         * 是否向 openclaw 子命令追加 `--install` 开关。
         */
        private boolean install;
        /**
         * 是否向 openclaw 子命令追加 `--write-state` 开关。
         */
        private boolean writeState;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--shell` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param shell 写入 `--shell` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder shell(Shell shell) {
            this.shell = shell;
            return this;
        }

        /**
         * 设置 `--install` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param install 是否向命令行追加 `--install` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(boolean install) {
            this.install = install;
            return this;
        }

        /**
         * 设置 `--write-state` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param writeState 是否向命令行追加 `--write-state` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder writeState(boolean writeState) {
            this.writeState = writeState;
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
         * 校验并复制当前构建器字段，创建独立的 `CompletionOptions`。
         *
         * @return 按当前字段创建的 CompletionOptions
         */
        public CompletionOptions build() {
            return new CompletionOptions(this);
        }
    }
}
