package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code message} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class MessageOptions implements CliSubArgs {

    /**
     * 当前子命令要执行的动作；未设置时命令行不包含 {@code --action}。
     */
    private final List<String> action;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
     */
    private final String account;
    /**
     * 消息或操作的目标地址；未设置时命令行不包含 {@code --target}。
     */
    private final String target;
    /**
     * 消息投递目标列表；未设置时命令行不包含 {@code --targets}。
     */
    private final List<String> targets;
    /**
     * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
     */
    private final String message;
    /**
     * 消息附带的媒体资源；未设置时命令行不包含 {@code --media}。
     */
    private final String media;
    /**
     * 目标消息标识；未设置时命令行不包含 {@code --message-id}。
     */
    private final String messageId;
    /**
     * 智能体身份使用的表情符号；未设置时命令行不包含 {@code --emoji}。
     */
    private final String emoji;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
     */
    private final boolean verbose;
    /**
     * 轮询问题文本；未设置时命令行不包含 {@code --poll-question}。
     */
    private final String pollQuestion;
    /**
     * 轮询问题的候选选项列表；未设置时命令行不包含 {@code --poll-options}。
     */
    private final List<String> pollOptions;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private MessageOptions(Builder b) {
        this.action = b.action == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.action);
        this.channel = b.channel;
        this.account = b.account;
        this.target = b.target;
        this.targets = b.targets == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.targets);
        this.message = b.message;
        this.media = b.media;
        this.messageId = b.messageId;
        this.emoji = b.emoji;
        this.json = b.json;
        this.dryRun = b.dryRun;
        this.verbose = b.verbose;
        this.pollQuestion = b.pollQuestion;
        this.pollOptions = b.pollOptions == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.pollOptions);
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code MessageOptions} 字段。
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
        out.addAll(action);
        OpenClawCliArgv.addIfPresent(out, "--channel", channel);
        OpenClawCliArgv.addIfPresent(out, "--account", account);
        OpenClawCliArgv.addIfPresent(out, "--target", target);
        OpenClawCliArgv.addRepeatable(out, "--targets", targets);
        OpenClawCliArgv.addIfPresent(out, "--message", message);
        OpenClawCliArgv.addIfPresent(out, "--media", media);
        OpenClawCliArgv.addIfPresent(out, "--message-id", messageId);
        OpenClawCliArgv.addIfPresent(out, "--emoji", emoji);
        OpenClawCliArgv.addIfPresent(out, "--poll-question", pollQuestion);
        for (String opt : pollOptions) {
            if (opt != null && OpenClawStrings.isNotBlank(opt)) {
                out.add("--poll-option");
                out.add(opt.trim());
            }
        }
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
        OpenClawCliArgv.addFlag(out, "--verbose", verbose);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code MessageOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前子命令要执行的动作；未设置时命令行不包含 {@code --action}。
         */
        private List<String> action = new ArrayList<>();
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
         */
        private String account;
        /**
         * 消息或操作的目标地址；未设置时命令行不包含 {@code --target}。
         */
        private String target;
        /**
         * 消息投递目标列表；未设置时命令行不包含 {@code --targets}。
         */
        private List<String> targets = new ArrayList<>();
        /**
         * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
         */
        private String message;
        /**
         * 消息附带的媒体资源；未设置时命令行不包含 {@code --media}。
         */
        private String media;
        /**
         * 目标消息标识；未设置时命令行不包含 {@code --message-id}。
         */
        private String messageId;
        /**
         * 智能体身份使用的表情符号；未设置时命令行不包含 {@code --emoji}。
         */
        private String emoji;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 {@code --dry-run} 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
         */
        private boolean verbose;
        /**
         * 轮询问题文本；未设置时命令行不包含 {@code --poll-question}。
         */
        private String pollQuestion;
        /**
         * 轮询问题的候选选项列表；未设置时命令行不包含 {@code --poll-options}。
         */
        private List<String> pollOptions = new ArrayList<>();
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 {@code --action} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param parts 批量执行的动作列表；作为 {@code --action} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder action(String... parts) {
            this.action = new ArrayList<>();
            if (parts != null) {
                for (String p : parts) {
                    if (p != null && OpenClawStrings.isNotBlank(p)) {
                        action.add(p.trim());
                    }
                }
            }
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
         * 设置 {@code --target} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param target 消息或操作的目标地址；作为 {@code --target} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder target(String target) {
            this.target = target;
            return this;
        }

        /**
         * 设置 {@code --add-target} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param t 要追加的投递目标；作为 {@code --add-target} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addTarget(String t) {
            if (t != null && OpenClawStrings.isNotBlank(t)) {
                targets.add(t.trim());
            }
            return this;
        }

        /**
         * 设置 {@code --message} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置 {@code --media} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param media 消息附带的媒体资源；作为 {@code --media} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder media(String media) {
            this.media = media;
            return this;
        }

        /**
         * 设置 {@code --message-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param messageId 目标消息标识；作为 {@code --message-id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * 设置 {@code --emoji} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param emoji 智能体身份使用的表情符号；作为 {@code --emoji} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder emoji(String emoji) {
            this.emoji = emoji;
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
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * 设置 {@code --poll-question} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param q 轮询问题文本；作为 {@code --poll-question} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pollQuestion(String q) {
            this.pollQuestion = q;
            return this;
        }

        /**
         * 设置 {@code --poll-option} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param option 轮询问题的候选选项；作为 {@code --poll-option} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pollOption(String option) {
            if (option != null && OpenClawStrings.isNotBlank(option)) {
                pollOptions.add(option.trim());
            }
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
         * 校验并复制当前构建器字段，创建独立的 {@code MessageOptions}。
         *
         * @return 按当前字段创建的 MessageOptions
         */
        public MessageOptions build() {
            return new MessageOptions(this);
        }
    }
}
