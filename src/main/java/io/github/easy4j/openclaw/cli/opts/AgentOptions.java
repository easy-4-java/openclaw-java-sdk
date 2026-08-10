package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw {@code agent} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AgentOptions implements CliSubArgs {

    /**
     * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
     */
    private final String message;
    /**
     * 消息投递目标；未设置时命令行不包含 {@code --to}。
     */
    private final String to;
    /**
     * 目标会话唯一标识；未设置时命令行不包含 {@code --session-id}。
     */
    private final String sessionId;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 模型思考强度；未设置时命令行不包含 {@code --thinking}。
     */
    private final String thinking;
    /**
     * 是否启用详细日志输出；未设置时命令行不包含 {@code --verbose}。
     */
    private final String verbose;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 回复目标消息标识；未设置时命令行不包含 {@code --reply-to}。
     */
    private final String replyTo;
    /**
     * 回复消息使用的通道；未设置时命令行不包含 {@code --reply-channel}。
     */
    private final String replyChannel;
    /**
     * 回复消息使用的通道账户；未设置时命令行不包含 {@code --reply-account}。
     */
    private final String replyAccount;
    /**
     * 是否向 openclaw 子命令追加 {@code --local} 开关。
     */
    private final boolean local;
    /**
     * 是否向 openclaw 子命令追加 {@code --deliver} 开关。
     */
    private final boolean deliver;
    /**
     * 该请求或进程允许等待的最长时间，单位为秒；超时后主动取消对应任务。
     */
    private final Integer timeoutSeconds;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
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
     * 创建空白构建器，供调用方链式设置 {@code AgentOptions} 字段。
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
     * {@code AgentOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
         */
        private String message;
        /**
         * 消息投递目标；未设置时命令行不包含 {@code --to}。
         */
        private String to;
        /**
         * 目标会话唯一标识；未设置时命令行不包含 {@code --session-id}。
         */
        private String sessionId;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 模型思考强度；未设置时命令行不包含 {@code --thinking}。
         */
        private String thinking;
        /**
         * 是否启用详细日志输出；未设置时命令行不包含 {@code --verbose}。
         */
        private String verbose;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 回复目标消息标识；未设置时命令行不包含 {@code --reply-to}。
         */
        private String replyTo;
        /**
         * 回复消息使用的通道；未设置时命令行不包含 {@code --reply-channel}。
         */
        private String replyChannel;
        /**
         * 回复消息使用的通道账户；未设置时命令行不包含 {@code --reply-account}。
         */
        private String replyAccount;
        /**
         * 是否向 openclaw 子命令追加 {@code --local} 开关。
         */
        private boolean local;
        /**
         * 是否向 openclaw 子命令追加 {@code --deliver} 开关。
         */
        private boolean deliver;
        /**
         * 该请求或进程允许等待的最长时间，单位为秒；超时后主动取消对应任务。
         */
        private Integer timeoutSeconds;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

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
         * 设置 {@code --to} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param to 消息投递目标；作为 {@code --to} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * 设置 {@code --session-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionId 目标会话唯一标识；作为 {@code --session-id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 设置 {@code --thinking} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 模型思考强度；作为 {@code --thinking} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(ThinkingLevel thinking) {
            Objects.requireNonNull(thinking, "thinking");
            this.thinking = thinking.cliValue();
            return this;
        }

        /**
         * 设置 {@code --thinking} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 模型思考强度；作为 {@code --thinking} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        /**
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否启用详细日志输出；作为 {@code --verbose} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(VerboseLevel verbose) {
            Objects.requireNonNull(verbose, "verbose");
            this.verbose = verbose.cliValue();
            return this;
        }

        /**
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否启用详细日志输出；作为 {@code --verbose} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(String verbose) {
            this.verbose = verbose;
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
         * 设置 {@code --reply-to} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyTo 回复目标消息标识；作为 {@code --reply-to} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        /**
         * 设置 {@code --reply-channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyChannel 回复消息使用的通道；作为 {@code --reply-channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyChannel(String replyChannel) {
            this.replyChannel = replyChannel;
            return this;
        }

        /**
         * 设置 {@code --reply-account} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param replyAccount 回复消息使用的通道账户；作为 {@code --reply-account} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder replyAccount(String replyAccount) {
            this.replyAccount = replyAccount;
            return this;
        }

        /**
         * 设置 {@code --local} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param local 是否向命令行追加 {@code --local} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder local(boolean local) {
            this.local = local;
            return this;
        }

        /**
         * 设置 {@code --deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否向命令行追加 {@code --deliver} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(boolean deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
         * 设置 {@code --timeout-seconds} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
         * 设置 {@code --timeout-seconds} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
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
         * 校验并复制当前构建器字段，创建独立的 {@code AgentOptions}。
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
