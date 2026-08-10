package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw pairing}:approval DM pairingpairing( channels pairingstream).
 * <p>pairing channel {@code --channel};onlypairing {@code approve} channel.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/pairing">pairing CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class PairingOptions implements CliSubArgs {

    /**
 * pairing subcommand:pairing.
     */
    public enum Verb {
 /** {@code pairing list}:. */
        LIST,
 /** {@code pairing approve}:pairing. */
        APPROVE
    }

 /** list approve. */
    private final Verb verb;
    /**
 * list:channel id ( {@code --channel} mutually exclusive);approve {@code --channel} channel.
     */
    private final String channelPositional;
    /**
 * list / approve:{@code --channel} id.
     */
    private final String channel;
    /**
 * list / approve:{@code --account} account id.
     */
    private final String account;
    /**
     * list：{@code --json}。
     */
    private final boolean json;
    /**
 * approve:pairing( channel See {@link #toSubcommandArguments}).
     */
    private final String approveCode;
    /**
 * approve:{@code --notify} message.
     */
    private final boolean notify;
    /**
 * argv.
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
 * {@link PairingOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.LIST;
        private String channelPositional;
        private String channel;
        private String account;
        private boolean json;
        private String approveCode;
        private boolean notify;
        private List<String> extra = new ArrayList<>();

        /**
 * {@code pairing list}( channel ).
         *
         * @return {@code this}
         */
        public Builder list() {
            this.verb = Verb.LIST;
            this.channelPositional = null;
            return this;
        }

        /**
         * {@code pairing list [channel]}。
         *
 * @param channelPositionalOrNull channel ( null)
         * @return {@code this}
         */
        public Builder list(String channelPositionalOrNull) {
            this.verb = Verb.LIST;
            this.channelPositional = channelPositionalOrNull;
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
         * @param json list：{@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
 * {@code pairing approve [channel] <code>};onlypairing channel channel.
         *
 * @param channelPositional null
 * @param code pairing
         * @return {@code this}
         */
        public Builder approve(String channelPositional, String code) {
            this.verb = Verb.APPROVE;
            this.channelPositional = channelPositional;
            this.approveCode = code;
            return this;
        }

        /**
         * @param notify {@code --notify}
         * @return {@code this}
         */
        public Builder notify(boolean notify) {
            this.notify = notify;
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
 * @return {@link PairingOptions}
         */
        public PairingOptions build() {
            return new PairingOptions(this);
        }
    }
}
