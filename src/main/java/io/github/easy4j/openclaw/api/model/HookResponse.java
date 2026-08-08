package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * agent(HTTP CLI).
 *
 * <p>Gateway {@code POST /hooks/agent} :
 * <pre>{@code
 * { "ok": true, "runId": "..." }
 * }</pre>
 * :
 * <pre>{@code
 * { "ok": false, "error": "..." }
 * }</pre>
 * </p>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HookResponse {

 /** ( {@code ok} map) */
    @JsonProperty("ok")
    private boolean success;

 /** HTTP status code; -1 */
    private int httpStatus = -1;

 /** runId(Corresponds to {@code runId} field) */
    private String runId;

 /** process */
    private String rawBody;

 /** error message( {@code error} field) */
    private String error;

 /** CLI completion */
    private boolean localInvocation;
}
