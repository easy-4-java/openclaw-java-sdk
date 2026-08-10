package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code pairing} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class PairingOptions implements CliSubArgs {

    /**
     * 定义设备配对动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示设备配对动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示设备配对动作的 {@code approve} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        APPROVE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 作为位置参数传递的通道名称；未设置时命令行不包含 {@code --channel-positional}。
     */
    private final String channelPositional;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
     */
    private final String account;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 待批准配对请求的验证码；未设置时命令行不包含 {@code --approve-code}。
     */
    private final String approveCode;
    /**
     * 是否向 openclaw 子命令追加 {@code --notify} 开关。
     */
    private final boolean notify;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code PairingOptions} 字段。
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
     * {@code PairingOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.LIST;
        /**
         * 作为位置参数传递的通道名称；未设置时命令行不包含 {@code --channel-positional}。
         */
        private String channelPositional;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
         */
        private String account;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 待批准配对请求的验证码；未设置时命令行不包含 {@code --approve-code}。
         */
        private String approveCode;
        /**
         * 是否向 openclaw 子命令追加 {@code --notify} 开关。
         */
        private boolean notify;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            this.channelPositional = null;
            return this;
        }

        /**
         * 设置 {@code --list} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelPositionalOrNull 用于筛选配对请求的可选通道；作为 {@code --list} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list(String channelPositionalOrNull) {
            this.verb = Verb.LIST;
            this.channelPositional = channelPositionalOrNull;
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
         * 设置 {@code --account} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 目标通道账户标识；作为 {@code --account} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
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
         * 设置 {@code --approve} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelPositional 待批准配对请求所属的通道；作为 {@code --approve} 的参数
         * @param code 配对请求的批准码；作为 {@code --approve} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve(String channelPositional, String code) {
            this.verb = Verb.APPROVE;
            this.channelPositional = channelPositional;
            this.approveCode = code;
            return this;
        }

        /**
         * 设置 {@code --notify} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param notify 是否向命令行追加 {@code --notify} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder notify(boolean notify) {
            this.notify = notify;
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
         * 校验并复制当前构建器字段，创建独立的 {@code PairingOptions}。
         *
         * @return 按当前字段创建的 PairingOptions
         */
        public PairingOptions build() {
            return new PairingOptions(this);
        }
    }
}
