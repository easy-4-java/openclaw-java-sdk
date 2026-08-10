package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code devices} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DevicesOptions implements CliSubArgs {

    /**
     * 定义设备管理动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示设备管理动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示设备管理动作的 {@code remove} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REMOVE,
        /**
         * 表示设备管理动作的 {@code clear} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        CLEAR,
        /**
         * 表示设备管理动作的 {@code approve} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        APPROVE,
        /**
         * 表示设备管理动作的 {@code reject} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REJECT,
        /**
         * 表示设备管理动作的 {@code rotate} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ROTATE,
        /**
         * 表示设备管理动作的 {@code revoke} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        REVOKE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 目标设备标识；未设置时命令行不包含 {@code --device-id}。
     */
    private final String deviceId;
    /**
     * 是否向 openclaw 子命令追加 {@code --clear-yes} 开关。
     */
    private final boolean clearYes;
    /**
     * 是否向 openclaw 子命令追加 {@code --clear-pending} 开关。
     */
    private final boolean clearPending;
    /**
     * 请求关联标识；未设置时命令行不包含 {@code --request-id}。
     */
    private final String requestId;
    /**
     * 是否向 openclaw 子命令追加 {@code --approve-latest} 开关。
     */
    private final boolean approveLatest;
    /**
     * 待轮换或撤销的设备角色；未设置时命令行不包含 {@code --role}。
     */
    private final String role;
    /**
     * 请求携带的授权作用域集合；未设置时命令行不包含 {@code --scopes}。
     */
    private final List<String> scopes;
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
     * 创建空白构建器，供调用方链式设置 {@code DevicesOptions} 字段。
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
     * {@code DevicesOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
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
         * 目标设备标识；未设置时命令行不包含 {@code --device-id}。
         */
        private String deviceId;
        /**
         * 是否向 openclaw 子命令追加 {@code --clear-yes} 开关。
         */
        private boolean clearYes;
        /**
         * 是否向 openclaw 子命令追加 {@code --clear-pending} 开关。
         */
        private boolean clearPending;
        /**
         * 请求关联标识；未设置时命令行不包含 {@code --request-id}。
         */
        private String requestId;
        /**
         * 是否向 openclaw 子命令追加 {@code --approve-latest} 开关。
         */
        private boolean approveLatest;
        /**
         * 待轮换或撤销的设备角色；未设置时命令行不包含 {@code --role}。
         */
        private String role;
        /**
         * 请求携带的授权作用域集合；未设置时命令行不包含 {@code --scopes}。
         */
        private List<String> scopes = new ArrayList<>();
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
         * 设置 {@code --remove} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 目标设备标识；作为 {@code --remove} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remove(String deviceId) {
            this.verb = Verb.REMOVE;
            this.deviceId = deviceId;
            return this;
        }

        /**
         * 设置 {@code --clear} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param yes 是否向命令行追加 {@code --clear} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clear(boolean yes) {
            this.verb = Verb.CLEAR;
            this.clearYes = yes;
            return this;
        }

        /**
         * 设置 {@code --clear-pending} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param pending 是否向命令行追加 {@code --clear-pending} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clearPending(boolean pending) {
            this.clearPending = pending;
            return this;
        }

        /**
         * 选择 {@code approve} 命令动作或布尔开关，并返回当前构建器。
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
         * 设置 {@code --approve} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
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
         * 设置 {@code --approve-latest} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param latest 是否向命令行追加 {@code --approve-latest} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder approveLatest(boolean latest) {
            this.approveLatest = latest;
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
         * 设置 {@code --rotate} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 目标设备标识；作为 {@code --rotate} 的参数
         * @param role 待轮换或撤销的设备角色；作为 {@code --rotate} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder rotate(String deviceId, String role) {
            this.verb = Verb.ROTATE;
            this.deviceId = deviceId;
            this.role = role;
            return this;
        }

        /**
         * 设置 {@code --scope} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param scope 重置或查询操作的作用域；作为 {@code --scope} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scope(String scope) {
            if (scope != null && OpenClawStrings.isNotBlank(scope)) {
                scopes.add(scope.trim());
            }
            return this;
        }

        /**
         * 设置 {@code --revoke} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param deviceId 目标设备标识；作为 {@code --revoke} 的参数
         * @param role 待轮换或撤销的设备角色；作为 {@code --revoke} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder revoke(String deviceId, String role) {
            this.verb = Verb.REVOKE;
            this.deviceId = deviceId;
            this.role = role;
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
         * 校验并复制当前构建器字段，创建独立的 {@code DevicesOptions}。
         *
         * @return 按当前字段创建的 DevicesOptions
         */
        public DevicesOptions build() {
            return new DevicesOptions(this);
        }
    }
}
