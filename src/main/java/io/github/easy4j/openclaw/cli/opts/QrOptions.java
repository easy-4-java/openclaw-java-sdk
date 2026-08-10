package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw qr}: Gateway pairingQR code setup code( {@code bootstrapToken},Gateway).
 * <p>documentation:{@code --token} {@code --password} ;{@code --remote} {@code gateway.remote.url}
 * {@code gateway.tailscale.mode=serve|funnel};/Tailscale {@code ws://} , {@code wss://} Tailscale Serve/Funnel.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/qr">qr CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class QrOptions implements CliSubArgs {

    /**
 * {@code --remote}: {@code gateway.remote.url}; {@code gateway.tailscale.mode=serve|funnel} Provides URL.
     */
    private final boolean remote;
    /**
 * {@code --url}: Gateway WebSocket URL.
     */
    private final String url;
    /**
 * {@code --public-url}:See URL( {@code --url} Seedocumentation).
     */
    private final String publicUrl;
    /**
 * {@code --token}:streamauthenticationGateway token( {@code --password} ).
     */
    private final String token;
    /**
 * {@code --password}:streamauthenticationGateway( {@code --token} ).
     */
    private final String password;
    /**
 * {@code --setup-code-only}:only setup code, QR .
     */
    private final boolean setupCodeOnly;
    /**
 * {@code --no-ascii}:skipsterminal ASCII QR code.
     */
    private final boolean noAscii;
    /**
 * {@code --json}: JSON( {@code setupCode},{@code gatewayUrl},{@code auth},{@code urlSource} field,Seedocumentation).
     */
    private final boolean json;
    /**
 * documentation, shell .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private QrOptions(Builder b) {
        this.remote = b.remote;
        this.url = b.url;
        this.publicUrl = b.publicUrl;
        this.token = b.token;
        this.password = b.password;
        this.setupCodeOnly = b.setupCodeOnly;
        this.noAscii = b.noAscii;
        this.json = b.json;
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
        OpenClawCliArgv.addFlag(out, "--remote", remote);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--public-url", publicUrl);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addFlag(out, "--setup-code-only", setupCodeOnly);
        OpenClawCliArgv.addFlag(out, "--no-ascii", noAscii);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link QrOptions} builder.
     */
    public static final class Builder {
        private boolean remote;
        private String url;
        private String publicUrl;
        private String token;
        private String password;
        private boolean setupCodeOnly;
        private boolean noAscii;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
         * @param remote {@code --remote}
         * @return {@code this}
         */
        public Builder remote(boolean remote) {
            this.remote = remote;
            return this;
        }

        /**
         * @param url {@code --url}
         * @return {@code this}
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * @param publicUrl {@code --public-url}
         * @return {@code this}
         */
        public Builder publicUrl(String publicUrl) {
            this.publicUrl = publicUrl;
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
         * @param setupCodeOnly {@code --setup-code-only}
         * @return {@code this}
         */
        public Builder setupCodeOnly(boolean setupCodeOnly) {
            this.setupCodeOnly = setupCodeOnly;
            return this;
        }

        /**
         * @param noAscii {@code --no-ascii}
         * @return {@code this}
         */
        public Builder noAscii(boolean noAscii) {
            this.noAscii = noAscii;
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
 * @return {@link QrOptions}
         */
        public QrOptions build() {
            return new QrOptions(this);
        }
    }
}
