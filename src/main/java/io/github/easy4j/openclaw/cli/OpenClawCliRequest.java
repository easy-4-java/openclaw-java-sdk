package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.util.OpenClawLists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地 openclaw CLI 的 `OpenClawCliRequest` 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public final class OpenClawCliRequest {

    /**
     * `OpenClawCliRequest` 生命周期内保存的 `dev` 对应状态。
     */
    private final boolean dev;
    /**
     * `OpenClawCliRequest` 生命周期内保存的 `profile` 对应状态。
     */
    private final String profile;
    /**
     * `OpenClawCliRequest` 生命周期内保存的 `container` 对应状态。
     */
    private final String container;
    /**
     * `OpenClawCliRequest` 生命周期内保存的 `noColor` 对应状态。
     */
    private final boolean noColor;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final Integer timeoutSeconds;
    /**
     * `OpenClawCliRequest` 生命周期内保存的 `arguments` 对应状态。
     */
    private final List<String> arguments;

    // ============================================================
    // Getters (non-Lombok)
    // ============================================================

    /**
     * 判断 `dev` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isDev() { return dev; }
    /**
     * 读取当前对象保存的 `profile` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getProfile() { return profile; }
    /**
     * 读取当前对象保存的 `container` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getContainer() { return container; }
    /**
     * 判断 `noColor` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isNoColor() { return noColor; }
    /**
     * 读取当前对象保存的 超时时间，单位为秒，不触发网络或子进程调用。
     *
     * @return 当前计数、状态码、可空配置或毫秒级时间值
     */
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    /**
     * 读取当前对象保存的 `arguments` 对应状态，不触发网络或子进程调用。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    public List<String> getArguments() { return arguments; }

    private OpenClawCliRequest(Builder b) {
        this.dev = b.dev;
        this.profile = b.profile;
        this.container = b.container;
        this.noColor = b.noColor;
        this.timeoutSeconds = b.timeoutSeconds;
        this.arguments = OpenClawLists.copyOf(b.arguments);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `OpenClawCliRequest` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 链式构建器，逐项收集 OpenClawCliRequest 的字段；build() 会复制当前快照，后续修改不会影响已构造的 OpenClawCliRequest。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * `Builder` 生命周期内保存的 `dev` 对应状态。
         */
        private boolean dev;
        /**
         * `Builder` 生命周期内保存的 `profile` 对应状态。
         */
        private String profile;
        /**
         * `Builder` 生命周期内保存的 `container` 对应状态。
         */
        private String container;
        /**
         * `Builder` 生命周期内保存的 `noColor` 对应状态。
         */
        private boolean noColor;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private Integer timeoutSeconds;
        /**
         * `Builder` 生命周期内保存的 `arguments` 对应状态。
         */
        private final List<String> arguments = new ArrayList<>();

        /**
         * 设置 `--dev` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dev 是否向命令行追加 `--dev` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dev(boolean dev) {
            this.dev = dev;
            return this;
        }

        /**
         * 设置 `--profile` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param profile 写入 `--profile` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        /**
         * 设置 `--container` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param container 写入 `--container` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder container(String container) {
            this.container = container;
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
         * 设置 `--timeout-seconds` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
         * 设置 `--arguments` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param args 写入 `--arguments` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder arguments(String... args) {
            this.arguments.clear();
            if (args != null) {
                this.arguments.addAll(Arrays.asList(args));
            }
            return this;
        }

        /**
         * 设置 `--arguments` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param args 写入 `--arguments` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder arguments(List<String> args) {
            this.arguments.clear();
            if (args != null) {
                this.arguments.addAll(args);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `OpenClawCliRequest`。
         *
         * @return 按当前字段创建的 OpenClawCliRequest
         */
        public OpenClawCliRequest build() {
            return new OpenClawCliRequest(this);
        }
    }
}
