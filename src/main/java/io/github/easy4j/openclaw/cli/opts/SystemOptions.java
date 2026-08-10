package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw system}:Gateway system——systemevent,heartbeat, presence;subcommand Gateway RPC.
 * <p>:{@code --url},{@code --token},{@code --timeout},{@code --expect-final}.systemevent,Gateway .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/system">system CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class SystemOptions implements CliSubArgs {

 /** {@code system heartbeat} . */
    public enum HeartbeatSub {
        /** {@code heartbeat last} */
        LAST,
        /** {@code heartbeat enable} */
        ENABLE,
        /** {@code heartbeat disable} */
        DISABLE
    }

 /** {@code system} subcommand. */
    public enum Mode {
        /** {@code system event} */
        EVENT,
        /** {@code system heartbeat} */
        HEARTBEAT,
        /** {@code system presence} */
        PRESENCE
    }

    /**
 * subcommand:{@code event}(systemevent),{@code heartbeat},{@code presence}.
     */
    private final Mode mode;
    /**
 * {@code heartbeat} :{@code last}(event),{@code enable},{@code disable}(documentation:/restoreheartbeatschedule).
     */
    private final HeartbeatSub heartbeatSub;
    /**
 * {@code --url}:Gateway WebSocket( RPC subcommand).
     */
    private final String gatewayUrl;
    /**
 * {@code --token}:RPC authentication token.
     */
    private final String gatewayToken;
    /**
 * {@code --timeout}:RPC timeout.
     */
    private final String timeout;
    /**
 * {@code --expect-final}: RPC .
     */
    private final boolean expectFinal;
    /**
 * {@code --text}:systemevent({@code system event} );heartbeat {@code System:} injectsession.
     */
    private final String eventText;
    /**
 * {@code --mode}:{@code now} heartbeat, {@code next-heartbeat}schedule.
     */
    private final String eventMode;
    /**
 * {@code --json}:.
     */
    private final boolean json;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private SystemOptions(Builder b) {
        this.mode = b.mode;
        this.heartbeatSub = b.heartbeatSub;
        this.gatewayUrl = b.gatewayUrl;
        this.gatewayToken = b.gatewayToken;
        this.timeout = b.timeout;
        this.expectFinal = b.expectFinal;
        this.eventText = b.eventText;
        this.eventMode = b.eventMode;
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
        switch (mode) {
            case EVENT:
                out.add("event");
                OpenClawCliArgv.addIfPresent(out, "--text", eventText);
                OpenClawCliArgv.addIfPresent(out, "--mode", eventMode);
                break;
            case HEARTBEAT:
                out.add("heartbeat");
                if (heartbeatSub == HeartbeatSub.LAST) {
                    out.add("last");
                } else if (heartbeatSub == HeartbeatSub.ENABLE) {
                    out.add("enable");
                } else if (heartbeatSub == HeartbeatSub.DISABLE) {
                    out.add("disable");
                }
                break;
            case PRESENCE:
                out.add("presence");
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--url", gatewayUrl);
        OpenClawCliArgv.addIfPresent(out, "--token", gatewayToken);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addFlag(out, "--expect-final", expectFinal);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link SystemOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.PRESENCE;
        private HeartbeatSub heartbeatSub = HeartbeatSub.LAST;
        private String gatewayUrl;
        private String gatewayToken;
        private String timeout;
        private boolean expectFinal;
        private String eventText;
        private String eventMode;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
 * {@link Mode#EVENT} event.
         *
         * @param text {@code --text}
         * @return {@code this}
         */
        public Builder event(String text) {
            this.mode = Mode.EVENT;
            this.eventText = text;
            return this;
        }

        /**
 * @param eventMode {@code --mode}(event subcommand)
         * @return {@code this}
         */
        public Builder eventMode(String eventMode) {
            this.eventMode = eventMode;
            return this;
        }

        /**
         * @return {@code this}，heartbeat last
         */
        public Builder heartbeatLast() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.LAST;
            return this;
        }

        /**
         * @return {@code this}，heartbeat enable
         */
        public Builder heartbeatEnable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.ENABLE;
            return this;
        }

        /**
         * @return {@code this}，heartbeat disable
         */
        public Builder heartbeatDisable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.DISABLE;
            return this;
        }

        /**
 * @return {@code this},presence subcommand
         */
        public Builder presence() {
            this.mode = Mode.PRESENCE;
            return this;
        }

        /**
         * @param url {@code --url}
         * @return {@code this}
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
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
         * @param expectFinal {@code --expect-final}
         * @return {@code this}
         */
        public Builder expectFinal(boolean expectFinal) {
            this.expectFinal = expectFinal;
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
 * @return {@link SystemOptions}
         */
        public SystemOptions build() {
            return new SystemOptions(this);
        }
    }
}
