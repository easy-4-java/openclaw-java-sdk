package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code configure} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ConfigureOptions implements CliSubArgs {

    /**
     * 要执行检查或配置的分区列表；未设置时命令行不包含 {@code --sections}。
     */
    private final List<String> sections;

    /**
 * @param b builder
     */
    private ConfigureOptions(Builder b) {
        this.sections = OpenClawLists.copyOf(b.sections);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ConfigureOptions} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        for (String s : sections) {
            if (s != null && !s.isEmpty()) {
                out.add("--section");
                out.add(s);
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code ConfigureOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 要执行检查或配置的分区列表；未设置时命令行不包含 {@code --sections}。
         */
        private final List<String> sections = new ArrayList<>();

        /**
         * 设置 {@code --section} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param section 要配置的设置分区；作为 {@code --section} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder section(String section) {
            if (section != null && !section.isEmpty()) {
                sections.add(section);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ConfigureOptions}。
         *
         * @return 按当前字段创建的 ConfigureOptions
         */
        public ConfigureOptions build() {
            return new ConfigureOptions(this);
        }
    }
}
