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
 * OpenResponses API request body.
 * <p>
 * Corresponds to {@code POST /v1/responses} JSON.
 * </p>
 *
 * <h3>field</h3>
 * <ul>
 * <li>{@code agent} - Agent ( {@code "openclaw/default"})
 * <li>{@code model} - LLM ( {@code "gpt-4o"})
 * </ul>
 *
 * <h3>usageexample</h3>
 * <pre>{@code
 * // 1:characters
 * ResponseRequest request = ResponseRequest.builder()
 *     .agent("openclaw/default")
 *     .input("What is the weather?")
 *     .build();
 *
 * // 2:Item array
 * ResponseRequest request = ResponseRequest.builder()
 *     .agent("openclaw/default")
 *     .input(List.of(
 *         InputItem.message().role("user").content("What is the weather?").build(),
 *         InputItem.imageSource("url", "https://example.com/photo.jpg").build()
 *     ))
 *     .build();
 *
 * // 3:tool call
 * ResponseRequest request = ResponseRequest.builder()
 *     .agent("openclaw/default")
 *     .input(List.of(
 *         InputItem.message().role("assistant").content(null).build(),
 *         InputItem.functionCallOutput().callId("call_abc").output("{\"temperature\":\"25C\"}").build()
 *     ))
 *     .build();
 * }</pre>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openresponses-http-api">OpenResponses API</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRequest {

    /**
 * Agent .
     */
    private String agent;

    /**
 * LLM .
     */
    private String model;

    /**
 * .
 * <p>characters Item objectarray.</p>
     */
    private Object input;

    /**
 * system(system).
     */
    private String instructions;

    /**
 * .
     */
    private List<Map<String, Object>> tools;

    /**
 * tool choice.
 * <p>:{@code "auto"},{@code "none"},{@code "required"},
 * {@code { "type": "function", "name": "..." }}.</p>
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
 * Whether to enable SSE streaming.
     */
    private Boolean stream;

    /**
 * token .
     */
    @JsonProperty("max_output_tokens")
    private Integer maxOutputTokens;

    /**
 * .
     */
    private Double temperature;

    /**
 * nucleus .
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
 * .
     */
    private String user;

    /**
 * ID.
     */
    @JsonProperty("previous_response_id")
    private String previousResponseId;

    // ==================== Inner Classes ====================

    /**
 * Response API Input Item .
     * <p>
 * :message,function_call_output,input_image,input_file
     * </p>
     *
 * <h3>usageexample</h3>
     * <pre>{@code
 * // (URL)
     * InputItem.imageSource("url", "https://example.com/photo.jpg")
     *
 * // (base64)
     * InputItem.imageSource("base64", "data:image/png;base64,...")
     *
 * // (URL, MIME )
     * InputItem.fileSource("url", "https://example.com/doc.pdf", "application/pdf")
     *
 * // (base64)
     * InputItem.fileSource("base64", "data:application/pdf;base64,...", "application/pdf")
     * }</pre>
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InputItem {

 /** Item :message,function_call_output,input_image,input_file */
        private String type;

        // message 类型字段
        private String role;
        private String content;

        // function_call_output 类型字段
        @JsonProperty("call_id")
        private String callId;
        private String output;

        // input_image / input_file 共享 source 字段
        private Source source;

        // ==================== Factory Methods ====================

        /**
 * message Item.
         */
        public static InputItemBuilder message() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_MESSAGE);
        }

        /**
 * Item.
         */
        public static InputItemBuilder functionCallOutput() {
            return InputItem.builder().type(OpenClawConstants.INPUT_TYPE_FUNCTION_CALL_OUTPUT);
        }

        /**
 * Item.
         *
 * @param sourceType :{@code "url"} {@code "base64"}
 * @param value URL base64
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
 * Item(URL).
         */
        public static InputItemBuilder imageUrl(String url) {
            return imageSource("url", url);
        }

        /**
 * Item(base64).
         */
        public static InputItemBuilder imageBase64(String base64Data) {
            return imageSource("base64", base64Data);
        }

        /**
 * Item.
         *
 * @param sourceType :{@code "url"} {@code "base64"}
 * @param value URL base64
 * @param mediaType MIME ( {@code "text/plain"},{@code "application/pdf"})
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
 * Item(URL).
         */
        public static InputItemBuilder fileUrl(String url) {
            return fileUrl(url, null);
        }

        /**
 * Item(URL, MIME ).
         */
        public static InputItemBuilder fileUrl(String url, String mediaType) {
            return fileSource("url", url, mediaType);
        }

        /**
 * Item(base64).
         */
        public static InputItemBuilder fileBase64(String base64Data) {
            return fileBase64(base64Data, null);
        }

        /**
 * Item(base64, MIME ).
         */
        public static InputItemBuilder fileBase64(String base64Data, String mediaType) {
            return fileSource("base64", base64Data, mediaType);
        }

        // ==================== Source Inner Class ====================

        /**
 * /.
         * <p>
 * :{@code { type: "url" | "base64", url?: string, media_type?: string, filename?: string, detail?: string }}
         * </p>
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Source {
 /** :{@code "url"} {@code "base64"} */
            private String type;

 /** URL base64 */
            private String url;

 /** MIME */
            @JsonProperty("media_type")
            private String mediaType;

 /** */
            private String filename;

 /** detail :{@code "low"},{@code "high"},{@code "auto"} */
            private String detail;
        }
    }
}
