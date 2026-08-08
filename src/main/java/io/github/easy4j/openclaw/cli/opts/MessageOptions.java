package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw message}:message(send,poll,react,thread,moderation ),Provides.
 * <p> {@code --channel};{@code --target} provider (Telegram chat id,Slack {@code channel:} ).
 * SecretRef action ;/ fail closed.subcommand, {@link Builder#extra(String...)}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/message">message CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class MessageOptions implements CliSubArgs {

    /**
 * subcommand( {@code send},{@code thread},{@code create}), CLI .
     */
    private final List<String> action;
    /**
 * {@code --channel}:discord,slack,telegram id;Required.
     */
    private final String channel;
    /**
 * {@code --account}: id.
     */
    private final String account;
    /**
 * {@code --target}:(channel,session id ,See message documentation Target formats).
     */
    private final String target;
    /**
 * {@code --targets} :.
     */
    private final List<String> targets;
    /**
 * {@code --message}:(send/edit subcommand).
     */
    private final String message;
    /**
 * {@code --media}:/url.
     */
    private final String media;
    /**
 * {@code --message-id}:,message id.
     */
    private final String messageId;
    /**
 * {@code --emoji}: emoji.
     */
    private final String emoji;
    /**
 * {@code --json}:.
     */
    private final boolean json;
    /**
 * {@code --dry-run}:( broadcast).
     */
    private final boolean dryRun;
    /**
 * {@code --verbose}: CLI .
     */
    private final boolean verbose;
    /**
 * {@code --poll-question}:.
     */
    private final String pollQuestion;
    /**
 * {@code --poll-option} :.
     */
    private final List<String> pollOptions;
    /**
 * argv( flag).
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
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@inheritDoc}
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
 * {@link MessageOptions} builder.
     */
    public static final class Builder {
        private List<String> action = new ArrayList<>();
        private String channel;
        private String account;
        private String target;
        private List<String> targets = new ArrayList<>();
        private String message;
        private String media;
        private String messageId;
        private String emoji;
        private boolean json;
        private boolean dryRun;
        private boolean verbose;
        private String pollQuestion;
        private List<String> pollOptions = new ArrayList<>();
        private List<String> extra = new ArrayList<>();

        /**
 * subcommand, {@code action("send")},{@code action("thread", "create")}.
         *
 * @param parts subcommand
         * @return {@code this}
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
         * @param channel {@code --channel}
         * @return {@code this}
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * @param account {@code --account}
         * @return {@code this}
         */
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        /**
         * @param target {@code --target}
         * @return {@code this}
         */
        public Builder target(String target) {
            this.target = target;
            return this;
        }

        /**
 * @param t {@code --targets}
         * @return {@code this}
         */
        public Builder addTarget(String t) {
            if (t != null && OpenClawStrings.isNotBlank(t)) {
                targets.add(t.trim());
            }
            return this;
        }

        /**
         * @param message {@code --message}
         * @return {@code this}
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * @param media {@code --media}
         * @return {@code this}
         */
        public Builder media(String media) {
            this.media = media;
            return this;
        }

        /**
         * @param messageId {@code --message-id}
         * @return {@code this}
         */
        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * @param emoji {@code --emoji}
         * @return {@code this}
         */
        public Builder emoji(String emoji) {
            this.emoji = emoji;
            return this;
        }

        /**
         * @param json {@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * @param dryRun {@code --dry-run}
         * @return {@code this}
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        /**
         * @param verbose {@code --verbose}
         * @return {@code this}
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * @param q {@code --poll-question}
         * @return {@code this}
         */
        public Builder pollQuestion(String q) {
            this.pollQuestion = q;
            return this;
        }

        /**
 * @param option {@code --poll-option}
         * @return {@code this}
         */
        public Builder pollOption(String option) {
            if (option != null && OpenClawStrings.isNotBlank(option)) {
                pollOptions.add(option.trim());
            }
            return this;
        }

        /**
 * appends extra argv token.
         *
 * @param tokens null
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link MessageOptions}
         */
        public MessageOptions build() {
            return new MessageOptions(this);
        }
    }
}
