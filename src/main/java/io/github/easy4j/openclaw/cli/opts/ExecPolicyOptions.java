package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw exec-policy}: exec policy approval.
 * <p>
 * {@code show},{@code preset <name>},{@code set} subcommand.{@code set} subcommand
 * {@code --host}/{@code --security}/{@code --ask}/{@code --ask-fallback} .
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/exec-policy">exec-policy CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ExecPolicyOptions implements CliSubArgs {

 /** subcommand. */
    public enum Mode {
 /** {@code show}: exec policy. */
        SHOW,
 /** {@code preset <name>}:(yolo/cautious/deny-all). */
        PRESET,
 /** {@code set}: exec policy field. */
        SET
    }

    /** SHOW / PRESET / SET。 */
    private final Mode mode;
 /** preset:(yolo/cautious/deny-all). */
    private final String presetName;
 /** set:{@code --host} exec host (auto/sandbox/gateway/node). */
    private final String host;
 /** set:{@code --security} exec security (deny/allowlist/full). */
    private final String security;
 /** set:{@code --ask} exec ask (off/on-miss/always). */
    private final String ask;
 /** set:{@code --ask-fallback} approval(deny/allowlist/full). */
    private final String askFallback;
 /** {@code --json}:JSON . */
    private final boolean json;

    private ExecPolicyOptions(Builder b) {
        this.mode = b.mode;
        this.presetName = b.presetName;
        this.host = b.host;
        this.security = b.security;
        this.ask = b.ask;
        this.askFallback = b.askFallback;
        this.json = b.json;
    }

    /**
 * @return {@link Builder}( {@link Mode#SHOW})
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (mode) {
            case SHOW:
                out.add("show");
                break;
            case PRESET:
                out.add("preset");
                if (presetName != null && !presetName.isEmpty()) {
                    out.add(presetName);
                }
                break;
            case SET:
                out.add("set");
                break;
            default:
                // 未指定模式时不输出子命令 token（等价于父命令默认动作）
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--host", host);
        OpenClawCliArgv.addIfPresent(out, "--security", security);
        OpenClawCliArgv.addIfPresent(out, "--ask", ask);
        OpenClawCliArgv.addIfPresent(out, "--ask-fallback", askFallback);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ExecPolicyOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.SHOW;
        private String presetName;
        private String host;
        private String security;
        private String ask;
        private String askFallback;
        private boolean json;

 /** {@code show} subcommand. */
        public Builder show() { this.mode = Mode.SHOW; return this; }
 /** {@code preset <name>} subcommand. */
        public Builder preset(String name) { this.mode = Mode.PRESET; this.presetName = name; return this; }
 /** {@code set} subcommand. */
        public Builder set() { this.mode = Mode.SET; return this; }
 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** set:{@code --host} exec host (auto/sandbox/gateway/node). */
        public Builder host(String host) { this.host = host; return this; }
 /** set:{@code --security} exec security (deny/allowlist/full). */
        public Builder security(String security) { this.security = security; return this; }
 /** set:{@code --ask} exec ask (off/on-miss/always). */
        public Builder ask(String ask) { this.ask = ask; return this; }
 /** set:{@code --ask-fallback} approval(deny/allowlist/full). */
        public Builder askFallback(String askFallback) { this.askFallback = askFallback; return this; }
 /** {@code --json}:JSON . */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
 * @return {@link ExecPolicyOptions}
         */
        public ExecPolicyOptions build() {
            return new ExecPolicyOptions(this);
        }
    }
}
