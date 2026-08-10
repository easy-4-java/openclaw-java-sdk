package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `health-command` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class HealthCommandOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeoutMs;
    /**
     * 是否向 openclaw 子命令追加 `--verbose` 开关。
     */
    private final boolean verbose;
    /**
     * 是否向 openclaw 子命令追加 `--debug` 开关。
     */
    private final boolean debug;

    /**
 * @param b builder
     */
    private HealthCommandOptions(Builder b) {
        this.json = b.json;
        this.timeoutMs = b.timeoutMs;
        this.verbose = b.verbose;
        this.debug = b.debug;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `HealthCommandOptions` 字段。
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
        if (json) {
            out.add("--json");
        }
        if (timeoutMs != null && !timeoutMs.isEmpty()) {
            out.add("--timeout");
            out.add(timeoutMs);
        }
        if (verbose) {
            out.add("--verbose");
        }
        if (debug) {
            out.add("--debug");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 HealthCommandOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 HealthCommandOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeoutMs;
        /**
         * 是否向 openclaw 子命令追加 `--verbose` 开关。
         */
        private boolean verbose;
        /**
         * 是否向 openclaw 子命令追加 `--debug` 开关。
         */
        private boolean debug;

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
         * 设置 `--timeout-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(String timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * 设置 `--debug` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param debug 是否向命令行追加 `--debug` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `HealthCommandOptions`。
         *
         * @return 按当前字段创建的 HealthCommandOptions
         */
        public HealthCommandOptions build() {
            return new HealthCommandOptions(this);
        }
    }
}
