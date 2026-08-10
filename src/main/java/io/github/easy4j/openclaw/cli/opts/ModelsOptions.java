package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `models` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ModelsOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `status` 协议模式；序列化时使用该固定取值。
         */
        STATUS,
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `set` 协议模式；序列化时使用该固定取值。
         */
        SET,
        /**
         * 选择 `scan` 协议模式；序列化时使用该固定取值。
         */
        SCAN,
        /**
         * 选择 `aliases_list` 协议模式；序列化时使用该固定取值。
         */
        ALIASES_LIST,
        /**
         * 选择 `fallbacks_list` 协议模式；序列化时使用该固定取值。
         */
        FALLBACKS_LIST,
        /**
         * 选择 `auth_add` 协议模式；序列化时使用该固定取值。
         */
        AUTH_ADD,
        /**
         * 选择 `auth_login` 协议模式；序列化时使用该固定取值。
         */
        AUTH_LOGIN,
        /**
         * 选择 `auth_setup_token` 协议模式；序列化时使用该固定取值。
         */
        AUTH_SETUP_TOKEN,
        /**
         * 选择 `auth_paste_token` 协议模式；序列化时使用该固定取值。
         */
        AUTH_PASTE_TOKEN
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 是否向 openclaw 子命令追加 `--status-json` 开关。
     */
    private final boolean statusJson;
    /**
     * 是否向 openclaw 子命令追加 `--status-plain` 开关。
     */
    private final boolean statusPlain;
    /**
     * 是否向 openclaw 子命令追加 `--status-check` 开关。
     */
    private final boolean statusCheck;
    /**
     * 是否向 openclaw 子命令追加 `--probe` 开关。
     */
    private final boolean probe;
    /**
     * 传给 openclaw 子命令 `--probe-provider` 选项的内容；为 null 时通常省略。
     */
    private final String probeProvider;
    /**
     * 传给 openclaw 子命令 `--probe-profile` 选项的内容；为 null 时通常省略。
     */
    private final String probeProfile;
    /**
     * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
     */
    private final String probeTimeout;
    /**
     * 传给 openclaw 子命令 `--probe-concurrency` 选项的内容；为 null 时通常省略。
     */
    private final String probeConcurrency;
    /**
     * 传给 openclaw 子命令 `--probe-max-tokens` 选项的内容；为 null 时通常省略。
     */
    private final String probeMaxTokens;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 传给 openclaw 子命令 `--model-or-alias` 选项的内容；为 null 时通常省略。
     */
    private final String modelOrAlias;
    /**
     * 传给 openclaw 子命令 `--auth-provider` 选项的内容；为 null 时通常省略。
     */
    private final String authProvider;
    /**
     * 是否向 openclaw 子命令追加 `--auth-set-default` 开关。
     */
    private final boolean authSetDefault;
    /**
     * 传给 openclaw 子命令 `--paste-profile-id` 选项的内容；为 null 时通常省略。
     */
    private final String pasteProfileId;
    /**
     * 传给 openclaw 子命令 `--paste-expires-in` 选项的内容；为 null 时通常省略。
     */
    private final String pasteExpiresIn;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `ModelsOptions` 字段。
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
     * 链式构建器，逐项收集 ModelsOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ModelsOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.STATUS;
        /**
         * 是否向 openclaw 子命令追加 `--status-json` 开关。
         */
        private boolean statusJson;
        /**
         * 是否向 openclaw 子命令追加 `--status-plain` 开关。
         */
        private boolean statusPlain;
        /**
         * 是否向 openclaw 子命令追加 `--status-check` 开关。
         */
        private boolean statusCheck;
        /**
         * 是否向 openclaw 子命令追加 `--probe` 开关。
         */
        private boolean probe;
        /**
         * 传给 openclaw 子命令 `--probe-provider` 选项的内容；为 null 时通常省略。
         */
        private String probeProvider;
        /**
         * 传给 openclaw 子命令 `--probe-profile` 选项的内容；为 null 时通常省略。
         */
        private String probeProfile;
        /**
         * 该阶段允许等待的最长时间，单位由字段名声明；超时后取消对应网络或进程任务。
         */
        private String probeTimeout;
        /**
         * 传给 openclaw 子命令 `--probe-concurrency` 选项的内容；为 null 时通常省略。
         */
        private String probeConcurrency;
        /**
         * 传给 openclaw 子命令 `--probe-max-tokens` 选项的内容；为 null 时通常省略。
         */
        private String probeMaxTokens;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 传给 openclaw 子命令 `--model-or-alias` 选项的内容；为 null 时通常省略。
         */
        private String modelOrAlias;
        /**
         * 传给 openclaw 子命令 `--auth-provider` 选项的内容；为 null 时通常省略。
         */
        private String authProvider;
        /**
         * 是否向 openclaw 子命令追加 `--auth-set-default` 开关。
         */
        private boolean authSetDefault;
        /**
         * 传给 openclaw 子命令 `--paste-profile-id` 选项的内容；为 null 时通常省略。
         */
        private String pasteProfileId;
        /**
         * 传给 openclaw 子命令 `--paste-expires-in` 选项的内容；为 null 时通常省略。
         */
        private String pasteExpiresIn;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `status` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder status() {
            this.mode = Mode.STATUS;
            return this;
        }

        /**
         * 设置 `--status-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusJson(boolean json) {
            this.statusJson = json;
            return this;
        }

        /**
         * 设置 `--status-plain` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param plain 是否向命令行追加 `--status-plain` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusPlain(boolean plain) {
            this.statusPlain = plain;
            return this;
        }

        /**
         * 设置 `--status-check` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param check 是否向命令行追加 `--status-check` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder statusCheck(boolean check) {
            this.statusCheck = check;
            return this;
        }

        /**
         * 设置 `--probe` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probe 是否向命令行追加 `--probe` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probe(boolean probe) {
            this.probe = probe;
            return this;
        }

        /**
         * 设置 `--probe-provider` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeProvider 写入 `--probe-provider` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeProvider(String probeProvider) {
            this.probeProvider = probeProvider;
            return this;
        }

        /**
         * 设置 `--probe-profile` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeProfile 写入 `--probe-profile` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeProfile(String probeProfile) {
            this.probeProfile = probeProfile;
            return this;
        }

        /**
         * 设置 `--probe-timeout` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeTimeout 写入 `--probe-timeout` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeTimeout(String probeTimeout) {
            this.probeTimeout = probeTimeout;
            return this;
        }

        /**
         * 设置 `--probe-concurrency` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeConcurrency 写入 `--probe-concurrency` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeConcurrency(String probeConcurrency) {
            this.probeConcurrency = probeConcurrency;
            return this;
        }

        /**
         * 设置 `--probe-max-tokens` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param probeMaxTokens 写入 `--probe-max-tokens` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder probeMaxTokens(String probeMaxTokens) {
            this.probeMaxTokens = probeMaxTokens;
            return this;
        }

        /**
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.mode = Mode.LIST;
            return this;
        }

        /**
         * 设置 `--set` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param modelOrAlias 写入 `--set` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set(String modelOrAlias) {
            this.mode = Mode.SET;
            this.modelOrAlias = modelOrAlias;
            return this;
        }

        /**
         * 选择 `scan` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder scan() {
            this.mode = Mode.SCAN;
            return this;
        }

        /**
         * 选择 `aliasesList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder aliasesList() {
            this.mode = Mode.ALIASES_LIST;
            return this;
        }

        /**
         * 选择 `fallbacksList` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder fallbacksList() {
            this.mode = Mode.FALLBACKS_LIST;
            return this;
        }

        /**
         * 选择 `authAdd` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authAdd() {
            this.mode = Mode.AUTH_ADD;
            return this;
        }

        /**
         * 设置 `--auth-login` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--auth-login` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authLogin(String provider) {
            this.mode = Mode.AUTH_LOGIN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 `--auth-set-default` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param setDefault 是否向命令行追加 `--auth-set-default` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authSetDefault(boolean setDefault) {
            this.authSetDefault = setDefault;
            return this;
        }

        /**
         * 设置 `--auth-setup-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--auth-setup-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authSetupToken(String provider) {
            this.mode = Mode.AUTH_SETUP_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 `--auth-paste-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param provider 写入 `--auth-paste-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authPasteToken(String provider) {
            this.mode = Mode.AUTH_PASTE_TOKEN;
            this.authProvider = provider;
            return this;
        }

        /**
         * 设置 `--paste-profile-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param profileId 写入 `--paste-profile-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pasteProfileId(String profileId) {
            this.pasteProfileId = profileId;
            return this;
        }

        /**
         * 设置 `--paste-expires-in` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expiresIn 写入 `--paste-expires-in` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder pasteExpiresIn(String expiresIn) {
            this.pasteExpiresIn = expiresIn;
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
         * 校验并复制当前构建器字段，创建独立的 `ModelsOptions`。
         *
         * @return 按当前字段创建的 ModelsOptions
         */
        public ModelsOptions build() {
            return new ModelsOptions(this);
        }
    }
}
