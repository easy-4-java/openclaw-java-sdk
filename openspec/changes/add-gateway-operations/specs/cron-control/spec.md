# 定时任务与运行跟踪 — Spec Delta

## Purpose

为 openclaw-java-sdk 的定时任务与运行跟踪定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CRON-01 调度管理契约完整
SDK SHALL 对目标版本的 cron get/list/status/add/update/remove 提供类型化操作，保留计划、时区、payload 与投递模式的区分；本地 SDK 不承担服务端到点执行承诺。

#### Scenario: CRON-01-S1 创建调度
- **GIVEN** 调用者给出合法计划和时区
- **WHEN** 创建请求成功
- **THEN** 保留服务端任务标识与计划信息，不自行计算不同的下次触发时间

#### Scenario: CRON-01-S2 非法计划
- **GIVEN** 计划不符合目标版本规则
- **WHEN** 提交创建或修改
- **THEN** 保留校验错误，不擅自更换时区或调度周期

### Requirement: CRON-02 手动运行是排队不是完成
SDK SHALL 把 cron.run 的受理视为排队结果，并使用返回 runId 查询 cron.runs；不得使用同任务最近一条历史代替本次运行身份。

#### Scenario: CRON-02-S1 并发手动触发
- **GIVEN** 同一任务短时间内触发两次
- **WHEN** 查询第一条 runId 的运行结果
- **THEN** 只绑定第一条运行，不混入第二条结果

#### Scenario: CRON-02-S2 排队超时
- **GIVEN** 任务仍排队而本地等待预算到期
- **WHEN** 结束等待
- **THEN** 返回等待到期并保留 runId，不标记任务执行失败

### Requirement: CRON-03 管理写入不可盲重放
SDK SHALL 对新增、删除、更新及手动运行保留失败阶段和结果不确定性；自动重试 SHALL 受目标契约和明确策略约束。

#### Scenario: CRON-03-S1 删除已确认
- **GIVEN** 服务端确认指定任务删除
- **WHEN** 重复读取该任务
- **THEN** 如实展示不存在，不自动重建

#### Scenario: CRON-03-S2 创建确认丢失
- **GIVEN** 服务端可能已新增任务
- **WHEN** 连接在确认前断开
- **THEN** 报告结果不确定并允许查询，不自动重复新增

## Sources

[O08 设备、Node、审批与 Cron RPC](https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
