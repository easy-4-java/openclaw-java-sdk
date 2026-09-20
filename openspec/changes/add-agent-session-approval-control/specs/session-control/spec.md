# 会话控制与订阅 — Spec Delta

## Purpose

为 openclaw-java-sdk 的会话控制与订阅定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: SESS-01 会话身份和生命周期管理
SDK SHALL 提供经目标版本确认的会话创建、解析、描述、修改、重置、删除与压缩入口；同一 sessionKey 不得被视为永久不变的物理会话身份。

#### Scenario: SESS-01-S1 重置形成新代际
- **GIVEN** 调用者重置会话并获得新状态
- **WHEN** 后续调用使用该会话
- **THEN** 缓存与订阅更新为新代际，旧状态不可覆盖

#### Scenario: SESS-01-S2 压缩未确认
- **GIVEN** 压缩请求收到受理但无完成事实
- **WHEN** 返回调用结果
- **THEN** 只宣告已受理，不伪造历史已压缩结果

### Requirement: SESS-02 订阅意图与授权范围明确
SDK SHALL 提供会话列表订阅和准确会话消息订阅的建立、取消与恢复；列表快照过滤条件 MUST NOT 被宣传为同一连接事件订阅的安全隔离边界。

#### Scenario: SESS-02-S1 订阅列表并取快照
- **GIVEN** 调用者提供非空列表参数
- **WHEN** 订阅请求成功
- **THEN** 返回快照与订阅状态，并处理订阅期间交错事件

#### Scenario: SESS-02-S2 取消准确订阅
- **GIVEN** 某会话订阅已建立
- **WHEN** 调用者关闭订阅
- **THEN** 释放相应监听与远端订阅，不影响其他会话订阅

### Requirement: SESS-03 会话发送区分投递和生成
SDK SHALL 对发送、历史注入与触发生成保持不同语义，保留服务端返回的运行和投递关联；不以投递确认推断目标 Agent 已完成回复。

#### Scenario: SESS-03-S1 投递确认
- **GIVEN** 消息发送方法返回确认
- **WHEN** SDK 转换结果
- **THEN** 保留发送结果而不制造完整 Agent 输出

#### Scenario: SESS-03-S2 无执行权限
- **GIVEN** 用户只能读取会话
- **WHEN** 尝试发送或触发执行
- **THEN** 保留权限拒绝且不换用工具或 CLI 绕过

## Sources

[O06 会话控制与历史](https://docs.openclaw.ai/gateway/protocol/rpc-session-control)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
