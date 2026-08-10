package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.util.OpenClawLists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地 openclaw CLI 的 {@code OpenClawCliRequest} 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public final class OpenClawCliRequest {

    /**
     * 是否向 CLI 追加开发模式开关。
     */
    private final boolean dev;
    /**
     * 传递给 CLI 的配置档案名称；为空时使用 CLI 默认档案。
     */
    private final String profile;
    /**
     * 传递给 CLI 的容器标识；为空时不追加容器选项。
     */
    private final String container;
    /**
     * 是否禁用 CLI 彩色输出，便于日志和机器解析。
     */
    private final boolean noColor;
    /**
     * 该请求或进程允许等待的最长时间，单位为秒；超时后主动取消对应任务。
     */
    private final Integer timeoutSeconds;
    /**
     * 按调用顺序保存的 CLI 参数；构建后以不可变列表暴露。
     */
    private final List<String> arguments;

    // ============================================================
    // Getters (non-Lombok)
    // ============================================================

    /**
     * 返回是否启用 CLI 开发模式。
     *
     * @return 是否为 CLI 启用开发模式
     */
    public boolean isDev() { return dev; }
    /**
     * 返回 CLI 配置档案名称；未指定时为空。
     *
     * @return CLI 配置文件名称；未指定时为 {@code null}
     */
    public String getProfile() { return profile; }
    /**
     * 返回 CLI 容器标识；未指定时为空。
     *
     * @return CLI 容器名称；未指定时为 {@code null}
     */
    public String getContainer() { return container; }
    /**
     * 返回是否禁用 CLI 彩色输出。
     *
     * @return 是否禁止 CLI 输出 ANSI 颜色
     */
    public boolean isNoColor() { return noColor; }
    /**
     * 返回 CLI 子进程超时，单位为秒。
     *
     * @return CLI 请求超时秒数；未设置时为空
     */
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    /**
     * 返回保持原始顺序的不可变 CLI 参数列表。
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
     * 创建空白构建器，供调用方链式设置 {@code OpenClawCliRequest} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code OpenClawCliRequest} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 是否向 CLI 追加开发模式开关。
         */
        private boolean dev;
        /**
         * 传递给 CLI 的配置档案名称；为空时使用 CLI 默认档案。
         */
        private String profile;
        /**
         * 传递给 CLI 的容器标识；为空时不追加容器选项。
         */
        private String container;
        /**
         * 是否禁用 CLI 彩色输出，便于日志和机器解析。
         */
        private boolean noColor;
        /**
         * 该请求或进程允许等待的最长时间，单位为秒；超时后主动取消对应任务。
         */
        private Integer timeoutSeconds;
        /**
         * 按调用顺序保存的 CLI 参数；构建后以不可变列表暴露。
         */
        private final List<String> arguments = new ArrayList<>();

        /**
         * 设置 {@code --dev} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dev 是否向命令行追加 {@code --dev} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dev(boolean dev) {
            this.dev = dev;
            return this;
        }

        /**
         * 设置 {@code --profile} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param profile openclaw CLI 配置档案名称；作为 {@code --profile} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        /**
         * 设置 {@code --container} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param container 目标容器名称；作为 {@code --container} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder container(String container) {
            this.container = container;
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
         * 设置 {@code --timeout-seconds} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutSeconds 超时时间，单位为秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
         * 设置 {@code --arguments} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param args 原样传给子命令的参数列表；作为 {@code --arguments} 的参数
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
         * 设置 {@code --arguments} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param args 原样传给子命令的参数列表；作为 {@code --arguments} 的参数
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
         * 校验并复制当前构建器字段，创建独立的 {@code OpenClawCliRequest}。
         *
         * @return 按当前字段创建的 OpenClawCliRequest
         */
        public OpenClawCliRequest build() {
            return new OpenClawCliRequest(this);
        }
    }
}
