package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawStrings;
import java.util.List;

/**
 * CLI 参数编码辅助类，统一处理可选值、布尔开关、可重复选项和未类型化的额外 token。
 * 所有方法都按调用顺序写入调用方提供的列表，不重排参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
final class OpenClawCliArgv {

    private OpenClawCliArgv() {
    }

    /**
     * 在字符串值非空白时依次追加选项名和值。
     *
     * @param out 接收参数的可变列表
     * @param flag CLI 选项名，例如 {@code --url}
     * @param value 选项值；为空白时忽略
     */
    static void addIfPresent(List<String> out, String flag, String value) {
        if (value != null && OpenClawStrings.isNotBlank(value)) {
            out.add(flag);
            out.add(value);
        }
    }

    /**
     * 在整数值大于零时依次追加选项名和十进制值。
     *
     * @param out 接收参数的可变列表
     * @param flag CLI 选项名
     * @param value 正整数选项值
     */
    static void addIfPositive(List<String> out, String flag, int value) {
        if (value > 0) {
            out.add(flag);
            out.add(Integer.toString(value));
        }
    }

    /**
     * 在整数值非空时依次追加选项名和十进制值。
     *
     * @param out 接收参数的可变列表
     * @param flag CLI 选项名
     * @param value 可空整数选项值
     */
    static void addIfNotNull(List<String> out, String flag, Integer value) {
        if (value != null) {
            out.add(flag);
            out.add(Integer.toString(value));
        }
    }

    /**
     * 在浮点值非空时依次追加选项名和十进制值。
     *
     * @param out 接收参数的可变列表
     * @param flag CLI 选项名
     * @param value 可空浮点选项值
     */
    static void addIfNotNull(List<String> out, String flag, Double value) {
        if (value != null) {
            out.add(flag);
            out.add(Double.toString(value));
        }
    }

    /**
     * 开关启用时仅追加选项名，不追加值 token。
     *
     * @param out 接收参数的可变列表
     * @param flag CLI 开关名
     * @param enabled 是否输出该开关
     */
    static void addFlag(List<String> out, String flag, boolean enabled) {
        if (enabled) {
            out.add(flag);
        }
    }

    /**
     * 为每个非空白值重复追加同一选项名，适用于 {@code --scope value} 等可重复选项。
     *
     * @param out 接收参数的可变列表
     * @param flag 可重复的 CLI 选项名
     * @param values 选项值列表；为空时不追加
     */
    static void addRepeatable(List<String> out, String flag, List<String> values) {
        if (values == null) {
            return;
        }
        for (String v : values) {
            if (v != null && OpenClawStrings.isNotBlank(v)) {
                out.add(flag);
                out.add(v.trim());
            }
        }
    }

    /**
     * 原样追加调用方提供的额外 token，供类型化选项尚未覆盖的 CLI 参数使用。
     *
     * @param out 接收参数的可变列表
     * @param extra 额外 token 列表；为空时不追加
     */
    static void addExtra(List<String> out, List<String> extra) {
        if (extra == null) {
            return;
        }
        out.addAll(extra);
    }
}
