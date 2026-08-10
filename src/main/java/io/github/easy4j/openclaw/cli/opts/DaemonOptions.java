package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw {@code daemon} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DaemonOptions implements CliSubArgs {

    /**
     * 定义守护进程子命令允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Subcommand {
        /**
         * 表示守护进程子命令的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS("status"),
        /**
         * 表示守护进程子命令的 {@code install} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSTALL("install"),
        /**
         * 表示守护进程子命令的 {@code uninstall} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UNINSTALL("uninstall"),
        /**
         * 表示守护进程子命令的 {@code start} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        START("start"),
        /**
         * 表示守护进程子命令的 {@code stop} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STOP("stop"),
        /**
         * 表示守护进程子命令的 {@code restart} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RESTART("restart");

        /**
         * 枚举动作对应的 CLI 子命令名称；未设置时命令行不包含 {@code --cli-name}。
         */
        private final String cliName;

        /**
 * @param cliName null, openclaw
         */
        Subcommand(String cliName) {
            this.cliName = cliName;
        }

        /**
         * 返回守护进程子命令在 openclaw CLI 中使用的固定名称。
         *
         * @return 当前守护进程动作对应的 CLI 子命令名称
         */
        public String cliName() {
            return cliName;
        }
    }

    /**
     * 要执行的子命令名称；未设置时命令行不包含 {@code --subcommand}。
     */
    private final Subcommand subcommand;
    /**
     * 状态检查使用的 Gateway RPC 参数；未设置时命令行不包含 {@code --status-rpc}。
     */
    private final GatewayRpcOptions statusRpc;
    /**
     * 状态 RPC 的附加参数；未设置时命令行不包含 {@code --status-extra}。
     */
    private final GatewayCliArgv.GatewayStatusOptions statusExtra;
    /**
     * 安装服务使用的监听端口；未设置时命令行不包含 {@code --install-port}。
     */
    private final String installPort;
    /**
     * 安装时选择的运行时；未设置时命令行不包含 {@code --install-runtime}。
     */
    private final String installRuntime;
    /**
     * 安装流程使用的认证令牌；未设置时命令行不包含 {@code --install-token}。
     */
    private final String installToken;
    /**
     * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
     */
    private final boolean installForce;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;

    /**
 * @param b builder
     */
    private DaemonOptions(Builder b) {
        this.subcommand = Objects.requireNonNull(b.subcommand, "subcommand");
        this.statusRpc = b.statusRpc;
        this.statusExtra = b.statusExtra;
        this.installPort = b.installPort;
        this.installRuntime = b.installRuntime;
        this.installToken = b.installToken;
        this.installForce = b.installForce;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code DaemonOptions} 字段。
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
        out.add(subcommand.cliName());
        switch (subcommand) {
            case STATUS:
                if (statusRpc != null) {
                    statusRpc.appendSharedFlags(out);
                }
                GatewayCliArgv.GatewayStatusOptions se = statusExtra != null
                        ? statusExtra
                        : GatewayCliArgv.GatewayStatusOptions.none();
                if (se.isNoProbe()) {
                    out.add("--no-probe");
                }
                if (se.isDeep()) {
                    out.add("--deep");
                }
                if (se.isRequireRpc()) {
                    out.add("--require-rpc");
                }
                break;
            case INSTALL:
                if (installPort != null && !installPort.isEmpty()) {
                    out.add("--port");
                    out.add(installPort);
                }
                if (installRuntime != null && !installRuntime.isEmpty()) {
                    out.add("--runtime");
                    out.add(installRuntime);
                }
                if (installToken != null && !installToken.isEmpty()) {
                    out.add("--token");
                    out.add(installToken);
                }
                if (installForce) {
                    out.add("--force");
                }
                break;
            case START:
            case STOP:
            case RESTART:
            case UNINSTALL:
                break;
            default:
                break;
        }
        if (json) {
            out.add("--json");
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code DaemonOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 要执行的子命令名称；未设置时命令行不包含 {@code --subcommand}。
         */
        private Subcommand subcommand;
        /**
         * 状态检查使用的 Gateway RPC 参数；未设置时命令行不包含 {@code --status-rpc}。
         */
        private GatewayRpcOptions statusRpc;
        /**
         * 状态 RPC 的附加参数；未设置时命令行不包含 {@code --status-extra}。
         */
        private GatewayCliArgv.GatewayStatusOptions statusExtra;
        /**
         * 安装服务使用的监听端口；未设置时命令行不包含 {@code --install-port}。
         */
        private String installPort;
        /**
         * 安装时选择的运行时；未设置时命令行不包含 {@code --install-runtime}。
         */
        private String installRuntime;
        /**
         * 安装流程使用的认证令牌；未设置时命令行不包含 {@code --install-token}。
         */
        private String installToken;
        /**
         * 是否向 openclaw 子命令追加 {@code --install-force} 开关。
         */
        private boolean installForce;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

        /**
         * 设置 {@code --subcommand} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param subcommand 要执行的子命令名称；作为 {@code --subcommand} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder subcommand(Subcommand subcommand) {
            this.subcommand = subcommand;
            return this;
        }

        /**
         * 设置 {@code --status-rpc} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param statusRpc 状态检查使用的 Gateway RPC 参数；作为 {@code --status-rpc} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusRpc(GatewayRpcOptions statusRpc) {
            this.statusRpc = statusRpc;
            return this;
        }

        /**
         * 设置 {@code --status-extra} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param statusExtra 状态 RPC 的附加参数；作为 {@code --status-extra} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusExtra(GatewayCliArgv.GatewayStatusOptions statusExtra) {
            this.statusExtra = statusExtra;
            return this;
        }

        /**
         * 设置 {@code --install-port} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installPort 安装服务使用的监听端口；作为 {@code --install-port} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installPort(String installPort) {
            this.installPort = installPort;
            return this;
        }

        /**
         * 设置 {@code --install-runtime} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installRuntime 安装时选择的运行时；作为 {@code --install-runtime} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installRuntime(String installRuntime) {
            this.installRuntime = installRuntime;
            return this;
        }

        /**
         * 设置 {@code --install-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installToken 安装流程使用的认证令牌；作为 {@code --install-token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installToken(String installToken) {
            this.installToken = installToken;
            return this;
        }

        /**
         * 设置 {@code --install-force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installForce 是否向命令行追加 {@code --install-force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installForce(boolean installForce) {
            this.installForce = installForce;
            return this;
        }

        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code DaemonOptions}。
         *
         * @return 按当前字段创建的 DaemonOptions
         */
        public DaemonOptions build() {
            return new DaemonOptions(this);
        }
    }
}
