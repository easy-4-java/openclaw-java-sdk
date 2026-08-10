package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `system` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SystemOptions implements CliSubArgs {

    /**
     * `HeartbeatSub` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum HeartbeatSub {
        /**
         * 选择 `last` 协议模式；序列化时使用该固定取值。
         */
        LAST,
        /**
         * 选择 `enable` 协议模式；序列化时使用该固定取值。
         */
        ENABLE,
        /**
         * 选择 `disable` 协议模式；序列化时使用该固定取值。
         */
        DISABLE
    }

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `event` 协议模式；序列化时使用该固定取值。
         */
        EVENT,
        /**
         * 选择 `heartbeat` 协议模式；序列化时使用该固定取值。
         */
        HEARTBEAT,
        /**
         * 选择 `presence` 协议模式；序列化时使用该固定取值。
         */
        PRESENCE
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--heartbeat-sub` 选项的内容；为 null 时通常省略。
     */
    private final HeartbeatSub heartbeatSub;
    /**
     * 传给 openclaw 子命令 `--gateway-url` 选项的内容；为 null 时通常省略。
     */
    private final String gatewayUrl;
    /**
     * 传给 openclaw 子命令 `--gateway-token` 选项的内容；为 null 时通常省略。
     */
    private final String gatewayToken;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 `--expect-final` 开关。
     */
    private final boolean expectFinal;
    /**
     * 传给 openclaw 子命令 `--event-text` 选项的内容；为 null 时通常省略。
     */
    private final String eventText;
    /**
     * 传给 openclaw 子命令 `--event-mode` 选项的内容；为 null 时通常省略。
     */
    private final String eventMode;
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
    private SystemOptions(Builder b) {
        this.mode = b.mode;
        this.heartbeatSub = b.heartbeatSub;
        this.gatewayUrl = b.gatewayUrl;
        this.gatewayToken = b.gatewayToken;
        this.timeout = b.timeout;
        this.expectFinal = b.expectFinal;
        this.eventText = b.eventText;
        this.eventMode = b.eventMode;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `SystemOptions` 字段。
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
        switch (mode) {
            case EVENT:
                out.add("event");
                OpenClawCliArgv.addIfPresent(out, "--text", eventText);
                OpenClawCliArgv.addIfPresent(out, "--mode", eventMode);
                break;
            case HEARTBEAT:
                out.add("heartbeat");
                if (heartbeatSub == HeartbeatSub.LAST) {
                    out.add("last");
                } else if (heartbeatSub == HeartbeatSub.ENABLE) {
                    out.add("enable");
                } else if (heartbeatSub == HeartbeatSub.DISABLE) {
                    out.add("disable");
                }
                break;
            case PRESENCE:
                out.add("presence");
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--url", gatewayUrl);
        OpenClawCliArgv.addIfPresent(out, "--token", gatewayToken);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addFlag(out, "--expect-final", expectFinal);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 SystemOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 SystemOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.PRESENCE;
        /**
         * 传给 openclaw 子命令 `--heartbeat-sub` 选项的内容；为 null 时通常省略。
         */
        private HeartbeatSub heartbeatSub = HeartbeatSub.LAST;
        /**
         * 传给 openclaw 子命令 `--gateway-url` 选项的内容；为 null 时通常省略。
         */
        private String gatewayUrl;
        /**
         * 传给 openclaw 子命令 `--gateway-token` 选项的内容；为 null 时通常省略。
         */
        private String gatewayToken;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 `--expect-final` 开关。
         */
        private boolean expectFinal;
        /**
         * 传给 openclaw 子命令 `--event-text` 选项的内容；为 null 时通常省略。
         */
        private String eventText;
        /**
         * 传给 openclaw 子命令 `--event-mode` 选项的内容；为 null 时通常省略。
         */
        private String eventMode;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--event` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param text 写入 `--event` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder event(String text) {
            this.mode = Mode.EVENT;
            this.eventText = text;
            return this;
        }

        /**
         * 设置 `--event-mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param eventMode 写入 `--event-mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder eventMode(String eventMode) {
            this.eventMode = eventMode;
            return this;
        }

        /**
         * 选择 `heartbeatLast` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatLast() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.LAST;
            return this;
        }

        /**
         * 选择 `heartbeatEnable` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatEnable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.ENABLE;
            return this;
        }

        /**
         * 选择 `heartbeatDisable` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatDisable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.DISABLE;
            return this;
        }

        /**
         * 选择 `presence` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder presence() {
            this.mode = Mode.PRESENCE;
            return this;
        }

        /**
         * 设置 `--gateway-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * 设置 `--gateway-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--gateway-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
            return this;
        }

        /**
         * 设置 `--timeout` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout 写入 `--timeout` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置 `--expect-final` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expectFinal 是否向命令行追加 `--expect-final` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder expectFinal(boolean expectFinal) {
            this.expectFinal = expectFinal;
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
         * 校验并复制当前构建器字段，创建独立的 `SystemOptions`。
         *
         * @return 按当前字段创建的 SystemOptions
         */
        public SystemOptions build() {
            return new SystemOptions(this);
        }
    }
}
