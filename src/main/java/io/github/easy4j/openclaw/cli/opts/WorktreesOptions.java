package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw worktrees}:,restore worktree.
 * <p>
 * {@code list},{@code create <repoRoot>},{@code remove <id>},{@code restore <id>},{@code gc} subcommand.
 * help.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/worktrees">worktrees CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class WorktreesOptions implements CliSubArgs {

 /** subcommand. */
    public enum Mode {
 /** {@code list}: worktree. */
        LIST,
 /** {@code create <repoRoot>}: worktree. */
        CREATE,
 /** {@code remove <id>}: worktree. */
        REMOVE,
 /** {@code restore <id>}:restore worktree. */
        RESTORE,
 /** {@code gc}:. */
        GC
    }

    private final Mode mode;
 /** create: {@code <repoRoot>} git checkout. */
    private final String repoRoot;
 /** remove/restore: {@code <id>} worktree id. */
    private final String id;
 /** create:{@code --name} worktree . */
    private final String name;
 /** create:{@code --base-ref} Git ref. */
    private final String baseRef;
 /** remove:{@code --force} . */
    private final boolean force;
 /** {@code --json}:JSON . */
    private final boolean json;

    private WorktreesOptions(Builder b) {
        this.mode = b.mode;
        this.repoRoot = b.repoRoot;
        this.id = b.id;
        this.name = b.name;
        this.baseRef = b.baseRef;
        this.force = b.force;
        this.json = b.json;
    }

    /**
 * @return {@link Builder}( {@link Mode#LIST})
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
            case CREATE:
                out.add("create");
                if (repoRoot != null && !repoRoot.isEmpty()) {
                    out.add(repoRoot);
                }
                break;
            case REMOVE:
                out.add("remove");
                if (id != null && !id.isEmpty()) {
                    out.add(id);
                }
                break;
            case RESTORE:
                out.add("restore");
                if (id != null && !id.isEmpty()) {
                    out.add(id);
                }
                break;
            case GC:
                out.add("gc");
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--name", name);
        OpenClawCliArgv.addIfPresent(out, "--base-ref", baseRef);
        OpenClawCliArgv.addFlag(out, "--force", force);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link WorktreesOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.LIST;
        private String repoRoot;
        private String id;
        private String name;
        private String baseRef;
        private boolean force;
        private boolean json;

 /** {@code list} subcommand. */
        public Builder list() { this.mode = Mode.LIST; return this; }
 /** {@code create <repoRoot>} subcommand. */
        public Builder create(String repoRoot) { this.mode = Mode.CREATE; this.repoRoot = repoRoot; return this; }
 /** {@code remove <id>} subcommand. */
        public Builder remove(String id) { this.mode = Mode.REMOVE; this.id = id; return this; }
 /** {@code restore <id>} subcommand. */
        public Builder restore(String id) { this.mode = Mode.RESTORE; this.id = id; return this; }
 /** {@code gc} subcommand. */
        public Builder gc() { this.mode = Mode.GC; return this; }
 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** create: {@code <repoRoot>} git checkout. */
        public Builder repoRoot(String repoRoot) { this.repoRoot = repoRoot; return this; }
 /** remove/restore: {@code <id>} worktree id. */
        public Builder id(String id) { this.id = id; return this; }
 /** create:{@code --name} worktree . */
        public Builder name(String name) { this.name = name; return this; }
 /** create:{@code --base-ref} Git ref. */
        public Builder baseRef(String baseRef) { this.baseRef = baseRef; return this; }
 /** remove:{@code --force} . */
        public Builder force(boolean force) { this.force = force; return this; }
 /** {@code --json}:JSON . */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
 * @return {@link WorktreesOptions}
         */
        public WorktreesOptions build() {
            return new WorktreesOptions(this);
        }
    }
}
