package io.github.easy4j.openclaw.exception;

import io.github.easy4j.openclaw.cli.availability.OpenClawCliAvailabilityReport;
import lombok.Getter;

/**
 * OpenClaw SDK 在 Open Claw Cli Startup 阶段失败时抛出的异常，并保留可用于诊断的原因信息。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawCliStartupException extends RuntimeException {

    /**
     * `OpenClawCliStartupException` 生命周期内保存的 `availabilityReport` 对应状态。
     */
    private final OpenClawCliAvailabilityReport availabilityReport;

    /**
     * 按给定配置创建 `OpenClawCliStartupException`，构造过程不隐式执行远程业务请求。
     *
     * @param message 消息正文
     * @param report 写入 `report` 协议字段的内容
     */
    public OpenClawCliStartupException(String message, OpenClawCliAvailabilityReport report) {
        super(message);
        this.availabilityReport = report;
    }
}
