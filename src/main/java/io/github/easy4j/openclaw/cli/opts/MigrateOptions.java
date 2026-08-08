package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw migrate}: agent system.
 * <p>
 * {@code list},{@code plan <provider>},{@code apply <provider>} subcommand
 * {@code migrate [provider]}. {@code addMigrationOptions} inject;{@code apply}
 * {@code --yes}/{@code --backup-output}/{@code --no-backup}/{@code --force}/{@code --dry-run}.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/migrate">migrate CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class MigrateOptions implements CliSubArgs {

 /** subcommand. */
    public enum Mode {
 /** {@code migrate [provider]}:Optional. */
        DEFAULT,
 /** {@code list}:migrateProvides. */
        LIST,
 /** {@code plan <provider>}:only,. */
        PLAN,
 /** {@code apply <provider>}:migrate. */
        APPLY
    }

    private final Mode mode;
 /** migrateProvides ID( {@code hermes}). */
    private final String provider;
 /** {@code --from}:directory. */
    private final String from;
 /** {@code --include-secrets}:credentials. */
    private final boolean includeSecrets;
 /** {@code --no-auth-credentials}:skips auth credentialsmigrate(Commander ). */
    private final boolean noAuthCredentials;
 /** {@code --overwrite}:backup. */
    private final boolean overwrite;
 /** {@code --dry-run}:only,. */
    private final boolean dryRun;
 /** {@code --yes}:. */
    private final boolean yes;
 /** {@code --skill}: id skillmigrate. */
    private final List<String> skills;
 /** {@code --plugin}: id Codex pluginmigrate. */
    private final List<String> plugins;
 /** {@code --backup-output}:migratebackupdirectory. */
    private final String backupOutput;
 /** {@code --no-backup}:skipsmigrate OpenClaw backup. */
    private final boolean noBackup;
 /** {@code --force}: {@code --no-backup}. */
    private final boolean force;
 /** {@code --verify-plugin-apps}:Codex :plugin app/list plugin app . */
    private final boolean verifyPluginApps;
 /** {@code --json}:JSON . */
    private final boolean json;

    private MigrateOptions(Builder b) {
        this.mode = b.mode;
        this.provider = b.provider;
        this.from = b.from;
        this.includeSecrets = b.includeSecrets;
        this.noAuthCredentials = b.noAuthCredentials;
        this.overwrite = b.overwrite;
        this.dryRun = b.dryRun;
        this.yes = b.yes;
        this.skills = OpenClawLists.copyOf(b.skills);
        this.plugins = OpenClawLists.copyOf(b.plugins);
        this.backupOutput = b.backupOutput;
        this.noBackup = b.noBackup;
        this.force = b.force;
        this.verifyPluginApps = b.verifyPluginApps;
        this.json = b.json;
    }

    /**
 * @return {@link Builder}( {@link Mode#DEFAULT})
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (mode) {
            case LIST:
                out.add("list");
                break;
            case PLAN:
                out.add("plan");
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
            case APPLY:
                out.add("apply");
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
            case DEFAULT:
            default:
                // 默认动作：可选后接 provider 位置参数
                if (provider != null && !provider.isEmpty()) {
                    out.add(provider);
                }
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--from", from);
        OpenClawCliArgv.addFlag(out, "--include-secrets", includeSecrets);
        OpenClawCliArgv.addFlag(out, "--no-auth-credentials", noAuthCredentials);
        OpenClawCliArgv.addFlag(out, "--overwrite", overwrite);
        // --dry-run / --yes / --backup-output / --no-backup / --force 仅在 DEFAULT 与 APPLY 下生效
        if (mode == Mode.DEFAULT || mode == Mode.APPLY) {
            OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
            OpenClawCliArgv.addFlag(out, "--yes", yes);
            OpenClawCliArgv.addIfPresent(out, "--backup-output", backupOutput);
            OpenClawCliArgv.addFlag(out, "--no-backup", noBackup);
            OpenClawCliArgv.addFlag(out, "--force", force);
        }
        OpenClawCliArgv.addRepeatable(out, "--skill", skills);
        OpenClawCliArgv.addRepeatable(out, "--plugin", plugins);
        OpenClawCliArgv.addFlag(out, "--verify-plugin-apps", verifyPluginApps);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link MigrateOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.DEFAULT;
        private String provider;
        private String from;
        private boolean includeSecrets;
        private boolean noAuthCredentials;
        private boolean overwrite;
        private boolean dryRun;
        private boolean yes;
        private List<String> skills;
        private List<String> plugins;
        private String backupOutput;
        private boolean noBackup;
        private boolean force;
        private boolean verifyPluginApps;
        private boolean json;

 /** {@code list} subcommand. */
        public Builder list() { this.mode = Mode.LIST; return this; }
 /** {@code plan <provider>} subcommand. */
        public Builder plan(String provider) { this.mode = Mode.PLAN; this.provider = provider; return this; }
 /** {@code apply <provider>} subcommand. */
        public Builder apply(String provider) { this.mode = Mode.APPLY; this.provider = provider; return this; }
 /** {@code migrate [provider]}. */
        public Builder defaultAction(String provider) { this.mode = Mode.DEFAULT; this.provider = provider; return this; }
 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** migrateProvides ID( {@code hermes}). */
        public Builder provider(String provider) { this.provider = provider; return this; }
 /** {@code --from}:directory. */
        public Builder from(String from) { this.from = from; return this; }
 /** {@code --include-secrets}:credentials. */
        public Builder includeSecrets(boolean includeSecrets) { this.includeSecrets = includeSecrets; return this; }
 /** {@code --no-auth-credentials}:skips auth credentialsmigrate. */
        public Builder noAuthCredentials(boolean noAuthCredentials) { this.noAuthCredentials = noAuthCredentials; return this; }
 /** {@code --overwrite}:. */
        public Builder overwrite(boolean overwrite) { this.overwrite = overwrite; return this; }
 /** {@code --dry-run}:only. */
        public Builder dryRun(boolean dryRun) { this.dryRun = dryRun; return this; }
 /** {@code --yes}:. */
        public Builder yes(boolean yes) { this.yes = yes; return this; }
 /** {@code --skill}:skill id. */
        public Builder skills(List<String> skills) { this.skills = skills; return this; }
 /** {@code --plugin}:Codex plugin id. */
        public Builder plugins(List<String> plugins) { this.plugins = plugins; return this; }
 /** {@code --backup-output}:migratebackup. */
        public Builder backupOutput(String backupOutput) { this.backupOutput = backupOutput; return this; }
 /** {@code --no-backup}:skipsmigrate OpenClaw backup. */
        public Builder noBackup(boolean noBackup) { this.noBackup = noBackup; return this; }
 /** {@code --force}:. */
        public Builder force(boolean force) { this.force = force; return this; }
 /** {@code --verify-plugin-apps}:Codex ,plugin app . */
        public Builder verifyPluginApps(boolean verifyPluginApps) { this.verifyPluginApps = verifyPluginApps; return this; }
 /** {@code --json}:JSON . */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
 * @return {@link MigrateOptions}
         */
        public MigrateOptions build() {
            return new MigrateOptions(this);
        }
    }
}
