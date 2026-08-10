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
 * OpenClaw default OkHttpClient factory.
 *
 * <p>Spring Provides {@link OkHttpClient} inject;
 * SDK , Chat,Tools,Responses connection pool.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class OpenClawOkHttpClientFactory {

    private OpenClawOkHttpClientFactory() {
    }

    /**
 * Creates from HTTP configurationhigh concurrencyconnection reuse.
     *
 * @param config HTTP
 * @return SDK OkHttpClient
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
 * Shuts down SDK .inject.
     *
 * @param client SDK
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
