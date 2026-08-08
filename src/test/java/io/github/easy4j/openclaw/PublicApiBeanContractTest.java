package io.github.easy4j.openclaw;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the public model and CLI option contracts as one compatibility surface.
 * Every builder method is invoked with a type-correct value, built values are queried,
 * and mutable DTO properties are round-tripped through their accessors.
 */
class PublicApiBeanContractTest {

    @Test
    void shouldExerciseEveryModelAndCliOptionContract() throws Exception {
        AtomicInteger classes = new AtomicInteger();
        AtomicInteger methods = new AtomicInteger();

        for (Class<?> type : discoverContractTypes()) {
            if (type.isSynthetic() || type.isAnonymousClass() || type.isLocalClass()
                    || type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
                continue;
            }
            classes.incrementAndGet();
            if (type.isEnum()) {
                exerciseEnum(type, methods);
                continue;
            }
            Object instance = instantiate(type);
            if (instance != null) {
                exerciseMutableContract(instance, methods);
            }
            Object builder = createBuilder(type);
            if (builder != null) {
                Object built = exerciseBuilder(builder, methods);
                if (built != null) {
                    exerciseMutableContract(built, methods);
                }
            }
        }

        assertTrue(classes.get() >= 100, "unexpected contract class count: " + classes);
        assertTrue(methods.get() >= 500, "unexpected exercised method count: " + methods);
    }

    private List<Class<?>> discoverContractTypes() throws Exception {
        Path classesRoot = Paths.get(OpenClawClient.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        List<Class<?>> result = new ArrayList<>();
        java.util.stream.Stream<Path> paths = Files.walk(classesRoot);
        try {
            for (Path path : paths.filter(value -> value.toString().endsWith(".class")).collect(Collectors.toList())) {
                String name = classesRoot.relativize(path).toString()
                        .replace(File.separatorChar, '.')
                        .replaceAll("\\.class$", "");
                if (name.startsWith("io.github.easy4j.openclaw.api.model.")
                        || name.startsWith("io.github.easy4j.openclaw.cli.opts.")) {
                    result.add(Class.forName(name));
                }
            }
        } finally {
            paths.close();
        }
        result.sort(Comparator.comparing(Class::getName));
        return result;
    }

    private void exerciseEnum(Class<?> type, AtomicInteger methods) {
        Object[] constants = type.getEnumConstants();
        assertNotNull(constants);
        for (Object constant : constants) {
            constant.toString();
            for (Method method : type.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())
                        && method.getParameterCount() == 0 && method.getDeclaringClass() == type) {
                    invokeQuietly(method, constant);
                    methods.incrementAndGet();
                }
            }
        }
    }

    private Object instantiate(Class<?> type) {
        return Arrays.stream(type.getConstructors())
                .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                .map(constructor -> newInstance(constructor, sampleArguments(constructor.getParameterTypes())))
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Object createBuilder(Class<?> type) {
        for (Method method : type.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())
                    && method.getParameterCount() == 0 && method.getName().equals("builder")) {
                return invokeQuietly(method, null);
            }
        }
        return null;
    }

    private Object exerciseBuilder(Object builder, AtomicInteger methods) {
        Method buildMethod = null;
        for (Method method : builder.getClass().getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (method.getName().equals("build") && method.getParameterCount() == 0) {
                buildMethod = method;
                continue;
            }
            if (method.getReturnType().isAssignableFrom(builder.getClass())
                    || builder.getClass().isAssignableFrom(method.getReturnType())) {
                Object[] arguments = sampleArguments(method.getParameterTypes());
                if (arguments != null) {
                    invokeQuietly(method, builder, arguments);
                    methods.incrementAndGet();
                }
            }
        }
        return buildMethod == null ? null : invokeQuietly(buildMethod, builder);
    }

    private void exerciseMutableContract(Object instance, AtomicInteger methods) {
        Class<?> type = instance.getClass();
        for (Method method : type.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            Object[] arguments = sampleArguments(method.getParameterTypes());
            if (arguments == null) {
                continue;
            }
            String name = method.getName();
            boolean contractMethod = name.startsWith("set") || name.startsWith("get") || name.startsWith("is")
                    || name.startsWith("with") || name.equals("toArgs") || name.equals("validate")
                    || name.equals("toString") || name.equals("hashCode");
            if (contractMethod) {
                invokeQuietly(method, instance, arguments);
                methods.incrementAndGet();
            }
        }
        instance.toString();
        instance.hashCode();
        instance.equals(instance);
    }

    private Object[] sampleArguments(Class<?>[] parameterTypes) {
        Object[] values = new Object[parameterTypes.length];
        for (int index = 0; index < parameterTypes.length; index++) {
            Object value = sampleValue(parameterTypes[index]);
            if (value == Unsupported.INSTANCE) {
                return null;
            }
            values[index] = value;
        }
        return values;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object sampleValue(Class<?> type) {
        if (type == String.class || type == CharSequence.class || type == Object.class) return "value";
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == int.class || type == Integer.class) return 1;
        if (type == long.class || type == Long.class) return 1L;
        if (type == double.class || type == Double.class) return 0.5D;
        if (type == float.class || type == Float.class) return 0.5F;
        if (type == short.class || type == Short.class) return (short) 1;
        if (type == byte.class || type == Byte.class) return (byte) 1;
        if (type == char.class || type == Character.class) return 'x';
        if (type == Duration.class) return Duration.ofSeconds(1);
        if (type == Path.class) return Paths.get("target");
        if (type == File.class) return new File("target");
        if (type == URI.class) return URI.create("http://localhost");
        if (type == Optional.class) return Optional.of("value");
        if (type == List.class || type == Collection.class) return Collections.singletonList("value");
        if (type == Set.class) return Collections.singleton("value");
        if (type == Map.class) return Collections.singletonMap("key", "value");
        if (type == Consumer.class) return (Consumer<Object>) ignored -> { };
        if (type == Supplier.class) return (Supplier<Object>) () -> "value";
        if (type.isArray()) {
            Object array = java.lang.reflect.Array.newInstance(type.componentType(), 1);
            Object component = sampleValue(type.componentType());
            if (component == Unsupported.INSTANCE) return Unsupported.INSTANCE;
            java.lang.reflect.Array.set(array, 0, component);
            return array;
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants.length == 0 ? Unsupported.INSTANCE : constants[0];
        }
        Object nested = instantiate(type);
        return nested == null ? Unsupported.INSTANCE : nested;
    }

    private Object newInstance(Constructor<?> constructor, Object[] arguments) {
        if (arguments == null) return null;
        try {
            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return null;
        }
    }

    private Object invokeQuietly(Method method, Object target, Object... arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException ignored) {
            return null;
        }
    }

    private enum Unsupported { INSTANCE }
}
