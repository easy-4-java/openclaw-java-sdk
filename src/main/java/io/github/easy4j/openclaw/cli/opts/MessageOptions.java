package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `message` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class MessageOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--action` 选项的内容；为 null 时通常省略。
     */
    private final List<String> action;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
     */
    private final String account;
    /**
     * 传给 openclaw 子命令 `--target` 选项的内容；为 null 时通常省略。
     */
    private final String target;
    /**
     * 传给 openclaw 子命令 `--targets` 选项的内容；为 null 时通常省略。
     */
    private final List<String> targets;
    /**
     * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
     */
    private final String message;
    /**
     * 传给 openclaw 子命令 `--media` 选项的内容；为 null 时通常省略。
     */
    private final String media;
    /**
     * 传给 openclaw 子命令 `--message-id` 选项的内容；为 null 时通常省略。
     */
    private final String messageId;
    /**
     * 传给 openclaw 子命令 `--emoji` 选项的内容；为 null 时通常省略。
     */
    private final String emoji;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 `--dry-run` 开关。
     */
    private final boolean dryRun;
    /**
     * 是否向 openclaw 子命令追加 `--verbose` 开关。
     */
    private final boolean verbose;
    /**
     * 传给 openclaw 子命令 `--poll-question` 选项的内容；为 null 时通常省略。
     */
    private final String pollQuestion;
    /**
     * 传给 openclaw 子命令 `--poll-options` 选项的内容；为 null 时通常省略。
     */
    private final List<String> pollOptions;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `MessageOptions` 字段。
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
     * 链式构建器，逐项收集 MessageOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 MessageOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--action` 选项的内容；为 null 时通常省略。
         */
        private List<String> action = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
         */
        private String account;
        /**
         * 传给 openclaw 子命令 `--target` 选项的内容；为 null 时通常省略。
         */
        private String target;
        /**
         * 传给 openclaw 子命令 `--targets` 选项的内容；为 null 时通常省略。
         */
        private List<String> targets = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
         */
        private String message;
        /**
         * 传给 openclaw 子命令 `--media` 选项的内容；为 null 时通常省略。
         */
        private String media;
        /**
         * 传给 openclaw 子命令 `--message-id` 选项的内容；为 null 时通常省略。
         */
        private String messageId;
        /**
         * 传给 openclaw 子命令 `--emoji` 选项的内容；为 null 时通常省略。
         */
        private String emoji;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 `--dry-run` 开关。
         */
        private boolean dryRun;
        /**
         * 是否向 openclaw 子命令追加 `--verbose` 开关。
         */
        private boolean verbose;
        /**
         * 传给 openclaw 子命令 `--poll-question` 选项的内容；为 null 时通常省略。
         */
        private String pollQuestion;
        /**
         * 传给 openclaw 子命令 `--poll-options` 选项的内容；为 null 时通常省略。
         */
        private List<String> pollOptions = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--action` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param parts 写入 `--action` 选项的内容
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
         * 设置 `--target` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param target 写入 `--target` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder target(String target) {
            this.target = target;
            return this;
        }

        /**
         * 设置 `--add-target` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param t 写入 `--add-target` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addTarget(String t) {
            if (t != null && OpenClawStrings.isNotBlank(t)) {
                targets.add(t.trim());
            }
            return this;
        }

        /**
         * 设置 `--message` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置 `--media` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param media 写入 `--media` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder media(String media) {
            this.media = media;
            return this;
        }

        /**
         * 设置 `--message-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param messageId 写入 `--message-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * 设置 `--emoji` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param emoji 写入 `--emoji` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder emoji(String emoji) {
            this.emoji = emoji;
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
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * 设置 `--poll-question` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param q 写入 `--poll-question` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pollQuestion(String q) {
            this.pollQuestion = q;
            return this;
        }

        /**
         * 设置 `--poll-option` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param option 写入 `--poll-option` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pollOption(String option) {
            if (option != null && OpenClawStrings.isNotBlank(option)) {
                pollOptions.add(option.trim());
            }
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
         * 校验并复制当前构建器字段，创建独立的 `MessageOptions`。
         *
         * @return 按当前字段创建的 MessageOptions
         */
        public MessageOptions build() {
            return new MessageOptions(this);
        }
    }
}
