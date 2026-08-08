package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static io.github.easy4j.openclaw.Java8Collections.list;
import static io.github.easy4j.openclaw.Java8Collections.map;

/**
 * CLI 选项的完整命令生成契约测试。
 *
 * <p>逐个执行高复杂度选项 Builder 的所有公开配置入口，并在每次状态变化后生成 argv，
 * 从而验证每种子命令都能稳定生成非空、不可变且不含 null 的参数列表。</p>
 */
class CliOptionsComprehensiveCoverageTest {

    private static final List<Class<?>> OPTION_TYPES = list(
            PluginsOptions.class,
            ChannelsOptions.class,
            AgentsOptions.class,
            ModelsOptions.class,
            SkillsOptions.class,
            HooksOptions.class,
            DevicesOptions.class,
            NodesOptions.class,
            DaemonOptions.class,
            CronOptions.class,
            ApprovalsOptions.class,
            SystemOptions.class,
            LogsOptions.class,
            SecretsOptions.class,
            McpOptions.class,
            DirectoryOptions.class,
            DoctorOptions.class,
            WorktreesOptions.class,
            HealthCommandOptions.class
    );

    @Test
    void everyPublicBuilderPathMustGenerateValidImmutableArguments() throws Exception {
        for (Class<?> optionType : OPTION_TYPES) {
            exerciseBuilder(optionType);
        }
    }

    private void exerciseBuilder(Class<?> optionType) throws Exception {
        Object builder = optionType.getMethod("builder").invoke(null);
        Method build = builder.getClass().getMethod("build");
        Method[] methods = builder.getClass().getDeclaredMethods();

        // 先填充所有字段，满足含必填字段的 Builder，再逐个切换子命令并生成参数。
        for (Method method : methods) {
            if (isBuilderConfigurationMethod(method)) {
                invokeBuilderMethod(builder, method, null);
            }
        }

        assertArguments(build.invoke(builder), optionType);
        for (Method method : methods) {
            if (!isBuilderConfigurationMethod(method)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].isEnum()) {
                for (Object enumValue : parameterTypes[0].getEnumConstants()) {
                    invokeBuilderMethod(builder, method, enumValue);
                    assertArguments(build.invoke(builder), optionType);
                }
            } else {
                invokeBuilderMethod(builder, method, null);
                assertArguments(build.invoke(builder), optionType);
            }
        }
    }

    private boolean isBuilderConfigurationMethod(Method method) {
        return Modifier.isPublic(method.getModifiers())
                && !method.isSynthetic()
                && !method.getName().equals("build");
    }

    private void invokeBuilderMethod(Object builder, Method method, Object enumOverride) throws Exception {
        Object[] arguments = new Object[method.getParameterCount()];
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int index = 0; index < parameterTypes.length; index++) {
            arguments[index] = enumOverride != null ? enumOverride : sampleValue(parameterTypes[index]);
        }
        try {
            method.invoke(builder, arguments);
        } catch (InvocationTargetException exception) {
            throw new AssertionError("Builder method failed: " + method, exception.getCause());
        }
    }

    private Object sampleValue(Class<?> type) throws Exception {
        if (type == String.class) {
            return "value";
        }
        if (type == boolean.class || type == Boolean.class) {
            return true;
        }
        if (type == int.class || type == Integer.class) {
            return 10;
        }
        if (type == long.class || type == Long.class) {
            return 10L;
        }
        if (type.isEnum()) {
            return type.getEnumConstants()[0];
        }
        if (type.isArray()) {
            Object values = Array.newInstance(type.getComponentType(), 1);
            Array.set(values, 0, sampleValue(type.getComponentType()));
            return values;
        }
        if (List.class.isAssignableFrom(type)) {
            return list("value");
        }
        if (Map.class.isAssignableFrom(type)) {
            return map("key", "value");
        }
        try {
            Object nestedBuilder = type.getMethod("builder").invoke(null);
            for (Method method : nestedBuilder.getClass().getDeclaredMethods()) {
                if (isBuilderConfigurationMethod(method)) {
                    invokeBuilderMethod(nestedBuilder, method, null);
                }
            }
            return nestedBuilder.getClass().getMethod("build").invoke(nestedBuilder);
        } catch (NoSuchMethodException exception) {
            throw new AssertionError("Unsupported builder parameter type: " + type.getName(), exception);
        }
    }

    @SuppressWarnings("unchecked")
    private void assertArguments(Object option, Class<?> optionType) {
        assertNotNull(option, optionType.getName());
        CliSubArgs cliSubArgs = (CliSubArgs) option;
        List<String> arguments = cliSubArgs.toSubcommandArguments();
        assertNotNull(arguments, optionType.getName());
        assertFalse(arguments.stream().anyMatch(java.util.Objects::isNull), optionType.getName());
        assertThrows(UnsupportedOperationException.class, () -> arguments.add("must-fail"), optionType.getName());
    }
}
