package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `status-command` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class StatusCommandOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--all` 开关。
     */
    private final boolean all;
    /**
     * 是否向 openclaw 子命令追加 `--deep` 开关。
     */
    private final boolean deep;
    /**
     * 是否向 openclaw 子命令追加 `--usage` 开关。
     */
    private final boolean usage;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;

    /**
 * @param b builder
     */
    private StatusCommandOptions(Builder b) {
        this.all = b.all;
        this.deep = b.deep;
        this.usage = b.usage;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `StatusCommandOptions` 字段。
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
        if (all) {
            out.add("--all");
        }
        if (deep) {
            out.add("--deep");
        }
        if (usage) {
            out.add("--usage");
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 StatusCommandOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 StatusCommandOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 是否向 openclaw 子命令追加 `--all` 开关。
         */
        private boolean all;
        /**
         * 是否向 openclaw 子命令追加 `--deep` 开关。
         */
        private boolean deep;
        /**
         * 是否向 openclaw 子命令追加 `--usage` 开关。
         */
        private boolean usage;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

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
         * 设置 `--deep` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deep 是否向命令行追加 `--deep` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * 设置 `--usage` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param usage 是否向命令行追加 `--usage` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder usage(boolean usage) {
            this.usage = usage;
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
         * 校验并复制当前构建器字段，创建独立的 `StatusCommandOptions`。
         *
         * @return 按当前字段创建的 StatusCommandOptions
         */
        public StatusCommandOptions build() {
            return new StatusCommandOptions(this);
        }
    }
}
