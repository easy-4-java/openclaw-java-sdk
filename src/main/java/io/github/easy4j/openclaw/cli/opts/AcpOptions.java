package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code acp} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class AcpOptions implements CliSubArgs {

    /**
     * 定义ACP 运行方式允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示ACP 运行方式的 {@code bridge} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        BRIDGE,
        /**
         * 表示ACP 运行方式的 {@code client} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CLIENT
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
     */
    private final String url;
    /**
     * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
     */
    private final String token;
    /**
     * 读取认证令牌的文件路径；未设置时命令行不包含 {@code --token-file}。
     */
    private final String tokenFile;
    /**
     * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
     */
    private final String password;
    /**
     * 读取密码的文件路径；未设置时命令行不包含 {@code --password-file}。
     */
    private final String passwordFile;
    /**
     * 目标会话标识；未设置时命令行不包含 {@code --session}。
     */
    private final String session;
    /**
     * 会话显示标签；未设置时命令行不包含 {@code --session-label}。
     */
    private final String sessionLabel;
    /**
     * 是否向 openclaw 子命令追加 {@code --require-existing} 开关。
     */
    private final boolean requireExisting;
    /**
     * 是否向 openclaw 子命令追加 {@code --reset-session} 开关。
     */
    private final boolean resetSession;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-prefix-cwd} 开关。
     */
    private final boolean noPrefixCwd;
    /**
     * ACP 会话来源标记；未设置时命令行不包含 {@code --provenance}。
     */
    private final String provenance;
    /**
     * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
     */
    private final boolean verbose;
    /**
     * 服务进程的工作目录；未设置时命令行不包含 {@code --cwd}。
     */
    private final String cwd;
    /**
     * ACP 服务监听地址；未设置时命令行不包含 {@code --server}。
     */
    private final String server;
    /**
     * 原样传给 ACP 服务进程的参数列表；未设置时命令行不包含 {@code --server-args}。
     */
    private final List<String> serverArgs;
    /**
     * 是否向 openclaw 子命令追加 {@code --server-verbose} 开关。
     */
    private final boolean serverVerbose;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code AcpOptions} 字段。
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
     * {@code AcpOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.BRIDGE;
        /**
         * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
         */
        private String url;
        /**
         * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
         */
        private String token;
        /**
         * 读取认证令牌的文件路径；未设置时命令行不包含 {@code --token-file}。
         */
        private String tokenFile;
        /**
         * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
         */
        private String password;
        /**
         * 读取密码的文件路径；未设置时命令行不包含 {@code --password-file}。
         */
        private String passwordFile;
        /**
         * 目标会话标识；未设置时命令行不包含 {@code --session}。
         */
        private String session;
        /**
         * 会话显示标签；未设置时命令行不包含 {@code --session-label}。
         */
        private String sessionLabel;
        /**
         * 是否向 openclaw 子命令追加 {@code --require-existing} 开关。
         */
        private boolean requireExisting;
        /**
         * 是否向 openclaw 子命令追加 {@code --reset-session} 开关。
         */
        private boolean resetSession;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-prefix-cwd} 开关。
         */
        private boolean noPrefixCwd;
        /**
         * ACP 会话来源标记；未设置时命令行不包含 {@code --provenance}。
         */
        private String provenance;
        /**
         * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
         */
        private boolean verbose;
        /**
         * 服务进程的工作目录；未设置时命令行不包含 {@code --cwd}。
         */
        private String cwd;
        /**
         * ACP 服务监听地址；未设置时命令行不包含 {@code --server}。
         */
        private String server;
        /**
         * 原样传给 ACP 服务进程的参数列表；未设置时命令行不包含 {@code --server-args}。
         */
        private List<String> serverArgs = new ArrayList<>();
        /**
         * 是否向 openclaw 子命令追加 {@code --server-verbose} 开关。
         */
        private boolean serverVerbose;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code bridge} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bridge() {
            this.mode = Mode.BRIDGE;
            return this;
        }

        /**
         * 设置 {@code --url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 {@code --token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 {@code --token-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokenFile 读取认证令牌的文件路径；作为 {@code --token-file} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tokenFile(String tokenFile) {
            this.tokenFile = tokenFile;
            return this;
        }

        /**
         * 设置 {@code --password} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password Gateway 或远程服务密码；作为 {@code --password} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置 {@code --password-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param passwordFile 读取密码的文件路径；作为 {@code --password-file} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder passwordFile(String passwordFile) {
            this.passwordFile = passwordFile;
            return this;
        }

        /**
         * 设置 {@code --session} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 目标会话标识；作为 {@code --session} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) {
            this.session = session;
            return this;
        }

        /**
         * 设置 {@code --session-label} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param sessionLabel 会话显示标签；作为 {@code --session-label} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessionLabel(String sessionLabel) {
            this.sessionLabel = sessionLabel;
            return this;
        }

        /**
         * 设置 {@code --require-existing} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requireExisting 是否向命令行追加 {@code --require-existing} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder requireExisting(boolean requireExisting) {
            this.requireExisting = requireExisting;
            return this;
        }

        /**
         * 设置 {@code --reset-session} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param resetSession 是否向命令行追加 {@code --reset-session} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder resetSession(boolean resetSession) {
            this.resetSession = resetSession;
            return this;
        }

        /**
         * 设置 {@code --no-prefix-cwd} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noPrefixCwd 是否向命令行追加 {@code --no-prefix-cwd} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noPrefixCwd(boolean noPrefixCwd) {
            this.noPrefixCwd = noPrefixCwd;
            return this;
        }

        /**
         * 设置 {@code --provenance} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provenance ACP 会话来源标记；作为 {@code --provenance} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder provenance(String provenance) {
            this.provenance = provenance;
            return this;
        }

        /**
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * 选择 {@code client} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder client() {
            this.mode = Mode.CLIENT;
            return this;
        }

        /**
         * 设置 {@code --cwd} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param cwd 服务进程的工作目录；作为 {@code --cwd} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cwd(String cwd) {
            this.cwd = cwd;
            return this;
        }

        /**
         * 设置 {@code --server} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param server ACP 服务监听地址；作为 {@code --server} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder server(String server) {
            this.server = server;
            return this;
        }

        /**
         * 设置 {@code --add-server-arg} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --add-server-arg} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder addServerArg(String token) {
            if (token != null && OpenClawStrings.isNotBlank(token)) {
                serverArgs.add(token.trim());
            }
            return this;
        }

        /**
         * 设置 {@code --server-verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param serverVerbose 是否向命令行追加 {@code --server-verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder serverVerbose(boolean serverVerbose) {
            this.serverVerbose = serverVerbose;
            return this;
        }

        /**
         * 设置 {@code --extra} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 原样追加到生成参数末尾的 CLI 参数列表；作为 {@code --extra} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code AcpOptions}。
         *
         * @return 按当前字段创建的 AcpOptions
         */
        public AcpOptions build() {
            return new AcpOptions(this);
        }
    }
}
