package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw exec-approvals}( {@code approvals}): exec approval(gateway node host).
 * <p>
 * {@code get},{@code set},{@code allowlist add <pattern>},{@code allowlist remove <pattern>} subcommand.
 * {@code --node},{@code --gateway};{@code set} {@code --file}/{@code --stdin};
 * {@code allowlist} {@code --agent}.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/exec-approvals">exec-approvals CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ExecApprovalsOptions implements CliSubArgs {

 /** subcommand. */
    public enum Mode {
 /** {@code get}:approval. */
        GET,
 /** {@code set}:approval JSON. */
        SET,
 /** {@code allowlist add <pattern>}:. */
        ALLOWLIST_ADD,
 /** {@code allowlist remove <pattern>}:. */
        ALLOWLIST_REMOVE,
 /** (subcommand):approval. */
        DEFAULT
    }

    private final Mode mode;
 /** allowlist add/remove: {@code <pattern>}. */
    private final String pattern;
 /** {@code --node}:node id//IP. */
    private final String node;
 /** {@code --gateway}: gateway approval. */
    private final boolean gateway;
 /** set:{@code --file} JSON . */
    private final String file;
 /** set:{@code --stdin} JSON. */
    private final boolean stdin;
 /** allowlist:{@code --agent} agent id( {@code *}). */
    private final String agent;

    private ExecApprovalsOptions(Builder b) {
        this.mode = b.mode;
        this.pattern = b.pattern;
        this.node = b.node;
        this.gateway = b.gateway;
        this.file = b.file;
        this.stdin = b.stdin;
        this.agent = b.agent;
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
            case GET:
                out.add("get");
                break;
            case SET:
                out.add("set");
                break;
            case ALLOWLIST_ADD:
                out.add("allowlist");
                out.add("add");
                if (pattern != null && !pattern.isEmpty()) {
                    out.add(pattern);
                }
                break;
            case ALLOWLIST_REMOVE:
                out.add("allowlist");
                out.add("remove");
                if (pattern != null && !pattern.isEmpty()) {
                    out.add(pattern);
                }
                break;
            case DEFAULT:
            default:
                // 父命令默认动作：不输出子命令 token
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--node", node);
        OpenClawCliArgv.addFlag(out, "--gateway", gateway);
        OpenClawCliArgv.addIfPresent(out, "--file", file);
        OpenClawCliArgv.addFlag(out, "--stdin", stdin);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ExecApprovalsOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.DEFAULT;
        private String pattern;
        private String node;
        private boolean gateway;
        private String file;
        private boolean stdin;
        private String agent;

 /** {@code get} subcommand. */
        public Builder get() { this.mode = Mode.GET; return this; }
 /** {@code set} subcommand. */
        public Builder set() { this.mode = Mode.SET; return this; }
 /** {@code allowlist add <pattern>} subcommand. */
        public Builder allowlistAdd(String pattern) { this.mode = Mode.ALLOWLIST_ADD; this.pattern = pattern; return this; }
 /** {@code allowlist remove <pattern>} subcommand. */
        public Builder allowlistRemove(String pattern) { this.mode = Mode.ALLOWLIST_REMOVE; this.pattern = pattern; return this; }
 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** {@code --node}:node id//IP. */
        public Builder node(String node) { this.node = node; return this; }
 /** {@code --gateway}: gateway approval. */
        public Builder gateway(boolean gateway) { this.gateway = gateway; return this; }
 /** set:{@code --file} JSON . */
        public Builder file(String file) { this.file = file; return this; }
 /** set:{@code --stdin} JSON. */
        public Builder stdin(boolean stdin) { this.stdin = stdin; return this; }
 /** allowlist:{@code --agent} agent id( {@code *}). */
        public Builder agent(String agent) { this.agent = agent; return this; }

        /**
 * @return {@link ExecApprovalsOptions}
         */
        public ExecApprovalsOptions build() {
            return new ExecApprovalsOptions(this);
        }
    }
}
