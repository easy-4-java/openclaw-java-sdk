package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import java.util.List;

/**
 * CLI flag argument fragment argv (package-private).
 * <p> {@link io.github.easy4j.openclaw.cli.args.CliSubArgs}
 * {@link io.github.easy4j.openclaw.cli.args.CliSubArgs#toSubcommandArguments}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
final class OpenClawCliArgv {

    private OpenClawCliArgv() {
    }

    /**
 * {@code value} null , {@code flag} {@code value} .
     *
 * @param out argument list
 * @param flag {@code "--url"}
 * @param value value
     */
    static void addIfPresent(List<String> out, String flag, String value) {
        if (value != null && OpenClawStrings.isNotBlank(value)) {
            out.add(flag);
            out.add(value);
        }
    }

    /**
 * {@code value} , {@code flag} valuecharacters.
     *
 * @param out argument list
 * @param flag
 * @param value value(milliseconds)
     */
    static void addIfPositive(List<String> out, String flag, int value) {
        if (value > 0) {
            out.add(flag);
            out.add(Integer.toString(value));
        }
    }

    /**
 * {@code value} null , {@code flag} {@link Integer} characters.
     *
 * @param out argument list
 * @param flag
 * @param value
     */
    static void addIfNotNull(List<String> out, String flag, Integer value) {
        if (value != null) {
            out.add(flag);
            out.add(Integer.toString(value));
        }
    }

    /**
 * {@code value} null , {@code flag} {@link Double} characters.
     *
 * @param out argument list
 * @param flag
 * @param value value
     */
    static void addIfNotNull(List<String> out, String flag, Double value) {
        if (value != null) {
            out.add(flag);
            out.add(Double.toString(value));
        }
    }

    /**
 * {@code enabled} When true,only {@code flag}(value token).
     *
 * @param out argument list
 * @param flag
 * @param enabled flag
     */
    static void addFlag(List<String> out, String flag, boolean enabled) {
        if (enabled) {
            out.add(flag);
        }
    }

    /**
 * {@code flag} value( {@code --scope}).
     *
 * @param out argument list
 * @param flag
 * @param values value, null
     */
    static void addRepeatable(List<String> out, String flag, List<String> values) {
        if (values == null) {
            return;
        }
        for (String v : values) {
            if (v != null && OpenClawStrings.isNotBlank(v)) {
                out.add(flag);
                out.add(v.trim());
            }
        }
    }

    /**
 * "" token ({@link io.github.easy4j.openclaw.cli.opts} Builder {@code extra}).
     *
 * @param out argument list
 * @param extra token, null
     */
    static void addExtra(List<String> out, List<String> extra) {
        if (extra == null) {
            return;
        }
        out.addAll(extra);
    }
}
