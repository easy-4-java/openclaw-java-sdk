package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw approvals}( {@code exec-approvals}):,Gateway node exec approval glob allowlist.
 * <p> {@code ~/.openclaw/exec-approvals.json};{@code --gateway} {@code --node} .
 * {@code set} JSON5, {@code --file} {@code --stdin} mutually exclusive.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/approvals">approvals CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class ApprovalsOptions implements CliSubArgs {

    /**
 * approvals subcommand:, allowlist.
     */
    public enum Verb {
 /** {@code approvals get}: exec Priority. */
        GET,
 /** {@code approvals set}:approval JSON. */
        SET,
 /** {@code approvals allowlist add}: agent glob. */
        ALLOWLIST_ADD,
 /** {@code approvals allowlist remove}: glob. */
        ALLOWLIST_REMOVE
    }

 /** get / set / allowlist . */
    private final Verb verb;
    /**
 * {@code --node}: {@code openclaw nodes} node.
     */
    private final String node;
    /**
 * {@code --gateway}:Gatewayapproval.
     */
    private final boolean gateway;
    /**
 * {@code --url}:node/gateway RPC WebSocket(with documentation Common options ).
     */
    private final String url;
    /**
 * {@code --token}:Gateway token.
     */
    private final String token;
    /**
 * {@code --password}:Gateway.
     */
    private final String password;
    /**
 * {@code --timeout}:RPC timeout.
     */
    private final String timeout;
    /**
 * {@code --json}:.
     */
    private final boolean json;
    /**
 * set:{@code --file} approval JSON .
     */
    private final String file;
    /**
 * set:{@code --stdin} JSON5.
     */
    private final boolean stdin;
    /**
 * allowlist add/remove:glob .
     */
    private final String allowlistPattern;
    /**
 * allowlist:{@code --agent} ( {@code *} agent).
     */
    private final String agent;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ApprovalsOptions(Builder b) {
        this.verb = b.verb;
        this.node = b.node;
        this.gateway = b.gateway;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.json = b.json;
        this.file = b.file;
        this.stdin = b.stdin;
        this.allowlistPattern = b.allowlistPattern;
        this.agent = b.agent;
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
        switch (verb) {
            case GET:
                out.add("get");
                break;
            case SET:
                out.add("set");
                OpenClawCliArgv.addIfPresent(out, "--file", file);
                if (stdin) {
                    out.add("--stdin");
                }
                break;
            case ALLOWLIST_ADD:
                out.add("allowlist");
                out.add("add");
                if (allowlistPattern != null && OpenClawStrings.isNotBlank(allowlistPattern)) {
                    out.add(allowlistPattern.trim());
                }
                break;
            case ALLOWLIST_REMOVE:
                out.add("allowlist");
                out.add("remove");
                if (allowlistPattern != null && OpenClawStrings.isNotBlank(allowlistPattern)) {
                    out.add(allowlistPattern.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--node", node);
        OpenClawCliArgv.addFlag(out, "--gateway", gateway);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ApprovalsOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.GET;
        private String node;
        private boolean gateway;
        private String url;
        private String token;
        private String password;
        private String timeout;
        private boolean json;
        private String file;
        private boolean stdin;
        private String allowlistPattern;
        private String agent;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code approvals get}）
         */
        public Builder get() {
            this.verb = Verb.GET;
            return this;
        }

        /**
         * @return {@code this}（{@code approvals set}）
         */
        public Builder set() {
            this.verb = Verb.SET;
            return this;
        }

        /**
         * @param path set：{@code --file}
         * @return {@code this}
         */
        public Builder file(String path) {
            this.file = path;
            return this;
        }

        /**
         * @param stdin set：{@code --stdin}
         * @return {@code this}
         */
        public Builder stdin(boolean stdin) {
            this.stdin = stdin;
            return this;
        }

        /**
 * @param pattern allowlist add:
         * @return {@code this}
         */
        public Builder allowlistAdd(String pattern) {
            this.verb = Verb.ALLOWLIST_ADD;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
 * @param pattern allowlist remove:
         * @return {@code this}
         */
        public Builder allowlistRemove(String pattern) {
            this.verb = Verb.ALLOWLIST_REMOVE;
            this.allowlistPattern = pattern;
            return this;
        }

        /**
         * @param node {@code --node}
         * @return {@code this}
         */
        public Builder node(String node) {
            this.node = node;
            return this;
        }

        /**
         * @param gateway {@code --gateway}
         * @return {@code this}
         */
        public Builder gateway(boolean gateway) {
            this.gateway = gateway;
            return this;
        }

        /**
         * @param url {@code --url}
         * @return {@code this}
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * @param password {@code --password}
         * @return {@code this}
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * @param timeout {@code --timeout}
         * @return {@code this}
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
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
         * @param agent {@code --agent}
         * @return {@code this}
         */
        public Builder agent(String agent) {
            this.agent = agent;
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
 * @return {@link ApprovalsOptions}
         */
        public ApprovalsOptions build() {
            return new ApprovalsOptions(this);
        }
    }
}
