package io.github.easy4j.openclaw.cli.availability;

import io.github.easy4j.openclaw.cli.OpenClawCliResult;
import lombok.Builder;
import lombok.Getter;

/**
 * 本地 openclaw CLI 的 `OpenClawCliAvailabilityReport` 支撑类型，用于参数编码、可用性检查或执行结果表达。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Builder
public class OpenClawCliAvailabilityReport {

    /**
     * `OpenClawCliAvailabilityReport` 生命周期内保存的 `status` 对应状态。
     */
    private final OpenClawCliAvailabilityStatus status;
    /**
     * `OpenClawCliAvailabilityReport` 生命周期内保存的 `available` 对应状态。
     */
    private final boolean available;
    /**
     * `OpenClawCliAvailabilityReport` 生命周期内保存的 `configuredExecutable` 对应状态。
     */
    private final String configuredExecutable;
    /**
     * `OpenClawCliAvailabilityReport` 生命周期内保存的 `resolvedExecutablePath` 对应状态。
     */
    private final String resolvedExecutablePath;
    /**
     * 消息正文。
     */
    private final String message;
    /**
     * `OpenClawCliAvailabilityReport` 生命周期内保存的 `probeResult` 对应状态。
     */
    private final OpenClawCliResult probeResult;

    /**
     * 判断 `available` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * 把探测状态、版本、退出码和错误信息组合为单行诊断说明，敏感配置不会写入文本。
     *
     * @return 服务返回或流式累积得到的文本
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
