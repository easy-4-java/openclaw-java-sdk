package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 工具调用结果，使用 ok 区分成功 payload 与结构化错误。
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
     * 工具不存在时返回的错误类型。
     */
    public static final String ERROR_TYPE_NOT_FOUND = "not_found";

    /**
     * 工具调用参数或请求格式无效时返回的错误类型。
     */
    public static final String ERROR_TYPE_INVALID_REQUEST = "invalid_request_error";

    /**
     * 工具执行过程失败时返回的错误类型。
     */
    public static final String ERROR_TYPE_TOOL_ERROR = "tool_error";

    /**
     * JSON 属性 {@code ok}，表示请求是否成功。
     */
    private Boolean ok;

    /**
     * JSON 属性 {@code result}，表示工具或 RPC 执行结果。
     */
    private Object result;

    /**
     * JSON 属性 {@code error}，表示错误详情。
     */
    private ErrorDetail error;

    /**
     * 工具调用失败的错误类型、消息和扩展详情。
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
         * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
         */
        private String type;

        /**
         * 工具调用失败时服务端返回的可读说明。
         */
        private String message;
    }
}
