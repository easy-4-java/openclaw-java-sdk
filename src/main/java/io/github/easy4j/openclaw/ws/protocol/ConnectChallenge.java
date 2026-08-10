package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gateway connect.challenge 事件负载，包含 nonce 与服务端时间戳。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectChallenge {

    /**
     * JSON 属性 {@code nonce}，表示握手挑战随机值。
     */
    private String nonce;

    /**
     * JSON 属性 {@code ts}，表示事件时间戳。
     */
    private Long ts;
}
