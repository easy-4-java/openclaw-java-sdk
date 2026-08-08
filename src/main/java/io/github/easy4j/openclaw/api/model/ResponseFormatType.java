package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * {@link ResponseFormat} type .
 *
 * <ul>
 * <li>{@code text} — </li>
 * <li>{@code json_object} — JSON</li>
 * <li>{@code json_schema} — ( {@code json_schema} field)</li>
 * </ul>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public enum ResponseFormatType {

    TEXT("text"),
    JSON_OBJECT("json_object"),
    JSON_SCHEMA("json_schema");

    private final String value;

    ResponseFormatType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
