package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw models}:,,authenticationsubcommand.
 * <p>{@code models status} authentication;{@code --probe} .subcommand {@link Builder#extra(String...)}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/models">models CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class ModelsOptions implements CliSubArgs {

    /**
 * models subcommand(status,list,set,scan,aliases,fallbacks,auth ).
     */
    public enum Mode {
 /** {@code models status}:/authentication,Optional live probe. */
        STATUS,
 /** {@code models list}:directory. */
        LIST,
 /** {@code models set}:. */
        SET,
 /** {@code models scan}:/. */
        SCAN,
        /** {@code models aliases list}。 */
        ALIASES_LIST,
        /** {@code models fallbacks list}。 */
        FALLBACKS_LIST,
 /** {@code models auth add}:authentication. */
        AUTH_ADD,
 /** {@code models auth login}: provider plugin OAuth secretstream. */
        AUTH_LOGIN,
 /** {@code models auth setup-token}:TTY provider token . */
        AUTH_SETUP_TOKEN,
 /** {@code models auth paste-token}: token . */
        AUTH_PASTE_TOKEN
    }

 /** models subcommand. */
    private final Mode mode;
    /**
 * status:{@code --json} .
     */
    private final boolean statusJson;
    /**
 * status:{@code --plain} .
     */
    private final boolean statusPlain;
    /**
 * status:{@code --check} /authentication(documentation:1=,2=).
     */
    private final boolean statusCheck;
    /**
 * status:{@code --probe} authentication.
     */
    private final boolean probe;
    /**
 * status:{@code --probe-provider} provider.
     */
    private final String probeProvider;
    /**
 * status:{@code --probe-profile} profile id .
     */
    private final String probeProfile;
    /**
 * status:{@code --probe-timeout} timeout.
     */
    private final String probeTimeout;
    /**
 * status:{@code --probe-concurrency} concurrency.
     */
    private final String probeConcurrency;
    /**
 * status:{@code --probe-max-tokens} token .
     */
    private final String probeMaxTokens;
    /**
 * status:{@code --agent} agent,authentication.
     */
    private final String agent;
    /**
 * set: {@code provider/model},documentation.
     */
    private final String modelOrAlias;
    /**
 * auth subcommand:{@code --provider} provider id.
     */
    private final String authProvider;
    /**
 * auth login:{@code --set-default} .
     */
    private final boolean authSetDefault;
    /**
 * paste-token:{@code --profile-id} id( {@code :manual}).
     */
    private final String pasteProfileId;
    /**
 * paste-token:{@code --expires-in} ( {@code 365d}).
     */
    private final String pasteExpiresIn;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ModelsOptions(Builder b) {
        this.mode = b.mode;
        this.statusJson = b.statusJson;
        this.statusPlain = b.statusPlain;
        this.statusCheck = b.statusCheck;
        this.probe = b.probe;
        this.probeProvider = b.probeProvider;
        this.probeProfile = b.probeProfile;
        this.probeTimeout = b.probeTimeout;
        this.probeConcurrency = b.probeConcurrency;
        this.probeMaxTokens = b.probeMaxTokens;
        this.agent = b.agent;
        this.modelOrAlias = b.modelOrAlias;
        this.authProvider = b.authProvider;
        this.authSetDefault = b.authSetDefault;
        this.pasteProfileId = b.pasteProfileId;
        this.pasteExpiresIn = b.pasteExpiresIn;
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
        switch (mode) {
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--json", statusJson);
                OpenClawCliArgv.addFlag(out, "--plain", statusPlain);
                OpenClawCliArgv.addFlag(out, "--check", statusCheck);
                OpenClawCliArgv.addFlag(out, "--probe", probe);
                OpenClawCliArgv.addIfPresent(out, "--probe-provider", probeProvider);
                OpenClawCliArgv.addIfPresent(out, "--probe-profile", probeProfile);
                OpenClawCliArgv.addIfPresent(out, "--probe-timeout", probeTimeout);
                OpenClawCliArgv.addIfPresent(out, "--probe-concurrency", probeConcurrency);
                OpenClawCliArgv.addIfPresent(out, "--probe-max-tokens", probeMaxTokens);
                OpenClawCliArgv.addIfPresent(out, "--agent", agent);
                break;
            case LIST:
                out.add("list");
                break;
            case SET:
                out.add("set");
                if (modelOrAlias != null && OpenClawStrings.isNotBlank(modelOrAlias)) {
                    out.add(modelOrAlias.trim());
                }
                break;
            case SCAN:
                out.add("scan");
                break;
            case ALIASES_LIST:
                out.add("aliases");
                out.add("list");
                break;
            case FALLBACKS_LIST:
                out.add("fallbacks");
                out.add("list");
                break;
            case AUTH_ADD:
                out.add("auth");
                out.add("add");
                break;
            case AUTH_LOGIN:
                out.add("auth");
                out.add("login");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                OpenClawCliArgv.addFlag(out, "--set-default", authSetDefault);
                break;
            case AUTH_SETUP_TOKEN:
                out.add("auth");
                out.add("setup-token");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                break;
            case AUTH_PASTE_TOKEN:
                out.add("auth");
                out.add("paste-token");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                OpenClawCliArgv.addIfPresent(out, "--profile-id", pasteProfileId);
                OpenClawCliArgv.addIfPresent(out, "--expires-in", pasteExpiresIn);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ModelsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.STATUS;
        private boolean statusJson;
        private boolean statusPlain;
        private boolean statusCheck;
        private boolean probe;
        private String probeProvider;
        private String probeProfile;
        private String probeTimeout;
        private String probeConcurrency;
        private String probeMaxTokens;
        private String agent;
        private String modelOrAlias;
        private String authProvider;
        private boolean authSetDefault;
        private String pasteProfileId;
        private String pasteExpiresIn;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code models status}）
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * @param json status：{@code --json}
         * @return {@code this}
         */
        public Builder statusJson(boolean json) {
            this.statusJson = json;
            return this;
        }

        /**
         * @param plain status：{@code --plain}
         * @return {@code this}
         */
        public Builder statusPlain(boolean plain) {
            this.statusPlain = plain;
            return this;
        }

        /**
         * @param check status：{@code --check}
         * @return {@code this}
         */
        public Builder statusCheck(boolean check) {
            this.statusCheck = check;
            return this;
        }

        /**
         * @param probe status：{@code --probe}
         * @return {@code this}
         */
        public Builder probe(boolean probe) {
            this.probe = probe;
            return this;
        }

        /**
         * @param probeProvider {@code --probe-provider}
         * @return {@code this}
         */
        public Builder probeProvider(String probeProvider) {
            this.probeProvider = probeProvider;
            return this;
        }

        /**
         * @param probeProfile {@code --probe-profile}
         * @return {@code this}
         */
        public Builder probeProfile(String probeProfile) {
            this.probeProfile = probeProfile;
            return this;
        }

        /**
         * @param probeTimeout {@code --probe-timeout}
         * @return {@code this}
         */
        public Builder probeTimeout(String probeTimeout) {
            this.probeTimeout = probeTimeout;
            return this;
        }

        /**
         * @param probeConcurrency {@code --probe-concurrency}
         * @return {@code this}
         */
        public Builder probeConcurrency(String probeConcurrency) {
            this.probeConcurrency = probeConcurrency;
            return this;
        }

        /**
         * @param probeMaxTokens {@code --probe-max-tokens}
         * @return {@code this}
         */
        public Builder probeMaxTokens(String probeMaxTokens) {
            this.probeMaxTokens = probeMaxTokens;
            return this;
        }

        /**
         * @param agent {@code --agent}
         * @return {@code this}
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * @return {@code this}（{@code models list}）
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
 * @param modelOrAlias set:
         * @return {@code this}
         */
        public Builder set(String modelOrAlias) {
            this.mode = Mode.SET;
            this.modelOrAlias = modelOrAlias;
            return this;
        }

        /**
         * @return {@code this}（{@code models scan}）
         */
        public Builder scan() {
            this.mode = Mode.SCAN;
            return this;
        }

        /**
         * @return {@code this}（{@code models aliases list}）
         */
        public Builder aliasesList() {
            this.mode = Mode.ALIASES_LIST;
            return this;
        }

        /**
         * @return {@code this}（{@code models fallbacks list}）
         */
        public Builder fallbacksList() {
            this.mode = Mode.FALLBACKS_LIST;
            return this;
        }

        /**
         * @return {@code this}（{@code models auth add}）
         */
        public Builder authAdd() {
            this.mode = Mode.AUTH_ADD;
            return this;
        }

        /**
         * @param provider {@code --provider}
         * @return {@code this}
         */
        public Builder authLogin(String provider) {
            this.mode = Mode.AUTH_LOGIN;
            this.authProvider = provider;
            return this;
        }

        /**
         * @param setDefault {@code --set-default}
         * @return {@code this}
         */
        public Builder authSetDefault(boolean setDefault) {
            this.authSetDefault = setDefault;
            return this;
        }

        /**
         * @param provider {@code --provider}
         * @return {@code this}
         */
        public Builder authSetupToken(String provider) {
            this.mode = Mode.AUTH_SETUP_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * @param provider {@code --provider}
         * @return {@code this}
         */
        public Builder authPasteToken(String provider) {
            this.mode = Mode.AUTH_PASTE_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * @param profileId {@code --profile-id}
         * @return {@code this}
         */
        public Builder pasteProfileId(String profileId) {
            this.pasteProfileId = profileId;
            return this;
        }

        /**
         * @param expiresIn {@code --expires-in}
         * @return {@code this}
         */
        public Builder pasteExpiresIn(String expiresIn) {
            this.pasteExpiresIn = expiresIn;
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
 * @return {@link ModelsOptions}
         */
        public ModelsOptions build() {
            return new ModelsOptions(this);
        }
    }
}
