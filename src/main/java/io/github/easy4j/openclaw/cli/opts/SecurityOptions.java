package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `security` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SecurityOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `audit` 协议模式；序列化时使用该固定取值。
         */
        AUDIT
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--deep` 开关。
     */
    private final boolean deep;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 是否向 openclaw 子命令追加 `--fix` 开关。
     */
    private final boolean fix;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SecurityOptions(Builder b) {
        this.mode = b.mode;
        this.deep = b.deep;
        this.password = b.password;
        this.token = b.token;
        this.fix = b.fix;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `SecurityOptions` 字段。
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
        if (mode == Mode.AUDIT) {
            out.add("audit");
            OpenClawCliArgv.addFlag(out, "--deep", deep);
            OpenClawCliArgv.addIfPresent(out, "--password", password);
            OpenClawCliArgv.addIfPresent(out, "--token", token);
            OpenClawCliArgv.addFlag(out, "--fix", fix);
            OpenClawCliArgv.addFlag(out, "--json", json);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 SecurityOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SecurityOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.AUDIT;
        /**
         * 是否向 openclaw 子命令追加 `--deep` 开关。
         */
        private boolean deep;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 是否向 openclaw 子命令追加 `--fix` 开关。
         */
        private boolean fix;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `audit` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder audit() {
            this.mode = Mode.AUDIT;
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
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 `--fix` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fix 是否向命令行追加 `--fix` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder fix(boolean fix) {
            this.fix = fix;
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
         * 校验并复制当前构建器字段，创建独立的 `SecurityOptions`。
         *
         * @return 按当前字段创建的 SecurityOptions
         */
        public SecurityOptions build() {
            return new SecurityOptions(this);
        }
    }
}
