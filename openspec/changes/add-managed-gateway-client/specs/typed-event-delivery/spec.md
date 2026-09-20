# 类型化事件与资源边界 — Spec Delta

## Purpose

为 openclaw-java-sdk 的类型化事件与资源边界定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: EVT-01 类型化事件与原始兼容通道并存
SDK SHALL 对已承诺支持的事件族提供类型化结果，并保留原始事件扩展路径；未知事件 SHALL 可观测而不导致整条连接解析失败。

#### Scenario: EVT-01-S1 新增可选事件
- **GIVEN** 服务端发送 SDK 尚不认识的事件类型
- **WHEN** 事件被接收
- **THEN** 通过原始通道暴露且已有订阅继续工作

#### Scenario: EVT-01-S2 已知事件错误
- **GIVEN** 已知事件缺失必需关联字段
- **WHEN** 解析失败
- **THEN** 产生限定于该事件的诊断，不误投其他运行

### Requirement: EVT-02 有序交付与回调异常隔离
SDK SHALL 在承诺的订阅范围内保持事件顺序并隔离用户回调异常；队列有界且溢出策略 SHALL 可观察，不得静默丢失审批与终态。

#### Scenario: EVT-02-S1 回调抛异常
- **GIVEN** 监听器 A 抛出异常
- **WHEN** 同一事件需通知监听器 B
- **THEN** B 仍得到事件，异常进入诊断且清理继续

#### Scenario: EVT-02-S2 消费积压
- **GIVEN** 有界队列已满且到来关键终态
- **WHEN** 执行溢出策略
- **THEN** 产生明确中断或恢复信号，禁止谎称终态已交付

### Requirement: EVT-03 资源归属清楚且可验证释放
关闭订阅或客户端 SHALL 释放其拥有的请求、响应体、队列与调度资源；共享外部客户端或执行器 MUST NOT 被无条件关闭，关闭动作 SHALL 幂等。

#### Scenario: EVT-03-S1 关闭自有资源
- **GIVEN** 客户端拥有网络与线程资源
- **WHEN** 重复调用关闭
- **THEN** 活动注册最终归零且自有线程在规定预算内终止，无重复业务回调

#### Scenario: EVT-03-S2 共享资源
- **GIVEN** 两个客户端复用宿主传入的连接资源
- **WHEN** 只关闭其中一个
- **THEN** 另一个仍可执行请求，宿主继续拥有共享资源生命周期

## Sources

[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
