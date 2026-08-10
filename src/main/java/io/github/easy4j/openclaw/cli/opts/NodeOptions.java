package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `node` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class NodeOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `run` 协议模式；序列化时使用该固定取值。
         */
        RUN,
        /**
         * 选择 `install` 协议模式；序列化时使用该固定取值。
         */
        INSTALL,
        /**
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS,
        /**
         * 选择 `stop` 协议模式；序列化时使用该固定取值。
         */
        STOP,
        /**
         * 选择 `restart` 协议模式；序列化时使用该固定取值。
         */
        RESTART,
        /**
         * 选择 `uninstall` 协议模式；序列化时使用该固定取值。
         */
        UNINSTALL
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
     */
    private final String host;
    /**
     * 传给 openclaw 子命令 `--port` 选项的内容；为 null 时通常省略。
     */
    private final String port;
    /**
     * 是否向 openclaw 子命令追加 `--tls` 开关。
     */
    private final boolean tls;
    /**
     * 传给 openclaw 子命令 `--tls-fingerprint` 选项的内容；为 null 时通常省略。
     */
    private final String tlsFingerprint;
    /**
     * 传给 openclaw 子命令 `--node-id` 选项的内容；为 null 时通常省略。
     */
    private final String nodeId;
    /**
     * 传给 openclaw 子命令 `--display-name` 选项的内容；为 null 时通常省略。
     */
    private final String displayName;
    /**
     * 传给 openclaw 子命令 `--runtime` 选项的内容；为 null 时通常省略。
     */
    private final String runtime;
    /**
     * 是否向 openclaw 子命令追加 `--force` 开关。
     */
    private final boolean force;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `NodeOptions` 字段。
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
     * 链式构建器，逐项收集 NodeOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 NodeOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.RUN;
        /**
         * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
         */
        private String host;
        /**
         * 传给 openclaw 子命令 `--port` 选项的内容；为 null 时通常省略。
         */
        private String port;
        /**
         * 是否向 openclaw 子命令追加 `--tls` 开关。
         */
        private boolean tls;
        /**
         * 传给 openclaw 子命令 `--tls-fingerprint` 选项的内容；为 null 时通常省略。
         */
        private String tlsFingerprint;
        /**
         * 传给 openclaw 子命令 `--node-id` 选项的内容；为 null 时通常省略。
         */
        private String nodeId;
        /**
         * 传给 openclaw 子命令 `--display-name` 选项的内容；为 null 时通常省略。
         */
        private String displayName;
        /**
         * 传给 openclaw 子命令 `--runtime` 选项的内容；为 null 时通常省略。
         */
        private String runtime;
        /**
         * 是否向 openclaw 子命令追加 `--force` 开关。
         */
        private boolean force;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `run` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run() {
            this.verb = Verb.RUN;
            return this;
        }

        /**
         * 选择 `install` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder install() {
            this.verb = Verb.INSTALL;
            return this;
        }

        /**
         * 选择 `status` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.verb = Verb.STATUS;
            return this;
        }

        /**
         * 选择 `stop` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder stop() {
            this.verb = Verb.STOP;
            return this;
        }

        /**
         * 选择 `restart` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder restart() {
            this.verb = Verb.RESTART;
            return this;
        }

        /**
         * 选择 `uninstall` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder uninstall() {
            this.verb = Verb.UNINSTALL;
            return this;
        }

        /**
         * 设置 `--host` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 写入 `--host` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * 设置 `--port` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param port 写入 `--port` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder port(String port) {
            this.port = port;
            return this;
        }

        /**
         * 设置 `--tls` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tls 是否向命令行追加 `--tls` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tls(boolean tls) {
            this.tls = tls;
            return this;
        }

        /**
         * 设置 `--tls-fingerprint` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param fingerprint 写入 `--tls-fingerprint` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tlsFingerprint(String fingerprint) {
            this.tlsFingerprint = fingerprint;
            return this;
        }

        /**
         * 设置 `--node-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeId 写入 `--node-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        /**
         * 设置 `--display-name` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param displayName 写入 `--display-name` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * 设置 `--runtime` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param runtime 写入 `--runtime` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runtime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        /**
         * 设置 `--force` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param force 是否向命令行追加 `--force` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder force(boolean force) {
            this.force = force;
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
         * 校验并复制当前构建器字段，创建独立的 `NodeOptions`。
         *
         * @return 按当前字段创建的 NodeOptions
         */
        public NodeOptions build() {
            return new NodeOptions(this);
        }
    }
}
