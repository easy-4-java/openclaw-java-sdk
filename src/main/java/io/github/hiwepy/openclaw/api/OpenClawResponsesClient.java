package io.github.hiwepy.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hiwepy.openclaw.OpenClawHttpClientConfig;
import io.github.hiwepy.openclaw.api.model.ResponseRequest;
import io.github.hiwepy.openclaw.api.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenResponses API 客户端。
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openresponses-http-api">OpenResponses API</a>
 */
@Slf4j
public class OpenClawResponsesClient extends OpenClawHttpClient {

    public OpenClawResponsesClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    public OpenClawResponsesClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    /**
     * 发送 OpenResponses 请求。
     */
    public ResponseResult createResponse(ResponseRequest request) {
        Map<String, String> headers = buildHeaders(request);
        String bodyModel = resolveModel(request);

        ResponseRequest normalized = ResponseRequest.builder()
                .model(bodyModel)
                .input(request.getInput())
                .instructions(request.getInstructions())
                .tools(request.getTools())
                .toolChoice(request.getToolChoice())
                .stream(request.getStream())
                .maxOutputTokens(request.getMaxOutputTokens())
                .temperature(request.getTemperature())
                .topP(request.getTopP())
                .user(request.getUser())
                .previousResponseId(request.getPreviousResponseId())
                .build();

        String json = postJson(OpenClawConstants.ENDPOINT_RESPONSES, normalized, headers);
        return parse(json, ResponseResult.class, "response");
    }

    private Map<String, String> buildHeaders(ResponseRequest request) {
        Map<String, String> headers = new HashMap<>();
        if (request.getModel() != null && !io.github.hiwepy.openclaw.util.OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }
        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(ResponseRequest request) {
        return request.getAgent() != null ? request.getAgent() : request.getModel();
    }
}
