package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Hook 执行结果，保留成功标记、运行标识、HTTP 状态和原始响应。
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
     * JSON 属性 {@code success}，表示Webhook 是否执行成功。
     */
    @JsonProperty("ok")
    private boolean success;

    /**
     * JSON 属性 {@code httpStatus}，表示HTTP 状态码。
     */
    private int httpStatus = -1;

    /**
     * JSON 属性 {@code runId}，表示一次智能体运行的标识。
     */
    private String runId;

    /**
     * JSON 属性 {@code rawBody}，表示未解析的原始响应体。
     */
    private String rawBody;

    /**
     * JSON 属性 {@code error}，表示错误详情。
     */
    private String error;

    /**
     * JSON 属性 {@code localInvocation}，表示结果是否来自本地调用。
     */
    private boolean localInvocation;
}
