# 配置与密钥管理 — Spec Delta

## Purpose

为 openclaw-java-sdk 的配置与密钥管理定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CFG-01 并发配置更新不覆盖他人修改
SDK SHALL 保留配置版本 hash，并按目标方法契约提交 baseHash 和 replacePaths；冲突时 MUST NOT 自动覆盖最新配置或移除并发校验条件。

#### Scenario: CFG-01-S1 带版本更新
- **GIVEN** 调用者读取版本 H 并提交修改
- **WHEN** 服务端仍是 H
- **THEN** 按 patch 或 apply 契约更新并返回新版本

#### Scenario: CFG-01-S2 版本冲突
- **GIVEN** 另一操作者已写入 H2
- **WHEN** 旧 baseHash=H 的写入被拒绝
- **THEN** 向调用者暴露冲突，要求重新读取和重新决策

### Requirement: CFG-02 持久化与运行时生效分离
SDK SHALL 区分配置写入、运行时应用、重启中、应用失败与结果不确定；不得把 config.set 的持久化确认自动描述为运行时已生效。

#### Scenario: CFG-02-S1 只持久化
- **GIVEN** 目标方法仅保证配置落盘
- **WHEN** 服务端确认写入
- **THEN** 返回 persisted 而非 runtime-applied 的事实

#### Scenario: CFG-02-S2 应用失败
- **GIVEN** 配置已持久化但运行时应用失败
- **WHEN** 服务端返回阶段性结果
- **THEN** 保留两个阶段结果，禁止自动覆盖或未经请求回滚

### Requirement: CFG-03 Schema 与密钥访问受权限制
SDK SHALL 暴露目标版本支持的配置 schema 和经授权的密钥管理结果，保留 SecretRef 等引用语义；敏感内容 MUST NOT 自动进入诊断或缓存导出。

#### Scenario: CFG-03-S1 读取 schema
- **GIVEN** 宿主有配置读取权限
- **WHEN** 查询支持的配置项
- **THEN** 返回 schema 与版本信息，不静默扩展未知配置字段

#### Scenario: CFG-03-S2 密钥操作失败
- **GIVEN** 密钥权限或安全策略拒绝访问
- **WHEN** 执行管理调用
- **THEN** 保留脱敏错误，不把密钥值或访问凭据写入日志

## Sources

[O09 Config RPC 更新与生效](https://docs.openclaw.ai/gateway/configuration/config-rpc)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
