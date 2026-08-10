package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw directory}:directory,"", {@code openclaw message send --target} ID.
 * <p> {@code --channel};., {@code --json}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/directory">directory CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class DirectoryOptions implements CliSubArgs {

    /**
 * directorysubcommand, token ({@code self},{@code peers list} ).
     */
    public enum Mode {
        /** {@code directory self} */
        SELF,
        /** {@code directory peers list} */
        PEERS_LIST,
        /** {@code directory groups list} */
        GROUPS_LIST,
 /** {@code directory groups members}( {@link Builder#groupId}) */
        GROUPS_MEMBERS
    }

    /**
 * directory:{@code self},{@code peers list},{@code groups list},{@code groups members}(with documentationexample).
     */
    private final Mode mode;
    /**
 * {@code --channel}: id (;only).
     */
    private final String channel;
    /**
 * {@code --account}: id(documentation).
     */
    private final String account;
    /**
 * {@code --json}:JSON ,for easy.
     */
    private final boolean json;
    /**
 * {@code --query}: peers/groups (documentationexample).
     */
    private final String query;
    /**
 * {@code --limit}:(documentation peers example 50).
     */
    private final Integer limit;
    /**
 * {@code --group-id}:{@code groups members} subcommand.
     */
    private final String groupId;
    /**
 * .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private DirectoryOptions(Builder b) {
        this.mode = b.mode;
        this.channel = b.channel;
        this.account = b.account;
        this.json = b.json;
        this.query = b.query;
        this.limit = b.limit;
        this.groupId = b.groupId;
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
            case SELF:
                out.add("self");
                break;
            case PEERS_LIST:
                out.add("peers");
                out.add("list");
                break;
            case GROUPS_LIST:
                out.add("groups");
                out.add("list");
                break;
            case GROUPS_MEMBERS:
                out.add("groups");
                out.add("members");
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--channel", channel);
        OpenClawCliArgv.addIfPresent(out, "--account", account);
        OpenClawCliArgv.addIfPresent(out, "--query", query);
        OpenClawCliArgv.addIfNotNull(out, "--limit", limit);
        OpenClawCliArgv.addIfPresent(out, "--group-id", groupId);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link DirectoryOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.PEERS_LIST;
        private String channel;
        private String account;
        private boolean json;
        private String query;
        private Integer limit;
        private String groupId;
        private List<String> extra = new ArrayList<>();

        /**
 * @return {@code this}, {@link Mode#SELF}
         */
        public Builder self() {
            this.mode = Mode.SELF;
            return this;
        }

        /**
 * @return {@code this}, {@link Mode#PEERS_LIST}
         */
        public Builder peersList() {
            this.mode = Mode.PEERS_LIST;
            return this;
        }

        /**
 * @return {@code this}, {@link Mode#GROUPS_LIST}
         */
        public Builder groupsList() {
            this.mode = Mode.GROUPS_LIST;
            return this;
        }

        /**
 * @param groupId ID({@code groups members})
         * @return {@code this}
         */
        public Builder groupsMembers(String groupId) {
            this.mode = Mode.GROUPS_MEMBERS;
            this.groupId = groupId;
            return this;
        }

        /**
         * @param channel {@code --channel}
         * @return {@code this}
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * @param account {@code --account}
         * @return {@code this}
         */
        public Builder account(String account) {
            this.account = account;
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
         * @param query {@code --query}
         * @return {@code this}
         */
        public Builder query(String query) {
            this.query = query;
            return this;
        }

        /**
         * @param limit {@code --limit}
         * @return {@code this}
         */
        public Builder limit(int limit) {
            this.limit = limit;
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
 * @return {@link DirectoryOptions}
         */
        public DirectoryOptions build() {
            return new DirectoryOptions(this);
        }
    }
}
