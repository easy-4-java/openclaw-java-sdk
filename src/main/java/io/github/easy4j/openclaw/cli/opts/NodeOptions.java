package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code node} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class NodeOptions implements CliSubArgs {

    /**
     * 定义节点守护进程动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示节点守护进程动作的 {@code run} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RUN,
        /**
         * 表示节点守护进程动作的 {@code install} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INSTALL,
        /**
         * 表示节点守护进程动作的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS,
        /**
         * 表示节点守护进程动作的 {@code stop} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STOP,
        /**
         * 表示节点守护进程动作的 {@code restart} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RESTART,
        /**
         * 表示节点守护进程动作的 {@code uninstall} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UNINSTALL
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 节点监听地址；未设置时命令行不包含 {@code --host}。
     */
    private final String host;
    /**
     * 节点监听端口；未设置时命令行不包含 {@code --port}。
     */
    private final String port;
    /**
     * 是否向 openclaw 子命令追加 {@code --tls} 开关。
     */
    private final boolean tls;
    /**
     * 用于校验远端证书的 TLS 指纹；未设置时命令行不包含 {@code --tls-fingerprint}。
     */
    private final String tlsFingerprint;
    /**
     * 节点唯一标识；未设置时命令行不包含 {@code --node-id}。
     */
    private final String nodeId;
    /**
     * 面向用户展示的名称；未设置时命令行不包含 {@code --display-name}。
     */
    private final String displayName;
    /**
     * 节点运行时名称；未设置时命令行不包含 {@code --runtime}。
     */
    private final String runtime;
    /**
     * 是否向 openclaw 子命令追加 {@code --force} 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private NodeOptions(Builder b) {
        this.verb = b.verb;
        this.host = b.host;
        this.port = b.port;
        this.tls = b.tls;
        this.tlsFingerprint = b.tlsFingerprint;
        this.nodeId = b.nodeId;
        this.displayName = b.displayName;
        this.runtime = b.runtime;
        this.force = b.force;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code NodeOptions} 字段。
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
        switch (verb) {
            case RUN:
                out.add("run");
                break;
            case INSTALL:
                out.add("install");
                break;
            case STATUS:
                out.add("status");
                break;
            case STOP:
                out.add("stop");
                break;
            case RESTART:
                out.add("restart");
                break;
            case UNINSTALL:
                out.add("uninstall");
                break;
            default:
                break;
        }
        if (verb == Verb.RUN || verb == Verb.INSTALL) {
            OpenClawCliArgv.addIfPresent(out, "--host", host);
            OpenClawCliArgv.addIfPresent(out, "--port", port);
            OpenClawCliArgv.addFlag(out, "--tls", tls);
            OpenClawCliArgv.addIfPresent(out, "--tls-fingerprint", tlsFingerprint);
            OpenClawCliArgv.addIfPresent(out, "--node-id", nodeId);
            OpenClawCliArgv.addIfPresent(out, "--display-name", displayName);
            if (verb == Verb.INSTALL) {
                OpenClawCliArgv.addIfPresent(out, "--runtime", runtime);
                OpenClawCliArgv.addFlag(out, "--force", force);
            }
        }
        if (verb == Verb.STATUS || verb == Verb.STOP || verb == Verb.RESTART || verb == Verb.UNINSTALL) {
            OpenClawCliArgv.addFlag(out, "--json", json);
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code NodeOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.RUN;
        /**
         * 节点监听地址；未设置时命令行不包含 {@code --host}。
         */
        private String host;
        /**
         * 节点监听端口；未设置时命令行不包含 {@code --port}。
         */
        private String port;
        /**
         * 是否向 openclaw 子命令追加 {@code --tls} 开关。
         */
        private boolean tls;
        /**
         * 用于校验远端证书的 TLS 指纹；未设置时命令行不包含 {@code --tls-fingerprint}。
         */
        private String tlsFingerprint;
        /**
         * 节点唯一标识；未设置时命令行不包含 {@code --node-id}。
         */
        private String nodeId;
        /**
         * 面向用户展示的名称；未设置时命令行不包含 {@code --display-name}。
         */
        private String displayName;
        /**
         * 节点运行时名称；未设置时命令行不包含 {@code --runtime}。
         */
        private String runtime;
        /**
         * 是否向 openclaw 子命令追加 {@code --force} 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code run} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run() {
            this.verb = Verb.RUN;
            return this;
        }

        /**
         * 选择 {@code install} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install() {
            this.verb = Verb.INSTALL;
            return this;
        }

        /**
         * 选择 {@code status} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * 选择 {@code stop} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stop() {
            this.verb = Verb.STOP;
            return this;
        }

        /**
         * 选择 {@code restart} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder restart() {
            this.verb = Verb.RESTART;
            return this;
        }

        /**
         * 选择 {@code uninstall} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstall() {
            this.verb = Verb.UNINSTALL;
            return this;
        }

        /**
         * 设置 {@code --host} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 节点监听地址；作为 {@code --host} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * 设置 {@code --port} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param port 节点监听端口；作为 {@code --port} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder port(String port) {
            this.port = port;
            return this;
        }

        /**
         * 设置 {@code --tls} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tls 是否向命令行追加 {@code --tls} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tls(boolean tls) {
            this.tls = tls;
            return this;
        }

        /**
         * 设置 {@code --tls-fingerprint} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fingerprint 用于校验节点证书的 TLS 指纹；作为 {@code --tls-fingerprint} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tlsFingerprint(String fingerprint) {
            this.tlsFingerprint = fingerprint;
            return this;
        }

        /**
         * 设置 {@code --node-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeId 节点唯一标识；作为 {@code --node-id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        /**
         * 设置 {@code --display-name} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param displayName 面向用户展示的名称；作为 {@code --display-name} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * 设置 {@code --runtime} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param runtime 节点运行时名称；作为 {@code --runtime} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runtime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        /**
         * 设置 {@code --force} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 {@code --force} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) {
            this.force = force;
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
         * 校验并复制当前构建器字段，创建独立的 {@code NodeOptions}。
         *
         * @return 按当前字段创建的 NodeOptions
         */
        public NodeOptions build() {
            return new NodeOptions(this);
        }
    }
}
