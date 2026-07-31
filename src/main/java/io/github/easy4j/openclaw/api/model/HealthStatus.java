package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * OpenClaw Gateway 健康状态响应模型。
 * <p>
 * SDK 启动探测时通过 {@code GET /v1/models} 触发；该 DTO 主要用于未来切换到
 * Gateway 原生健康端点（如 {@code /healthz}）时的反序列化。宽松的字段集与
 * {@link JsonIgnoreProperties#ignoreUnknown()} 配合，保证 Gateway 后续扩展字段
 * 不会破坏解析。
 * </p>
 *
 * @author wandl
 * @since 2.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStatus {

    /**
     * 状态字符串（如 {@code "ok"}）。
     */
    private String status;

    /**
     * 平台标识。
     */
    private String platform;

    /**
     * 服务端版本。
     */
    private String version;

}
