package io.github.easy4j.openclaw.cli.opts;

/**
 * openclaw CLI 的详细日志开关，序列化为 {@code on} 或 {@code off}。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum VerboseLevel {

    /**
     * 表示详细日志开关的 {@code on} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    ON("on"),
    /**
     * 表示详细日志开关的 {@code off} 取值；写入 CLI 或 JSON 时保持该固定拼写。
     */
    OFF("off");

    /** openclaw CLI 接受的小写详细日志开关值。 */
    private final String cliValue;

    /**
     * @param cliValue 传给 openclaw CLI 的小写详细日志开关值
     */
    VerboseLevel(String cliValue) {
        this.cliValue = cliValue;
    }

    /**
     * 返回该日志等级在 openclaw CLI 中接受的小写参数值。
     *
     * @return openclaw CLI 接受的 {@code on} 或 {@code off}
     */
    public String cliValue() {
        return cliValue;
    }
}
