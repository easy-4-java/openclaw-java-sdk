package io.github.easy4j.openclaw.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * OpenClaw SDK 的 `OpenClawLists` 类型，封装其公开契约和生命周期边界。
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
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }

    /**
     * 根据参数创建符合 OpenClaw 协议约束的 `OpenClawLists`。
     *
     * @param <T> 方法使用的泛型类型
     * @param elements 写入 `elements` 协议字段的内容
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
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
     * @param source 写入 `source` 协议字段的内容
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
     */
    public static <T> List<T> copyOf(Collection<? extends T> source) {
        if (source == null || source.isEmpty()) {
            return empty();
        }
        return Collections.unmodifiableList(new ArrayList<>(source));
    }
}
