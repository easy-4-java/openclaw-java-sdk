package io.github.easy4j.openclaw.cli.availability;

import io.github.easy4j.openclaw.cli.OpenClawCliResult;
import lombok.Builder;
import lombok.Getter;

/**
 * 本地 openclaw CLI 的 {@code OpenClawCliAvailabilityReport} 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
public class OpenClawCliAvailabilityReport {

    /**
     * CLI 可用性检查的分类结果，用于区分可用、缺失和探测失败。
     */
    private final OpenClawCliAvailabilityStatus status;
    /**
     * CLI 可执行文件和启动探测是否均通过。
     */
    private final boolean available;
    /**
     * 配置中声明的 CLI 可执行文件名称或路径。
     */
    private final String configuredExecutable;
    /**
     * 经 PATH 或显式路径解析得到的可执行文件绝对路径。
     */
    private final String resolvedExecutablePath;
    /**
     * 消息正文。
     */
    private final String message;
    /**
     * CLI 启动探测命令的退出码及输出摘要。
     */
    private final OpenClawCliResult probeResult;

    /**
     * 返回 CLI 路径解析与启动探测是否均成功。
     *
     * @return 探测状态为可用时返回 {@code true}
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * 把探测状态、版本、退出码和错误信息组合为单行诊断说明，敏感配置不会写入文本。
     *
     * @return 可直接写入日志的单行 CLI 可用性诊断文本
     */
    public String toDiagnosticMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenClaw CLI ");
        sb.append(available ? "ready" : "unavailable");
        sb.append(" [").append(status).append(']');
        if (configuredExecutable != null) {
            sb.append(" executable=").append(configuredExecutable);
        }
        if (resolvedExecutablePath != null) {
            sb.append(" resolved=").append(resolvedExecutablePath);
        }
        if (message != null && !message.isEmpty()) {
            sb.append(" — ").append(message);
        }
        return sb.toString();
    }
}
