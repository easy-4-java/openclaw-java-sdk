package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw devices}:approval/devicepairing,device token.
 * <p> {@code --url} Provides {@code --token} {@code --password},CLI .
 * {@code operator.pairing} {@code operator.admin} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/devices">devices CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class DevicesOptions implements CliSubArgs {

    /**
 * devices subcommand:,,approval,device token.
     */
    public enum Verb {
 /** {@code devices list}:pairingpairingdevice. */
        LIST,
 /** {@code devices remove}:pairing. */
        REMOVE,
 /** {@code devices clear}:( {@code --yes}). */
        CLEAR,
 /** {@code devices approve}:pairing( id ). */
        APPROVE,
 /** {@code devices reject}:. */
        REJECT,
 /** {@code devices rotate}:device token( scope ). */
        ROTATE,
 /** {@code devices revoke}:device token. */
        REVOKE
    }

 /** list / remove / clear / approve / reject / rotate / revoke . */
    private final Verb verb;
    /**
 * remove / rotate / revoke:device id({@code --device} , Builder ).
     */
    private final String deviceId;
    /**
 * clear:{@code --yes} Required,.
     */
    private final boolean clearYes;
    /**
 * clear:{@code --pending} .
     */
    private final boolean clearPending;
    /**
 * approve / reject:pairing id;approve {@code approveLatest} ComposesSeedocumentation.
     */
    private final String requestId;
    /**
 * approve:{@code --latest} .
     */
    private final boolean approveLatest;
    /**
 * rotate / revoke:(device).
     */
    private final String role;
    /**
 * rotate: {@code --scope} operator scope;.
     */
    private final List<String> scopes;
    /**
 * :{@code --url} Gateway WebSocket( gateway " url ").
     */
    private final String url;
    /**
 * :{@code --token} Gateway token.
     */
    private final String token;
    /**
 * :{@code --password} Gatewayauthentication.
     */
    private final String password;
    /**
 * :{@code --timeout} RPC timeout.
     */
    private final String timeout;
    /**
 * :{@code --json} .
     */
    private final boolean json;
    /**
 * argv.
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private DevicesOptions(Builder b) {
        this.verb = b.verb;
        this.deviceId = b.deviceId;
        this.clearYes = b.clearYes;
        this.clearPending = b.clearPending;
        this.requestId = b.requestId;
        this.approveLatest = b.approveLatest;
        this.role = b.role;
        this.scopes = b.scopes == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.scopes);
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
                break;
            case REMOVE:
                out.add("remove");
                if (deviceId != null && OpenClawStrings.isNotBlank(deviceId)) {
                    out.add(deviceId.trim());
                }
                break;
            case CLEAR:
                out.add("clear");
                OpenClawCliArgv.addFlag(out, "--yes", clearYes);
                OpenClawCliArgv.addFlag(out, "--pending", clearPending);
                break;
            case APPROVE:
                out.add("approve");
                if (approveLatest) {
                    out.add("--latest");
                } else if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case REJECT:
                out.add("reject");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case ROTATE:
                out.add("rotate");
                OpenClawCliArgv.addIfPresent(out, "--device", deviceId);
                OpenClawCliArgv.addIfPresent(out, "--role", role);
                for (String s : scopes) {
                    if (s != null && OpenClawStrings.isNotBlank(s)) {
                        out.add("--scope");
                        out.add(s.trim());
                    }
                }
                break;
            case REVOKE:
                out.add("revoke");
                OpenClawCliArgv.addIfPresent(out, "--device", deviceId);
                OpenClawCliArgv.addIfPresent(out, "--role", role);
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
 * {@link DevicesOptions} builder.
     */
    public static final class Builder {
        private Verb verb = Verb.LIST;
        private String deviceId;
        private boolean clearYes;
        private boolean clearPending;
        private String requestId;
        private boolean approveLatest;
        private String role;
        private List<String> scopes = new ArrayList<>();
        private String url;
        private String token;
        private String password;
        private String timeout;
        private boolean json;
        private List<String> extra = new ArrayList<>();

        /**
         * @return {@code this}（{@code devices list}）
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
 * @param deviceId device ID
         * @return {@code this}
         */
        public Builder remove(String deviceId) {
            this.verb = Verb.REMOVE;
            this.deviceId = deviceId;
            return this;
        }

        /**
         * @param yes clear：{@code --yes}
         * @return {@code this}
         */
        public Builder clear(boolean yes) {
            this.verb = Verb.CLEAR;
            this.clearYes = yes;
            return this;
        }

        /**
         * @param pending clear：{@code --pending}
         * @return {@code this}
         */
        public Builder clearPending(boolean pending) {
            this.clearPending = pending;
            return this;
        }

        /**
 * @return {@code this}( approve)
         */
        public Builder approve() {
            this.verb = Verb.APPROVE;
            this.requestId = null;
            this.approveLatest = false;
            return this;
        }

        /**
 * @param requestId ID
         * @return {@code this}
         */
        public Builder approve(String requestId) {
            this.verb = Verb.APPROVE;
            this.requestId = requestId;
            this.approveLatest = false;
            return this;
        }

        /**
         * @param latest {@code --latest}
         * @return {@code this}
         */
        public Builder approveLatest(boolean latest) {
            this.approveLatest = latest;
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
         * @param deviceId {@code --device}
         * @param role {@code --role}
         * @return {@code this}
         */
        public Builder rotate(String deviceId, String role) {
            this.verb = Verb.ROTATE;
            this.deviceId = deviceId;
            this.role = role;
            return this;
        }

        /**
 * @param scope rotate: {@code --scope}
         * @return {@code this}
         */
        public Builder scope(String scope) {
            if (scope != null && OpenClawStrings.isNotBlank(scope)) {
                scopes.add(scope.trim());
            }
            return this;
        }

        /**
         * @param deviceId {@code --device}
         * @param role {@code --role}
         * @return {@code this}
         */
        public Builder revoke(String deviceId, String role) {
            this.verb = Verb.REVOKE;
            this.deviceId = deviceId;
            this.role = role;
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
 * @return {@link DevicesOptions}
         */
        public DevicesOptions build() {
            return new DevicesOptions(this);
        }
    }
}
