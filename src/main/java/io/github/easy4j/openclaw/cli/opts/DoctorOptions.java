package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw doctor}: Gateway repair.
 * <p>{@code --repair} {@code --fix} ;{@code --fix} backup {@code ~/.openclaw/openclaw.json.bak} key.
 * only TTY {@code --non-interactive} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/doctor">doctor CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class DoctorOptions implements CliSubArgs {

    /**
 * {@code --no-workspace-suggestions}: memory/search .
     */
    private final boolean noWorkspaceSuggestions;
    /**
 * {@code --yes}:,.
     */
    private final boolean yes;
    /**
 * {@code --repair}:repair;CLI {@code --repair}({@code --fix} documentation).
     */
    private final boolean repair;
    /**
 * {@code --force}:repair,.
     */
    private final boolean force;
    /**
 * {@code --non-interactive}:,onlydocumentation"securitymigrate".
     */
    private final boolean nonInteractive;
    /**
 * {@code --generate-gateway-token}:Gateway token .
     */
    private final boolean generateGatewayToken;
    /**
 * {@code --deep}:system, Gateway .
     */
    private final boolean deep;

    /**
 * @param b builder
     */
    private DoctorOptions(Builder b) {
        this.noWorkspaceSuggestions = b.noWorkspaceSuggestions;
        this.yes = b.yes;
        this.repair = b.repair;
        this.force = b.force;
        this.nonInteractive = b.nonInteractive;
        this.generateGatewayToken = b.generateGatewayToken;
        this.deep = b.deep;
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
        if (noWorkspaceSuggestions) {
            out.add("--no-workspace-suggestions");
        }
        if (yes) {
            out.add("--yes");
        }
        if (repair) {
            out.add("--repair");
        }
        if (force) {
            out.add("--force");
        }
        if (nonInteractive) {
            out.add("--non-interactive");
        }
        if (generateGatewayToken) {
            out.add("--generate-gateway-token");
        }
        if (deep) {
            out.add("--deep");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link DoctorOptions} builder.
     */
    public static final class Builder {

        private boolean noWorkspaceSuggestions;
        private boolean yes;
        private boolean repair;
        private boolean force;
        private boolean nonInteractive;
        private boolean generateGatewayToken;
        private boolean deep;

        /**
         * @param noWorkspaceSuggestions {@code --no-workspace-suggestions}
         * @return {@code this}
         */
        public Builder noWorkspaceSuggestions(boolean noWorkspaceSuggestions) {
            this.noWorkspaceSuggestions = noWorkspaceSuggestions;
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

 /** {@code --repair}({@code --fix} , repair ). */
        public Builder repair(boolean repair) {
            this.repair = repair;
            return this;
        }

        /**
         * @param force {@code --force}
         * @return {@code this}
         */
        public Builder force(boolean force) {
            this.force = force;
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
         * @param generateGatewayToken {@code --generate-gateway-token}
         * @return {@code this}
         */
        public Builder generateGatewayToken(boolean generateGatewayToken) {
            this.generateGatewayToken = generateGatewayToken;
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
 * @return {@link DoctorOptions}
         */
        public DoctorOptions build() {
            return new DoctorOptions(this);
        }
    }
}
