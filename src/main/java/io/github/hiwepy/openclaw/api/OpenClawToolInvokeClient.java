package io.github.hiwepy.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hiwepy.openclaw.OpenClawHttpClientConfig;
import io.github.hiwepy.openclaw.exception.OpenClawHttpException;
import io.github.hiwepy.openclaw.util.OpenClawStrings;
import io.github.hiwepy.openclaw.api.model.ToolInvokeRequest;
import io.github.hiwepy.openclaw.api.model.ToolInvokeResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Objects;

/**
 * Tools Invoke API 客户端。
 *
 * @see <a href="https://docs.openclaw.ai/gateway/tools-invoke-http-api">Tools Invoke API</a>
 */
@Slf4j
public class OpenClawToolInvokeClient extends OpenClawHttpClient {

    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config) {
        super(config);
    }

    public OpenClawToolInvokeClient(OpenClawHttpClientConfig config, ObjectMapper objectMapper, OkHttpClient httpClient) {
        super(config, objectMapper, httpClient);
    }

    public ToolInvokeResult invoke(ToolInvokeRequest request) {
        Objects.requireNonNull(request, "request");
        if (OpenClawStrings.isBlank(request.getTool())) {
            throw new IllegalArgumentException("tool name is required");
        }

        try {
            Request.Builder builder = authedBuilder(resolveUrl(OpenClawConstants.ENDPOINT_TOOLS_INVOKE));
            Request httpRequest = builder.post(RequestBody.create(objectMapper.writeValueAsString(request), JSON)).build();

            try (Response response = httpClient.newCall(httpRequest).execute()) {
                int status = response.code();
                String respBody = response.body() != null ? response.body().string() : "";

                if (status == 404) {
                    ToolInvokeResult result = new ToolInvokeResult();
                    result.setOk(false);
                    ToolInvokeResult.ErrorDetail error = new ToolInvokeResult.ErrorDetail();
                    error.setType(ToolInvokeResult.ERROR_TYPE_NOT_FOUND);
                    error.setMessage("Tool not available: " + request.getTool());
                    result.setError(error);
                    return result;
                }
                if (status < 200 || status >= 300) {
                    throw new OpenClawHttpException("POST /tools/invoke returned status " + status, status, respBody);
                }
                return parse(respBody, ToolInvokeResult.class);
            }
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenClawHttpException("POST /tools/invoke failed: " + e.getMessage(), e);
        }
    }
}
