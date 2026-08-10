package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `exec-approvals` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ExecApprovalsOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `get` 协议模式；序列化时使用该固定取值。
         */
        GET,
        /**
         * 选择 `set` 协议模式；序列化时使用该固定取值。
         */
        SET,
        /**
         * 选择 `allowlist_add` 协议模式；序列化时使用该固定取值。
         */
        ALLOWLIST_ADD,
        /**
         * 选择 `allowlist_remove` 协议模式；序列化时使用该固定取值。
         */
        ALLOWLIST_REMOVE,
        /**
         * 选择 `default` 协议模式；序列化时使用该固定取值。
         */
        DEFAULT
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--pattern` 选项的内容；为 null 时通常省略。
     */
    private final String pattern;
    /**
     * 传给 openclaw 子命令 `--node` 选项的内容；为 null 时通常省略。
     */
    private final String node;
    /**
     * 是否向 openclaw 子命令追加 `--gateway` 开关。
     */
    private final boolean gateway;
    /**
     * 传给 openclaw 子命令 `--file` 选项的内容；为 null 时通常省略。
     */
    private final String file;
    /**
     * 是否向 openclaw 子命令追加 `--stdin` 开关。
     */
    private final boolean stdin;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
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
     * 创建空白构建器，供调用方链式设置 `ExecApprovalsOptions` 字段。
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
     * 链式构建器，逐项收集 ExecApprovalsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ExecApprovalsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.DEFAULT;
        /**
         * 传给 openclaw 子命令 `--pattern` 选项的内容；为 null 时通常省略。
         */
        private String pattern;
        /**
         * 传给 openclaw 子命令 `--node` 选项的内容；为 null 时通常省略。
         */
        private String node;
        /**
         * 是否向 openclaw 子命令追加 `--gateway` 开关。
         */
        private boolean gateway;
        /**
         * 传给 openclaw 子命令 `--file` 选项的内容；为 null 时通常省略。
         */
        private String file;
        /**
         * 是否向 openclaw 子命令追加 `--stdin` 开关。
         */
        private boolean stdin;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;

        /**
         * 选择 `get` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder get() { this.mode = Mode.GET; return this; }
        /**
         * 选择 `set` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() { this.mode = Mode.SET; return this; }
        /**
         * 设置 `--allowlist-add` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 写入 `--allowlist-add` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistAdd(String pattern) { this.mode = Mode.ALLOWLIST_ADD; this.pattern = pattern; return this; }
        /**
         * 设置 `--allowlist-remove` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pattern 写入 `--allowlist-remove` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowlistRemove(String pattern) { this.mode = Mode.ALLOWLIST_REMOVE; this.pattern = pattern; return this; }
        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 `--node` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param node 写入 `--node` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder node(String node) { this.node = node; return this; }
        /**
         * 设置 `--gateway` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gateway 是否向命令行追加 `--gateway` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gateway(boolean gateway) { this.gateway = gateway; return this; }
        /**
         * 设置 `--file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param file 写入 `--file` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder file(String file) { this.file = file; return this; }
        /**
         * 设置 `--stdin` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param stdin 是否向命令行追加 `--stdin` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stdin(boolean stdin) { this.stdin = stdin; return this; }
        /**
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) { this.agent = agent; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `ExecApprovalsOptions`。
         *
         * @return 按当前字段创建的 ExecApprovalsOptions
         */
        public ExecApprovalsOptions build() {
            return new ExecApprovalsOptions(this);
        }
    }
}
