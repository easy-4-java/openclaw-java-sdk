package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `onboard` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OnboardOptions implements CliSubArgs {

    /**
     * 传给 openclaw 子命令 `--segments` 选项的内容；为 null 时通常省略。
     */
    private final List<String> segments;

    /**
 * @param segments null;,
     */
    private OnboardOptions(List<String> segments) {
        this.segments = segments;
    }

    /**
     * 创建空白构建器，供调用方链式设置 `OnboardOptions` 字段。
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
        return segments;
    }

    /**
     * 链式构建器，逐项收集 OnboardOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 OnboardOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 传给 openclaw 子命令 `--s` 选项的内容；为 null 时通常省略。
         */
        private final List<String> s = new ArrayList<>();

        /**
         * 设置 `--flow` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param flow 写入 `--flow` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder flow(String flow) {
            if (flow != null && !flow.isEmpty()) {
                s.add("--flow");
                s.add(flow);
            }
            return this;
        }

        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(String mode) {
            if (mode != null && !mode.isEmpty()) {
                s.add("--mode");
                s.add(mode);
            }
            return this;
        }

        /**
         * 设置 `--remote-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param remoteUrl 写入 `--remote-url` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remoteUrl(String remoteUrl) {
            if (remoteUrl != null && !remoteUrl.isEmpty()) {
                s.add("--remote-url");
                s.add(remoteUrl);
            }
            return this;
        }

        /**
         * 设置 `--non-interactive` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 `--non-interactive` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            if (nonInteractive) {
                s.add("--non-interactive");
            }
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            if (json) {
                s.add("--json");
            }
            return this;
        }

        /**
         * 设置 `--auth-choice` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param authChoice 写入 `--auth-choice` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder authChoice(String authChoice) {
            if (authChoice != null && !authChoice.isEmpty()) {
                s.add("--auth-choice");
                s.add(authChoice);
            }
            return this;
        }

        /**
         * 设置 `--custom-base-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param customBaseUrl 写入 `--custom-base-url` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder customBaseUrl(String customBaseUrl) {
            if (customBaseUrl != null && !customBaseUrl.isEmpty()) {
                s.add("--custom-base-url");
                s.add(customBaseUrl);
            }
            return this;
        }

        /**
         * 设置 `--custom-model-id` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param customModelId 写入 `--custom-model-id` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder customModelId(String customModelId) {
            if (customModelId != null && !customModelId.isEmpty()) {
                s.add("--custom-model-id");
                s.add(customModelId);
            }
            return this;
        }

        /**
         * 设置 `--secret-input-mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param secretInputMode 写入 `--secret-input-mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder secretInputMode(String secretInputMode) {
            if (secretInputMode != null && !secretInputMode.isEmpty()) {
                s.add("--secret-input-mode");
                s.add(secretInputMode);
            }
            return this;
        }

        /**
         * 设置 `--accept-risk` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param acceptRisk 是否向命令行追加 `--accept-risk` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder acceptRisk(boolean acceptRisk) {
            if (acceptRisk) {
                s.add("--accept-risk");
            }
            return this;
        }

        /**
         * 设置 `--gateway-auth` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gatewayAuth 写入 `--gateway-auth` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayAuth(String gatewayAuth) {
            if (gatewayAuth != null && !gatewayAuth.isEmpty()) {
                s.add("--gateway-auth");
                s.add(gatewayAuth);
            }
            return this;
        }

        /**
         * 设置 `--gateway-token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gatewayToken 写入 `--gateway-token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayToken(String gatewayToken) {
            if (gatewayToken != null && !gatewayToken.isEmpty()) {
                s.add("--gateway-token");
                s.add(gatewayToken);
            }
            return this;
        }

        /**
         * 设置 `--gateway-token-ref-env` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param envVar 写入 `--gateway-token-ref-env` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder gatewayTokenRefEnv(String envVar) {
            if (envVar != null && !envVar.isEmpty()) {
                s.add("--gateway-token-ref-env");
                s.add(envVar);
            }
            return this;
        }

        /**
         * 设置 `--install-daemon` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installDaemon 是否向命令行追加 `--install-daemon` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installDaemon(boolean installDaemon) {
            if (installDaemon) {
                s.add("--install-daemon");
            }
            return this;
        }

        /**
         * 设置 `--skip-health` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skipHealth 是否向命令行追加 `--skip-health` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skipHealth(boolean skipHealth) {
            if (skipHealth) {
                s.add("--skip-health");
            }
            return this;
        }

        /**
         * 设置 `--allow-unconfigured` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allowUnconfigured 是否向命令行追加 `--allow-unconfigured` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowUnconfigured(boolean allowUnconfigured) {
            if (allowUnconfigured) {
                s.add("--allow-unconfigured");
            }
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
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `OnboardOptions`。
         *
         * @return 按当前字段创建的 OnboardOptions
         */
        public OnboardOptions build() {
            return new OnboardOptions(OpenClawLists.copyOf(s));
        }
    }
}
