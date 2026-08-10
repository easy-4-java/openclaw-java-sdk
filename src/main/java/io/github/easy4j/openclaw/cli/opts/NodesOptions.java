package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code nodes} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class NodesOptions implements CliSubArgs {

    /**
     * 定义节点管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示节点管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示节点管理动作的 {@code pending} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PENDING,
        /**
         * 表示节点管理动作的 {@code approve} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        APPROVE,
        /**
         * 表示节点管理动作的 {@code reject} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REJECT,
        /**
         * 表示节点管理动作的 {@code rename} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RENAME,
        /**
         * 表示节点管理动作的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS,
        /**
         * 表示节点管理动作的 {@code invoke} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        INVOKE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 是否向 openclaw 子命令追加 {@code --list-connected} 开关。
     */
    private final boolean listConnected;
    /**
     * 筛选节点最近连接时间的范围；未设置时命令行不包含 {@code --last-connected}。
     */
    private final String lastConnected;
    /**
     * 请求关联标识；未设置时命令行不包含 {@code --request-id}。
     */
    private final String requestId;
    /**
     * 目标节点名称或标识；未设置时命令行不包含 {@code --node-ref}。
     */
    private final String nodeRef;
    /**
     * 目标资源名称；未设置时命令行不包含 {@code --name}。
     */
    private final String name;
    /**
     * 节点 RPC 方法或命令名称；未设置时命令行不包含 {@code --command}。
     */
    private final String command;
    /**
     * 节点命令的 JSON 参数；未设置时命令行不包含 {@code --params-json}。
     */
    private final String paramsJson;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String invokeTimeout;
    /**
     * 防止请求重复执行的幂等键；未设置时命令行不包含 {@code --idempotency-key}。
     */
    private final String idempotencyKey;
    /**
     * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
     */
    private final String url;
    /**
     * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
     */
    private final String token;
    /**
     * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
     */
    private final String password;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String timeout;
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
    private NodesOptions(Builder b) {
        this.verb = b.verb;
        this.listConnected = b.listConnected;
        this.lastConnected = b.lastConnected;
        this.requestId = b.requestId;
        this.nodeRef = b.nodeRef;
        this.name = b.name;
        this.command = b.command;
        this.paramsJson = b.paramsJson;
        this.invokeTimeout = b.invokeTimeout;
        this.idempotencyKey = b.idempotencyKey;
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code NodesOptions} 字段。
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
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case PENDING:
                out.add("pending");
                break;
            case APPROVE:
                out.add("approve");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case REJECT:
                out.add("reject");
                if (requestId != null && OpenClawStrings.isNotBlank(requestId)) {
                    out.add(requestId.trim());
                }
                break;
            case RENAME:
                out.add("rename");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--name", name);
                break;
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--connected", listConnected);
                OpenClawCliArgv.addIfPresent(out, "--last-connected", lastConnected);
                break;
            case INVOKE:
                out.add("invoke");
                OpenClawCliArgv.addIfPresent(out, "--node", nodeRef);
                OpenClawCliArgv.addIfPresent(out, "--command", command);
                OpenClawCliArgv.addIfPresent(out, "--params", paramsJson);
                OpenClawCliArgv.addIfPresent(out, "--invoke-timeout", invokeTimeout);
                OpenClawCliArgv.addIfPresent(out, "--idempotency-key", idempotencyKey);
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
     * {@code NodesOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.LIST;
        /**
         * 是否向 openclaw 子命令追加 {@code --list-connected} 开关。
         */
        private boolean listConnected;
        /**
         * 筛选节点最近连接时间的范围；未设置时命令行不包含 {@code --last-connected}。
         */
        private String lastConnected;
        /**
         * 请求关联标识；未设置时命令行不包含 {@code --request-id}。
         */
        private String requestId;
        /**
         * 目标节点名称或标识；未设置时命令行不包含 {@code --node-ref}。
         */
        private String nodeRef;
        /**
         * 目标资源名称；未设置时命令行不包含 {@code --name}。
         */
        private String name;
        /**
         * 节点 RPC 方法或命令名称；未设置时命令行不包含 {@code --command}。
         */
        private String command;
        /**
         * 节点命令的 JSON 参数；未设置时命令行不包含 {@code --params-json}。
         */
        private String paramsJson;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String invokeTimeout;
        /**
         * 防止请求重复执行的幂等键；未设置时命令行不包含 {@code --idempotency-key}。
         */
        private String idempotencyKey;
        /**
         * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
         */
        private String url;
        /**
         * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
         */
        private String token;
        /**
         * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
         */
        private String password;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String timeout;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 {@code --list-connected} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param connected 是否向命令行追加 {@code --list-connected} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder listConnected(boolean connected) {
            this.listConnected = connected;
            return this;
        }

        /**
         * 设置 {@code --last-connected} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param duration 筛选最近连接节点的时间范围；作为 {@code --last-connected} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder lastConnected(String duration) {
            this.lastConnected = duration;
            return this;
        }

        /**
         * 选择 {@code pending} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pending() {
            this.verb = Verb.PENDING;
            return this;
        }

        /**
         * 设置 {@code --approve} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param requestId 请求标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approve(String requestId) {
            this.verb = Verb.APPROVE;
            this.requestId = requestId;
            return this;
        }

        /**
         * 设置 {@code --reject} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
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
         * 设置 {@code --rename} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeRef 目标节点名称或标识；作为 {@code --rename} 的参数
         * @param displayName 面向用户展示的名称；作为 {@code --rename} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rename(String nodeRef, String displayName) {
            this.verb = Verb.RENAME;
            this.nodeRef = nodeRef;
            this.name = displayName;
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
         * 设置 {@code --invoke} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nodeRef 目标节点名称或标识；作为 {@code --invoke} 的参数
         * @param command 节点 RPC 方法或命令名称；作为 {@code --invoke} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder invoke(String nodeRef, String command) {
            this.verb = Verb.INVOKE;
            this.nodeRef = nodeRef;
            this.command = command;
            return this;
        }

        /**
         * 设置 {@code --params-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder paramsJson(String json) {
            this.paramsJson = json;
            return this;
        }

        /**
         * 设置 {@code --invoke-timeout} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ms 超时时长，单位为毫秒；作为 {@code --invoke-timeout} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder invokeTimeout(String ms) {
            this.invokeTimeout = ms;
            return this;
        }

        /**
         * 设置 {@code --idempotency-key} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param key 请求幂等键或目标键名；作为 {@code --idempotency-key} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder idempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

        /**
         * 设置 {@code --url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 {@code --token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 认证令牌或待追加的原始服务参数；作为 {@code --token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 {@code --password} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password Gateway 或远程服务密码；作为 {@code --password} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
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
         * 校验并复制当前构建器字段，创建独立的 {@code NodesOptions}。
         *
         * @return 按当前字段创建的 NodesOptions
         */
        public NodesOptions build() {
            return new NodesOptions(this);
        }
    }
}
