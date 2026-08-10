package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * openclaw {@code agents} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AgentsOptions implements CliSubArgs {

    /**
     * 定义智能体管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示智能体管理动作的 {@code default_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DEFAULT_LIST,
        /**
         * 表示智能体管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示智能体管理动作的 {@code add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ADD,
        /**
         * 表示智能体管理动作的 {@code bindings} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        BINDINGS,
        /**
         * 表示智能体管理动作的 {@code bind} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        BIND,
        /**
         * 表示智能体管理动作的 {@code unbind} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UNBIND,
        /**
         * 表示智能体管理动作的 {@code set_identity} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET_IDENTITY,
        /**
         * 表示智能体管理动作的 {@code delete} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DELETE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
     */
    private final boolean listJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-bindings} 开关。
     */
    private final boolean listBindings;
    /**
     * 待新增资源的名称；未设置时命令行不包含 {@code --add-name}。
     */
    private final String addName;
    /**
     * 智能体工作目录；未设置时命令行不包含 {@code --workspace}。
     */
    private final String workspace;
    /**
     * 目标模型标识；未设置时命令行不包含 {@code --model}。
     */
    private final String model;
    /**
     * 智能体数据目录路径；未设置时命令行不包含 {@code --agent-dir}。
     */
    private final String agentDir;
    /**
     * 通道绑定定义列表；未设置时命令行不包含 {@code --bind-values}。
     */
    private final List<String> bindValues;
    /**
     * 是否向 openclaw 子命令追加 {@code --non-interactive} 开关。
     */
    private final boolean nonInteractive;
    /**
     * 是否向 openclaw 子命令追加 {@code --add-json} 开关。
     */
    private final boolean addJson;
    /**
     * 筛选通道绑定的智能体标识；未设置时命令行不包含 {@code --bindings-agent}。
     */
    private final String bindingsAgent;
    /**
     * 是否向 openclaw 子命令追加 {@code --bindings-json} 开关。
     */
    private final boolean bindingsJson;
    /**
     * 待绑定通道的智能体标识；未设置时命令行不包含 {@code --bind-agent}。
     */
    private final String bindAgent;
    /**
     * 是否向 openclaw 子命令追加 {@code --bind-json} 开关。
     */
    private final boolean bindJson;
    /**
     * 待解除通道绑定的智能体标识；未设置时命令行不包含 {@code --unbind-agent}。
     */
    private final String unbindAgent;
    /**
     * 是否向 openclaw 子命令追加 {@code --unbind-all} 开关。
     */
    private final boolean unbindAll;
    /**
     * 是否向 openclaw 子命令追加 {@code --unbind-json} 开关。
     */
    private final boolean unbindJson;
    /**
     * 待删除的智能体标识；未设置时命令行不包含 {@code --delete-agent-id}。
     */
    private final String deleteAgentId;
    /**
     * 是否向 openclaw 子命令追加 {@code --delete-force} 开关。
     */
    private final boolean deleteForce;
    /**
     * 是否向 openclaw 子命令追加 {@code --delete-json} 开关。
     */
    private final boolean deleteJson;
    /**
     * 待更新身份的智能体标识；未设置时命令行不包含 {@code --identity-agent}。
     */
    private final String identityAgent;
    /**
     * 智能体身份使用的工作目录；未设置时命令行不包含 {@code --identity-workspace}。
     */
    private final String identityWorkspace;
    /**
     * 智能体身份定义文件；未设置时命令行不包含 {@code --identity-file}。
     */
    private final String identityFile;
    /**
     * 是否向 openclaw 子命令追加 {@code --from-identity} 开关。
     */
    private final boolean fromIdentity;
    /**
     * 智能体身份显示名称；未设置时命令行不包含 {@code --identity-name}。
     */
    private final String identityName;
    /**
     * 智能体身份主题；未设置时命令行不包含 {@code --identity-theme}。
     */
    private final String identityTheme;
    /**
     * 智能体身份表情符号；未设置时命令行不包含 {@code --identity-emoji}。
     */
    private final String identityEmoji;
    /**
     * 智能体身份头像；未设置时命令行不包含 {@code --identity-avatar}。
     */
    private final String identityAvatar;
    /**
     * 是否向 openclaw 子命令追加 {@code --identity-json} 开关。
     */
    private final boolean identityJson;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code AgentsOptions} 字段。
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
     * {@code AgentsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.DEFAULT_LIST;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
         */
        private boolean listJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-bindings} 开关。
         */
        private boolean listBindings;
        /**
         * 待新增资源的名称；未设置时命令行不包含 {@code --add-name}。
         */
        private String addName;
        /**
         * 智能体工作目录；未设置时命令行不包含 {@code --workspace}。
         */
        private String workspace;
        /**
         * 目标模型标识；未设置时命令行不包含 {@code --model}。
         */
        private String model;
        /**
         * 智能体数据目录路径；未设置时命令行不包含 {@code --agent-dir}。
         */
        private String agentDir;
        /**
         * 通道绑定定义列表；未设置时命令行不包含 {@code --bind-values}。
         */
        private List<String> bindValues = new ArrayList<>();
        /**
         * 是否向 openclaw 子命令追加 {@code --non-interactive} 开关。
         */
        private boolean nonInteractive;
        /**
         * 是否向 openclaw 子命令追加 {@code --add-json} 开关。
         */
        private boolean addJson;
        /**
         * 筛选通道绑定的智能体标识；未设置时命令行不包含 {@code --bindings-agent}。
         */
        private String bindingsAgent;
        /**
         * 是否向 openclaw 子命令追加 {@code --bindings-json} 开关。
         */
        private boolean bindingsJson;
        /**
         * 待绑定通道的智能体标识；未设置时命令行不包含 {@code --bind-agent}。
         */
        private String bindAgent;
        /**
         * 是否向 openclaw 子命令追加 {@code --bind-json} 开关。
         */
        private boolean bindJson;
        /**
         * 待解除通道绑定的智能体标识；未设置时命令行不包含 {@code --unbind-agent}。
         */
        private String unbindAgent;
        /**
         * 是否向 openclaw 子命令追加 {@code --unbind-all} 开关。
         */
        private boolean unbindAll;
        /**
         * 是否向 openclaw 子命令追加 {@code --unbind-json} 开关。
         */
        private boolean unbindJson;
        /**
         * 待删除的智能体标识；未设置时命令行不包含 {@code --delete-agent-id}。
         */
        private String deleteAgentId;
        /**
         * 是否向 openclaw 子命令追加 {@code --delete-force} 开关。
         */
        private boolean deleteForce;
        /**
         * 是否向 openclaw 子命令追加 {@code --delete-json} 开关。
         */
        private boolean deleteJson;
        /**
         * 待更新身份的智能体标识；未设置时命令行不包含 {@code --identity-agent}。
         */
        private String identityAgent;
        /**
         * 智能体身份使用的工作目录；未设置时命令行不包含 {@code --identity-workspace}。
         */
        private String identityWorkspace;
        /**
         * 智能体身份定义文件；未设置时命令行不包含 {@code --identity-file}。
         */
        private String identityFile;
        /**
         * 是否向 openclaw 子命令追加 {@code --from-identity} 开关。
         */
        private boolean fromIdentity;
        /**
         * 智能体身份显示名称；未设置时命令行不包含 {@code --identity-name}。
         */
        private String identityName;
        /**
         * 智能体身份主题；未设置时命令行不包含 {@code --identity-theme}。
         */
        private String identityTheme;
        /**
         * 智能体身份表情符号；未设置时命令行不包含 {@code --identity-emoji}。
         */
        private String identityEmoji;
        /**
         * 智能体身份头像；未设置时命令行不包含 {@code --identity-avatar}。
         */
        private String identityAvatar;
        /**
         * 是否向 openclaw 子命令追加 {@code --identity-json} 开关。
         */
        private boolean identityJson;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code defaultList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 {@code --list-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
         * 设置 {@code --list-bindings} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param bindings 是否向命令行追加 {@code --list-bindings} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listBindings(boolean bindings) {
            this.listBindings = bindings;
            return this;
        }

        /**
         * 设置 {@code --add} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --add} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add(String name) {
            this.verb = Verb.ADD;
            this.addName = name;
            return this;
        }

        /**
         * 设置 {@code --workspace} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param workspace 智能体工作目录；作为 {@code --workspace} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder workspace(String workspace) {
            this.workspace = workspace;
            return this;
        }

        /**
         * 设置 {@code --model} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param model 模型标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置 {@code --agent-dir} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agentDir 智能体数据目录路径；作为 {@code --agent-dir} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agentDir(String agentDir) {
            this.agentDir = agentDir;
            return this;
        }

        /**
         * 设置 {@code --bind} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channelBinding 智能体与通道的绑定定义；作为 {@code --bind} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bind(String channelBinding) {
            if (channelBinding != null && OpenClawStrings.isNotBlank(channelBinding)) {
                this.bindValues.add(channelBinding.trim());
            }
            return this;
        }

        /**
         * 设置 {@code --non-interactive} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 {@code --non-interactive} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
            return this;
        }

        /**
         * 设置 {@code --add-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addJson(boolean json) {
            this.addJson = json;
            return this;
        }

        /**
         * 选择 {@code bindings} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindings() {
            this.verb = Verb.BINDINGS;
            return this;
        }

        /**
         * 设置 {@code --bindings-agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --bindings-agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindingsAgent(String agent) {
            this.bindingsAgent = agent;
            return this;
        }

        /**
         * 设置 {@code --bindings-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindingsJson(boolean json) {
            this.bindingsJson = json;
            return this;
        }

        /**
         * 选择 {@code bindCommand} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindCommand() {
            this.verb = Verb.BIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        /**
         * 设置 {@code --bind-agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --bind-agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindAgent(String agent) {
            this.bindAgent = agent;
            return this;
        }

        /**
         * 设置 {@code --bind-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bindJson(boolean json) {
            this.bindJson = json;
            return this;
        }

        /**
         * 选择 {@code unbind} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbind() {
            this.verb = Verb.UNBIND;
            this.bindValues = new ArrayList<>();
            return this;
        }

        /**
         * 设置 {@code --unbind-agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --unbind-agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindAgent(String agent) {
            this.unbindAgent = agent;
            return this;
        }

        /**
         * 设置 {@code --unbind-all} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 {@code --unbind-all} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindAll(boolean all) {
            this.unbindAll = all;
            return this;
        }

        /**
         * 设置 {@code --unbind-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unbindJson(boolean json) {
            this.unbindJson = json;
            return this;
        }

        /**
         * 设置 {@code --delete} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
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
         * 设置 {@code --delete-force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 {@code --delete-force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deleteForce(boolean force) {
            this.deleteForce = force;
            return this;
        }

        /**
         * 设置 {@code --delete-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deleteJson(boolean json) {
            this.deleteJson = json;
            return this;
        }

        /**
         * 选择 {@code setIdentity} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder setIdentity() {
            this.verb = Verb.SET_IDENTITY;
            return this;
        }

        /**
         * 设置 {@code --identity-agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --identity-agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityAgent(String agent) {
            this.identityAgent = agent;
            return this;
        }

        /**
         * 设置 {@code --identity-workspace} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param workspace 智能体工作目录；作为 {@code --identity-workspace} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityWorkspace(String workspace) {
            this.identityWorkspace = workspace;
            return this;
        }

        /**
         * 设置 {@code --identity-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityFile(String path) {
            this.identityFile = path;
            return this;
        }

        /**
         * 设置 {@code --from-identity} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fromIdentity 是否向命令行追加 {@code --from-identity} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder fromIdentity(boolean fromIdentity) {
            this.fromIdentity = fromIdentity;
            return this;
        }

        /**
         * 设置 {@code --identity-name} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --identity-name} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityName(String name) {
            this.identityName = name;
            return this;
        }

        /**
         * 设置 {@code --identity-theme} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param theme 智能体身份主题；作为 {@code --identity-theme} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityTheme(String theme) {
            this.identityTheme = theme;
            return this;
        }

        /**
         * 设置 {@code --identity-emoji} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param emoji 智能体身份使用的表情符号；作为 {@code --identity-emoji} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityEmoji(String emoji) {
            this.identityEmoji = emoji;
            return this;
        }

        /**
         * 设置 {@code --identity-avatar} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param avatar 智能体身份头像；作为 {@code --identity-avatar} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityAvatar(String avatar) {
            this.identityAvatar = avatar;
            return this;
        }

        /**
         * 设置 {@code --identity-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder identityJson(boolean json) {
            this.identityJson = json;
            return this;
        }

        /**
         * 设置 {@code --extra} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 原样追加到生成参数末尾的 CLI 参数列表；作为 {@code --extra} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code AgentsOptions}。
         *
         * @return 按当前字段创建的 AgentsOptions
         */
        public AgentsOptions build() {
            return new AgentsOptions(this);
        }
    }
}
