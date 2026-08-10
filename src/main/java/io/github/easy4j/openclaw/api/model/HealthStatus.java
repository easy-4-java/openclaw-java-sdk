package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Gateway 健康状态、版本和附加详情。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStatus {

    /**
     * 健康检查返回的服务状态字符串。
     */
    private String status;

    /**
     * JSON 属性 {@code platform}，表示客户端运行平台。
     */
    private String platform;

    /**
     * JSON 属性 {@code version}，表示版本标识。
     */
    private String version;

}
