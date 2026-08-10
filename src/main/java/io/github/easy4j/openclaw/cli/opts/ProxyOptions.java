package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `proxy` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ProxyOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `start` 协议模式；序列化时使用该固定取值。
         */
        START,
        /**
         * 选择 `run` 协议模式；序列化时使用该固定取值。
         */
        RUN,
        /**
         * 选择 `validate` 协议模式；序列化时使用该固定取值。
         */
        VALIDATE,
        /**
         * 选择 `coverage` 协议模式；序列化时使用该固定取值。
         */
        COVERAGE,
        /**
         * 选择 `sessions` 协议模式；序列化时使用该固定取值。
         */
        SESSIONS,
        /**
         * 选择 `query` 协议模式；序列化时使用该固定取值。
         */
        QUERY,
        /**
         * 选择 `blob` 协议模式；序列化时使用该固定取值。
         */
        BLOB,
        /**
         * 选择 `purge` 协议模式；序列化时使用该固定取值。
         */
        PURGE
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--run-command` 选项的内容；为 null 时通常省略。
     */
    private final List<String> runCommand;
    /**
     * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
     */
    private final String host;
    /**
     * 传给 openclaw 子命令 `--port` 选项的内容；为 null 时通常省略。
     */
    private final Integer port;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--proxy-url` 选项的内容；为 null 时通常省略。
     */
    private final String proxyUrl;
    /**
     * 传给 openclaw 子命令 `--proxy-ca-file` 选项的内容；为 null 时通常省略。
     */
    private final String proxyCaFile;
    /**
     * 传给 openclaw 子命令 `--allowed-urls` 选项的内容；为 null 时通常省略。
     */
    private final List<String> allowedUrls;
    /**
     * 传给 openclaw 子命令 `--denied-urls` 选项的内容；为 null 时通常省略。
     */
    private final List<String> deniedUrls;
    /**
     * 是否向 openclaw 子命令追加 `--apns-reachable` 开关。
     */
    private final boolean apnsReachable;
    /**
     * 传给 openclaw 子命令 `--apns-authority` 选项的内容；为 null 时通常省略。
     */
    private final String apnsAuthority;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final Integer timeoutMs;
    /**
     * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
     */
    private final Integer limit;
    /**
     * 传给 openclaw 子命令 `--preset` 选项的内容；为 null 时通常省略。
     */
    private final String preset;
    /**
     * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
     */
    private final String session;
    /**
     * 传给 openclaw 子命令 `--blob-id` 选项的内容；为 null 时通常省略。
     */
    private final String blobId;

    private ProxyOptions(Builder b) {
        this.mode = b.mode;
        this.runCommand = OpenClawLists.copyOf(b.runCommand);
        this.host = b.host;
        this.port = b.port;
        this.json = b.json;
        this.proxyUrl = b.proxyUrl;
        this.proxyCaFile = b.proxyCaFile;
        this.allowedUrls = OpenClawLists.copyOf(b.allowedUrls);
        this.deniedUrls = OpenClawLists.copyOf(b.deniedUrls);
        this.apnsReachable = b.apnsReachable;
        this.apnsAuthority = b.apnsAuthority;
        this.timeoutMs = b.timeoutMs;
        this.limit = b.limit;
        this.preset = b.preset;
        this.session = b.session;
        this.blobId = b.blobId;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `ProxyOptions` 字段。
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
        if (mode != null) {
            out.add(mode.name().toLowerCase());
        }
        if (runCommand != null && !runCommand.isEmpty()) {
            out.addAll(runCommand);
        }
        OpenClawCliArgv.addIfPresent(out, "--host", host);
        OpenClawCliArgv.addIfNotNull(out, "--port", port);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addIfPresent(out, "--proxy-url", proxyUrl);
        OpenClawCliArgv.addIfPresent(out, "--proxy-ca-file", proxyCaFile);
        OpenClawCliArgv.addRepeatable(out, "--allowed-url", allowedUrls);
        OpenClawCliArgv.addRepeatable(out, "--denied-url", deniedUrls);
        OpenClawCliArgv.addFlag(out, "--apns-reachable", apnsReachable);
        OpenClawCliArgv.addIfPresent(out, "--apns-authority", apnsAuthority);
        OpenClawCliArgv.addIfNotNull(out, "--timeout-ms", timeoutMs);
        OpenClawCliArgv.addIfNotNull(out, "--limit", limit);
        OpenClawCliArgv.addIfPresent(out, "--preset", preset);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--id", blobId);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 ProxyOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ProxyOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.START;
        /**
         * 传给 openclaw 子命令 `--run-command` 选项的内容；为 null 时通常省略。
         */
        private List<String> runCommand;
        /**
         * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
         */
        private String host;
        /**
         * 传给 openclaw 子命令 `--port` 选项的内容；为 null 时通常省略。
         */
        private Integer port;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--proxy-url` 选项的内容；为 null 时通常省略。
         */
        private String proxyUrl;
        /**
         * 传给 openclaw 子命令 `--proxy-ca-file` 选项的内容；为 null 时通常省略。
         */
        private String proxyCaFile;
        /**
         * 传给 openclaw 子命令 `--allowed-urls` 选项的内容；为 null 时通常省略。
         */
        private List<String> allowedUrls;
        /**
         * 传给 openclaw 子命令 `--denied-urls` 选项的内容；为 null 时通常省略。
         */
        private List<String> deniedUrls;
        /**
         * 是否向 openclaw 子命令追加 `--apns-reachable` 开关。
         */
        private boolean apnsReachable;
        /**
         * 传给 openclaw 子命令 `--apns-authority` 选项的内容；为 null 时通常省略。
         */
        private String apnsAuthority;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private Integer timeoutMs;
        /**
         * 传给 openclaw 子命令 `--limit` 选项的内容；为 null 时通常省略。
         */
        private Integer limit;
        /**
         * 传给 openclaw 子命令 `--preset` 选项的内容；为 null 时通常省略。
         */
        private String preset;
        /**
         * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
         */
        private String session;
        /**
         * 传给 openclaw 子命令 `--blob-id` 选项的内容；为 null 时通常省略。
         */
        private String blobId;

        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 选择 `start` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder start() { this.mode = Mode.START; return this; }
        /**
         * 设置 `--run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param cmd 写入 `--run` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run(List<String> cmd) { this.mode = Mode.RUN; this.runCommand = cmd; return this; }
        /**
         * 选择 `validate` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder validate() { this.mode = Mode.VALIDATE; return this; }
        /**
         * 选择 `coverage` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder coverage() { this.mode = Mode.COVERAGE; return this; }
        /**
         * 选择 `sessions` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessions() { this.mode = Mode.SESSIONS; return this; }
        /**
         * 选择 `query` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder query() { this.mode = Mode.QUERY; return this; }
        /**
         * 选择 `blob` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder blob() { this.mode = Mode.BLOB; return this; }
        /**
         * 选择 `purge` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder purge() { this.mode = Mode.PURGE; return this; }
        /**
         * 设置 `--host` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 写入 `--host` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) { this.host = host; return this; }
        /**
         * 设置 `--port` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param port 写入 `--port` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder port(Integer port) { this.port = port; return this; }
        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }
        /**
         * 设置 `--proxy-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param proxyUrl 写入 `--proxy-url` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder proxyUrl(String proxyUrl) { this.proxyUrl = proxyUrl; return this; }
        /**
         * 设置 `--proxy-ca-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param proxyCaFile 写入 `--proxy-ca-file` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder proxyCaFile(String proxyCaFile) { this.proxyCaFile = proxyCaFile; return this; }
        /**
         * 设置 `--allowed-urls` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param urls 写入 `--allowed-urls` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowedUrls(List<String> urls) { this.allowedUrls = urls; return this; }
        /**
         * 设置 `--denied-urls` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param urls 写入 `--denied-urls` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deniedUrls(List<String> urls) { this.deniedUrls = urls; return this; }
        /**
         * 设置 `--apns-reachable` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apnsReachable 是否向命令行追加 `--apns-reachable` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apnsReachable(boolean apnsReachable) { this.apnsReachable = apnsReachable; return this; }
        /**
         * 设置 `--apns-authority` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apnsAuthority 写入 `--apns-authority` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apnsAuthority(String apnsAuthority) { this.apnsAuthority = apnsAuthority; return this; }
        /**
         * 设置 `--timeout-ms` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        /**
         * 设置 `--limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 写入 `--limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(Integer limit) { this.limit = limit; return this; }
        /**
         * 设置 `--preset` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param preset 写入 `--preset` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder preset(String preset) { this.preset = preset; return this; }
        /**
         * 设置 `--session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 写入 `--session` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) { this.session = session; return this; }
        /**
         * 设置 `--blob-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param blobId 写入 `--blob-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder blobId(String blobId) { this.blobId = blobId; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `ProxyOptions`。
         *
         * @return 按当前字段创建的 ProxyOptions
         */
        public ProxyOptions build() {
            return new ProxyOptions(this);
        }
    }
}
