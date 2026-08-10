package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code onboard} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OnboardOptions implements CliSubArgs {

    /**
     * 引导流程分段配置；未设置时命令行不包含 {@code --segments}。
     */
    private final List<String> segments;

    /**
     * @param segments 要传给 onboard 的步骤分段；为空时不限制步骤
     */
    private OnboardOptions(List<String> segments) {
        this.segments = segments;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code OnboardOptions} 字段。
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
     * {@code OnboardOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        /**
         * 引导流程使用的分段配置；未设置时命令行不包含 {@code --s}。
         */
        private final List<String> s = new ArrayList<>();

        /**
         * 设置 {@code --flow} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param flow 引导流程名称；作为 {@code --flow} 的参数
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
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
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
         * 设置 {@code --remote-url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param remoteUrl 远程 Gateway URL；作为 {@code --remote-url} 的参数
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
         * 设置 {@code --non-interactive} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param nonInteractive 是否向命令行追加 {@code --non-interactive} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder nonInteractive(boolean nonInteractive) {
            if (nonInteractive) {
                s.add("--non-interactive");
            }
            return this;
        }

        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
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
         * 设置 {@code --auth-choice} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param authChoice 引导流程选择的认证方式；作为 {@code --auth-choice} 的参数
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
         * 设置 {@code --custom-base-url} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param customBaseUrl 自定义模型服务的基础 URL；作为 {@code --custom-base-url} 的参数
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
         * 设置 {@code --custom-model-id} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param customModelId 自定义模型标识；作为 {@code --custom-model-id} 的参数
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
         * 设置 {@code --secret-input-mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param secretInputMode 密钥输入方式；作为 {@code --secret-input-mode} 的参数
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
         * 设置 {@code --accept-risk} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param acceptRisk 是否向命令行追加 {@code --accept-risk} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder acceptRisk(boolean acceptRisk) {
            if (acceptRisk) {
                s.add("--accept-risk");
            }
            return this;
        }

        /**
         * 设置 {@code --gateway-auth} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gatewayAuth Gateway 认证方式；作为 {@code --gateway-auth} 的参数
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
         * 设置 {@code --gateway-token} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param gatewayToken Gateway Bearer Token；作为 {@code --gateway-token} 的参数
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
         * 设置 {@code --gateway-token-ref-env} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param envVar 保存 Gateway Token 的环境变量名称；作为 {@code --gateway-token-ref-env} 的参数
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
         * 设置 {@code --install-daemon} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param installDaemon 是否向命令行追加 {@code --install-daemon} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder installDaemon(boolean installDaemon) {
            if (installDaemon) {
                s.add("--install-daemon");
            }
            return this;
        }

        /**
         * 设置 {@code --skip-health} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param skipHealth 是否向命令行追加 {@code --skip-health} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder skipHealth(boolean skipHealth) {
            if (skipHealth) {
                s.add("--skip-health");
            }
            return this;
        }

        /**
         * 设置 {@code --allow-unconfigured} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param allowUnconfigured 是否向命令行追加 {@code --allow-unconfigured} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder allowUnconfigured(boolean allowUnconfigured) {
            if (allowUnconfigured) {
                s.add("--allow-unconfigured");
            }
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
                Collections.addAll(s, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code OnboardOptions}。
         *
         * @return 按当前字段创建的 OnboardOptions
         */
        public OnboardOptions build() {
            return new OnboardOptions(OpenClawLists.copyOf(s));
        }
    }
}
