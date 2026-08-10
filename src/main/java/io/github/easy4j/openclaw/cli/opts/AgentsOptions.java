package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * openclaw `agents` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AgentsOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `default_list` 协议模式；序列化时使用该固定取值。
         */
        DEFAULT_LIST,
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `add` 协议模式；序列化时使用该固定取值。
         */
        ADD,
        /**
         * 选择 `bindings` 协议模式；序列化时使用该固定取值。
         */
        BINDINGS,
        /**
         * 选择 `bind` 协议模式；序列化时使用该固定取值。
         */
        BIND,
        /**
         * 选择 `unbind` 协议模式；序列化时使用该固定取值。
         */
        UNBIND,
        /**
         * 选择 `set_identity` 协议模式；序列化时使用该固定取值。
         */
        SET_IDENTITY,
        /**
         * 选择 `delete` 协议模式；序列化时使用该固定取值。
         */
        DELETE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 `--list-json` 开关。
     */
    private final boolean listJson;
    /**
     * 是否向 openclaw 子命令追加 `--list-bindings` 开关。
     */
    private final boolean listBindings;
    /**
     * 传给 openclaw 子命令 `--add-name` 选项的内容；为 null 时通常省略。
     */
    private final String addName;
    /**
     * 传给 openclaw 子命令 `--workspace` 选项的内容；为 null 时通常省略。
     */
    private final String workspace;
    /**
     * 传给 openclaw 子命令 `--model` 选项的内容；为 null 时通常省略。
     */
    private final String model;
    /**
     * 传给 openclaw 子命令 `--agent-dir` 选项的内容；为 null 时通常省略。
     */
    private final String agentDir;
    /**
     * 传给 openclaw 子命令 `--bind-values` 选项的内容；为 null 时通常省略。
     */
    private final List<String> bindValues;
    /**
     * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
     */
    private final boolean nonInteractive;
    /**
     * 是否向 openclaw 子命令追加 `--add-json` 开关。
     */
    private final boolean addJson;
    /**
     * 传给 openclaw 子命令 `--bindings-agent` 选项的内容；为 null 时通常省略。
     */
    private final String bindingsAgent;
    /**
     * 是否向 openclaw 子命令追加 `--bindings-json` 开关。
     */
    private final boolean bindingsJson;
    /**
     * 传给 openclaw 子命令 `--bind-agent` 选项的内容；为 null 时通常省略。
     */
    private final String bindAgent;
    /**
     * 是否向 openclaw 子命令追加 `--bind-json` 开关。
     */
    private final boolean bindJson;
    /**
     * 传给 openclaw 子命令 `--unbind-agent` 选项的内容；为 null 时通常省略。
     */
    private final String unbindAgent;
    /**
     * 是否向 openclaw 子命令追加 `--unbind-all` 开关。
     */
    private final boolean unbindAll;
    /**
     * 是否向 openclaw 子命令追加 `--unbind-json` 开关。
     */
    private final boolean unbindJson;
    /**
     * 传给 openclaw 子命令 `--delete-agent-id` 选项的内容；为 null 时通常省略。
     */
    private final String deleteAgentId;
    /**
     * 是否向 openclaw 子命令追加 `--delete-force` 开关。
     */
    private final boolean deleteForce;
    /**
     * 是否向 openclaw 子命令追加 `--delete-json` 开关。
     */
    private final boolean deleteJson;
    /**
     * 传给 openclaw 子命令 `--identity-agent` 选项的内容；为 null 时通常省略。
     */
    private final String identityAgent;
    /**
     * 传给 openclaw 子命令 `--identity-workspace` 选项的内容；为 null 时通常省略。
     */
    private final String identityWorkspace;
    /**
     * 传给 openclaw 子命令 `--identity-file` 选项的内容；为 null 时通常省略。
     */
    private final String identityFile;
    /**
     * 是否向 openclaw 子命令追加 `--from-identity` 开关。
     */
    private final boolean fromIdentity;
    /**
     * 传给 openclaw 子命令 `--identity-name` 选项的内容；为 null 时通常省略。
     */
    private final String identityName;
    /**
     * 传给 openclaw 子命令 `--identity-theme` 选项的内容；为 null 时通常省略。
     */
    private final String identityTheme;
    /**
     * 传给 openclaw 子命令 `--identity-emoji` 选项的内容；为 null 时通常省略。
     */
    private final String identityEmoji;
    /**
     * 传给 openclaw 子命令 `--identity-avatar` 选项的内容；为 null 时通常省略。
     */
    private final String identityAvatar;
    /**
     * 是否向 openclaw 子命令追加 `--identity-json` 开关。
     */
    private final boolean identityJson;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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

    /**
     * 创建空白构建器，供调用方链式设置 `AgentsOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
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
     * 链式构建器，逐项收集 AgentsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 AgentsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.DEFAULT_LIST;
        /**
         * 是否向 openclaw 子命令追加 `--list-json` 开关。
         */
        private boolean listJson;
        /**
         * 是否向 openclaw 子命令追加 `--list-bindings` 开关。
         */
        private boolean listBindings;
        /**
         * 传给 openclaw 子命令 `--add-name` 选项的内容；为 null 时通常省略。
         */
        private String addName;
        /**
         * 传给 openclaw 子命令 `--workspace` 选项的内容；为 null 时通常省略。
         */
        private String workspace;
        /**
         * 传给 openclaw 子命令 `--model` 选项的内容；为 null 时通常省略。
         */
        private String model;
        /**
         * 传给 openclaw 子命令 `--agent-dir` 选项的内容；为 null 时通常省略。
         */
        private String agentDir;
        /**
         * 传给 openclaw 子命令 `--bind-values` 选项的内容；为 null 时通常省略。
         */
        private List<String> bindValues = new ArrayList<>();
        /**
         * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
         */
        private boolean nonInteractive;
        /**
         * 是否向 openclaw 子命令追加 `--add-json` 开关。
         */
        private boolean addJson;
        /**
         * 传给 openclaw 子命令 `--bindings-agent` 选项的内容；为 null 时通常省略。
         */
        private String bindingsAgent;
        /**
         * 是否向 openclaw 子命令追加 `--bindings-json` 开关。
         */
        private boolean bindingsJson;
        /**
         * 传给 openclaw 子命令 `--bind-agent` 选项的内容；为 null 时通常省略。
         */
        private String bindAgent;
        /**
         * 是否向 openclaw 子命令追加 `--bind-json` 开关。
         */
        private boolean bindJson;
        /**
         * 传给 openclaw 子命令 `--unbind-agent` 选项的内容；为 null 时通常省略。
         */
        private String unbindAgent;
        /**
         * 是否向 openclaw 子命令追加 `--unbind-all` 开关。
         */
        private boolean unbindAll;
        /**
         * 是否向 openclaw 子命令追加 `--unbind-json` 开关。
         */
        private boolean unbindJson;
        /**
         * 传给 openclaw 子命令 `--delete-agent-id` 选项的内容；为 null 时通常省略。
         */
        private String deleteAgentId;
        /**
         * 是否向 openclaw 子命令追加 `--delete-force` 开关。
         */
        private boolean deleteForce;
        /**
         * 是否向 openclaw 子命令追加 `--delete-json` 开关。
         */
        private boolean deleteJson;
        /**
         * 传给 openclaw 子命令 `--identity-agent` 选项的内容；为 null 时通常省略。
         */
        private String identityAgent;
        /**
         * 传给 openclaw 子命令 `--identity-workspace` 选项的内容；为 null 时通常省略。
         */
        private String identityWorkspace;
        /**
         * 传给 openclaw 子命令 `--identity-file` 选项的内容；为 null 时通常省略。
         */
        private String identityFile;
        /**
         * 是否向 openclaw 子命令追加 `--from-identity` 开关。
         */
        private boolean fromIdentity;
        /**
         * 传给 openclaw 子命令 `--identity-name` 选项的内容；为 null 时通常省略。
         */
        private String identityName;
        /**
         * 传给 openclaw 子命令 `--identity-theme` 选项的内容；为 null 时通常省略。
         */
        private String identityTheme;
        /**
         * 传给 openclaw 子命令 `--identity-emoji` 选项的内容；为 null 时通常省略。
         */
        private String identityEmoji;
        /**
         * 传给 openclaw 子命令 `--identity-avatar` 选项的内容；为 null 时通常省略。
         */
        private String identityAvatar;
        /**
         * 是否向 openclaw 子命令追加 `--identity-json` 开关。
         */
        private boolean identityJson;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `defaultList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 `--list-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
         * 设置 `--list-bindings` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param bindings 是否向命令行追加 `--list-bindings` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listBindings(boolean bindings) {
            this.listBindings = bindings;
            return this;
        }

        /**
         * 设置 `--add` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--add` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add(String name) {
            this.verb = Verb.ADD;
            this.addName = name;
            return this;
        }

        /**
         * 设置 `--workspace` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param workspace 写入 `--workspace` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder workspace(String workspace) {
            this.workspace = workspace;
            return this;
        }

        /**
         * 设置 `--model` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param model 模型标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置 `--agent-dir` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agentDir 写入 `--agent-dir` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agentDir(String agentDir) {
            this.agentDir = agentDir;
            return this;
        }

        /**
         * 设置 `--bind` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelBinding 写入 `--bind` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bind(String channelBinding) {
            if (channelBinding != null && OpenClawStrings.isNotBlank(channelBinding)) {
                this.bindValues.add(channelBinding.trim());
            }
            return this;
        }

        /**
         * 设置 `--non-interactive` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 `--non-interactive` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
            return this;
        }

        /**
         * 设置 `--add-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addJson(boolean json) {
            this.addJson = json;
            return this;
        }

        /**
         * 选择 `bindings` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindings() {
            this.verb = Verb.BINDINGS;
            return this;
        }

        /**
         * 设置 `--bindings-agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--bindings-agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindingsAgent(String agent) {
            this.bindingsAgent = agent;
            return this;
        }

        /**
         * 设置 `--bindings-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindingsJson(boolean json) {
            this.bindingsJson = json;
            return this;
        }

        /**
         * 选择 `bindCommand` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindCommand() {
            this.verb = Verb.BIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        /**
         * 设置 `--bind-agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--bind-agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindAgent(String agent) {
            this.bindAgent = agent;
            return this;
        }

        /**
         * 设置 `--bind-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindJson(boolean json) {
            this.bindJson = json;
            return this;
        }

        /**
         * 选择 `unbind` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbind() {
            this.verb = Verb.UNBIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        /**
         * 设置 `--unbind-agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--unbind-agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindAgent(String agent) {
            this.unbindAgent = agent;
            return this;
        }

        /**
         * 设置 `--unbind-all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--unbind-all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindAll(boolean all) {
            this.unbindAll = all;
            return this;
        }

        /**
         * 设置 `--unbind-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindJson(boolean json) {
            this.unbindJson = json;
            return this;
        }

        /**
         * 设置 `--delete` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agentId Agent 标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder delete(String agentId) {
            this.verb = Verb.DELETE;
            this.deleteAgentId = agentId;
            return this;
        }

        /**
         * 设置 `--delete-force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--delete-force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deleteForce(boolean force) {
            this.deleteForce = force;
            return this;
        }

        /**
         * 设置 `--delete-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deleteJson(boolean json) {
            this.deleteJson = json;
            return this;
        }

        /**
         * 选择 `setIdentity` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder setIdentity() {
            this.verb = Verb.SET_IDENTITY;
            return this;
        }

        /**
         * 设置 `--identity-agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--identity-agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityAgent(String agent) {
            this.identityAgent = agent;
            return this;
        }

        /**
         * 设置 `--identity-workspace` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param workspace 写入 `--identity-workspace` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityWorkspace(String workspace) {
            this.identityWorkspace = workspace;
            return this;
        }

        /**
         * 设置 `--identity-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityFile(String path) {
            this.identityFile = path;
            return this;
        }

        /**
         * 设置 `--from-identity` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fromIdentity 是否向命令行追加 `--from-identity` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder fromIdentity(boolean fromIdentity) {
            this.fromIdentity = fromIdentity;
            return this;
        }

        /**
         * 设置 `--identity-name` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--identity-name` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityName(String name) {
            this.identityName = name;
            return this;
        }

        /**
         * 设置 `--identity-theme` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param theme 写入 `--identity-theme` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityTheme(String theme) {
            this.identityTheme = theme;
            return this;
        }

        /**
         * 设置 `--identity-emoji` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param emoji 写入 `--identity-emoji` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityEmoji(String emoji) {
            this.identityEmoji = emoji;
            return this;
        }

        /**
         * 设置 `--identity-avatar` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param avatar 写入 `--identity-avatar` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityAvatar(String avatar) {
            this.identityAvatar = avatar;
            return this;
        }

        /**
         * 设置 `--identity-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityJson(boolean json) {
            this.identityJson = json;
            return this;
        }

        /**
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `AgentsOptions`。
         *
         * @return 按当前字段创建的 AgentsOptions
         */
        public AgentsOptions build() {
            return new AgentsOptions(this);
        }
    }
}
