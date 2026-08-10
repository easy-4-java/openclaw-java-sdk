package io.github.easy4j.openclaw.api.sse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.exception.OpenClawHttpException;
import io.github.easy4j.openclaw.api.model.ChatChunk;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * SSE 协议读取器，识别事件边界与 data 行，将 JSON 数据解析为 ChatChunk，并在 [DONE] 时发出完成事件。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public class SseStreamReader {

    /**
     * SSE data 行表示流正常结束时使用的标记。
     */
    private static final String DONE_MARKER = "[DONE]";

    /**
     * 负责协议 JSON 序列化与反序列化的映射器。
     */
    private final ObjectMapper objectMapper;

    /**
     * 按给定配置创建 {@code SseStreamReader}，构造过程不隐式执行远程业务请求。
     *
     * @param objectMapper JSON 映射器
     */
    public SseStreamReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 按给定配置创建 {@code SseStreamReader}，构造过程不隐式执行远程业务请求。
     */
    public SseStreamReader() {
        this(null);
    }

    /**
     * 持续读取 SSE 输入，按空行切分事件，并把 data 内容交给事件处理器。
     *
     * @param <T> 方法使用的泛型类型
     * @param inputStream SSE 字节输入流
     * @param handler 事件处理器
     * @param chunkClass 反序列化流式片段使用的目标类型
     */
    public <T> void readStream(InputStream inputStream, SseEventHandler handler, Class<T> chunkClass) {
        if (inputStream == null) {
            handler.onError(new OpenClawHttpException("SSE input stream is null", null));
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String currentEvent = null;
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    // 空行表示事件边界，重置 event type
                    currentEvent = null;
                    continue;
                }

                if (line.startsWith("event:")) {
                    currentEvent = line.substring("event:".length()).trim();
                    continue;
                }

                if (line.startsWith("data:")) {
                    String data = line.substring("data:".length()).trim();

                    if (DONE_MARKER.equals(data)) {
                        handler.onComplete();
                        return;
                    }

                    SseEvent sseEvent = SseEvent.of(currentEvent, data);

                    // 尝试解析为 ChatCompletionChunk
                    if (chunkClass != null) {
                        try {
                            T chunk = objectMapper.readValue(data, chunkClass);
                            sseEvent.setParsed(chunk);
                        } catch (Exception parseEx) {
                            log.debug("Failed to parse SSE data as {}: {}", chunkClass.getSimpleName(), parseEx.getMessage());
                        }
                    }

                    try {
                        handler.onEvent(sseEvent);
                    } catch (Exception handlerEx) {
                        log.warn("SSE event handler threw exception: {}", handlerEx.getMessage(), handlerEx);
                    }
                }
                // 忽略其他行（如 id:, retry:, 注释）
            }

            // 流正常关闭但未收到 [DONE]
            handler.onComplete();
        } catch (Exception e) {
            log.error("SSE stream read error: {}", e.getMessage(), e);
            handler.onError(e);
        }
    }

    /**
     * 持续读取 SSE 输入，按空行切分事件，并把 data 内容交给事件处理器。
     *
     * @param inputStream SSE 字节输入流
     * @param handler 事件处理器
     */
    public void readChatCompletionStream(InputStream inputStream, SseEventHandler handler) {
        readStream(inputStream, handler, ChatChunk.class);
    }

    /**
     * 持续读取 SSE 输入，按空行切分事件，并把 data 内容交给事件处理器。
     *
     * @param inputStream SSE 字节输入流
     * @param handler 事件处理器
     */
    public void readResponseStream(InputStream inputStream, SseEventHandler handler) {
        readStream(inputStream, handler, null);
    }
}
