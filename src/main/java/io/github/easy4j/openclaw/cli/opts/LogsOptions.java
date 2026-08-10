package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw {@code logs} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class LogsOptions implements CliSubArgs {

    /**
     * Gateway RPC 连接参数；未设置时命令行不包含 {@code --rpc}。
     */
    private final GatewayRpcOptions rpc;
    /**
     * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
     */
    private final String limit;
    /**
     * 读取或传输的最大字节数；未设置时命令行不包含 {@code --max-bytes}。
     */
    private final String maxBytes;
    /**
     * 是否向 openclaw 子命令追加 {@code --follow} 开关。
     */
    private final boolean follow;
    /**
     * 轮询间隔，单位为毫秒；未设置时命令行不包含 {@code --interval-ms}。
     */
    private final String intervalMs;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 {@code --plain} 开关。
     */
    private final boolean plain;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-color} 开关。
     */
    private final boolean noColor;
    /**
     * 是否向 openclaw 子命令追加 {@code --local-time} 开关。
     */
    private final boolean localTime;

    /**
     * 从构建器复制日志过滤、跟随和 RPC 连接选项；未设置 RPC 时使用空配置。
     *
     * @param b 待冻结的日志选项构建器
     */
    private LogsOptions(Builder b) {
        this.rpc = b.rpc != null ? b.rpc : GatewayRpcOptions.builder().build();
        this.limit = b.limit;
        this.maxBytes = b.maxBytes;
        this.follow = b.follow;
        this.intervalMs = b.intervalMs;
        this.json = b.json;
        this.plain = b.plain;
        this.noColor = b.noColor;
        this.localTime = b.localTime;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code LogsOptions} 字段。
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
        rpc.appendSharedFlags(out);
        if (limit != null && !limit.isEmpty()) {
            out.add("--limit");
            out.add(limit);
        }
        if (maxBytes != null && !maxBytes.isEmpty()) {
            out.add("--max-bytes");
            out.add(maxBytes);
        }
        if (follow) {
            out.add("--follow");
        }
        if (intervalMs != null && !intervalMs.isEmpty()) {
            out.add("--interval");
            out.add(intervalMs);
        }
        if (json) {
            out.add("--json");
        }
        if (plain) {
            out.add("--plain");
        }
        if (noColor) {
            out.add("--no-color");
        }
        if (localTime) {
            out.add("--local-time");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code LogsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * Gateway RPC 连接参数；未设置时命令行不包含 {@code --rpc}。
         */
        private GatewayRpcOptions rpc;
        /**
         * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
         */
        private String limit;
        /**
         * 读取或传输的最大字节数；未设置时命令行不包含 {@code --max-bytes}。
         */
        private String maxBytes;
        /**
         * 是否向 openclaw 子命令追加 {@code --follow} 开关。
         */
        private boolean follow;
        /**
         * 轮询间隔，单位为毫秒；未设置时命令行不包含 {@code --interval-ms}。
         */
        private String intervalMs;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 {@code --plain} 开关。
         */
        private boolean plain;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-color} 开关。
         */
        private boolean noColor;
        /**
         * 是否向 openclaw 子命令追加 {@code --local-time} 开关。
         */
        private boolean localTime;

        /**
         * 设置 {@code --rpc} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc Gateway RPC 连接参数；作为 {@code --rpc} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rpc(GatewayRpcOptions rpc) {
            this.rpc = Objects.requireNonNull(rpc, "rpc");
            return this;
        }

        /**
         * 设置 {@code --limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 返回结果数量上限；作为 {@code --limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(String limit) {
            this.limit = limit;
            return this;
        }

        /**
         * 设置 {@code --max-bytes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param maxBytes 读取或传输的最大字节数；作为 {@code --max-bytes} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder maxBytes(String maxBytes) {
            this.maxBytes = maxBytes;
            return this;
        }

        /**
         * 设置 {@code --follow} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param follow 是否向命令行追加 {@code --follow} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder follow(boolean follow) {
            this.follow = follow;
            return this;
        }

        /**
         * 设置 {@code --interval-ms} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param intervalMs 轮询间隔，单位为毫秒；作为 {@code --interval-ms} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder intervalMs(String intervalMs) {
            this.intervalMs = intervalMs;
            return this;
        }

        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 {@code --plain} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plain 是否向命令行追加 {@code --plain} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plain(boolean plain) {
            this.plain = plain;
            return this;
        }

        /**
         * 设置 {@code --no-color} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noColor 是否向命令行追加 {@code --no-color} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noColor(boolean noColor) {
            this.noColor = noColor;
            return this;
        }

        /**
         * 设置 {@code --local-time} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param localTime 是否向命令行追加 {@code --local-time} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder localTime(boolean localTime) {
            this.localTime = localTime;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code LogsOptions}。
         *
         * @return 按当前字段创建的 LogsOptions
         */
        public LogsOptions build() {
            return new LogsOptions(this);
        }
    }
}
