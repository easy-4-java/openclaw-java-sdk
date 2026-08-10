package io.github.easy4j.openclaw.ws.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OpenClaw JSON 协议中的 `ConnectChallenge` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
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
     * 映射 OpenClaw JSON 字段 `nonce` 的 协议内容。
     */
    private String nonce;

    /**
     * 映射 OpenClaw JSON 字段 `ts` 的 协议内容。
     */
    private Long ts;
}
