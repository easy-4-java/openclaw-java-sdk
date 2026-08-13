package io.github.easy4j.openclaw.api.model;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * 思考配置的联合类型，可序列化为布尔值或等级字符串。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonSerialize(using = ThinkOption.ThinkOptionSerializer.class)
@JsonDeserialize(using = ThinkOption.ThinkOptionDeserializer.class)
public interface ThinkOption {

    Object toJsonValue();

    static class ThinkBoolean implements ThinkOption {
        public static final ThinkBoolean ENABLED = new ThinkBoolean(true);
        public static final ThinkBoolean DISABLED = new ThinkBoolean(false);
        private final boolean enabled;

        public ThinkBoolean(boolean enabled) { this.enabled = enabled; }
        public boolean isEnabled() { return enabled; }
        @Override public Object toJsonValue() { return enabled; }
    }

    static class ThinkLevel implements ThinkOption {
        private static final List<String> VALID = java.util.Arrays.asList(
                "off", "minimal", "low", "medium", "high", "xhigh", "adaptive", "max", "ultra");
        public static final ThinkLevel OFF = new ThinkLevel("off");
        public static final ThinkLevel MINIMAL = new ThinkLevel("minimal");
        public static final ThinkLevel LOW = new ThinkLevel("low");
        public static final ThinkLevel MEDIUM = new ThinkLevel("medium");
        public static final ThinkLevel HIGH = new ThinkLevel("high");
        public static final ThinkLevel XHIGH = new ThinkLevel("xhigh");
        public static final ThinkLevel ADAPTIVE = new ThinkLevel("adaptive");
        public static final ThinkLevel MAX = new ThinkLevel("max");
        public static final ThinkLevel ULTRA = new ThinkLevel("ultra");
        private final String level;

        public ThinkLevel(String level) {
            if (level != null && !VALID.contains(level))
                throw new IllegalArgumentException("think level must be one of " + VALID + ", got: " + level);
            this.level = level;
        }
        public String getLevel() { return level; }
        @Override public Object toJsonValue() { return level; }
    }

    class ThinkOptionSerializer extends ValueSerializer<ThinkOption> {
        @Override
        public void serialize(ThinkOption value, JsonGenerator gen, SerializationContext serializers)
                throws JacksonException {
            if (value == null) gen.writeNull();
            else serializers.writeValue(gen, value.toJsonValue());
        }
    }

    class ThinkOptionDeserializer extends ValueDeserializer<ThinkOption> {
        @Override
        public ThinkOption deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            JsonToken token = p.currentToken();
            if (token == JsonToken.VALUE_TRUE) return ThinkBoolean.ENABLED;
            if (token == JsonToken.VALUE_FALSE) return ThinkBoolean.DISABLED;
            if (token == JsonToken.VALUE_STRING) return new ThinkLevel(p.getValueAsString());
            if (token == JsonToken.VALUE_NULL) return null;
            throw new IllegalStateException("Cannot deserialize ThinkOption from token: " + token);
        }
    }
}
