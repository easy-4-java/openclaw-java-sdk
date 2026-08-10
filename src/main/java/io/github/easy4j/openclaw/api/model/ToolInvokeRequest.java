package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 工具调用请求，包含工具名、参数、会话和 dry-run 开关。
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
     * JSON 属性 {@code tool}，表示工具定义或调用信息。
     */
    private String tool;

    /**
     * JSON 属性 {@code action}，表示待执行动作。
     */
    private String action;

    /**
     * JSON 属性 {@code args}，表示工具调用参数映射。
     */
    private Map<String, Object> args;

    /**
     * JSON 属性 {@code sessionKey}，表示Gateway 会话路由键。
     */
    private String sessionKey;

    /**
     * JSON 属性 {@code dryRun}，表示是否仅校验而不执行工具。
     */
    private Boolean dryRun;
}
