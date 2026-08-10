package io.github.easy4j.openclaw.api;

import io.github.easy4j.openclaw.HttpCallCancellation;
import io.github.easy4j.openclaw.OpenClawHttpClientConfig;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Request;
import okio.Buffer;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.io.ByteArrayOutputStream;

/**
 * 基于 Netty 的非阻塞 HTTP 传输。
 *
 * <p>所有网络 I/O 均由固定数量的 Netty event-loop 线程处理，不为单次请求创建工作线程。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@Slf4j
public final class OpenClawAsyncHttpTransport implements AutoCloseable {

    private final AsyncHttpClient client;

    /**
     * 创建非阻塞传输。
     *
     * @param config HTTP 配置
     */
    public OpenClawAsyncHttpTransport(OpenClawHttpClientConfig config) {
        Objects.requireNonNull(config, "config");
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setConnectTimeout(Math.max(1, config.getConnectTimeoutMillis()))
                .setReadTimeout(config.getReadTimeoutMillis() > 0 ? config.getReadTimeoutMillis() : -1)
                .setMaxConnections(Math.max(1, config.getMaxRequests()))
                .setMaxConnectionsPerHost(Math.max(1, config.getMaxRequestsPerHost()))
                .setIoThreadsCount(Math.max(1, config.getIoThreadsCount()))
                .setKeepAlive(true);
        if (config.getCallTimeoutMillis() > 0) {
            builder.setRequestTimeout(config.getCallTimeoutMillis());
        }
        this.client = new DefaultAsyncHttpClient(builder.build());
        log.debug("OpenClaw async transport initialized: ioThreads={}, maxConnections={}, maxConnectionsPerHost={}",
                config.getIoThreadsCount(), config.getMaxRequests(), config.getMaxRequestsPerHost());
    }

    /**
     * 异步执行请求并返回完整响应体。
     *
     * @param request OkHttp 请求描述，仅作为兼容的请求模型
     * @param cancellation 可选取消信号
     * @return 异步响应体
     */
    public CompletableFuture<String> execute(Request request, HttpCallCancellation cancellation) {
        Objects.requireNonNull(request, "request");
        BoundRequestBuilder builder = prepare(request);

        org.asynchttpclient.ListenableFuture<Response> pending = builder.execute();
        AutoCloseable registration = Objects.nonNull(cancellation)
                ? cancellation.onCancel(() -> pending.cancel(true)) : null;
        CompletableFuture<String> result = new CompletableFuture<>();
        pending.toCompletableFuture().whenComplete((response, error) -> {
            closeRegistration(registration);
            if (Objects.nonNull(error)) {
                result.completeExceptionally(unwrap(error));
                return;
            }
            String responseBody = response.getResponseBody();
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                result.completeExceptionally(new OpenClawHttpException(
                        "Request returned status " + response.getStatusCode(), response.getStatusCode(), responseBody));
                return;
            }
            result.complete(responseBody);
        });
        result.whenComplete((value, error) -> {
            if (result.isCancelled()) {
                pending.cancel(true);
            }
        });
        return result;
    }

    /**
     * 以增量回调方式异步消费响应体，Netty event-loop 不会为每个流创建线程。
     *
     * @param request 请求描述
     * @param cancellation 可选取消信号
     * @param bodyConsumer 响应数据块消费者，必须快速返回
     * @return 流结束信号
     */
    public CompletableFuture<Void> executeStream(Request request, HttpCallCancellation cancellation,
                                                 Consumer<byte[]> bodyConsumer) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(bodyConsumer, "bodyConsumer");
        CompletableFuture<Void> result = new CompletableFuture<>();
        ByteArrayOutputStream errorBody = new ByteArrayOutputStream();
        int[] statusCode = new int[1];
        org.asynchttpclient.ListenableFuture<Void> pending = prepare(request).execute(new AsyncHandler<Void>() {
            @Override
            public State onStatusReceived(HttpResponseStatus responseStatus) {
                statusCode[0] = responseStatus.getStatusCode();
                return State.CONTINUE;
            }

            @Override
            public State onHeadersReceived(HttpHeaders headers) {
                return State.CONTINUE;
            }

            @Override
            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                byte[] bytes = bodyPart.getBodyPartBytes();
                if (statusCode[0] >= 200 && statusCode[0] < 300) {
                    bodyConsumer.accept(bytes);
                } else if (errorBody.size() < 8_192) {
                    errorBody.write(bytes, 0, Math.min(bytes.length, 8_192 - errorBody.size()));
                }
                return State.CONTINUE;
            }

            @Override
            public Void onCompleted() {
                if (statusCode[0] < 200 || statusCode[0] >= 300) {
                    result.completeExceptionally(new OpenClawHttpException(
                            "Stream returned status " + statusCode[0], statusCode[0],
                            errorBody.toString(java.nio.charset.StandardCharsets.UTF_8)));
                } else {
                    result.complete(null);
                }
                return null;
            }

            @Override
            public void onThrowable(Throwable error) {
                result.completeExceptionally(error);
            }
        });
        AutoCloseable registration = Objects.nonNull(cancellation)
                ? cancellation.onCancel(() -> pending.cancel(true)) : null;
        result.whenComplete((value, error) -> {
            closeRegistration(registration);
            if (result.isCancelled()) {
                pending.cancel(true);
            }
        });
        return result;
    }

    private BoundRequestBuilder prepare(Request request) {
        BoundRequestBuilder builder = client.prepare(request.method(), request.url().toString());
        copyHeaders(request.headers(), builder);
        byte[] body = requestBody(request);
        if (body.length > 0) {
            builder.setBody(body);
        }
        return builder;
    }

    private void copyHeaders(Headers headers, BoundRequestBuilder builder) {
        for (String name : headers.names()) {
            for (String value : headers.values(name)) {
                builder.addHeader(name, value);
            }
        }
    }

    private byte[] requestBody(Request request) {
        if (Objects.isNull(request.body())) {
            return new byte[0];
        }
        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readByteArray();
        } catch (IOException error) {
            throw new OpenClawHttpException("Failed to read request body: " + error.getMessage(), error);
        }
    }

    private Throwable unwrap(Throwable error) {
        return error instanceof CompletionException && Objects.nonNull(error.getCause()) ? error.getCause() : error;
    }

    private void closeRegistration(AutoCloseable registration) {
        if (Objects.isNull(registration)) {
            return;
        }
        try {
            registration.close();
        } catch (Exception error) {
            log.debug("Failed to unregister async cancellation callback: {}", error.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException error) {
            log.warn("Failed to close OpenClaw async transport: {}", error.getMessage());
        }
    }
}
