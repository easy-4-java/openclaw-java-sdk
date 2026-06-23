package io.github.hiwepy.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hiwepy.openclaw.OpenClawHttpClientConfig;
import io.github.hiwepy.openclaw.api.model.EmbeddingsRequest;
import io.github.hiwepy.openclaw.api.model.EmbeddingsResponse;
import io.github.hiwepy.openclaw.util.OpenClawStrings;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Embeddings API 客户端。
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Embeddings</a>
 */
@Slf4j
public class OpenClawEmbeddingsClient extends OpenClawHttpClient {

    public OpenClawEmbeddingsClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    public OpenClawEmbeddingsClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    public EmbeddingsResponse createEmbeddings(EmbeddingsRequest request) {
        Map<String, String> headers = buildHeaders(request);
        String bodyModel = resolveModel(request);

        EmbeddingsRequest normalized = EmbeddingsRequest.builder()
                .model(bodyModel)
                .input(request.getInput())
                .build();

        String json = postJson(OpenClawConstants.ENDPOINT_EMBEDDINGS, normalized, headers);
        return parse(json, EmbeddingsResponse.class, "embeddings");
    }

    private Map<String, String> buildHeaders(EmbeddingsRequest request) {
        Map<String, String> headers = new HashMap<>();
        if (request.getModel() != null && !OpenClawStrings.isAgentTarget(request.getModel())) {
            headers.put(OpenClawConstants.HEADER_X_OPENCLAW_MODEL, request.getModel());
        }
        return headers.isEmpty() ? null : headers;
    }

    private String resolveModel(EmbeddingsRequest request) {
        return request.getAgent() != null ? request.getAgent() : request.getModel();
    }
}
