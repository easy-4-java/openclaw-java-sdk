package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * OpenAI Models API .
 * <p>
 * Corresponds to {@code GET /v1/models} JSON.
 * OpenClaw agent ( {@code openclaw},{@code openclaw/default},
 * {@code openclaw/<agentId>}), provider directory.
 * </p>
 *
 * <p> agent ().</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
  *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelsResponse {

 /** object, {@code "list"}. */
    private String object;

 /** . */
    private List<ModelData> data;

    /**
 * /agent .
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelData {
 /** , {@code "openclaw"},{@code "openclaw/default"},{@code "openclaw/research"}. */
        private String id;
 /** object, {@code "model"}. */
        private String object;
 /** . */
        private Long created;
 /** . */
        @JsonProperty("owned_by")
        private String ownedBy;
    }
}
