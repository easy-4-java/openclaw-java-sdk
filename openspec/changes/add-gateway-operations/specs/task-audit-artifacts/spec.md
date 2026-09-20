# 任务、审计、用量与产物 — Spec Delta

## Purpose

为 openclaw-java-sdk 的任务、审计、用量与产物定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: OBS-01 任务查询和取消保留运行归属
SDK SHALL 支持目标版本的任务列表、详情与取消，并保留其关联 Agent、session、run 等身份；任务取消受理不得替代所有底层工作的终态。

#### Scenario: OBS-01-S1 按任务读取
- **GIVEN** 任务有明确执行关联
- **WHEN** 查询任务详情
- **THEN** 返回准确状态和可用的相关运行标识

#### Scenario: OBS-01-S2 取消后仍终结中
- **GIVEN** 取消已受理但底层工作未报告结束
- **WHEN** 上层查看状态
- **THEN** 显示取消中或服务端状态，不伪造所有资源已释放

### Requirement: OBS-02 审计与用量查询不混淆缺省
SDK SHALL 对审计、执行检查和用量提供授权范围内的分页与时间窗口，保留币种、单位、时间和未知值；没有数据 MUST NOT 自动变成零成本或无事件。

#### Scenario: OBS-02-S1 分页审计
- **GIVEN** 用户读取指定时间和执行范围
- **WHEN** 返回带游标结果
- **THEN** 保留分页续读和执行身份，不越权合并其他用户数据

#### Scenario: OBS-02-S2 用量缺省
- **GIVEN** 响应没有某项用量或成本
- **WHEN** 展示查询结果
- **THEN** 保留未知，不输出已验证零成本

### Requirement: OBS-03 产物下载受权且保护短期地址
SDK SHALL 保留 artifact 与运行归属、过期信息及内容限制；下载凭据不得跨域转发，过期地址 SHALL 重新向权威接口请求而非无限重试。

#### Scenario: OBS-03-S1 有效产物下载
- **GIVEN** 用户有权读取目标产物
- **WHEN** 下载有效地址
- **THEN** 按大小限制流式读取并在结束后关闭资源

#### Scenario: OBS-03-S2 过期或跨域重定向
- **GIVEN** 地址过期或重定向到非获准目标
- **WHEN** 下载请求继续
- **THEN** 停止或经授权重新取址，不把 Gateway Token 或签名链接写入日志

## Sources

[O13 任务、审计及用量台账](https://docs.openclaw.ai/gateway/protocol/ledgers)；[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
