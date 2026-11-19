# 远程终端与实时语音 — Spec Delta

## Purpose

为 openclaw-java-sdk 的远程终端与实时语音定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: TV-01 终端会话全生命周期
终端适配器 SHALL 支持目标版本的 open、input、resize、attach、list 和 close，输出与退出事件按终端身份关联；权限不足或不支持时不得降级为任意 shell 执行。

#### Scenario: TV-01-S1 终端输入输出
- **GIVEN** 终端已明确建立且宿主授权
- **WHEN** 写入输入并收到输出事件
- **THEN** 只有该终端的消费者收到输出与退出事实

#### Scenario: TV-01-S2 断线重新附着
- **GIVEN** 终端连接断开
- **WHEN** 宿主请求 attach
- **THEN** 按服务端拥有者和目标身份恢复；不保证重放缺失输出，也不自动重开并重跑命令

### Requirement: TV-02 语音会话区分会话和输出取消
语音适配器 SHALL 区分会话创建、音频输入、输出事件、输出取消、工具回填及会话关闭；音频格式与大小 SHALL 依目标能力协商。

#### Scenario: TV-02-S1 取消输出
- **GIVEN** 语音会话保持连接且正在输出
- **WHEN** 调用者仅取消当前输出
- **THEN** 停止对应输出而不擅自关闭整个会话

#### Scenario: TV-02-S2 不支持格式
- **GIVEN** 输入音频格式不在已确认支持范围
- **WHEN** 提交音频
- **THEN** 明确拒绝或请求转换策略，不伪造格式成功或无限缓冲

### Requirement: TV-03 高风险适配默认不自动启用
远程终端、麦克风输入和敏感 Talk 配置 SHALL 需要显式宿主授权及对应服务端权限；SDK 构造或连接握手 MUST NOT 自动启动这些能力。

#### Scenario: TV-03-S1 显式启用
- **GIVEN** 宿主明确允许某语音或终端操作
- **WHEN** 调用对应入口
- **THEN** 仅执行被允许操作并保留生命周期句柄

#### Scenario: TV-03-S2 默认连接
- **GIVEN** 应用只创建普通 Gateway 客户端
- **WHEN** 完成连接
- **THEN** 不会打开终端、采集音频或读取额外敏感配置

## Sources

[O12 系统、渠道、插件与终端 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels)；[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
