package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw `gateway-command` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class GatewayCommandOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--segments` 选项的内容；为 null 时通常省略。
     */
    private final List<String> segments;

    /**
 * @param segments null,
     */
    private GatewayCommandOptions(List<String> segments) {
        this.segments = OpenClawLists.copyOf(segments);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `GatewayCommandOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 `gateway-command` 子命令的 `empty` 行为，并保留未设置选项的省略语义。
     *
     * @return 按当前参数创建、查询或解析得到的 GatewayCommandOptions
     */
    public static GatewayCommandOptions empty() {
        return new GatewayCommandOptions(OpenClawLists.empty());
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        return segments;
    }

    /**
     * 链式构建器，逐项收集 GatewayCommandOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 GatewayCommandOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--s` 选项的内容；为 null 时通常省略。
         */
        private final List<String> s = new ArrayList<>();

        /**
         * 设置 `--add` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--add` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add(String... tokens) {
            if (tokens != null) {
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
         * 设置 `--health` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc 写入 `--health` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder health(GatewayRpcOptions rpc) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.health(rpc));
            return this;
        }

        /**
         * 设置 `--status` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc 写入 `--status` 选项的内容
         * @param extra 写入 `--status` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status(GatewayRpcOptions rpc, GatewayCliArgv.GatewayStatusOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.status(rpc, extra));
            return this;
        }

        /**
         * 设置 `--probe` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc 写入 `--probe` 选项的内容
         * @param extra 写入 `--probe` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probe(GatewayRpcOptions rpc, GatewayCliArgv.GatewayProbeOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.probe(rpc, extra));
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `GatewayCommandOptions`。
         *
         * @return 按当前字段创建的 GatewayCommandOptions
         */
        public GatewayCommandOptions build() {
            return new GatewayCommandOptions(OpenClawLists.copyOf(s));
        }
    }
}
