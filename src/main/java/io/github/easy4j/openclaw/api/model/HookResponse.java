package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * OpenClaw JSON 协议中的 `HookResponse` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HookResponse {

    /**
     * 映射 OpenClaw JSON 字段 `success` 的 布尔开关。
     */
    @JsonProperty("ok")
    private boolean success;

    /**
     * 映射 OpenClaw JSON 字段 `httpStatus` 的 协议内容。
     */
    private int httpStatus = -1;

    /**
     * 映射 OpenClaw JSON 字段 `runId` 的 关联标识。
     */
    private String runId;

    /**
     * 映射 OpenClaw JSON 字段 `rawBody` 的 协议内容。
     */
    private String rawBody;

    /**
     * 映射 OpenClaw JSON 字段 `error` 的 协议内容。
     */
    private String error;

    /**
     * 映射 OpenClaw JSON 字段 `localInvocation` 的 布尔开关。
     */
    private boolean localInvocation;
}
