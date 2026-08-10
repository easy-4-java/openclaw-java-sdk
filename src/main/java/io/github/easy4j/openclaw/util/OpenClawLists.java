package io.github.easy4j.openclaw.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * list utility(Java 8 ),Used forReplacement for {@link List#of},{@link List#copyOf(Collection)} JDK 9+ API.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class OpenClawLists {

    private OpenClawLists() {
    }

    /**
 * Returns an immutable(Equivalent to {@code List.of}).
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }

    /**
 * Constructs from varargs(Equivalent to {@code List.of(e1, e2, ...)}).
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        if (elements == null || elements.length == 0) {
            return empty();
        }
        return Collections.unmodifiableList(Arrays.asList(elements.clone()));
    }

    /**
 * Copies as(Equivalent to {@code List.copyOf(source)}).
     */
    public static <T> List<T> copyOf(Collection<? extends T> source) {
        if (source == null || source.isEmpty()) {
            return empty();
        }
        return Collections.unmodifiableList(new ArrayList<>(source));
    }
}
