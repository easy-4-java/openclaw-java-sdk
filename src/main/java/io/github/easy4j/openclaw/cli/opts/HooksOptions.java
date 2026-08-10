package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw hooks}:, agent hooks(event).
 * <p>plugin hook plugin; hook {@code openclaw plugins install},{@code hooks install} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/hooks">hooks CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class HooksOptions implements CliSubArgs {

    /**
 * hooks subcommand: {@code list} , info, install.
     */
    public enum Mode {
 /** {@code hooks list}: workspace,managed,extra,bundled directory hook. */
        LIST,
 /** {@code hooks info}: hook ,event. */
        INFO,
 /** {@code hooks check}: eligible . */
        CHECK,
 /** {@code hooks enable}: {@code hooks.internal.entries.*.enabled}. */
        ENABLE,
 /** {@code hooks disable}: hook. */
        DISABLE,
 /** {@code hooks install}:, plugins stream. */
        INSTALL
    }

 /** list / info / check / enable / disable / install . */
    private final Mode mode;
    /**
 * list:{@code --eligible} hook.
     */
    private final boolean listEligible;
    /**
 * list:{@code --json} .
     */
    private final boolean listJson;
    /**
 * list:{@code --verbose} diagnostic.
     */
    private final boolean listVerbose;
    /**
 * info / enable / disable:hook key .
     */
    private final String hookName;
    /**
     * info：{@code --json}。
     */
    private final boolean infoJson;
    /**
     * check：{@code --json}。
     */
    private final boolean checkJson;
    /**
 * install:,npm ( plugins system).
     */
    private final String installSpec;
    /**
 * install:{@code --link} directory {@code hooks.internal.load.extraDirs} .
     */
    private final boolean installLink;
    /**
 * install:{@code --pin} npm version {@code hooks.internal.installs}.
     */
    private final boolean installPin;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private HooksOptions(Builder b) {
        this.mode = b.mode;
        this.listEligible = b.listEligible;
        this.listJson = b.listJson;
        this.listVerbose = b.listVerbose;
        this.hookName = b.hookName;
        this.infoJson = b.infoJson;
        this.checkJson = b.checkJson;
        this.installSpec = b.installSpec;
        this.installLink = b.installLink;
        this.installPin = b.installPin;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
 * :Corresponds to CLI hooks( {@code hooks list} ).
     */
    public static HooksOptions defaultList() {
        return builder().list().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--eligible", listEligible);
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
                break;
            case INFO:
                out.add("info");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", infoJson);
                break;
            case CHECK:
                out.add("check");
                OpenClawCliArgv.addFlag(out, "--json", checkJson);
                break;
            case ENABLE:
                out.add("enable");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                break;
            case DISABLE:
                out.add("disable");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                break;
            case INSTALL:
                out.add("install");
                if (installSpec != null && OpenClawStrings.isNotBlank(installSpec)) {
                    out.add(installSpec.trim());
                }
                OpenClawCliArgv.addFlag(out, "--link", installLink);
                OpenClawCliArgv.addFlag(out, "--pin", installPin);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link HooksOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private boolean listEligible;
        private boolean listJson;
        private boolean listVerbose;
        private String hookName;
        private boolean infoJson;
        private boolean checkJson;
        private String installSpec;
        private boolean installLink;
        private boolean installPin;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code hooks list}）
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * @param eligible list：{@code --eligible}
         * @return {@code this}
         */
        public Builder listEligible(boolean eligible) {
            this.listEligible = eligible;
            return this;
        }

        /**
         * @param json list：{@code --json}
         * @return {@code this}
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
         * @param verbose list：{@code --verbose}
         * @return {@code this}
         */
        public Builder listVerbose(boolean verbose) {
            this.listVerbose = verbose;
            return this;
        }

        /**
 * @param name hook
         * @return {@code this}
         */
        public Builder info(String name) {
            this.mode = Mode.INFO;
            this.hookName = name;
            return this;
        }

        /**
         * @param json info：{@code --json}
         * @return {@code this}
         */
        public Builder infoJson(boolean json) {
            this.infoJson = json;
            return this;
        }

        /**
         * @return {@code this}（{@code hooks check}）
         */
        public Builder check() {
            this.mode = Mode.CHECK;
            return this;
        }

        /**
         * @param json check：{@code --json}
         * @return {@code this}
         */
        public Builder checkJson(boolean json) {
            this.checkJson = json;
            return this;
        }

        /**
 * @param name hook
         * @return {@code this}
         */
        public Builder enable(String name) {
            this.mode = Mode.ENABLE;
            this.hookName = name;
            return this;
        }

        /**
 * @param name hook
         * @return {@code this}
         */
        public Builder disable(String name) {
            this.mode = Mode.DISABLE;
            this.hookName = name;
            return this;
        }

        /**
 * @param spec install: spec
         * @return {@code this}
         */
        public Builder install(String spec) {
            this.mode = Mode.INSTALL;
            this.installSpec = spec;
            return this;
        }

        /**
         * @param link {@code --link}
         * @return {@code this}
         */
        public Builder installLink(boolean link) {
            this.installLink = link;
            return this;
        }

        /**
         * @param pin {@code --pin}
         * @return {@code this}
         */
        public Builder installPin(boolean pin) {
            this.installPin = pin;
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
 * @return {@link HooksOptions}
         */
        public HooksOptions build() {
            return new HooksOptions(this);
        }
    }
}
