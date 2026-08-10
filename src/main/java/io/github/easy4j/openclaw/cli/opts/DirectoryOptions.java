package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code directory} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DirectoryOptions implements CliSubArgs {

    /**
     * 定义目录查询动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示目录查询动作的 {@code self} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SELF,
        /**
         * 表示目录查询动作的 {@code peers_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PEERS_LIST,
        /**
         * 表示目录查询动作的 {@code groups_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        GROUPS_LIST,
        /**
         * 表示目录查询动作的 {@code groups_members} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        GROUPS_MEMBERS
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
     */
    private final String account;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 目录搜索关键字；未设置时命令行不包含 {@code --query}。
     */
    private final String query;
    /**
     * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
     */
    private final Integer limit;
    /**
     * 待查询成员的群组标识；未设置时命令行不包含 {@code --group-id}。
     */
    private final String groupId;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code DirectoryOptions} 字段。
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
     * {@code DirectoryOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.PEERS_LIST;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 目标通道账户标识；未设置时命令行不包含 {@code --account}。
         */
        private String account;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 目录搜索关键字；未设置时命令行不包含 {@code --query}。
         */
        private String query;
        /**
         * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
         */
        private Integer limit;
        /**
         * 待查询成员的群组标识；未设置时命令行不包含 {@code --group-id}。
         */
        private String groupId;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code self} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder self() {
            this.mode = Mode.SELF;
            return this;
        }

        /**
         * 选择 {@code peersList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder peersList() {
            this.mode = Mode.PEERS_LIST;
            return this;
        }

        /**
         * 选择 {@code groupsList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder groupsList() {
            this.mode = Mode.GROUPS_LIST;
            return this;
        }

        /**
         * 设置 {@code --groups-members} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param groupId 待查询成员的群组标识；作为 {@code --groups-members} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder groupsMembers(String groupId) {
            this.mode = Mode.GROUPS_MEMBERS;
            this.groupId = groupId;
            return this;
        }

        /**
         * 设置 {@code --channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 目标消息通道；作为 {@code --channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 {@code --account} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 目标通道账户标识；作为 {@code --account} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 {@code --query} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param query 目录搜索关键字；作为 {@code --query} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder query(String query) {
            this.query = query;
            return this;
        }

        /**
         * 设置 {@code --limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 返回结果数量上限；作为 {@code --limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(int limit) {
            this.limit = limit;
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
         * 校验并复制当前构建器字段，创建独立的 {@code DirectoryOptions}。
         *
         * @return 按当前字段创建的 DirectoryOptions
         */
        public DirectoryOptions build() {
            return new DirectoryOptions(this);
        }
    }
}
