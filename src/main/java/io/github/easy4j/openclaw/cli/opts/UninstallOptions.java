package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw uninstall}: Gateway (CLI ).
 * <p>fieldCorresponds todocumentation Options ;Composes.documentation: state workspace
 * {@code openclaw backup create} restore;{@code --non-interactive} {@code --yes} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/uninstall">uninstall CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class UninstallOptions implements CliSubArgs {

    /**
 * {@code --service}: Gateway (launchd/systemd ).
     */
    private final boolean service;
    /**
 * {@code --state}:( OpenClaw /directory).
     */
    private final boolean state;
    /**
 * {@code --workspace}: agent workspace directory.
     */
    private final boolean workspace;
    /**
 * {@code --app}: macOS (only macOS ).
     */
    private final boolean app;
    /**
 * {@code --all}:Equivalent to service,state,workspace,app(documentation).
     */
    private final boolean all;
    /**
 * {@code --yes}:skips.
     */
    private final boolean yes;
    /**
 * {@code --non-interactive}:;documentation {@code --yes} .
     */
    private final boolean nonInteractive;
    /**
 * {@code --dry-run}:only,.
     */
    private final boolean dryRun;
    /**
 * documentation argv, shell .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private UninstallOptions(Builder b) {
        this.service = b.service;
        this.state = b.state;
        this.workspace = b.workspace;
        this.app = b.app;
        this.all = b.all;
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
        OpenClawCliArgv.addFlag(out, "--service", service);
        OpenClawCliArgv.addFlag(out, "--state", state);
        OpenClawCliArgv.addFlag(out, "--workspace", workspace);
        OpenClawCliArgv.addFlag(out, "--app", app);
        OpenClawCliArgv.addFlag(out, "--all", all);
        OpenClawCliArgv.addFlag(out, "--yes", yes);
        OpenClawCliArgv.addFlag(out, "--non-interactive", nonInteractive);
        OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link UninstallOptions} builder.
     */
    public static final class Builder {
        private boolean service;
        private boolean state;
        private boolean workspace;
        private boolean app;
        private boolean all;
        private boolean yes;
        private boolean nonInteractive;
        private boolean dryRun;
        private List<String> extra = new ArrayList<>();

        /**
         * @param service {@code --service}
         * @return {@code this}
         */
        public Builder service(boolean service) {
            this.service = service;
            return this;
        }

        /**
         * @param state {@code --state}
         * @return {@code this}
         */
        public Builder state(boolean state) {
            this.state = state;
            return this;
        }

        /**
         * @param workspace {@code --workspace}
         * @return {@code this}
         */
        public Builder workspace(boolean workspace) {
            this.workspace = workspace;
            return this;
        }

        /**
         * @param app {@code --app}
         * @return {@code this}
         */
        public Builder app(boolean app) {
            this.app = app;
            return this;
        }

        /**
         * @param all {@code --all}
         * @return {@code this}
         */
        public Builder all(boolean all) {
            this.all = all;
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
 * @return {@link UninstallOptions}
         */
        public UninstallOptions build() {
            return new UninstallOptions(this);
        }
    }
}
