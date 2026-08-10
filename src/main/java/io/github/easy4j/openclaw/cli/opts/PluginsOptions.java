package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw plugins}:, Gateway plugin,hook bundle(Codex/Claude/Cursor).
 * <p>:version;{@code --dangerously-force-unsafe-install} only, {@code before_install} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/plugins">plugins CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class PluginsOptions implements CliSubArgs {

    /**
 * plugins subcommand:,,,diagnostic, marketplace .
     */
    public enum Mode {
 /** {@code plugins list}:plugin(openclaw bundle). */
        LIST,
 /** {@code plugins install}: ClawHub,npm, marketplace . */
        INSTALL,
 /** {@code plugins inspect}:,hook,. */
        INSPECT,
 /** {@code plugins info}:{@code inspect} . */
        INFO,
 /** {@code plugins enable}:plugin id. */
        ENABLE,
 /** {@code plugins disable}:plugin id. */
        DISABLE,
 /** {@code plugins uninstall}:directory. */
        UNINSTALL,
 /** {@code plugins doctor}:. */
        DOCTOR,
 /** {@code plugins update}: {@code plugins.installs} . */
        UPDATE,
 /** {@code plugins marketplace list}: marketplace plugin. */
        MARKETPLACE_LIST
    }

 /** plugins subcommand. */
    private final Mode mode;
    /**
 * list:{@code --enabled} onlyplugin.
     */
    private final boolean listEnabled;
    /**
 * list:{@code --verbose} .
     */
    private final boolean listVerbose;
    /**
 * list:{@code --json} diagnostic.
     */
    private final boolean listJson;
    /**
 * install: spec, {@code clawhub:} .
     */
    private final String installSpec;
    /**
 * install:{@code --force} .
     */
    private final boolean installForce;
    /**
 * install:{@code --pin} npm version {@code plugins.installs}.
     */
    private final boolean installPin;
    /**
 * install:{@code --dangerously-force-unsafe-install} critical (break-glass).
     */
    private final boolean dangerouslyForceUnsafeInstall;
    /**
 * install:{@code --marketplace} marketplace (owner/repo URL).
     */
    private final String marketplace;
    /**
 * install:{@code --link} directory {@code plugins.load.paths} .
     */
    private final boolean installLink;
    /**
 * inspect / info:plugin id, {@code inspectAll} .
     */
    private final String inspectId;
    /**
 * inspect / info:{@code --json} .
     */
    private final boolean inspectJson;
    /**
 * inspect:{@code --all} fleet .
     */
    private final boolean inspectAll;
    /**
 * enable / disable / uninstall / update:plugin id npm spec(update documentation).
     */
    private final String pluginId;
    /**
 * uninstall:{@code --dry-run} .
     */
    private final boolean uninstallDryRun;
    /**
 * uninstall:{@code --keep-files} plugindirectory.
     */
    private final boolean uninstallKeepFiles;
    /**
 * update:{@code --all} .
     */
    private final boolean updateAll;
    /**
 * update:{@code --dry-run} .
     */
    private final boolean updateDryRun;
    /**
 * update:{@code --yes} skips.
     */
    private final boolean yes;
    /**
 * marketplace list:marketplace ({@code owner/repo},git URL ).
     */
    private final String marketplaceSource;
    /**
 * marketplace list:{@code --json} manifest .
     */
    private final boolean marketplaceJson;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private PluginsOptions(Builder b) {
        this.mode = b.mode;
        this.listEnabled = b.listEnabled;
        this.listVerbose = b.listVerbose;
        this.listJson = b.listJson;
        this.installSpec = b.installSpec;
        this.installForce = b.installForce;
        this.installPin = b.installPin;
        this.dangerouslyForceUnsafeInstall = b.dangerouslyForceUnsafeInstall;
        this.marketplace = b.marketplace;
        this.installLink = b.installLink;
        this.inspectId = b.inspectId;
        this.inspectJson = b.inspectJson;
        this.inspectAll = b.inspectAll;
        this.pluginId = b.pluginId;
        this.uninstallDryRun = b.uninstallDryRun;
        this.uninstallKeepFiles = b.uninstallKeepFiles;
        this.updateAll = b.updateAll;
        this.updateDryRun = b.updateDryRun;
        this.yes = b.yes;
        this.marketplaceSource = b.marketplaceSource;
        this.marketplaceJson = b.marketplaceJson;
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
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--enabled", listEnabled);
                OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                break;
            case INSTALL:
                out.add("install");
                OpenClawCliArgv.addFlag(out, "-l", installLink);
                if (installSpec != null && OpenClawStrings.isNotBlank(installSpec)) {
                    out.add(installSpec.trim());
                }
                OpenClawCliArgv.addFlag(out, "--force", installForce);
                OpenClawCliArgv.addFlag(out, "--pin", installPin);
                OpenClawCliArgv.addFlag(out, "--dangerously-force-unsafe-install", dangerouslyForceUnsafeInstall);
                OpenClawCliArgv.addIfPresent(out, "--marketplace", marketplace);
                break;
            case INSPECT:
            case INFO:
                out.add(mode == Mode.INFO ? "info" : "inspect");
                if (inspectAll) {
                    out.add("--all");
                } else if (inspectId != null && OpenClawStrings.isNotBlank(inspectId)) {
                    out.add(inspectId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", inspectJson);
                break;
            case ENABLE:
                out.add("enable");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                break;
            case DISABLE:
                out.add("disable");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                break;
            case UNINSTALL:
                out.add("uninstall");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--dry-run", uninstallDryRun);
                OpenClawCliArgv.addFlag(out, "--keep-files", uninstallKeepFiles);
                break;
            case DOCTOR:
                out.add("doctor");
                break;
            case UPDATE:
                out.add("update");
                if (updateAll) {
                    out.add("--all");
                } else if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--dry-run", updateDryRun);
                OpenClawCliArgv.addFlag(out, "--dangerously-force-unsafe-install", dangerouslyForceUnsafeInstall);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                break;
            case MARKETPLACE_LIST:
                out.add("marketplace");
                out.add("list");
                if (marketplaceSource != null && OpenClawStrings.isNotBlank(marketplaceSource)) {
                    out.add(marketplaceSource.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", marketplaceJson);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link PluginsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private boolean listEnabled;
        private boolean listVerbose;
        private boolean listJson;
        private String installSpec;
        private boolean installForce;
        private boolean installPin;
        private boolean dangerouslyForceUnsafeInstall;
        private String marketplace;
        private boolean installLink;
        private String inspectId;
        private boolean inspectJson;
        private boolean inspectAll;
        private String pluginId;
        private boolean uninstallDryRun;
        private boolean uninstallKeepFiles;
        private boolean updateAll;
        private boolean updateDryRun;
        private boolean yes;
        private String marketplaceSource;
        private boolean marketplaceJson;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code plugins list}）
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * @param enabled list：{@code --enabled}
         * @return {@code this}
         */
        public Builder listEnabled(boolean enabled) {
            this.listEnabled = enabled;
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
         * @param json list：{@code --json}
         * @return {@code this}
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
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
         * @param force {@code --force}
         * @return {@code this}
         */
        public Builder installForce(boolean force) {
            this.installForce = force;
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
         * @param unsafe {@code --dangerously-force-unsafe-install}
         * @return {@code this}
         */
        public Builder dangerouslyForceUnsafeInstall(boolean unsafe) {
            this.dangerouslyForceUnsafeInstall = unsafe;
            return this;
        }

        /**
         * @param marketplace {@code --marketplace}
         * @return {@code this}
         */
        public Builder marketplace(String marketplace) {
            this.marketplace = marketplace;
            return this;
        }

        /**
         * @param link install：{@code -l}
         * @return {@code this}
         */
        public Builder installLink(boolean link) {
            this.installLink = link;
            return this;
        }

        /**
 * @param id inspect:plugin ID
         * @return {@code this}
         */
        public Builder inspect(String id) {
            this.mode = Mode.INSPECT;
            this.inspectId = id;
            this.inspectAll = false;
            return this;
        }

        /**
         * @param all inspect：{@code --all}
         * @return {@code this}
         */
        public Builder inspectAll(boolean all) {
            this.inspectAll = all;
            if (all) {
                this.mode = Mode.INSPECT;
                this.inspectId = null;
            }
            return this;
        }

        /**
         * @param json inspect：{@code --json}
         * @return {@code this}
         */
        public Builder inspectJson(boolean json) {
            this.inspectJson = json;
            return this;
        }

        /**
 * @param id info:plugin ID
         * @return {@code this}
         */
        public Builder info(String id) {
            this.mode = Mode.INFO;
            this.inspectId = id;
            return this;
        }

        /**
 * @param id enable:plugin ID
         * @return {@code this}
         */
        public Builder enable(String id) {
            this.mode = Mode.ENABLE;
            this.pluginId = id;
            return this;
        }

        /**
 * @param id disable:plugin ID
         * @return {@code this}
         */
        public Builder disable(String id) {
            this.mode = Mode.DISABLE;
            this.pluginId = id;
            return this;
        }

        /**
 * @param id uninstall:plugin ID
         * @return {@code this}
         */
        public Builder uninstall(String id) {
            this.mode = Mode.UNINSTALL;
            this.pluginId = id;
            return this;
        }

        /**
         * @param dryRun {@code --dry-run}
         * @return {@code this}
         */
        public Builder uninstallDryRun(boolean dryRun) {
            this.uninstallDryRun = dryRun;
            return this;
        }

        /**
         * @param keep {@code --keep-files}
         * @return {@code this}
         */
        public Builder uninstallKeepFiles(boolean keep) {
            this.uninstallKeepFiles = keep;
            return this;
        }

        /**
         * @return {@code this}（{@code plugins doctor}）
         */
        public Builder doctor() {
            this.mode = Mode.DOCTOR;
            return this;
        }

        /**
 * @param idOrSpec update:plugin ID spec
         * @return {@code this}
         */
        public Builder update(String idOrSpec) {
            this.mode = Mode.UPDATE;
            this.updateAll = false;
            this.pluginId = idOrSpec;
            return this;
        }

        /**
         * @param all update：{@code --all}
         * @return {@code this}
         */
        public Builder updateAll(boolean all) {
            this.mode = Mode.UPDATE;
            this.updateAll = all;
            this.pluginId = null;
            return this;
        }

        /**
         * @param dryRun update：{@code --dry-run}
         * @return {@code this}
         */
        public Builder updateDryRun(boolean dryRun) {
            this.updateDryRun = dryRun;
            return this;
        }

        /**
         * @param yes update：{@code --yes}
         * @return {@code this}
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
 * @param source marketplace list:
         * @return {@code this}
         */
        public Builder marketplaceList(String source) {
            this.mode = Mode.MARKETPLACE_LIST;
            this.marketplaceSource = source;
            return this;
        }

        /**
         * @param json marketplace：{@code --json}
         * @return {@code this}
         */
        public Builder marketplaceJson(boolean json) {
            this.marketplaceJson = json;
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
 * @return {@link PluginsOptions}
         */
        public PluginsOptions build() {
            return new PluginsOptions(this);
        }
    }
}
