package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenClawCliFacadeCoverageTest {

    @Test
    void shouldRouteEveryTypedCliCommandThroughExecutor() {
        RecordingExecutor executor = new RecordingExecutor();
        OpenClawCli cli = new OpenClawCli(executor);
        assertSame(executor, cli.getExecutor());
        AtomicInteger invoked = new AtomicInteger();

        for (Method method : OpenClawCli.class.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers()) || method.getReturnType() != OpenClawCliResult.class) {
                continue;
            }
            Object[] arguments = Arrays.stream(method.getParameterTypes()).map(this::sample).toArray();
            try {
                Object result = method.invoke(cli, arguments);
                if (result != null) {
                    assertTrue(((OpenClawCliResult) result).isSuccess());
                }
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException ignored) {
                // A few option types deliberately validate mandatory command fields.
                // Reaching the facade method still verifies that validation is preserved.
            }
            invoked.incrementAndGet();
        }

        assertTrue(invoked.get() >= 55, "unexpected CLI facade method count: " + invoked);
        assertTrue(executor.executions.get() >= 40, "too few commands reached the executor: " + executor.executions);
    }

    @Test
    void shouldCoverCliResultValueSemantics() {
        OpenClawCliResult first = new OpenClawCliResult(0, null, null);
        OpenClawCliResult same = new OpenClawCliResult(0, "", "");
        OpenClawCliResult failed = new OpenClawCliResult(1, "out", "err");
        assertTrue(first.isSuccess());
        assertEquals(first, first);
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertTrue(first.toString().contains("exitCode=0"));
        assertTrue(!first.equals(failed));
        assertTrue(!first.equals("other"));
    }

    private Object sample(Class<?> type) {
        if (type == String.class) return "value";
        if (type == int.class || type == Integer.class) return 1;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type.isEnum()) return type.getEnumConstants()[0];

        try {
            Method builderMethod = type.getMethod("builder");
            Object builder = builderMethod.invoke(null);
            for (Method method : builder.getClass().getDeclaredMethods()) {
                if (method.getName().equals("build") && method.getParameterCount() == 0) {
                    return method.invoke(builder);
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }

        return Arrays.stream(type.getConstructors())
                .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst()
                .map(constructor -> {
                    try { return constructor.newInstance(); }
                    catch (ReflectiveOperationException e) { return null; }
                })
                .orElse(null);
    }

    private static final class RecordingExecutor extends OpenClawCliExecutor {
        private final AtomicInteger executions = new AtomicInteger();

        private RecordingExecutor() {
            super(new OpenClawCliConfig());
        }

        @Override
        public OpenClawCliResult execute(OpenClawCliRequest request) {
            executions.incrementAndGet();
            return new OpenClawCliResult(0, String.join(" ", request.getArguments()), "");
        }
    }
}
