# Gateway 聊天事件语义 — Spec Delta

## Purpose

为 openclaw-java-sdk 的Gateway 聊天事件语义定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CHAT-01 追加与替换事件区分
SDK SHALL 依据目标协议区分 deltaText 追加与 replace=true 替换；存在完整 message 快照时 SHALL 按其契约对账，不能把快照再次当作增量追加。

#### Scenario: CHAT-01-S1 替换更新
- **GIVEN** 缓存文本为 A
- **WHEN** 收到 replace=true 且 deltaText=ABC
- **THEN** 缓存变为 ABC 而不是 AABC，并将替换事实暴露给订阅者

#### Scenario: CHAT-01-S2 普通增量
- **GIVEN** 缓存文本为 A
- **WHEN** 收到追加 deltaText=B 后收到内容为 AB 的最终快照
- **THEN** 最终文本为 AB，不重复拼接

### Requirement: CHAT-02 运行身份严格关联
SDK SHALL 使用可获得的 agent、sessionKey、sessionId、runId 与连接代际明确关联事件；未知 runId MUST NOT 被路由到任意活动流；缺少标识时只有经目标版本验证且唯一无歧义的兼容路径才可接收。

#### Scenario: CHAT-02-S1 并发隔离
- **GIVEN** A 与 B 两个运行并发
- **WHEN** A 的事件与未知运行 C 的事件交错到达
- **THEN** A 只收到自己的事件，C 被隔离诊断且不进入 B

#### Scenario: CHAT-02-S2 生命周期切换
- **GIVEN** 会话已 reset 并产生新代际
- **WHEN** 收到旧连接或旧 sessionId 的迟到事件
- **THEN** 不污染新运行；必要时触发权威状态刷新

### Requirement: CHAT-03 终态错误和积压处理
SDK SHALL 以至多一次方式完成、取消或失败单次聊天流；回调异常、异常事件和流量溢出 SHALL 不导致其他流串扰或无限缓存。

#### Scenario: CHAT-03-S1 重复终态
- **GIVEN** 一个流已收到 final
- **WHEN** 再收到同一流的重复 final 或迟到 delta
- **THEN** 终态只通知一次，迟到事件不改变已完成结果

#### Scenario: CHAT-03-S2 缓冲上限
- **GIVEN** 消费者持续慢于事件生产且达到配置上限
- **WHEN** 下一个不可丢失事件到达
- **THEN** 产生明确溢出或恢复信号并执行受控终止，不静默丢弃或无限扩容

## Sources

[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
