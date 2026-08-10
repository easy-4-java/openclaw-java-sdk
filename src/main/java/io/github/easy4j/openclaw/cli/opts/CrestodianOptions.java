package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw crestodian}: ring-zero repair.
 * <p>
 * openclaw ({@code src/cli/program/register.crestodian.ts}),
 * directory {@code bypassConfigGuard, loadPlugins "never", ensureCliPath false}.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/crestodian">crestodian CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class CrestodianOptions implements CliSubArgs {

 /** {@code -m, --message}: Crestodian . */
    private final String message;
 /** {@code --yes}:. */
    private final boolean yes;
 /** {@code --json}: JSON . */
    private final boolean json;

    private CrestodianOptions(Builder b) {
        this.message = b.message;
        this.yes = b.yes;
        this.json = b.json;
    }

    /**
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        if (message != null && !message.isEmpty()) {
            out.add("--message");
            out.add(message);
        }
        if (yes) {
            out.add("--yes");
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link CrestodianOptions} builder.
     */
    public static final class Builder {
        private String message;
        private boolean yes;
        private boolean json;

 /** {@code -m, --message}: Crestodian . */
        public Builder message(String message) { this.message = message; return this; }
 /** {@code --yes}:. */
        public Builder yes(boolean yes) { this.yes = yes; return this; }
 /** {@code --json}: JSON . */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
 * @return {@link CrestodianOptions}
         */
        public CrestodianOptions build() {
            return new CrestodianOptions(this);
        }
    }
}
