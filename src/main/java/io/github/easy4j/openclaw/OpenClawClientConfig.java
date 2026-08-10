package io.github.easy4j.openclaw;

import lombok.Data;

/**
 * OpenClaw unified client configuration( POJO, Spring {@code @ConfigurationProperties} map).
 * <p>
 * Composes {@link OpenClawHttpClientConfig}(HTTP/Gateway ) {@link OpenClawCliConfig}( CLI ),
 * {@link io.github.easy4j.openclaw.OpenClawClient} .
 * </p>
 *
 * @see OpenClawHttpClientConfig
 * @see OpenClawCliConfig
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawClientConfig {

 /** HTTP/Gateway */
    private final OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();

 /** CLI */
    private final OpenClawCliConfig cli = new OpenClawCliConfig();

}
