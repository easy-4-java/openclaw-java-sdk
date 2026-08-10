package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code plugins} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class PluginsOptions implements CliSubArgs {

    /**
     * 定义插件管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示插件管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示插件管理动作的 {@code install} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSTALL,
        /**
         * 表示插件管理动作的 {@code inspect} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSPECT,
        /**
         * 表示插件管理动作的 {@code info} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INFO,
        /**
         * 表示插件管理动作的 {@code enable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ENABLE,
        /**
         * 表示插件管理动作的 {@code disable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DISABLE,
        /**
         * 表示插件管理动作的 {@code uninstall} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UNINSTALL,
        /**
         * 表示插件管理动作的 {@code doctor} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DOCTOR,
        /**
         * 表示插件管理动作的 {@code update} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UPDATE,
        /**
         * 表示插件管理动作的 {@code marketplace_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        MARKETPLACE_LIST
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-enabled} 开关。
     */
    private final boolean listEnabled;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-verbose} 开关。
     */
    private final boolean listVerbose;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
     */
    private final boolean listJson;
    /**
     * 待安装 Hook 或插件的包说明；未设置时命令行不包含 {@code --install-spec}。
     */
    private final String installSpec;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
     */
    private final boolean installForce;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-pin} 开关。
     */
    private final boolean installPin;
    /**
     * 是否向 openclaw 子命令追加 {@code --dangerously-force-unsafe-install} 开关。
     */
    private final boolean dangerouslyForceUnsafeInstall;
    /**
     * 插件市场来源；未设置时命令行不包含 {@code --marketplace}。
     */
    private final String marketplace;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-link} 开关。
     */
    private final boolean installLink;
    /**
     * 待检查资源的标识；未设置时命令行不包含 {@code --inspect-id}。
     */
    private final String inspectId;
    /**
     * 是否向 openclaw 子命令追加 {@code --inspect-json} 开关。
     */
    private final boolean inspectJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --inspect-all} 开关。
     */
    private final boolean inspectAll;
    /**
     * 目标插件标识；未设置时命令行不包含 {@code --plugin-id}。
     */
    private final String pluginId;
    /**
     * 是否向 openclaw 子命令追加 {@code --uninstall-dry-run} 开关。
     */
    private final boolean uninstallDryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --uninstall-keep-files} 开关。
     */
    private final boolean uninstallKeepFiles;
    /**
     * 是否向 openclaw 子命令追加 {@code --update-all} 开关。
     */
    private final boolean updateAll;
    /**
     * 是否向 openclaw 子命令追加 {@code --update-dry-run} 开关。
     */
    private final boolean updateDryRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 插件市场索引来源；未设置时命令行不包含 {@code --marketplace-source}。
     */
    private final String marketplaceSource;
    /**
     * 是否向 openclaw 子命令追加 {@code --marketplace-json} 开关。
     */
    private final boolean marketplaceJson;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code PluginsOptions} 字段。
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
     * {@code PluginsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 是否向 openclaw 子命令追加 {@code --list-enabled} 开关。
         */
        private boolean listEnabled;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-verbose} 开关。
         */
        private boolean listVerbose;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-json} 开关。
         */
        private boolean listJson;
        /**
         * 待安装 Hook 或插件的包说明；未设置时命令行不包含 {@code --install-spec}。
         */
        private String installSpec;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
         */
        private boolean installForce;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-pin} 开关。
         */
        private boolean installPin;
        /**
         * 是否向 openclaw 子命令追加 {@code --dangerously-force-unsafe-install} 开关。
         */
        private boolean dangerouslyForceUnsafeInstall;
        /**
         * 插件市场来源；未设置时命令行不包含 {@code --marketplace}。
         */
        private String marketplace;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-link} 开关。
         */
        private boolean installLink;
        /**
         * 待检查资源的标识；未设置时命令行不包含 {@code --inspect-id}。
         */
        private String inspectId;
        /**
         * 是否向 openclaw 子命令追加 {@code --inspect-json} 开关。
         */
        private boolean inspectJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --inspect-all} 开关。
         */
        private boolean inspectAll;
        /**
         * 目标插件标识；未设置时命令行不包含 {@code --plugin-id}。
         */
        private String pluginId;
        /**
         * 是否向 openclaw 子命令追加 {@code --uninstall-dry-run} 开关。
         */
        private boolean uninstallDryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --uninstall-keep-files} 开关。
         */
        private boolean uninstallKeepFiles;
        /**
         * 是否向 openclaw 子命令追加 {@code --update-all} 开关。
         */
        private boolean updateAll;
        /**
         * 是否向 openclaw 子命令追加 {@code --update-dry-run} 开关。
         */
        private boolean updateDryRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 插件市场索引来源；未设置时命令行不包含 {@code --marketplace-source}。
         */
        private String marketplaceSource;
        /**
         * 是否向 openclaw 子命令追加 {@code --marketplace-json} 开关。
         */
        private boolean marketplaceJson;
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
         * 设置 {@code --list-enabled} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param enabled 是否向命令行追加 {@code --list-enabled} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listEnabled(boolean enabled) {
            this.listEnabled = enabled;
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
         * 设置 {@code --install-force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 {@code --install-force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installForce(boolean force) {
            this.installForce = force;
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
         * 设置 {@code --dangerously-force-unsafe-install} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param unsafe 是否向命令行追加 {@code --dangerously-force-unsafe-install} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder dangerouslyForceUnsafeInstall(boolean unsafe) {
            this.dangerouslyForceUnsafeInstall = unsafe;
            return this;
        }

        /**
         * 设置 {@code --marketplace} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param marketplace 插件市场来源；作为 {@code --marketplace} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplace(String marketplace) {
            this.marketplace = marketplace;
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
         * 设置 {@code --inspect} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --inspect} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder inspect(String id) {
            this.mode = Mode.INSPECT;
            this.inspectId = id;
            this.inspectAll = false;
            return this;
        }

        /**
         * 设置 {@code --inspect-all} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 {@code --inspect-all} 开关
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
         * 设置 {@code --inspect-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder inspectJson(boolean json) {
            this.inspectJson = json;
            return this;
        }

        /**
         * 设置 {@code --info} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --info} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder info(String id) {
            this.mode = Mode.INFO;
            this.inspectId = id;
            return this;
        }

        /**
         * 设置 {@code --enable} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --enable} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder enable(String id) {
            this.mode = Mode.ENABLE;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 {@code --disable} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --disable} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder disable(String id) {
            this.mode = Mode.DISABLE;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 {@code --uninstall} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param id 目标资源标识；作为 {@code --uninstall} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstall(String id) {
            this.mode = Mode.UNINSTALL;
            this.pluginId = id;
            return this;
        }

        /**
         * 设置 {@code --uninstall-dry-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 {@code --uninstall-dry-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstallDryRun(boolean dryRun) {
            this.uninstallDryRun = dryRun;
            return this;
        }

        /**
         * 设置 {@code --uninstall-keep-files} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param keep 是否向命令行追加 {@code --uninstall-keep-files} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstallKeepFiles(boolean keep) {
            this.uninstallKeepFiles = keep;
            return this;
        }

        /**
         * 选择 {@code doctor} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder doctor() {
            this.mode = Mode.DOCTOR;
            return this;
        }

        /**
         * 设置 {@code --update} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param idOrSpec 待更新插件的标识或安装说明；作为 {@code --update} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update(String idOrSpec) {
            this.mode = Mode.UPDATE;
            this.updateAll = false;
            this.pluginId = idOrSpec;
            return this;
        }

        /**
         * 设置 {@code --update-all} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 {@code --update-all} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateAll(boolean all) {
            this.mode = Mode.UPDATE;
            this.updateAll = all;
            this.pluginId = null;
            return this;
        }

        /**
         * 设置 {@code --update-dry-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param dryRun 是否向命令行追加 {@code --update-dry-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateDryRun(boolean dryRun) {
            this.updateDryRun = dryRun;
            return this;
        }

        /**
         * 设置 {@code --yes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 {@code --yes} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
            return this;
        }

        /**
         * 设置 {@code --marketplace-list} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param source 插件市场数据来源；作为 {@code --marketplace-list} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplaceList(String source) {
            this.mode = Mode.MARKETPLACE_LIST;
            this.marketplaceSource = source;
            return this;
        }

        /**
         * 设置 {@code --marketplace-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder marketplaceJson(boolean json) {
            this.marketplaceJson = json;
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
         * 校验并复制当前构建器字段，创建独立的 {@code PluginsOptions}。
         *
         * @return 按当前字段创建的 PluginsOptions
         */
        public PluginsOptions build() {
            return new PluginsOptions(this);
        }
    }
}
