package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw backup}:directory,directory,sessionOptional workspace {@code .tar.gz},.
 * <p> {@code manifest.json}; {@code --no-include-workspace} {@code --only-config}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/backup">backup CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class BackupOptions implements CliSubArgs {

    /**
 * backup subcommand:.
     */
    public enum Mode {
 /** {@code backup create}:. */
        CREATE,
 /** {@code backup verify}: tarball manifest . */
        VERIFY
    }

 /** {@code create} {@code verify}. */
    private final Mode mode;
    /**
 * create:{@code --output} directory(documentation:directory home ).
     */
    private final String outputDir;
    /**
 * create:{@code --dry-run} ( {@code --json} ComposesSeedocumentationexample).
     */
    private final boolean dryRun;
    /**
 * create:{@code --json} .
     */
    private final boolean json;
    /**
 * create:{@code --verify} {@code backup verify} .
     */
    private final boolean verifyAfterCreate;
    /**
 * create:{@code --no-include-workspace} skips workspace (backup).
     */
    private final boolean noIncludeWorkspace;
    /**
 * create:{@code --only-config} JSON .
     */
    private final boolean onlyConfig;
    /**
 * verify: {@code .tar.gz} .
     */
    private final String verifyArchivePath;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private BackupOptions(Builder b) {
        this.mode = b.mode;
        this.outputDir = b.outputDir;
        this.dryRun = b.dryRun;
        this.json = b.json;
        this.verifyAfterCreate = b.verifyAfterCreate;
        this.noIncludeWorkspace = b.noIncludeWorkspace;
        this.onlyConfig = b.onlyConfig;
        this.verifyArchivePath = b.verifyArchivePath;
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
        if (mode == Mode.CREATE) {
            out.add("create");
            OpenClawCliArgv.addIfPresent(out, "--output", outputDir);
            OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
            OpenClawCliArgv.addFlag(out, "--json", json);
            OpenClawCliArgv.addFlag(out, "--verify", verifyAfterCreate);
            OpenClawCliArgv.addFlag(out, "--no-include-workspace", noIncludeWorkspace);
            OpenClawCliArgv.addFlag(out, "--only-config", onlyConfig);
        } else {
            out.add("verify");
            if (verifyArchivePath != null && OpenClawStrings.isNotBlank(verifyArchivePath)) {
                out.add(verifyArchivePath.trim());
            }
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link BackupOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.CREATE;
        private String outputDir;
        private boolean dryRun;
        private boolean json;
        private boolean verifyAfterCreate;
        private boolean noIncludeWorkspace;
        private boolean onlyConfig;
        private String verifyArchivePath;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code backup create}）
         */
        public Builder create() {
            this.mode = Mode.CREATE;
            return this;
        }

        /**
         * @param outputDirOrFile create：{@code --output}
         * @return {@code this}
         */
        public Builder output(String outputDirOrFile) {
            this.outputDir = outputDirOrFile;
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
         * @param json {@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * @param verify create：{@code --verify}
         * @return {@code this}
         */
        public Builder verifyAfterCreate(boolean verify) {
            this.verifyAfterCreate = verify;
            return this;
        }

        /**
         * @param noWorkspace {@code --no-include-workspace}
         * @return {@code this}
         */
        public Builder noIncludeWorkspace(boolean noWorkspace) {
            this.noIncludeWorkspace = noWorkspace;
            return this;
        }

        /**
         * @param onlyConfig {@code --only-config}
         * @return {@code this}
         */
        public Builder onlyConfig(boolean onlyConfig) {
            this.onlyConfig = onlyConfig;
            return this;
        }

        /**
 * @param archivePath verify:
         * @return {@code this}
         */
        public Builder verify(String archivePath) {
            this.mode = Mode.VERIFY;
            this.verifyArchivePath = archivePath;
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
 * @return {@link BackupOptions}
         */
        public BackupOptions build() {
            return new BackupOptions(this);
        }
    }
}
