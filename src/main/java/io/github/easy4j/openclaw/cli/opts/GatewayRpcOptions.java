package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * consistent with officialdocumentation"Query a running Gateway" WebSocket RPC ,
 * {@code gateway health|status|probe},{@code openclaw logs} .
 * <p>documentation: {@code --url} ,CLI ,
 * {@code --token} {@code --password};.{@code --timeout} timeout/(Seemilliseconds).</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class GatewayRpcOptions {

    /**
 * {@code --url}: Gateway WebSocket URL( {@code ws://127.0.0.1:18789}).
     */
    private final String url;
    /**
 * {@code --token}:Gateway token; {@code --url} Provides(documentation/env).
     */
    private final String token;
    /**
 * {@code --password}:Gateway authentication; token mutually exclusive, token Used forconnection(SeeGatewayauthenticationdocumentation).
     */
    private final String password;
    /**
 * {@code --timeout}:timeoutcharacters(documentation timeout/budget,subcommand, status 10000ms).
     */
    private final String timeout;
    /**
 * {@code --expect-final}: Gateway agent ,""not onlystreamingevent.
     */
    private final boolean expectFinal;
    /**
 * {@code --json}: JSON ( spinner ).
     */
    private final boolean json;

    /**
 * {@link Builder} ; {@link #builder}.
     *
 * @param b null builder
     */
    private GatewayRpcOptions(Builder b) {
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.expectFinal = b.expectFinal;
        this.json = b.json;
    }

    /**
 * @return {@code --url} value, null
     */
    public String getUrl() {
        return url;
    }

    /**
 * @return {@code --token} value, null
     */
    public String getToken() {
        return token;
    }

    /**
 * @return {@code --password} value, null
     */
    public String getPassword() {
        return password;
    }

    /**
 * @return {@code --timeout} characters, null
     */
    public String getTimeout() {
        return timeout;
    }

    /**
 * @return Whether to enable {@code --expect-final}
     */
    public boolean isExpectFinal() {
        return expectFinal;
    }

    /**
 * @return Whether to enable {@code --json}
     */
    public boolean isJson() {
        return json;
    }

    /**
 * @return Used for {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
 * argument list(subcommand {@code health|status|probe}).
     */
    public void appendSharedFlags(List<String> args) {
        Objects.requireNonNull(args, "args");
        if (url != null && !url.isEmpty()) {
            args.add("--url");
            args.add(url);
        }
        if (token != null && !token.isEmpty()) {
            args.add("--token");
            args.add(token);
        }
        if (password != null && !password.isEmpty()) {
            args.add("--password");
            args.add(password);
        }
        if (timeout != null && !timeout.isEmpty()) {
            args.add("--timeout");
            args.add(timeout);
        }
        if (expectFinal) {
            args.add("--expect-final");
        }
        if (json) {
            args.add("--json");
        }
    }

    /**
 * onlyobjectargument fragment(subcommand).
     */
    public List<String> toFlagList() {
        List<String> args = new ArrayList<>();
        appendSharedFlags(args);
        return args;
    }

    /**
 * {@link GatewayRpcOptions} builder;field CLI Corresponds to.
     */
    public static final class Builder {

        private String url;
        private String token;
        private String password;
        private String timeout;
        private boolean expectFinal;
        private boolean json;

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

 /** timeout/characters, CLI (millisecondsdocumentation). */
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
 * @return {@link GatewayRpcOptions}
         */
        public GatewayRpcOptions build() {
            return new GatewayRpcOptions(this);
        }
    }
}
