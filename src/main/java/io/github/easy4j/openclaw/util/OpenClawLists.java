package io.github.easy4j.openclaw.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * JDK 8 兼容的不可变列表创建与防御性复制工具。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawLists {

    private OpenClawLists() {
    }

    /**
     * 返回不可变空列表，供没有 CLI 参数的场景复用。
     *
     * @param <T> 方法使用的泛型类型
     * @return 可安全共享的不可变空列表
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }

    /**
     * 按传入顺序复制元素并创建不可变列表。
     *
     * @param <T> 方法使用的泛型类型
     * @param elements 待复制或转为不可变视图的元素集合
     * @return 保持参数顺序的不可变元素副本；没有元素时返回空列表
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        if (elements == null || elements.length == 0) {
            return empty();
        }
        return Collections.unmodifiableList(Arrays.asList(elements.clone()));
    }

    /**
     * 复制源集合并返回不可变快照；源集合为 null 时返回空列表。
     *
     * @param <T> 方法使用的泛型类型
     * @param source 待复制的源集合；可为空
     * @return 与源集合迭代顺序一致的不可变副本；源为空时返回空列表
     */
    public static <T> List<T> copyOf(Collection<? extends T> source) {
        if (source == null || source.isEmpty()) {
            return empty();
        }
        return Collections.unmodifiableList(new ArrayList<>(source));
    }
}
