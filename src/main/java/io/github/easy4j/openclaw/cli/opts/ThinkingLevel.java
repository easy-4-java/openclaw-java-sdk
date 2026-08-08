package io.github.easy4j.openclaw.cli.opts;

/**
 * {@code openclaw agent --thinking} value: agent"",consistent with official agent CLI .
 *
 * @see <a href="https://docs.openclaw.ai/cli/agent">agent CLI</a>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public enum ThinkingLevel {

 /** (documentation {@code off}). */
    OFF("off"),
 /** {@code minimal}. */
    MINIMAL("minimal"),
 /** {@code low}. */
    LOW("low"),
 /** {@code medium}. */
    MEDIUM("medium"),
 /** {@code high}. */
    HIGH("high"),
 /** {@code xhigh}. */
    XHIGH("xhigh");

    /**
 * CLI {@code --thinking} ( openclaw documentation).
     */
    private final String cliValue;

    /**
 * @param cliValue null, openclaw documentation
     */
    ThinkingLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
 * CLI {@code --thinking} value.
     *
 * @return openclaw token
     */
    public String cliValue() {
        return cliValue;
    }
}
