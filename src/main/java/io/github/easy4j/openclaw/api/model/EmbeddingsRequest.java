package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenAI Embeddings API request body.
 * <p>
 * Corresponds to {@code POST /v1/embeddings} JSON.
 * </p>
 *
 * <h3>field</h3>
 * <ul>
 * <li>{@code agent} - Agent ( {@code "openclaw/default"})
 * <li>{@code model} - embedding( {@code "openai/text-embedding-3-small"})
 * </ul>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api">OpenAI Chat Completions</a>
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
public class EmbeddingsRequest {

    /**
 * Agent .
 * <p> {@code "openclaw"},{@code "openclaw/default"} {@code "openclaw/<agentId>"}.</p>
     */
    private String agent;

    /**
 * embedding.
 * <p> {@code "openai/text-embedding-3-small"}.
 * , Agent embedding.</p>
     */
    private String model;

    /**
 * (characterscharactersarray).
 * <p>characterscharactersarray.</p>
     */
    private Object input;
}
