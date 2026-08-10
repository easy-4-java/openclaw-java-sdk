package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `mcp` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class McpOptions implements CliSubArgs {

    /**
     * `ClaudeChannelMode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum ClaudeChannelMode {
        /**
         * 选择 `off` 协议模式；序列化时使用该固定取值。
         */
        OFF("off"),
        /**
         * 选择 `on` 协议模式；序列化时使用该固定取值。
         */
        ON("on"),
        /**
         * 选择 `auto` 协议模式；序列化时使用该固定取值。
         */
        AUTO("auto");

        /**
         * 传给 openclaw 子命令 `--cli-value` 选项的内容；为 null 时通常省略。
         */
        private final String cliValue;

        ClaudeChannelMode(String cliValue) {
            this.cliValue = cliValue;
        }

        String cliValue() {
            return cliValue;
        }
    }

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `serve` 协议模式；序列化时使用该固定取值。
         */
        SERVE,
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `show` 协议模式；序列化时使用该固定取值。
         */
        SHOW,
        /**
         * 选择 `set` 协议模式；序列化时使用该固定取值。
         */
        SET,
        /**
         * 选择 `unset` 协议模式；序列化时使用该固定取值。
         */
        UNSET
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--token-file` 选项的内容；为 null 时通常省略。
     */
    private final String tokenFile;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 传给 openclaw 子命令 `--password-file` 选项的内容；为 null 时通常省略。
     */
    private final String passwordFile;
    /**
     * 传给 openclaw 子命令 `--claude-channel-mode` 选项的内容；为 null 时通常省略。
     */
    private final ClaudeChannelMode claudeChannelMode;
    /**
     * 是否向 openclaw 子命令追加 `--verbose` 开关。
     */
    private final boolean verbose;
    /**
     * 传给 openclaw 子命令 `--show-name` 选项的内容；为 null 时通常省略。
     */
    private final String showName;
    /**
     * 是否向 openclaw 子命令追加 `--show-json` 开关。
     */
    private final boolean showJson;
    /**
     * 传给 openclaw 子命令 `--set-name` 选项的内容；为 null 时通常省略。
     */
    private final String setName;
    /**
     * 传给 openclaw 子命令 `--set-json` 选项的内容；为 null 时通常省略。
     */
    private final String setJson;
    /**
     * 传给 openclaw 子命令 `--unset-name` 选项的内容；为 null 时通常省略。
     */
    private final String unsetName;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private McpOptions(Builder b) {
        this.mode = b.mode;
        this.url = b.url;
        this.token = b.token;
        this.tokenFile = b.tokenFile;
        this.password = b.password;
        this.passwordFile = b.passwordFile;
        this.claudeChannelMode = b.claudeChannelMode;
        this.verbose = b.verbose;
        this.showName = b.showName;
        this.showJson = b.showJson;
        this.setName = b.setName;
        this.setJson = b.setJson;
        this.unsetName = b.unsetName;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `McpOptions` 字段。
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
            case SERVE:
                out.add("serve");
                OpenClawCliArgv.addIfPresent(out, "--url", url);
                OpenClawCliArgv.addIfPresent(out, "--token", token);
                OpenClawCliArgv.addIfPresent(out, "--token-file", tokenFile);
                OpenClawCliArgv.addIfPresent(out, "--password", password);
                OpenClawCliArgv.addIfPresent(out, "--password-file", passwordFile);
                if (claudeChannelMode != null) {
                    out.add("--claude-channel-mode");
                    out.add(claudeChannelMode.cliValue());
                }
                OpenClawCliArgv.addFlag(out, "--verbose", verbose);
                break;
            case LIST:
                out.add("list");
                break;
            case SHOW:
                out.add("show");
                if (showName != null && OpenClawStrings.isNotBlank(showName)) {
                    out.add(showName.trim());
                }
                OpenClawCliArgv.addFlag(out, "--json", showJson);
                break;
            case SET:
                out.add("set");
                if (setName != null && OpenClawStrings.isNotBlank(setName)) {
                    out.add(setName.trim());
                }
                if (setJson != null && OpenClawStrings.isNotBlank(setJson)) {
                    out.add(setJson);
                }
                break;
            case UNSET:
                out.add("unset");
                if (unsetName != null && OpenClawStrings.isNotBlank(unsetName)) {
                    out.add(unsetName.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 McpOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 McpOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.SERVE;
        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--token-file` 选项的内容；为 null 时通常省略。
         */
        private String tokenFile;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 传给 openclaw 子命令 `--password-file` 选项的内容；为 null 时通常省略。
         */
        private String passwordFile;
        /**
         * 传给 openclaw 子命令 `--claude-channel-mode` 选项的内容；为 null 时通常省略。
         */
        private ClaudeChannelMode claudeChannelMode;
        /**
         * 是否向 openclaw 子命令追加 `--verbose` 开关。
         */
        private boolean verbose;
        /**
         * 传给 openclaw 子命令 `--show-name` 选项的内容；为 null 时通常省略。
         */
        private String showName;
        /**
         * 是否向 openclaw 子命令追加 `--show-json` 开关。
         */
        private boolean showJson;
        /**
         * 传给 openclaw 子命令 `--set-name` 选项的内容；为 null 时通常省略。
         */
        private String setName;
        /**
         * 传给 openclaw 子命令 `--set-json` 选项的内容；为 null 时通常省略。
         */
        private String setJson;
        /**
         * 传给 openclaw 子命令 `--unset-name` 选项的内容；为 null 时通常省略。
         */
        private String unsetName;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 `serve` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder serve() {
            this.mode = Mode.SERVE;
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
         * 设置 `--token-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tokenFile(String path) {
            this.tokenFile = path;
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
         * 设置 `--password-file` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder passwordFile(String path) {
            this.passwordFile = path;
            return this;
        }

        /**
         * 设置 `--claude-channel-mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--claude-channel-mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder claudeChannelMode(ClaudeChannelMode mode) {
            this.claudeChannelMode = mode;
            return this;
        }

        /**
         * 设置 `--verbose` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 `--verbose` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
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
         * 设置 `--show` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--show` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder show(String name) {
            this.mode = Mode.SHOW;
            this.showName = name;
            return this;
        }

        /**
         * 设置 `--show-json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder showJson(boolean json) {
            this.showJson = json;
            return this;
        }

        /**
         * 设置 `--set` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--set` 选项的内容
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set(String name, String json) {
            this.mode = Mode.SET;
            this.setName = name;
            this.setJson = json;
            return this;
        }

        /**
         * 设置 `--unset` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--unset` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unset(String name) {
            this.mode = Mode.UNSET;
            this.unsetName = name;
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
         * 校验并复制当前构建器字段，创建独立的 `McpOptions`。
         *
         * @return 按当前字段创建的 McpOptions
         */
        public McpOptions build() {
            return new McpOptions(this);
        }
    }
}
