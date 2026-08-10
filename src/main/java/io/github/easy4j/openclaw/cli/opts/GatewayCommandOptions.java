package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw {@code gateway-command} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class GatewayCommandOptions implements CliSubArgs {

    /**
     * 引导流程分段配置；未设置时命令行不包含 {@code --segments}。
     */
    private final List<String> segments;

    /**
 * @param segments null,
     */
    private GatewayCommandOptions(List<String> segments) {
        this.segments = OpenClawLists.copyOf(segments);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code GatewayCommandOptions} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 {@code gateway-command} 子命令的 {@code empty} 行为，并保留未设置选项的省略语义。
     *
     * @return 不包含附加选项的参数对象
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
     * {@code GatewayCommandOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 引导流程使用的分段配置；未设置时命令行不包含 {@code --s}。
         */
        private final List<String> s = new ArrayList<>();

        /**
         * 设置 {@code --add} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 原样追加到生成参数末尾的 CLI 参数列表；作为 {@code --add} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add(String... tokens) {
            if (tokens != null) {
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
         * 设置 {@code --health} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc Gateway RPC 连接参数；作为 {@code --health} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder health(GatewayRpcOptions rpc) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.health(rpc));
            return this;
        }

        /**
         * 设置 {@code --status} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc Gateway RPC 连接参数；作为 {@code --status} 的参数
         * @param extra 附加到 RPC 请求的原始参数；作为 {@code --status} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status(GatewayRpcOptions rpc, GatewayCliArgv.GatewayStatusOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.status(rpc, extra));
            return this;
        }

        /**
         * 设置 {@code --probe} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc Gateway RPC 连接参数；作为 {@code --probe} 的参数
         * @param extra 附加到 RPC 请求的原始参数；作为 {@code --probe} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probe(GatewayRpcOptions rpc, GatewayCliArgv.GatewayProbeOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.probe(rpc, extra));
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code GatewayCommandOptions}。
         *
         * @return 按当前字段创建的 GatewayCommandOptions
         */
        public GatewayCommandOptions build() {
            return new GatewayCommandOptions(OpenClawLists.copyOf(s));
        }
    }
}
