package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw update}: stable/beta/dev channelsecurity(npm git streamSeedocumentation).
 * <p>{@code --dry-run} only;{@code --yes} Used forskips;{@code openclaw --update} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/update">update CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class UpdateOptions implements CliSubArgs {

    /**
 * update subcommand:,{@code update status} channel,{@code update wizard} channel.
     */
    public enum Mode {
 /** {@code openclaw update}(subcommand),stream. */
        DEFAULT,
 /** {@code update status}:channel,git npm version. */
        STATUS,
 /** {@code update wizard}:channel Gateway. */
        WIZARD
    }

 /** ,status wizard. */
    private final Mode mode;
    /**
 * {@code --channel}:channel(stable/beta/dev ,documentation).
     */
    private final String channel;
    /**
 * {@code --tag}:only( git npm dist-tag;{@code main} map).
     */
    private final String tag;
    /**
 * {@code --dry-run}:,channel,,.
     */
    private final boolean dryRun;
    /**
 * {@code --no-restart}: Gateway .
     */
    private final boolean noRestart;
    /**
 * {@code --yes}:skips.
     */
    private final boolean yes;
    /**
 * {@code --json}: {@code UpdateRunResult} status JSON.
     */
    private final boolean json;
    /**
 * {@code --timeout}:timeout(stream 1200 seconds,status ,Seedocumentation).
     */
    private final String timeout;
    /**
 * documentation argv .
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private UpdateOptions(Builder b) {
        this.mode = b.mode;
        this.channel = b.channel;
        this.tag = b.tag;
        this.dryRun = b.dryRun;
        this.noRestart = b.noRestart;
        this.yes = b.yes;
        this.json = b.json;
        this.timeout = b.timeout;
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
        switch (mode) {
            case DEFAULT:
                OpenClawCliArgv.addIfPresent(out, "--channel", channel);
                OpenClawCliArgv.addIfPresent(out, "--tag", tag);
                OpenClawCliArgv.addFlag(out, "--dry-run", dryRun);
                OpenClawCliArgv.addFlag(out, "--no-restart", noRestart);
                OpenClawCliArgv.addFlag(out, "--yes", yes);
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--json", json);
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            case WIZARD:
                out.add("wizard");
                OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link UpdateOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.DEFAULT;
        private String channel;
        private String tag;
        private boolean dryRun;
        private boolean noRestart;
        private boolean yes;
        private boolean json;
        private String timeout;
        private List<String> extra = new ArrayList<>();

        /**
 * {@code openclaw update}(subcommand).
         *
         * @return {@code this}
         */
        public Builder update() {
            this.mode = Mode.DEFAULT;
            return this;
        }

        /**
         * @param channel {@code --channel}
         * @return {@code this}
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * @param tag {@code --tag}
         * @return {@code this}
         */
        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * @param dryRun {@code --dry-run}
         * @return {@code this}
         */
        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        /**
         * @param noRestart {@code --no-restart}
         * @return {@code this}
         */
        public Builder noRestart(boolean noRestart) {
            this.noRestart = noRestart;
            return this;
        }

        /**
         * @param yes {@code --yes}
         * @return {@code this}
         */
        public Builder yes(boolean yes) {
            this.yes = yes;
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
         * @param timeout {@code --timeout}
         * @return {@code this}
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * @return {@code this}（{@code update status}）
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * @return {@code this}（{@code update wizard}）
         */
        public Builder wizard() {
            this.mode = Mode.WIZARD;
            return this;
        }

        /**
 * appends extra argv token.
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
 * @return {@link UpdateOptions}
         */
        public UpdateOptions build() {
            return new UpdateOptions(this);
        }
    }
}
