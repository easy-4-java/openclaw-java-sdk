package io.github.easy4j.openclaw;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OpenClaw 默认 OkHttpClient 工厂。
 *
 * <p>Spring 等容器已经提供 {@link OkHttpClient} 时应优先使用注入构造器；本工厂只负责
 * SDK 独立运行场景，并保证 Chat、Tools、Responses 等客户端共享同一连接池。</p>
 */
public final class OpenClawOkHttpClientFactory {

    private OpenClawOkHttpClientFactory() {
    }

    /**
     * 按 HTTP 配置创建适合高并发长连接复用的客户端。
     *
     * @param config HTTP 配置
     * @return SDK 自主管理的 OkHttpClient
     */
    public static OkHttpClient create(OpenClawHttpClientConfig config) {
        Objects.requireNonNull(config, "config");
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(Math.max(1, config.getMaxRequests()));
        dispatcher.setMaxRequestsPerHost(Math.max(1, config.getMaxRequestsPerHost()));
        ConnectionPool connectionPool = new ConnectionPool(
                Math.max(1, config.getMaxIdleConnections()),
                Math.max(1L, config.getKeepAliveDurationMillis()),
                TimeUnit.MILLISECONDS);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .connectTimeout(Math.max(1, config.getConnectTimeoutMillis()), TimeUnit.MILLISECONDS)
                .readTimeout(Math.max(0, config.getReadTimeoutMillis()), TimeUnit.MILLISECONDS)
                .writeTimeout(Math.max(1, config.getWriteTimeoutMillis()), TimeUnit.MILLISECONDS)
                .callTimeout(Math.max(0, config.getCallTimeoutMillis()), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure());
        if (!config.isVerifySsl()) {
            builder.hostnameVerifier((hostname, session) -> true);
        }
        return builder.build();
    }

    /**
     * 释放 SDK 自建客户端资源。外部注入的客户端不得调用此方法。
     *
     * @param client SDK 自建客户端
     */
    public static void shutdown(OkHttpClient client) {
        if (Objects.isNull(client)) {
            return;
        }
        client.dispatcher().cancelAll();
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();
    }
}
