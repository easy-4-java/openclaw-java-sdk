package io.github.easy4j.openclaw.cli.args;

import java.util.Collections;
import java.util.List;

/**
 * CLI "top-level command"argument fragment( {@code openclaw} top-level command name).
 * <p>
 * See {@code io.github.easy4j.openclaw.cli.opts} ( {@link io.github.easy4j.openclaw.cli.opts.AgentOptions}).
 * </p>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@FunctionalInterface
public interface CliSubArgs {

    /**
 * @return executableargument list,top-level command name token ( shell )
     */
    List<String> toSubcommandArguments();

    /**
 * @return subcommand(top-level command token)
     */
    static CliSubArgs empty() {
        return Collections::emptyList;
    }
}
