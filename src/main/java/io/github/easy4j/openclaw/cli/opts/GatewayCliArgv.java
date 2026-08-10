package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Gateway CLI 参数工厂，把共享 RPC 连接配置与 health、status、probe 各自的选项合并为不可变参数列表。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class GatewayCliArgv {

    private GatewayCliArgv() {
    }

    /**
     * 生成 gateway health 参数，并追加共享的 RPC 认证和连接选项。
     *
     * @param rpc Gateway URL、认证与超时等共享 RPC 选项
     * @return 以 {@code health} 开头、随后为共享 RPC 选项的不可变参数列表
     */
    public static List<String> health(GatewayRpcOptions rpc) {
        Objects.requireNonNull(rpc, "rpc");
        List<String> args = new ArrayList<>();
        args.add("health");
        rpc.appendSharedFlags(args);
        return Collections.unmodifiableList(args);
    }

    /**
     * 生成 gateway status 参数，并按顺序合并 RPC 选项与状态查询选项。
     *
     * @param rpc Gateway URL、认证与超时等共享 RPC 选项
     * @param extra 是否跳过探测、执行深度检查或强制 RPC 成功的状态选项；为空时不追加这些开关
     * @return 以 {@code status} 开头并包含已启用状态开关的不可变参数列表
     */
    public static List<String> status(GatewayRpcOptions rpc, GatewayStatusOptions extra) {
        Objects.requireNonNull(rpc, "rpc");
        GatewayStatusOptions e = extra != null ? extra : GatewayStatusOptions.none();
        List<String> args = new ArrayList<>();
        args.add("status");
        rpc.appendSharedFlags(args);
        if (e.isNoProbe()) {
            args.add("--no-probe");
        }
        if (e.isDeep()) {
            args.add("--deep");
        }
        if (e.isRequireRpc()) {
            args.add("--require-rpc");
        }
        return Collections.unmodifiableList(args);
    }

    /**
     * 生成 gateway probe 参数，并按顺序合并 RPC 选项与探测选项。
     *
     * @param rpc Gateway URL、认证与超时等共享 RPC 选项
     * @param extra SSH 目标、私钥和自动发现选项；为空时不追加 SSH 参数
     * @return 以 {@code probe} 开头并包含连接、认证和 SSH 选项的不可变参数列表
     */
    public static List<String> probe(GatewayRpcOptions rpc, GatewayProbeOptions extra) {
        Objects.requireNonNull(rpc, "rpc");
        GatewayProbeOptions p = extra != null ? extra : GatewayProbeOptions.none();
        List<String> args = new ArrayList<>();
        args.add("probe");
        rpc.appendSharedFlags(args);
        if (p.getSsh() != null && !p.getSsh().isEmpty()) {
            args.add("--ssh");
            args.add(p.getSsh());
        }
        if (p.getSshIdentity() != null && !p.getSshIdentity().isEmpty()) {
            args.add("--ssh-identity");
            args.add(p.getSshIdentity());
        }
        if (p.isSshAuto()) {
            args.add("--ssh-auto");
        }
        return Collections.unmodifiableList(args);
    }

    /**
     * openclaw {@code gateway-status} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class GatewayStatusOptions {

        /**
         * 是否向 openclaw 子命令追加 {@code --no-probe} 开关。
         */
        private final boolean noProbe;
        /**
         * 是否向 openclaw 子命令追加 {@code --deep} 开关。
         */
        private final boolean deep;
        /**
         * 是否向 openclaw 子命令追加 {@code --require-rpc} 开关。
         */
        private final boolean requireRpc;

        /**
         * @param noProbe   {@code --no-probe}
         * @param deep      {@code --deep}
         * @param requireRpc {@code --require-rpc}
         */
        private GatewayStatusOptions(boolean noProbe, boolean deep, boolean requireRpc) {
            this.noProbe = noProbe;
            this.deep = deep;
            this.requireRpc = requireRpc;
        }

        /**
         * 创建不附加 {@code --no-probe}、{@code --deep} 或 {@code --require-rpc} 的状态选项。
         *
         * @return 不附加状态或探测标志的选项对象
         */
        public static GatewayStatusOptions none() {
            return new GatewayStatusOptions(false, false, false);
        }

        /**
         * 创建空白构建器，供调用方链式设置 {@code GatewayStatusOptions} 字段。
         *
         * @return 新的空白构建器
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * 返回 gateway-status 是否跳过连通性探测。
         *
         * @return 是否跳过 Gateway 探测
         */
        public boolean isNoProbe() {
            return noProbe;
        }

        /**
         * 返回 gateway-status 是否执行深度检查。
         *
         * @return 是否执行深度状态检查
         */
        public boolean isDeep() {
            return deep;
        }

        /**
         * 返回 gateway-status 是否要求 RPC 检查成功。
         *
         * @return 是否要求 Gateway RPC 可用
         */
        public boolean isRequireRpc() {
            return requireRpc;
        }

        /**
         * {@code GatewayStatusOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        public static final class Builder {

            /**
             * 是否向 openclaw 子命令追加 {@code --no-probe} 开关。
             */
            private boolean noProbe;
            /**
             * 是否向 openclaw 子命令追加 {@code --deep} 开关。
             */
            private boolean deep;
            /**
             * 是否向 openclaw 子命令追加 {@code --require-rpc} 开关。
             */
            private boolean requireRpc;

            /**
             * 设置 {@code --no-probe} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param noProbe 是否向命令行追加 {@code --no-probe} 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder noProbe(boolean noProbe) {
                this.noProbe = noProbe;
                return this;
            }

            /**
             * 设置 {@code --deep} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param deep 是否向命令行追加 {@code --deep} 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder deep(boolean deep) {
                this.deep = deep;
                return this;
            }

            /**
             * 设置 {@code --require-rpc} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param requireRpc 是否向命令行追加 {@code --require-rpc} 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder requireRpc(boolean requireRpc) {
                this.requireRpc = requireRpc;
                return this;
            }

            /**
             * 校验并复制当前构建器字段，创建独立的 {@code GatewayStatusOptions}。
             *
             * @return 按当前字段创建的 GatewayStatusOptions
             */
            public GatewayStatusOptions build() {
                return new GatewayStatusOptions(noProbe, deep, requireRpc);
            }
        }
    }

    /**
     * openclaw {@code gateway-probe} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class GatewayProbeOptions {

        /**
         * 远程 Gateway 的 SSH 目标；未设置时命令行不包含 {@code --ssh}。
         */
        private final String ssh;
        /**
         * SSH 私钥文件路径；未设置时命令行不包含 {@code --ssh-identity}。
         */
        private final String sshIdentity;
        /**
         * 是否向 openclaw 子命令追加 {@code --ssh-auto} 开关。
         */
        private final boolean sshAuto;

        /**
         * @param ssh         {@code --ssh}
         * @param sshIdentity {@code --ssh-identity}
         * @param sshAuto     {@code --ssh-auto}
         */
        private GatewayProbeOptions(String ssh, String sshIdentity, boolean sshAuto) {
            this.ssh = ssh;
            this.sshIdentity = sshIdentity;
            this.sshAuto = sshAuto;
        }

        /**
         * 创建不附加 SSH 目标、私钥或自动发现开关的探测选项。
         *
         * @return 不附加状态或探测标志的选项对象
         */
        public static GatewayProbeOptions none() {
            return new GatewayProbeOptions(null, null, false);
        }

        /**
         * 创建空白构建器，供调用方链式设置 {@code GatewayProbeOptions} 字段。
         *
         * @return 新的空白构建器
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * 返回 Gateway CLI 使用的 SSH 目标。
         *
         * @return SSH 目标；未指定时为 {@code null}
         */
        public String getSsh() {
            return ssh;
        }

        /**
         * 返回 SSH 私钥路径；未配置时为空。
         *
         * @return SSH 私钥文件路径；未配置时为 {@code null}
         */
        public String getSshIdentity() {
            return sshIdentity;
        }

        /**
         * 返回 gateway-probe 是否自动发现 SSH 参数。
         *
         * @return 是否自动推导 SSH 目标
         */
        public boolean isSshAuto() {
            return sshAuto;
        }

        /**
         * {@code GatewayProbeOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        public static final class Builder {

            /**
             * 远程 Gateway 的 SSH 目标；未设置时命令行不包含 {@code --ssh}。
             */
            private String ssh;
            /**
             * SSH 私钥文件路径；未设置时命令行不包含 {@code --ssh-identity}。
             */
            private String sshIdentity;
            /**
             * 是否向 openclaw 子命令追加 {@code --ssh-auto} 开关。
             */
            private boolean sshAuto;

            /**
             * 设置 {@code --ssh} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param ssh 远程 Gateway 的 SSH 目标；作为 {@code --ssh} 的参数
             * @return 当前构建器，便于继续链式配置
             */
            public Builder ssh(String ssh) {
                this.ssh = ssh;
                return this;
            }

            /**
             * 设置 {@code --ssh-identity} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param sshIdentity SSH 私钥文件路径；作为 {@code --ssh-identity} 的参数
             * @return 当前构建器，便于继续链式配置
             */
            public Builder sshIdentity(String sshIdentity) {
                this.sshIdentity = sshIdentity;
                return this;
            }

            /**
             * 设置 {@code --ssh-auto} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param sshAuto 是否向命令行追加 {@code --ssh-auto} 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder sshAuto(boolean sshAuto) {
                this.sshAuto = sshAuto;
                return this;
            }

            /**
             * 校验并复制当前构建器字段，创建独立的 {@code GatewayProbeOptions}。
             *
             * @return 按当前字段创建的 GatewayProbeOptions
             */
            public GatewayProbeOptions build() {
                return new GatewayProbeOptions(ssh, sshIdentity, sshAuto);
            }
        }
    }
}
