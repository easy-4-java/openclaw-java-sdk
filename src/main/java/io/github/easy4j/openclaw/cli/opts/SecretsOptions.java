package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw secrets}: SecretRef,Gatewaysecret,migrate.
 * <p>{@code reload} {@code secrets.reload} RPC;{@code audit --check} Used for CI ; exec Provides dry-run {@code --allow-exec}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/secrets">secrets CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class SecretsOptions implements CliSubArgs {

    /**
 * secrets subcommand:,.
     */
    public enum Mode {
 /** {@code secrets reload}: SecretRef . */
        RELOAD,
 /** {@code secrets audit}:,. */
        AUDIT,
 /** {@code secrets configure}:TTY Optional. */
        CONFIGURE,
 /** {@code secrets apply}: JSON dry-run . */
        APPLY
    }

 /** reload / audit / configure / apply . */
    private final Mode mode;
    /**
 * reload:{@code --url} Gateway WebSocket ( gateway ).
     */
    private final String gatewayUrl;
    /**
 * reload:{@code --token} Gateway token(with documentation gateway RPC ).
     */
    private final String gatewayToken;
    /**
 * reload:{@code --timeout} RPC timeout.
     */
    private final String timeout;
    /**
 * subcommand:{@code --json} (configure TTY JSON).
     */
    private final boolean json;
    /**
 * audit:{@code --check} (PrioritySeedocumentation).
     */
    private final boolean auditCheck;
    /**
 * configure / apply:{@code --allow-exec} SecretRef Provides.
     */
    private final boolean allowExec;
    /**
 * configure:{@code --plan-out} {@code apply --from}.
     */
    private final String planOut;
    /**
 * configure:{@code --apply} ( {@code --yes}).
     */
    private final boolean configureApply;
    /**
 * configure:{@code --yes} skips.
     */
    private final boolean yes;
    /**
 * configure:{@code --providers-only} {@code secrets.providers},map.
     */
    private final boolean providersOnly;
    /**
 * configure:{@code --skip-provider-setup} skipsProvides,map.
     */
    private final boolean skipProviderSetup;
    /**
 * configure:{@code --agent} {@code auth-profiles.json} .
     */
    private final String agent;
    /**
 * apply:{@code --from} .
     */
    private final String applyFrom;
    /**
 * apply:{@code --dry-run} ,;skips exec {@code --allow-exec}.
     */
    private final boolean dryRun;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SecretsOptions(Builder b) {
        this.mode = b.mode;
        this.gatewayUrl = b.gatewayUrl;
        this.gatewayToken = b.gatewayToken;
        this.timeout = b.timeout;
        this.json = b.json;
        this.auditCheck = b.auditCheck;
        this.allowExec = b.allowExec;
        this.planOut = b.planOut;
        this.configureApply = b.configureApply;
        this.yes = b.yes;
        this.providersOnly = b.providersOnly;
        this.skipProviderSetup = b.skipProviderSetup;
        this.agent = b.agent;
        this.applyFrom = b.applyFrom;
        this.dryRun = b.dryRun;
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
            case RELOAD:
                out.add("reload");
                OpenClawCliArgv.addIfPresent(out, "--url", gatewayUrl);
                OpenClawCliArgv.addIfPresent(out, "--token", gatewayToken);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            case AUDIT:
                out.add("audit");
                OpenClawCliArgv.addFlag(out, "--check", auditCheck);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                break;
            case CONFIGURE:
                out.add("configure");
                OpenClawCliArgv.addIfPresent(out, "--plan-out", planOut);
                OpenClawCliArgv.addFlag(out, "--apply", configureApply);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                OpenClawCliArgv.addFlag(out, "--providers-only", providersOnly);
                OpenClawCliArgv.addFlag(out, "--skip-provider-setup", skipProviderSetup);
                OpenClawCliArgv.addIfPresent(out, "--agent", agent);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                break;
            case APPLY:
                out.add("apply");
                OpenClawCliArgv.addIfPresent(out, "--from", applyFrom);
                OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
                OpenClawCliArgv.addFlag(out, "--allow-exec", allowExec);
                OpenClawCliArgv.addFlag(out, "--json", json);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link SecretsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.AUDIT;
        private String gatewayUrl;
        private String gatewayToken;
        private String timeout;
        private boolean json;
        private boolean auditCheck;
        private boolean allowExec;
        private String planOut;
        private boolean configureApply;
        private boolean yes;
        private boolean providersOnly;
        private boolean skipProviderSetup;
        private String agent;
        private String applyFrom;
        private boolean dryRun;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code secrets reload}）
         */
        public Builder reload() {
            this.mode = Mode.RELOAD;
            return this;
        }

        /**
         * @param url reload：{@code --url}
         * @return {@code this}
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * @param token reload：{@code --token}
         * @return {@code this}
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
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
         * @return {@code this}（{@code secrets audit}）
         */
        public Builder audit() {
            this.mode = Mode.AUDIT;
            return this;
        }

        /**
         * @param check audit：{@code --check}
         * @return {@code this}
         */
        public Builder auditCheck(boolean check) {
            this.auditCheck = check;
            return this;
        }

        /**
         * @return {@code this}（{@code secrets configure}）
         */
        public Builder configure() {
            this.mode = Mode.CONFIGURE;
            return this;
        }

        /**
         * @param path configure：{@code --plan-out}
         * @return {@code this}
         */
        public Builder planOut(String path) {
            this.planOut = path;
            return this;
        }

        /**
         * @param apply configure：{@code --apply}
         * @return {@code this}
         */
        public Builder configureApply(boolean apply) {
            this.configureApply = apply;
            return this;
        }

        /**
         * @param yes {@code --yes}
         * @return {@code this}
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
         * @param providersOnly {@code --providers-only}
         * @return {@code this}
         */
        public Builder providersOnly(boolean providersOnly) {
            this.providersOnly = providersOnly;
            return this;
        }

        /**
         * @param skip {@code --skip-provider-setup}
         * @return {@code this}
         */
        public Builder skipProviderSetup(boolean skip) {
            this.skipProviderSetup = skip;
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
 * @param planPath apply:{@code --from}
         * @return {@code this}
         */
        public Builder apply(String planPath) {
            this.mode = Mode.APPLY;
            this.applyFrom = planPath;
            return this;
        }

        /**
         * @param dryRun apply：{@code --dry-run}
         * @return {@code this}
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        /**
         * @param allowExec {@code --allow-exec}
         * @return {@code this}
         */
        public Builder allowExec(boolean allowExec) {
            this.allowExec = allowExec;
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
 * @return {@link SecretsOptions}
         */
        public SecretsOptions build() {
            return new SecretsOptions(this);
        }
    }
}
