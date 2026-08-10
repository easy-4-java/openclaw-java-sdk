package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `worktrees` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class WorktreesOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `create` 协议模式；序列化时使用该固定取值。
         */
        CREATE,
        /**
         * 选择 `remove` 协议模式；序列化时使用该固定取值。
         */
        REMOVE,
        /**
         * 选择 `restore` 协议模式；序列化时使用该固定取值。
         */
        RESTORE,
        /**
         * 选择 `gc` 协议模式；序列化时使用该固定取值。
         */
        GC
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--repo-root` 选项的内容；为 null 时通常省略。
     */
    private final String repoRoot;
    /**
     * 传给 openclaw 子命令 `--id` 选项的内容；为 null 时通常省略。
     */
    private final String id;
    /**
     * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
     */
    private final String name;
    /**
     * 传给 openclaw 子命令 `--base-ref` 选项的内容；为 null 时通常省略。
     */
    private final String baseRef;
    /**
     * 是否向 openclaw 子命令追加 `--force` 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;

    private WorktreesOptions(Builder b) {
        this.mode = b.mode;
        this.repoRoot = b.repoRoot;
        this.id = b.id;
        this.name = b.name;
        this.baseRef = b.baseRef;
        this.force = b.force;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `WorktreesOptions` 字段。
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
            case LIST:
                out.add("list");
                break;
            case CREATE:
                out.add("create");
                if (repoRoot != null && !repoRoot.isEmpty()) {
                    out.add(repoRoot);
                }
                break;
            case REMOVE:
                out.add("remove");
                if (id != null && !id.isEmpty()) {
                    out.add(id);
                }
                break;
            case RESTORE:
                out.add("restore");
                if (id != null && !id.isEmpty()) {
                    out.add(id);
                }
                break;
            case GC:
                out.add("gc");
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--name", name);
        OpenClawCliArgv.addIfPresent(out, "--base-ref", baseRef);
        OpenClawCliArgv.addFlag(out, "--force", force);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 WorktreesOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 WorktreesOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.LIST;
        /**
         * 传给 openclaw 子命令 `--repo-root` 选项的内容；为 null 时通常省略。
         */
        private String repoRoot;
        /**
         * 传给 openclaw 子命令 `--id` 选项的内容；为 null 时通常省略。
         */
        private String id;
        /**
         * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
         */
        private String name;
        /**
         * 传给 openclaw 子命令 `--base-ref` 选项的内容；为 null 时通常省略。
         */
        private String baseRef;
        /**
         * 是否向 openclaw 子命令追加 `--force` 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() { this.mode = Mode.LIST; return this; }
        /**
         * 设置 `--create` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param repoRoot 写入 `--create` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder create(String repoRoot) { this.mode = Mode.CREATE; this.repoRoot = repoRoot; return this; }
        /**
         * 设置 `--remove` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--remove` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove(String id) { this.mode = Mode.REMOVE; this.id = id; return this; }
        /**
         * 设置 `--restore` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--restore` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder restore(String id) { this.mode = Mode.RESTORE; this.id = id; return this; }
        /**
         * 选择 `gc` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gc() { this.mode = Mode.GC; return this; }
        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 `--repo-root` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param repoRoot 写入 `--repo-root` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder repoRoot(String repoRoot) { this.repoRoot = repoRoot; return this; }
        /**
         * 设置 `--id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder id(String id) { this.id = id; return this; }
        /**
         * 设置 `--name` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--name` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder name(String name) { this.name = name; return this; }
        /**
         * 设置 `--base-ref` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param baseRef 写入 `--base-ref` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder baseRef(String baseRef) { this.baseRef = baseRef; return this; }
        /**
         * 设置 `--force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) { this.force = force; return this; }
        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `WorktreesOptions`。
         *
         * @return 按当前字段创建的 WorktreesOptions
         */
        public WorktreesOptions build() {
            return new WorktreesOptions(this);
        }
    }
}
