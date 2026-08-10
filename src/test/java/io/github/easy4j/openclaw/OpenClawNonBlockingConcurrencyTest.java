package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OpenClaw 非阻塞传输的 500 并发回归测试。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class OpenClawNonBlockingConcurrencyTest {

    private static final int CONCURRENCY = 500;

    @Test
    void shouldCompleteFiveHundredRequestsWithoutPerRequestThreads() throws Exception {
        AtomicInteger requestCount = new AtomicInteger();
        long baselineAsyncThreads = countAsyncHttpClientThreads();
        try (NonBlockingFixture server = new NonBlockingFixture(requestCount, false)) {

            OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
            config.setBaseUrl("http://127.0.0.1:" + server.port());
            config.setStartupCheckEnabled(false);
            config.setIoThreadsCount(4);
            config.setMaxRequests(1_024);
            config.setMaxRequestsPerHost(512);

            ChatRequest request = ChatRequest.builder()
                    .agent("openclaw/test")
                    .messages(Collections.singletonList(ChatMessage.ofUser("ping")))
                    .build();

            try (OpenClawClient client = new OpenClawClient(config)) {
                List<CompletableFuture<?>> futures = new ArrayList<>(CONCURRENCY);
                for (int index = 0; index < CONCURRENCY; index++) {
                    futures.add(client.chat().chatCompletionAsync(request));
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                        .get(30, TimeUnit.SECONDS);

                assertEquals(CONCURRENCY, requestCount.get());
                assertTrue(countAsyncHttpClientThreads() - baselineAsyncThreads <= config.getIoThreadsCount() + 4,
                        "async transport created per-request threads");
            }
        }
    }

    @Test
    void shouldCompleteFiveHundredSseStreamsWithBoundedSharedThreads() throws Exception {
        AtomicInteger requestCount = new AtomicInteger();
        long baselineAsyncThreads = countAsyncHttpClientThreads();
        try (NonBlockingFixture server = new NonBlockingFixture(requestCount, true)) {
            OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
            config.setBaseUrl("http://127.0.0.1:" + server.port());
            config.setStartupCheckEnabled(false);
            config.setIoThreadsCount(4);
            config.setMaxRequests(1_024);
            config.setMaxRequestsPerHost(512);
            config.setStreamCorePoolSize(8);
            config.setStreamMaxPoolSize(8);

            ChatRequest request = ChatRequest.builder()
                    .agent("openclaw/test")
                    .messages(Collections.singletonList(ChatMessage.ofUser("ping")))
                    .build();
            try (OpenClawClient client = new OpenClawClient(config)) {
                List<CompletableFuture<?>> streams = new ArrayList<>(CONCURRENCY);
                for (int index = 0; index < CONCURRENCY; index++) {
                    StreamingChatResponse stream = client.chat().chatCompletionStream(request);
                    streams.add(stream);
                }
                CompletableFuture.allOf(streams.toArray(new CompletableFuture<?>[0]))
                        .get(30, TimeUnit.SECONDS);

                assertEquals(CONCURRENCY, requestCount.get());
                assertTrue(countThreads("openclaw-sse-consumer-") <= config.getStreamMaxPoolSize());
                assertTrue(countAsyncHttpClientThreads() - baselineAsyncThreads <= config.getIoThreadsCount() + 4);
            }
        }
    }

    private long countAsyncHttpClientThreads() {
        return Thread.getAllStackTraces().keySet().stream()
                .map(Thread::getName)
                .filter(name -> name.contains("AsyncHttpClient"))
                .count();
    }

    private long countThreads(String prefix) {
        return Thread.getAllStackTraces().keySet().stream()
                .map(Thread::getName)
                .filter(name -> name.startsWith(prefix))
                .count();
    }

    /** 基于 Netty 的非阻塞本地夹具，避免测试服务器自身成为 500 并发瓶颈。 */
    private static final class NonBlockingFixture implements AutoCloseable {
        private final EventLoopGroup boss = new NioEventLoopGroup(1);
        private final EventLoopGroup workers = new NioEventLoopGroup(4);
        private final Channel channel;

        private NonBlockingFixture(AtomicInteger requestCount, boolean sse) throws InterruptedException {
            this.channel = new ServerBootstrap()
                    .group(boss, workers)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1_024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socket) {
                            socket.pipeline().addLast(new HttpServerCodec(), new HttpObjectAggregator(1_048_576),
                                    new SimpleChannelInboundHandler<FullHttpRequest>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext context, FullHttpRequest request) {
                                            int index = requestCount.incrementAndGet();
                                            byte[] body = (sse ? "data: [DONE]\n\n" : "{\"id\":\"chatcmpl-" + index
                                                    + "\",\"model\":\"openclaw/test\",\"choices\":[{\"index\":0,"
                                                    + "\"message\":{\"role\":\"assistant\",\"content\":\"ok\"},"
                                                    + "\"finish_reason\":\"stop\"}]}").getBytes(StandardCharsets.UTF_8);
                                            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(body));
                                            response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                                                    sse ? "text/event-stream" : "application/json");
                                            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, body.length);
                                            boolean keepAlive = HttpUtil.isKeepAlive(request);
                                            if (keepAlive) {
                                                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                            }
                                            context.writeAndFlush(response).addListener(future -> {
                                                if (!keepAlive) {
                                                    context.close();
                                                }
                                            });
                                        }
                                    });
                        }
                    }).bind("127.0.0.1", 0).sync().channel();
        }

        private int port() {
            return ((InetSocketAddress) channel.localAddress()).getPort();
        }

        @Override
        public void close() {
            channel.close().awaitUninterruptibly();
            boss.shutdownGracefully().awaitUninterruptibly();
            workers.shutdownGracefully().awaitUninterruptibly();
        }
    }
}
