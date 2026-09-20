# 建立 Agent、Session 与 Approval 执行闭环 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

新增组合式 AgentClient、SessionClient 和 ApprovalClient，复用受管理 RPC 与事件交付；不继续把所有方法堆到 OpenClawClient。门面仅提供领域入口与少量兼容便捷方法。

RunHandle 保存已知的 agentId、sessionKey、sessionId、runId、关联任务与本地等待状态。远端运行事实和本地 Future 状态分开存储，wait timeout 不等于 runtime failure，local cancel 不等于 remote cancel。取消方法需从锁定目标版本中明确映射，不能臆造 agent.cancel。

Agent 定义与文件管理绑定明确目标。Session 控制支持创建、解析、描述、修改、重置、删除、压缩和准确订阅；以服务端实际返回身份更新状态，不盲目把 sessionKey 当物理身份。

审批读取、事件和决策分别建模，保留审批类型与有效决策，服务端决定唯一权威终态。不会在构造器、事件监听器或恢复流程里自动批准。并发已决、过期、拒绝、权限不足与断线不确定均为一等结果。

备选方案是统一成一个 Object/Map 管理客户端，扩展快但下游难以正确处理生命周期；另一方案是把审批放在本地自动策略中，越过了宿主授权意图。本方案选择窄领域 API 和服务端权威结果。

## Technical Approach

Agent 提交 → RunHandle → 订阅／等待 → 审批事实通知 → 宿主显式决策 → 远端状态对账 → 最终结果

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Agent 领域客户端与运行句柄）`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/params/`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/result/`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Session 领域客户端）`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Approval 领域客户端）`

## Risks / Trade-offs

本地与远端状态混淆 → 在类型和测试中分开表示；事件早于受理响应 → 在受管理层预登记关联并通过服务端身份对账。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)；[O06 会话控制与历史](https://docs.openclaw.ai/gateway/protocol/rpc-session-control)；[O08 设备、Node、审批与 Cron RPC](https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals)；[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
