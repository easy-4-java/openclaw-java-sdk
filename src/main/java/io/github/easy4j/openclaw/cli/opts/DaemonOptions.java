package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * openclaw `daemon` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DaemonOptions implements CliSubArgs {

    /**
     * `Subcommand` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Subcommand {
        /**
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS("status"),
        /**
         * 选择 `install` 协议模式；序列化时使用该固定取值。
         */
        INSTALL("install"),
        /**
         * 选择 `uninstall` 协议模式；序列化时使用该固定取值。
         */
        UNINSTALL("uninstall"),
        /**
         * 选择 `start` 协议模式；序列化时使用该固定取值。
         */
        START("start"),
        /**
         * 选择 `stop` 协议模式；序列化时使用该固定取值。
         */
        STOP("stop"),
        /**
         * 选择 `restart` 协议模式；序列化时使用该固定取值。
         */
        RESTART("restart");

        /**
         * 传给 openclaw 子命令 `--cli-name` 选项的内容；为 null 时通常省略。
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
         * @return 服务返回或流式累积得到的文本
         */
        public String cliName() {
            return cliName;
        }
    }

    /**
     * 传给 openclaw 子命令 `--subcommand` 选项的内容；为 null 时通常省略。
     */
    private final Subcommand subcommand;
    /**
     * 传给 openclaw 子命令 `--status-rpc` 选项的内容；为 null 时通常省略。
     */
    private final GatewayRpcOptions statusRpc;
    /**
     * 传给 openclaw 子命令 `--status-extra` 选项的内容；为 null 时通常省略。
     */
    private final GatewayCliArgv.GatewayStatusOptions statusExtra;
    /**
     * 传给 openclaw 子命令 `--install-port` 选项的内容；为 null 时通常省略。
     */
    private final String installPort;
    /**
     * 传给 openclaw 子命令 `--install-runtime` 选项的内容；为 null 时通常省略。
     */
    private final String installRuntime;
    /**
     * 传给 openclaw 子命令 `--install-token` 选项的内容；为 null 时通常省略。
     */
    private final String installToken;
    /**
     * 是否向 openclaw 子命令追加 `--install-force` 开关。
     */
    private final boolean installForce;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
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
     * 创建空白构建器，供调用方链式设置 `DaemonOptions` 字段。
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
     * 链式构建器，逐项收集 DaemonOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 DaemonOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--subcommand` 选项的内容；为 null 时通常省略。
         */
        private Subcommand subcommand;
        /**
         * 传给 openclaw 子命令 `--status-rpc` 选项的内容；为 null 时通常省略。
         */
        private GatewayRpcOptions statusRpc;
        /**
         * 传给 openclaw 子命令 `--status-extra` 选项的内容；为 null 时通常省略。
         */
        private GatewayCliArgv.GatewayStatusOptions statusExtra;
        /**
         * 传给 openclaw 子命令 `--install-port` 选项的内容；为 null 时通常省略。
         */
        private String installPort;
        /**
         * 传给 openclaw 子命令 `--install-runtime` 选项的内容；为 null 时通常省略。
         */
        private String installRuntime;
        /**
         * 传给 openclaw 子命令 `--install-token` 选项的内容；为 null 时通常省略。
         */
        private String installToken;
        /**
         * 是否向 openclaw 子命令追加 `--install-force` 开关。
         */
        private boolean installForce;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

        /**
         * 设置 `--subcommand` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param subcommand 写入 `--subcommand` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder subcommand(Subcommand subcommand) {
            this.subcommand = subcommand;
            return this;
        }

        /**
         * 设置 `--status-rpc` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param statusRpc 写入 `--status-rpc` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusRpc(GatewayRpcOptions statusRpc) {
            this.statusRpc = statusRpc;
            return this;
        }

        /**
         * 设置 `--status-extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param statusExtra 写入 `--status-extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusExtra(GatewayCliArgv.GatewayStatusOptions statusExtra) {
            this.statusExtra = statusExtra;
            return this;
        }

        /**
         * 设置 `--install-port` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installPort 写入 `--install-port` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installPort(String installPort) {
            this.installPort = installPort;
            return this;
        }

        /**
         * 设置 `--install-runtime` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installRuntime 写入 `--install-runtime` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installRuntime(String installRuntime) {
            this.installRuntime = installRuntime;
            return this;
        }

        /**
         * 设置 `--install-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installToken 写入 `--install-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installToken(String installToken) {
            this.installToken = installToken;
            return this;
        }

        /**
         * 设置 `--install-force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installForce 是否向命令行追加 `--install-force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installForce(boolean installForce) {
            this.installForce = installForce;
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `DaemonOptions`。
         *
         * @return 按当前字段创建的 DaemonOptions
         */
        public DaemonOptions build() {
            return new DaemonOptions(this);
        }
    }
}
