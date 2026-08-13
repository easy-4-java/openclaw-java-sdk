package io.github.easy4j.openclaw.cli.opts;

/**
 * 定义模型思考强度允许的固定取值及其 CLI/JSON 序列化拼写。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum ThinkingLevel {

    /**
     * 表示模型思考强度的 {@code off} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    OFF("off"),
    /**
     * 表示模型思考强度的 {@code minimal} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    MINIMAL("minimal"),
    /**
     * 表示模型思考强度的 {@code low} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    LOW("low"),
    /**
     * 表示模型思考强度的 {@code medium} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    MEDIUM("medium"),
    /**
     * 表示模型思考强度的 {@code high} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    HIGH("high"),
    /**
     * 表示模型思考强度的 {@code xhigh} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    XHIGH("xhigh"),
    /**
     * 表示由模型提供方动态决定思考预算的 {@code adaptive} 取值。
     */
    ADAPTIVE("adaptive"),
    /**
     * 表示模型支持的最大思考强度 {@code max} 取值。
     */
    MAX("max"),
    /**
     * 表示最大思考强度并允许运行时主动编排子智能体的 {@code ultra} 取值。
     */
    ULTRA("ultra");

    /** openclaw CLI 接受的小写思考等级值。 */
    private final String cliValue;

    /**
     * @param cliValue 传给 openclaw CLI 的小写思考等级值
     */
    ThinkingLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
     * 返回该思考等级在 openclaw CLI 中接受的小写参数值。
     *
     * @return 当前思考等级对应的 CLI 参数值
     */
    public String cliValue() {
        return cliValue;
    }
}
