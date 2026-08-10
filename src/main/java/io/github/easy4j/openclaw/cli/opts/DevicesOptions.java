package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `devices` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DevicesOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `remove` 协议模式；序列化时使用该固定取值。
         */
        REMOVE,
        /**
         * 选择 `clear` 协议模式；序列化时使用该固定取值。
         */
        CLEAR,
        /**
         * 选择 `approve` 协议模式；序列化时使用该固定取值。
         */
        APPROVE,
        /**
         * 选择 `reject` 协议模式；序列化时使用该固定取值。
         */
        REJECT,
        /**
         * 选择 `rotate` 协议模式；序列化时使用该固定取值。
         */
        ROTATE,
        /**
         * 选择 `revoke` 协议模式；序列化时使用该固定取值。
         */
        REVOKE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--device-id` 选项的内容；为 null 时通常省略。
     */
    private final String deviceId;
    /**
     * 是否向 openclaw 子命令追加 `--clear-yes` 开关。
     */
    private final boolean clearYes;
    /**
     * 是否向 openclaw 子命令追加 `--clear-pending` 开关。
     */
    private final boolean clearPending;
    /**
     * 传给 openclaw 子命令 `--request-id` 选项的内容；为 null 时通常省略。
     */
    private final String requestId;
    /**
     * 是否向 openclaw 子命令追加 `--approve-latest` 开关。
     */
    private final boolean approveLatest;
    /**
     * 传给 openclaw 子命令 `--role` 选项的内容；为 null 时通常省略。
     */
    private final String role;
    /**
     * 传给 openclaw 子命令 `--scopes` 选项的内容；为 null 时通常省略。
     */
    private final List<String> scopes;
    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String timeout;
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
    private DevicesOptions(Builder b) {
        this.verb = b.verb;
        this.deviceId = b.deviceId;
        this.clearYes = b.clearYes;
        this.clearPending = b.clearPending;
        this.requestId = b.requestId;
        this.approveLatest = b.approveLatest;
        this.role = b.role;
        this.scopes = b.scopes == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.scopes);
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `DevicesOptions` 字段。
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
            case LIST:
                out.add("list");
                break;
            case REMOVE:
                out.add("remove");
                if (deviceId != null && OpenClawStrings.isNotBlank(deviceId)) {
                    out.add(deviceId.trim());
                }
                break;
            case CLEAR:
                out.add("clear");
                OpenClawCliArgv.addFlag(out, "--yes", clearYes);
                OpenClawCliArgv.addFlag(out, "--pending", clearPending);
                break;
            case APPROVE:
                out.add("approve");
                if (approveLatest) {
                    out.add("--latest");
                } else if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case REJECT:
                out.add("reject");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case ROTATE:
                out.add("rotate");
                OpenClawCliArgv.addIfPresent(out, "--device", deviceId);
                OpenClawCliArgv.addIfPresent(out, "--role", role);
                for (String s : scopes) {
                    if (s != null && OpenClawStrings.isNotBlank(s)) {
                        out.add("--scope");
                        out.add(s.trim());
                    }
                }
                break;
            case REVOKE:
                out.add("revoke");
                OpenClawCliArgv.addIfPresent(out, "--device", deviceId);
                OpenClawCliArgv.addIfPresent(out, "--role", role);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addIfPresent(out, "--timeout", timeout);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 DevicesOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 DevicesOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.LIST;
        /**
         * 传给 openclaw 子命令 `--device-id` 选项的内容；为 null 时通常省略。
         */
        private String deviceId;
        /**
         * 是否向 openclaw 子命令追加 `--clear-yes` 开关。
         */
        private boolean clearYes;
        /**
         * 是否向 openclaw 子命令追加 `--clear-pending` 开关。
         */
        private boolean clearPending;
        /**
         * 传给 openclaw 子命令 `--request-id` 选项的内容；为 null 时通常省略。
         */
        private String requestId;
        /**
         * 是否向 openclaw 子命令追加 `--approve-latest` 开关。
         */
        private boolean approveLatest;
        /**
         * 传给 openclaw 子命令 `--role` 选项的内容；为 null 时通常省略。
         */
        private String role;
        /**
         * 传给 openclaw 子命令 `--scopes` 选项的内容；为 null 时通常省略。
         */
        private List<String> scopes = new ArrayList<>();
        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 `--remove` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 写入 `--remove` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove(String deviceId) {
            this.verb = Verb.REMOVE;
            this.deviceId = deviceId;
            return this;
        }

        /**
         * 设置 `--clear` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 `--clear` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clear(boolean yes) {
            this.verb = Verb.CLEAR;
            this.clearYes = yes;
            return this;
        }

        /**
         * 设置 `--clear-pending` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pending 是否向命令行追加 `--clear-pending` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clearPending(boolean pending) {
            this.clearPending = pending;
            return this;
        }

        /**
         * 选择 `approve` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve() {
            this.verb = Verb.APPROVE;
            this.requestId = null;
            this.approveLatest = false;
            return this;
        }

        /**
         * 设置 `--approve` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requestId 请求标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve(String requestId) {
            this.verb = Verb.APPROVE;
            this.requestId = requestId;
            this.approveLatest = false;
            return this;
        }

        /**
         * 设置 `--approve-latest` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param latest 是否向命令行追加 `--approve-latest` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approveLatest(boolean latest) {
            this.approveLatest = latest;
            return this;
        }

        /**
         * 设置 `--reject` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requestId 请求标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder reject(String requestId) {
            this.verb = Verb.REJECT;
            this.requestId = requestId;
            return this;
        }

        /**
         * 设置 `--rotate` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 写入 `--rotate` 选项的内容
         * @param role 写入 `--rotate` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rotate(String deviceId, String role) {
            this.verb = Verb.ROTATE;
            this.deviceId = deviceId;
            this.role = role;
            return this;
        }

        /**
         * 设置 `--scope` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param scope 写入 `--scope` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scope(String scope) {
            if (scope != null && OpenClawStrings.isNotBlank(scope)) {
                scopes.add(scope.trim());
            }
            return this;
        }

        /**
         * 设置 `--revoke` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 写入 `--revoke` 选项的内容
         * @param role 写入 `--revoke` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder revoke(String deviceId, String role) {
            this.verb = Verb.REVOKE;
            this.deviceId = deviceId;
            this.role = role;
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
         * 校验并复制当前构建器字段，创建独立的 `DevicesOptions`。
         *
         * @return 按当前字段创建的 DevicesOptions
         */
        public DevicesOptions build() {
            return new DevicesOptions(this);
        }
    }
}
