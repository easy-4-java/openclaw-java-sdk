package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw configure}:,Used for,device agent default value; {@code --section} .
 * <p> {@code openclaw config}(subcommand);key {@code openclaw config get|set|unset}.
 * Model {@code agents.defaults.models} ; provider authentication provider directory.</p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/configure">configure CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ConfigureOptions implements CliSubArgs {

    /**
 * {@code --section} value,Used for(workspace,model,web,gateway,daemon,channels,plugins,skills,health).
     */
    private final List<String> sections;

    /**
 * @param b builder
     */
    private ConfigureOptions(Builder b) {
        this.sections = OpenClawLists.copyOf(b.sections);
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
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ConfigureOptions} builder.
     */
    public static final class Builder {

 /** section . */
        private final List<String> sections = new ArrayList<>();

        /**
 * {@code --section}(;documentation workspace,model,web,gateway,daemon,channels,plugins,skills,health).
         */
        public Builder section(String section) {
            if (section != null && !section.isEmpty()) {
                sections.add(section);
            }
            return this;
        }

        /**
 * @return {@link ConfigureOptions}
         */
        public ConfigureOptions build() {
            return new ConfigureOptions(this);
        }
    }
}
