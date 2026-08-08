package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code openclaw gateway} subcommand;RPC {@link #health} / {@link #status} / {@link #probe},
 * subcommand( {@code run},{@code call}) {@link #add(String...)} documentation.
 * <p>Gateway OpenClaw WebSocket (node,session,hooks);subcommand {@code run},lifecycle,
 * {@code discover},{@code call} RPC ,See gateway CLI documentation.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class GatewayCommandOptions implements CliSubArgs {

    /**
 * {@code gateway} subcommand token (executable name),consistent with official CLI .
     */
    private final List<String> segments;

    /**
 * @param segments null,
     */
    private GatewayCommandOptions(List<String> segments) {
        this.segments = OpenClawLists.copyOf(segments);
    }

    /**
 * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
 * subcommand token:Corresponds to {@code openclaw gateway}( CLI Equivalent to,Seedocumentation Run the Gateway ).
     */
    public static GatewayCommandOptions empty() {
        return new GatewayCommandOptions(OpenClawLists.empty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> toSubcommandArguments() {
        return segments;
    }

    /**
 * {@link GatewayCommandOptions} builder:subcommand.
     */
    public static final class Builder {

 /** token . */
        private final List<String> s = new ArrayList<>();

        /**
 * CLI token( {@code run},{@code call} ).
         *
 * @param tokens null
         * @return {@code this}
         */
        public Builder add(String... tokens) {
            if (tokens != null) {
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
 * {@code gateway health ...} .
         *
 * @param rpc null
         * @return {@code this}
         */
        public Builder health(GatewayRpcOptions rpc) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.health(rpc));
            return this;
        }

        /**
 * {@code gateway status ...} .
         *
 * @param rpc null
 * @param extra null
         * @return {@code this}
         */
        public Builder status(GatewayRpcOptions rpc, GatewayCliArgv.GatewayStatusOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.status(rpc, extra));
            return this;
        }

        /**
 * {@code gateway probe ...} .
         *
 * @param rpc null
 * @param extra null
         * @return {@code this}
         */
        public Builder probe(GatewayRpcOptions rpc, GatewayCliArgv.GatewayProbeOptions extra) {
            Objects.requireNonNull(rpc, "rpc");
            s.addAll(GatewayCliArgv.probe(rpc, extra));
            return this;
        }

        /**
 * @return {@link GatewayCommandOptions}
         */
        public GatewayCommandOptions build() {
            return new GatewayCommandOptions(OpenClawLists.copyOf(s));
        }
    }
}
