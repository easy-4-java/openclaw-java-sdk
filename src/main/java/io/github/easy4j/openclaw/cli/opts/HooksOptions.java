package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code hooks} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class HooksOptions implements CliSubArgs {

    /**
     * 定义Hook 管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示Hook 管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示Hook 管理动作的 {@code info} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INFO,
        /**
         * 表示Hook 管理动作的 {@code check} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CHECK,
        /**
         * 表示Hook 管理动作的 {@code enable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ENABLE,
        /**
         * 表示Hook 管理动作的 {@code disable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DISABLE,
        /**
         * 表示Hook 管理动作的 {@code install} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSTALL
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-eligible} 开关。
     */
    private final boolean listEligible;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
     */
    private final boolean listJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-verbose} 开关。
     */
    private final boolean listVerbose;
    /**
     * 目标 Hook 名称；未设置时命令行不包含 {@code --hook-name}。
     */
    private final String hookName;
    /**
     * 是否向 openclaw 子命令追加 {@code --info-json} 开关。
     */
    private final boolean infoJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --check-json} 开关。
     */
    private final boolean checkJson;
    /**
     * 待安装 Hook 或插件的包说明；未设置时命令行不包含 {@code --install-spec}。
     */
    private final String installSpec;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-link} 开关。
     */
    private final boolean installLink;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-pin} 开关。
     */
    private final boolean installPin;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private HooksOptions(Builder b) {
        this.mode = b.mode;
        this.listEligible = b.listEligible;
        this.listJson = b.listJson;
        this.listVerbose = b.listVerbose;
        this.hookName = b.hookName;
        this.infoJson = b.infoJson;
        this.checkJson = b.checkJson;
        this.installSpec = b.installSpec;
        this.installLink = b.installLink;
        this.installPin = b.installPin;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code HooksOptions} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 {@code hooks} 子命令的 {@code defaultList} 行为，并保留未设置选项的省略语义。
     *
     * @return 预设为 list 子命令的 Hook 选项
     */
    public static HooksOptions defaultList() {
        return builder().list().build();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--eligible", listEligible);
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
                break;
            case INFO:
                out.add("info");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", infoJson);
                break;
            case CHECK:
                out.add("check");
                OpenClawCliArgv.addFlag(out, "--json", checkJson);
                break;
            case ENABLE:
                out.add("enable");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                break;
            case DISABLE:
                out.add("disable");
                if (hookName != null && OpenClawStrings.isNotBlank(hookName)) {
                    out.add(hookName.trim());
                }
                break;
            case INSTALL:
                out.add("install");
                if (installSpec != null && OpenClawStrings.isNotBlank(installSpec)) {
                    out.add(installSpec.trim());
                }
                OpenClawCliArgv.addFlag(out, "--link", installLink);
                OpenClawCliArgv.addFlag(out, "--pin", installPin);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code HooksOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 是否向 openclaw 子命令追加 {@code --list-eligible} 开关。
         */
        private boolean listEligible;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
         */
        private boolean listJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-verbose} 开关。
         */
        private boolean listVerbose;
        /**
         * 目标 Hook 名称；未设置时命令行不包含 {@code --hook-name}。
         */
        private String hookName;
        /**
         * 是否向 openclaw 子命令追加 {@code --info-json} 开关。
         */
        private boolean infoJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --check-json} 开关。
         */
        private boolean checkJson;
        /**
         * 待安装 Hook 或插件的包说明；未设置时命令行不包含 {@code --install-spec}。
         */
        private String installSpec;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-link} 开关。
         */
        private boolean installLink;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-pin} 开关。
         */
        private boolean installPin;
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
         * 设置 {@code --list-eligible} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param eligible 是否向命令行追加 {@code --list-eligible} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listEligible(boolean eligible) {
            this.listEligible = eligible;
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
         * 设置 {@code --list-verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --list-verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listVerbose(boolean verbose) {
            this.listVerbose = verbose;
            return this;
        }

        /**
         * 设置 {@code --info} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --info} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder info(String name) {
            this.mode = Mode.INFO;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 {@code --info-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder infoJson(boolean json) {
            this.infoJson = json;
            return this;
        }

        /**
         * 选择 {@code check} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder check() {
            this.mode = Mode.CHECK;
            return this;
        }

        /**
         * 设置 {@code --check-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder checkJson(boolean json) {
            this.checkJson = json;
            return this;
        }

        /**
         * 设置 {@code --enable} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --enable} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder enable(String name) {
            this.mode = Mode.ENABLE;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 {@code --disable} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --disable} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder disable(String name) {
            this.mode = Mode.DISABLE;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 {@code --install} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param spec Hook 安装来源或包说明；作为 {@code --install} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(String spec) {
            this.mode = Mode.INSTALL;
            this.installSpec = spec;
            return this;
        }

        /**
         * 设置 {@code --install-link} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param link 是否向命令行追加 {@code --install-link} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installLink(boolean link) {
            this.installLink = link;
            return this;
        }

        /**
         * 设置 {@code --install-pin} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pin 是否向命令行追加 {@code --install-pin} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installPin(boolean pin) {
            this.installPin = pin;
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
         * 校验并复制当前构建器字段，创建独立的 {@code HooksOptions}。
         *
         * @return 按当前字段创建的 HooksOptions
         */
        public HooksOptions build() {
            return new HooksOptions(this);
        }
    }
}
