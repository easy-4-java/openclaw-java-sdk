package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw terminal}:terminal UI({@code tui --local} ).
 * <p>
 * openclaw ({@code src/cli/tui-cli.ts}),{@code terminal} Commander
 * {@code .alias("terminal")} {@code tui} ; {@code openclaw terminal}
 * {@code --local} . {@link TuiOptions} / {@link ChatOptions} .
 * </p>
 * <p>
 * {@link ChatOptions} field,only/.
 * </p>
 *
 * @see ChatOptions
 * @see TuiOptions
 * @see <a href="https://docs.openclaw.ai/cli/terminal">terminal CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class TerminalOptions implements CliSubArgs {

 /** Delegates to {@link ChatOptions}, argv . */
    private final ChatOptions delegate;

    private TerminalOptions(ChatOptions delegate) {
        this.delegate = delegate;
    }

    /**
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        return delegate.toSubcommandArguments();
    }

    /**
 * {@link TerminalOptions} builder: {@link ChatOptions.Builder} .
     */
    public static final class Builder {
        private final ChatOptions.Builder b = ChatOptions.builder();

 /** {@code --local}:embedding agent (terminal when true). */
        public Builder local(boolean local) { b.local(local); return this; }
        /** {@code --url}：Gateway WebSocket URL。 */
        public Builder url(String url) { b.url(url); return this; }
 /** {@code --token}:Gateway token. */
        public Builder token(String token) { b.token(token); return this; }
 /** {@code --password}:Gateway . */
        public Builder password(String password) { b.password(password); return this; }
 /** {@code --session}:sessionkey. */
        public Builder session(String session) { b.session(session); return this; }
 /** {@code --deliver}:. */
        public Builder deliver(boolean deliver) { b.deliver(deliver); return this; }
 /** {@code --thinking}:. */
        public Builder thinking(String thinking) { b.thinking(thinking); return this; }
 /** {@code --message}:connectionmessage. */
        public Builder message(String message) { b.message(message); return this; }
 /** {@code --timeout-ms}:Agent timeoutmilliseconds. */
        public Builder timeoutMs(Integer timeoutMs) { b.timeoutMs(timeoutMs); return this; }
 /** {@code --history-limit}:. */
        public Builder historyLimit(Integer historyLimit) { b.historyLimit(historyLimit); return this; }

        /**
 * @return {@link TerminalOptions}
         */
        public TerminalOptions build() {
            return new TerminalOptions(b.build());
        }
    }
}
