package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw completion}: shell ,Optional profile state directory.
 * <p> {@code --install} {@code --write-state} stdout;{@code --install} profile source .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/completion">completion CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class CompletionOptions implements CliSubArgs {

    /**
 * {@code --shell} (documentationvalue).
     */
    public enum Shell {
        ZSH("zsh"),
        BASH("bash"),
        POWERSHELL("powershell"),
        FISH("fish");

 /** {@code --shell} CLI . */
        private final String cliValue;

        /**
 * @param cliValue null shell
         */
        Shell(String cliValue) {
            this.cliValue = cliValue;
        }

        /**
 * @return CLI shell token
         */
        String cliValue() {
            return cliValue;
        }
    }

    /**
 * {@code -s} / {@code --shell}: shell(documentation:{@code zsh},{@code bash},{@code powershell},{@code fish}; {@code zsh}).
     */
    private final Shell shell;
    /**
 * {@code -i} / {@code --install}: shell profile source .
     */
    private final boolean install;
    /**
 * {@code --write-state}: {@code $OPENCLAW_STATE_DIR/completions}, stdout.
     */
    private final boolean writeState;
    /**
 * {@code -y} / {@code --yes}:skips.
     */
    private final boolean yes;
    /**
 * argv .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private CompletionOptions(Builder b) {
        this.shell = b.shell;
        this.install = b.install;
        this.writeState = b.writeState;
        this.yes = b.yes;
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
        if (shell != null) {
            out.add("--shell");
            out.add(shell.cliValue());
        }
        OpenClawCliArgv.addFlag(out, "--install", install);
        OpenClawCliArgv.addFlag(out, "--write-state", writeState);
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link CompletionOptions} builder.
     */
    public static final class Builder {
        private Shell shell;
        private boolean install;
        private boolean writeState;
        private boolean yes;
        private List<String> extra = new ArrayList<>();

        /**
         * @param shell {@code --shell}
         * @return {@code this}
         */
        public Builder shell(Shell shell) {
            this.shell = shell;
            return this;
        }

        /**
         * @param install {@code --install}
         * @return {@code this}
         */
        public Builder install(boolean install) {
            this.install = install;
            return this;
        }

        /**
         * @param writeState {@code --write-state}
         * @return {@code this}
         */
        public Builder writeState(boolean writeState) {
            this.writeState = writeState;
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
 * @return {@link CompletionOptions}
         */
        public CompletionOptions build() {
            return new CompletionOptions(this);
        }
    }
}
