package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw config}: {@code openclaw.json} , schema ;subcommand {@code openclaw configure} .
 * <p> {@code --section} Used for,valuedocumentation {@code workspace|model|web|gateway|daemon|channels|plugins|skills|health}.
 * {@link #tail(String...)} {@code get/set/unset/validate/schema/file} subcommand,JSON value builder flag.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/config">config CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ConfigOptions implements CliSubArgs {

    /**
 * {@code --section}:subcommand.
     */
    private final List<String> sections;
    /**
 * subcommand argv: {@code "get","agents.defaults.workspace"},{@code "validate","--json"},{@code "set", path, value, ...flags} , shell .
     */
    private final List<String> tail;

    /**
 * @param b builder
     */
    private ConfigOptions(Builder b) {
        this.sections = OpenClawLists.copyOf(b.sections);
        this.tail = OpenClawLists.copyOf(b.tail);
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
        for (String s : sections) {
            if (s != null && !s.isEmpty()) {
                out.add("--section");
                out.add(s);
            }
        }
        out.addAll(tail);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ConfigOptions} builder.
     */
    public static final class Builder {

 /** {@code --section} value. */
        private final List<String> sections = new ArrayList<>();
 /** subcommand. */
        private final List<String> tail = new ArrayList<>();

 /** subcommand;. */
        public Builder section(String section) {
            if (section != null && !section.isEmpty()) {
                sections.add(section);
            }
            return this;
        }

        /**
 * subcommand( {@code "get", "browser.executablePath"},{@code "validate", "--json"}).
         */
        public Builder tail(String... tokens) {
            if (tokens != null) {
                for (String t : tokens) {
                    if (t != null) {
                        tail.add(t);
                    }
                }
            }
            return this;
        }

        /**
 * @return {@link ConfigOptions}
         */
        public ConfigOptions build() {
            return new ConfigOptions(this);
        }
    }
}
