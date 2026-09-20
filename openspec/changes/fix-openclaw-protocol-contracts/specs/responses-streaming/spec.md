# Responses 流式契约 — Spec Delta

## Purpose

为 openclaw-java-sdk 的Responses 流式契约定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: RSP-01 JSON 与 SSE 入口明确分离
SDK SHALL 区分普通 Responses JSON 响应和 Responses SSE 流；普通 JSON 入口接到 stream=true 时 SHALL 在发出请求前拒绝并指向流式入口，流式入口 SHALL 使用独立事件解析而非 Chat Completions chunk 解析。

#### Scenario: RSP-01-S1 启动 Responses 流
- **GIVEN** 用户选择流式入口且请求有效
- **WHEN** 服务端返回 text/event-stream
- **THEN** 返回可取消订阅并逐项分发 Responses 事件

#### Scenario: RSP-01-S2 错误入口
- **GIVEN** 用户向普通 JSON 入口传 stream=true
- **WHEN** 提交请求
- **THEN** 在网络发送前获得参数错误，不把 SSE 当作完整 JSON 解析

### Requirement: RSP-02 流式终态与内容重建
SDK SHALL 保留 output item、content part、工具调用标识及用量，并区分 completed、incomplete、failed 与本地取消；每条订阅的终态回调 SHALL 至多发生一次。

#### Scenario: RSP-02-S1 多输出项完成
- **GIVEN** 同一响应含文本和 function_call 输出项
- **WHEN** 增量事件及 completed 到达
- **THEN** 分别重建输出项，保留 call_id、参数与用量，完成一次

#### Scenario: RSP-02-S2 未完成或传输中断
- **GIVEN** 服务端返回 incomplete、failed 或未终态即 EOF
- **WHEN** 流结束
- **THEN** 报告相应远程终态或传输中断，禁止当成 completed

### Requirement: RSP-03 续接与被忽略参数保持真实语义
SDK SHALL 保留 previous_response_id、function_call_output 的调用关联及空字符串输出；对目标版本接收但忽略的参数 SHALL 明确说明，不承诺这些字段带来存储、推理或工具次数限制效果。

#### Scenario: RSP-03-S1 空工具输出续接
- **GIVEN** 已有 function_call 的 call_id
- **WHEN** 以空字符串输出继续该响应
- **THEN** 原样保留标识和空值并形成有效续接输入

#### Scenario: RSP-03-S2 参数被忽略
- **GIVEN** 目标契约声明 store 或 reasoning 被忽略
- **WHEN** 调用者设置该字段
- **THEN** 文档与诊断说明不保证执行效果，SDK 不伪造生效证明

## Sources

[O03 Responses HTTP 与 SSE](https://docs.openclaw.ai/gateway/openresponses-http-api)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
