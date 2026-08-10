package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `doctor` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DoctorOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--no-workspace-suggestions` 开关。
     */
    private final boolean noWorkspaceSuggestions;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 `--repair` 开关。
     */
    private final boolean repair;
    /**
     * 是否向 openclaw 子命令追加 `--force` 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
     */
    private final boolean nonInteractive;
    /**
     * 是否向 openclaw 子命令追加 `--generate-gateway-token` 开关。
     */
    private final boolean generateGatewayToken;
    /**
     * 是否向 openclaw 子命令追加 `--deep` 开关。
     */
    private final boolean deep;

    /**
 * @param b builder
     */
    private DoctorOptions(Builder b) {
        this.noWorkspaceSuggestions = b.noWorkspaceSuggestions;
        this.yes = b.yes;
        this.repair = b.repair;
        this.force = b.force;
        this.nonInteractive = b.nonInteractive;
        this.generateGatewayToken = b.generateGatewayToken;
        this.deep = b.deep;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `DoctorOptions` 字段。
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
        if (noWorkspaceSuggestions) {
            out.add("--no-workspace-suggestions");
        }
        if (yes) {
            out.add("--yes");
        }
        if (repair) {
            out.add("--repair");
        }
        if (force) {
            out.add("--force");
        }
        if (nonInteractive) {
            out.add("--non-interactive");
        }
        if (generateGatewayToken) {
            out.add("--generate-gateway-token");
        }
        if (deep) {
            out.add("--deep");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 DoctorOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 DoctorOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 是否向 openclaw 子命令追加 `--no-workspace-suggestions` 开关。
         */
        private boolean noWorkspaceSuggestions;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 `--repair` 开关。
         */
        private boolean repair;
        /**
         * 是否向 openclaw 子命令追加 `--force` 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 `--non-interactive` 开关。
         */
        private boolean nonInteractive;
        /**
         * 是否向 openclaw 子命令追加 `--generate-gateway-token` 开关。
         */
        private boolean generateGatewayToken;
        /**
         * 是否向 openclaw 子命令追加 `--deep` 开关。
         */
        private boolean deep;

        /**
         * 设置 `--no-workspace-suggestions` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noWorkspaceSuggestions 是否向命令行追加 `--no-workspace-suggestions` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noWorkspaceSuggestions(boolean noWorkspaceSuggestions) {
            this.noWorkspaceSuggestions = noWorkspaceSuggestions;
            return this;
        }

        /**
         * 设置 `--yes` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 `--yes` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
         * 设置 `--repair` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param repair 是否向命令行追加 `--repair` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder repair(boolean repair) {
            this.repair = repair;
            return this;
        }

        /**
         * 设置 `--force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) {
            this.force = force;
            return this;
        }

        /**
         * 设置 `--non-interactive` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 `--non-interactive` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            this.nonInteractive = nonInteractive;
            return this;
        }

        /**
         * 设置 `--generate-gateway-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param generateGatewayToken 是否向命令行追加 `--generate-gateway-token` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder generateGatewayToken(boolean generateGatewayToken) {
            this.generateGatewayToken = generateGatewayToken;
            return this;
        }

        /**
         * 设置 `--deep` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deep 是否向命令行追加 `--deep` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `DoctorOptions`。
         *
         * @return 按当前字段创建的 DoctorOptions
         */
        public DoctorOptions build() {
            return new DoctorOptions(this);
        }
    }
}
