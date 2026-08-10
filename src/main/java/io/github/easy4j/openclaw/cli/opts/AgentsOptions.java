package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * {@code openclaw agents}: agent( workspace,authentication).
 * <p>stream agent;skillSee {@code agents.defaults.skills} . flag {@link Builder#extra(String...)}.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/agents">agents CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class AgentsOptions implements CliSubArgs {

    /**
 * subcommand: {@code openclaw agents} {@code list} CLI .
     */
    public enum Verb {
        /**
 * {@code openclaw agents} subcommand: {@code list} token, Gateway list .
         */
        DEFAULT_LIST,
        /**
 * {@code agents list}.
         */
        LIST,
        /**
 * {@code agents add [name]}: agent.
         */
        ADD,
        /**
 * {@code agents bindings}:.
         */
        BINDINGS,
        /**
 * {@code agents bind}: agent .
         */
        BIND,
        /**
 * {@code agents unbind}: {@code --all} .
         */
        UNBIND,
        /**
 * {@code agents set-identity}: {@code agents.list[].identity}(emoji).
         */
        SET_IDENTITY,
        /**
 * {@code agents delete}: workspace ( force ,Seedocumentation).
         */
        DELETE
    }

 /** agents subcommand. */
    private final Verb verb;
    /**
 * list / DEFAULT_LIST:{@code --json} .
     */
    private final boolean listJson;
    /**
 * list:{@code --bindings} , agent .
     */
    private final boolean listBindings;
    /**
 * add: agent id ({@code main} ).
     */
    private final String addName;
    /**
 * add:{@code --workspace} ; add name Required.
     */
    private final String workspace;
    /**
 * add:{@code --model} .
     */
    private final String model;
    /**
 * add:{@code --agent-dir} agent directory.
     */
    private final String agentDir;
    /**
 * add / bind / unbind: {@code --bind channel:account} (account documentation).
     */
    private final List<String> bindValues;
    /**
 * add:{@code --non-interactive} ; add flag .
     */
    private final boolean nonInteractive;
    /**
 * add:{@code --json} .
     */
    private final boolean addJson;
    /**
 * bindings:{@code --agent} agent .
     */
    private final String bindingsAgent;
    /**
     * bindings：{@code --json}。
     */
    private final boolean bindingsJson;
    /**
 * bind:{@code --agent}, agent.
     */
    private final String bindAgent;
    /**
     * bind：{@code --json}。
     */
    private final boolean bindJson;
    /**
 * unbind:{@code --agent}, agent.
     */
    private final String unbindAgent;
    /**
 * unbind:{@code --all} agent ( {@code --bind} ).
     */
    private final boolean unbindAll;
    /**
     * unbind：{@code --json}。
     */
    private final boolean unbindJson;
    /**
 * delete:agent id ( {@code main}).
     */
    private final String deleteAgentId;
    /**
 * delete:{@code --force} skips.
     */
    private final boolean deleteForce;
    /**
     * delete：{@code --json}。
     */
    private final boolean deleteJson;
    /**
 * set-identity:{@code --agent} {@code --workspace} mutually exclusiveComposes( agent workspace agent).
     */
    private final String identityAgent;
    /**
 * set-identity:{@code --workspace} Used for agent {@code IDENTITY.md}.
     */
    private final String identityWorkspace;
    /**
 * set-identity:{@code --identity-file} .
     */
    private final String identityFile;
    /**
 * set-identity:{@code --from-identity} workspace {@code --identity-file} {@code IDENTITY.md}.
     */
    private final boolean fromIdentity;
    /**
 * set-identity:{@code --name} .
     */
    private final String identityName;
    /**
 * set-identity:{@code --theme} .
     */
    private final String identityTheme;
    /**
 * set-identity:{@code --emoji} .
     */
    private final String identityEmoji;
    /**
 * set-identity:{@code --avatar} workspace ,http(s) URL data URI.
     */
    private final String identityAvatar;
    /**
     * set-identity：{@code --json}。
     */
    private final boolean identityJson;
    /**
 * documentation argv,.
     */
    private final List<String> extra;

    private AgentsOptions(Builder b) {
        this.verb = b.verb;
        this.listJson = b.listJson;
        this.listBindings = b.listBindings;
        this.addName = b.addName;
        this.workspace = b.workspace;
        this.model = b.model;
        this.agentDir = b.agentDir;
        this.bindValues = b.bindValues == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.bindValues);
        this.nonInteractive = b.nonInteractive;
        this.addJson = b.addJson;
        this.bindingsAgent = b.bindingsAgent;
        this.bindingsJson = b.bindingsJson;
        this.bindAgent = b.bindAgent;
        this.bindJson = b.bindJson;
        this.unbindAgent = b.unbindAgent;
        this.unbindAll = b.unbindAll;
        this.unbindJson = b.unbindJson;
        this.deleteAgentId = b.deleteAgentId;
        this.deleteForce = b.deleteForce;
        this.deleteJson = b.deleteJson;
        this.identityAgent = b.identityAgent;
        this.identityWorkspace = b.identityWorkspace;
        this.identityFile = b.identityFile;
        this.fromIdentity = b.fromIdentity;
        this.identityName = b.identityName;
        this.identityTheme = b.identityTheme;
        this.identityEmoji = b.identityEmoji;
        this.identityAvatar = b.identityAvatar;
        this.identityJson = b.identityJson;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (verb) {
            case DEFAULT_LIST:
                break;
            case LIST:
                out.add("list");
                break;
            case ADD:
                out.add("add");
                if (addName != null && OpenClawStrings.isNotBlank(addName)) {
                    out.add(addName.trim());
                }
                break;
            case BINDINGS:
                out.add("bindings");
                break;
            case BIND:
                out.add("bind");
                break;
            case UNBIND:
                out.add("unbind");
                break;
            case SET_IDENTITY:
                out.add("set-identity");
                break;
            case DELETE:
                out.add("delete");
                if (deleteAgentId != null && OpenClawStrings.isNotBlank(deleteAgentId)) {
                    out.add(deleteAgentId.trim());
                }
                break;
            default:
                break;
        }
        if (verb == Verb.DEFAULT_LIST || verb == Verb.LIST) {
            OpenClawCliArgv.addFlag(out, "--json", listJson);
            OpenClawCliArgv.addFlag(out, "--bindings", listBindings);
        }
        if (verb == Verb.ADD) {
            OpenClawCliArgv.addIfPresent(out, "--workspace", workspace);
            OpenClawCliArgv.addIfPresent(out, "--model", model);
            OpenClawCliArgv.addIfPresent(out, "--agent-dir", agentDir);
            OpenClawCliArgv.addRepeatable(out, "--bind", bindValues);
            OpenClawCliArgv.addFlag(out, "--non-interactive", nonInteractive);
            OpenClawCliArgv.addFlag(out, "--json", addJson);
        }
        if (verb == Verb.BINDINGS) {
            OpenClawCliArgv.addIfPresent(out, "--agent", bindingsAgent);
            OpenClawCliArgv.addFlag(out, "--json", bindingsJson);
        }
        if (verb == Verb.BIND) {
            OpenClawCliArgv.addIfPresent(out, "--agent", bindAgent);
            OpenClawCliArgv.addRepeatable(out, "--bind", bindValues);
            OpenClawCliArgv.addFlag(out, "--json", bindJson);
        }
        if (verb == Verb.UNBIND) {
            OpenClawCliArgv.addIfPresent(out, "--agent", unbindAgent);
            OpenClawCliArgv.addRepeatable(out, "--bind", bindValues);
            OpenClawCliArgv.addFlag(out, "--all", unbindAll);
            OpenClawCliArgv.addFlag(out, "--json", unbindJson);
        }
        if (verb == Verb.DELETE) {
            OpenClawCliArgv.addFlag(out, "--force", deleteForce);
            OpenClawCliArgv.addFlag(out, "--json", deleteJson);
        }
        if (verb == Verb.SET_IDENTITY) {
            OpenClawCliArgv.addIfPresent(out, "--agent", identityAgent);
            OpenClawCliArgv.addIfPresent(out, "--workspace", identityWorkspace);
            OpenClawCliArgv.addIfPresent(out, "--identity-file", identityFile);
            OpenClawCliArgv.addFlag(out, "--from-identity", fromIdentity);
            OpenClawCliArgv.addIfPresent(out, "--name", identityName);
            OpenClawCliArgv.addIfPresent(out, "--theme", identityTheme);
            OpenClawCliArgv.addIfPresent(out, "--emoji", identityEmoji);
            OpenClawCliArgv.addIfPresent(out, "--avatar", identityAvatar);
            OpenClawCliArgv.addFlag(out, "--json", identityJson);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link AgentsOptions}.
     */
    public static final class Builder {
        private Verb verb = Verb.DEFAULT_LIST;
        private boolean listJson;
        private boolean listBindings;
        private String addName;
        private String workspace;
        private String model;
        private String agentDir;
        private List<String> bindValues = new ArrayList<>();
        private boolean nonInteractive;
        private boolean addJson;
        private String bindingsAgent;
        private boolean bindingsJson;
        private String bindAgent;
        private boolean bindJson;
        private String unbindAgent;
        private boolean unbindAll;
        private boolean unbindJson;
        private String deleteAgentId;
        private boolean deleteForce;
        private boolean deleteJson;
        private String identityAgent;
        private String identityWorkspace;
        private String identityFile;
        private boolean fromIdentity;
        private String identityName;
        private String identityTheme;
        private String identityEmoji;
        private String identityAvatar;
        private boolean identityJson;
        private List<String> extra = new ArrayList<>();

 /** list( {@code openclaw agents} ). */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

 /** {@code agents list}. */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

 /** list / list:{@code --json}. */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /** list：{@code --bindings}。 */
        public Builder listBindings(boolean bindings) {
            this.listBindings = bindings;
            return this;
        }

        /** {@code agents add [name]}。 */
        public Builder add(String name) {
            this.verb = Verb.ADD;
            this.addName = name;
            return this;
        }

        public Builder workspace(String workspace) {
            this.workspace = workspace;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder agentDir(String agentDir) {
            this.agentDir = agentDir;
            return this;
        }

 /** {@code --bind}. */
        public Builder bind(String channelBinding) {
            if (channelBinding != null && OpenClawStrings.isNotBlank(channelBinding)) {
                this.bindValues.add(channelBinding.trim());
            }
            return this;
        }

        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
            return this;
        }

        public Builder addJson(boolean json) {
            this.addJson = json;
            return this;
        }

        public Builder bindings() {
            this.verb = Verb.BINDINGS;
            return this;
        }

        public Builder bindingsAgent(String agent) {
            this.bindingsAgent = agent;
            return this;
        }

        public Builder bindingsJson(boolean json) {
            this.bindingsJson = json;
            return this;
        }

        public Builder bindCommand() {
            this.verb = Verb.BIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        public Builder bindAgent(String agent) {
            this.bindAgent = agent;
            return this;
        }

        public Builder bindJson(boolean json) {
            this.bindJson = json;
            return this;
        }

        public Builder unbind() {
            this.verb = Verb.UNBIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        public Builder unbindAgent(String agent) {
            this.unbindAgent = agent;
            return this;
        }

        public Builder unbindAll(boolean all) {
            this.unbindAll = all;
            return this;
        }

        public Builder unbindJson(boolean json) {
            this.unbindJson = json;
            return this;
        }

        /** {@code agents delete <id>}。 */
        public Builder delete(String agentId) {
            this.verb = Verb.DELETE;
            this.deleteAgentId = agentId;
            return this;
        }

        public Builder deleteForce(boolean force) {
            this.deleteForce = force;
            return this;
        }

        public Builder deleteJson(boolean json) {
            this.deleteJson = json;
            return this;
        }

        public Builder setIdentity() {
            this.verb = Verb.SET_IDENTITY;
            return this;
        }

        public Builder identityAgent(String agent) {
            this.identityAgent = agent;
            return this;
        }

        public Builder identityWorkspace(String workspace) {
            this.identityWorkspace = workspace;
            return this;
        }

        public Builder identityFile(String path) {
            this.identityFile = path;
            return this;
        }

        public Builder fromIdentity(boolean fromIdentity) {
            this.fromIdentity = fromIdentity;
            return this;
        }

        public Builder identityName(String name) {
            this.identityName = name;
            return this;
        }

        public Builder identityTheme(String theme) {
            this.identityTheme = theme;
            return this;
        }

        public Builder identityEmoji(String emoji) {
            this.identityEmoji = emoji;
            return this;
        }

        public Builder identityAvatar(String avatar) {
            this.identityAvatar = avatar;
            return this;
        }

        public Builder identityJson(boolean json) {
            this.identityJson = json;
            return this;
        }

        /**
 * token(documentation flag).
         *
 * @param tokens argv
         * @return this
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        public AgentsOptions build() {
            return new AgentsOptions(this);
        }
    }
}
