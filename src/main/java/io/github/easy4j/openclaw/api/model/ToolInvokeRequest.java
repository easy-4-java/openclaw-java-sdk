package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Tools Invoke API request body.
 * <p>
 * Corresponds to {@code POST /tools/invoke} JSON.
 * , Gateway authentication + .
 * secretauthentication(token/password) operator .
 * </p>
 *
 * <h3></h3>
 * <p>:</p>
 * <ul>
 *   <li>{@code tools.profile} / {@code tools.byProvider.profile}</li>
 *   <li>{@code tools.allow} / {@code tools.byProvider.allow}</li>
 *   <li>{@code agents.<id>.tools.allow} / {@code agents.<id>.tools.byProvider.allow}</li>
 * <li>( session key mapchannel)</li>
 * <li> agent </li>
 * </ul>
 * <p>, {@code 404}.</p>
 *
 * <h3></h3>
 * <p> session ,Gateway HTTP :</p>
 * <ul>
 * <li>{@code exec},{@code spawn},{@code shell} - RCE </li>
 * <li>{@code fs_write},{@code fs_delete},{@code fs_move} - system</li>
 * <li>{@code apply_patch} - </li>
 * <li>{@code sessions_spawn},{@code sessions_send} - session</li>
 * <li>{@code cron} - </li>
 * <li>{@code gateway} - Gateway </li>
 * <li>{@code nodes} - node</li>
 * <li>{@code whatsapp_login} - </li>
 * </ul>
 * <p> {@code gateway.tools.deny} {@code gateway.tools.allow} .</p>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/tools-invoke-http-api">Tools Invoke API</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolInvokeRequest {

    /**
 * (Required).
 * <p> {@code "sessions_list"},{@code "browser"} .</p>
     */
    private String tool;

    /**
 * (Optional).
 * <p> schema {@code action} args ,map args .</p>
     */
    private String action;

    /**
 * (Optional).
 * <p>keyvalue.</p>
     */
    private Map<String, Object> args;

    /**
 * session key(Optional).
 * <p> {@code "main"},Gateway session key
 * ( {@code session.mainKey} agent, global scope {@code "global"}).</p>
     */
    private String sessionKey;

    /**
 * (Optional).
 * <p>.</p>
     */
    private Boolean dryRun;
}
