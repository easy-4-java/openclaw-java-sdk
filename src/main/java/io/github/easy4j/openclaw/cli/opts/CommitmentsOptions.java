package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw commitments}:(follow-up commitments).
 * <p>
 * Equivalent to {@code list}; {@code dismiss <ids...>} subcommand.
 * {@code enablePositionalOptions} subcommand {@code --json}/{@code --agent}/
 * {@code --status}/{@code --all} .
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/commitments">commitments CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class CommitmentsOptions implements CliSubArgs {

 /** , {@code dismiss} subcommand. */
    public enum Mode {
 /** :subcommand token,with documentationexample {@code openclaw commitments} . */
        LIST,
 /** {@code dismiss <ids...>}: id . */
        DISMISS
    }

 /** LIST DISMISS. */
    private final Mode mode;
 /** {@code --json}:JSON . */
    private final boolean json;
 /** {@code --agent}: agent id. */
    private final String agent;
 /** {@code --status}:(pending/sent/dismissed/snoozed/expired). */
    private final String status;
 /** {@code --all}:. */
    private final boolean all;
 /** dismiss: {@code <ids...>}, id . */
    private final List<String> dismissIds;

    private CommitmentsOptions(Builder b) {
        this.mode = b.mode;
        this.json = b.json;
        this.agent = b.agent;
        this.status = b.status;
        this.all = b.all;
        this.dismissIds = OpenClawLists.copyOf(b.dismissIds);
    }

    /**
 * @return {@link Builder}( {@link Mode#LIST})
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        if (mode == Mode.DISMISS) {
            out.add("dismiss");
            if (dismissIds != null) {
                out.addAll(dismissIds);
            }
        }
        if (json) {
            out.add("--json");
        }
        if (agent != null && !agent.isEmpty()) {
            out.add("--agent");
            out.add(agent);
        }
        if (status != null && !status.isEmpty()) {
            out.add("--status");
            out.add(status);
        }
        if (all) {
            out.add("--all");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link CommitmentsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private boolean json;
        private String agent;
        private String status;
        private boolean all;
        private List<String> dismissIds;

 /** {@code dismiss} subcommand. */
        public Builder dismiss() { this.mode = Mode.DISMISS; return this; }
 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** {@code --json}:JSON . */
        public Builder json(boolean json) { this.json = json; return this; }
 /** {@code --agent}: agent id. */
        public Builder agent(String agent) { this.agent = agent; return this; }
 /** {@code --status}:(pending/sent/dismissed/snoozed/expired). */
        public Builder status(String status) { this.status = status; return this; }
 /** {@code --all}:. */
        public Builder all(boolean all) { this.all = all; return this; }
 /** dismiss: {@code <ids...>}, id . */
        public Builder dismissIds(List<String> ids) { this.dismissIds = ids; return this; }

        /**
 * @return {@link CommitmentsOptions}
         */
        public CommitmentsOptions build() {
            return new CommitmentsOptions(this);
        }
    }
}
