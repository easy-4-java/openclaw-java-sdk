package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw skills}: ClawHub/, agent skills,skill.
 * <p> {@code openclaw skills} {@code skills list} ;streamplugin,version skills CLI .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/skills">skills CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class SkillsOptions implements CliSubArgs {

    /**
 * skills subcommand:,,.
     */
    public enum Verb {
        /**
 * {@code openclaw skills} : {@code list} token, CLI list .
         */
        DEFAULT_LIST,
 /** {@code skills search}:key ClawHub directory. */
        SEARCH,
 /** {@code skills install}: slug skilldirectory. */
        INSTALL,
 /** {@code skills update}: skill {@code --all}. */
        UPDATE,
 /** {@code skills list}:/Seeskill. */
        LIST,
 /** {@code skills info}:. */
        INFO,
 /** {@code skills check}: workspace skill. */
        CHECK
    }

 /** search / install / update / list / info / check list. */
    private final Verb verb;
    /**
 * search:keyargument list.
     */
    private final List<String> searchWords;
    /**
 * search:{@code --limit} .
     */
    private final Integer searchLimit;
    /**
     * search：{@code --json}。
     */
    private final boolean searchJson;
    /**
 * install:skill slug .
     */
    private final String installSlug;
    /**
 * install:{@code --version} version tag.
     */
    private final String installVersion;
    /**
 * install:{@code --force} .
     */
    private final boolean installForce;
    /**
 * update: slug; {@code updateAll} mutually exclusive semantics Builder .
     */
    private final String updateSlug;
    /**
 * update:{@code --all} skills.
     */
    private final boolean updateAll;
    /**
 * list / :{@code --eligible} skill.
     */
    private final boolean listEligible;
    /**
 * list / :{@code --json}.
     */
    private final boolean listJson;
    /**
 * list / :{@code --verbose} .
     */
    private final boolean listVerbose;
    /**
 * info:skill .
     */
    private final String infoName;
    /**
     * info：{@code --json}。
     */
    private final boolean infoJson;
    /**
 * check:{@code --json} .
     */
    private final boolean checkJson;
    /**
 * argv.
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
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@inheritDoc}
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
 * {@link SkillsOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.DEFAULT_LIST;
        private List<String> searchWords = new ArrayList<>();
        private Integer searchLimit;
        private boolean searchJson;
        private String installSlug;
        private String installVersion;
        private boolean installForce;
        private String updateSlug;
        private boolean updateAll;
        private boolean listEligible;
        private boolean listJson;
        private boolean listVerbose;
        private String infoName;
        private boolean infoJson;
        private boolean checkJson;
        private List<String> extra = new ArrayList<>();

        /**
 * @return {@code this}(subcommand, list flag )
         */
        public Builder defaultList() {
            this.verb = Verb.DEFAULT_LIST;
            return this;
        }

        /**
         * {@code skills search [words...]}。
         *
 * @param queryWords
         * @return {@code this}
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
         * @param limit search：{@code --limit}
         * @return {@code this}
         */
        public Builder searchLimit(int limit) {
            this.searchLimit = limit;
            return this;
        }

        /**
         * @param json search：{@code --json}
         * @return {@code this}
         */
        public Builder searchJson(boolean json) {
            this.searchJson = json;
            return this;
        }

        /**
         * @param slug install：slug
         * @return {@code this}
         */
        public Builder install(String slug) {
            this.verb = Verb.INSTALL;
            this.installSlug = slug;
            return this;
        }

        /**
         * @param version install：{@code --version}
         * @return {@code this}
         */
        public Builder installVersion(String version) {
            this.installVersion = version;
            return this;
        }

        /**
         * @param force install：{@code --force}
         * @return {@code this}
         */
        public Builder installForce(boolean force) {
            this.installForce = force;
            return this;
        }

        /**
         * @param slug update：slug
         * @return {@code this}
         */
        public Builder update(String slug) {
            this.verb = Verb.UPDATE;
            this.updateSlug = slug;
            this.updateAll = false;
            return this;
        }

        /**
         * @param all update：{@code --all}
         * @return {@code this}
         */
        public Builder updateAll(boolean all) {
            this.verb = Verb.UPDATE;
            this.updateAll = all;
            this.updateSlug = null;
            return this;
        }

        /**
         * @return {@code this}（{@code skills list}）
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * @param eligible list：{@code --eligible}
         * @return {@code this}
         */
        public Builder listEligible(boolean eligible) {
            this.listEligible = eligible;
            return this;
        }

        /**
         * @param json list：{@code --json}
         * @return {@code this}
         */
        public Builder listJson(boolean json) {
            this.listJson = json;
            return this;
        }

        /**
         * @param verbose list：{@code --verbose}
         * @return {@code this}
         */
        public Builder listVerbose(boolean verbose) {
            this.listVerbose = verbose;
            return this;
        }

        /**
 * @param name info:skill
         * @return {@code this}
         */
        public Builder info(String name) {
            this.verb = Verb.INFO;
            this.infoName = name;
            return this;
        }

        /**
         * @param json info：{@code --json}
         * @return {@code this}
         */
        public Builder infoJson(boolean json) {
            this.infoJson = json;
            return this;
        }

        /**
         * @return {@code this}（{@code skills check}）
         */
        public Builder check() {
            this.verb = Verb.CHECK;
            return this;
        }

        /**
         * @param json check：{@code --json}
         * @return {@code this}
         */
        public Builder checkJson(boolean json) {
            this.checkJson = json;
            return this;
        }

        /**
 * token.
         *
 * @param tokens argv
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link SkillsOptions}
         */
        public SkillsOptions build() {
            return new SkillsOptions(this);
        }
    }
}
