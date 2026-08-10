package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw dashboard}:authentication Control UI.
 * <p>documentation: {@code gateway.auth.token} SecretRef; SecretRef token,// URL <strong> token</strong>,
 * terminal,secret.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/dashboard">dashboard CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class DashboardOptions implements CliSubArgs {

    /**
 * {@code --no-open}: URL(documentation),.
     */
    private final boolean noOpen;
    /**
 * documentation CLI token.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private DashboardOptions(Builder b) {
        this.noOpen = b.noOpen;
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
        OpenClawCliArgv.addFlag(out, "--no-open", noOpen);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link DashboardOptions} builder.
     */
    public static final class Builder {
        private boolean noOpen;
        private List<String> extra = new ArrayList<>();

        /**
         * @param noOpen {@code --no-open}
         * @return {@code this}
         */
        public Builder noOpen(boolean noOpen) {
            this.noOpen = noOpen;
            return this;
        }

        /**
 * appends extra CLI token.
         *
 * @param tokens null
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
 * @return {@link DashboardOptions}
         */
        public DashboardOptions build() {
            return new DashboardOptions(this);
        }
    }
}
