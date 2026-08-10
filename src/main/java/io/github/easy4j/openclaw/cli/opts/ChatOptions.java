package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `chat` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ChatOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--local` 开关。
     */
    private final boolean local;
    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
     */
    private final String session;
    /**
     * 是否向 openclaw 子命令追加 `--deliver` 开关。
     */
    private final boolean deliver;
    /**
     * 传给 openclaw 子命令 `--thinking` 选项的内容；为 null 时通常省略。
     */
    private final String thinking;
    /**
     * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
     */
    private final String message;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final Integer timeoutMs;
    /**
     * 传给 openclaw 子命令 `--history-limit` 选项的内容；为 null 时通常省略。
     */
    private final Integer historyLimit;

    private ChatOptions(Builder b) {
        this.local = b.local;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.session = b.session;
        this.deliver = b.deliver;
        this.thinking = b.thinking;
        this.message = b.message;
        this.timeoutMs = b.timeoutMs;
        this.historyLimit = b.historyLimit;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `ChatOptions` 字段。
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
        OpenClawCliArgv.addFlag(out, "--local", local);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addFlag(out, "--deliver", deliver);
        OpenClawCliArgv.addIfPresent(out, "--thinking", thinking);
        OpenClawCliArgv.addIfPresent(out, "--message", message);
        OpenClawCliArgv.addIfNotNull(out, "--timeout-ms", timeoutMs);
        OpenClawCliArgv.addIfNotNull(out, "--history-limit", historyLimit);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 ChatOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ChatOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 是否向 openclaw 子命令追加 `--local` 开关。
         */
        private boolean local;
        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
         */
        private String session;
        /**
         * 是否向 openclaw 子命令追加 `--deliver` 开关。
         */
        private boolean deliver;
        /**
         * 传给 openclaw 子命令 `--thinking` 选项的内容；为 null 时通常省略。
         */
        private String thinking;
        /**
         * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
         */
        private String message;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private Integer timeoutMs;
        /**
         * 传给 openclaw 子命令 `--history-limit` 选项的内容；为 null 时通常省略。
         */
        private Integer historyLimit;

        /**
         * 设置 `--local` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param local 是否向命令行追加 `--local` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder local(boolean local) { this.local = local; return this; }
        /**
         * 设置 `--url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) { this.url = url; return this; }
        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) { this.token = token; return this; }
        /**
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) { this.password = password; return this; }
        /**
         * 设置 `--session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 写入 `--session` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) { this.session = session; return this; }
        /**
         * 设置 `--deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deliver 是否向命令行追加 `--deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deliver(boolean deliver) { this.deliver = deliver; return this; }
        /**
         * 设置 `--thinking` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param thinking 写入 `--thinking` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder thinking(String thinking) { this.thinking = thinking; return this; }
        /**
         * 设置 `--message` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) { this.message = message; return this; }
        /**
         * 设置 `--timeout-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        /**
         * 设置 `--history-limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param historyLimit 写入 `--history-limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder historyLimit(Integer historyLimit) { this.historyLimit = historyLimit; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `ChatOptions`。
         *
         * @return 按当前字段创建的 ChatOptions
         */
        public ChatOptions build() {
            return new ChatOptions(this);
        }
    }
}
