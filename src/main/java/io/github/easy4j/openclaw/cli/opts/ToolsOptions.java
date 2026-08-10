package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw tools}:help(root help alias), CLI .
 * <p>
 * openclaw ({@code src/cli/run-main-policy.ts}),{@code tools}
 * {@code ROOT_HELP_ALIASES} ,{@code openclaw tools --help} help.
 * Commander {@code .option(...)} .
 * </p>
 * <p>
 * :"tools" {@code mcp tools} subcommand( MCP include/exclude ),
 * {@link McpOptions} Wraps;only {@code tools} .
 * </p>
 *
 * @see McpOptions
 * @see <a href="https://docs.openclaw.ai/cli/tools">tools CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ToolsOptions implements CliSubArgs {

    /**
 * :Corresponds to {@code openclaw tools}(help).
     *
 * @return
     */
    public static ToolsOptions empty() {
        return INSTANCE;
    }

    private static final ToolsOptions INSTANCE = new ToolsOptions();

    private ToolsOptions() {
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
