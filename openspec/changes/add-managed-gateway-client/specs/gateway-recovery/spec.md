# 连接恢复与订阅对账 — Spec Delta

## Purpose

为 openclaw-java-sdk 的连接恢复与订阅对账定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: REC-01 有界重连与关闭语义
SDK SHALL 暴露连接状态、重试预算、退避及停止原因；用户关闭后 SHALL 停止重连；重连只恢复连接，不自动重放业务写请求。

#### Scenario: REC-01-S1 暂时不可用
- **GIVEN** Gateway 返回可重试失败和 retryAfterMs
- **WHEN** 重试预算未耗尽
- **THEN** 在预算内遵守等待并采用带抖动退避，不并发发起无限连接

#### Scenario: REC-01-S2 主动关闭
- **GIVEN** 客户端正在等待下一次重连
- **WHEN** 宿主关闭客户端
- **THEN** 取消重连与相关定时器，不再创建新连接

### Requirement: REC-02 重连恢复先订阅再快照对账
成功重连后 SDK SHALL 根据显式订阅意图重新订阅并获取权威快照；必须考虑订阅和快照交错事件，不声称可以重放所有丢失事件。

#### Scenario: REC-02-S1 快照期间变化
- **GIVEN** 连接恢复且订阅已注册
- **WHEN** 快照生成时收到失效事件
- **THEN** 合并可用事实并执行必要尾随刷新

#### Scenario: REC-02-S2 数据不能重放
- **GIVEN** 断线期间存在不可恢复的临时进度事件
- **WHEN** 恢复连接
- **THEN** 暴露恢复缺口并以当前权威状态继续，不伪造缺失事件

### Requirement: REC-03 执行不确定性必须显式暴露
对已经发送但未收到确认的有副作用请求，SDK SHALL 暴露结果不确定，并提供已存在关联信息供查询；只有经目标契约证明安全且宿主明确配置的请求才可自动重试。

#### Scenario: REC-03-S1 执行后断线
- **GIVEN** 创建运行的请求可能已在服务端生效
- **WHEN** 确认到达前断线
- **THEN** 不自动再次创建，保留关联并允许后续查询

#### Scenario: REC-03-S2 可重试只读查询
- **GIVEN** 请求是经分类的只读查询且宿主允许重试
- **WHEN** 瞬态断线后恢复
- **THEN** 在预算内重试并记录次数，不将此策略推广至写请求

## Sources

[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)；[O05 Gateway 握手与设备凭据](https://docs.openclaw.ai/gateway/protocol/handshake)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
