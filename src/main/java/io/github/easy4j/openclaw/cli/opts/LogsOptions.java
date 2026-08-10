package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code openclaw logs}: RPC Gateway .
 * <p>Seedocumentation"Options"; Gateway flag({@link GatewayRpcOptions}),
 * {@code --timeout} 30000ms,{@code --expect-final} Used for agent .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/logs">logs CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class LogsOptions implements CliSubArgs {

    /**
 * RPC :{@code --url},{@code --token},{@code --password},{@code --timeout},{@code --expect-final},{@code --json} ( gateway documentation"Shared options").
     */
    private final GatewayRpcOptions rpc;
    /**
 * {@code --limit}:(documentation {@code 200}).
     */
    private final String limit;
    /**
 * {@code --max-bytes}:bytes(documentation {@code 250000}).
     */
    private final String maxBytes;
    /**
 * {@code --follow}:stream( {@code --interval} ).
     */
    private final boolean follow;
    /**
 * {@code --interval}:follow milliseconds(documentation {@code 1000}).
     */
    private final String intervalMs;
    /**
 * {@code --json}: JSON event.
     */
    private final boolean json;
    /**
 * {@code --plain}:,.
     */
    private final boolean plain;
    /**
 * {@code --no-color}: ANSI .
     */
    private final boolean noColor;
    /**
 * {@code --local-time}:.
     */
    private final boolean localTime;

    /**
 * @param b builder;{@code rpc} {@link GatewayRpcOptions}
     */
    private LogsOptions(Builder b) {
        this.rpc = b.rpc != null ? b.rpc : GatewayRpcOptions.builder().build();
        this.limit = b.limit;
        this.maxBytes = b.maxBytes;
        this.follow = b.follow;
        this.intervalMs = b.intervalMs;
        this.json = b.json;
        this.plain = b.plain;
        this.noColor = b.noColor;
        this.localTime = b.localTime;
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
        rpc.appendSharedFlags(out);
        if (limit != null && !limit.isEmpty()) {
            out.add("--limit");
            out.add(limit);
        }
        if (maxBytes != null && !maxBytes.isEmpty()) {
            out.add("--max-bytes");
            out.add(maxBytes);
        }
        if (follow) {
            out.add("--follow");
        }
        if (intervalMs != null && !intervalMs.isEmpty()) {
            out.add("--interval");
            out.add(intervalMs);
        }
        if (json) {
            out.add("--json");
        }
        if (plain) {
            out.add("--plain");
        }
        if (noColor) {
            out.add("--no-color");
        }
        if (localTime) {
            out.add("--local-time");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link LogsOptions} builder.
     */
    public static final class Builder {

        private GatewayRpcOptions rpc;
        private String limit;
        private String maxBytes;
        private boolean follow;
        private String intervalMs;
        private boolean json;
        private boolean plain;
        private boolean noColor;
        private boolean localTime;

 /** {@code --url} / {@code --token} / {@code --timeout} / {@code --expect-final} . */
        public Builder rpc(GatewayRpcOptions rpc) {
            this.rpc = Objects.requireNonNull(rpc, "rpc");
            return this;
        }

        /**
         * @param limit {@code --limit}
         * @return {@code this}
         */
        public Builder limit(String limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param maxBytes {@code --max-bytes}
         * @return {@code this}
         */
        public Builder maxBytes(String maxBytes) {
            this.maxBytes = maxBytes;
            return this;
        }

        /**
         * @param follow {@code --follow}
         * @return {@code this}
         */
        public Builder follow(boolean follow) {
            this.follow = follow;
            return this;
        }

        /**
 * @param intervalMs {@code --interval}(millisecondscharacters)
         * @return {@code this}
         */
        public Builder intervalMs(String intervalMs) {
            this.intervalMs = intervalMs;
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
         * @param plain {@code --plain}
         * @return {@code this}
         */
        public Builder plain(boolean plain) {
            this.plain = plain;
            return this;
        }

        /**
         * @param noColor {@code --no-color}
         * @return {@code this}
         */
        public Builder noColor(boolean noColor) {
            this.noColor = noColor;
            return this;
        }

        /**
         * @param localTime {@code --local-time}
         * @return {@code this}
         */
        public Builder localTime(boolean localTime) {
            this.localTime = localTime;
            return this;
        }

        /**
 * @return {@link LogsOptions}
         */
        public LogsOptions build() {
            return new LogsOptions(this);
        }
    }
}
