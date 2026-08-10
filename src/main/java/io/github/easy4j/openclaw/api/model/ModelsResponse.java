package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenClaw JSON 协议中的 `ModelsResponse` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
     */
    private String object;

    /**
     * 映射 OpenClaw JSON 字段 `data` 的 有序数组。
     */
    private List<ModelData> data;

    /**
     * OpenClaw JSON 协议中的 `ModelData` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
         * 映射 OpenClaw JSON 字段 `id` 的 关联标识。
         */
        private String id;
        /**
         * 映射 OpenClaw JSON 字段 `object` 的 协议内容。
         */
        private String object;
        /**
         * 映射 OpenClaw JSON 字段 `created` 的 协议内容。
         */
        private Long created;
        /**
         * 映射 OpenClaw JSON 字段 `ownedBy` 的 协议内容。
         */
        @JsonProperty("owned_by")
        private String ownedBy;
    }
}
