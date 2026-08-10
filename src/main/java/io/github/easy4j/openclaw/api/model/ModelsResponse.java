package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 模型列表端点响应，包含模型数据数组。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelsResponse {

    /**
     * JSON 属性 {@code object}，表示响应资源类型。
     */
    private String object;

    /**
     * JSON 属性 {@code data}，表示响应数据条目。
     */
    private List<ModelData> data;

    /**
     * 单个模型的标识、所有者和创建时间。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelData {
        /**
         * JSON 属性 {@code id}，表示协议对象或请求的唯一标识。
         */
        private String id;
        /**
         * JSON 属性 {@code object}，表示响应资源类型。
         */
        private String object;
        /**
         * JSON 属性 {@code created}，表示创建时间戳。
         */
        private Long created;
        /**
         * JSON 属性 {@code ownedBy}，表示会话所有者标识。
         */
        @JsonProperty("owned_by")
        private String ownedBy;
    }
}
