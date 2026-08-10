package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code tools} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ToolsOptions implements CliSubArgs {

    /**
     * 选择或编码 {@code tools} 子命令的 {@code empty} 行为，并保留未设置选项的省略语义。
     *
     * @return 不包含附加选项的参数对象
     */
    public static ToolsOptions empty() {
        return INSTANCE;
    }

    /**
     * 无选项场景复用的不可变空参数对象。
     */
    private static final ToolsOptions INSTANCE = new ToolsOptions();

    private ToolsOptions() {
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        return Collections.emptyList();
    }
}
