package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw mcp}:{@code mcp serve} OpenClaw stdio MCP Gateway session;
 * {@code list|show|set|unset} {@code mcp.servers} ().
 *
 * @see <a href="https://docs.openclaw.ai/cli/mcp">mcp CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class McpOptions implements CliSubArgs {

    /**
 * {@code mcp serve --claude-channel-mode}: Claude channel.
     */
    public enum ClaudeChannelMode {
 /** {@code off}:only MCP , Claude . */
        OFF("off"),
 /** {@code on}: {@code notifications/claude/channel} . */
        ON("on"),
 /** {@code auto}: {@code on} (documentation:). */
        AUTO("auto");

        private final String cliValue;

        ClaudeChannelMode(String cliValue) {
            this.cliValue = cliValue;
        }

        String cliValue() {
            return cliValue;
        }
    }

    /**
 * mcp subcommand:stdio serve, MCP server .
     */
    public enum Mode {
 /** {@code mcp serve}:connection Gateway MCP sessionevent. */
        SERVE,
 /** {@code mcp list}: {@code mcp.servers} . */
        LIST,
 /** {@code mcp show}: server JSON. */
        SHOW,
 /** {@code mcp set}: server (JSON objectcharacters). */
        SET,
 /** {@code mcp unset}: server. */
        UNSET
    }

 /** serve registry subcommand. */
    private final Mode mode;
    /**
 * serve:{@code --url} Gateway WebSocket( acp ).
     */
    private final String url;
    /**
 * serve:{@code --token} Gateway token(process).
     */
    private final String token;
    /**
 * serve:{@code --token-file} token.
     */
    private final String tokenFile;
    /**
 * serve:{@code --password} Gateway.
     */
    private final String password;
    /**
 * serve:{@code --password-file} .
     */
    private final String passwordFile;
    /**
 * serve:{@code --claude-channel-mode} See {@link ClaudeChannelMode}.
     */
    private final ClaudeChannelMode claudeChannelMode;
    /**
 * serve:{@code --verbose} stderr diagnostic.
     */
    private final boolean verbose;
    /**
 * show:server ;object(documentation).
     */
    private final String showName;
    /**
     * show：{@code --json}。
     */
    private final boolean showJson;
    /**
 * set:server .
     */
    private final String setName;
    /**
 * set: JSON object( server ).
     */
    private final String setJson;
    /**
 * unset: server .
     */
    private final String unsetName;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private McpOptions(Builder b) {
        this.mode = b.mode;
        this.url = b.url;
        this.token = b.token;
        this.tokenFile = b.tokenFile;
        this.password = b.password;
        this.passwordFile = b.passwordFile;
        this.claudeChannelMode = b.claudeChannelMode;
        this.verbose = b.verbose;
        this.showName = b.showName;
        this.showJson = b.showJson;
        this.setName = b.setName;
        this.setJson = b.setJson;
        this.unsetName = b.unsetName;
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
            case SERVE:
                out.add("serve");
                OpenClawCliArgv.addIfPresent(out, "--url", url);
                OpenClawCliArgv.addIfPresent(out, "--token", token);
                OpenClawCliArgv.addIfPresent(out, "--token-file", tokenFile);
                OpenClawCliArgv.addIfPresent(out, "--password", password);
                OpenClawCliArgv.addIfPresent(out, "--password-file", passwordFile);
                if (claudeChannelMode != null) {
                    out.add("--claude-channel-mode");
                    out.add(claudeChannelMode.cliValue());
                }
                OpenClawCliArgv.addFlag(out, "--verbose", verbose);
                break;
            case LIST:
                out.add("list");
                break;
            case SHOW:
                out.add("show");
                if (showName != null && OpenClawStrings.isNotBlank(showName)) {
                    out.add(showName.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", showJson);
                break;
            case SET:
                out.add("set");
                if (setName != null && OpenClawStrings.isNotBlank(setName)) {
                    out.add(setName.trim());
                }
                if (setJson != null && OpenClawStrings.isNotBlank(setJson)) {
                    out.add(setJson);
                }
                break;
            case UNSET:
                out.add("unset");
                if (unsetName != null && OpenClawStrings.isNotBlank(unsetName)) {
                    out.add(unsetName.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link McpOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.SERVE;
        private String url;
        private String token;
        private String tokenFile;
        private String password;
        private String passwordFile;
        private ClaudeChannelMode claudeChannelMode;
        private boolean verbose;
        private String showName;
        private boolean showJson;
        private String setName;
        private String setJson;
        private String unsetName;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code mcp serve}）
         */
        public Builder serve() {
            this.mode = Mode.SERVE;
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
         * @param path {@code --token-file}
         * @return {@code this}
         */
        public Builder tokenFile(String path) {
            this.tokenFile = path;
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
         * @param path {@code --password-file}
         * @return {@code this}
         */
        public Builder passwordFile(String path) {
            this.passwordFile = path;
            return this;
        }

        /**
         * @param mode {@code --claude-channel-mode}
         * @return {@code this}
         */
        public Builder claudeChannelMode(ClaudeChannelMode mode) {
            this.claudeChannelMode = mode;
            return this;
        }

        /**
         * @param verbose {@code --verbose}
         * @return {@code this}
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * @return {@code this}（{@code mcp list}）
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
 * {@code show};{@code name} When empty,Equivalent todocumentation""object.
         *
 * @param name MCP ( null)
         * @return {@code this}
         */
        public Builder show(String name) {
            this.mode = Mode.SHOW;
            this.showName = name;
            return this;
        }

        /**
         * @param json show：{@code --json}
         * @return {@code this}
         */
        public Builder showJson(boolean json) {
            this.showJson = json;
            return this;
        }

        /**
 * {@code set <name> <json>},{@code json} JSON characters.
         *
 * @param name MCP
 * @param json JSON
         * @return {@code this}
         */
        public Builder set(String name, String json) {
            this.mode = Mode.SET;
            this.setName = name;
            this.setJson = json;
            return this;
        }

        /**
 * @param name unset:
         * @return {@code this}
         */
        public Builder unset(String name) {
            this.mode = Mode.UNSET;
            this.unsetName = name;
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
 * @return {@link McpOptions}
         */
        public McpOptions build() {
            return new McpOptions(this);
        }
    }
}
