package io.github.easy4j.openclaw.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.api.model.ToolInvokeRequest;
import io.github.easy4j.openclaw.api.model.ToolInvokeResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Objects;

/**
 * Tools Invoke API client.
 *
 * @see <a href="https://docs.openclaw.ai/gateway/tools-invoke-http-api">Tools Invoke API</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
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
        return invoke(request, null);
    }

    public ToolInvokeResult invoke(ToolInvokeRequest request, HttpCallCancellation cancellation) {
        Objects.requireNonNull(request, "request");

        debug("=== Tool Invoke Request ===");
        debug("tool: {}", request.getTool());
        debug("action: {}", request.getAction());
        debug("args: {}", request.getArgs());

        if (OpenClawStrings.isBlank(request.getTool())) {
            String msg = "Tool name is required";
            warn(msg);
            throw new IllegalArgumentException(msg);
        }

        try {
            Request.Builder builder = authedBuilder(resolveUrl(OpenClawConstants.ENDPOINT_TOOLS_INVOKE));
            Request httpRequest = builder.post(RequestBody.create(objectMapper.writeValueAsString(request), JSON)).build();

            debug("Sending tool invoke request...");

            Call call = httpClient.newCall(httpRequest);
            AutoCloseable registration = cancellation != null ? cancellation.onCancel(call::cancel) : null;
            try (Response response = call.execute()) {
                int status = response.code();
                String respBody = response.body() != null ? response.body().string() : "";

                debug("Tool invoke response status: {}", status);
                debug("Tool invoke response body: {}", respBody);

                if (status == 404) {
                    String msg = "Tool not available: " + request.getTool();
                    warn(msg);
                    ToolInvokeResult result = new ToolInvokeResult();
                    result.setOk(false);
                    ToolInvokeResult.ErrorDetail error = new ToolInvokeResult.ErrorDetail();
                    error.setType(ToolInvokeResult.ERROR_TYPE_NOT_FOUND);
                    error.setMessage(msg);
                    result.setError(error);
                    return result;
                }

                if (status < 200 || status >= 300) {
                    throw new OpenClawHttpException("POST /tools/invoke returned status " + status, status, respBody);
                }

                ToolInvokeResult result = parse(respBody, ToolInvokeResult.class);
                debug("Tool invoke success, ok: {}", result.getOk());
                return result;
            } finally {
                closeRegistration(registration);
            }
        } catch (OpenClawHttpException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenClawHttpException("POST /tools/invoke failed: " + e.getMessage(), e);
        }
    }

    private void closeRegistration(AutoCloseable registration) {
        if (registration == null) {
            return;
        }
        try {
            registration.close();
        } catch (Exception error) {
            debug("Failed to unregister tool cancellation callback: {}", error.getMessage());
        }
    }
}
