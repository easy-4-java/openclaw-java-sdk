package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code openclaw daemon}:<strong></strong>, {@code openclaw gateway} subcommand(status/install/start/stop/restart/uninstall).
 * <p> {@code gateway} documentation"Manage the Gateway service"; {@link GatewayCommandOptions} gateway CLI.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/daemon">daemon CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class DaemonOptions implements CliSubArgs {

    /**
 * documentationsubcommand.
     */
    public enum Subcommand {
        STATUS("status"),
        INSTALL("install"),
        UNINSTALL("uninstall"),
        START("start"),
        STOP("stop"),
        RESTART("restart");

 /** CLI subcommand. */
        private final String cliName;

        /**
 * @param cliName null, openclaw
         */
        Subcommand(String cliName) {
            this.cliName = cliName;
        }

        /**
 * @return subcommand token( {@code "status"})
         */
        public String cliName() {
            return cliName;
        }
    }

    /**
 * subcommand:documentation {@code status|install|uninstall|start|stop|restart}.
     */
    private final Subcommand subcommand;
    /**
 * only {@link Subcommand#STATUS}: RPC ({@code --url},{@code --token} ,See {@link GatewayRpcOptions}).
     */
    private final GatewayRpcOptions statusRpc;
    /**
 * only {@link Subcommand#STATUS}:{@code --no-probe},{@code --deep},{@code --require-rpc}( gateway status documentation).
     */
    private final GatewayCliArgv.GatewayStatusOptions statusExtra;
    /**
 * only {@link Subcommand#INSTALL}:{@code --port} WebSocket .
     */
    private final String installPort;
    /**
 * only {@link Subcommand#INSTALL}:{@code --runtime} Node/Bun .
     */
    private final String installRuntime;
    /**
 * only {@link Subcommand#INSTALL}:{@code --token} token(SecretRef See gateway documentation).
     */
    private final String installToken;
    /**
 * only {@link Subcommand#INSTALL}:{@code --force} process.
     */
    private final boolean installForce;
    /**
 * {@code --json}:subcommand(documentation:lifecycle).
     */
    private final boolean json;

    /**
 * @param b builder
     */
    private DaemonOptions(Builder b) {
        this.subcommand = Objects.requireNonNull(b.subcommand, "subcommand");
        this.statusRpc = b.statusRpc;
        this.statusExtra = b.statusExtra;
        this.installPort = b.installPort;
        this.installRuntime = b.installRuntime;
        this.installToken = b.installToken;
        this.installForce = b.installForce;
        this.json = b.json;
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
        out.add(subcommand.cliName());
        switch (subcommand) {
            case STATUS:
                if (statusRpc != null) {
                    statusRpc.appendSharedFlags(out);
                }
                GatewayCliArgv.GatewayStatusOptions se = statusExtra != null
                        ? statusExtra
                        : GatewayCliArgv.GatewayStatusOptions.none();
                if (se.isNoProbe()) {
                    out.add("--no-probe");
                }
                if (se.isDeep()) {
                    out.add("--deep");
                }
                if (se.isRequireRpc()) {
                    out.add("--require-rpc");
                }
                break;
            case INSTALL:
                if (installPort != null && !installPort.isEmpty()) {
                    out.add("--port");
                    out.add(installPort);
                }
                if (installRuntime != null && !installRuntime.isEmpty()) {
                    out.add("--runtime");
                    out.add(installRuntime);
                }
                if (installToken != null && !installToken.isEmpty()) {
                    out.add("--token");
                    out.add(installToken);
                }
                if (installForce) {
                    out.add("--force");
                }
                break;
            case START:
            case STOP:
            case RESTART:
            case UNINSTALL:
                break;
            default:
                break;
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link DaemonOptions} builder.
     */
    public static final class Builder {

        private Subcommand subcommand;
        private GatewayRpcOptions statusRpc;
        private GatewayCliArgv.GatewayStatusOptions statusExtra;
        private String installPort;
        private String installRuntime;
        private String installToken;
        private boolean installForce;
        private boolean json;

        /**
 * @param subcommand null
         * @return {@code this}
         */
        public Builder subcommand(Subcommand subcommand) {
            this.subcommand = subcommand;
            return this;
        }

 /** only {@link Subcommand#STATUS}: RPC . */
        public Builder statusRpc(GatewayRpcOptions statusRpc) {
            this.statusRpc = statusRpc;
            return this;
        }

 /** only {@link Subcommand#STATUS}:{@code --no-probe} / {@code --deep} / {@code --require-rpc}. */
        public Builder statusExtra(GatewayCliArgv.GatewayStatusOptions statusExtra) {
            this.statusExtra = statusExtra;
            return this;
        }

        /**
         * @param installPort {@code daemon install --port}
         * @return {@code this}
         */
        public Builder installPort(String installPort) {
            this.installPort = installPort;
            return this;
        }

        /**
         * @param installRuntime {@code --runtime}
         * @return {@code this}
         */
        public Builder installRuntime(String installRuntime) {
            this.installRuntime = installRuntime;
            return this;
        }

        /**
 * @param installToken {@code --token}(install )
         * @return {@code this}
         */
        public Builder installToken(String installToken) {
            this.installToken = installToken;
            return this;
        }

        /**
         * @param installForce {@code --force}
         * @return {@code this}
         */
        public Builder installForce(boolean installForce) {
            this.installForce = installForce;
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
 * @return {@link DaemonOptions}
         */
        public DaemonOptions build() {
            return new DaemonOptions(this);
        }
    }
}
