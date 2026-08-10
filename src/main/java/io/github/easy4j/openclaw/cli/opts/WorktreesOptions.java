package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code worktrees} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class WorktreesOptions implements CliSubArgs {

    /**
     * 定义工作区管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示工作区管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示工作区管理动作的 {@code create} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CREATE,
        /**
         * 表示工作区管理动作的 {@code remove} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REMOVE,
        /**
         * 表示工作区管理动作的 {@code restore} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RESTORE,
        /**
         * 表示工作区管理动作的 {@code gc} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        GC
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * Git 仓库根目录；未设置时命令行不包含 {@code --repo-root}。
     */
    private final String repoRoot;
    /**
     * 目标资源标识；未设置时命令行不包含 {@code --id}。
     */
    private final String id;
    /**
     * 目标资源名称；未设置时命令行不包含 {@code --name}。
     */
    private final String name;
    /**
     * 创建工作区时使用的 Git 基准引用；未设置时命令行不包含 {@code --base-ref}。
     */
    private final String baseRef;
    /**
     * 是否向 openclaw 子命令追加 {@code --force} 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
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
     * 创建空白构建器，供调用方链式设置 {@code WorktreesOptions} 字段。
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
     * {@code WorktreesOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.LIST;
        /**
         * Git 仓库根目录；未设置时命令行不包含 {@code --repo-root}。
         */
        private String repoRoot;
        /**
         * 目标资源标识；未设置时命令行不包含 {@code --id}。
         */
        private String id;
        /**
         * 目标资源名称；未设置时命令行不包含 {@code --name}。
         */
        private String name;
        /**
         * 创建工作区时使用的 Git 基准引用；未设置时命令行不包含 {@code --base-ref}。
         */
        private String baseRef;
        /**
         * 是否向 openclaw 子命令追加 {@code --force} 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() { this.mode = Mode.LIST; return this; }
        /**
         * 设置 {@code --create} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param repoRoot Git 仓库根目录；作为 {@code --create} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder create(String repoRoot) { this.mode = Mode.CREATE; this.repoRoot = repoRoot; return this; }
        /**
         * 设置 {@code --remove} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --remove} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove(String id) { this.mode = Mode.REMOVE; this.id = id; return this; }
        /**
         * 设置 {@code --restore} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --restore} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder restore(String id) { this.mode = Mode.RESTORE; this.id = id; return this; }
        /**
         * 选择 {@code gc} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gc() { this.mode = Mode.GC; return this; }
        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 {@code --repo-root} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param repoRoot Git 仓库根目录；作为 {@code --repo-root} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder repoRoot(String repoRoot) { this.repoRoot = repoRoot; return this; }
        /**
         * 设置 {@code --id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder id(String id) { this.id = id; return this; }
        /**
         * 设置 {@code --name} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --name} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder name(String name) { this.name = name; return this; }
        /**
         * 设置 {@code --base-ref} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param baseRef 创建工作区时使用的 Git 基准引用；作为 {@code --base-ref} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder baseRef(String baseRef) { this.baseRef = baseRef; return this; }
        /**
         * 设置 {@code --force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 {@code --force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) { this.force = force; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code WorktreesOptions}。
         *
         * @return 按当前字段创建的 WorktreesOptions
         */
        public WorktreesOptions build() {
            return new WorktreesOptions(this);
        }
    }
}
