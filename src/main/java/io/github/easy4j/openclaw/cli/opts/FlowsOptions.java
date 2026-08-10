package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `flows` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class FlowsOptions implements CliSubArgs {

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
         * 选择 `show` 协议模式；序列化时使用该固定取值。
         */
        SHOW,
        /**
         * 选择 `cancel` 协议模式；序列化时使用该固定取值。
         */
        CANCEL
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--list-json` 开关。
     */
    private final boolean listJson;
    /**
     * 传给 openclaw 子命令 `--lookup` 选项的内容；为 null 时通常省略。
     */
    private final String lookup;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private FlowsOptions(Builder b) {
        this.mode = b.mode;
        this.listJson = b.listJson;
        this.lookup = b.lookup;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `FlowsOptions` 字段。
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
        out.add("flow");
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                break;
            case SHOW:
                out.add("show");
                if (lookup != null && OpenClawStrings.isNotBlank(lookup)) {
                    out.add(lookup.trim());
                }
                break;
            case CANCEL:
                out.add("cancel");
                if (lookup != null && OpenClawStrings.isNotBlank(lookup)) {
                    out.add(lookup.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 FlowsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 FlowsOptions。
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
         * 是否向 openclaw 子命令追加 `--list-json` 开关。
         */
        private boolean listJson;
        /**
         * 传给 openclaw 子命令 `--lookup` 选项的内容；为 null 时通常省略。
         */
        private String lookup;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.mode = Mode.LIST;
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
         * 设置 `--show` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lookup 写入 `--show` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder show(String lookup) {
            this.mode = Mode.SHOW;
            this.lookup = lookup;
            return this;
        }

        /**
         * 把取消信号传播到底层网络调用或 Future，并以幂等方式结束当前任务。
         *
         * @param lookup 写入 `--cancel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cancel(String lookup) {
            this.mode = Mode.CANCEL;
            this.lookup = lookup;
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
         * 校验并复制当前构建器字段，创建独立的 `FlowsOptions`。
         *
         * @return 按当前字段创建的 FlowsOptions
         */
        public FlowsOptions build() {
            return new FlowsOptions(this);
        }
    }
}
