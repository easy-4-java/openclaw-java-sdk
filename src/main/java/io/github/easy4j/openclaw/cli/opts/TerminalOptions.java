package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code terminal} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class TerminalOptions implements CliSubArgs {

    /**
     * 是否把终端任务委派给 Gateway；未设置时命令行不包含 {@code --delegate}。
     */
    private final ChatOptions delegate;

    private TerminalOptions(ChatOptions delegate) {
        this.delegate = delegate;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code TerminalOptions} 字段。
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
     * {@code TerminalOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 是否启用委派执行；未设置时命令行不包含 {@code --b}。
         */
        private final ChatOptions.Builder b = ChatOptions.builder();

        /**
         * 设置 {@code --local} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param local 是否向命令行追加 {@code --local} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder local(boolean local) { b.local(local); return this; }
        /**
         * 设置 {@code --url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) { b.url(url); return this; }
        /**
         * 设置 {@code --token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) { b.token(token); return this; }
        /**
         * 设置 {@code --password} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password Gateway 或远程服务密码；作为 {@code --password} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) { b.password(password); return this; }
        /**
         * 设置 {@code --session} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 目标会话标识；作为 {@code --session} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) { b.session(session); return this; }
        /**
         * 设置 {@code --deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否向命令行追加 {@code --deliver} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(boolean deliver) { b.deliver(deliver); return this; }
        /**
         * 设置 {@code --thinking} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 模型思考强度；作为 {@code --thinking} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) { b.thinking(thinking); return this; }
        /**
         * 设置 {@code --message} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { b.message(message); return this; }
        /**
         * 设置 {@code --timeout-ms} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer timeoutMs) { b.timeoutMs(timeoutMs); return this; }
        /**
         * 设置 {@code --history-limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param historyLimit 返回或保留的历史消息条数上限；作为 {@code --history-limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder historyLimit(Integer historyLimit) { b.historyLimit(historyLimit); return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code TerminalOptions}。
         *
         * @return 按当前字段创建的 TerminalOptions
         */
        public TerminalOptions build() {
            return new TerminalOptions(b.build());
        }
    }
}
