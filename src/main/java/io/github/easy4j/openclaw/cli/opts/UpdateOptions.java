package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code update} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class UpdateOptions implements CliSubArgs {

    /**
     * 定义SDK 或 CLI 更新动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示SDK 或 CLI 更新动作的 {@code default} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DEFAULT,
        /**
         * 表示SDK 或 CLI 更新动作的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS,
        /**
         * 表示SDK 或 CLI 更新动作的 {@code wizard} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        WIZARD
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 用于筛选或标记资源的标签；未设置时命令行不包含 {@code --tag}。
     */
    private final String tag;
    /**
     * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-restart} 开关。
     */
    private final boolean noRestart;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code UpdateOptions} 字段。
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
     * {@code UpdateOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.DEFAULT;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 用于筛选或标记资源的标签；未设置时命令行不包含 {@code --tag}。
         */
        private String tag;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-restart} 开关。
         */
        private boolean noRestart;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code update} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update() {
            this.mode = Mode.DEFAULT;
            return this;
        }

        /**
         * 设置 {@code --channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 目标消息通道；作为 {@code --channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 {@code --tag} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tag 用于筛选或标记资源的标签；作为 {@code --tag} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tag(String tag) {
            this.tag = tag;
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
         * 设置 {@code --no-restart} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noRestart 是否向命令行追加 {@code --no-restart} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noRestart(boolean noRestart) {
            this.noRestart = noRestart;
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
         * 设置 {@code --timeout} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout CLI 接受的超时配置；作为 {@code --timeout} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 选择 {@code status} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * 选择 {@code wizard} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder wizard() {
            this.mode = Mode.WIZARD;
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
         * 校验并复制当前构建器字段，创建独立的 {@code UpdateOptions}。
         *
         * @return 按当前字段创建的 UpdateOptions
         */
        public UpdateOptions build() {
            return new UpdateOptions(this);
        }
    }
}
