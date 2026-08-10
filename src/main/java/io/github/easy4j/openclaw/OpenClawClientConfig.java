package io.github.easy4j.openclaw;

import lombok.Data;

/**
 * OpenClawClient 聚合配置，同时持有 HTTP 与 CLI 两个可独立启停的子配置。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawClientConfig {

    /**
     * HTTP、SSE 和 WebSocket 通道共享的配置对象。
     */
    private final OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();

    /**
     * 本地 CLI 通道配置对象。
     */
    private final OpenClawCliConfig cli = new OpenClawCliConfig();

}
