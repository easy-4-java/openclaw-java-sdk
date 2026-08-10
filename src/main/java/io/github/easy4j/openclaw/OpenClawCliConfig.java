package io.github.easy4j.openclaw;

import lombok.Data;

/**
 * 本地 CLI 通道配置，定义可执行文件、工作目录、启动探测、命令超时和最大并发子进程数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Data
public class OpenClawCliConfig {

    /**
     * 是否创建并开放对应通信通道；关闭后门面不会初始化该子系统。
     */
    private boolean enabled = true;

    /**
     * 是否在客户端构造阶段执行可用性探测；默认关闭以避免启动阻塞。
     */
    private boolean startupCheckEnabled = false;

    /**
     * 启动探测失败时是否中断客户端构造；关闭时仅记录警告。
     */
    private boolean failFastOnUnavailable = false;

    /**
     * 本地 openclaw 可执行文件名或绝对路径。
     */
    private String executable = "openclaw";

    /**
     * 单次 CLI 命令默认超时，单位为秒。
     */
    private int timeout = 300;

    /**
     * CLI 子进程工作目录；为空时继承当前 JVM 工作目录。
     */
    private String workingDirectory;

    /**
     * 允许同时运行的 CLI 子进程数；非正数使用按 CPU 核心数计算的默认值。
     */
    private int maxConcurrentExecutions = 0;

    /**
     * 执行 openclaw --version 启动探测的超时，单位为秒。
     */
    private int probeTimeoutSeconds = 5;

}
