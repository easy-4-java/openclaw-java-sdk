package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code completion} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CompletionOptions implements CliSubArgs {

    /**
     * 定义Shell 补全类型允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Shell {
        /**
         * 表示Shell 补全类型的 {@code zsh} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ZSH("zsh"),
        /**
         * 表示Shell 补全类型的 {@code bash} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        BASH("bash"),
        /**
         * 表示Shell 补全类型的 {@code powershell} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        POWERSHELL("powershell"),
        /**
         * 表示Shell 补全类型的 {@code fish} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        FISH("fish");

        /**
         * 枚举常量对应的 CLI 固定参数值；未设置时命令行不包含 {@code --cli-value}。
         */
        private final String cliValue;

        /**
         * @param cliValue 传给 completion 子命令的 shell 名称
         */
        Shell(String cliValue) {
            this.cliValue = cliValue;
        }

        /**
         * @return CLI 接受的 shell 名称
         */
        String cliValue() {
            return cliValue;
        }
    }

    /**
     * 生成补全脚本的 Shell 类型；未设置时命令行不包含 {@code --shell}。
     */
    private final Shell shell;
    /**
     * 是否向 openclaw 子命令追加 {@code --install} 开关。
     */
    private final boolean install;
    /**
     * 是否向 openclaw 子命令追加 {@code --write-state} 开关。
     */
    private final boolean writeState;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code CompletionOptions} 字段。
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
     * {@code CompletionOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 生成补全脚本的 Shell 类型；未设置时命令行不包含 {@code --shell}。
         */
        private Shell shell;
        /**
         * 是否向 openclaw 子命令追加 {@code --install} 开关。
         */
        private boolean install;
        /**
         * 是否向 openclaw 子命令追加 {@code --write-state} 开关。
         */
        private boolean writeState;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 {@code --shell} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param shell 生成补全脚本的 Shell 类型；作为 {@code --shell} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder shell(Shell shell) {
            this.shell = shell;
            return this;
        }

        /**
         * 设置 {@code --install} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param install 是否向命令行追加 {@code --install} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(boolean install) {
            this.install = install;
            return this;
        }

        /**
         * 设置 {@code --write-state} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param writeState 是否向命令行追加 {@code --write-state} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder writeState(boolean writeState) {
            this.writeState = writeState;
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
         * 校验并复制当前构建器字段，创建独立的 {@code CompletionOptions}。
         *
         * @return 按当前字段创建的 CompletionOptions
         */
        public CompletionOptions build() {
            return new CompletionOptions(this);
        }
    }
}
