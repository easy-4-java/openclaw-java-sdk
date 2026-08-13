package io.github.easy4j.openclaw.api.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
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

    /**
     * 返回 Jackson 应直接写入 JSON 的布尔值或等级字符串。
     *
     * @return 用于 JSON 序列化的布尔值或等级字符串
     */
    Object toJsonValue();

    /**
     * 以 JSON 布尔值表示的思考开关。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    static class ThinkBoolean implements ThinkOption {
        /**
         * 以 JSON 布尔值 true 表示启用思考的共享不可变选项。
         */
        public static final ThinkBoolean ENABLED = new ThinkBoolean(true);
        /**
         * 以 JSON 布尔值 false 表示禁用思考的共享不可变选项。
         */
        public static final ThinkBoolean DISABLED = new ThinkBoolean(false);
        /**
         * JSON 属性 {@code enabled}，表示是否启用。
         */
        private final boolean enabled;
        /**
         * 构造以 JSON 布尔值表示的思考开关。
         *
         * @param enabled 是否启用对应能力
         */
        public ThinkBoolean(boolean enabled) { this.enabled = enabled; }
        /**
         * 返回布尔形式的思考开关值。
         *
         * @return 是否启用模型思考过程
         */
        public boolean isEnabled() { return enabled; }
        /**
         * 返回 Jackson 应直接写入 JSON 的布尔值或等级字符串。
         *
         * @return 用于 JSON 序列化的布尔值或等级字符串
         */
        @Override public Object toJsonValue() { return enabled; }
    }

    /**
     * 以 OpenClaw 标准思考等级表示的思考强度。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    static class ThinkLevel implements ThinkOption {
        /**
         * ThinkLevel 接受的标准化等级集合。
         */
        private static final List<String> VALID = java.util.Arrays.asList(
                "off", "minimal", "low", "medium", "high", "xhigh", "adaptive", "max", "ultra");
        /**
         * 禁用思考的共享选项。
         */
        public static final ThinkLevel OFF = new ThinkLevel("off");
        /**
         * 最小思考强度共享选项。
         */
        public static final ThinkLevel MINIMAL = new ThinkLevel("minimal");
        /**
         * 低思考强度共享选项。
         */
        public static final ThinkLevel LOW = new ThinkLevel("low");
        /**
         * 中等思考强度共享选项。
         */
        public static final ThinkLevel MEDIUM = new ThinkLevel("medium");
        /**
         * 高思考强度共享选项。
         */
        public static final ThinkLevel HIGH = new ThinkLevel("high");
        /**
         * 超高思考强度共享选项。
         */
        public static final ThinkLevel XHIGH = new ThinkLevel("xhigh");
        /**
         * 由提供方动态管理思考预算的共享选项。
         */
        public static final ThinkLevel ADAPTIVE = new ThinkLevel("adaptive");
        /**
         * 最大思考强度共享选项。
         */
        public static final ThinkLevel MAX = new ThinkLevel("max");
        /**
         * 最大思考与主动子智能体编排强度共享选项。
         */
        public static final ThinkLevel ULTRA = new ThinkLevel("ultra");
        /**
         * JSON 属性 {@code level}，表示等级。
         */
        private final String level;
        /**
         * 构造 OpenClaw 支持的字符串思考等级。
         *
         * @param level 思考强度等级
         * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
         */
        public ThinkLevel(String level) {
            if (level != null && !VALID.contains(level))
                throw new IllegalArgumentException("think level must be one of " + VALID + ", got: " + level);
            this.level = level;
        }
        /**
         * 返回标准化后的思考强度等级。
         *
         * @return 经校验的小写思考强度等级
         */
        public String getLevel() { return level; }
        /**
         * 返回 Jackson 应直接写入 JSON 的布尔值或等级字符串。
         *
         * @return 用于 JSON 序列化的布尔值或等级字符串
         */
        @Override public Object toJsonValue() { return level; }
    }

    /**
     * 把 ThinkOption 直接写为 JSON 布尔值或字符串的 Jackson 序列化器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    class ThinkOptionSerializer extends JsonSerializer<ThinkOption> {
        /**
         * 把 ThinkOption 的布尔值或等级写入 JSON，不引入额外包装字段。
         *
         * @param value 待写入 JSON 的思考选项
         * @param gen Jackson 输出当前值的 JSON 生成器
         * @param serializers Jackson 当前序列化上下文
         * @throws IOException 网络、流或子进程 I/O 失败时抛出
         */
        @Override
        public void serialize(ThinkOption value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value == null) gen.writeNull();
            else gen.writeObject(value.toJsonValue());
        }
    }

    /**
     * 从 JSON 布尔值或等级字符串恢复 ThinkOption 的 Jackson 反序列化器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    class ThinkOptionDeserializer extends JsonDeserializer<ThinkOption> {
        /**
         * 从 JSON 布尔值或字符串恢复对应 ThinkOption 实现，非法类型由 Jackson 报错。
         *
         * @param p 指向待反序列化 JSON 值的解析器
         * @param ctxt Jackson 当前反序列化上下文
         * @return 由 JSON 布尔值或等级字符串恢复的思考选项
         * @throws IOException 网络、流或子进程 I/O 失败时抛出
         */
        @Override
        public ThinkOption deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken token = p.currentToken();
            if (token == JsonToken.VALUE_TRUE) return ThinkBoolean.ENABLED;
            if (token == JsonToken.VALUE_FALSE) return ThinkBoolean.DISABLED;
            if (token == JsonToken.VALUE_STRING) return new ThinkLevel(p.getValueAsString());
            if (token == JsonToken.VALUE_NULL) return null;
            throw new IOException("Cannot deserialize ThinkOption from token: " + token);
        }
    }
}
