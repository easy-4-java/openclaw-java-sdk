package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code openclaw agent} ,consistent with officialdocumentation Options Corresponds to.
 * <p> {@link Builder#build} :{@code --message} Required; {@code --to},{@code --session-id},{@code --agent} .</p>
 * <p>field:{@link ThinkingLevel},{@link VerboseLevel},timeoutseconds {@link Integer}; {@code thinking(String)} / {@code verbose(String)} CLI value.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/agent">agent CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class AgentOptions implements CliSubArgs {

    /**
 * {@code -m} / {@code --message}: agent Required(Gateway embedding).
     */
    private final String message;
    /**
 * {@code -t} / {@code --to}:,Used forsessionkey(session key); {@code --session-id},{@code --agent} .
     */
    private final String to;
    /**
 * {@code --session-id}:session id, {@code --to} sessionkey.
     */
    private final String sessionId;
    /**
 * {@code --agent}: agent id, agent.
     */
    private final String agent;
    /**
 * {@code --thinking}:agent (See {@link ThinkingLevel}), CLI token.
     */
    private final String thinking;
    /**
 * {@code --verbose}:session verbose {@code on} {@code off}(See {@link VerboseLevel}), token.
     */
    private final String verbose;
    /**
 * {@code --channel}:;session(documentation:session).
     */
    private final String channel;
    /**
 * {@code --reply-to}:(channelthread id).
     */
    private final String replyTo;
    /**
 * {@code --reply-channel}:( {@code --channel} See agent documentation Notes).
     */
    private final String replyChannel;
    /**
 * {@code --reply-account}:.
     */
    private final String replyAccount;
    /**
 * {@code --local}:pluginembedding agent, Gateway(documentation:plugin providers/tools/channels).
     */
    private final boolean local;
    /**
 * {@code --deliver}: agent channel/target(only turn ).
     */
    private final boolean deliver;
    /**
 * agent timeout(seconds);{@code null} {@code --timeout}( 600 secondsvalue,Seedocumentation).
     */
    private final Integer timeoutSeconds;
    /**
 * {@code --json}: JSON .
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
 * {@link AgentOptions} builder.
     *
 * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * openclaw agent --to +15555550123 --message "status update" --deliver
     * openclaw agent --agent ops --message "Summarize logs"
     * openclaw agent --session-id 1234 --message "Summarize inbox" --thinking medium
     * openclaw agent --to +15555550123 --message "Trace logs" --verbose on --json
     * openclaw agent --agent ops --message "Generate report" --deliver --reply-channel slack --reply-to "#reports"
     * openclaw agent --agent ops --message "Run locally" --local
 * @return
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
 * {@link AgentOptions} builder;{@link #build} documentationRequiredsession.
     */
    public static final class Builder {

        private String message;
        private String to;
        private String sessionId;
        private String agent;
        private String thinking;
        private String verbose;
        private String channel;
        private String replyTo;
        private String replyChannel;
        private String replyAccount;
        private boolean local;
        private boolean deliver;
        private Integer timeoutSeconds;
        private boolean json;

        /**
 * {@code -m} / {@code --message}:message(Required).
         *
 * @param message message
         * @return this
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
 * {@code -t} / {@code --to}:Used for session .
         *
 * @param to
         * @return this
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
 * {@code --session-id}:session id.
         *
 * @param sessionId session id
         * @return this
         */
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
 * {@code --agent}: agent id,.
         *
 * @param agent agent
         * @return this
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
 * {@code --thinking}:documentation.
         *
 * @param thinking
         * @return this
         */
        public Builder thinking(ThinkingLevel thinking) {
            Objects.requireNonNull(thinking, "thinking");
            this.thinking = thinking.cliValue();
            return this;
        }

        /**
 * {@code --thinking}: CLI value( {@link #thinking(ThinkingLevel)} mutually exclusive).
         *
 * @param thinking token
         * @return this
         */
        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        /**
 * {@code --verbose}:session verbose .
         *
         * @param verbose on / off
         * @return this
         */
        public Builder verbose(VerboseLevel verbose) {
            Objects.requireNonNull(verbose, "verbose");
            this.verbose = verbose.cliValue();
            return this;
        }

        /**
 * {@code --verbose}:value( {@link #verbose(VerboseLevel)} mutually exclusive).
         *
 * @param verbose token, {@code on} {@code off}
         * @return this
         */
        public Builder verbose(String verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
 * {@code --channel}:;session.
         *
 * @param channel
         * @return this
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
 * {@code --reply-to}:.
         *
 * @param replyTo
         * @return this
         */
        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        /**
 * {@code --reply-channel}:.
         *
 * @param replyChannel
         * @return this
         */
        public Builder replyChannel(String replyChannel) {
            this.replyChannel = replyChannel;
            return this;
        }

        /**
 * {@code --reply-account}:.
         *
 * @param replyAccount id
         * @return this
         */
        public Builder replyAccount(String replyAccount) {
            this.replyAccount = replyAccount;
            return this;
        }

        /**
 * {@code --local}:pluginembedding agent.
         *
 * @param local embedding
         * @return this
         */
        public Builder local(boolean local) {
            this.local = local;
            return this;
        }

        /**
 * {@code --deliver}: channel/target.
         *
 * @param deliver
         * @return this
         */
        public Builder deliver(boolean deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
 * {@code --timeout}: agent timeout(seconds).
         *
 * @param timeoutSeconds seconds;{@code null} flag
         * @return this
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
 * {@code --timeout}: agent timeout(seconds).
         *
 * @param timeoutSeconds seconds
         * @return this
         */
        public Builder timeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
 * {@code --json}: JSON .
         *
 * @param json JSON
         * @return this
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
 * {@link AgentOptions}.
 * <p>:{@code message} ;{@code to},{@code sessionId},{@code agent} .</p>
         *
 * @return completionobject
 * @throws IllegalStateException documentationRequired
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
