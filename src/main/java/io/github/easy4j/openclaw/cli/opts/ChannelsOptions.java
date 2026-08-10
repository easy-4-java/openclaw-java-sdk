package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw channels}:Provides, Gateway ,.
 * <p>{@code status --probe} Gateway {@code probeAccount} ;only.
 * {@code add} per-channel flag , {@link Builder#extra(String...)} {@code channels add --help}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/channels">channels CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ChannelsOptions implements CliSubArgs {

    /**
 * channels subcommand:,,.
     */
    public enum Verb {
 /** {@code channels list}:. */
        LIST,
 /** {@code channels status}:,Optional live probe. */
        STATUS,
 /** {@code channels capabilities}:Provides. */
        CAPABILITIES,
 /** {@code channels resolve}: id. */
        RESOLVE,
 /** {@code channels logs}:. */
        LOGS,
 /** {@code channels add}:. */
        ADD,
 /** {@code channels remove}:. */
        REMOVE,
 /** {@code channels login}:( QR stream). */
        LOGIN,
 /** {@code channels logout}:session. */
        LOGOUT
    }

 /** channels subcommand. */
    private final Verb verb;
    /**
 * status:{@code --probe} .
     */
    private final boolean statusProbe;
    /**
 * status / capabilities:{@code --timeout} timeout.
     */
    private final String timeout;
    /**
     * status / capabilities / logs / resolve：{@code --json}。
     */
    private final boolean json;
    /**
 * subcommand:{@code --channel} Provides id.
     */
    private final String channel;
    /**
 * capabilities / resolve:{@code --account} only {@code --channel} .
     */
    private final String account;
    /**
 * capabilities:{@code --target} Discord .
     */
    private final String target;
    /**
 * resolve:{@code --kind} ,.
     */
    private final String kind;
    /**
 * resolve:argument list.
     */
    private final List<String> resolvePositional;
    /**
 * logs:{@code --lines} .
     */
    private final Integer logLines;
    /**
 * remove:{@code --delete} /(per-channel ).
     */
    private final boolean removeDelete;
    /**
 * login:{@code --verbose} .
     */
    private final boolean loginVerbose;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ChannelsOptions(Builder b) {
        this.verb = b.verb;
        this.statusProbe = b.statusProbe;
        this.timeout = b.timeout;
        this.json = b.json;
        this.channel = b.channel;
        this.account = b.account;
        this.target = b.target;
        this.kind = b.kind;
        this.resolvePositional = b.resolvePositional == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.resolvePositional);
        this.logLines = b.logLines;
        this.removeDelete = b.removeDelete;
        this.loginVerbose = b.loginVerbose;
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
        switch (verb) {
            case LIST:
                out.add("list");
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--probe", statusProbe);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case CAPABILITIES:
                out.add("capabilities");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--account", account);
                OpenClawCliArgv.addIfPresent(out, "--target", target);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case RESOLVE:
                out.add("resolve");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--account", account);
                OpenClawCliArgv.addIfPresent(out, "--kind", kind);
                OpenClawCliArgv.addFlag(out, "--json", json);
                out.addAll(resolvePositional);
                break;
            case LOGS:
                out.add("logs");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfNotNull(out, "--lines", logLines);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case ADD:
                out.add("add");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                break;
            case REMOVE:
                out.add("remove");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addFlag(out, "--delete", removeDelete);
                break;
            case LOGIN:
                out.add("login");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addFlag(out, "--verbose", loginVerbose);
                break;
            case LOGOUT:
                out.add("logout");
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ChannelsOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.LIST;
        private boolean statusProbe;
        private String timeout;
        private boolean json;
        private String channel;
        private String account;
        private String target;
        private String kind;
        private List<String> resolvePositional = new ArrayList<>();
        private Integer logLines;
        private boolean removeDelete;
        private boolean loginVerbose;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code channels list}）
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * @return {@code this}（{@code channels status}）
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * @param probe status：{@code --probe}
         * @return {@code this}
         */
        public Builder statusProbe(boolean probe) {
            this.statusProbe = probe;
            return this;
        }

        /**
         * @return {@code this}（{@code channels capabilities}）
         */
        public Builder capabilities() {
            this.verb = Verb.CAPABILITIES;
            return this;
        }

        /**
 * @param positionalNames resolve:
         * @return {@code this}
         */
        public Builder resolve(String... positionalNames) {
            this.verb = Verb.RESOLVE;
            this.resolvePositional = new ArrayList<>();
            if (positionalNames != null) {
                for (String p : positionalNames) {
                    if (p != null && OpenClawStrings.isNotBlank(p)) {
                        resolvePositional.add(p.trim());
                    }
                }
            }
            return this;
        }

        /**
         * @return {@code this}（{@code channels logs}）
         */
        public Builder logs() {
            this.verb = Verb.LOGS;
            return this;
        }

        /**
         * @return {@code this}（{@code channels add}）
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
         * @return {@code this}（{@code channels remove}）
         */
        public Builder remove() {
            this.verb = Verb.REMOVE;
            return this;
        }

        /**
         * @return {@code this}（{@code channels login}）
         */
        public Builder login() {
            this.verb = Verb.LOGIN;
            return this;
        }

        /**
         * @return {@code this}（{@code channels logout}）
         */
        public Builder logout() {
            this.verb = Verb.LOGOUT;
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
         * @param kind resolve：{@code --kind}
         * @return {@code this}
         */
        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        /**
         * @param timeout {@code --timeout}
         * @return {@code this}
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
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
         * @param lines logs：{@code --lines}
         * @return {@code this}
         */
        public Builder logLines(int lines) {
            this.logLines = lines;
            return this;
        }

        /**
         * @param delete remove：{@code --delete}
         * @return {@code this}
         */
        public Builder removeDelete(boolean delete) {
            this.removeDelete = delete;
            return this;
        }

        /**
         * @param verbose login：{@code --verbose}
         * @return {@code this}
         */
        public Builder loginVerbose(boolean verbose) {
            this.loginVerbose = verbose;
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
 * @return {@link ChannelsOptions}
         */
        public ChannelsOptions build() {
            return new ChannelsOptions(this);
        }
    }
}
