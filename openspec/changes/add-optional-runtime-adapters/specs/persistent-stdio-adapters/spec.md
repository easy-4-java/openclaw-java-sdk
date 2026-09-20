# MCP 与 ACP 持久协议适配 — Spec Delta

## Purpose

为 openclaw-java-sdk 的MCP 与 ACP 持久协议适配定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: STDIO-01 持久双向协议而非一次性 stdout
MCP 和 ACP 适配器 SHALL 保持独立的持久双向 stdio 会话，处理请求、响应与通知；初始化完成前 SHALL 不宣称协议会话可用。

#### Scenario: STDIO-01-S1 协议初始化
- **GIVEN** 目标进程支持所选协议
- **WHEN** 双方初始化与能力协商成功
- **THEN** 返回持续可用的会话，而非等待进程退出才返回 stdout

#### Scenario: STDIO-01-S2 进程提前退出
- **GIVEN** 进程在初始化或响应前退出
- **WHEN** 处理 EOF 和退出码
- **THEN** 失败所有待处理请求并报告阶段，不悬挂等待

### Requirement: STDIO-02 MCP 与 ACP 能力分别协商
适配器 SHALL 分别遵循锁定版本的 MCP 与 ACP 生命周期，支持其声明的工具或会话、prompt、通知和取消能力；不得用同一 DTO 假定两个协议兼容。

#### Scenario: STDIO-02-S1 ACP 会话恢复
- **GIVEN** 目标 ACP 版本支持恢复且 session 已知
- **WHEN** 恢复后提交 prompt
- **THEN** 保留会话身份并正确接收流式更新

#### Scenario: STDIO-02-S2 MCP 能力缺省
- **GIVEN** 目标 MCP 服务未声明所需能力
- **WHEN** 请求该能力
- **THEN** 明确返回不支持，不以任意 CLI 拼接替代协议

### Requirement: STDIO-03 协议帧、诊断和进程关闭隔离
适配器 SHALL 区分 stdout 协议帧与 stderr 诊断，使用有界帧和缓冲限制，提供取消与幂等关闭；关闭仅处理自己拥有的进程资源。

#### Scenario: STDIO-03-S1 诊断穿插
- **GIVEN** 进程持续在 stderr 写诊断
- **WHEN** stdout 有合法响应帧
- **THEN** 协议解析不受诊断影响且日志脱敏

#### Scenario: STDIO-03-S2 关闭或帧溢出
- **GIVEN** 进程不退出或帧超过上限
- **WHEN** 执行关闭或故障处理
- **THEN** 在有界预算内终止自有进程与等待者，并保留失败证据

## Sources

[O14 MCP serve](https://docs.openclaw.ai/cli/mcp/serve)；[O15 ACP bridge](https://docs.openclaw.ai/cli/acp)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
