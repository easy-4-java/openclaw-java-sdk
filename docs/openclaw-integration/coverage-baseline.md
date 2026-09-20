# 官方能力与 SDK 静态覆盖基线

状态：静态观察，不是验收报告。完整来源见 [source-register](source-register.md)。

## 证据等级

`PARTIAL_STATIC` 表示已读代码存在相关实现但契约不完整；`CLI_ONLY_IN_REVIEWED_SURFACE` 表示已有 CLI 而本次检查的公共 WS 面未见对应类型化实现；`NOT_WRAPPED_IN_REVIEWED_SURFACE` 不等于全仓库永远无法间接调用；`VERIFY_SCOPE` 需要进一步逐版本核实。所有条目的真实集成状态均为 NOT_RUN。

| 能力 | 静态状态 | 观察边界 | 源码 | 对应规范 |
|---|---|---|---|---|
| Responses JSON / SSE | PARTIAL_STATIC | 普通 Responses 路径传递 stream 字段后仍按 JSON 解析；Chat SSE 入口不能证明 Responses SSE 已实现。 | C01,C08 | `responses-streaming` |
| Chat replace 与运行关联 | PARTIAL_STATIC | 所读 handler 追加文本，未知 runId 有活动流回退；属于静态风险，未运行复现。 | C02 | `gateway-chat-events` |
| 设备握手结果 | PARTIAL_STATIC | 此前所读 HelloOk 未完整接收设备 Token 和快照，输入配置不等于认证闭环。 | C03 | `gateway-device-auth` |
| 历史与分页恢复 | PARTIAL_STATIC | 已有历史查询，恢复和分页元数据需要补齐；不宣称当前完全不能查历史。 | C04 | `session-history-contract` |
| Tools Invoke | PARTIAL_STATIC | 已有请求和执行面；字段、dryRun 说明及安全语义需要修正。 | C05 | `tools-invoke-contract` |
| 公共受管理 RPC | NOT_WRAPPED_IN_REVIEWED_SURFACE | 现有通用请求管理为 private；底层可发原始帧不是公共受管理 API。 | C02 | `managed-rpc` |
| 自动连接恢复 | PARTIAL_STATIC | 已有连接与断线清理；尚未形成本文定义的恢复、对账和重放策略。 | C02 | `gateway-recovery` |
| 类型化事件 | PARTIAL_STATIC | 原始事件 listener 已有；不能把没有类型化 DTO 写成所有事件收不到。 | C02 | `typed-event-delivery` |
| Agent 生命周期 | CLI_OR_PARTIAL_STATIC | 有 Agent CLI、聊天和身份查询；缺本文定义的原生运行闭环。 | C02,C06 | `agent-lifecycle` |
| Session 控制 | CLI_OR_PARTIAL_STATIC | 有 list/send 与聊天 history/abort，其他控制与订阅需补齐。 | C02,C06 | `session-control` |
| Approval 闭环 | CLI_ONLY_IN_REVIEWED_SURFACE | 有审批 CLI；所读 WS 公共业务方法没有专用审批客户端。 | C02,C06 | `approval-control` |
| Config / Secrets | CLI_OR_PARTIAL_STATIC | 有 config.get 与 CLI；写入冲突和运行时生效契约需补齐。 | C02,C06 | `configuration-control` |
| Cron | CLI_OR_PARTIAL_STATIC | 有 cron.list 与 CLI；手动排队与准确 runId 跟踪需补齐。 | C02,C06 | `cron-control` |
| Tools / Models 目录 | CLI_OR_PARTIAL_STATIC | 已有部分 HTTP/CLI；WS 目录、有效权限和业务结果需专门封装。 | C02,C06 | `tool-model-catalogs` |
| Skills / Plugins | CLI_ONLY_IN_REVIEWED_SURFACE | 有 CLI；远程原生领域客户端和运行时阶段反馈未见于所读公共 WS 面。 | C02,C06 | `skill-plugin-lifecycle` |
| Channels / Devices / Nodes | CLI_ONLY_IN_REVIEWED_SURFACE | 有相关 CLI 和握手字段；不是完整远程控制和 node 执行闭环。 | C02,C06 | `channel-node-control` |
| Tasks / Audit / Usage / Artifacts | VERIFY_SCOPE | 部分有 CLI，本文定义的台账查询、用量与产物下载需逐版本核实。 | C02,C06 | `task-audit-artifacts` |
| Terminal / Talk / TTS | VERIFY_SCOPE | Terminal CLI 不等于交互终端客户端；语音协议要独立验证。 | C06,C07 | `terminal-voice-adapters` |
| MCP / ACP | CLI_ONLY_IN_REVIEWED_SURFACE | 已有启动命令，但一次性 executor 不等于持久双向协议适配。 | C06,C07 | `persistent-stdio-adapters` |
| Browser / Infer / CLI DTO | PARTIAL_STATIC | 已有自由参数入口；缺机器输出版本契约和精确支持等级。 | C06,C07 | `cli-typed-results` |
| 三分支发布证据 | VERIFY_SCOPE | 先前类数量和 blob 对比不等于行为或运行兼容证明。 | SDK HEAD | `sdk-compatibility-governance` |

## 不能沿用的推断

有 stream 字段不等于完成 SSE；存在 idempotencyKey 不等于恰好一次；HTTP 的共享 Token 加窄 scope 头不等于租户隔离；协议常量为 4 不等于完整支持所有 v4 事件；CLI 命令存在不等于原生协议或稳定机器输出。依据分别见 O03、O07、O02、O04 与 C06/C07。

## 现有行为与新规范的关系

本包未建立“已实现全功能”的根规范。全部拟实现契约放在 changes 下，修正既有代码的规范也是首次建档的 ADDED Requirements。后续如已存在同名生效规范，合并本包时必须改用匹配原规范的 MODIFIED 完整条目，不能直接覆盖或重复新增。
