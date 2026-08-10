package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `pairing` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class PairingOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `approve` 协议模式；序列化时使用该固定取值。
         */
        APPROVE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--channel-positional` 选项的内容；为 null 时通常省略。
     */
    private final String channelPositional;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
     */
    private final String account;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--approve-code` 选项的内容；为 null 时通常省略。
     */
    private final String approveCode;
    /**
     * 是否向 openclaw 子命令追加 `--notify` 开关。
     */
    private final boolean notify;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private PairingOptions(Builder b) {
        this.verb = b.verb;
        this.channelPositional = b.channelPositional;
        this.channel = b.channel;
        this.account = b.account;
        this.json = b.json;
        this.approveCode = b.approveCode;
        this.notify = b.notify;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `PairingOptions` 字段。
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
        if (verb == Verb.LIST) {
            out.add("list");
            if (channelPositional != null && OpenClawStrings.isNotBlank(channelPositional)) {
                out.add(channelPositional.trim());
            }
            OpenClawCliArgv.addIfPresent(out, "--channel", channel);
            OpenClawCliArgv.addIfPresent(out, "--account", account);
            OpenClawCliArgv.addFlag(out, "--json", json);
        } else {
            out.add("approve");
            OpenClawCliArgv.addIfPresent(out, "--channel", channel);
            OpenClawCliArgv.addIfPresent(out, "--account", account);
            if (channel == null && channelPositional != null && OpenClawStrings.isNotBlank(channelPositional)) {
                out.add(channelPositional.trim());
            }
            if (approveCode != null && OpenClawStrings.isNotBlank(approveCode)) {
                out.add(approveCode.trim());
            }
            OpenClawCliArgv.addFlag(out, "--notify", notify);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 PairingOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 PairingOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.LIST;
        /**
         * 传给 openclaw 子命令 `--channel-positional` 选项的内容；为 null 时通常省略。
         */
        private String channelPositional;
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
         */
        private String account;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--approve-code` 选项的内容；为 null 时通常省略。
         */
        private String approveCode;
        /**
         * 是否向 openclaw 子命令追加 `--notify` 开关。
         */
        private boolean notify;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            this.channelPositional = null;
            return this;
        }

        /**
         * 设置 `--list` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelPositionalOrNull 写入 `--list` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list(String channelPositionalOrNull) {
            this.verb = Verb.LIST;
            this.channelPositional = channelPositionalOrNull;
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
         * 设置 `--account` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 写入 `--account` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
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
         * 设置 `--approve` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelPositional 写入 `--approve` 选项的内容
         * @param code 写入 `--approve` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve(String channelPositional, String code) {
            this.verb = Verb.APPROVE;
            this.channelPositional = channelPositional;
            this.approveCode = code;
            return this;
        }

        /**
         * 设置 `--notify` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param notify 是否向命令行追加 `--notify` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder notify(boolean notify) {
            this.notify = notify;
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
         * 校验并复制当前构建器字段，创建独立的 `PairingOptions`。
         *
         * @return 按当前字段创建的 PairingOptions
         */
        public PairingOptions build() {
            return new PairingOptions(this);
        }
    }
}
