package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenClaw JSON 协议中的 `ToolInvokeResult` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolInvokeResult {

    /**
     * OpenClaw 协议固定值 {@code "not_found"}；调用方不应在运行时修改。
     */
    public static final String ERROR_TYPE_NOT_FOUND = "not_found";

    /**
     * OpenClaw 协议固定值 {@code "invalid_request_error"}；调用方不应在运行时修改。
     */
    public static final String ERROR_TYPE_INVALID_REQUEST = "invalid_request_error";

    /**
     * OpenClaw 协议固定值 {@code "tool_error"}；调用方不应在运行时修改。
     */
    public static final String ERROR_TYPE_TOOL_ERROR = "tool_error";

    /**
     * 映射 OpenClaw JSON 字段 `ok` 的 布尔开关。
     */
    private Boolean ok;

    /**
     * 映射 OpenClaw JSON 字段 `result` 的 协议内容。
     */
    private Object result;

    /**
     * 映射 OpenClaw JSON 字段 `error` 的 协议内容。
     */
    private ErrorDetail error;

    /**
     * OpenClaw JSON 协议中的 `ErrorDetail` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDetail {
        /**
         * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
         */
        private String type;

        /**
         * 映射 OpenClaw JSON 字段 `message` 的 协议内容。
         */
        private String message;
    }
}
