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
 * OpenClaw JSON 协议中的 `ThinkOption` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonSerialize(using = ThinkOption.ThinkOptionSerializer.class)
@JsonDeserialize(using = ThinkOption.ThinkOptionDeserializer.class)
public interface ThinkOption {

    /**
     * 根据 OpenClaw JSON 语义构造、提取或更新 `ThinkOption` 中的 `toJsonValue` 数据。
     *
     * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
     */
    Object toJsonValue();

    /**
     * OpenClaw JSON 协议中的 `ThinkBoolean` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    static class ThinkBoolean implements ThinkOption {
        /**
         * OpenClaw 协议固定值 {@code new ThinkBoolean(true)}；调用方不应在运行时修改。
         */
        public static final ThinkBoolean ENABLED = new ThinkBoolean(true);
        /**
         * OpenClaw 协议固定值 {@code new ThinkBoolean(false)}；调用方不应在运行时修改。
         */
        public static final ThinkBoolean DISABLED = new ThinkBoolean(false);
        /**
         * 映射 OpenClaw JSON 字段 `enabled` 的 布尔开关。
         */
        private final boolean enabled;
        /**
         * 按协议字段创建 `ThinkBoolean`，供 Jackson 序列化、反序列化或调用方读取。
         *
         * @param enabled 写入 `enabled` 协议字段的内容
         */
        public ThinkBoolean(boolean enabled) { this.enabled = enabled; }
        /**
         * 判断 `enabled` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isEnabled() { return enabled; }
        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `ThinkBoolean` 中的 `toJsonValue` 数据。
         *
         * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
         */
        @Override public Object toJsonValue() { return enabled; }
    }

    /**
     * OpenClaw JSON 协议中的 `ThinkLevel` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    static class ThinkLevel implements ThinkOption {
        /**
         * OpenClaw 协议固定值 {@code java.util.Arrays.asList("low", "medium", "high")}；调用方不应在运行时修改。
         */
        private static final List<String> VALID = java.util.Arrays.asList("low", "medium", "high");
        /**
         * OpenClaw 协议固定值 {@code new ThinkLevel("low")}；调用方不应在运行时修改。
         */
        public static final ThinkLevel LOW = new ThinkLevel("low");
        /**
         * OpenClaw 协议固定值 {@code new ThinkLevel("medium")}；调用方不应在运行时修改。
         */
        public static final ThinkLevel MEDIUM = new ThinkLevel("medium");
        /**
         * OpenClaw 协议固定值 {@code new ThinkLevel("high")}；调用方不应在运行时修改。
         */
        public static final ThinkLevel HIGH = new ThinkLevel("high");
        /**
         * 映射 OpenClaw JSON 字段 `level` 的 协议内容。
         */
        private final String level;
        /**
         * 按协议字段创建 `ThinkLevel`，供 Jackson 序列化、反序列化或调用方读取。
         *
         * @param level 写入 `level` 协议字段的内容
         * @throws IllegalArgumentException 必填参数缺失、格式错误或超出范围时抛出
         */
        public ThinkLevel(String level) {
            if (level != null && !VALID.contains(level))
                throw new IllegalArgumentException("think level must be one of " + VALID + ", got: " + level);
            this.level = level;
        }
        /**
         * 读取当前对象保存的 `level` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getLevel() { return level; }
        /**
         * 根据 OpenClaw JSON 语义构造、提取或更新 `ThinkLevel` 中的 `toJsonValue` 数据。
         *
         * @return 按声明类型解析的值；ThinkOption 标量保持布尔或字符串形式
         */
        @Override public Object toJsonValue() { return level; }
    }

    /**
     * OpenClaw JSON 协议中的 `ThinkOptionSerializer` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    class ThinkOptionSerializer extends JsonSerializer<ThinkOption> {
        /**
         * 把 ThinkOption 的布尔值或等级写入 JSON，不引入额外包装字段。
         *
         * @param value 写入 `value` 协议字段的内容
         * @param gen 写入 `gen` 协议字段的内容
         * @param serializers 写入 `serializers` 协议字段的内容
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
     * OpenClaw JSON 协议中的 `ThinkOptionDeserializer` 数据结构；字段名和嵌套关系与 Gateway 请求或响应保持一致。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    class ThinkOptionDeserializer extends JsonDeserializer<ThinkOption> {
        /**
         * 从 JSON 布尔值或字符串恢复对应 ThinkOption 实现，非法类型由 Jackson 报错。
         *
         * @param p 写入 `p` 协议字段的内容
         * @param ctxt 写入 `ctxt` 协议字段的内容
         * @return 按当前参数创建、查询或解析得到的 ThinkOption
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
