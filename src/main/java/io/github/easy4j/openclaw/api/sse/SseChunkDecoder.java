package io.github.easy4j.openclaw.api.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.model.ChatChunk;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可增量接收网络数据块的 SSE 解码器。
 *
 * <p>按换行字节切分后再进行 UTF-8 解码，能够正确处理跨网络数据块的多字节字符。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public final class SseChunkDecoder {

    private static final String DONE_MARKER = "[DONE]";

    private final ObjectMapper objectMapper;
    private final SseEventHandler handler;
    private final ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
    private final AtomicBoolean completed = new AtomicBoolean();
    private String currentEvent;

    public SseChunkDecoder(ObjectMapper objectMapper, SseEventHandler handler) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.handler = Objects.requireNonNull(handler, "handler");
    }

    /** 接收一个网络数据块。 */
    public void accept(byte[] bytes) {
        if (completed.get()) {
            return;
        }
        for (byte value : bytes) {
            if (value == '\n') {
                decodeLine();
            } else {
                lineBuffer.write(value);
            }
        }
    }

    /** 标记网络流正常结束。 */
    public void complete() {
        if (lineBuffer.size() > 0) {
            decodeLine();
        }
        completeOnce();
    }

    /** 标记网络流异常结束。 */
    public void fail(Throwable error) {
        if (completed.compareAndSet(false, true)) {
            handler.onError(error);
        }
    }

    private void decodeLine() {
        byte[] bytes = lineBuffer.toByteArray();
        lineBuffer.reset();
        int length = bytes.length > 0 && bytes[bytes.length - 1] == '\r' ? bytes.length - 1 : bytes.length;
        String line = new String(bytes, 0, length, StandardCharsets.UTF_8);
        if (line.isEmpty()) {
            currentEvent = null;
            return;
        }
        if (line.startsWith("event:")) {
            currentEvent = line.substring("event:".length()).trim();
            return;
        }
        if (!line.startsWith("data:")) {
            return;
        }
        String data = line.substring("data:".length()).trim();
        if (DONE_MARKER.equals(data)) {
            completeOnce();
            return;
        }
        try {
            SseEvent event = SseEvent.of(currentEvent, data);
            event.setParsed(objectMapper.readValue(data, ChatChunk.class));
            handler.onEvent(event);
        } catch (Exception error) {
            handler.onError(error);
            completed.set(true);
        }
    }

    private void completeOnce() {
        if (completed.compareAndSet(false, true)) {
            handler.onComplete();
        }
    }
}
