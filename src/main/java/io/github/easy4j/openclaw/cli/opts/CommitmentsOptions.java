package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code commitments} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CommitmentsOptions implements CliSubArgs {

    /**
     * 定义承诺记录动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示承诺记录动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示承诺记录动作的 {@code dismiss} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DISMISS
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 筛选资源的状态；未设置时命令行不包含 {@code --status}。
     */
    private final String status;
    /**
     * 是否向 openclaw 子命令追加 {@code --all} 开关。
     */
    private final boolean all;
    /**
     * 待忽略的安全检查结果标识集合；未设置时命令行不包含 {@code --dismiss-ids}。
     */
    private final List<String> dismissIds;

    private CommitmentsOptions(Builder b) {
        this.mode = b.mode;
        this.json = b.json;
        this.agent = b.agent;
        this.status = b.status;
        this.all = b.all;
        this.dismissIds = OpenClawLists.copyOf(b.dismissIds);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code CommitmentsOptions} 字段。
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
        if (mode == Mode.DISMISS) {
            out.add("dismiss");
            if (dismissIds != null) {
                out.addAll(dismissIds);
            }
        }
        if (json) {
            out.add("--json");
        }
        if (agent != null && !agent.isEmpty()) {
            out.add("--agent");
            out.add(agent);
        }
        if (status != null && !status.isEmpty()) {
            out.add("--status");
            out.add(status);
        }
        if (all) {
            out.add("--all");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code CommitmentsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 筛选资源的状态；未设置时命令行不包含 {@code --status}。
         */
        private String status;
        /**
         * 是否向 openclaw 子命令追加 {@code --all} 开关。
         */
        private boolean all;
        /**
         * 待忽略的安全检查结果标识集合；未设置时命令行不包含 {@code --dismiss-ids}。
         */
        private List<String> dismissIds;

        /**
         * 选择 {@code dismiss} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dismiss() { this.mode = Mode.DISMISS; return this; }
        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }
        /**
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) { this.agent = agent; return this; }
        /**
         * 设置 {@code --status} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param status 筛选资源的状态；作为 {@code --status} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status(String status) { this.status = status; return this; }
        /**
         * 设置 {@code --all} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 {@code --all} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder all(boolean all) { this.all = all; return this; }
        /**
         * 设置 {@code --dismiss-ids} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ids 待忽略安全检查结果的标识集合；作为 {@code --dismiss-ids} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dismissIds(List<String> ids) { this.dismissIds = ids; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code CommitmentsOptions}。
         *
         * @return 按当前字段创建的 CommitmentsOptions
         */
        public CommitmentsOptions build() {
            return new CommitmentsOptions(this);
        }
    }
}
