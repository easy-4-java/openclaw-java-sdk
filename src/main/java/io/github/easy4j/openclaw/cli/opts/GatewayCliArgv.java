package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 本地 openclaw CLI 的 `GatewayCliArgv` 支撑类型，用于参数编码、可用性检查或执行结果表达。
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
     * @param rpc 写入 `rpc` 协议字段的内容
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
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
     * @param rpc 写入 `rpc` 协议字段的内容
     * @param extra 写入 `extra` 协议字段的内容
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
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
     * @param rpc 写入 `rpc` 协议字段的内容
     * @param extra 写入 `extra` 协议字段的内容
     * @return 按协议顺序返回的数据列表；没有数据时为空列表
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
     * openclaw `gateway-status` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class GatewayStatusOptions {

        /**
         * 是否向 openclaw 子命令追加 `--no-probe` 开关。
         */
        private final boolean noProbe;
        /**
         * 是否向 openclaw 子命令追加 `--deep` 开关。
         */
        private final boolean deep;
        /**
         * 是否向 openclaw 子命令追加 `--require-rpc` 开关。
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
         * 选择或编码 `gateway-status` 子命令的 `none` 行为，并保留未设置选项的省略语义。
         *
         * @return 按当前参数创建、查询或解析得到的 GatewayStatusOptions
         */
        public static GatewayStatusOptions none() {
            return new GatewayStatusOptions(false, false, false);
        }

        /**
         * 创建空白构建器，供调用方链式设置 `GatewayStatusOptions` 字段。
         *
         * @return 新的空白构建器
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * 判断 `noProbe` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isNoProbe() {
            return noProbe;
        }

        /**
         * 判断 `deep` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isDeep() {
            return deep;
        }

        /**
         * 判断 `requireRpc` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isRequireRpc() {
            return requireRpc;
        }

        /**
         * 链式构建器，逐项收集 GatewayStatusOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 GatewayStatusOptions。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        public static final class Builder {

            /**
             * 是否向 openclaw 子命令追加 `--no-probe` 开关。
             */
            private boolean noProbe;
            /**
             * 是否向 openclaw 子命令追加 `--deep` 开关。
             */
            private boolean deep;
            /**
             * 是否向 openclaw 子命令追加 `--require-rpc` 开关。
             */
            private boolean requireRpc;

            /**
             * 设置 `--no-probe` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param noProbe 是否向命令行追加 `--no-probe` 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder noProbe(boolean noProbe) {
                this.noProbe = noProbe;
                return this;
            }

            /**
             * 设置 `--deep` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param deep 是否向命令行追加 `--deep` 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder deep(boolean deep) {
                this.deep = deep;
                return this;
            }

            /**
             * 设置 `--require-rpc` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param requireRpc 是否向命令行追加 `--require-rpc` 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder requireRpc(boolean requireRpc) {
                this.requireRpc = requireRpc;
                return this;
            }

            /**
             * 校验并复制当前构建器字段，创建独立的 `GatewayStatusOptions`。
             *
             * @return 按当前字段创建的 GatewayStatusOptions
             */
            public GatewayStatusOptions build() {
                return new GatewayStatusOptions(noProbe, deep, requireRpc);
            }
        }
    }

    /**
     * openclaw `gateway-probe` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class GatewayProbeOptions {

        /**
         * 传给 openclaw 子命令 `--ssh` 选项的内容；为 null 时通常省略。
         */
        private final String ssh;
        /**
         * 传给 openclaw 子命令 `--ssh-identity` 选项的内容；为 null 时通常省略。
         */
        private final String sshIdentity;
        /**
         * 是否向 openclaw 子命令追加 `--ssh-auto` 开关。
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
         * 选择或编码 `gateway-probe` 子命令的 `none` 行为，并保留未设置选项的省略语义。
         *
         * @return 按当前参数创建、查询或解析得到的 GatewayProbeOptions
         */
        public static GatewayProbeOptions none() {
            return new GatewayProbeOptions(null, null, false);
        }

        /**
         * 创建空白构建器，供调用方链式设置 `GatewayProbeOptions` 字段。
         *
         * @return 新的空白构建器
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * 读取当前对象保存的 `ssh` 对应状态，不触发网络或子进程调用。
         *
         * @return 服务返回或流式累积得到的文本
         */
        public String getSsh() {
            return ssh;
        }

        /**
         * 读取当前对象保存的 `sshIdentity` 对应状态，不触发网络或子进程调用。
         *
         * @return 可用于关联后续请求的标识
         */
        public String getSshIdentity() {
            return sshIdentity;
        }

        /**
         * 判断 `sshAuto` 对应状态 是否满足协议或生命周期条件。
         *
         * @return 条件成立返回 {@code true}，否则返回 {@code false}
         */
        public boolean isSshAuto() {
            return sshAuto;
        }

        /**
         * 链式构建器，逐项收集 GatewayProbeOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 GatewayProbeOptions。
         *
         * @author <a href="https://github.com/loong10k">Loong Wan</a>
         * @since 1.0.0
         */
        public static final class Builder {

            /**
             * 传给 openclaw 子命令 `--ssh` 选项的内容；为 null 时通常省略。
             */
            private String ssh;
            /**
             * 传给 openclaw 子命令 `--ssh-identity` 选项的内容；为 null 时通常省略。
             */
            private String sshIdentity;
            /**
             * 是否向 openclaw 子命令追加 `--ssh-auto` 开关。
             */
            private boolean sshAuto;

            /**
             * 设置 `--ssh` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param ssh 写入 `--ssh` 选项的内容
             * @return 当前构建器，便于继续链式配置
             */
            public Builder ssh(String ssh) {
                this.ssh = ssh;
                return this;
            }

            /**
             * 设置 `--ssh-identity` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param sshIdentity 写入 `--ssh-identity` 选项的内容
             * @return 当前构建器，便于继续链式配置
             */
            public Builder sshIdentity(String sshIdentity) {
                this.sshIdentity = sshIdentity;
                return this;
            }

            /**
             * 设置 `--ssh-auto` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
             *
             * @param sshAuto 是否向命令行追加 `--ssh-auto` 开关
             * @return 当前构建器，便于继续链式配置
             */
            public Builder sshAuto(boolean sshAuto) {
                this.sshAuto = sshAuto;
                return this;
            }

            /**
             * 校验并复制当前构建器字段，创建独立的 `GatewayProbeOptions`。
             *
             * @return 按当前字段创建的 GatewayProbeOptions
             */
            public GatewayProbeOptions build() {
                return new GatewayProbeOptions(ssh, sshIdentity, sshAuto);
            }
        }
    }
}
