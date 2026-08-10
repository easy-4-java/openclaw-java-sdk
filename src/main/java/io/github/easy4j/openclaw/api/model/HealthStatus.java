package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * OpenClaw JSON 协议中的 `HealthStatus` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStatus {

    /**
     * 映射 OpenClaw JSON 字段 `status` 的 协议内容。
     */
    private String status;

    /**
     * 映射 OpenClaw JSON 字段 `platform` 的 协议内容。
     */
    private String platform;

    /**
     * 映射 OpenClaw JSON 字段 `version` 的 协议内容。
     */
    private String version;

}
