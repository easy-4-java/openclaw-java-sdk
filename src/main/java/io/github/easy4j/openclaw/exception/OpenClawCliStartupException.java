package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityReport;
import lombok.Getter;

/**
 * CLI 路径解析或启动探测失败时抛出的异常，并携带完整可用性报告。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawCliStartupException extends RuntimeException {

    /**
     * CLI 启动失败前的可用性探测报告，保留路径解析与探测详情。
     */
    private final OpenClawCliAvailabilityReport availabilityReport;

    /**
     * 根据启动阶段的 CLI 可用性报告创建异常，并保留报告中的路径和探测诊断。
     *
     * @param message 消息正文
     * @param report CLI 启动前生成的可用性探测报告
     */
    public OpenClawCliStartupException(String message, OpenClawCliAvailabilityReport report) {
        super(message);
        this.availabilityReport = report;
    }
}
