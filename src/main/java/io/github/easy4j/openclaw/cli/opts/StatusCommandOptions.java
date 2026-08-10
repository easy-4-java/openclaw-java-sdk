package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw status}:sessiondiagnostic( {@code gateway status}).
 * <p>documentation:{@code --deep} WhatsApp Web,Telegram,Discord,Slack,Signal ;
 * {@code --usage} Provides" X%";{@code --all} Secrets diagnostic(See Notes).</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/status">status CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class StatusCommandOptions implements CliSubArgs {

    /**
 * {@code --all}:( Secrets ,secret diagnostic,documentation SecretRef).
     */
    private final boolean all;
    /**
 * {@code --deep}:(documentation IM ).
     */
    private final boolean deep;
    /**
 * {@code --usage}:NormalizesProvides(documentation: {@code X% left} ).
     */
    private final boolean usage;
    /**
 * {@code --json}: JSON(documentation Notes {@code status --json} ).
     */
    private final boolean json;

    /**
 * @param b builder
     */
    private StatusCommandOptions(Builder b) {
        this.all = b.all;
        this.deep = b.deep;
        this.usage = b.usage;
        this.json = b.json;
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
        if (all) {
            out.add("--all");
        }
        if (deep) {
            out.add("--deep");
        }
        if (usage) {
            out.add("--usage");
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link StatusCommandOptions} builder.
     */
    public static final class Builder {

        private boolean all;
        private boolean deep;
        private boolean usage;
        private boolean json;

        /**
         * @param all {@code --all}
         * @return {@code this}
         */
        public Builder all(boolean all) {
            this.all = all;
            return this;
        }

        /**
         * @param deep {@code --deep}
         * @return {@code this}
         */
        public Builder deep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * @param usage {@code --usage}
         * @return {@code this}
         */
        public Builder usage(boolean usage) {
            this.usage = usage;
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
 * @return {@link StatusCommandOptions}
         */
        public StatusCommandOptions build() {
            return new StatusCommandOptions(this);
        }
    }
}
