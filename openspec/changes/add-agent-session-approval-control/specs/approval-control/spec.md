# 人工审批闭环 — Spec Delta

## Purpose

为 openclaw-java-sdk 的人工审批闭环定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: APR-01 审批对象及类型可区分
SDK SHALL 支持目标版本的审批读取、决策和历史，并区分通用、exec 与 plugin 审批对象；呈现经授权的范围、请求原因、期限和有效决策，不自动批准。

#### Scenario: APR-01-S1 显示待审批项
- **GIVEN** 收到获准可见的审批事件
- **WHEN** 上层读取审批详情
- **THEN** 得到准确审批 ID、类型、可用决策和有效期限

#### Scenario: APR-01-S2 权限不足
- **GIVEN** 连接没有审批查询或决策权限
- **WHEN** 访问审批
- **THEN** 得到真实授权错误，不从其他会话泄露详情

### Requirement: APR-02 并发决策尊重服务端终态
SDK SHALL 将服务端已决、已过期或不存在结果映射为可辨识状态；重复或竞争决策 MUST NOT 被当成新的执行授权。

#### Scenario: APR-02-S1 另一操作员已处理
- **GIVEN** 两个操作员读取同一待审批对象
- **WHEN** 第二人提交过时决定
- **THEN** 显示已有终态或冲突并刷新，禁止覆盖或二次执行

#### Scenario: APR-02-S2 过期审批
- **GIVEN** 审批期限已到
- **WHEN** 调用者提交决定
- **THEN** 显示过期状态，SDK 不为其重建新的审批以规避限制

### Requirement: APR-03 审批事件是事实而非执行指令
审批通知 SHALL 只改变审批视图与对应运行状态；SDK MUST NOT 因收到请求事件自动执行命令，断线恢复 SHALL 通过权威查询弥补事件缺失。

#### Scenario: APR-03-S1 收到请求
- **GIVEN** 一个 exec 审批请求事件到达
- **WHEN** 事件被处理
- **THEN** 只通知宿主等待决策，不执行命令

#### Scenario: APR-03-S2 丢失已决事件
- **GIVEN** 断线期间审批已结束
- **WHEN** 客户端重新连接
- **THEN** 重新读取审批真相并更新状态，不永久停留在等待

## Sources

[O08 设备、Node、审批与 Cron RPC](https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
