package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code crestodian} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CrestodianOptions implements CliSubArgs {

    /**
     * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
     */
    private final String message;
    /**
     * 是否向 openclaw 子命令追加 {@code --yes} 开关。
     */
    private final boolean yes;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;

    private CrestodianOptions(Builder b) {
        this.message = b.message;
        this.yes = b.yes;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code CrestodianOptions} 字段。
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
        if (message != null && !message.isEmpty()) {
            out.add("--message");
            out.add(message);
        }
        if (yes) {
            out.add("--yes");
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code CrestodianOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
         */
        private String message;
        /**
         * 是否向 openclaw 子命令追加 {@code --yes} 开关。
         */
        private boolean yes;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

        /**
         * 设置 {@code --message} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { this.message = message; return this; }
        /**
         * 设置 {@code --yes} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 {@code --yes} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder yes(boolean yes) { this.yes = yes; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code CrestodianOptions}。
         *
         * @return 按当前字段创建的 CrestodianOptions
         */
        public CrestodianOptions build() {
            return new CrestodianOptions(this);
        }
    }
}
