package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `acp` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AcpOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `bridge` 协议模式；序列化时使用该固定取值。
         */
        BRIDGE,
        /**
         * 选择 `client` 协议模式；序列化时使用该固定取值。
         */
        CLIENT
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--token-file` 选项的内容；为 null 时通常省略。
     */
    private final String tokenFile;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 传给 openclaw 子命令 `--password-file` 选项的内容；为 null 时通常省略。
     */
    private final String passwordFile;
    /**
     * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
     */
    private final String session;
    /**
     * 传给 openclaw 子命令 `--session-label` 选项的内容；为 null 时通常省略。
     */
    private final String sessionLabel;
    /**
     * 是否向 openclaw 子命令追加 `--require-existing` 开关。
     */
    private final boolean requireExisting;
    /**
     * 是否向 openclaw 子命令追加 `--reset-session` 开关。
     */
    private final boolean resetSession;
    /**
     * 是否向 openclaw 子命令追加 `--no-prefix-cwd` 开关。
     */
    private final boolean noPrefixCwd;
    /**
     * 传给 openclaw 子命令 `--provenance` 选项的内容；为 null 时通常省略。
     */
    private final String provenance;
    /**
     * 是否向 openclaw 子命令追加 `--verbose` 开关。
     */
    private final boolean verbose;
    /**
     * 传给 openclaw 子命令 `--cwd` 选项的内容；为 null 时通常省略。
     */
    private final String cwd;
    /**
     * 传给 openclaw 子命令 `--server` 选项的内容；为 null 时通常省略。
     */
    private final String server;
    /**
     * 传给 openclaw 子命令 `--server-args` 选项的内容；为 null 时通常省略。
     */
    private final List<String> serverArgs;
    /**
     * 是否向 openclaw 子命令追加 `--server-verbose` 开关。
     */
    private final boolean serverVerbose;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private AcpOptions(Builder b) {
        this.mode = b.mode;
        this.url = b.url;
        this.token = b.token;
        this.tokenFile = b.tokenFile;
        this.password = b.password;
        this.passwordFile = b.passwordFile;
        this.session = b.session;
        this.sessionLabel = b.sessionLabel;
        this.requireExisting = b.requireExisting;
        this.resetSession = b.resetSession;
        this.noPrefixCwd = b.noPrefixCwd;
        this.provenance = b.provenance;
        this.verbose = b.verbose;
        this.cwd = b.cwd;
        this.server = b.server;
        this.serverArgs = b.serverArgs == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.serverArgs);
        this.serverVerbose = b.serverVerbose;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `AcpOptions` 字段。
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
        if (mode == Mode.CLIENT) {
            out.add("client");
            OpenClawCliArgv.addIfPresent(out, "--cwd", cwd);
            OpenClawCliArgv.addIfPresent(out, "--server", server);
            if (!serverArgs.isEmpty()) {
                out.add("--server-args");
                out.addAll(serverArgs);
            }
            OpenClawCliArgv.addFlag(out, "--server-verbose", serverVerbose);
            OpenClawCliArgv.addFlag(out, "--verbose", verbose);
            OpenClawCliArgv.addExtra(out, extra);
            return Collections.unmodifiableList(out);
        }
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--token-file", tokenFile);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--password-file", passwordFile);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--session-label", sessionLabel);
        OpenClawCliArgv.addFlag(out, "--require-existing", requireExisting);
        OpenClawCliArgv.addFlag(out, "--reset-session", resetSession);
        OpenClawCliArgv.addFlag(out, "--no-prefix-cwd", noPrefixCwd);
        OpenClawCliArgv.addIfPresent(out, "--provenance", provenance);
        OpenClawCliArgv.addFlag(out, "--verbose", verbose);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 AcpOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 AcpOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.BRIDGE;
        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--token-file` 选项的内容；为 null 时通常省略。
         */
        private String tokenFile;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 传给 openclaw 子命令 `--password-file` 选项的内容；为 null 时通常省略。
         */
        private String passwordFile;
        /**
         * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
         */
        private String session;
        /**
         * 传给 openclaw 子命令 `--session-label` 选项的内容；为 null 时通常省略。
         */
        private String sessionLabel;
        /**
         * 是否向 openclaw 子命令追加 `--require-existing` 开关。
         */
        private boolean requireExisting;
        /**
         * 是否向 openclaw 子命令追加 `--reset-session` 开关。
         */
        private boolean resetSession;
        /**
         * 是否向 openclaw 子命令追加 `--no-prefix-cwd` 开关。
         */
        private boolean noPrefixCwd;
        /**
         * 传给 openclaw 子命令 `--provenance` 选项的内容；为 null 时通常省略。
         */
        private String provenance;
        /**
         * 是否向 openclaw 子命令追加 `--verbose` 开关。
         */
        private boolean verbose;
        /**
         * 传给 openclaw 子命令 `--cwd` 选项的内容；为 null 时通常省略。
         */
        private String cwd;
        /**
         * 传给 openclaw 子命令 `--server` 选项的内容；为 null 时通常省略。
         */
        private String server;
        /**
         * 传给 openclaw 子命令 `--server-args` 选项的内容；为 null 时通常省略。
         */
        private List<String> serverArgs = new ArrayList<>();
        /**
         * 是否向 openclaw 子命令追加 `--server-verbose` 开关。
         */
        private boolean serverVerbose;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `bridge` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bridge() {
            this.mode = Mode.BRIDGE;
            return this;
        }

        /**
         * 设置 `--url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 `--token-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokenFile 写入 `--token-file` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tokenFile(String tokenFile) {
            this.tokenFile = tokenFile;
            return this;
        }

        /**
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置 `--password-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param passwordFile 写入 `--password-file` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder passwordFile(String passwordFile) {
            this.passwordFile = passwordFile;
            return this;
        }

        /**
         * 设置 `--session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 写入 `--session` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) {
            this.session = session;
            return this;
        }

        /**
         * 设置 `--session-label` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionLabel 写入 `--session-label` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionLabel(String sessionLabel) {
            this.sessionLabel = sessionLabel;
            return this;
        }

        /**
         * 设置 `--require-existing` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requireExisting 是否向命令行追加 `--require-existing` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder requireExisting(boolean requireExisting) {
            this.requireExisting = requireExisting;
            return this;
        }

        /**
         * 设置 `--reset-session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param resetSession 是否向命令行追加 `--reset-session` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder resetSession(boolean resetSession) {
            this.resetSession = resetSession;
            return this;
        }

        /**
         * 设置 `--no-prefix-cwd` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noPrefixCwd 是否向命令行追加 `--no-prefix-cwd` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noPrefixCwd(boolean noPrefixCwd) {
            this.noPrefixCwd = noPrefixCwd;
            return this;
        }

        /**
         * 设置 `--provenance` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provenance 写入 `--provenance` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder provenance(String provenance) {
            this.provenance = provenance;
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * 选择 `client` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder client() {
            this.mode = Mode.CLIENT;
            return this;
        }

        /**
         * 设置 `--cwd` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param cwd 写入 `--cwd` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cwd(String cwd) {
            this.cwd = cwd;
            return this;
        }

        /**
         * 设置 `--server` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param server 写入 `--server` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder server(String server) {
            this.server = server;
            return this;
        }

        /**
         * 设置 `--add-server-arg` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--add-server-arg` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addServerArg(String token) {
            if (token != null && OpenClawStrings.isNotBlank(token)) {
                serverArgs.add(token.trim());
            }
            return this;
        }

        /**
         * 设置 `--server-verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param serverVerbose 是否向命令行追加 `--server-verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder serverVerbose(boolean serverVerbose) {
            this.serverVerbose = serverVerbose;
            return this;
        }

        /**
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `AcpOptions`。
         *
         * @return 按当前字段创建的 AcpOptions
         */
        public AcpOptions build() {
            return new AcpOptions(this);
        }
    }
}
