package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code openclaw gateway &lt;subcommand&gt; ...} argument sequence(executable name {@code --dev} , {@link io.github.easy4j.openclaw.cli.OpenClawCliRequest} ).
 *
 * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class GatewayCliArgv {

    private GatewayCliArgv() {
    }

    /**
 * {@code gateway health} ,documentation:
     * {@code openclaw gateway health --url ws://127.0.0.1:18789}
     *
 * @param rpc null, RPC (URL,token )
 * @return argument list, {@code "health"}
     */
    public static List<String> health(GatewayRpcOptions rpc) {
        Objects.requireNonNull(rpc, "rpc");
        List<String> args = new ArrayList<>();
        args.add("health");
        rpc.appendSharedFlags(args);
        return Collections.unmodifiableList(args);
    }

    /**
 * {@code gateway status} ;See {@link GatewayCliArgv.GatewayStatusOptions}.
     *
 * @param rpc null
 * @param extra null,Equivalent to {@link GatewayStatusOptions#none}
 * @return argument list, {@code "status"}
     */
    public static List<String> status(GatewayRpcOptions rpc, GatewayStatusOptions extra) {
        Objects.requireNonNull(rpc, "rpc");
        GatewayStatusOptions e = extra != null ? extra : GatewayStatusOptions.none();
        List<String> args = new ArrayList<>();
        args.add("status");
        rpc.appendSharedFlags(args);
        if (e.isNoProbe()) {
            args.add("--no-probe");
        }
        if (e.isDeep()) {
            args.add("--deep");
        }
        if (e.isRequireRpc()) {
            args.add("--require-rpc");
        }
        return Collections.unmodifiableList(args);
    }

    /**
 * {@code gateway probe} ;SSH See {@link GatewayCliArgv.GatewayProbeOptions}.
     *
 * @param rpc null
 * @param extra null,Equivalent to {@link GatewayProbeOptions#none}
 * @return argument list, {@code "probe"}
     */
    public static List<String> probe(GatewayRpcOptions rpc, GatewayProbeOptions extra) {
        Objects.requireNonNull(rpc, "rpc");
        GatewayProbeOptions p = extra != null ? extra : GatewayProbeOptions.none();
        List<String> args = new ArrayList<>();
        args.add("probe");
        rpc.appendSharedFlags(args);
        if (p.getSsh() != null && !p.getSsh().isEmpty()) {
            args.add("--ssh");
            args.add(p.getSsh());
        }
        if (p.getSshIdentity() != null && !p.getSshIdentity().isEmpty()) {
            args.add("--ssh-identity");
            args.add(p.getSshIdentity());
        }
        if (p.isSshAuto()) {
            args.add("--ssh-auto");
        }
        return Collections.unmodifiableList(args);
    }

    /**
 * {@code gateway status} (documentation:{@code --no-probe},{@code --deep},{@code --require-rpc}).
     */
    public static final class GatewayStatusOptions {

        /**
 * {@code --no-probe}: Gateway (launchd/systemd ),skips WebSocket RPC .
         */
        private final boolean noProbe;
        /**
 * {@code --deep}:,system(documentation:).
         */
        private final boolean deep;
        /**
 * {@code --require-rpc}: RPC probe failed(:only RPC ).
         */
        private final boolean requireRpc;

        /**
         * @param noProbe   {@code --no-probe}
         * @param deep      {@code --deep}
         * @param requireRpc {@code --require-rpc}
         */
        private GatewayStatusOptions(boolean noProbe, boolean deep, boolean requireRpc) {
            this.noProbe = noProbe;
            this.deep = deep;
            this.requireRpc = requireRpc;
        }

        /**
 * @return when false
         */
        public static GatewayStatusOptions none() {
            return new GatewayStatusOptions(false, false, false);
        }

        /**
 * @return {@link Builder}
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
 * @return {@code --no-probe}
         */
        public boolean isNoProbe() {
            return noProbe;
        }

        /**
 * @return {@code --deep}
         */
        public boolean isDeep() {
            return deep;
        }

        /**
 * @return {@code --require-rpc}
         */
        public boolean isRequireRpc() {
            return requireRpc;
        }

        /**
 * {@link GatewayStatusOptions} builder.
         */
        public static final class Builder {

            private boolean noProbe;
            private boolean deep;
            private boolean requireRpc;

            /**
             * @param noProbe {@code --no-probe}
             * @return {@code this}
             */
            public Builder noProbe(boolean noProbe) {
                this.noProbe = noProbe;
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
             * @param requireRpc {@code --require-rpc}
             * @return {@code this}
             */
            public Builder requireRpc(boolean requireRpc) {
                this.requireRpc = requireRpc;
                return this;
            }

            /**
 * @return {@link GatewayStatusOptions}
             */
            public GatewayStatusOptions build() {
                return new GatewayStatusOptions(noProbe, deep, requireRpc);
            }
        }
    }

    /**
 * {@code gateway probe} SSH (documentation:{@code --ssh},{@code --ssh-identity},{@code --ssh-auto}).
     */
    public static final class GatewayProbeOptions {

        /**
 * {@code --ssh}: SSH only loopback Gateway(documentation Remote over SSH , {@code user@host}).
         */
        private final String ssh;
        /**
 * {@code --ssh-identity}:SSH .
         */
        private final String sshIdentity;
        /**
 * {@code --ssh-auto}: gateway host SSH (documentation:TXT ).
         */
        private final boolean sshAuto;

        /**
         * @param ssh         {@code --ssh}
         * @param sshIdentity {@code --ssh-identity}
         * @param sshAuto     {@code --ssh-auto}
         */
        private GatewayProbeOptions(String ssh, String sshIdentity, boolean sshAuto) {
            this.ssh = ssh;
            this.sshIdentity = sshIdentity;
            this.sshAuto = sshAuto;
        }

        /**
 * @return SSH default value
         */
        public static GatewayProbeOptions none() {
            return new GatewayProbeOptions(null, null, false);
        }

        /**
 * @return {@link Builder}
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
 * @return {@code --ssh}, null
         */
        public String getSsh() {
            return ssh;
        }

        /**
 * @return {@code --ssh-identity}, null
         */
        public String getSshIdentity() {
            return sshIdentity;
        }

        /**
 * @return {@code --ssh-auto}
         */
        public boolean isSshAuto() {
            return sshAuto;
        }

        /**
 * {@link GatewayProbeOptions} builder.
         */
        public static final class Builder {

            private String ssh;
            private String sshIdentity;
            private boolean sshAuto;

            /**
             * @param ssh {@code --ssh}
             * @return {@code this}
             */
            public Builder ssh(String ssh) {
                this.ssh = ssh;
                return this;
            }

            /**
             * @param sshIdentity {@code --ssh-identity}
             * @return {@code this}
             */
            public Builder sshIdentity(String sshIdentity) {
                this.sshIdentity = sshIdentity;
                return this;
            }

            /**
             * @param sshAuto {@code --ssh-auto}
             * @return {@code this}
             */
            public Builder sshAuto(boolean sshAuto) {
                this.sshAuto = sshAuto;
                return this;
            }

            /**
 * @return {@link GatewayProbeOptions}
             */
            public GatewayProbeOptions build() {
                return new GatewayProbeOptions(ssh, sshIdentity, sshAuto);
            }
        }
    }
}
