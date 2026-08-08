package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw tui}:connection Gateway terminal UI.
 * <p>documentation:At startupGatewayauthentication SecretRef(token/password); agent workspace directory
 * {@code agent::...} {@code --session}, agent sessionkey.See TUI .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/tui">tui CLI</a>
 * @see <a href="https://docs.openclaw.ai/web/tui">TUI </a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class TuiOptions implements CliSubArgs {

    /**
 * {@code --url}:Gateway WebSocket (exampleSeedocumentation).
     */
    private final String url;
    /**
 * {@code --token}:Gateway token,with documentationexample {@code openclaw tui --url ... --token &lt;token&gt;} .
     */
    private final String token;
    /**
 * {@code --password}:Gatewayauthentication( token mutually exclusive,SeeGatewayauthenticationdocumentation).
     */
    private final String password;
    /**
 * {@code --session}:sessionkey( {@code main},{@code bugfix}); {@code agent::...} .
     */
    private final String session;
    /**
 * {@code --deliver}:documentationexample {@code --session} .
     */
    private final boolean deliver;
    /**
 * CLI token.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private TuiOptions(Builder b) {
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.session = b.session;
        this.deliver = b.deliver;
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
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addFlag(out, "--deliver", deliver);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link TuiOptions} builder.
     */
    public static final class Builder {
        private String url;
        private String token;
        private String password;
        private String session;
        private boolean deliver;
        private List<String> extra = new ArrayList<>();

        /**
         * @param url {@code --url}
         * @return {@code this}
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * @param password {@code --password}
         * @return {@code this}
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * @param session {@code --session}
         * @return {@code this}
         */
        public Builder session(String session) {
            this.session = session;
            return this;
        }

        /**
         * @param deliver {@code --deliver}
         * @return {@code this}
         */
        public Builder deliver(boolean deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
 * @param tokens CLI token
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link TuiOptions}
         */
        public TuiOptions build() {
            return new TuiOptions(this);
        }
    }
}
