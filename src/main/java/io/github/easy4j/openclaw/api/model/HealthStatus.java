package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * OpenClaw Gateway .
 * <p>
 * SDK {@code GET /v1/models} ; DTO Used for
 * Gateway ( {@code /healthz}).field
 * {@link JsonIgnoreProperties#ignoreUnknown} , Gateway field
 * .
 * </p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 2.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStatus {

    /**
 * characters( {@code "ok"}).
     */
    private String status;

    /**
 * .
     */
    private String platform;

    /**
 * version.
     */
    private String version;

}
