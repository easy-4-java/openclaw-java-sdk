package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw chat}:terminal UI({@code tui --local} ).
 * <p>
 * openclaw ({@code src/cli/tui-cli.ts}),{@code chat} Commander {@code .alias("chat")}
 * {@code tui} ; {@code openclaw chat} {@code --local} .
 * {@link TuiOptions} .
 * </p>
 * <p>
 * :{@code --local} {@code --url},{@code --token},{@code --password} .
 * </p>
 *
 * @see TuiOptions
 * @see <a href="https://docs.openclaw.ai/cli/chat">chat CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class ChatOptions implements CliSubArgs {

 /** {@code --local}:embedding agent (chat when true). */
    private final boolean local;
    /** {@code --url}：Gateway WebSocket URL。 */
    private final String url;
 /** {@code --token}:Gateway token. */
    private final String token;
 /** {@code --password}:Gateway . */
    private final String password;
 /** {@code --session}:sessionkey( {@code main},{@code scope=global} {@code global}). */
    private final String session;
 /** {@code --deliver}:. */
    private final boolean deliver;
 /** {@code --thinking}:. */
    private final String thinking;
 /** {@code --message}:connectionmessage. */
    private final String message;
 /** {@code --timeout-ms}:Agent timeoutmilliseconds. */
    private final Integer timeoutMs;
 /** {@code --history-limit}:( {@code 200}). */
    private final Integer historyLimit;

    private ChatOptions(Builder b) {
        this.local = b.local;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.session = b.session;
        this.deliver = b.deliver;
        this.thinking = b.thinking;
        this.message = b.message;
        this.timeoutMs = b.timeoutMs;
        this.historyLimit = b.historyLimit;
    }

    /**
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        OpenClawCliArgv.addFlag(out, "--local", local);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addFlag(out, "--deliver", deliver);
        OpenClawCliArgv.addIfPresent(out, "--thinking", thinking);
        OpenClawCliArgv.addIfPresent(out, "--message", message);
        OpenClawCliArgv.addIfNotNull(out, "--timeout-ms", timeoutMs);
        OpenClawCliArgv.addIfNotNull(out, "--history-limit", historyLimit);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ChatOptions} builder.
     */
    public static final class Builder {
        private boolean local;
        private String url;
        private String token;
        private String password;
        private String session;
        private boolean deliver;
        private String thinking;
        private String message;
        private Integer timeoutMs;
        private Integer historyLimit;

 /** {@code --local}:embedding agent (chat when true). */
        public Builder local(boolean local) { this.local = local; return this; }
        /** {@code --url}：Gateway WebSocket URL。 */
        public Builder url(String url) { this.url = url; return this; }
 /** {@code --token}:Gateway token. */
        public Builder token(String token) { this.token = token; return this; }
 /** {@code --password}:Gateway . */
        public Builder password(String password) { this.password = password; return this; }
 /** {@code --session}:sessionkey. */
        public Builder session(String session) { this.session = session; return this; }
 /** {@code --deliver}:. */
        public Builder deliver(boolean deliver) { this.deliver = deliver; return this; }
 /** {@code --thinking}:. */
        public Builder thinking(String thinking) { this.thinking = thinking; return this; }
 /** {@code --message}:connectionmessage. */
        public Builder message(String message) { this.message = message; return this; }
 /** {@code --timeout-ms}:Agent timeoutmilliseconds. */
        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
 /** {@code --history-limit}:. */
        public Builder historyLimit(Integer historyLimit) { this.historyLimit = historyLimit; return this; }

        /**
 * @return {@link ChatOptions}
         */
        public ChatOptions build() {
            return new ChatOptions(this);
        }
    }
}
