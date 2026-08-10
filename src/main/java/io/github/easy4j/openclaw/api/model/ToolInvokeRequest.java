package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `ToolInvokeRequest` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolInvokeRequest {

    /**
     * 映射 OpenClaw JSON 字段 `tool` 的 协议内容。
     */
    private String tool;

    /**
     * 映射 OpenClaw JSON 字段 `action` 的 协议内容。
     */
    private String action;

    /**
     * 映射 OpenClaw JSON 字段 `args` 的 键值对象。
     */
    private Map<String, Object> args;

    /**
     * 映射 OpenClaw JSON 字段 `sessionKey` 的 协议内容。
     */
    private String sessionKey;

    /**
     * 映射 OpenClaw JSON 字段 `dryRun` 的 布尔开关。
     */
    private Boolean dryRun;
}
