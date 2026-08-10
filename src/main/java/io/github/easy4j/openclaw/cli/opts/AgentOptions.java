package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw `agent` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AgentOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
     */
    private final String message;
    /**
     * 传给 openclaw 子命令 `--to` 选项的内容；为 null 时通常省略。
     */
    private final String to;
    /**
     * 传给 openclaw 子命令 `--session-id` 选项的内容；为 null 时通常省略。
     */
    private final String sessionId;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 传给 openclaw 子命令 `--thinking` 选项的内容；为 null 时通常省略。
     */
    private final String thinking;
    /**
     * 传给 openclaw 子命令 `--verbose` 选项的内容；为 null 时通常省略。
     */
    private final String verbose;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--reply-to` 选项的内容；为 null 时通常省略。
     */
    private final String replyTo;
    /**
     * 传给 openclaw 子命令 `--reply-channel` 选项的内容；为 null 时通常省略。
     */
    private final String replyChannel;
    /**
     * 传给 openclaw 子命令 `--reply-account` 选项的内容；为 null 时通常省略。
     */
    private final String replyAccount;
    /**
     * 是否向 openclaw 子命令追加 `--local` 开关。
     */
    private final boolean local;
    /**
     * 是否向 openclaw 子命令追加 `--deliver` 开关。
     */
    private final boolean deliver;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final Integer timeoutSeconds;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;

    private AgentOptions(Builder b) {
        this.message = b.message;
        this.to = b.to;
        this.sessionId = b.sessionId;
        this.agent = b.agent;
        this.thinking = b.thinking;
        this.verbose = b.verbose;
        this.channel = b.channel;
        this.replyTo = b.replyTo;
        this.replyChannel = b.replyChannel;
        this.replyAccount = b.replyAccount;
        this.local = b.local;
        this.deliver = b.deliver;
        this.timeoutSeconds = b.timeoutSeconds;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `AgentOptions` 字段。
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
        // --message 在 build() 已保证非空
        out.add("--message");
        out.add(message);
        if (to != null && !to.isEmpty()) {
            out.add("--to");
            out.add(to);
        }
        if (sessionId != null && !sessionId.isEmpty()) {
            out.add("--session-id");
            out.add(sessionId);
        }
        if (agent != null && !agent.isEmpty()) {
            out.add("--agent");
            out.add(agent);
        }
        if (thinking != null && !thinking.isEmpty()) {
            out.add("--thinking");
            out.add(thinking);
        }
        if (verbose != null && !verbose.isEmpty()) {
            out.add("--verbose");
            out.add(verbose);
        }
        if (channel != null && !channel.isEmpty()) {
            out.add("--channel");
            out.add(channel);
        }
        if (replyTo != null && !replyTo.isEmpty()) {
            out.add("--reply-to");
            out.add(replyTo);
        }
        if (replyChannel != null && !replyChannel.isEmpty()) {
            out.add("--reply-channel");
            out.add(replyChannel);
        }
        if (replyAccount != null && !replyAccount.isEmpty()) {
            out.add("--reply-account");
            out.add(replyAccount);
        }
        if (local) {
            out.add("--local");
        }
        if (deliver) {
            out.add("--deliver");
        }
        if (timeoutSeconds != null) {
            out.add("--timeout");
            out.add(Integer.toString(timeoutSeconds));
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 AgentOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 AgentOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
         */
        private String message;
        /**
         * 传给 openclaw 子命令 `--to` 选项的内容；为 null 时通常省略。
         */
        private String to;
        /**
         * 传给 openclaw 子命令 `--session-id` 选项的内容；为 null 时通常省略。
         */
        private String sessionId;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 传给 openclaw 子命令 `--thinking` 选项的内容；为 null 时通常省略。
         */
        private String thinking;
        /**
         * 传给 openclaw 子命令 `--verbose` 选项的内容；为 null 时通常省略。
         */
        private String verbose;
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--reply-to` 选项的内容；为 null 时通常省略。
         */
        private String replyTo;
        /**
         * 传给 openclaw 子命令 `--reply-channel` 选项的内容；为 null 时通常省略。
         */
        private String replyChannel;
        /**
         * 传给 openclaw 子命令 `--reply-account` 选项的内容；为 null 时通常省略。
         */
        private String replyAccount;
        /**
         * 是否向 openclaw 子命令追加 `--local` 开关。
         */
        private boolean local;
        /**
         * 是否向 openclaw 子命令追加 `--deliver` 开关。
         */
        private boolean deliver;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private Integer timeoutSeconds;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

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
         * 设置 `--to` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param to 写入 `--to` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * 设置 `--session-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionId 写入 `--session-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 设置 `--thinking` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 写入 `--thinking` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(ThinkingLevel thinking) {
            Objects.requireNonNull(thinking, "thinking");
            this.thinking = thinking.cliValue();
            return this;
        }

        /**
         * 设置 `--thinking` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 写入 `--thinking` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 写入 `--verbose` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(VerboseLevel verbose) {
            Objects.requireNonNull(verbose, "verbose");
            this.verbose = verbose.cliValue();
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 写入 `--verbose` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(String verbose) {
            this.verbose = verbose;
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
         * 设置 `--reply-to` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyTo 写入 `--reply-to` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        /**
         * 设置 `--reply-channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyChannel 写入 `--reply-channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyChannel(String replyChannel) {
            this.replyChannel = replyChannel;
            return this;
        }

        /**
         * 设置 `--reply-account` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyAccount 写入 `--reply-account` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyAccount(String replyAccount) {
            this.replyAccount = replyAccount;
            return this;
        }

        /**
         * 设置 `--local` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param local 是否向命令行追加 `--local` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder local(boolean local) {
            this.local = local;
            return this;
        }

        /**
         * 设置 `--deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否向命令行追加 `--deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(boolean deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
         * 设置 `--timeout-seconds` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
         * 设置 `--timeout-seconds` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
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
         * 校验并复制当前构建器字段，创建独立的 `AgentOptions`。
         *
         * @return 按当前字段创建的 AgentOptions
         * @throws IllegalStateException 当前连接或生命周期状态不允许调用时抛出
         */
        public AgentOptions build() {
            if (message == null || OpenClawStrings.isBlank(message)) {
                throw new IllegalStateException("agent: --message is required and must be non-blank");
            }
            boolean hasSessionSelector = (to != null && OpenClawStrings.isNotBlank(to))
                    || (sessionId != null && OpenClawStrings.isNotBlank(sessionId))
                    || (agent != null && OpenClawStrings.isNotBlank(agent));
            if (!hasSessionSelector) {
                throw new IllegalStateException(
                        "agent: at least one of --to, --session-id, or --agent is required (non-blank)");
            }
            return new AgentOptions(this);
        }
    }
}
