package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw reset}:resetdirectory( CLI); {@code openclaw backup create}.
 * <p> {@code --scope} ;{@code --non-interactive} Provides {@code --scope} {@code --yes}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/reset">reset CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class ResetOptions implements CliSubArgs {

    /**
 * {@code --scope} value, reset documentation.
     */
    public enum Scope {
 /** onlyreset {@code config}. */
        CONFIG("config"),
 /** session {@code config+creds+sessions}. */
        CONFIG_CREDS_SESSIONS("config+creds+sessions"),
 /** reset {@code full}. */
        FULL("full");

        private final String cliValue;

        Scope(String cliValue) {
            this.cliValue = cliValue;
        }

        String cliValue() {
            return cliValue;
        }
    }

    /**
 * {@code --scope}:;{@code null} flag.
     */
    private final Scope scope;
    /**
 * {@code --yes}:skips.
     */
    private final boolean yes;
    /**
 * {@code --non-interactive}:;documentation {@code --scope},{@code --yes} .
     */
    private final boolean nonInteractive;
    /**
 * {@code --dry-run}:,.
     */
    private final boolean dryRun;
    /**
 * documentation argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ResetOptions(Builder b) {
        this.scope = b.scope;
        this.yes = b.yes;
        this.nonInteractive = b.nonInteractive;
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
        if (scope != null) {
            out.add("--scope");
            out.add(scope.cliValue());
        }
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addFlag(out, "--non-interactive", nonInteractive);
        OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ResetOptions} builder.
     */
    public static final class Builder {
        private Scope scope;
        private boolean yes;
        private boolean nonInteractive;
        private boolean dryRun;
        private List<String> extra = new ArrayList<>();

        /**
         * @param scope {@code --scope}
         * @return {@code this}
         */
        public Builder scope(Scope scope) {
            this.scope = scope;
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
         * @param nonInteractive {@code --non-interactive}
         * @return {@code this}
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
            return this;
        }

        /**
         * @param dryRun {@code --dry-run}
         * @return {@code this}
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
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
 * @return {@link ResetOptions}
         */
        public ResetOptions build() {
            return new ResetOptions(this);
        }
    }
}
