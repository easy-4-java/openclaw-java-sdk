# OpenClaw Java SDK：官方能力整合路线与 OpenSpec 变更图

版本：0.1.0-draft · 日期：2026-09-20 · 状态：待评审。

## 目标与边界

把官方能力差距分析转成可审查、可验证和可分批实现的行为规范。首先修正已有协议行为，再建立受管理客户端，然后扩展领域操作。现有 HTTP、SSE、WS 与 CLI 的分工继续保留；Java 8/17/21 三条发行线保持同一已批准语义。

不在本轮执行范围：修改 SDK、添加依赖、运行真实 Agent、批准工具执行、创建发布、写入用户电脑或推送仓库。本包只交付文档。

## 变更依赖

```text
G0 版本与兼容治理
 └─ G1 协议契约修正
     └─ G2 RPC / 连接恢复 / 事件交付
         └─ G3 Agent / Session / Approval
             ├─ G4 Gateway 运营管理
             └─ G5 可选运行时适配
```

六组按独立变更保存。G4 和 G5 是较大领域集合，应按各自 capability 分批实现并逐项审查；仅有当前提案不授权并行大改。

| 编号 | Change ID | 优先级 | 规范数 |
|---|---|---|---|
| G0 | `establish-sdk-compatibility-governance` | P0 | 1 |
| G1 | `fix-openclaw-protocol-contracts` | P0 | 5 |
| G2 | `add-managed-gateway-client` | P0 | 3 |
| G3 | `add-agent-session-approval-control` | P1 | 3 |
| G4 | `add-gateway-operations` | P1/P2 | 6 |
| G5 | `add-optional-runtime-adapters` | P2 | 3 |

## 设计职责

proposal 解释为什么、范围与破坏性变化；specs 只写外部行为和验收场景；design 解释组件划分、候选方案、风险与迁移；tasks 把场景映射到可验收的工作项。类名和文件路径属于 design/tasks，而不是需求本身。

## 三分支协作

以 2.x 作为本次公共源码阅读参照，不因此指定它为未来唯一开发主干。每次实现必须明确起始分支，并把相同业务修改同步到其他适用分支；不能直接整分支合并来覆盖 JDK、Jackson 或 Maven 的发行线差异。已批准功能的测试场景和语义相同，二进制 ABI 不要求跨 Jackson 主版本一致。

建议根文档由文档专用提交维护，版本线只同步明确的文档和实现提交，不把本包当成已合并到 main 的事实。各条线独立记录测试 SHA、依赖来源与发布限制。

## 准入状态

人审：尚未发生。G0 证据已记录本机可获得的 OpenClaw `2026.7.1-2 (0790d9f)` 及来源，并完成 [真实 Gateway 基线验证](../../openspec/changes/establish-sdk-compatibility-governance/evidence/2026-11-18-live-gateway.md)。锁定版机器可读协议 schema 仍缺失，版本锁定待人审确认，G1 编码保持阻断。首次文档编制时 OpenSpec CLI 不可用；后续 G0 证据记录了 OpenSpec 1.8.0 strict 校验和三分支构建结果，2026-11-18 又以 OpenSpec 1.14.1 strict 校验 6/6 通过。CLI 格式校验和历史构建不代替当前 SDK 行为或真实 Gateway 验收。

## 尚未纳入强承诺的扩展

实时官方索引还可能包含新渠道、A2A、云会话/Worker、ClawHub 独立服务等。上一轮差距清单并非官方目录的穷尽审计；这些能力必须单独完成需求与版本评估，不因本文已有 21 个 capability 就宣称全覆盖。Gateway 内部 worker 私有协议、模型/沙箱内核与原生插件运行时不属于本 Java 外部 SDK 的默认实现职责。

## 完成定义

文档完成：四类产物齐全、结构与引用可检查，保持待评审。能力完成：目标版本锁定、场景真正执行、每个适用分支有证据、兼容差异获准、人审接受。规范生效：满足本项目归档条件后才更新根 specs；不得把“任务已生成”写成“功能已实现”。
