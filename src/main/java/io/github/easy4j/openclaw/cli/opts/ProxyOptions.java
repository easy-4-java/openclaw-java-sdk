package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code proxy} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ProxyOptions implements CliSubArgs {

    /**
     * 定义代理管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示代理管理动作的 {@code start} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        START,
        /**
         * 表示代理管理动作的 {@code run} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RUN,
        /**
         * 表示代理管理动作的 {@code validate} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        VALIDATE,
        /**
         * 表示代理管理动作的 {@code coverage} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        COVERAGE,
        /**
         * 表示代理管理动作的 {@code sessions} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SESSIONS,
        /**
         * 表示代理管理动作的 {@code query} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        QUERY,
        /**
         * 表示代理管理动作的 {@code blob} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        BLOB,
        /**
         * 表示代理管理动作的 {@code purge} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PURGE
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 节点要执行的命令；未设置时命令行不包含 {@code --run-command}。
     */
    private final List<String> runCommand;
    /**
     * 节点监听地址；未设置时命令行不包含 {@code --host}。
     */
    private final String host;
    /**
     * 节点监听端口；未设置时命令行不包含 {@code --port}。
     */
    private final Integer port;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;
    /**
     * 出站代理 URL；未设置时命令行不包含 {@code --proxy-url}。
     */
    private final String proxyUrl;
    /**
     * 代理 TLS CA 证书文件；未设置时命令行不包含 {@code --proxy-ca-file}。
     */
    private final String proxyCaFile;
    /**
     * 允许节点访问的 URL 列表；未设置时命令行不包含 {@code --allowed-urls}。
     */
    private final List<String> allowedUrls;
    /**
     * 禁止节点访问的 URL 列表；未设置时命令行不包含 {@code --denied-urls}。
     */
    private final List<String> deniedUrls;
    /**
     * 是否向 openclaw 子命令追加 {@code --apns-reachable} 开关。
     */
    private final boolean apnsReachable;
    /**
     * APNs 服务的 authority；未设置时命令行不包含 {@code --apns-authority}。
     */
    private final String apnsAuthority;
    /**
     * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
     */
    private final Integer timeoutMs;
    /**
     * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
     */
    private final Integer limit;
    /**
     * 预定义执行策略名称；未设置时命令行不包含 {@code --preset}。
     */
    private final String preset;
    /**
     * 目标会话标识；未设置时命令行不包含 {@code --session}。
     */
    private final String session;
    /**
     * 目标二进制对象标识；未设置时命令行不包含 {@code --blob-id}。
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
     * 创建空白构建器，供调用方链式设置 {@code ProxyOptions} 字段。
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
     * {@code ProxyOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.START;
        /**
         * 节点要执行的命令；未设置时命令行不包含 {@code --run-command}。
         */
        private List<String> runCommand;
        /**
         * 节点监听地址；未设置时命令行不包含 {@code --host}。
         */
        private String host;
        /**
         * 节点监听端口；未设置时命令行不包含 {@code --port}。
         */
        private Integer port;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 出站代理 URL；未设置时命令行不包含 {@code --proxy-url}。
         */
        private String proxyUrl;
        /**
         * 代理 TLS CA 证书文件；未设置时命令行不包含 {@code --proxy-ca-file}。
         */
        private String proxyCaFile;
        /**
         * 允许节点访问的 URL 列表；未设置时命令行不包含 {@code --allowed-urls}。
         */
        private List<String> allowedUrls;
        /**
         * 禁止节点访问的 URL 列表；未设置时命令行不包含 {@code --denied-urls}。
         */
        private List<String> deniedUrls;
        /**
         * 是否向 openclaw 子命令追加 {@code --apns-reachable} 开关。
         */
        private boolean apnsReachable;
        /**
         * APNs 服务的 authority；未设置时命令行不包含 {@code --apns-authority}。
         */
        private String apnsAuthority;
        /**
         * 该请求或进程允许等待的最长时间，单位为毫秒；超时后主动取消对应任务。
         */
        private Integer timeoutMs;
        /**
         * 返回结果数量上限；未设置时命令行不包含 {@code --limit}。
         */
        private Integer limit;
        /**
         * 预定义执行策略名称；未设置时命令行不包含 {@code --preset}。
         */
        private String preset;
        /**
         * 目标会话标识；未设置时命令行不包含 {@code --session}。
         */
        private String session;
        /**
         * 目标二进制对象标识；未设置时命令行不包含 {@code --blob-id}。
         */
        private String blobId;

        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 选择 {@code start} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder start() { this.mode = Mode.START; return this; }
        /**
         * 设置 {@code --run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param cmd 要由节点执行的命令；作为 {@code --run} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run(List<String> cmd) { this.mode = Mode.RUN; this.runCommand = cmd; return this; }
        /**
         * 选择 {@code validate} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder validate() { this.mode = Mode.VALIDATE; return this; }
        /**
         * 选择 {@code coverage} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder coverage() { this.mode = Mode.COVERAGE; return this; }
        /**
         * 选择 {@code sessions} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder sessions() { this.mode = Mode.SESSIONS; return this; }
        /**
         * 选择 {@code query} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder query() { this.mode = Mode.QUERY; return this; }
        /**
         * 选择 {@code blob} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder blob() { this.mode = Mode.BLOB; return this; }
        /**
         * 选择 {@code purge} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder purge() { this.mode = Mode.PURGE; return this; }
        /**
         * 设置 {@code --host} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 节点监听地址；作为 {@code --host} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) { this.host = host; return this; }
        /**
         * 设置 {@code --port} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param port 节点监听端口；作为 {@code --port} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder port(Integer port) { this.port = port; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }
        /**
         * 设置 {@code --proxy-url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param proxyUrl 出站代理 URL；作为 {@code --proxy-url} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder proxyUrl(String proxyUrl) { this.proxyUrl = proxyUrl; return this; }
        /**
         * 设置 {@code --proxy-ca-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param proxyCaFile 代理 TLS CA 证书文件；作为 {@code --proxy-ca-file} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder proxyCaFile(String proxyCaFile) { this.proxyCaFile = proxyCaFile; return this; }
        /**
         * 设置 {@code --allowed-urls} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param urls 允许或拒绝访问的 URL 集合；作为 {@code --allowed-urls} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowedUrls(List<String> urls) { this.allowedUrls = urls; return this; }
        /**
         * 设置 {@code --denied-urls} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param urls 允许或拒绝访问的 URL 集合；作为 {@code --denied-urls} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder deniedUrls(List<String> urls) { this.deniedUrls = urls; return this; }
        /**
         * 设置 {@code --apns-reachable} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apnsReachable 是否向命令行追加 {@code --apns-reachable} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apnsReachable(boolean apnsReachable) { this.apnsReachable = apnsReachable; return this; }
        /**
         * 设置 {@code --apns-authority} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param apnsAuthority APNs 服务的 authority；作为 {@code --apns-authority} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder apnsAuthority(String apnsAuthority) { this.apnsAuthority = apnsAuthority; return this; }
        /**
         * 设置 {@code --timeout-ms} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param timeoutMs 超时时间，单位为毫秒
         * @return 当前构建器，便于继续链式配置
         */
        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        /**
         * 设置 {@code --limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 返回结果数量上限；作为 {@code --limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder limit(Integer limit) { this.limit = limit; return this; }
        /**
         * 设置 {@code --preset} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param preset 预定义执行策略名称；作为 {@code --preset} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder preset(String preset) { this.preset = preset; return this; }
        /**
         * 设置 {@code --session} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 目标会话标识；作为 {@code --session} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) { this.session = session; return this; }
        /**
         * 设置 {@code --blob-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param blobId 目标二进制对象标识；作为 {@code --blob-id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder blobId(String blobId) { this.blobId = blobId; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ProxyOptions}。
         *
         * @return 按当前字段创建的 ProxyOptions
         */
        public ProxyOptions build() {
            return new ProxyOptions(this);
        }
    }
}
