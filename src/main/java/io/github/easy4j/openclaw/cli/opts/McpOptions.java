package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code mcp} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class McpOptions implements CliSubArgs {

    /**
     * 定义MCP 配置动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum ClaudeChannelMode {
        /**
         * 表示MCP 配置动作的 {@code off} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        OFF("off"),
        /**
         * 表示MCP 配置动作的 {@code on} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ON("on"),
        /**
         * 表示MCP 配置动作的 {@code auto} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        AUTO("auto");

        /**
         * 枚举常量对应的 CLI 固定参数值；未设置时命令行不包含 {@code --cli-value}。
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
     * 定义MCP 配置动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示MCP 配置动作的 {@code serve} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SERVE,
        /**
         * 表示MCP 配置动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示MCP 配置动作的 {@code show} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SHOW,
        /**
         * 表示MCP 配置动作的 {@code set} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET,
        /**
         * 表示MCP 配置动作的 {@code unset} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        UNSET
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
     */
    private final String url;
    /**
     * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
     */
    private final String token;
    /**
     * 读取认证令牌的文件路径；未设置时命令行不包含 {@code --token-file}。
     */
    private final String tokenFile;
    /**
     * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
     */
    private final String password;
    /**
     * 读取密码的文件路径；未设置时命令行不包含 {@code --password-file}。
     */
    private final String passwordFile;
    /**
     * Claude 通道集成模式；未设置时命令行不包含 {@code --claude-channel-mode}。
     */
    private final ClaudeChannelMode claudeChannelMode;
    /**
     * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
     */
    private final boolean verbose;
    /**
     * 待查看的 MCP 配置项名称；未设置时命令行不包含 {@code --show-name}。
     */
    private final String showName;
    /**
     * 是否向 openclaw 子命令追加 {@code --show-json} 开关。
     */
    private final boolean showJson;
    /**
     * 待设置的 MCP 配置项名称；未设置时命令行不包含 {@code --set-name}。
     */
    private final String setName;
    /**
     * MCP 配置项的新 JSON 值；未设置时命令行不包含 {@code --set-json}。
     */
    private final String setJson;
    /**
     * 待删除的 MCP 配置项名称；未设置时命令行不包含 {@code --unset-name}。
     */
    private final String unsetName;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
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
     * 创建空白构建器，供调用方链式设置 {@code McpOptions} 字段。
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
     * {@code McpOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.SERVE;
        /**
         * 目标 Gateway 或远程服务 URL；未设置时命令行不包含 {@code --url}。
         */
        private String url;
        /**
         * 认证令牌或待追加的原始服务参数；未设置时命令行不包含 {@code --token}。
         */
        private String token;
        /**
         * 读取认证令牌的文件路径；未设置时命令行不包含 {@code --token-file}。
         */
        private String tokenFile;
        /**
         * Gateway 或远程服务密码；未设置时命令行不包含 {@code --password}。
         */
        private String password;
        /**
         * 读取密码的文件路径；未设置时命令行不包含 {@code --password-file}。
         */
        private String passwordFile;
        /**
         * Claude 通道集成模式；未设置时命令行不包含 {@code --claude-channel-mode}。
         */
        private ClaudeChannelMode claudeChannelMode;
        /**
         * 是否向 openclaw 子命令追加 {@code --verbose} 开关。
         */
        private boolean verbose;
        /**
         * 待查看的 MCP 配置项名称；未设置时命令行不包含 {@code --show-name}。
         */
        private String showName;
        /**
         * 是否向 openclaw 子命令追加 {@code --show-json} 开关。
         */
        private boolean showJson;
        /**
         * 待设置的 MCP 配置项名称；未设置时命令行不包含 {@code --set-name}。
         */
        private String setName;
        /**
         * MCP 配置项的新 JSON 值；未设置时命令行不包含 {@code --set-json}。
         */
        private String setJson;
        /**
         * 待删除的 MCP 配置项名称；未设置时命令行不包含 {@code --unset-name}。
         */
        private String unsetName;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 选择 {@code serve} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder serve() {
            this.mode = Mode.SERVE;
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
         * 设置 {@code --token-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tokenFile(String path) {
            this.tokenFile = path;
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
         * 设置 {@code --password-file} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param path 相对于 Gateway 根地址的端点路径
         * @return 当前构建器，便于继续链式配置
         */
        public Builder passwordFile(String path) {
            this.passwordFile = path;
            return this;
        }

        /**
         * 设置 {@code --claude-channel-mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --claude-channel-mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder claudeChannelMode(ClaudeChannelMode mode) {
            this.claudeChannelMode = mode;
            return this;
        }

        /**
         * 设置 {@code --verbose} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param verbose 是否向命令行追加 {@code --verbose} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
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
         * 设置 {@code --show} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --show} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder show(String name) {
            this.mode = Mode.SHOW;
            this.showName = name;
            return this;
        }

        /**
         * 设置 {@code --show-json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder showJson(boolean json) {
            this.showJson = json;
            return this;
        }

        /**
         * 设置 {@code --set} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --set} 的参数
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
         * 设置 {@code --unset} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --unset} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder unset(String name) {
            this.mode = Mode.UNSET;
            this.unsetName = name;
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
         * 校验并复制当前构建器字段，创建独立的 {@code McpOptions}。
         *
         * @return 按当前字段创建的 McpOptions
         */
        public McpOptions build() {
            return new McpOptions(this);
        }
    }
}
