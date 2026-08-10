package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw health}:<strong></strong> Gateway ( {@code gateway health} RPC subcommand).
 * <p>documentation:;{@code --verbose} .
 * agent agent session .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/health">health CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class HealthCommandOptions implements CliSubArgs {

    /**
 * {@code --json}: JSON .
     */
    private final boolean json;
    /**
 * {@code --timeout}:connectiontimeoutmilliseconds(documentation {@code 10000}),characters CLI .
     */
    private final String timeoutMs;
    /**
 * {@code --verbose}:;,Gatewayconnection, agent.
     */
    private final boolean verbose;
    /**
 * {@code --debug}:documentation {@code --verbose} .
     */
    private final boolean debug;

    /**
 * @param b builder
     */
    private HealthCommandOptions(Builder b) {
        this.json = b.json;
        this.timeoutMs = b.timeoutMs;
        this.verbose = b.verbose;
        this.debug = b.debug;
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
        if (json) {
            out.add("--json");
        }
        if (timeoutMs != null && !timeoutMs.isEmpty()) {
            out.add("--timeout");
            out.add(timeoutMs);
        }
        if (verbose) {
            out.add("--verbose");
        }
        if (debug) {
            out.add("--debug");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link HealthCommandOptions} builder.
     */
    public static final class Builder {

        private boolean json;
        private String timeoutMs;
        private boolean verbose;
        private boolean debug;

        /**
         * @param json {@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

 /** connectiontimeoutmilliseconds(documentation 10000). */
        public Builder timeoutMs(String timeoutMs) {
            this.timeoutMs = timeoutMs;
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

 /** documentation {@code --verbose} . */
        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
 * @return {@link HealthCommandOptions}
         */
        public HealthCommandOptions build() {
            return new HealthCommandOptions(this);
        }
    }
}
