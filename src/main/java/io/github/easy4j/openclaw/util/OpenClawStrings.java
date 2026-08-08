package io.github.easy4j.openclaw.util;

import io.github.easy4j.openclaw.api.OpenClawConstants;

import java.util.Map;
import java.util.Objects;

/**
 * SDK string utility,avoids introducing Spring/Commons .
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class OpenClawStrings {

    private OpenClawStrings() {
    }

    /**
 * @return {@code true} value {@code null},only
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
 * @return {@code true} value {@code null} characters
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
 * @return blank trim value, {@code null}
     */
    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
 * @return blank {@code defaultValue},value trim
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        String trimmed = trimToNull(value);
        return trimmed != null ? trimmed : Objects.requireNonNull(defaultValue, "defaultValue");
    }

    /**
 * @return {@code null}
     */
    public static String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    /**
 * value blank Map.
     */
    public static void putIfNotBlank(Map<String, Object> target, String key, String value) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(key, "key");
        if (isNotBlank(value)) {
            target.put(key, value.trim());
        }
    }

    /**
 * model value Agent .
     */
    public static boolean isAgentTarget(String value) {
        if (value == null) {
            return false;
        }
        return value.startsWith(OpenClawConstants.AGENT_PREFIX_OPENCLAW) ||
                value.startsWith(OpenClawConstants.AGENT_PREFIX_AGENT_COLON) ||
                value.startsWith(OpenClawConstants.AGENT_PREFIX_OPENCLAW_COLON);
    }
}
