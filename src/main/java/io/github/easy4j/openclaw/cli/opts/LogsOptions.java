package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw `logs` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class LogsOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--rpc` 选项的内容；为 null 时通常省略。
     */
    private final GatewayRpcOptions rpc;
    /**
     * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
     */
    private final String limit;
    /**
     * 传给 openclaw 子命令 `--max-bytes` 选项的内容；为 null 时通常省略。
     */
    private final String maxBytes;
    /**
     * 是否向 openclaw 子命令追加 `--follow` 开关。
     */
    private final boolean follow;
    /**
     * 传给 openclaw 子命令 `--interval-ms` 选项的内容；为 null 时通常省略。
     */
    private final String intervalMs;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 是否向 openclaw 子命令追加 `--plain` 开关。
     */
    private final boolean plain;
    /**
     * 是否向 openclaw 子命令追加 `--no-color` 开关。
     */
    private final boolean noColor;
    /**
     * 是否向 openclaw 子命令追加 `--local-time` 开关。
     */
    private final boolean localTime;

    /**
 * @param b builder;{@code rpc} {@link GatewayRpcOptions}
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
     * 创建空白构建器，供调用方链式设置 `LogsOptions` 字段。
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
     * 链式构建器，逐项收集 LogsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 LogsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--rpc` 选项的内容；为 null 时通常省略。
         */
        private GatewayRpcOptions rpc;
        /**
         * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
         */
        private String limit;
        /**
         * 传给 openclaw 子命令 `--max-bytes` 选项的内容；为 null 时通常省略。
         */
        private String maxBytes;
        /**
         * 是否向 openclaw 子命令追加 `--follow` 开关。
         */
        private boolean follow;
        /**
         * 传给 openclaw 子命令 `--interval-ms` 选项的内容；为 null 时通常省略。
         */
        private String intervalMs;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 是否向 openclaw 子命令追加 `--plain` 开关。
         */
        private boolean plain;
        /**
         * 是否向 openclaw 子命令追加 `--no-color` 开关。
         */
        private boolean noColor;
        /**
         * 是否向 openclaw 子命令追加 `--local-time` 开关。
         */
        private boolean localTime;

        /**
         * 设置 `--rpc` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param rpc 写入 `--rpc` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rpc(GatewayRpcOptions rpc) {
            this.rpc = Objects.requireNonNull(rpc, "rpc");
            return this;
        }

        /**
         * 设置 `--limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 写入 `--limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(String limit) {
            this.limit = limit;
            return this;
        }

        /**
         * 设置 `--max-bytes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param maxBytes 写入 `--max-bytes` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder maxBytes(String maxBytes) {
            this.maxBytes = maxBytes;
            return this;
        }

        /**
         * 设置 `--follow` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param follow 是否向命令行追加 `--follow` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder follow(boolean follow) {
            this.follow = follow;
            return this;
        }

        /**
         * 设置 `--interval-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param intervalMs 写入 `--interval-ms` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder intervalMs(String intervalMs) {
            this.intervalMs = intervalMs;
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 `--plain` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plain 是否向命令行追加 `--plain` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder plain(boolean plain) {
            this.plain = plain;
            return this;
        }

        /**
         * 设置 `--no-color` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noColor 是否向命令行追加 `--no-color` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noColor(boolean noColor) {
            this.noColor = noColor;
            return this;
        }

        /**
         * 设置 `--local-time` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param localTime 是否向命令行追加 `--local-time` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder localTime(boolean localTime) {
            this.localTime = localTime;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `LogsOptions`。
         *
         * @return 按当前字段创建的 LogsOptions
         */
        public LogsOptions build() {
            return new LogsOptions(this);
        }
    }
}
