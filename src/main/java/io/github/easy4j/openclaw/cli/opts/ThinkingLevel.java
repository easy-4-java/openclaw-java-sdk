package io.github.easy4j.openclaw.cli.opts;

/**
 * `ThinkingLevel` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum ThinkingLevel {

    /**
     * 选择 `off` 协议模式；序列化时使用该固定取值。
     */
    OFF("off"),
    /**
     * 选择 `minimal` 协议模式；序列化时使用该固定取值。
     */
    MINIMAL("minimal"),
    /**
     * 选择 `low` 协议模式；序列化时使用该固定取值。
     */
    LOW("low"),
    /**
     * 选择 `medium` 协议模式；序列化时使用该固定取值。
     */
    MEDIUM("medium"),
    /**
     * 选择 `high` 协议模式；序列化时使用该固定取值。
     */
    HIGH("high"),
    /**
     * 选择 `xhigh` 协议模式；序列化时使用该固定取值。
     */
    XHIGH("xhigh");

    /**
     * 传给 openclaw 子命令 `--cli-value` 选项的内容；为 null 时通常省略。
     */
    private final String cliValue;

    /**
 * @param cliValue null, openclaw documentation
     */
    ThinkingLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
     * 返回该思考等级在 openclaw CLI 中接受的小写参数值。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String cliValue() {
        return cliValue;
    }
}
