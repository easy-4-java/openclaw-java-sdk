package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code skills} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SkillsOptions implements CliSubArgs {

    /**
     * 定义技能管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示技能管理动作的 {@code default_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DEFAULT_LIST,
        /**
         * 表示技能管理动作的 {@code search} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SEARCH,
        /**
         * 表示技能管理动作的 {@code install} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSTALL,
        /**
         * 表示技能管理动作的 {@code update} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UPDATE,
        /**
         * 表示技能管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示技能管理动作的 {@code info} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INFO,
        /**
         * 表示技能管理动作的 {@code check} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CHECK
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 技能搜索关键字列表；未设置时命令行不包含 {@code --search-words}。
     */
    private final List<String> searchWords;
    /**
     * 技能搜索结果数量上限；未设置时命令行不包含 {@code --search-limit}。
     */
    private final Integer searchLimit;
    /**
     * 是否向 openclaw 子命令追加 {@code --search-json} 开关。
     */
    private final boolean searchJson;
    /**
     * 待安装技能的短标识；未设置时命令行不包含 {@code --install-slug}。
     */
    private final String installSlug;
    /**
     * 待安装资源的版本；未设置时命令行不包含 {@code --install-version}。
     */
    private final String installVersion;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
     */
    private final boolean installForce;
    /**
     * 待更新技能的短标识；未设置时命令行不包含 {@code --update-slug}。
     */
    private final String updateSlug;
    /**
     * 是否向 openclaw 子命令追加 {@code --update-all} 开关。
     */
    private final boolean updateAll;
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
     * 待查看详情的资源名称；未设置时命令行不包含 {@code --info-name}。
     */
    private final String infoName;
    /**
     * 是否向 openclaw 子命令追加 {@code --info-json} 开关。
     */
    private final boolean infoJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --check-json} 开关。
     */
    private final boolean checkJson;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SkillsOptions(Builder b) {
        this.verb = b.verb;
        this.searchWords = b.searchWords == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.searchWords);
        this.searchLimit = b.searchLimit;
        this.searchJson = b.searchJson;
        this.installSlug = b.installSlug;
        this.installVersion = b.installVersion;
        this.installForce = b.installForce;
        this.updateSlug = b.updateSlug;
        this.updateAll = b.updateAll;
        this.listEligible = b.listEligible;
        this.listJson = b.listJson;
        this.listVerbose = b.listVerbose;
        this.infoName = b.infoName;
        this.infoJson = b.infoJson;
        this.checkJson = b.checkJson;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code SkillsOptions} 字段。
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
        switch (verb) {
            case DEFAULT_LIST:
                break;
            case SEARCH:
                out.add("search");
                out.addAll(searchWords);
                OpenClawCliArgv.addIfNotNull(out, "--limit", searchLimit);
                OpenClawCliArgv.addFlag(out, "--json", searchJson);
                break;
            case INSTALL:
                out.add("install");
                if (installSlug != null && OpenClawStrings.isNotBlank(installSlug)) {
                    out.add(installSlug.trim());
                }
                OpenClawCliArgv.addIfPresent(out, "--version", installVersion);
                OpenClawCliArgv.addFlag(out, "--force", installForce);
                break;
            case UPDATE:
                out.add("update");
                if (updateAll) {
                    out.add("--all");
                } else if (updateSlug != null && OpenClawStrings.isNotBlank(updateSlug)) {
                    out.add(updateSlug.trim());
                }
                break;
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--eligible", listEligible);
                OpenClawCliArgv.addFlag(out, "--json", listJson);
                OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
                break;
            case INFO:
                out.add("info");
                if (infoName != null && OpenClawStrings.isNotBlank(infoName)) {
                    out.add(infoName.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", infoJson);
                break;
            case CHECK:
                out.add("check");
                OpenClawCliArgv.addFlag(out, "--json", checkJson);
                break;
            default:
                break;
        }
        if (verb == Verb.DEFAULT_LIST) {
            OpenClawCliArgv.addFlag(out, "--eligible", listEligible);
            OpenClawCliArgv.addFlag(out, "--json", listJson);
            OpenClawCliArgv.addFlag(out, "--verbose", listVerbose);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code SkillsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.DEFAULT_LIST;
        /**
         * 技能搜索关键字列表；未设置时命令行不包含 {@code --search-words}。
         */
        private List<String> searchWords = new ArrayList<>();
        /**
         * 技能搜索结果数量上限；未设置时命令行不包含 {@code --search-limit}。
         */
        private Integer searchLimit;
        /**
         * 是否向 openclaw 子命令追加 {@code --search-json} 开关。
         */
        private boolean searchJson;
        /**
         * 待安装技能的短标识；未设置时命令行不包含 {@code --install-slug}。
         */
        private String installSlug;
        /**
         * 待安装资源的版本；未设置时命令行不包含 {@code --install-version}。
         */
        private String installVersion;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
         */
        private boolean installForce;
        /**
         * 待更新技能的短标识；未设置时命令行不包含 {@code --update-slug}。
         */
        private String updateSlug;
        /**
         * 是否向 openclaw 子命令追加 {@code --update-all} 开关。
         */
        private boolean updateAll;
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
         * 待查看详情的资源名称；未设置时命令行不包含 {@code --info-name}。
         */
        private String infoName;
        /**
         * 是否向 openclaw 子命令追加 {@code --info-json} 开关。
         */
        private boolean infoJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --check-json} 开关。
         */
        private boolean checkJson;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code defaultList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

        /**
         * 设置 {@code --search} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param queryWords 技能搜索关键字列表；作为 {@code --search} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder search(String... queryWords) {
            this.verb = Verb.SEARCH;
            this.searchWords = new ArrayList<>();
            if (queryWords != null) {
                for (String w : queryWords) {
                    if (w != null && OpenClawStrings.isNotBlank(w)) {
                        searchWords.add(w.trim());
                    }
                }
            }
            return this;
        }

        /**
         * 设置 {@code --search-limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 返回结果数量上限；作为 {@code --search-limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder searchLimit(int limit) {
            this.searchLimit = limit;
            return this;
        }

        /**
         * 设置 {@code --search-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder searchJson(boolean json) {
            this.searchJson = json;
            return this;
        }

        /**
         * 设置 {@code --install} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param slug 技能的稳定短标识；作为 {@code --install} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(String slug) {
            this.verb = Verb.INSTALL;
            this.installSlug = slug;
            return this;
        }

        /**
         * 设置 {@code --install-version} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param version 待安装技能的版本；作为 {@code --install-version} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installVersion(String version) {
            this.installVersion = version;
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
         * 设置 {@code --update} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param slug 技能的稳定短标识；作为 {@code --update} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update(String slug) {
            this.verb = Verb.UPDATE;
            this.updateSlug = slug;
            this.updateAll = false;
            return this;
        }

        /**
         * 设置 {@code --update-all} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 {@code --update-all} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateAll(boolean all) {
            this.verb = Verb.UPDATE;
            this.updateAll = all;
            this.updateSlug = null;
            return this;
        }

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
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
            this.verb = Verb.INFO;
            this.infoName = name;
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
            this.verb = Verb.CHECK;
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
         * 校验并复制当前构建器字段，创建独立的 {@code SkillsOptions}。
         *
         * @return 按当前字段创建的 SkillsOptions
         */
        public SkillsOptions build() {
            return new SkillsOptions(this);
        }
    }
}
