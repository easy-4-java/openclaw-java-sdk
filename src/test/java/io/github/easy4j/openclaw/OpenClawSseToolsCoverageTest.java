package io.github.easy4j.openclaw;

import io.github.easy4j.openclaw.api.model.ChatChunk;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ResponseRequest;
import io.github.easy4j.openclaw.api.model.Tools;
import io.github.easy4j.openclaw.api.sse.SseEvent;
import io.github.easy4j.openclaw.api.sse.SseEventAccumulator;
import io.github.easy4j.openclaw.api.sse.SseEventHandler;
import io.github.easy4j.openclaw.api.sse.SseStreamReader;
import io.github.easy4j.openclaw.api.sse.StreamingChatResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenClawSseToolsCoverageTest {

    @Test
    void shouldAccumulateTextToolCallsAndCallbacks() {
        ChatMessage.ToolCall firstToolPart = ChatMessage.ToolCall.of("call-1", "weather", "{\"city\":");
        ChatMessage.ToolCall secondToolPart = ChatMessage.ToolCall.of(null, null, "\"Beijing\"}");
        ChatChunk first = chunk("id", "model", "assistant", "hello ", firstToolPart, null);
        ChatChunk second = chunk(null, null, null, "world", secondToolPart, "tool_calls");
        ChatChunk empty = new ChatChunk();

        SseEventAccumulator accumulator = new SseEventAccumulator();
        accumulator.merge(empty);
        accumulator.merge(first);
        accumulator.merge(second);
        assertEquals("hello world", accumulator.getAccumulatedContent());
        assertTrue(accumulator.isToolCall());
        assertTrue(accumulator.isComplete());
        ChatChunk accumulated = accumulator.getAccumulated();
        assertEquals("id", accumulated.getId());
        assertEquals("{\"city\":\"Beijing\"}", accumulated.getChoices().get(0).getDelta()
                .getToolCalls().get(0).getFunction().getArguments());
        accumulator.reset();
        assertEquals("", accumulator.getAccumulatedContent());
        assertFalse(accumulator.isComplete());
        assertNull(accumulator.getAccumulated().getChoices().get(0).getDelta().getContent());

        AtomicReference<String> delta = new AtomicReference<>();
        AtomicReference<String> completed = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        AtomicInteger chunks = new AtomicInteger();
        AtomicInteger toolCalls = new AtomicInteger();
        StreamingChatResponse stream = StreamingChatResponse.builder()
                .onDelta(delta::set).onChunk(ignored -> chunks.incrementAndGet())
                .onToolCall(ignored -> toolCalls.incrementAndGet()).onComplete(completed::set)
                .onError(error::set).build();
        stream.onEvent(SseEvent.terminal());
        stream.onEvent(SseEvent.of(null, "unparsed"));
        SseEvent parsed = SseEvent.of("message", "{}");
        parsed.setParsed(first);
        stream.onEvent(parsed);
        stream.onComplete();
        assertEquals("hello ", delta.get());
        assertEquals("hello ", completed.get());
        assertEquals(1, chunks.get());
        assertEquals(1, toolCalls.get());

        StreamingChatResponse failed = new StreamingChatResponse().onError(error::set);
        failed.setDeltaConsumer(delta::set);
        failed.onError(new IllegalStateException("failed"));
        assertEquals("failed", error.get().getMessage());
    }

    @Test
    void shouldReadAllSseFormatsAndFailures() {
        String input = ":comment\nretry: 1000\nevent: response.delta\n"
                + "data: {\"id\":\"1\",\"choices\":[]}\n\n"
                + "data: invalid-json\n\n"
                + "data: [DONE]\n\n";
        RecordingHandler handler = new RecordingHandler();
        SseStreamReader reader = new SseStreamReader();
        reader.readChatCompletionStream(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), handler);
        assertEquals(2, handler.events.size());
        assertEquals("response.delta", handler.events.get(0).getEvent());
        assertNotNull(handler.events.get(0).getParsed());
        assertNull(handler.events.get(1).getParsed());
        assertEquals(1, handler.completed);

        RecordingHandler raw = new RecordingHandler();
        reader.readResponseStream(new ByteArrayInputStream("data: {\"value\":1}\n".getBytes(StandardCharsets.UTF_8)), raw);
        assertEquals(1, raw.events.size());
        assertEquals(1, raw.completed);
        reader.readStream(null, raw, ChatChunk.class);
        assertNotNull(raw.error);

        SseEventHandler throwing = new SseEventHandler() {
            @Override public void onEvent(SseEvent event) { throw new IllegalStateException("handler failed"); }
            @Override public void onComplete() { }
            @Override public void onError(Throwable error) { }
        };
        reader.readResponseStream(new ByteArrayInputStream("data: {}\n\n".getBytes(StandardCharsets.UTF_8)), throwing);
    }

    @Test
    void shouldBuildToolsParseArgumentsAndCreateResponseInputs() {
        Map<String, Object> tool = Tools.function("weather", "Get weather")
                .param("city", "string", "City")
                .param("units", "string", "Units", true)
                .build();
        assertEquals("function", tool.get("type"));
        assertThrows(NullPointerException.class, () -> Tools.function(null, "description"));

        ChatMessage.ToolCall call = ChatMessage.ToolCall.of("call", "weather", "{\"city\":\"Beijing\"}");
        assertEquals("Beijing", Tools.parseArgsAsMap(call).get("city"));
        call.getFunction().setArguments("");
        assertTrue(Tools.parseArgsAsMap(call).isEmpty());
        call.getFunction().setArguments("invalid");
        assertThrows(IllegalArgumentException.class, () -> Tools.parseArgsAsMap(call));
        assertThrows(NullPointerException.class, () -> Tools.parseArgs(null, Map.class));

        ChatMessage message = ChatMessage.ofAssistant(null, Arrays.asList(call));
        assertTrue(Tools.hasToolCalls(message));
        assertFalse(Tools.hasToolCalls(null));
        assertTrue(Tools.isToolCallFinish("tool_calls"));
        assertEquals(1, Tools.extractToolCalls(message).size());
        assertTrue(Tools.extractToolCalls(ChatMessage.ofUser("hello")).isEmpty());
        assertEquals("plain", Tools.toolResult("id", "plain").getContent());
        assertTrue(Tools.toolResult("id", Collections.singletonMap("ok", true)).getContent().contains("true"));

        assertEquals("message", ResponseRequest.InputItem.message().role("user").content("hello").build().getType());
        assertEquals("function_call_output", ResponseRequest.InputItem.functionCallOutput()
                .callId("call").output("result").build().getType());
        assertEquals("input_image", ResponseRequest.InputItem.imageSource("url", "http://image").build().getType());
        assertEquals("url", ResponseRequest.InputItem.imageUrl("http://image").build().getSource().getType());
        assertEquals("base64", ResponseRequest.InputItem.imageBase64("data").build().getSource().getType());
        assertEquals("input_file", ResponseRequest.InputItem.fileSource("url", "http://file", "text/plain").build().getType());
        assertNull(ResponseRequest.InputItem.fileUrl("http://file").build().getSource().getMediaType());
        assertEquals("text/plain", ResponseRequest.InputItem.fileUrl("http://file", "text/plain").build().getSource().getMediaType());
        assertNull(ResponseRequest.InputItem.fileBase64("data").build().getSource().getMediaType());
        assertEquals("text/plain", ResponseRequest.InputItem.fileBase64("data", "text/plain").build().getSource().getMediaType());
    }

    private ChatChunk chunk(String id, String model, String role, String content,
                            ChatMessage.ToolCall toolCall, String finishReason) {
        ChatChunk.DeltaMessage delta = new ChatChunk.DeltaMessage();
        delta.setRole(role);
        delta.setContent(content);
        delta.setToolCalls(toolCall == null ? null : Arrays.asList(toolCall));
        ChatChunk.DeltaChoice choice = new ChatChunk.DeltaChoice();
        choice.setIndex(0);
        choice.setDelta(delta);
        choice.setFinishReason(finishReason);
        ChatChunk chunk = new ChatChunk();
        chunk.setId(id);
        chunk.setModel(model);
        chunk.setChoices(Arrays.asList(choice));
        return chunk;
    }

    private static final class RecordingHandler implements SseEventHandler {
        private final List<SseEvent> events = new ArrayList<>();
        private int completed;
        private Throwable error;
        @Override public void onEvent(SseEvent event) { events.add(event); }
        @Override public void onComplete() { completed++; }
        @Override public void onError(Throwable error) { this.error = error; }
    }
}
