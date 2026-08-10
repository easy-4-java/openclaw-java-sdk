package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `update` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class UpdateOptions implements CliSubArgs {

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
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS,
        /**
         * 选择 `wizard` 协议模式；序列化时使用该固定取值。
         */
        WIZARD
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--tag` 选项的内容；为 null 时通常省略。
     */
    private final String tag;
    /**
     * 是否向 openclaw 子命令追加 `--dry-run` 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 `--no-restart` 开关。
     */
    private final boolean noRestart;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private UpdateOptions(Builder b) {
        this.mode = b.mode;
        this.channel = b.channel;
        this.tag = b.tag;
        this.dryRun = b.dryRun;
        this.noRestart = b.noRestart;
        this.yes = b.yes;
        this.json = b.json;
        this.timeout = b.timeout;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `UpdateOptions` 字段。
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
            case DEFAULT:
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--tag", tag);
                OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
                OpenClawCliArgv.addFlag(out, "--no-restart", noRestart);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            case WIZARD:
                out.add("wizard");
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 UpdateOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 UpdateOptions。
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
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--tag` 选项的内容；为 null 时通常省略。
         */
        private String tag;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 `--no-restart` 开关。
         */
        private boolean noRestart;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `update` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update() {
            this.mode = Mode.DEFAULT;
            return this;
        }

        /**
         * 设置 `--channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 写入 `--channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 `--tag` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tag 写入 `--tag` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tag(String tag) {
            this.tag = tag;
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
         * 设置 `--no-restart` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noRestart 是否向命令行追加 `--no-restart` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noRestart(boolean noRestart) {
            this.noRestart = noRestart;
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
         * 设置 `--timeout` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout 写入 `--timeout` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 选择 `status` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * 选择 `wizard` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder wizard() {
            this.mode = Mode.WIZARD;
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
         * 校验并复制当前构建器字段，创建独立的 `UpdateOptions`。
         *
         * @return 按当前字段创建的 UpdateOptions
         */
        public UpdateOptions build() {
            return new UpdateOptions(this);
        }
    }
}
