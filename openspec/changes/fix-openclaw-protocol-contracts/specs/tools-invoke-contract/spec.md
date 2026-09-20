# Tools Invoke 真实调用语义 — Spec Delta

## Purpose

为 openclaw-java-sdk 的Tools Invoke 真实调用语义定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: TOOL-01 调用字段与关联保真
SDK SHALL 支持目标契约的 agentId、sessionKey、idempotencyKey 和工具参数，保留工具调用关联；MUST NOT 把幂等标识存在解释为跨故障恰好一次执行保证。

#### Scenario: TOOL-01-S1 显式关联
- **GIVEN** 调用者给出 agentId、sessionKey 和 idempotencyKey
- **WHEN** 请求被序列化
- **THEN** 字段按目标契约发出，返回结果保留可用的调用关联

#### Scenario: TOOL-01-S2 身份冲突
- **GIVEN** agentId 与显式 sessionKey 指向不同智能体
- **WHEN** 服务端拒绝请求
- **THEN** 保留错误，不擅自删去身份字段重试

### Requirement: TOOL-02 dryRun 不提供虚假的无副作用保障
SDK 文档与 API 说明 SHALL 明确目标版本中 dryRun 是否被忽略；当该字段被忽略时，调用路径 MUST NOT 标记为安全预演或无副作用执行。

#### Scenario: TOOL-02-S1 兼容字段仍可传递
- **GIVEN** 调用者显式设置 dryRun=true 且目标版本忽略它
- **WHEN** 执行实际调用
- **THEN** 兼容透传但说明仍可能产生副作用，不产生预演成功标志

#### Scenario: TOOL-02-S2 宿主要求只验证
- **GIVEN** 宿主明确禁止工具执行
- **WHEN** 没有可验证的服务端预演能力
- **THEN** 在宿主执行策略层拒绝实际调用，而不是依赖 dryRun

### Requirement: TOOL-03 认证模式与执行失败分层
SDK SHALL 区分传输错误、RPC 拒绝、工具业务失败与结果不确定；共享 Gateway 密钥 MUST NOT 被宣传为按租户受限凭据，scopes 请求头 MUST NOT 被当成此模式下的隔离保证。

#### Scenario: TOOL-03-S1 工具业务失败
- **GIVEN** 传输或 RPC 成功但工具结果 ok=false
- **WHEN** 转换结果
- **THEN** 保留工具业务错误，不返回成功值或自动二次执行

#### Scenario: TOOL-03-S2 窄 scopes 请求头
- **GIVEN** 调用者使用共享操作员 Token 并填写较窄 scopes
- **WHEN** 构造安全说明或访问策略
- **THEN** 明确其不构成租户隔离，且不把密钥交给不受信任调用方

## Sources

[O07 HTTP Tools Invoke 与认证语义](https://docs.openclaw.ai/gateway/tools-invoke-http-api)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
