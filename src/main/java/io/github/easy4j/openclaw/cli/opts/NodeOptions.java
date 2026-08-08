package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw node}: node host,connection Gateway WebSocket {@code system.run} / {@code system.which} .
 * <p>connection Gateway {@code role: node} pairing, {@code openclaw devices approve} .
 * {@code run} {@code install} Gatewayauthentication/remote , CLI {@code --token}(See node documentation Gateway auth ).</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/node">node CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class NodeOptions implements CliSubArgs {

    /**
 * node subcommand:,lifecycle.
     */
    public enum Verb {
 /** {@code node run}: node host. */
        RUN,
 /** {@code node install}:. */
        INSTALL,
 /** {@code node status}:. */
        STATUS,
 /** {@code node stop}:. */
        STOP,
 /** {@code node restart}:. */
        RESTART,
 /** {@code node uninstall}:. */
        UNINSTALL
    }

 /** run / install / . */
    private final Verb verb;
    /**
 * run / install:{@code --host} Gateway WebSocket ( loopback).
     */
    private final String host;
    /**
 * run / install:{@code --port} Gateway WebSocket ( 18789).
     */
    private final String port;
    /**
 * run / install:{@code --tls} TLS connectionGateway.
     */
    private final boolean tls;
    /**
 * run / install:{@code --tls-fingerprint} sha256 .
     */
    private final String tlsFingerprint;
    /**
 * run / install:{@code --node-id} node id(pairing token,Seedocumentation).
     */
    private final String nodeId;
    /**
 * run / install:{@code --display-name} node.
     */
    private final String displayName;
    /**
 * install:{@code --runtime} ({@code node} {@code bun}).
     */
    private final String runtime;
    /**
 * install:{@code --force} .
     */
    private final boolean force;
    /**
 * status / stop / restart / uninstall:{@code --json} .
     */
    private final boolean json;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private NodeOptions(Builder b) {
        this.verb = b.verb;
        this.host = b.host;
        this.port = b.port;
        this.tls = b.tls;
        this.tlsFingerprint = b.tlsFingerprint;
        this.nodeId = b.nodeId;
        this.displayName = b.displayName;
        this.runtime = b.runtime;
        this.force = b.force;
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
        switch (verb) {
            case RUN:
                out.add("run");
                break;
            case INSTALL:
                out.add("install");
                break;
            case STATUS:
                out.add("status");
                break;
            case STOP:
                out.add("stop");
                break;
            case RESTART:
                out.add("restart");
                break;
            case UNINSTALL:
                out.add("uninstall");
                break;
            default:
                break;
        }
        if (verb == Verb.RUN || verb == Verb.INSTALL) {
            OpenClawCliArgv.addIfPresent(out, "--host", host);
            OpenClawCliArgv.addIfPresent(out, "--port", port);
            OpenClawCliArgv.addFlag(out, "--tls", tls);
            OpenClawCliArgv.addIfPresent(out, "--tls-fingerprint", tlsFingerprint);
            OpenClawCliArgv.addIfPresent(out, "--node-id", nodeId);
            OpenClawCliArgv.addIfPresent(out, "--display-name", displayName);
            if (verb == Verb.INSTALL) {
                OpenClawCliArgv.addIfPresent(out, "--runtime", runtime);
                OpenClawCliArgv.addFlag(out, "--force", force);
            }
        }
        if (verb == Verb.STATUS || verb == Verb.STOP || verb == Verb.RESTART || verb == Verb.UNINSTALL) {
            OpenClawCliArgv.addFlag(out, "--json", json);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link NodeOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.RUN;
        private String host;
        private String port;
        private boolean tls;
        private String tlsFingerprint;
        private String nodeId;
        private String displayName;
        private String runtime;
        private boolean force;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code node run}）
         */
        public Builder run() {
            this.verb = Verb.RUN;
            return this;
        }

        /**
         * @return {@code this}（{@code node install}）
         */
        public Builder install() {
            this.verb = Verb.INSTALL;
            return this;
        }

        /**
         * @return {@code this}（{@code node status}）
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * @return {@code this}（{@code node stop}）
         */
        public Builder stop() {
            this.verb = Verb.STOP;
            return this;
        }

        /**
         * @return {@code this}（{@code node restart}）
         */
        public Builder restart() {
            this.verb = Verb.RESTART;
            return this;
        }

        /**
         * @return {@code this}（{@code node uninstall}）
         */
        public Builder uninstall() {
            this.verb = Verb.UNINSTALL;
            return this;
        }

        /**
         * @param host {@code --host}
         * @return {@code this}
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * @param port {@code --port}
         * @return {@code this}
         */
        public Builder port(String port) {
            this.port = port;
            return this;
        }

        /**
         * @param tls {@code --tls}
         * @return {@code this}
         */
        public Builder tls(boolean tls) {
            this.tls = tls;
            return this;
        }

        /**
         * @param fingerprint {@code --tls-fingerprint}
         * @return {@code this}
         */
        public Builder tlsFingerprint(String fingerprint) {
            this.tlsFingerprint = fingerprint;
            return this;
        }

        /**
         * @param nodeId {@code --node-id}
         * @return {@code this}
         */
        public Builder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        /**
         * @param displayName {@code --display-name}
         * @return {@code this}
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * @param runtime install：{@code --runtime}
         * @return {@code this}
         */
        public Builder runtime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        /**
         * @param force install：{@code --force}
         * @return {@code this}
         */
        public Builder force(boolean force) {
            this.force = force;
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
 * @return {@link NodeOptions}
         */
        public NodeOptions build() {
            return new NodeOptions(this);
        }
    }
}
