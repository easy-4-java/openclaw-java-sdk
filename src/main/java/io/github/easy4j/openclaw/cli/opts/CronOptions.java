package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw cron}:Gateway (isolated session),.
 * <p>documentation:isolated {@code --announce} ;{@code --no-deliver} ;{@code --at} {@code --keep-after-run}.
 * subcommand {@code openclaw cron --help} , {@link Builder#extra(String...)}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/cron">cron CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class CronOptions implements CliSubArgs {

    /**
 * cron subcommand:,.
     */
    public enum Verb {
 /** {@code cron run}:;{@code --due} only. */
        RUN,
 /** {@code cron runs}: job id . */
        RUNS,
 /** {@code cron add}:. */
        ADD,
 /** {@code cron edit}:. */
        EDIT,
 /** {@code cron list}:. */
        LIST,
 /** {@code cron delete}:. */
        DELETE
    }

 /** cron subcommand. */
    private final Verb verb;
    /**
 * run / edit / delete: id .
     */
    private final String jobId;
    /**
 * run:{@code --due} only( force-run ).
     */
    private final boolean runDue;
    /**
 * runs:{@code --id} job.
     */
    private final String runsId;
    /**
 * runs:{@code --limit} .
     */
    private final Integer runsLimit;
    /**
 * add / edit:{@code --name} .
     */
    private final String name;
    /**
 * add / edit:{@code --cron} cron .
     */
    private final String cronExpr;
    /**
 * add / edit:{@code --session} sessionkey({@code main},{@code isolated},{@code current},{@code session:...} ,Seedocumentation).
     */
    private final String session;
    /**
 * add / edit:{@code --message} agent .
     */
    private final String message;
    /**
 * add / edit:{@code --at} ; UTC Provides {@code --tz}.
     */
    private final String at;
    /**
 * add / edit:{@code --tz} {@code --at} .
     */
    private final String tz;
    /**
 * add / edit:{@code --keep-after-run} .
     */
    private final boolean keepAfterRun;
    /**
 * add / edit:{@code --announce} /webhook (isolated ,Seedocumentation).
     */
    private final boolean announce;
    /**
 * add / edit:{@code --no-deliver} ,(Equivalent tomessage).
     */
    private final boolean noDeliver;
    /**
 * add / edit:{@code --light-context} isolated agent bootstrap(inject workspace ).
     */
    private final boolean lightContext;
    /**
 * add / edit:{@code --announce} {@code --channel}.
     */
    private final String channel;
    /**
 * add / edit:{@code --announce} {@code --to} .
     */
    private final String to;
    /**
 * add / edit:{@code --model} ( allowlist ,Seedocumentation).
     */
    private final String model;
    /**
 * add / edit:{@code --agent} agent.
     */
    private final String agent;
    /**
 * add / edit:{@code --clear-agent} agent .
     */
    private final boolean clearAgent;
    /**
 * add / edit: {@code true} {@code --best-effort-deliver};{@code null} flag.
     */
    private final Boolean bestEffortDeliver;
    /**
 * add / edit: {@code true} {@code --no-best-effort-deliver};{@code null} .
     */
    private final Boolean noBestEffortDeliver;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private CronOptions(Builder b) {
        this.verb = b.verb;
        this.jobId = b.jobId;
        this.runDue = b.runDue;
        this.runsId = b.runsId;
        this.runsLimit = b.runsLimit;
        this.name = b.name;
        this.cronExpr = b.cronExpr;
        this.session = b.session;
        this.message = b.message;
        this.at = b.at;
        this.tz = b.tz;
        this.keepAfterRun = b.keepAfterRun;
        this.announce = b.announce;
        this.noDeliver = b.noDeliver;
        this.lightContext = b.lightContext;
        this.channel = b.channel;
        this.to = b.to;
        this.model = b.model;
        this.agent = b.agent;
        this.clearAgent = b.clearAgent;
        this.bestEffortDeliver = b.bestEffortDeliver;
        this.noBestEffortDeliver = b.noBestEffortDeliver;
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
            case RUN:
                out.add("run");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--due", runDue);
                break;
            case RUNS:
                out.add("runs");
                OpenClawCliArgv.addIfPresent(out, "--id", runsId);
                OpenClawCliArgv.addIfNotNull(out, "--limit", runsLimit);
                break;
            case ADD:
                out.add("add");
                appendAddEditFlags(out, false);
                break;
            case EDIT:
                out.add("edit");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                appendAddEditFlags(out, true);
                break;
            case LIST:
                out.add("list");
                break;
            case DELETE:
                out.add("delete");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    private void appendAddEditFlags(List<String> out, boolean edit) {
        OpenClawCliArgv.addIfPresent(out, "--name", name);
        OpenClawCliArgv.addIfPresent(out, "--cron", cronExpr);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--message", message);
        OpenClawCliArgv.addIfPresent(out, "--at", at);
        OpenClawCliArgv.addIfPresent(out, "--tz", tz);
        OpenClawCliArgv.addFlag(out, "--keep-after-run", keepAfterRun);
        OpenClawCliArgv.addFlag(out, "--announce", announce);
        OpenClawCliArgv.addFlag(out, "--light-context", lightContext);
        OpenClawCliArgv.addFlag(out, "--no-deliver", noDeliver);
        OpenClawCliArgv.addIfPresent(out, "--channel", channel);
        OpenClawCliArgv.addIfPresent(out, "--to", to);
        OpenClawCliArgv.addIfPresent(out, "--model", model);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        OpenClawCliArgv.addFlag(out, "--clear-agent", clearAgent);
        if (Boolean.TRUE.equals(bestEffortDeliver)) {
            out.add("--best-effort-deliver");
        }
        if (Boolean.TRUE.equals(noBestEffortDeliver)) {
            out.add("--no-best-effort-deliver");
        }
    }

    /**
 * {@link CronOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.LIST;
        private String jobId;
        private boolean runDue;
        private String runsId;
        private Integer runsLimit;
        private String name;
        private String cronExpr;
        private String session;
        private String message;
        private String at;
        private String tz;
        private boolean keepAfterRun;
        private boolean announce;
        private boolean noDeliver;
        private boolean lightContext;
        private String channel;
        private String to;
        private String model;
        private String agent;
        private boolean clearAgent;
        private Boolean bestEffortDeliver;
        private Boolean noBestEffortDeliver;
        private List<String> extra = new ArrayList<>();

        /**
 * @param jobId ID( null)
         * @return {@code this}
         */
        public Builder run(String jobId) {
            this.verb = Verb.RUN;
            this.jobId = jobId;
            return this;
        }

        /**
         * @param due run：{@code --due}
         * @return {@code this}
         */
        public Builder runDue(boolean due) {
            this.runDue = due;
            return this;
        }

        /**
         * @param jobId runs：{@code --id}
         * @return {@code this}
         */
        public Builder runs(String jobId) {
            this.verb = Verb.RUNS;
            this.runsId = jobId;
            return this;
        }

        /**
         * @param limit runs：{@code --limit}
         * @return {@code this}
         */
        public Builder runsLimit(int limit) {
            this.runsLimit = limit;
            return this;
        }

        /**
         * @return {@code this}（{@code cron add}）
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
 * @param jobId ID
         * @return {@code this}
         */
        public Builder edit(String jobId) {
            this.verb = Verb.EDIT;
            this.jobId = jobId;
            return this;
        }

        /**
         * @return {@code this}（{@code cron list}）
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
 * @param jobId ID
         * @return {@code this}
         */
        public Builder delete(String jobId) {
            this.verb = Verb.DELETE;
            this.jobId = jobId;
            return this;
        }

        /**
         * @param name {@code --name}
         * @return {@code this}
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param expr {@code --cron}
         * @return {@code this}
         */
        public Builder cronExpr(String expr) {
            this.cronExpr = expr;
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
         * @param message {@code --message}
         * @return {@code this}
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * @param at {@code --at}
         * @return {@code this}
         */
        public Builder at(String at) {
            this.at = at;
            return this;
        }

        /**
         * @param tz {@code --tz}
         * @return {@code this}
         */
        public Builder tz(String tz) {
            this.tz = tz;
            return this;
        }

        /**
         * @param keep {@code --keep-after-run}
         * @return {@code this}
         */
        public Builder keepAfterRun(boolean keep) {
            this.keepAfterRun = keep;
            return this;
        }

        /**
         * @param announce {@code --announce}
         * @return {@code this}
         */
        public Builder announce(boolean announce) {
            this.announce = announce;
            return this;
        }

        /**
         * @param noDeliver {@code --no-deliver}
         * @return {@code this}
         */
        public Builder noDeliver(boolean noDeliver) {
            this.noDeliver = noDeliver;
            return this;
        }

        /**
         * @param lightContext {@code --light-context}
         * @return {@code this}
         */
        public Builder lightContext(boolean lightContext) {
            this.lightContext = lightContext;
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
         * @param to {@code --to}
         * @return {@code this}
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * @param model {@code --model}
         * @return {@code this}
         */
        public Builder model(String model) {
            this.model = model;
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
         * @param clear {@code --clear-agent}
         * @return {@code this}
         */
        public Builder clearAgent(boolean clear) {
            this.clearAgent = clear;
            return this;
        }

        /**
 * @param v When true, {@code --best-effort-deliver}
         * @return {@code this}
         */
        public Builder bestEffortDeliver(boolean v) {
            this.bestEffortDeliver = v ? Boolean.TRUE : null;
            return this;
        }

        /**
 * @param v When true, {@code --no-best-effort-deliver}
         * @return {@code this}
         */
        public Builder noBestEffortDeliver(boolean v) {
            this.noBestEffortDeliver = v ? Boolean.TRUE : null;
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
 * @return {@link CronOptions}
         */
        public CronOptions build() {
            return new CronOptions(this);
        }
    }
}
