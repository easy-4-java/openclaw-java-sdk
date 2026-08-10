package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * openclaw `terminal` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class TerminalOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--delegate` 选项的内容；为 null 时通常省略。
     */
    private final ChatOptions delegate;

    private TerminalOptions(ChatOptions delegate) {
        this.delegate = delegate;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `TerminalOptions` 字段。
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
        return delegate.toSubcommandArguments();
    }

    /**
     * 链式构建器，逐项收集 TerminalOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 TerminalOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--b` 选项的内容；为 null 时通常省略。
         */
        private final ChatOptions.Builder b = ChatOptions.builder();

        /**
         * 设置 `--local` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param local 是否向命令行追加 `--local` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder local(boolean local) { b.local(local); return this; }
        /**
         * 设置 `--url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) { b.url(url); return this; }
        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) { b.token(token); return this; }
        /**
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) { b.password(password); return this; }
        /**
         * 设置 `--session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 写入 `--session` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) { b.session(session); return this; }
        /**
         * 设置 `--deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否向命令行追加 `--deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(boolean deliver) { b.deliver(deliver); return this; }
        /**
         * 设置 `--thinking` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 写入 `--thinking` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) { b.thinking(thinking); return this; }
        /**
         * 设置 `--message` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { b.message(message); return this; }
        /**
         * 设置 `--timeout-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer timeoutMs) { b.timeoutMs(timeoutMs); return this; }
        /**
         * 设置 `--history-limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param historyLimit 写入 `--history-limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder historyLimit(Integer historyLimit) { b.historyLimit(historyLimit); return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `TerminalOptions`。
         *
         * @return 按当前字段创建的 TerminalOptions
         */
        public TerminalOptions build() {
            return new TerminalOptions(b.build());
        }
    }
}
