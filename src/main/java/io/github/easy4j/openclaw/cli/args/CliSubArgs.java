package io.github.easy4j.openclaw.cli.args;

import java.util.Collections;
import java.util.List;

/**
 * `CliSubArgs` 生命周期回调契约；实现方应避免在网络回调线程中执行长时间阻塞任务。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface CliSubArgs {

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    List<String> toSubcommandArguments();

    /**
     * 返回不可变空列表，供没有 CLI 参数的场景复用。
     *
     * @return 按当前参数创建、查询或解析得到的 CliSubArgs
     */
    static CliSubArgs empty() {
        return Collections::emptyList;
    }
}
