package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code reset} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ResetOptions implements CliSubArgs {

    /**
     * 定义重置范围允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Scope {
        /**
         * 表示重置范围的 {@code config} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CONFIG("config"),
        /**
         * 表示重置范围的 {@code config_creds_sessions} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CONFIG_CREDS_SESSIONS("config+creds+sessions"),
        /**
         * 表示重置范围的 {@code full} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        FULL("full");

        /**
         * 枚举常量对应的 CLI 固定参数值；未设置时命令行不包含 {@code --cli-value}。
         */
        private final String cliValue;

        Scope(String cliValue) {
            this.cliValue = cliValue;
        }

        String cliValue() {
            return cliValue;
        }
    }

    /**
     * 重置或查询操作的作用域；未设置时命令行不包含 {@code --scope}。
     */
    private final Scope scope;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 {@code --non-interactive} 开关。
     */
    private final boolean nonInteractive;
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
    private ResetOptions(Builder b) {
        this.scope = b.scope;
        this.yes = b.yes;
        this.nonInteractive = b.nonInteractive;
        this.dryRun = b.dryRun;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ResetOptions} 字段。
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
        if (scope != null) {
            out.add("--scope");
            out.add(scope.cliValue());
        }
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addFlag(out, "--non-interactive", nonInteractive);
        OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code ResetOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 重置或查询操作的作用域；未设置时命令行不包含 {@code --scope}。
         */
        private Scope scope;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 {@code --non-interactive} 开关。
         */
        private boolean nonInteractive;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 {@code --scope} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param scope 重置或查询操作的作用域；作为 {@code --scope} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scope(Scope scope) {
            this.scope = scope;
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
         * 设置 {@code --non-interactive} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 {@code --non-interactive} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
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
         * 校验并复制当前构建器字段，创建独立的 {@code ResetOptions}。
         *
         * @return 按当前字段创建的 ResetOptions
         */
        public ResetOptions build() {
            return new ResetOptions(this);
        }
    }
}
