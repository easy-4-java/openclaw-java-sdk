package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw nodes}:pairing node host(approval) {@code invoke} .
 * <p>{@code system.run} shell exec {@code host=node};{@code nodes invoke} , RPC.
 * {@code --url},{@code --token},{@code --password},{@code --timeout},{@code --json} devices documentationGateway.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/nodes">nodes CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class NodesOptions implements CliSubArgs {

    /**
 * nodes subcommand:,pairing, invoke.
     */
    public enum Verb {
 /** {@code nodes list}:pairingpairing, {@code --connected} {@code --last-connected} . */
        LIST,
 /** {@code nodes pending}:onlyapproval( pairing scope). */
        PENDING,
 /** {@code nodes approve}:( scope ). */
        APPROVE,
 /** {@code nodes reject}:. */
        REJECT,
 /** {@code nodes rename}:. */
        RENAME,
 /** {@code nodes status}: list . */
        STATUS,
 /** {@code nodes invoke}:node command JSON params. */
        INVOKE
    }

 /** nodes subcommand. */
    private final Verb verb;
    /**
 * list / status:{@code --connected} node.
     */
    private final boolean listConnected;
    /**
 * list / status:{@code --last-connected} node( {@code 24h}).
     */
    private final String lastConnected;
    /**
 * approve / reject:pairing id.
     */
    private final String requestId;
    /**
 * rename / invoke:{@code --node} (id, IP,Seedocumentation).
     */
    private final String nodeRef;
    /**
 * rename:{@code --name} .
     */
    private final String name;
    /**
 * invoke:{@code --command} .
     */
    private final String command;
    /**
 * invoke:{@code --params} JSON objectcharacters, {@code {}}.
     */
    private final String paramsJson;
    /**
 * invoke:{@code --invoke-timeout} timeoutmilliseconds( 15000).
     */
    private final String invokeTimeout;
    /**
 * invoke:{@code --idempotency-key} Optionalkey.
     */
    private final String idempotencyKey;
    /**
 * :{@code --url} Gateway WebSocket.
     */
    private final String url;
    /**
 * :{@code --token}.
     */
    private final String token;
    /**
 * :{@code --password}.
     */
    private final String password;
    /**
 * :{@code --timeout} RPC .
     */
    private final String timeout;
    /**
 * :{@code --json}.
     */
    private final boolean json;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private NodesOptions(Builder b) {
        this.verb = b.verb;
        this.listConnected = b.listConnected;
        this.lastConnected = b.lastConnected;
        this.requestId = b.requestId;
        this.nodeRef = b.nodeRef;
        this.name = b.name;
        this.command = b.command;
        this.paramsJson = b.paramsJson;
        this.invokeTimeout = b.invokeTimeout;
        this.idempotencyKey = b.idempotencyKey;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
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
            case LIST:
                out.add("list");
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case PENDING:
                out.add("pending");
                break;
            case APPROVE:
                out.add("approve");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case REJECT:
                out.add("reject");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case RENAME:
                out.add("rename");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--name", name);
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case INVOKE:
                out.add("invoke");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--command", command);
                OpenClawCliArgv.addIfPresent(out, "--params", paramsJson);
                OpenClawCliArgv.addIfPresent(out, "--invoke-timeout", invokeTimeout);
                OpenClawCliArgv.addIfPresent(out, "--idempotency-key", idempotencyKey);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link NodesOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.LIST;
        private boolean listConnected;
        private String lastConnected;
        private String requestId;
        private String nodeRef;
        private String name;
        private String command;
        private String paramsJson;
        private String invokeTimeout;
        private String idempotencyKey;
        private String url;
        private String token;
        private String password;
        private String timeout;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code nodes list}）
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * @param connected list：{@code --connected}
         * @return {@code this}
         */
        public Builder listConnected(boolean connected) {
            this.listConnected = connected;
            return this;
        }

        /**
         * @param duration list：{@code --last-connected}
         * @return {@code this}
         */
        public Builder lastConnected(String duration) {
            this.lastConnected = duration;
            return this;
        }

        /**
         * @return {@code this}（{@code nodes pending}）
         */
        public Builder pending() {
            this.verb = Verb.PENDING;
            return this;
        }

        /**
 * @param requestId ID
         * @return {@code this}
         */
        public Builder approve(String requestId) {
            this.verb = Verb.APPROVE;
            this.requestId = requestId;
            return this;
        }

        /**
 * @param requestId ID
         * @return {@code this}
         */
        public Builder reject(String requestId) {
            this.verb = Verb.REJECT;
            this.requestId = requestId;
            return this;
        }

        /**
         * @param nodeRef {@code --node}
         * @param displayName {@code --name}
         * @return {@code this}
         */
        public Builder rename(String nodeRef, String displayName) {
            this.verb = Verb.RENAME;
            this.nodeRef = nodeRef;
            this.name = displayName;
            return this;
        }

        /**
         * @return {@code this}（{@code nodes status}）
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * @param nodeRef {@code --node}
         * @param command {@code --command}
         * @return {@code this}
         */
        public Builder invoke(String nodeRef, String command) {
            this.verb = Verb.INVOKE;
            this.nodeRef = nodeRef;
            this.command = command;
            return this;
        }

        /**
         * @param json {@code --params}
         * @return {@code this}
         */
        public Builder paramsJson(String json) {
            this.paramsJson = json;
            return this;
        }

        /**
         * @param ms invoke：{@code --invoke-timeout}
         * @return {@code this}
         */
        public Builder invokeTimeout(String ms) {
            this.invokeTimeout = ms;
            return this;
        }

        /**
         * @param key {@code --idempotency-key}
         * @return {@code this}
         */
        public Builder idempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

        /**
         * @param url {@code --url}
         * @return {@code this}
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * @param token {@code --token}
         * @return {@code this}
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * @param password {@code --password}
         * @return {@code this}
         */
        public Builder password(String password) {
            this.password = password;
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
 * @return {@link NodesOptions}
         */
        public NodesOptions build() {
            return new NodesOptions(this);
        }
    }
}
