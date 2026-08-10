package io.github.easy4j.openclaw;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OkHttpClient 工厂，把 SDK 的超时、连接池、Dispatcher 并发和连接失败重试配置转换为线程安全客户端。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawOkHttpClientFactory {

    private OpenClawOkHttpClientFactory() {
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawOkHttpClientFactory`。
     *
     * @param config SDK 配置
     * @return 当前门面持有的 OkHttpClient；对应通道未启用时不可调用
     */
    public static OkHttpClient create(OpenClawHttpClientConfig config) {
        Objects.requireNonNull(config, "config");
        int maxRequests = Math.max(1, config.getMaxRequests());
        AtomicInteger threadIndex = new AtomicInteger();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(maxRequests, maxRequests, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxRequests), runnable -> {
                    Thread thread = new Thread(runnable,
                            "openclaw-okhttp-dispatcher-" + threadIndex.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                });
        executor.allowCoreThreadTimeOut(true);
        Dispatcher dispatcher = new Dispatcher(executor);
        dispatcher.setMaxRequests(maxRequests);
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
     * 关闭工厂创建客户端的 Dispatcher 线程池并清空连接池；调用方必须确认客户端未被其他组件复用。
     *
     * @param client 写入 `client` 协议字段的内容
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
