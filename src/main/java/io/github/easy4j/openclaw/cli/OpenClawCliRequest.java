package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.util.OpenClawLists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CLI :global parameters + {@code openclaw} argument sequence.
 * <p>
 * global parametersconsistent with documentation:<code>[--dev] [--profile &lt;name&gt;] [--container &lt;name&gt;] [--no-color]</code>,subcommand and flags.
 * </p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public final class OpenClawCliRequest {

    private final boolean dev;
    private final String profile;
    private final String container;
    private final boolean noColor;
 /** null {@link io.github.easy4j.openclaw.OpenClawClientConfig#getLocalTimeoutSeconds} */
    private final Integer timeoutSeconds;
    private final List<String> arguments;

    // ============================================================
    // Getters (non-Lombok)
    // ============================================================

    public boolean isDev() { return dev; }
    public String getProfile() { return profile; }
    public String getContainer() { return container; }
    public boolean isNoColor() { return noColor; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public List<String> getArguments() { return arguments; }

    private OpenClawCliRequest(Builder b) {
        this.dev = b.dev;
        this.profile = b.profile;
        this.container = b.container;
        this.noColor = b.noColor;
        this.timeoutSeconds = b.timeoutSeconds;
        this.arguments = OpenClawLists.copyOf(b.arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
 * builder.
     */
    public static final class Builder {

        private boolean dev;
        private String profile;
        private String container;
        private boolean noColor;
        private Integer timeoutSeconds;
        private final List<String> arguments = new ArrayList<>();

        public Builder dev(boolean dev) {
            this.dev = dev;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder container(String container) {
            this.container = container;
            return this;
        }

        public Builder noColor(boolean noColor) {
            this.noColor = noColor;
            return this;
        }

        /**
 * processtimeout(seconds);/CLI timeout.
         */
        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
 * argument sequence(executable nameglobal parameters), {@code "gateway", "health"}.
         */
        public Builder arguments(String... args) {
            this.arguments.clear();
            if (args != null) {
                this.arguments.addAll(Arrays.asList(args));
            }
            return this;
        }

        public Builder arguments(List<String> args) {
            this.arguments.clear();
            if (args != null) {
                this.arguments.addAll(args);
            }
            return this;
        }

        public OpenClawCliRequest build() {
            return new OpenClawCliRequest(this);
        }
    }
}
