package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.easy4j.openclaw.api.OpenClawConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * OpenClaw JSON 协议中的 `ResponseRequest` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
public class ResponseRequest {

    /**
     * 映射 OpenClaw JSON 字段 `agent` 的 协议内容。
     */
    private String agent;

    /**
     * 映射 OpenClaw JSON 字段 `model` 的 协议内容。
     */
    private String model;

    /**
     * 映射 OpenClaw JSON 字段 `input` 的 协议内容。
     */
    private Object input;

    /**
     * 映射 OpenClaw JSON 字段 `instructions` 的 协议内容。
     */
    private String instructions;

    /**
     * 映射 OpenClaw JSON 字段 `tools` 的 有序数组。
     */
    private List<Map<String, Object>> tools;

    /**
     * 映射 OpenClaw JSON 字段 `toolChoice` 的 协议内容。
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * 映射 OpenClaw JSON 字段 `stream` 的 布尔开关。
     */
    private Boolean stream;

    /**
     * 映射 OpenClaw JSON 字段 `maxOutputTokens` 的 协议内容。
     */
    @JsonProperty("max_output_tokens")
    private Integer maxOutputTokens;

    /**
     * 映射 OpenClaw JSON 字段 `temperature` 的 协议内容。
     */
    private Double temperature;

    /**
     * 映射 OpenClaw JSON 字段 `topP` 的 协议内容。
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 映射 OpenClaw JSON 字段 `user` 的 协议内容。
     */
    private String user;

    /**
     * 映射 OpenClaw JSON 字段 `previousResponseId` 的 关联标识。
     */
    @JsonProperty("previous_response_id")
    private String previousResponseId;

    // ==================== Inner Classes ====================

    /**
     * OpenClaw JSON 协议中的 `InputItem` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
    public static class InputItem {

        /**
         * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
         */
        private String type;

        /**
         * 映射 OpenClaw JSON 字段 `role` 的 协议内容。
         */
        private String role;
        /**
         * 映射 OpenClaw JSON 字段 `content` 的 协议内容。
         */
        private String content;

        /**
         * 映射 OpenClaw JSON 字段 `callId` 的 关联标识。
         */
        @JsonProperty("call_id")
        private String callId;
        /**
         * 映射 OpenClaw JSON 字段 `output` 的 协议内容。
         */
        private String output;

        /**
         * 映射 OpenClaw JSON 字段 `source` 的 协议内容。
         */
        private Source source;

        // ==================== Factory Methods ====================

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `message` 数据。
         *
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder message() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_MESSAGE);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `functionCallOutput` 数据。
         *
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder functionCallOutput() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_FUNCTION_CALL_OUTPUT);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `imageSource` 数据。
         *
         * @param sourceType 写入 `sourceType` 协议字段的内容
         * @param value 写入 `value` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder imageSource(String sourceType, String value) {
            return InputItem.builder()
                    .type(OpenClawConstants.INPUT_TYPE_IMAGE)
                    .source(Source.builder()
                            .type(sourceType)
                            .url(value)
                            .build());
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `imageUrl` 数据。
         *
         * @param url 完整目标 URL
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder imageUrl(String url) {
            return imageSource("url", url);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `imageBase64` 数据。
         *
         * @param base64Data 写入 `base64Data` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder imageBase64(String base64Data) {
            return imageSource("base64", base64Data);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `fileSource` 数据。
         *
         * @param sourceType 写入 `sourceType` 协议字段的内容
         * @param value 写入 `value` 协议字段的内容
         * @param mediaType 写入 `mediaType` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder fileSource(String sourceType, String value, String mediaType) {
            return InputItem.builder()
                    .type(OpenClawConstants.INPUT_TYPE_FILE)
                    .source(Source.builder()
                            .type(sourceType)
                            .url(value)
                            .mediaType(mediaType)
                            .build());
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `fileUrl` 数据。
         *
         * @param url 完整目标 URL
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder fileUrl(String url) {
            return fileUrl(url, null);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `fileUrl` 数据。
         *
         * @param url 完整目标 URL
         * @param mediaType 写入 `mediaType` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder fileUrl(String url, String mediaType) {
            return fileSource("url", url, mediaType);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `fileBase64` 数据。
         *
         * @param base64Data 写入 `base64Data` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder fileBase64(String base64Data) {
            return fileBase64(base64Data, null);
        }

        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `InputItem` 中的 `fileBase64` 数据。
         *
         * @param base64Data 写入 `base64Data` 协议字段的内容
         * @param mediaType 写入 `mediaType` 协议字段的内容
         * @return 预填充当前工厂方法字段、可继续链式补充内容的 InputItemBuilder
         */
        public static InputItemBuilder fileBase64(String base64Data, String mediaType) {
            return fileSource("base64", base64Data, mediaType);
        }

        // ==================== Source Inner Class ====================

        /**
         * OpenClaw JSON 协议中的 `Source` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Source {
            /**
             * 映射 OpenClaw JSON 字段 `type` 的 协议内容。
             */
            private String type;

            /**
             * 映射 OpenClaw JSON 字段 `url` 的 协议内容。
             */
            private String url;

            /**
             * 映射 OpenClaw JSON 字段 `mediaType` 的 协议内容。
             */
            @JsonProperty("media_type")
            private String mediaType;

            /**
             * 映射 OpenClaw JSON 字段 `filename` 的 协议内容。
             */
            private String filename;

            /**
             * 映射 OpenClaw JSON 字段 `detail` 的 协议内容。
             */
            private String detail;
        }
    }
}
