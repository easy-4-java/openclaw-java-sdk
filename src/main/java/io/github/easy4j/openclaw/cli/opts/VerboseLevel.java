package io.github.easy4j.openclaw.cli.opts;

/**
 * {@code openclaw agent --verbose} value:session verbose session,consistent with official agent CLI .
 *
 * @see <a href="https://docs.openclaw.ai/cli/agent">agent CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public enum VerboseLevel {

    /**
 * {@code on}:session verbose(documentation:persist verbose level for the session).
     */
    ON("on"),
    /**
 * {@code off}:session verbose .
     */
    OFF("off");

    /**
 * CLI {@code --verbose} ({@code on} {@code off}).
     */
    private final String cliValue;

    /**
 * @param cliValue null CLI characters
     */
    VerboseLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
 * CLI {@code --verbose} value.
     *
 * @return {@code on} {@code off}
     */
    public String cliValue() {
        return cliValue;
    }
}
