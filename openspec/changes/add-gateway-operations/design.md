# 补齐 Gateway 运营与管理能力 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

按 Config、Cron、Catalog、Skills/Plugins、Channels/Devices/Nodes、Tasks/Audit/Usage/Artifacts 分组实现。每个组具有独立 DTO、测试和方法映射，共用 RPC／事件核心，不新增多套连接。

Config 保存 snapshot hash，读改写带 baseHash；数组替换按 replacePaths 契约处理。持久化与运行时生效分别呈现；不要把失败后的自动覆盖或回滚当成默认恢复。

Cron 手动触发只返回排队结果，按准确 runId 查询历史。工具目录与当前有效权限分开；WS 外层 ok 与工具业务 ok 分层。Skills/Plugins 的发现、包传输、安装和运行时发布分别记录；即使新包文件已落盘，也不代表插件已加载。

渠道发送保持账户和线程身份；Node 控制区分 operator 与 node 角色。Node 的具体执行处理器由宿主注册且按允许命令执行，不提供默认任意命令处理器。设备配对和凭据操作不自动升级权限。

任务、审计、用量和产物是只读/受控查询面。产物下载另用最小权限请求，不携带 Gateway 凭据跨域；流式大小限制和过期处理与正常内容错误分离。

备选方案是全部转调 CLI，复用简单但远程场景需要本地二进制且错误语义不稳定；全部动态 Map RPC 则失去类型价值。本方案保留 CLI 兼容，同时提供独立的原生领域 API。

## Technical Approach

领域请求 → 作用域与版本映射 → Gateway RPC → 分阶段结果 → 事件失效通知 → 权威查询／分页

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `src/main/java/io/github/easy4j/openclaw/ws/protocol/result/ConfigGetResult.java`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Config 领域客户端）`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/result/CronListResult.java`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Cron 领域客户端）`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Catalog 领域客户端）`
- `src/main/java/io/github/easy4j/openclaw/api/OpenClawToolInvokeClient.java`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Skills 与 Plugins 管理客户端）`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Channels、Devices 与 Node 控制客户端）`
- `src/main/java/io/github/easy4j/openclaw/ws/（新增 Tasks、Audit、Usage 与 Artifacts 查询客户端）`

## Risks / Trade-offs

一次变更范围大 → 按 capability 与任务组分批实现和审查，未通过的能力不标为支持；高权限操作误触发 → 默认不执行管理写入，仅显式方法调用生效。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O08 设备、Node、审批与 Cron RPC](https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals)；[O09 Config RPC 更新与生效](https://docs.openclaw.ai/gateway/configuration/config-rpc)；[O10 工具、模型与 Skills 管理](https://docs.openclaw.ai/gateway/protocol/operator-methods)；[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)；[O12 系统、渠道、插件与终端 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels)；[O13 任务、审计及用量台账](https://docs.openclaw.ai/gateway/protocol/ledgers)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
