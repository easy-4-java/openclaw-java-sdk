package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Task Flow subcommand:Corresponds to {@code openclaw tasks flow list|show|cancel}(stream, sticky cancel ).
 * <p> {@link io.github.easy4j.openclaw.cli.OpenClawCli#flows(FlowsOptions)} ; CLI {@code tasks} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/flows">flows CLI</a>
 * @see <a href="https://docs.openclaw.ai/automation/taskflow">Task Flow</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class FlowsOptions implements CliSubArgs {

 /** {@code flow list|show|cancel} . */
    public enum Mode {
        /** {@code flow list} */
        LIST,
        /** {@code flow show &lt;id&gt;} */
        SHOW,
        /** {@code flow cancel &lt;id&gt;} */
        CANCEL
    }

    /**
 * {@code flow list|show|cancel} ( Task Flow documentation CLI ).
     */
    private final Mode mode;
    /**
 * {@code flow list --json}:stream JSON.
     */
    private final boolean listJson;
    /**
 * {@code flow show|cancel} :flow id lookup key(documentation {@code lookup} ).
     */
    private final String lookup;
    /**
 * {@code openclaw tasks} token.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private FlowsOptions(Builder b) {
        this.mode = b.mode;
        this.listJson = b.listJson;
        this.lookup = b.lookup;
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
        out.add("flow");
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                break;
            case SHOW:
                out.add("show");
                if (lookup != null && OpenClawStrings.isNotBlank(lookup)) {
                    out.add(lookup.trim());
                }
                break;
            case CANCEL:
                out.add("cancel");
                if (lookup != null && OpenClawStrings.isNotBlank(lookup)) {
                    out.add(lookup.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link FlowsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private boolean listJson;
        private String lookup;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}，{@link Mode#LIST}
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * @param json {@code flow list --json}
         * @return {@code this}
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
 * @param lookup flow (show)
         * @return {@code this}
         */
        public Builder show(String lookup) {
            this.mode = Mode.SHOW;
            this.lookup = lookup;
            return this;
        }

        /**
 * @param lookup flow (cancel)
         * @return {@code this}
         */
        public Builder cancel(String lookup) {
            this.mode = Mode.CANCEL;
            this.lookup = lookup;
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
 * @return {@link FlowsOptions}
         */
        public FlowsOptions build() {
            return new FlowsOptions(this);
        }
    }
}
