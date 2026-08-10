package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 向量嵌入请求，包含智能体、模型和单条或批量输入。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddingsRequest {

    /**
     * JSON 属性 {@code agent}，表示智能体标识。
     */
    private String agent;

    /**
     * JSON 属性 {@code model}，表示模型标识。
     */
    private String model;

    /**
     * JSON 属性 {@code input}，表示模型输入。
     */
    private Object input;
}
