package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `directory` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DirectoryOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `self` 协议模式；序列化时使用该固定取值。
         */
        SELF,
        /**
         * 选择 `peers_list` 协议模式；序列化时使用该固定取值。
         */
        PEERS_LIST,
        /**
         * 选择 `groups_list` 协议模式；序列化时使用该固定取值。
         */
        GROUPS_LIST,
        /**
         * 选择 `groups_members` 协议模式；序列化时使用该固定取值。
         */
        GROUPS_MEMBERS
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
     */
    private final String account;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--query` 选项的内容；为 null 时通常省略。
     */
    private final String query;
    /**
     * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
     */
    private final Integer limit;
    /**
     * 传给 openclaw 子命令 `--group-id` 选项的内容；为 null 时通常省略。
     */
    private final String groupId;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `DirectoryOptions` 字段。
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
     * 链式构建器，逐项收集 DirectoryOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 DirectoryOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.PEERS_LIST;
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--account` 选项的内容；为 null 时通常省略。
         */
        private String account;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--query` 选项的内容；为 null 时通常省略。
         */
        private String query;
        /**
         * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
         */
        private Integer limit;
        /**
         * 传给 openclaw 子命令 `--group-id` 选项的内容；为 null 时通常省略。
         */
        private String groupId;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `self` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder self() {
            this.mode = Mode.SELF;
            return this;
        }

        /**
         * 选择 `peersList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder peersList() {
            this.mode = Mode.PEERS_LIST;
            return this;
        }

        /**
         * 选择 `groupsList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder groupsList() {
            this.mode = Mode.GROUPS_LIST;
            return this;
        }

        /**
         * 设置 `--groups-members` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param groupId 写入 `--groups-members` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder groupsMembers(String groupId) {
            this.mode = Mode.GROUPS_MEMBERS;
            this.groupId = groupId;
            return this;
        }

        /**
         * 设置 `--channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 写入 `--channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 `--account` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param account 写入 `--account` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 `--query` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param query 写入 `--query` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder query(String query) {
            this.query = query;
            return this;
        }

        /**
         * 设置 `--limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 写入 `--limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(int limit) {
            this.limit = limit;
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
         * 校验并复制当前构建器字段，创建独立的 `DirectoryOptions`。
         *
         * @return 按当前字段创建的 DirectoryOptions
         */
        public DirectoryOptions build() {
            return new DirectoryOptions(this);
        }
    }
}
