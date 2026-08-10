package io.github.easy4j.openclaw.cli.opts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * openclaw {@code gateway-rpc} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class GatewayRpcOptions {

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
     * 是否向 openclaw 子命令追加 {@code --expect-final} 开关。
     */
    private final boolean expectFinal;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;

    /**
 * {@link Builder} ; {@link #builder}.
     *
 * @param b null builder
     */
    private GatewayRpcOptions(Builder b) {
        this.url = b.url;
        this.token = b.token;
        this.password = b.password;
        this.timeout = b.timeout;
        this.expectFinal = b.expectFinal;
        this.json = b.json;
    }

    /**
     * 返回完整目标 URL。
     *
     * @return 规范化后的目标地址或路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 返回 Gateway RPC Bearer Token；未配置时为空。
     *
     * @return Gateway RPC Bearer Token；未配置时为 {@code null}
     */
    public String getToken() {
        return token;
    }

    /**
     * 返回 Gateway RPC 密码；未配置时为空。
     *
     * @return Gateway RPC 密码；未配置时为 {@code null}
     */
    public String getPassword() {
        return password;
    }

    /**
     * 返回 Gateway RPC 调用超时配置。
     *
     * @return 原样传给 CLI 的超时配置；未配置时为 {@code null}
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * 返回 RPC 命令是否等待最终响应帧。
     *
     * @return 是否等待 RPC 的最终事件
     */
    public boolean isExpectFinal() {
        return expectFinal;
    }

    /**
     * 判断 JSON 文本 是否满足协议或生命周期条件。
     *
     * @return 是否要求 CLI 输出 JSON
     */
    public boolean isJson() {
        return json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code GatewayRpcOptions} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 选择或编码 {@code gateway-rpc} 子命令的 {@code appendSharedFlags} 行为，并保留未设置选项的省略语义。
     *
     * @param args 接收通用 RPC 标志的可变命令行参数列表
     */
    public void appendSharedFlags(List<String> args) {
        Objects.requireNonNull(args, "args");
        if (url != null && !url.isEmpty()) {
            args.add("--url");
            args.add(url);
        }
        if (token != null && !token.isEmpty()) {
            args.add("--token");
            args.add(token);
        }
        if (password != null && !password.isEmpty()) {
            args.add("--password");
            args.add(password);
        }
        if (timeout != null && !timeout.isEmpty()) {
            args.add("--timeout");
            args.add(timeout);
        }
        if (expectFinal) {
            args.add("--expect-final");
        }
        if (json) {
            args.add("--json");
        }
    }

    /**
     * 选择或编码 {@code gateway-rpc} 子命令的 {@code toFlagList} 行为，并保留未设置选项的省略语义。
     *
     * @return 按 CLI 规定顺序生成的共享 RPC 参数列表
     */
    public List<String> toFlagList() {
        List<String> args = new ArrayList<>();
        appendSharedFlags(args);
        return args;
    }

    /**
     * {@code GatewayRpcOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

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
         * 是否向 openclaw 子命令追加 {@code --expect-final} 开关。
         */
        private boolean expectFinal;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

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
         * 校验并复制当前构建器字段，创建独立的 {@code GatewayRpcOptions}。
         *
         * @return 按当前字段创建的 GatewayRpcOptions
         */
        public GatewayRpcOptions build() {
            return new GatewayRpcOptions(this);
        }
    }
}
