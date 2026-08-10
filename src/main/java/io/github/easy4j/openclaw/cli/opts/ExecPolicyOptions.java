package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `exec-policy` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class ExecPolicyOptions implements CliSubArgs {

    /**
     * `Mode` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Mode {
        /**
         * 选择 `show` 协议模式；序列化时使用该固定取值。
         */
        SHOW,
        /**
         * 选择 `preset` 协议模式；序列化时使用该固定取值。
         */
        PRESET,
        /**
         * 选择 `set` 协议模式；序列化时使用该固定取值。
         */
        SET
    }

    /**
     * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
     */
    private final Mode mode;
    /**
     * 传给 openclaw 子命令 `--preset-name` 选项的内容；为 null 时通常省略。
     */
    private final String presetName;
    /**
     * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
     */
    private final String host;
    /**
     * 传给 openclaw 子命令 `--security` 选项的内容；为 null 时通常省略。
     */
    private final String security;
    /**
     * 传给 openclaw 子命令 `--ask` 选项的内容；为 null 时通常省略。
     */
    private final String ask;
    /**
     * 传给 openclaw 子命令 `--ask-fallback` 选项的内容；为 null 时通常省略。
     */
    private final String askFallback;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
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
     * 创建空白构建器，供调用方链式设置 `ExecPolicyOptions` 字段。
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
     * 链式构建器，逐项收集 ExecPolicyOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 ExecPolicyOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--mode` 选项的内容；为 null 时通常省略。
         */
        private Mode mode = Mode.SHOW;
        /**
         * 传给 openclaw 子命令 `--preset-name` 选项的内容；为 null 时通常省略。
         */
        private String presetName;
        /**
         * 传给 openclaw 子命令 `--host` 选项的内容；为 null 时通常省略。
         */
        private String host;
        /**
         * 传给 openclaw 子命令 `--security` 选项的内容；为 null 时通常省略。
         */
        private String security;
        /**
         * 传给 openclaw 子命令 `--ask` 选项的内容；为 null 时通常省略。
         */
        private String ask;
        /**
         * 传给 openclaw 子命令 `--ask-fallback` 选项的内容；为 null 时通常省略。
         */
        private String askFallback;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;

        /**
         * 选择 `show` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder show() { this.mode = Mode.SHOW; return this; }
        /**
         * 设置 `--preset` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--preset` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder preset(String name) { this.mode = Mode.PRESET; this.presetName = name; return this; }
        /**
         * 选择 `set` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder set() { this.mode = Mode.SET; return this; }
        /**
         * 设置 `--mode` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param mode 写入 `--mode` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        /**
         * 设置 `--host` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param host 写入 `--host` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder host(String host) { this.host = host; return this; }
        /**
         * 设置 `--security` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param security 写入 `--security` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder security(String security) { this.security = security; return this; }
        /**
         * 设置 `--ask` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param ask 写入 `--ask` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder ask(String ask) { this.ask = ask; return this; }
        /**
         * 设置 `--ask-fallback` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param askFallback 写入 `--ask-fallback` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder askFallback(String askFallback) { this.askFallback = askFallback; return this; }
        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) { this.json = json; return this; }

        /**
         * 校验并复制当前构建器字段，创建独立的 `ExecPolicyOptions`。
         *
         * @return 按当前字段创建的 ExecPolicyOptions
         */
        public ExecPolicyOptions build() {
            return new ExecPolicyOptions(this);
        }
    }
}
