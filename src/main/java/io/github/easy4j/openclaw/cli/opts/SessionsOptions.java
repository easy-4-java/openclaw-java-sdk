package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw sessions}: agent session,session(repair).
 * <p>{@code cleanup} {@code session.maintenance} ; cron (See cron documentation).{@code --all-agents} agent store.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/sessions">sessions CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class SessionsOptions implements CliSubArgs {

    /**
 * session, {@code sessions cleanup} subcommand.
     */
    public enum Mode {
        /**
 * :subcommand token,with documentationexample {@code openclaw sessions} .
         */
        LIST,
 /** {@code sessions cleanup}:. */
        CLEANUP
    }

 /** LIST CLEANUP. */
    private final Mode mode;
    /**
 * {@code --agent}: agent session store.
     */
    private final String agent;
    /**
 * {@code --all-agents}: agent .
     */
    private final boolean allAgents;
    /**
 * list:{@code --active} session(value).
     */
    private final Integer activeMinutes;
    /**
 * list:{@code --verbose} .
     */
    private final boolean verbose;
    /**
 * list:{@code --json} session store .
     */
    private final boolean json;
    /**
 * list / cleanup:{@code --store} {@code sessions.json} ( {@code --agent}/{@code --all-agents} ,Seedocumentation).
     */
    private final String store;
    /**
 * cleanup:{@code --dry-run} ,.
     */
    private final boolean cleanupDryRun;
    /**
 * cleanup:{@code --enforce} {@code session.maintenance.mode=warn} .
     */
    private final boolean cleanupEnforce;
    /**
 * cleanup:{@code --fix-missing} transcript .
     */
    private final boolean cleanupFixMissing;
    /**
 * cleanup:{@code --active-key} sessionkey.
     */
    private final String cleanupActiveKey;
    /**
 * cleanup:{@code --json} store .
     */
    private final boolean cleanupJson;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SessionsOptions(Builder b) {
        this.mode = b.mode;
        this.agent = b.agent;
        this.allAgents = b.allAgents;
        this.activeMinutes = b.activeMinutes;
        this.verbose = b.verbose;
        this.json = b.json;
        this.store = b.store;
        this.cleanupDryRun = b.cleanupDryRun;
        this.cleanupEnforce = b.cleanupEnforce;
        this.cleanupFixMissing = b.cleanupFixMissing;
        this.cleanupActiveKey = b.cleanupActiveKey;
        this.cleanupJson = b.cleanupJson;
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
        if (mode == Mode.CLEANUP) {
            out.add("cleanup");
            OpenClawCliArgv.addFlag(out, "--dry-run", cleanupDryRun);
            OpenClawCliArgv.addFlag(out, "--enforce", cleanupEnforce);
            OpenClawCliArgv.addFlag(out, "--fix-missing", cleanupFixMissing);
            OpenClawCliArgv.addIfPresent(out, "--active-key", cleanupActiveKey);
            OpenClawCliArgv.addIfPresent(out, "--agent", agent);
            OpenClawCliArgv.addFlag(out, "--all-agents", allAgents);
            OpenClawCliArgv.addIfPresent(out, "--store", store);
            OpenClawCliArgv.addFlag(out, "--json", cleanupJson);
        } else {
            OpenClawCliArgv.addIfPresent(out, "--agent", agent);
            OpenClawCliArgv.addFlag(out, "--all-agents", allAgents);
            OpenClawCliArgv.addIfNotNull(out, "--active", activeMinutes);
            OpenClawCliArgv.addFlag(out, "--verbose", verbose);
            OpenClawCliArgv.addFlag(out, "--json", json);
            OpenClawCliArgv.addIfPresent(out, "--store", store);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link SessionsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private String agent;
        private boolean allAgents;
        private Integer activeMinutes;
        private boolean verbose;
        private boolean json;
        private String store;
        private boolean cleanupDryRun;
        private boolean cleanupEnforce;
        private boolean cleanupFixMissing;
        private String cleanupActiveKey;
        private boolean cleanupJson;
        private List<String> extra = new ArrayList<>();

        /**
 * session(subcommand token).
         *
         * @return {@code this}
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * {@code sessions cleanup}。
         *
         * @return {@code this}
         */
        public Builder cleanup() {
            this.mode = Mode.CLEANUP;
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
         * @param allAgents {@code --all-agents}
         * @return {@code this}
         */
        public Builder allAgents(boolean allAgents) {
            this.allAgents = allAgents;
            return this;
        }

        /**
 * {@code --active}:.
         *
 * @param minutes
         * @return {@code this}
         */
        public Builder activeMinutes(int minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
 * @param minutes {@code --active}( null)
         * @return {@code this}
         */
        public Builder activeMinutes(Integer minutes) {
            this.activeMinutes = minutes;
            return this;
        }

        /**
         * @param verbose list：{@code --verbose}
         * @return {@code this}
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
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
         * @param storePath {@code --store}
         * @return {@code this}
         */
        public Builder store(String storePath) {
            this.store = storePath;
            return this;
        }

        /**
         * @param dryRun cleanup：{@code --dry-run}
         * @return {@code this}
         */
        public Builder cleanupDryRun(boolean dryRun) {
            this.cleanupDryRun = dryRun;
            return this;
        }

        /**
         * @param enforce cleanup：{@code --enforce}
         * @return {@code this}
         */
        public Builder cleanupEnforce(boolean enforce) {
            this.cleanupEnforce = enforce;
            return this;
        }

        /**
         * @param fixMissing cleanup：{@code --fix-missing}
         * @return {@code this}
         */
        public Builder cleanupFixMissing(boolean fixMissing) {
            this.cleanupFixMissing = fixMissing;
            return this;
        }

        /**
         * @param sessionKey cleanup：{@code --active-key}
         * @return {@code this}
         */
        public Builder cleanupActiveKey(String sessionKey) {
            this.cleanupActiveKey = sessionKey;
            return this;
        }

        /**
         * @param json cleanup：{@code --json}
         * @return {@code this}
         */
        public Builder cleanupJson(boolean json) {
            this.cleanupJson = json;
            return this;
        }

        /**
 * CLI token.
         *
 * @param tokens argv
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link SessionsOptions}
         */
        public SessionsOptions build() {
            return new SessionsOptions(this);
        }
    }
}
