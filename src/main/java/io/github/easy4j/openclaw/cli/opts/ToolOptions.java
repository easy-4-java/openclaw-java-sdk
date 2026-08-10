package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw tool}:(reserved command root), CLI .
 * <p>
 * openclaw ({@code src/cli/command-registration-policy.ts}),{@code tool}
 * {@code RESERVED_NON_PLUGIN_COMMAND_ROOTS} ,only used forplugin,
 * Commander {@code .option(...)} .
 * </p>
 * <p>
 * CLI : {@link #empty} Equivalent to {@code openclaw tool}.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/tool">tool CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ToolOptions implements CliSubArgs {

    /**
 * :Corresponds to {@code openclaw tool}.
     *
 * @return
     */
    public static ToolOptions empty() {
        return INSTANCE;
    }

    private static final ToolOptions INSTANCE = new ToolOptions();

    private ToolOptions() {
    }

    /**
     * {@inheritDoc}
     *
 * @return (subcommand token )
     */
    @Override
    public List<String> toSubcommandArguments() {
        return Collections.emptyList();
    }
}
