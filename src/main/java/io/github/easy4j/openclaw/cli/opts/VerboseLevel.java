package io.github.easy4j.openclaw.cli.opts;

/**
 * `VerboseLevel` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum VerboseLevel {

    /**
     * 选择 `on` 协议模式；序列化时使用该固定取值。
     */
    ON("on"),
    /**
     * 选择 `off` 协议模式；序列化时使用该固定取值。
     */
    OFF("off");

    /**
     * 传给 openclaw 子命令 `--cli-value` 选项的内容；为 null 时通常省略。
     */
    private final String cliValue;

    /**
 * @param cliValue null CLI characters
     */
    VerboseLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
     * 返回该日志等级在 openclaw CLI 中接受的小写参数值。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String cliValue() {
        return cliValue;
    }
}
