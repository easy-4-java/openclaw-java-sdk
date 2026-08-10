package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `skills` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SkillsOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `default_list` 协议模式；序列化时使用该固定取值。
         */
        DEFAULT_LIST,
        /**
         * 选择 `search` 协议模式；序列化时使用该固定取值。
         */
        SEARCH,
        /**
         * 选择 `install` 协议模式；序列化时使用该固定取值。
         */
        INSTALL,
        /**
         * 选择 `update` 协议模式；序列化时使用该固定取值。
         */
        UPDATE,
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
        CHECK
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--search-words` 选项的内容；为 null 时通常省略。
     */
    private final List<String> searchWords;
    /**
     * 传给 openclaw 子命令 `--search-limit` 选项的内容；为 null 时通常省略。
     */
    private final Integer searchLimit;
    /**
     * 是否向 openclaw 子命令追加 `--search-json` 开关。
     */
    private final boolean searchJson;
    /**
     * 传给 openclaw 子命令 `--install-slug` 选项的内容；为 null 时通常省略。
     */
    private final String installSlug;
    /**
     * 传给 openclaw 子命令 `--install-version` 选项的内容；为 null 时通常省略。
     */
    private final String installVersion;
    /**
     * 是否向 openclaw 子命令追加 `--install-force` 开关。
     */
    private final boolean installForce;
    /**
     * 传给 openclaw 子命令 `--update-slug` 选项的内容；为 null 时通常省略。
     */
    private final String updateSlug;
    /**
     * 是否向 openclaw 子命令追加 `--update-all` 开关。
     */
    private final boolean updateAll;
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
     * 传给 openclaw 子命令 `--info-name` 选项的内容；为 null 时通常省略。
     */
    private final String infoName;
    /**
     * 是否向 openclaw 子命令追加 `--info-json` 开关。
     */
    private final boolean infoJson;
    /**
     * 是否向 openclaw 子命令追加 `--check-json` 开关。
     */
    private final boolean checkJson;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `SkillsOptions` 字段。
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
     * 链式构建器，逐项收集 SkillsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SkillsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.DEFAULT_LIST;
        /**
         * 传给 openclaw 子命令 `--search-words` 选项的内容；为 null 时通常省略。
         */
        private List<String> searchWords = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--search-limit` 选项的内容；为 null 时通常省略。
         */
        private Integer searchLimit;
        /**
         * 是否向 openclaw 子命令追加 `--search-json` 开关。
         */
        private boolean searchJson;
        /**
         * 传给 openclaw 子命令 `--install-slug` 选项的内容；为 null 时通常省略。
         */
        private String installSlug;
        /**
         * 传给 openclaw 子命令 `--install-version` 选项的内容；为 null 时通常省略。
         */
        private String installVersion;
        /**
         * 是否向 openclaw 子命令追加 `--install-force` 开关。
         */
        private boolean installForce;
        /**
         * 传给 openclaw 子命令 `--update-slug` 选项的内容；为 null 时通常省略。
         */
        private String updateSlug;
        /**
         * 是否向 openclaw 子命令追加 `--update-all` 开关。
         */
        private boolean updateAll;
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
         * 传给 openclaw 子命令 `--info-name` 选项的内容；为 null 时通常省略。
         */
        private String infoName;
        /**
         * 是否向 openclaw 子命令追加 `--info-json` 开关。
         */
        private boolean infoJson;
        /**
         * 是否向 openclaw 子命令追加 `--check-json` 开关。
         */
        private boolean checkJson;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `defaultList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

        /**
         * 设置 `--search` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param queryWords 写入 `--search` 选项的内容
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
         * 设置 `--search-limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 写入 `--search-limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder searchLimit(int limit) {
            this.searchLimit = limit;
            return this;
        }

        /**
         * 设置 `--search-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder searchJson(boolean json) {
            this.searchJson = json;
            return this;
        }

        /**
         * 设置 `--install` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param slug 写入 `--install` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install(String slug) {
            this.verb = Verb.INSTALL;
            this.installSlug = slug;
            return this;
        }

        /**
         * 设置 `--install-version` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param version 写入 `--install-version` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installVersion(String version) {
            this.installVersion = version;
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
         * 设置 `--update` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param slug 写入 `--update` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder update(String slug) {
            this.verb = Verb.UPDATE;
            this.updateSlug = slug;
            this.updateAll = false;
            return this;
        }

        /**
         * 设置 `--update-all` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param all 是否向命令行追加 `--update-all` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder updateAll(boolean all) {
            this.verb = Verb.UPDATE;
            this.updateAll = all;
            this.updateSlug = null;
            return this;
        }

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
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
            this.verb = Verb.INFO;
            this.infoName = name;
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
            this.verb = Verb.CHECK;
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
         * 校验并复制当前构建器字段，创建独立的 `SkillsOptions`。
         *
         * @return 按当前字段创建的 SkillsOptions
         */
        public SkillsOptions build() {
            return new SkillsOptions(this);
        }
    }
}
