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
 * Responses API 请求，支持多模态输入、工具、采样和响应格式。
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

    /**
     * JSON 属性 {@code instructions}，表示模型执行指令。
     */
    private String instructions;

    /**
     * JSON 属性 {@code tools}，表示可供模型调用的工具定义。
     */
    private List<Map<String, Object>> tools;

    /**
     * JSON 属性 {@code toolChoice}，表示工具选择策略。
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * JSON 属性 {@code stream}，表示是否启用流式响应。
     */
    private Boolean stream;

    /**
     * JSON 属性 {@code maxOutputTokens}，表示最大输出 Token 数。
     */
    @JsonProperty("max_output_tokens")
    private Integer maxOutputTokens;

    /**
     * JSON 属性 {@code temperature}，表示采样温度。
     */
    private Double temperature;

    /**
     * JSON 属性 {@code topP}，表示核采样概率阈值。
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * JSON 属性 {@code user}，表示终端用户标识。
     */
    private String user;

    /**
     * JSON 属性 {@code previousResponseId}，表示要延续的上一条响应标识。
     */
    @JsonProperty("previous_response_id")
    private String previousResponseId;

    /**
     * JSON 属性 {@code maxToolCalls}，OpenClaw 当前接受该兼容参数但不参与执行。
     */
    @JsonProperty("max_tool_calls")
    private Integer maxToolCalls;

    /**
     * JSON 属性 {@code reasoning}，OpenClaw 当前接受该兼容参数但不参与执行。
     */
    private Object reasoning;

    /**
     * JSON 属性 {@code metadata}，OpenClaw 当前接受该兼容参数但不参与执行。
     */
    private Map<String, String> metadata;

    /**
     * JSON 属性 {@code store}，OpenClaw 当前接受该兼容参数但不参与执行。
     */
    private Boolean store;

    /**
     * JSON 属性 {@code truncation}，OpenClaw 当前接受该兼容参数但不参与执行。
     */
    private Object truncation;

    // ==================== Inner Classes ====================

    /**
     * Responses API 输入项，可表示消息、函数输出、图片或文件。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InputItem {

        /**
         * 手写构建器：显式声明以规避 JDK 21 javac 在 Enter 阶段先于 Lombok
         * 生成解析嵌套 builder 类型的时序问题（公开 API 与 Lombok 生成形态一致）。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        public static class InputItemBuilder {

            private final InputItem item = new InputItem();

            /**
             * 设置 JSON 属性 {@code type}。
             *
             * @param value 类型判别值
             * @return 当前构建器
             */
            public InputItemBuilder type(String value) {
                item.type = value;
                return this;
            }

            /**
             * 设置 JSON 属性 {@code role}。
             *
             * @param value 消息角色
             * @return 当前构建器
             */
            public InputItemBuilder role(String value) {
                item.role = value;
                return this;
            }

            /**
             * 设置 JSON 属性 {@code content}。
             *
             * @param value 消息正文
             * @return 当前构建器
             */
            public InputItemBuilder content(String value) {
                item.content = value;
                return this;
            }

            /**
             * 设置 JSON 属性 {@code call_id}。
             *
             * @param value 函数调用标识
             * @return 当前构建器
             */
            public InputItemBuilder callId(String value) {
                item.callId = value;
                return this;
            }

            /**
             * 设置函数调用输出文本。
             *
             * @param value 输出文本
             * @return 当前构建器
             */
            public InputItemBuilder output(String value) {
                item.output = value;
                return this;
            }

            /**
             * 设置 JSON 属性 {@code source}。
             *
             * @param value 数据来源
             * @return 当前构建器
             */
            public InputItemBuilder source(Source value) {
                item.source = value;
                return this;
            }

            /**
             * 构建不可变 {@link InputItem}。
             *
             * @return 输入项实例
             */
            public InputItem build() {
                return item;
            }
        }

        /**
         * 返回输入项构建器。
         *
         * @return 新的 {@link InputItemBuilder} 实例
         */
        public static InputItemBuilder builder() {
            return new InputItemBuilder();
        }

        /**
         * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
         */
        private String type;

        /**
         * JSON 属性 {@code role}，表示聊天消息角色。
         */
        private String role;
        /**
         * JSON 属性 {@code content}，表示消息或输出正文。
         */
        private String content;

        /**
         * JSON 属性 {@code callId}，表示函数调用标识。
         */
        @JsonProperty("call_id")
        private String callId;
        /**
         * 与 {@code callId} 对应的函数调用输出文本。
         */
        private String output;

        /**
         * JSON 属性 {@code source}，表示数据来源。
         */
        private Source source;

        // ==================== Factory Methods ====================

        /**
         * 创建 message 类型的 Responses API 输入项构建器。
         *
         * @return 已设置 {@code type=message} 的输入项构建器
         */
        public static InputItem.InputItemBuilder message() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_MESSAGE);
        }

        /**
         * 创建 function_call_output 类型的输入项构建器。
         *
         * @return 已设置 {@code type=function_call_output} 的输入项构建器
         */
        public static InputItem.InputItemBuilder functionCallOutput() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_FUNCTION_CALL_OUTPUT);
        }

        /**
         * 创建图片输入项，并记录来源类型和值。
         *
         * @param sourceType 图片来源类型，例如 {@code url} 或 {@code base64}
         * @param value 与来源类型匹配的 URL 或 Base64 数据
         * @return 已设置图片类型和来源信息的输入项构建器
         */
        public static InputItem.InputItemBuilder imageSource(String sourceType, String value) {
            return InputItem.builder()
                    .type(OpenClawConstants.INPUT_TYPE_IMAGE)
                    .source(buildSource(sourceType, value, null));
        }

        /**
         * 创建以 URL 为来源的图片输入项。
         *
         * @param url 完整目标 URL
         * @return 已设置 URL 图片来源的输入项构建器
         */
        public static InputItem.InputItemBuilder imageUrl(String url) {
            return imageSource("url", url);
        }

        /**
         * 创建以 Base64 数据为来源的图片输入项。
         *
         * @param base64Data 不含 Data URI 前缀的 Base64 编码负载
         * @return 已设置 Base64 图片来源的输入项构建器
         */
        public static InputItem.InputItemBuilder imageBase64(String base64Data) {
            return imageSource("base64", base64Data);
        }

        /**
         * 创建文件输入项，并记录来源类型、值和可选媒体类型。
         *
         * @param sourceType 文件来源类型，例如 {@code url} 或 {@code base64}
         * @param value 与来源类型匹配的 URL 或 Base64 数据
         * @param mediaType 文件的 MIME 类型；未指定时可为 {@code null}
         * @return 已设置文件类型、来源和媒体类型的输入项构建器
         */
        public static InputItem.InputItemBuilder fileSource(String sourceType, String value, String mediaType) {
            return InputItem.builder()
                    .type(OpenClawConstants.INPUT_TYPE_FILE)
                    .source(buildSource(sourceType, value, mediaType));
        }

        private static Source buildSource(String sourceType, String value, String mediaType) {
            Source.SourceBuilder builder = Source.builder().type(sourceType).mediaType(mediaType);
            if ("base64".equals(sourceType)) {
                builder.data(value);
            } else {
                builder.url(value);
            }
            return builder.build();
        }

        /**
         * 创建以 URL 为来源的文件输入项。
         *
         * @param url 完整目标 URL
         * @return 已设置 URL 文件来源且不指定媒体类型的输入项构建器
         */
        public static InputItem.InputItemBuilder fileUrl(String url) {
            return fileUrl(url, null);
        }

        /**
         * 创建以 URL 为来源的文件输入项。
         *
         * @param url 完整目标 URL
         * @param mediaType 文件的 MIME 类型；未指定时可为 {@code null}
         * @return 已设置 URL 文件来源和媒体类型的输入项构建器
         */
        public static InputItem.InputItemBuilder fileUrl(String url, String mediaType) {
            return fileSource("url", url, mediaType);
        }

        /**
         * 创建以 Base64 数据为来源的文件输入项。
         *
         * @param base64Data 不含 Data URI 前缀的 Base64 编码负载
         * @return 已设置 Base64 文件来源且不指定媒体类型的输入项构建器
         */
        public static InputItem.InputItemBuilder fileBase64(String base64Data) {
            return fileBase64(base64Data, null);
        }

        /**
         * 创建以 Base64 数据为来源的文件输入项。
         *
         * @param base64Data 不含 Data URI 前缀的 Base64 编码负载
         * @param mediaType 文件的 MIME 类型；未指定时可为 {@code null}
         * @return 已设置 Base64 文件来源和媒体类型的输入项构建器
         */
        public static InputItem.InputItemBuilder fileBase64(String base64Data, String mediaType) {
            return fileSource("base64", base64Data, mediaType);
        }

        // ==================== Source Inner Class ====================

        /**
         * 图片或文件输入的来源类型、值、文件名和媒体类型。
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
        public static class Source {
            /**
             * JSON 属性 {@code type}，表示对象或协议帧的类型判别值。
             */
            private String type;

            /**
             * JSON 属性 {@code url}，表示目标地址。
             */
            private String url;

            /**
             * JSON 属性 {@code data}，表示 Base64 编码的数据负载。
             */
            private String data;

            /**
             * JSON 属性 {@code mediaType}，表示文件媒体类型。
             */
            @JsonProperty("media_type")
            private String mediaType;

            /**
             * JSON 属性 {@code filename}，表示文件名。
             */
            private String filename;

            /**
             * JSON 属性 {@code detail}，表示输出详细度。
             */
            private String detail;
        }
    }
}
