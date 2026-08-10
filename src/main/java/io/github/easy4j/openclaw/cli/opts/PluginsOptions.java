package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `plugins` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class PluginsOptions implements CliSubArgs {

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
         * 选择 `install` 协议模式；序列化时使用该固定取值。
         */
        INSTALL,
        /**
         * 选择 `inspect` 协议模式；序列化时使用该固定取值。
         */
        INSPECT,
        /**
         * 选择 `info` 协议模式；序列化时使用该固定取值。
         */
        INFO,
        /**
         * 选择 `enable` 协议模式；序列化时使用该固定取值。
         */
        ENABLE,
        /**
         * 选择 `disable` 协议模式；序列化时使用该固定取值。
         */
        DISABLE,
        /**
         * 选择 `uninstall` 协议模式；序列化时使用该固定取值。
         */
        UNINSTALL,
        /**
         * 选择 `doctor` 协议模式；序列化时使用该固定取值。
         */
        DOCTOR,
        /**
         * 选择 `update` 协议模式；序列化时使用该固定取值。
         */
        UPDATE,
        /**
         * 选择 `marketplace_list` 协议模式；序列化时使用该固定取值。
         */
        MARKETPLACE_LIST
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--list-enabled` 开关。
     */
    private final boolean listEnabled;
    /**
     * 是否向 openclaw 子命令追加 `--list-verbose` 开关。
     */
    private final boolean listVerbose;
    /**
     * 是否向 openclaw 子命令追加 `--list-json` 开关。
     */
    private final boolean listJson;
    /**
     * 传给 openclaw 子命令 `--install-spec` 选项的内容；为 null 时通常省略。
     */
    private final String installSpec;
    /**
     * 是否向 openclaw 子命令追加 `--install-force` 开关。
     */
    private final boolean installForce;
    /**
     * 是否向 openclaw 子命令追加 `--install-pin` 开关。
     */
    private final boolean installPin;
    /**
     * 是否向 openclaw 子命令追加 `--dangerously-force-unsafe-install` 开关。
     */
    private final boolean dangerouslyForceUnsafeInstall;
    /**
     * 传给 openclaw 子命令 `--marketplace` 选项的内容；为 null 时通常省略。
     */
    private final String marketplace;
    /**
     * 是否向 openclaw 子命令追加 `--install-link` 开关。
     */
    private final boolean installLink;
    /**
     * 传给 openclaw 子命令 `--inspect-id` 选项的内容；为 null 时通常省略。
     */
    private final String inspectId;
    /**
     * 是否向 openclaw 子命令追加 `--inspect-json` 开关。
     */
    private final boolean inspectJson;
    /**
     * 是否向 openclaw 子命令追加 `--inspect-all` 开关。
     */
    private final boolean inspectAll;
    /**
     * 传给 openclaw 子命令 `--plugin-id` 选项的内容；为 null 时通常省略。
     */
    private final String pluginId;
    /**
     * 是否向 openclaw 子命令追加 `--uninstall-dry-run` 开关。
     */
    private final boolean uninstallDryRun;
    /**
     * 是否向 openclaw 子命令追加 `--uninstall-keep-files` 开关。
     */
    private final boolean uninstallKeepFiles;
    /**
     * 是否向 openclaw 子命令追加 `--update-all` 开关。
     */
    private final boolean updateAll;
    /**
     * 是否向 openclaw 子命令追加 `--update-dry-run` 开关。
     */
    private final boolean updateDryRun;
    /**
     * 是否向 openclaw 子命令追加 `--yes` 开关。
     */
    private final boolean yes;
    /**
     * 传给 openclaw 子命令 `--marketplace-source` 选项的内容；为 null 时通常省略。
     */
    private final String marketplaceSource;
    /**
     * 是否向 openclaw 子命令追加 `--marketplace-json` 开关。
     */
    private final boolean marketplaceJson;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private PluginsOptions(Builder b) {
        this.mode = b.mode;
        this.listEnabled = b.listEnabled;
        this.listVerbose = b.listVerbose;
        this.listJson = b.listJson;
        this.installSpec = b.installSpec;
        this.installForce = b.installForce;
        this.installPin = b.installPin;
        this.dangerouslyForceUnsafeInstall = b.dangerouslyForceUnsafeInstall;
        this.marketplace = b.marketplace;
        this.installLink = b.installLink;
        this.inspectId = b.inspectId;
        this.inspectJson = b.inspectJson;
        this.inspectAll = b.inspectAll;
        this.pluginId = b.pluginId;
        this.uninstallDryRun = b.uninstallDryRun;
        this.uninstallKeepFiles = b.uninstallKeepFiles;
        this.updateAll = b.updateAll;
        this.updateDryRun = b.updateDryRun;
        this.yes = b.yes;
        this.marketplaceSource = b.marketplaceSource;
        this.marketplaceJson = b.marketplaceJson;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `PluginsOptions` 字段。
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
        switch (mode) {
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--enabled", listEnabled);
                OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                break;
            case INSTALL:
                out.add("install");
                OpenClawCliArgv.addFlag(out, "-l", installLink);
                if (installSpec != null && OpenClawStrings.isNotBlank(installSpec)) {
                    out.add(installSpec.trim());
                }
                OpenClawCliArgv.addFlag(out, "--force", installForce);
                OpenClawCliArgv.addFlag(out, "--pin", installPin);
                OpenClawCliArgv.addFlag(out, "--dangerously-force-unsafe-install", dangerouslyForceUnsafeInstall);
                OpenClawCliArgv.addIfPresent(out, "--marketplace", marketplace);
                break;
            case INSPECT:
            case INFO:
                out.add(mode == Mode.INFO ? "info" : "inspect");
                if (inspectAll) {
                    out.add("--all");
                } else if (inspectId != null && OpenClawStrings.isNotBlank(inspectId)) {
                    out.add(inspectId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", inspectJson);
                break;
            case ENABLE:
                out.add("enable");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                break;
            case DISABLE:
                out.add("disable");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                break;
            case UNINSTALL:
                out.add("uninstall");
                if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--dry-run", uninstallDryRun);
                OpenClawCliArgv.addFlag(out, "--keep-files", uninstallKeepFiles);
                break;
            case DOCTOR:
                out.add("doctor");
                break;
            case UPDATE:
                out.add("update");
                if (updateAll) {
                    out.add("--all");
                } else if (pluginId != null && OpenClawStrings.isNotBlank(pluginId)) {
                    out.add(pluginId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--dry-run", updateDryRun);
                OpenClawCliArgv.addFlag(out, "--dangerously-force-unsafe-install", dangerouslyForceUnsafeInstall);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                break;
            case MARKETPLACE_LIST:
                out.add("marketplace");
                out.add("list");
                if (marketplaceSource != null && OpenClawStrings.isNotBlank(marketplaceSource)) {
                    out.add(marketplaceSource.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", marketplaceJson);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 PluginsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 PluginsOptions。
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
         * 是否向 openclaw 子命令追加 `--list-enabled` 开关。
         */
        private boolean listEnabled;
        /**
         * 是否向 openclaw 子命令追加 `--list-verbose` 开关。
         */
        private boolean listVerbose;
        /**
         * 是否向 openclaw 子命令追加 `--list-json` 开关。
         */
        private boolean listJson;
        /**
         * 传给 openclaw 子命令 `--install-spec` 选项的内容；为 null 时通常省略。
         */
        private String installSpec;
        /**
         * 是否向 openclaw 子命令追加 `--install-force` 开关。
         */
        private boolean installForce;
        /**
         * 是否向 openclaw 子命令追加 `--install-pin` 开关。
         */
        private boolean installPin;
        /**
         * 是否向 openclaw 子命令追加 `--dangerously-force-unsafe-install` 开关。
         */
        private boolean dangerouslyForceUnsafeInstall;
        /**
         * 传给 openclaw 子命令 `--marketplace` 选项的内容；为 null 时通常省略。
         */
        private String marketplace;
        /**
         * 是否向 openclaw 子命令追加 `--install-link` 开关。
         */
        private boolean installLink;
        /**
         * 传给 openclaw 子命令 `--inspect-id` 选项的内容；为 null 时通常省略。
         */
        private String inspectId;
        /**
         * 是否向 openclaw 子命令追加 `--inspect-json` 开关。
         */
        private boolean inspectJson;
        /**
         * 是否向 openclaw 子命令追加 `--inspect-all` 开关。
         */
        private boolean inspectAll;
        /**
         * 传给 openclaw 子命令 `--plugin-id` 选项的内容；为 null 时通常省略。
         */
        private String pluginId;
        /**
         * 是否向 openclaw 子命令追加 `--uninstall-dry-run` 开关。
         */
        private boolean uninstallDryRun;
        /**
         * 是否向 openclaw 子命令追加 `--uninstall-keep-files` 开关。
         */
        private boolean uninstallKeepFiles;
        /**
         * 是否向 openclaw 子命令追加 `--update-all` 开关。
         */
        private boolean updateAll;
        /**
         * 是否向 openclaw 子命令追加 `--update-dry-run` 开关。
         */
        private boolean updateDryRun;
        /**
         * 是否向 openclaw 子命令追加 `--yes` 开关。
         */
        private boolean yes;
        /**
         * 传给 openclaw 子命令 `--marketplace-source` 选项的内容；为 null 时通常省略。
         */
        private String marketplaceSource;
        /**
         * 是否向 openclaw 子命令追加 `--marketplace-json` 开关。
         */
        private boolean marketplaceJson;
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
         * 设置 `--list-enabled` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param enabled 是否向命令行追加 `--list-enabled` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listEnabled(boolean enabled) {
            this.listEnabled = enabled;
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
         * 设置 `--install-force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--install-force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installForce(boolean force) {
            this.installForce = force;
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
         * 设置 `--dangerously-force-unsafe-install` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param unsafe 是否向命令行追加 `--dangerously-force-unsafe-install` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dangerouslyForceUnsafeInstall(boolean unsafe) {
            this.dangerouslyForceUnsafeInstall = unsafe;
            return this;
        }

        /**
         * 设置 `--marketplace` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param marketplace 写入 `--marketplace` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplace(String marketplace) {
            this.marketplace = marketplace;
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
         * 设置 `--inspect` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--inspect` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder inspect(String id) {
            this.mode = Mode.INSPECT;
            this.inspectId = id;
            this.inspectAll = false;
            return this;
        }

        /**
         * 设置 `--inspect-all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--inspect-all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder inspectAll(boolean all) {
            this.inspectAll = all;
            if (all) {
                this.mode = Mode.INSPECT;
                this.inspectId = null;
            }
            return this;
        }

        /**
         * 设置 `--inspect-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder inspectJson(boolean json) {
            this.inspectJson = json;
            return this;
        }

        /**
         * 设置 `--info` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--info` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder info(String id) {
            this.mode = Mode.INFO;
            this.inspectId = id;
            return this;
        }

        /**
         * 设置 `--enable` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--enable` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder enable(String id) {
            this.mode = Mode.ENABLE;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 `--disable` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--disable` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder disable(String id) {
            this.mode = Mode.DISABLE;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 `--uninstall` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 写入 `--uninstall` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstall(String id) {
            this.mode = Mode.UNINSTALL;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 `--uninstall-dry-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 `--uninstall-dry-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstallDryRun(boolean dryRun) {
            this.uninstallDryRun = dryRun;
            return this;
        }

        /**
         * 设置 `--uninstall-keep-files` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param keep 是否向命令行追加 `--uninstall-keep-files` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstallKeepFiles(boolean keep) {
            this.uninstallKeepFiles = keep;
            return this;
        }

        /**
         * 选择 `doctor` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder doctor() {
            this.mode = Mode.DOCTOR;
            return this;
        }

        /**
         * 设置 `--update` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param idOrSpec 写入 `--update` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update(String idOrSpec) {
            this.mode = Mode.UPDATE;
            this.updateAll = false;
            this.pluginId = idOrSpec;
            return this;
        }

        /**
         * 设置 `--update-all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--update-all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateAll(boolean all) {
            this.mode = Mode.UPDATE;
            this.updateAll = all;
            this.pluginId = null;
            return this;
        }

        /**
         * 设置 `--update-dry-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 `--update-dry-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateDryRun(boolean dryRun) {
            this.updateDryRun = dryRun;
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
         * 设置 `--marketplace-list` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param source 写入 `--marketplace-list` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplaceList(String source) {
            this.mode = Mode.MARKETPLACE_LIST;
            this.marketplaceSource = source;
            return this;
        }

        /**
         * 设置 `--marketplace-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplaceJson(boolean json) {
            this.marketplaceJson = json;
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
         * 校验并复制当前构建器字段，创建独立的 `PluginsOptions`。
         *
         * @return 按当前字段创建的 PluginsOptions
         */
        public PluginsOptions build() {
            return new PluginsOptions(this);
        }
    }
}
