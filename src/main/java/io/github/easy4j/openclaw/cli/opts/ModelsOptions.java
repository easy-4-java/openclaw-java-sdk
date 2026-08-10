package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code models} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ModelsOptions implements CliSubArgs {

    /**
     * 定义模型配置动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示模型配置动作的 {@code status} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        STATUS,
        /**
         * 表示模型配置动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示模型配置动作的 {@code set} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET,
        /**
         * 表示模型配置动作的 {@code scan} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SCAN,
        /**
         * 表示模型配置动作的 {@code aliases_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ALIASES_LIST,
        /**
         * 表示模型配置动作的 {@code fallbacks_list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        FALLBACKS_LIST,
        /**
         * 表示模型配置动作的 {@code auth_add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUTH_ADD,
        /**
         * 表示模型配置动作的 {@code auth_login} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUTH_LOGIN,
        /**
         * 表示模型配置动作的 {@code auth_setup_token} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUTH_SETUP_TOKEN,
        /**
         * 表示模型配置动作的 {@code auth_paste_token} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUTH_PASTE_TOKEN
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 {@code --status-json} 开关。
     */
    private final boolean statusJson;
    /**
     * 是否向 openclaw 子命令追加 {@code --status-plain} 开关。
     */
    private final boolean statusPlain;
    /**
     * 是否向 openclaw 子命令追加 {@code --status-check} 开关。
     */
    private final boolean statusCheck;
    /**
     * 是否向 openclaw 子命令追加 {@code --probe} 开关。
     */
    private final boolean probe;
    /**
     * 要探测的模型提供方；未设置时命令行不包含 {@code --probe-provider}。
     */
    private final String probeProvider;
    /**
     * 模型探测使用的配置档案；未设置时命令行不包含 {@code --probe-profile}。
     */
    private final String probeProfile;
    /**
     * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
     */
    private final String probeTimeout;
    /**
     * 模型探测的最大并发数；未设置时命令行不包含 {@code --probe-concurrency}。
     */
    private final String probeConcurrency;
    /**
     * 单次模型探测允许的最大 Token 数；未设置时命令行不包含 {@code --probe-max-tokens}。
     */
    private final String probeMaxTokens;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 待设为默认值的模型标识或别名；未设置时命令行不包含 {@code --model-or-alias}。
     */
    private final String modelOrAlias;
    /**
     * 认证凭据所属的提供方；未设置时命令行不包含 {@code --auth-provider}。
     */
    private final String authProvider;
    /**
     * 是否向 openclaw 子命令追加 {@code --auth-set-default} 开关。
     */
    private final boolean authSetDefault;
    /**
     * 粘贴认证信息所属的配置档案标识；未设置时命令行不包含 {@code --paste-profile-id}。
     */
    private final String pasteProfileId;
    /**
     * 粘贴认证信息的有效期；未设置时命令行不包含 {@code --paste-expires-in}。
     */
    private final String pasteExpiresIn;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private ModelsOptions(Builder b) {
        this.mode = b.mode;
        this.statusJson = b.statusJson;
        this.statusPlain = b.statusPlain;
        this.statusCheck = b.statusCheck;
        this.probe = b.probe;
        this.probeProvider = b.probeProvider;
        this.probeProfile = b.probeProfile;
        this.probeTimeout = b.probeTimeout;
        this.probeConcurrency = b.probeConcurrency;
        this.probeMaxTokens = b.probeMaxTokens;
        this.agent = b.agent;
        this.modelOrAlias = b.modelOrAlias;
        this.authProvider = b.authProvider;
        this.authSetDefault = b.authSetDefault;
        this.pasteProfileId = b.pasteProfileId;
        this.pasteExpiresIn = b.pasteExpiresIn;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ModelsOptions} 字段。
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
            case STATUS:
                out.add("status");
                OpenClawCliArgv.addFlag(out, "--json", statusJson);
                OpenClawCliArgv.addFlag(out, "--plain", statusPlain);
                OpenClawCliArgv.addFlag(out, "--check", statusCheck);
                OpenClawCliArgv.addFlag(out, "--probe", probe);
                OpenClawCliArgv.addIfPresent(out, "--probe-provider", probeProvider);
                OpenClawCliArgv.addIfPresent(out, "--probe-profile", probeProfile);
                OpenClawCliArgv.addIfPresent(out, "--probe-timeout", probeTimeout);
                OpenClawCliArgv.addIfPresent(out, "--probe-concurrency", probeConcurrency);
                OpenClawCliArgv.addIfPresent(out, "--probe-max-tokens", probeMaxTokens);
                OpenClawCliArgv.addIfPresent(out, "--agent", agent);
                break;
            case LIST:
                out.add("list");
                break;
            case SET:
                out.add("set");
                if (modelOrAlias != null && OpenClawStrings.isNotBlank(modelOrAlias)) {
                    out.add(modelOrAlias.trim());
                }
                break;
            case SCAN:
                out.add("scan");
                break;
            case ALIASES_LIST:
                out.add("aliases");
                out.add("list");
                break;
            case FALLBACKS_LIST:
                out.add("fallbacks");
                out.add("list");
                break;
            case AUTH_ADD:
                out.add("auth");
                out.add("add");
                break;
            case AUTH_LOGIN:
                out.add("auth");
                out.add("login");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                OpenClawCliArgv.addFlag(out, "--set-default", authSetDefault);
                break;
            case AUTH_SETUP_TOKEN:
                out.add("auth");
                out.add("setup-token");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                break;
            case AUTH_PASTE_TOKEN:
                out.add("auth");
                out.add("paste-token");
                OpenClawCliArgv.addIfPresent(out, "--provider", authProvider);
                OpenClawCliArgv.addIfPresent(out, "--profile-id", pasteProfileId);
                OpenClawCliArgv.addIfPresent(out, "--expires-in", pasteExpiresIn);
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code ModelsOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.STATUS;
        /**
         * 是否向 openclaw 子命令追加 {@code --status-json} 开关。
         */
        private boolean statusJson;
        /**
         * 是否向 openclaw 子命令追加 {@code --status-plain} 开关。
         */
        private boolean statusPlain;
        /**
         * 是否向 openclaw 子命令追加 {@code --status-check} 开关。
         */
        private boolean statusCheck;
        /**
         * 是否向 openclaw 子命令追加 {@code --probe} 开关。
         */
        private boolean probe;
        /**
         * 要探测的模型提供方；未设置时命令行不包含 {@code --probe-provider}。
         */
        private String probeProvider;
        /**
         * 模型探测使用的配置档案；未设置时命令行不包含 {@code --probe-profile}。
         */
        private String probeProfile;
        /**
         * 该请求或进程允许等待的最长时间，单位为字段声明的计量单位；超时后主动取消对应任务。
         */
        private String probeTimeout;
        /**
         * 模型探测的最大并发数；未设置时命令行不包含 {@code --probe-concurrency}。
         */
        private String probeConcurrency;
        /**
         * 单次模型探测允许的最大 Token 数；未设置时命令行不包含 {@code --probe-max-tokens}。
         */
        private String probeMaxTokens;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 待设为默认值的模型标识或别名；未设置时命令行不包含 {@code --model-or-alias}。
         */
        private String modelOrAlias;
        /**
         * 认证凭据所属的提供方；未设置时命令行不包含 {@code --auth-provider}。
         */
        private String authProvider;
        /**
         * 是否向 openclaw 子命令追加 {@code --auth-set-default} 开关。
         */
        private boolean authSetDefault;
        /**
         * 粘贴认证信息所属的配置档案标识；未设置时命令行不包含 {@code --paste-profile-id}。
         */
        private String pasteProfileId;
        /**
         * 粘贴认证信息的有效期；未设置时命令行不包含 {@code --paste-expires-in}。
         */
        private String pasteExpiresIn;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code status} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * 设置 {@code --status-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusJson(boolean json) {
            this.statusJson = json;
            return this;
        }

        /**
         * 设置 {@code --status-plain} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plain 是否向命令行追加 {@code --status-plain} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusPlain(boolean plain) {
            this.statusPlain = plain;
            return this;
        }

        /**
         * 设置 {@code --status-check} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param check 是否向命令行追加 {@code --status-check} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusCheck(boolean check) {
            this.statusCheck = check;
            return this;
        }

        /**
         * 设置 {@code --probe} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probe 是否向命令行追加 {@code --probe} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probe(boolean probe) {
            this.probe = probe;
            return this;
        }

        /**
         * 设置 {@code --probe-provider} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeProvider 要探测的模型提供方；作为 {@code --probe-provider} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeProvider(String probeProvider) {
            this.probeProvider = probeProvider;
            return this;
        }

        /**
         * 设置 {@code --probe-profile} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeProfile 模型探测使用的配置档案；作为 {@code --probe-profile} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeProfile(String probeProfile) {
            this.probeProfile = probeProfile;
            return this;
        }

        /**
         * 设置 {@code --probe-timeout} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeTimeout 单次模型探测超时；作为 {@code --probe-timeout} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeTimeout(String probeTimeout) {
            this.probeTimeout = probeTimeout;
            return this;
        }

        /**
         * 设置 {@code --probe-concurrency} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeConcurrency 模型探测的最大并发数；作为 {@code --probe-concurrency} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeConcurrency(String probeConcurrency) {
            this.probeConcurrency = probeConcurrency;
            return this;
        }

        /**
         * 设置 {@code --probe-max-tokens} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeMaxTokens 单次模型探测允许的最大 Token 数；作为 {@code --probe-max-tokens} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeMaxTokens(String probeMaxTokens) {
            this.probeMaxTokens = probeMaxTokens;
            return this;
        }

        /**
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * 设置 {@code --set} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param modelOrAlias 待设为默认值的模型标识或别名；作为 {@code --set} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set(String modelOrAlias) {
            this.mode = Mode.SET;
            this.modelOrAlias = modelOrAlias;
            return this;
        }

        /**
         * 选择 {@code scan} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scan() {
            this.mode = Mode.SCAN;
            return this;
        }

        /**
         * 选择 {@code aliasesList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder aliasesList() {
            this.mode = Mode.ALIASES_LIST;
            return this;
        }

        /**
         * 选择 {@code fallbacksList} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder fallbacksList() {
            this.mode = Mode.FALLBACKS_LIST;
            return this;
        }

        /**
         * 选择 {@code authAdd} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authAdd() {
            this.mode = Mode.AUTH_ADD;
            return this;
        }

        /**
         * 设置 {@code --auth-login} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --auth-login} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authLogin(String provider) {
            this.mode = Mode.AUTH_LOGIN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 {@code --auth-set-default} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param setDefault 是否向命令行追加 {@code --auth-set-default} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authSetDefault(boolean setDefault) {
            this.authSetDefault = setDefault;
            return this;
        }

        /**
         * 设置 {@code --auth-setup-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --auth-setup-token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authSetupToken(String provider) {
            this.mode = Mode.AUTH_SETUP_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 {@code --auth-paste-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 目标模型或密钥提供方；作为 {@code --auth-paste-token} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authPasteToken(String provider) {
            this.mode = Mode.AUTH_PASTE_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 {@code --paste-profile-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param profileId 认证配置档案标识；作为 {@code --paste-profile-id} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pasteProfileId(String profileId) {
            this.pasteProfileId = profileId;
            return this;
        }

        /**
         * 设置 {@code --paste-expires-in} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expiresIn 粘贴认证信息的有效期；作为 {@code --paste-expires-in} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pasteExpiresIn(String expiresIn) {
            this.pasteExpiresIn = expiresIn;
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
         * 校验并复制当前构建器字段，创建独立的 {@code ModelsOptions}。
         *
         * @return 按当前字段创建的 ModelsOptions
         */
        public ModelsOptions build() {
            return new ModelsOptions(this);
        }
    }
}
