# 修正现有 HTTP、SSE 与 WS 协议契约

状态：DRAFT / REVIEW_REQUIRED。文档已提出，未批准实现，未归档。优先级：P0。

## Why

现有通信面已存在，但静态核查发现部分行为与文档契约不一致。应先修正可能拼错内容、串流或误导副作用判断的行为，再扩展新方法。

## What Changes

- `responses-streaming`: Responses 流式契约。
- `gateway-chat-events`: Gateway 聊天事件语义。
- `gateway-device-auth`: 设备握手与凭据生命周期。
- `session-history-contract`: 会话历史与分页契约。
- `tools-invoke-contract`: Tools Invoke 真实调用语义。

- **BREAKING（行为收紧，待评审）**：普通 Responses JSON 方法拒绝 stream=true；未知运行不再回退至任意流；未经验证的危险重试与权限回退禁止。

此次交付只创建 OpenSpec 文档与证据清单，不修改 Java 代码、构建文件、测试、依赖或发布配置。本提案描述后续拟实施行为，不证明这些能力现在已完成。

## Capabilities

### New Capabilities

- `responses-streaming`: Responses 流式契约。
- `gateway-chat-events`: Gateway 聊天事件语义。
- `gateway-device-auth`: 设备握手与凭据生命周期。
- `session-history-contract`: 会话历史与分页契约。
- `tools-invoke-contract`: Tools Invoke 真实调用语义。

### Modified Capabilities

无现有同名 OpenSpec 生效规范可修改。本次是首次引入这些能力的正式规范条目，因此使用 ADDED Requirements；并不意味着仓库中不存在相关旧代码。

## Impact

适用分支为 feature/1.0.x、feature/2.0.x、feature/3.0.x。具体源码触点见 design.md；可观测行为见 specs/；验证与执行步骤见 tasks.md。main 不作为第四条已验证发行线。

依赖变更：`establish-sdk-compatibility-governance`

不在范围：重写 OpenClaw 内部模型引擎、原生插件运行时、沙箱内核或 Gateway 私有 worker 协议；不提供自动授权、跨租户共享密钥隔离或恰好一次执行保证。

## Review and Entry Criteria

必须完成人工范围评审、锁定目标 Gateway 构建与协议映射，并完成依赖变更的必要验收后，才能进入本变更实现。OpenSpec 的 artifact 完整性状态不等于人审通过或产品已完成。具体门禁是本项目约定，而非声称 OpenSpec 官方强制该流程。
