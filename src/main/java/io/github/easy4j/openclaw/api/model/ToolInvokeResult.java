package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tools Invoke API .
 * <p>
 * Corresponds to {@code POST /tools/invoke} JSON.
 * </p>
 *
 * <h3>status code</h3>
 * <ul>
 * <li>{@code 200} - :{@code { ok: true, result }}</li>
 * <li>{@code 400} - :{@code { ok: false, error: { type, message } }}</li>
 * <li>{@code 401} - </li>
 * <li>{@code 404} - </li>
 * <li>{@code 405} - </li>
 * <li>{@code 429} - authentication({@code Retry-After} )</li>
 * <li>{@code 500} - </li>
 * </ul>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/tools-invoke-http-api">Tools Invoke API</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolInvokeResult {

 /** : */
    public static final String ERROR_TYPE_NOT_FOUND = "not_found";

 /** : */
    public static final String ERROR_TYPE_INVALID_REQUEST = "invalid_request_error";

 /** : */
    public static final String ERROR_TYPE_TOOL_ERROR = "tool_error";

    /**
 * .
 * <p>{@code true} tool call,{@code false} .</p>
     */
    private Boolean ok;

    /**
 * (only {@code ok} {@code true} ).
 * <p>.</p>
     */
    private Object result;

    /**
 * error message(only {@code ok} {@code false} ).
     */
    private ErrorDetail error;

    /**
 * details.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDetail {
        /**
 * .
         * <ul>
 * <li>{@code "invalid_request_error"} - </li>
 * <li>{@code "tool_error"} - </li>
         * </ul>
         */
        private String type;

        /**
 * message(security).
         */
        private String message;
    }
}
