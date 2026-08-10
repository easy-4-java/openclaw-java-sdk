package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code exec-policy} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ExecPolicyOptions implements CliSubArgs {

    /**
     * 定义执行安全策略来源允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 表示执行安全策略来源的 {@code show} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SHOW,
        /**
         * 表示执行安全策略来源的 {@code preset} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        PRESET,
        /**
         * 表示执行安全策略来源的 {@code set} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        SET
    }

    /**
     * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
     */
    private final Mode mode;
    /**
     * 预定义执行策略名称；未设置时命令行不包含 {@code --preset-name}。
     */
    private final String presetName;
    /**
     * 节点监听地址；未设置时命令行不包含 {@code --host}。
     */
    private final String host;
    /**
     * MCP 传输安全策略；未设置时命令行不包含 {@code --security}。
     */
    private final String security;
    /**
     * 执行前询问策略；未设置时命令行不包含 {@code --ask}。
     */
    private final String ask;
    /**
     * 询问不可用时的回退策略；未设置时命令行不包含 {@code --ask-fallback}。
     */
    private final String askFallback;
    /**
     * 是否向 openclaw 子命令追加 {@code --json} 开关。
     */
    private final boolean json;

    private ExecPolicyOptions(Builder b) {
        this.mode = b.mode;
        this.presetName = b.presetName;
        this.host = b.host;
        this.security = b.security;
        this.ask = b.ask;
        this.askFallback = b.askFallback;
        this.json = b.json;
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code ExecPolicyOptions} 字段。
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
            case SHOW:
                out.add("show");
                break;
            case PRESET:
                out.add("preset");
                if (presetName != null && !presetName.isEmpty()) {
                    out.add(presetName);
                }
                break;
            case SET:
                out.add("set");
                break;
            default:
                // 未指定模式时不输出子命令 token（等价于父命令默认动作）
                break;
        }
        OpenClawCliArgv.addIfPresent(out, "--host", host);
        OpenClawCliArgv.addIfPresent(out, "--security", security);
        OpenClawCliArgv.addIfPresent(out, "--ask", ask);
        OpenClawCliArgv.addIfPresent(out, "--ask-fallback", askFallback);
        OpenClawCliArgv.addFlag(out, "--json", json);
        return Collections.unmodifiableList(out);
    }

    /**
     * {@code ExecPolicyOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 子命令使用的执行模式；未设置时命令行不包含 {@code --mode}。
         */
        private Mode mode = Mode.SHOW;
        /**
         * 预定义执行策略名称；未设置时命令行不包含 {@code --preset-name}。
         */
        private String presetName;
        /**
         * 节点监听地址；未设置时命令行不包含 {@code --host}。
         */
        private String host;
        /**
         * MCP 传输安全策略；未设置时命令行不包含 {@code --security}。
         */
        private String security;
        /**
         * 执行前询问策略；未设置时命令行不包含 {@code --ask}。
         */
        private String ask;
        /**
         * 询问不可用时的回退策略；未设置时命令行不包含 {@code --ask-fallback}。
         */
        private String askFallback;
        /**
         * 是否向 openclaw 子命令追加 {@code --json} 开关。
         */
        private boolean json;

        /**
         * 选择 {@code show} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder show() { this.mode = Mode.SHOW; return this; }
        /**
         * 设置 {@code --preset} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --preset} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder preset(String name) { this.mode = Mode.PRESET; this.presetName = name; return this; }
        /**
         * 选择 {@code set} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() { this.mode = Mode.SET; return this; }
        /**
         * 设置 {@code --mode} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 子命令使用的执行模式；作为 {@code --mode} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 {@code --host} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 节点监听地址；作为 {@code --host} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) { this.host = host; return this; }
        /**
         * 设置 {@code --security} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param security MCP 传输安全策略；作为 {@code --security} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder security(String security) { this.security = security; return this; }
        /**
         * 设置 {@code --ask} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ask 执行前询问策略；作为 {@code --ask} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder ask(String ask) { this.ask = ask; return this; }
        /**
         * 设置 {@code --ask-fallback} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param askFallback 询问不可用时的回退策略；作为 {@code --ask-fallback} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder askFallback(String askFallback) { this.askFallback = askFallback; return this; }
        /**
         * 设置 {@code --json} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code ExecPolicyOptions}。
         *
         * @return 按当前字段创建的 ExecPolicyOptions
         */
        public ExecPolicyOptions build() {
            return new ExecPolicyOptions(this);
        }
    }
}
