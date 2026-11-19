# 会话历史与分页契约 — Spec Delta

## Purpose

为 openclaw-java-sdk 的会话历史与分页契约定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: HIST-01 分页遵守服务端游标
SDK SHALL 保留目标版本返回的 hasMore、nextOffset 或游标、稳定锚点及消息身份；MUST NOT 以显示消息数或列表条数自行推算下一页。

#### Scenario: HIST-01-S1 过滤后的历史页
- **GIVEN** 历史返回条数不等于服务端扫描或分页步长
- **WHEN** 读取下一页
- **THEN** 使用返回的 nextOffset 或游标并按消息身份去重，不从显示条数推算

#### Scenario: HIST-01-S2 没有下一页
- **GIVEN** 响应明确表示结束或没有合法续页信息
- **WHEN** 使用分页迭代器
- **THEN** 终止迭代，不重复请求最后一页

### Requirement: HIST-02 恢复元数据保留缺省语义
SDK SHALL 保留 sessionInfo、inFlightRun 及目标版本提供的生命周期元数据，区分字段缺省、明确 null 与已知值；不适用字段 SHALL 按目标版本兼容说明处理。

#### Scenario: HIST-02-S1 历史携带运行
- **GIVEN** 历史结果含可恢复 runId 和会话身份
- **WHEN** 调用恢复逻辑
- **THEN** 只关联该准确身份，不猜测最近活动运行

#### Scenario: HIST-02-S2 未知活动状态
- **GIVEN** activeRunIds 或恢复信息缺省或为 null
- **WHEN** 合并状态
- **THEN** 按对应契约保留未更新或标记未知，不能无条件视为无运行

### Requirement: HIST-03 历史刷新不跨会话代际
SDK SHALL 防止慢历史响应覆盖更新的会话身份、reset 或删除结果；快照读取期间收到的失效事件 SHALL 触发必要的尾随刷新。

#### Scenario: HIST-03-S1 读取期间有更新
- **GIVEN** 历史读取尚未返回
- **WHEN** 收到改变同一会话的事件
- **THEN** 记录失效并在响应后对账，必要时再次刷新

#### Scenario: HIST-03-S2 旧代际删除
- **GIVEN** 相同 sessionKey 已对应新的 sessionId
- **WHEN** 旧 sessionId 的删除事件迟到
- **THEN** 保留新会话，不依据 key 单独删除新代际

## Sources

[O06 会话控制与历史](https://docs.openclaw.ai/gateway/protocol/rpc-session-control)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
