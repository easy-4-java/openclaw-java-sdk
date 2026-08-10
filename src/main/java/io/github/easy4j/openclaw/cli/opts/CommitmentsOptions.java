package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `commitments` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CommitmentsOptions implements CliSubArgs {

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
         * 选择 `dismiss` 协议模式；序列化时使用该固定取值。
         */
        DISMISS
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 传给 openclaw 子命令 `--status` 选项的内容；为 null 时通常省略。
     */
    private final String status;
    /**
     * 是否向 openclaw 子命令追加 `--all` 开关。
     */
    private final boolean all;
    /**
     * 传给 openclaw 子命令 `--dismiss-ids` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `CommitmentsOptions` 字段。
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
     * 链式构建器，逐项收集 CommitmentsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 CommitmentsOptions。
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
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 传给 openclaw 子命令 `--status` 选项的内容；为 null 时通常省略。
         */
        private String status;
        /**
         * 是否向 openclaw 子命令追加 `--all` 开关。
         */
        private boolean all;
        /**
         * 传给 openclaw 子命令 `--dismiss-ids` 选项的内容；为 null 时通常省略。
         */
        private List<String> dismissIds;

        /**
         * 选择 `dismiss` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dismiss() { this.mode = Mode.DISMISS; return this; }
        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }
        /**
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) { this.agent = agent; return this; }
        /**
         * 设置 `--status` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param status 写入 `--status` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status(String status) { this.status = status; return this; }
        /**
         * 设置 `--all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder all(boolean all) { this.all = all; return this; }
        /**
         * 设置 `--dismiss-ids` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ids 写入 `--dismiss-ids` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dismissIds(List<String> ids) { this.dismissIds = ids; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `CommitmentsOptions`。
         *
         * @return 按当前字段创建的 CommitmentsOptions
         */
        public CommitmentsOptions build() {
            return new CommitmentsOptions(this);
        }
    }
}
