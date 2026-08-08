package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw onboard}: Gateway / .
 * <p> flag {@link Builder} ; {@link Builder#extra(String...)} documentation.
 * {@code --json} ; {@code --non-interactive}.Gateway token SecretRef See onboard documentation.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/onboard">onboard CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class OnboardOptions implements CliSubArgs {

    /**
 * shell onboard subcommand argv ;{@link #toSubcommandArguments} .
     */
    private final List<String> segments;

    /**
 * @param segments null;,
     */
    private OnboardOptions(List<String> segments) {
        this.segments = segments;
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
        return segments;
    }

    /**
 * {@link OnboardOptions} builder:consistent with officialdocumentation flag.
     */
    public static final class Builder {

 /** ,. */
        private final List<String> s = new ArrayList<>();

        /**
         * @param flow {@code --flow}
         * @return {@code this}
         */
        public Builder flow(String flow) {
            if (flow != null && !flow.isEmpty()) {
                s.add("--flow");
                s.add(flow);
            }
            return this;
        }

        /**
         * @param mode {@code --mode}
         * @return {@code this}
         */
        public Builder mode(String mode) {
            if (mode != null && !mode.isEmpty()) {
                s.add("--mode");
                s.add(mode);
            }
            return this;
        }

        /**
         * @param remoteUrl {@code --remote-url}
         * @return {@code this}
         */
        public Builder remoteUrl(String remoteUrl) {
            if (remoteUrl != null && !remoteUrl.isEmpty()) {
                s.add("--remote-url");
                s.add(remoteUrl);
            }
            return this;
        }

        /**
 * @param nonInteractive When true, {@code --non-interactive}
         * @return {@code this}
         */
        public Builder nonInteractive(boolean nonInteractive) {
            if (nonInteractive) {
                s.add("--non-interactive");
            }
            return this;
        }

        /**
 * @param json When true, {@code --json}
         * @return {@code this}
         */
        public Builder json(boolean json) {
            if (json) {
                s.add("--json");
            }
            return this;
        }

        /**
         * @param authChoice {@code --auth-choice}
         * @return {@code this}
         */
        public Builder authChoice(String authChoice) {
            if (authChoice != null && !authChoice.isEmpty()) {
                s.add("--auth-choice");
                s.add(authChoice);
            }
            return this;
        }

        /**
         * @param customBaseUrl {@code --custom-base-url}
         * @return {@code this}
         */
        public Builder customBaseUrl(String customBaseUrl) {
            if (customBaseUrl != null && !customBaseUrl.isEmpty()) {
                s.add("--custom-base-url");
                s.add(customBaseUrl);
            }
            return this;
        }

        /**
         * @param customModelId {@code --custom-model-id}
         * @return {@code this}
         */
        public Builder customModelId(String customModelId) {
            if (customModelId != null && !customModelId.isEmpty()) {
                s.add("--custom-model-id");
                s.add(customModelId);
            }
            return this;
        }

        /**
         * @param secretInputMode {@code --secret-input-mode}
         * @return {@code this}
         */
        public Builder secretInputMode(String secretInputMode) {
            if (secretInputMode != null && !secretInputMode.isEmpty()) {
                s.add("--secret-input-mode");
                s.add(secretInputMode);
            }
            return this;
        }

        /**
 * @param acceptRisk When true, {@code --accept-risk}
         * @return {@code this}
         */
        public Builder acceptRisk(boolean acceptRisk) {
            if (acceptRisk) {
                s.add("--accept-risk");
            }
            return this;
        }

        /**
         * @param gatewayAuth {@code --gateway-auth}
         * @return {@code this}
         */
        public Builder gatewayAuth(String gatewayAuth) {
            if (gatewayAuth != null && !gatewayAuth.isEmpty()) {
                s.add("--gateway-auth");
                s.add(gatewayAuth);
            }
            return this;
        }

        /**
         * @param gatewayToken {@code --gateway-token}
         * @return {@code this}
         */
        public Builder gatewayToken(String gatewayToken) {
            if (gatewayToken != null && !gatewayToken.isEmpty()) {
                s.add("--gateway-token");
                s.add(gatewayToken);
            }
            return this;
        }

        /**
 * @param envVar {@code --gateway-token-ref-env}
         * @return {@code this}
         */
        public Builder gatewayTokenRefEnv(String envVar) {
            if (envVar != null && !envVar.isEmpty()) {
                s.add("--gateway-token-ref-env");
                s.add(envVar);
            }
            return this;
        }

        /**
 * @param installDaemon When true, {@code --install-daemon}
         * @return {@code this}
         */
        public Builder installDaemon(boolean installDaemon) {
            if (installDaemon) {
                s.add("--install-daemon");
            }
            return this;
        }

        /**
 * @param skipHealth When true, {@code --skip-health}
         * @return {@code this}
         */
        public Builder skipHealth(boolean skipHealth) {
            if (skipHealth) {
                s.add("--skip-health");
            }
            return this;
        }

        /**
 * @param allowUnconfigured When true, {@code --allow-unconfigured}
         * @return {@code this}
         */
        public Builder allowUnconfigured(boolean allowUnconfigured) {
            if (allowUnconfigured) {
                s.add("--allow-unconfigured");
            }
            return this;
        }

        /**
 * documentation flag sub-arguments, shell .
         *
 * @param tokens null
         * @return {@code this}
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
 * @return {@link OnboardOptions}
         */
        public OnboardOptions build() {
            return new OnboardOptions(OpenClawLists.copyOf(s));
        }
    }
}
