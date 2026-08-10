package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code flows} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class FlowsOptions implements CliSubArgs {

    /**
     * 定义流程管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示流程管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示流程管理动作的 {@code show} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SHOW,
        /**
         * 表示流程管理动作的 {@code cancel} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CANCEL
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
     */
    private final boolean listJson;
    /**
     * 用于查找流程的名称或标识；未设置时命令行不包含 {@code --lookup}。
     */
    private final String lookup;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code FlowsOptions} 字段。
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
     * {@code FlowsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
         */
        private boolean listJson;
        /**
         * 用于查找流程的名称或标识；未设置时命令行不包含 {@code --lookup}。
         */
        private String lookup;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.mode = Mode.LIST;
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
         * 设置 {@code --show} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lookup 用于查找流程的名称或标识；作为 {@code --show} 的参数
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
         * @param lookup 用于查找流程的名称或标识；作为 {@code --cancel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cancel(String lookup) {
            this.mode = Mode.CANCEL;
            this.lookup = lookup;
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
         * 校验并复制当前构建器字段，创建独立的 {@code FlowsOptions}。
         *
         * @return 按当前字段创建的 FlowsOptions
         */
        public FlowsOptions build() {
            return new FlowsOptions(this);
        }
    }
}
