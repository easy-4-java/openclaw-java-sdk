package io.github.easy4j.openclaw;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatRequest;
import io.github.easy4j.openclaw.api.model.ChatResponse;
import io.github.easy4j.openclaw.api.model.ThinkOption;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JacksonCompatibilityTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void preservesGatewayRequestNamesUnicodeAndNullOmission() throws Exception {
        ChatRequest request = ChatRequest.builder()
                .model("openclaw/default")
                .messages(List.of(ChatMessage.ofUser("你好 🌍")))
                .maxCompletionTokens(128).build();
        var json = mapper.readTree(mapper.writeValueAsBytes(request));
        assertEquals(128, json.get("max_completion_tokens").asInt());
        assertEquals("你好 🌍", json.at("/messages/0/content").asText());
        assertFalse(json.has("maxCompletionTokens"));
        assertFalse(json.has("temperature"));
    }

    @Test
    void readsForwardCompatibleGatewayResponseAndSnakeCase() throws Exception {
        ChatResponse response = mapper.readValue("""
                {"id":"chat-1","future_field":{},"choices":[
                  {"index":0,"finish_reason":"stop","future_choice":true,
                   "message":{"role":"assistant","content":"完成"}}]}
                """, ChatResponse.class);
        assertEquals("chat-1", response.getId());
        assertTrue(response.getChoices().get(0).isStop());
        assertEquals("完成", response.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void preservesCustomThinkBooleanAndLevelCodecsAndRejectsInvalidTokens() throws Exception {
        for (String json : List.of("true", "false", "\"low\"", "\"medium\"", "\"high\"")) {
            ThinkOption option = mapper.readValue(json, ThinkOption.class);
            assertEquals(json, mapper.writeValueAsString(option));
        }
        assertNull(mapper.readValue("null", ThinkOption.class));
        assertThrows(IOException.class, () -> mapper.readValue("{}", ThinkOption.class));
        assertThrows(IllegalArgumentException.class, () -> mapper.readValue("\"invalid\"", ThinkOption.class));
    }
}
