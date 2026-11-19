# Agent 运行与定义管理 — Spec Delta

## Purpose

为 openclaw-java-sdk 的Agent 运行与定义管理定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: AGENT-01 受理和执行结果分离
SDK SHALL 区分运行请求受理、执行中、等待到期、远端失败与远端成功；运行句柄 SHALL 保留已返回的 runId 及作用域身份，不能以受理成功代替完成。

#### Scenario: AGENT-01-S1 运行已受理
- **GIVEN** 提交有效 Agent 运行且服务端返回运行标识
- **WHEN** SDK 返回受理结果
- **THEN** 状态为已受理而非已完成，并提供后续等待或查询入口

#### Scenario: AGENT-01-S2 等待返回 pending
- **GIVEN** 运行仍在进行
- **WHEN** 一次等待到期或返回 pending
- **THEN** 保留仍在运行或未知状态，不将其改为远端执行失败

### Requirement: AGENT-02 运行取消使用正式控制路径
SDK SHALL 区分停止本地等待与请求远端取消；远端取消 SHALL 使用目标版本明确支持的作用域和方法，并以确认或后续状态判定结果。

#### Scenario: AGENT-02-S1 请求取消
- **GIVEN** 运行标识已知且宿主具有权限
- **WHEN** 用户显式请求远端取消
- **THEN** 按正式控制路径发出并保留确认，不擅自杀死 Gateway 进程

#### Scenario: AGENT-02-S2 取消未确认
- **GIVEN** 取消请求发出后连接断开
- **WHEN** SDK 结束本地等待
- **THEN** 报告取消结果不确定而非已取消，允许以运行身份对账

### Requirement: AGENT-03 定义和文件管理有明确作用域
SDK SHALL 提供目标版本支持的 Agent 列表、创建、更新、删除与文件管理契约；文件和删除请求 SHALL 保留目标 Agent 和路径范围，不能自动跨 Agent 回退。

#### Scenario: AGENT-03-S1 Agent 文件更新
- **GIVEN** 指定 Agent 与允许路径
- **WHEN** 提交文件内容
- **THEN** 只返回该目标的服务端结果和版本事实

#### Scenario: AGENT-03-S2 拒绝越界
- **GIVEN** 路径或 Agent 不获授权
- **WHEN** 服务端拒绝文件操作
- **THEN** 保留错误，禁止改写到其他目录或以 CLI 提权补做

## Sources

[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
