package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw acp}: OpenClaw ACP , stdio IDE/, WebSocket Gateway session.
 * <p>Used for" ACP OpenClaw"; MCP channelsession, {@code openclaw mcp serve}.
 * {@code --url} {@code --token} {@code --password},Gateway.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/acp">acp CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class AcpOptions implements CliSubArgs {

    /**
 * {@code acp} : bridge, {@code acp client}.
     */
    public enum Mode {
 /** stdio with Gateway WebSocket ACP . */
        BRIDGE,
 /** ACP : bridge ,Used for IDE . */
        CLIENT
    }

 /** {@code client} subcommand. */
    private final Mode mode;
    /**
 * {@code --url}: Gateway WebSocket URL; {@code gateway.remote.url}(See acp documentation).
     */
    private final String url;
    /**
 * {@code --token}:Gateway token;valueprocess,production {@code --token-file} .
     */
    private final String token;
    /**
 * {@code --token-file}:Gateway token,.
     */
    private final String tokenFile;
    /**
 * {@code --password}:Gatewayauthentication;.
     */
    private final String password;
    /**
 * {@code --password-file}:Gateway.
     */
    private final String passwordFile;
    /**
 * {@code --session}: Gateway sessionkey(agent sessionkey,See acp documentation Selecting agents).
     */
    private final String session;
    /**
 * {@code --session-label}:session; {@code --session} mutually exclusive semanticsSeedocumentation Session mapping.
     */
    private final String sessionLabel;
    /**
 * {@code --require-existing}:sessionkey,.
     */
    private final boolean requireExisting;
    /**
 * {@code --reset-session}: prompt resetkeyCorresponds tosession id(key, transcript).
     */
    private final boolean resetSession;
    /**
 * {@code --no-prefix-cwd}: prompt working directory.
     */
    private final boolean noPrefixCwd;
    /**
 * {@code --provenance}: ACP /(See acp Options).
     */
    private final String provenance;
    /**
 * {@code --verbose} / {@code -v}: stderr .
     */
    private final boolean verbose;
    /**
 * {@code acp client --cwd}:ACP sessionworking directory,.
     */
    private final String cwd;
    /**
 * {@code acp client --server}: ACP subprocess( {@code openclaw}).
     */
    private final String server;
    /**
 * {@code acp client --server-args} argument list,( {@code acp --url ...}).
     */
    private final List<String> serverArgs;
    /**
 * {@code acp client --server-verbose}: ACP process verbose .
     */
    private final boolean serverVerbose;
    /**
 * documentation argv ,.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private AcpOptions(Builder b) {
        this.mode = b.mode;
        this.url = b.url;
        this.token = b.token;
        this.tokenFile = b.tokenFile;
        this.password = b.password;
        this.passwordFile = b.passwordFile;
        this.session = b.session;
        this.sessionLabel = b.sessionLabel;
        this.requireExisting = b.requireExisting;
        this.resetSession = b.resetSession;
        this.noPrefixCwd = b.noPrefixCwd;
        this.provenance = b.provenance;
        this.verbose = b.verbose;
        this.cwd = b.cwd;
        this.server = b.server;
        this.serverArgs = b.serverArgs == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.serverArgs);
        this.serverVerbose = b.serverVerbose;
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
        if (mode == Mode.CLIENT) {
            out.add("client");
            OpenClawCliArgv.addIfPresent(out, "--cwd", cwd);
            OpenClawCliArgv.addIfPresent(out, "--server", server);
            if (!serverArgs.isEmpty()) {
                out.add("--server-args");
                out.addAll(serverArgs);
            }
            OpenClawCliArgv.addFlag(out, "--server-verbose", serverVerbose);
            OpenClawCliArgv.addFlag(out, "--verbose", verbose);
            OpenClawCliArgv.addExtra(out, extra);
            return Collections.unmodifiableList(out);
        }
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--token-file", tokenFile);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--password-file", passwordFile);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--session-label", sessionLabel);
        OpenClawCliArgv.addFlag(out, "--require-existing", requireExisting);
        OpenClawCliArgv.addFlag(out, "--reset-session", resetSession);
        OpenClawCliArgv.addFlag(out, "--no-prefix-cwd", noPrefixCwd);
        OpenClawCliArgv.addIfPresent(out, "--provenance", provenance);
        OpenClawCliArgv.addFlag(out, "--verbose", verbose);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link AcpOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.BRIDGE;
        private String url;
        private String token;
        private String tokenFile;
        private String password;
        private String passwordFile;
        private String session;
        private String sessionLabel;
        private boolean requireExisting;
        private boolean resetSession;
        private boolean noPrefixCwd;
        private String provenance;
        private boolean verbose;
        private String cwd;
        private String server;
        private List<String> serverArgs = new ArrayList<>();
        private boolean serverVerbose;
        private List<String> extra = new ArrayList<>();

        /**
 * @return {@code this}(bridge )
         */
        public Builder bridge() {
            this.mode = Mode.BRIDGE;
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
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * @param tokenFile {@code --token-file}
         * @return {@code this}
         */
        public Builder tokenFile(String tokenFile) {
            this.tokenFile = tokenFile;
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
         * @param passwordFile {@code --password-file}
         * @return {@code this}
         */
        public Builder passwordFile(String passwordFile) {
            this.passwordFile = passwordFile;
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
         * @param sessionLabel {@code --session-label}
         * @return {@code this}
         */
        public Builder sessionLabel(String sessionLabel) {
            this.sessionLabel = sessionLabel;
            return this;
        }

        /**
         * @param requireExisting {@code --require-existing}
         * @return {@code this}
         */
        public Builder requireExisting(boolean requireExisting) {
            this.requireExisting = requireExisting;
            return this;
        }

        /**
         * @param resetSession {@code --reset-session}
         * @return {@code this}
         */
        public Builder resetSession(boolean resetSession) {
            this.resetSession = resetSession;
            return this;
        }

        /**
         * @param noPrefixCwd {@code --no-prefix-cwd}
         * @return {@code this}
         */
        public Builder noPrefixCwd(boolean noPrefixCwd) {
            this.noPrefixCwd = noPrefixCwd;
            return this;
        }

        /**
         * @param provenance {@code --provenance}
         * @return {@code this}
         */
        public Builder provenance(String provenance) {
            this.provenance = provenance;
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
 * @return {@code this}(client subcommand)
         */
        public Builder client() {
            this.mode = Mode.CLIENT;
            return this;
        }

        /**
         * @param cwd client：{@code --cwd}
         * @return {@code this}
         */
        public Builder cwd(String cwd) {
            this.cwd = cwd;
            return this;
        }

        /**
         * @param server client：{@code --server}
         * @return {@code this}
         */
        public Builder server(String server) {
            this.server = server;
            return this;
        }

        /**
 * @param token {@code --server-args}
         * @return {@code this}
         */
        public Builder addServerArg(String token) {
            if (token != null && OpenClawStrings.isNotBlank(token)) {
                serverArgs.add(token.trim());
            }
            return this;
        }

        /**
         * @param serverVerbose client：{@code --server-verbose}
         * @return {@code this}
         */
        public Builder serverVerbose(boolean serverVerbose) {
            this.serverVerbose = serverVerbose;
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
 * @return {@link AcpOptions}
         */
        public AcpOptions build() {
            return new AcpOptions(this);
        }
    }
}
