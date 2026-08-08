package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw security}:security,Optionaldocumentationrepair({@code audit --fix}).
 * <p>{@code --token}/{@code --password} onlyauthentication,;{@code --deep} ;{@code --json} CI/.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/security">security CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class SecurityOptions implements CliSubArgs {

 /** subcommand( {@code audit}). */
    public enum Mode {
        /** {@code security audit} */
        AUDIT
    }

    /**
 * ; {@link Mode#AUDIT}({@code security audit}).
     */
    private final Mode mode;
    /**
 * {@code --deep}:security(documentationexample CI ).
     */
    private final boolean deep;
    /**
 * {@code --password}:Gateway( SecretRef/).
     */
    private final String password;
    /**
 * {@code --token}: token(only).
     */
    private final String token;
    /**
 * {@code --fix}:documentationsecurity( groupPolicy,key);secret,.
     */
    private final boolean fix;
    /**
 * {@code --json}:( {@code --fix} repair).
     */
    private final boolean json;
    /**
 * .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SecurityOptions(Builder b) {
        this.mode = b.mode;
        this.deep = b.deep;
        this.password = b.password;
        this.token = b.token;
        this.fix = b.fix;
        this.json = b.json;
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
        if (mode == Mode.AUDIT) {
            out.add("audit");
            OpenClawCliArgv.addFlag(out, "--deep", deep);
            OpenClawCliArgv.addIfPresent(out, "--password", password);
            OpenClawCliArgv.addIfPresent(out, "--token", token);
            OpenClawCliArgv.addFlag(out, "--fix", fix);
            OpenClawCliArgv.addFlag(out, "--json", json);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link SecurityOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.AUDIT;
        private boolean deep;
        private String password;
        private String token;
        private boolean fix;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
 * @return {@code this}, {@link Mode#AUDIT}
         */
        public Builder audit() {
            this.mode = Mode.AUDIT;
            return this;
        }

        /**
         * @param deep {@code --deep}
         * @return {@code this}
         */
        public Builder deep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * @param password {@code --password}
         * @return {@code this}
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * @param fix {@code --fix}
         * @return {@code this}
         */
        public Builder fix(boolean fix) {
            this.fix = fix;
            return this;
        }

        /**
         * @param json {@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
 * @param tokens CLI token
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link SecurityOptions}
         */
        public SecurityOptions build() {
            return new SecurityOptions(this);
        }
    }
}
