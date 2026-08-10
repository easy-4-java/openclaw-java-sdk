package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `hooks` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class HooksOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `info` 协议模式；序列化时使用该固定取值。
         */
        INFO,
        /**
         * 选择 `check` 协议模式；序列化时使用该固定取值。
         */
        CHECK,
        /**
         * 选择 `enable` 协议模式；序列化时使用该固定取值。
         */
        ENABLE,
        /**
         * 选择 `disable` 协议模式；序列化时使用该固定取值。
         */
        DISABLE,
        /**
         * 选择 `install` 协议模式；序列化时使用该固定取值。
         */
        INSTALL
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--list-eligible` 开关。
     */
    private final boolean listEligible;
    /**
     * 是否向 openclaw 子命令追加 `--list-json` 开关。
     */
    private final boolean listJson;
    /**
     * 是否向 openclaw 子命令追加 `--list-verbose` 开关。
     */
    private final boolean listVerbose;
    /**
     * 传给 openclaw 子命令 `--hook-name` 选项的内容；为 null 时通常省略。
     */
    private final String hookName;
    /**
     * 是否向 openclaw 子命令追加 `--info-json` 开关。
     */
    private final boolean infoJson;
    /**
     * 是否向 openclaw 子命令追加 `--check-json` 开关。
     */
    private final boolean checkJson;
    /**
     * 传给 openclaw 子命令 `--install-spec` 选项的内容；为 null 时通常省略。
     */
    private final String installSpec;
    /**
     * 是否向 openclaw 子命令追加 `--install-link` 开关。
     */
    private final boolean installLink;
    /**
     * 是否向 openclaw 子命令追加 `--install-pin` 开关。
     */
    private final boolean installPin;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `HooksOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 `hooks` 子命令的 `defaultList` 行为，并保留未设置选项的省略语义。
     *
     * @return 按当前参数创建、查询或解析得到的 HooksOptions
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
     * 链式构建器，逐项收集 HooksOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 HooksOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.LIST;
        /**
         * 是否向 openclaw 子命令追加 `--list-eligible` 开关。
         */
        private boolean listEligible;
        /**
         * 是否向 openclaw 子命令追加 `--list-json` 开关。
         */
        private boolean listJson;
        /**
         * 是否向 openclaw 子命令追加 `--list-verbose` 开关。
         */
        private boolean listVerbose;
        /**
         * 传给 openclaw 子命令 `--hook-name` 选项的内容；为 null 时通常省略。
         */
        private String hookName;
        /**
         * 是否向 openclaw 子命令追加 `--info-json` 开关。
         */
        private boolean infoJson;
        /**
         * 是否向 openclaw 子命令追加 `--check-json` 开关。
         */
        private boolean checkJson;
        /**
         * 传给 openclaw 子命令 `--install-spec` 选项的内容；为 null 时通常省略。
         */
        private String installSpec;
        /**
         * 是否向 openclaw 子命令追加 `--install-link` 开关。
         */
        private boolean installLink;
        /**
         * 是否向 openclaw 子命令追加 `--install-pin` 开关。
         */
        private boolean installPin;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * 设置 `--list-eligible` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param eligible 是否向命令行追加 `--list-eligible` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listEligible(boolean eligible) {
            this.listEligible = eligible;
            return this;
        }

        /**
         * 设置 `--list-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
         * 设置 `--list-verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--list-verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listVerbose(boolean verbose) {
            this.listVerbose = verbose;
            return this;
        }

        /**
         * 设置 `--info` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--info` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder info(String name) {
            this.mode = Mode.INFO;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 `--info-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder infoJson(boolean json) {
            this.infoJson = json;
            return this;
        }

        /**
         * 选择 `check` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder check() {
            this.mode = Mode.CHECK;
            return this;
        }

        /**
         * 设置 `--check-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder checkJson(boolean json) {
            this.checkJson = json;
            return this;
        }

        /**
         * 设置 `--enable` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--enable` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder enable(String name) {
            this.mode = Mode.ENABLE;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 `--disable` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--disable` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder disable(String name) {
            this.mode = Mode.DISABLE;
            this.hookName = name;
            return this;
        }

        /**
         * 设置 `--install` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param spec 写入 `--install` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(String spec) {
            this.mode = Mode.INSTALL;
            this.installSpec = spec;
            return this;
        }

        /**
         * 设置 `--install-link` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param link 是否向命令行追加 `--install-link` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installLink(boolean link) {
            this.installLink = link;
            return this;
        }

        /**
         * 设置 `--install-pin` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pin 是否向命令行追加 `--install-pin` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installPin(boolean pin) {
            this.installPin = pin;
            return this;
        }

        /**
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `HooksOptions`。
         *
         * @return 按当前字段创建的 HooksOptions
         */
        public HooksOptions build() {
            return new HooksOptions(this);
        }
    }
}
