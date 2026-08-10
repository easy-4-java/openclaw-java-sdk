package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code system} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SystemOptions implements CliSubArgs {

    /**
     * 定义系统维护动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum HeartbeatSub {
        /**
         * 表示系统维护动作的 {@code last} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LAST,
        /**
         * 表示系统维护动作的 {@code enable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ENABLE,
        /**
         * 表示系统维护动作的 {@code disable} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DISABLE
    }

    /**
     * 定义系统维护动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示系统维护动作的 {@code event} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        EVENT,
        /**
         * 表示系统维护动作的 {@code heartbeat} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        HEARTBEAT,
        /**
         * 表示系统维护动作的 {@code presence} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PRESENCE
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 心跳查询的子命令或范围；未设置时命令行不包含 {@code --heartbeat-sub}。
     */
    private final HeartbeatSub heartbeatSub;
    /**
     * Gateway 服务地址；未设置时命令行不包含 {@code --gateway-url}。
     */
    private final String gatewayUrl;
    /**
     * Gateway Bearer Token；未设置时命令行不包含 {@code --gateway-token}。
     */
    private final String gatewayToken;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
    /**
     * 是否向 openclaw 子命令追加 {@code --expect-final} 开关。
     */
    private final boolean expectFinal;
    /**
     * 系统事件正文；未设置时命令行不包含 {@code --event-text}。
     */
    private final String eventText;
    /**
     * 系统事件的投递模式；未设置时命令行不包含 {@code --event-mode}。
     */
    private final String eventMode;
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
     * 创建空白构建器，供调用方链式设置 {@code SystemOptions} 字段。
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
     * {@code SystemOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.PRESENCE;
        /**
         * 心跳查询的子命令或范围；未设置时命令行不包含 {@code --heartbeat-sub}。
         */
        private HeartbeatSub heartbeatSub = HeartbeatSub.LAST;
        /**
         * Gateway 服务地址；未设置时命令行不包含 {@code --gateway-url}。
         */
        private String gatewayUrl;
        /**
         * Gateway Bearer Token；未设置时命令行不包含 {@code --gateway-token}。
         */
        private String gatewayToken;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 {@code --expect-final} 开关。
         */
        private boolean expectFinal;
        /**
         * 系统事件正文；未设置时命令行不包含 {@code --event-text}。
         */
        private String eventText;
        /**
         * 系统事件的投递模式；未设置时命令行不包含 {@code --event-mode}。
         */
        private String eventMode;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 {@code --event} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param text 系统事件正文；作为 {@code --event} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder event(String text) {
            this.mode = Mode.EVENT;
            this.eventText = text;
            return this;
        }

        /**
         * 设置 {@code --event-mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param eventMode 系统事件的投递模式；作为 {@code --event-mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder eventMode(String eventMode) {
            this.eventMode = eventMode;
            return this;
        }

        /**
         * 选择 {@code heartbeatLast} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatLast() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.LAST;
            return this;
        }

        /**
         * 选择 {@code heartbeatEnable} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatEnable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.ENABLE;
            return this;
        }

        /**
         * 选择 {@code heartbeatDisable} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder heartbeatDisable() {
            this.mode = Mode.HEARTBEAT;
            this.heartbeatSub = HeartbeatSub.DISABLE;
            return this;
        }

        /**
         * 选择 {@code presence} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder presence() {
            this.mode = Mode.PRESENCE;
            return this;
        }

        /**
         * 设置 {@code --gateway-url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayUrl(String url) {
            this.gatewayUrl = url;
            return this;
        }

        /**
         * 设置 {@code --gateway-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --gateway-token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayToken(String token) {
            this.gatewayToken = token;
            return this;
        }

        /**
         * 设置 {@code --timeout} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeout CLI 接受的超时配置；作为 {@code --timeout} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置 {@code --expect-final} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expectFinal 是否向命令行追加 {@code --expect-final} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder expectFinal(boolean expectFinal) {
            this.expectFinal = expectFinal;
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
         * 校验并复制当前构建器字段，创建独立的 {@code SystemOptions}。
         *
         * @return 按当前字段创建的 SystemOptions
         */
        public SystemOptions build() {
            return new SystemOptions(this);
        }
    }
}
