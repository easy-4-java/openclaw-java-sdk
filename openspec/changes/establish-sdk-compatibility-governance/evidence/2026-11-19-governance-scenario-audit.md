# G0 场景证据核对（2026-11-19）

[规格场景](../specs/sdk-compatibility-governance/spec.md)的当前检查记录如下。原始追踪表保留首次编制时的历史状态。真实 Gateway 验证结果以对应 SDK 提交、目标版本和具体调用为准。

| 场景 | 已核对的证据 | 剩余验收 |
|---|---|---|
| GOV-01-S1 真实通过可追溯 | [实测记录](2026-11-18-live-gateway.md)绑定三条维护线、两版 Gateway、JDK、Maven、命令和四项测试。 | 核对发布兼容矩阵的实际内容及评审记录。 |
| GOV-01-S2 缺少运行证据 | [能力清单](capability-inventory.json)保留静态存在、未封装以及 `liveVerified=false`。维护线 README 仅声明已实测的 WebSocket 范围。 | 发布时逐项核对能力声明。 |
| GOV-02-S1 依赖适配差异 | [共用样本](fixtures/GatewayCompatibilityFixtureTest.java)在 Java 8、17、21 上执行，两版 Gateway 共通过 24 项检查。 | 流重建、终态语义及允许差异清单仍待验收。 |
| GOV-02-S2 单分支缺测 | [实测记录](2026-11-18-live-gateway.md)为各维护线分别列出结果，并独立记录默认分支拒绝；其它协议能力仍未标记为通过。 | 用只验证一条线的样本演示发布流程如何阻止全线声明。 |
| GOV-03-S1 文档评审 | [设计稿](../design.md)标明草案，[任务清单](../tasks.md)保留未完成项。 | 维护者记录范围、版本和依赖审批。 |
| GOV-03-S2 提前归档 | [归档判定](../design.md#评审与归档判定)列明阻断项，生效规范尚未更新。 | 维护者确认归档拒绝条件和验收记录。 |
| GOV-04-S1 版本升级 | 本页对两个精确提交的官方 schema 进行离线比较。候选版握手与会话读取有独立实测。 | 确认候选字段的样本、语义和采用范围。 |
| GOV-04-S2 已移除调用 | 锁定版核心注册表声明 `tasks.list`，候选版核心方法清单与连接公告均缺少该项。三条维护线实测候选版返回 `FORBIDDEN`，SDK 保留错误并全程使用原权限。 | 当前 SDK 未封装该调用。显式的能力级不可用结果仍待验收。 |

## 锁定版与候选版 schema 差异

| Gateway | 上游提交 | 定义数 | 生成产物 SHA-256 |
|---|---|---:|---|
| 2026.7.1-2 | `0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c` | 322 | `0c5758a9750691b6fb070c6344297bce6ddeba89afd715e45531a9508a168699` |
| 2026.11.5 | `e13cf15ece9bbd969b7362927c315362c90c97ff` | 1149 | `fd29d1da7e6ebd0c1014617611ff442164495b3130bbdaea79ca87925324b8b6` |

两份产物均为 Draft-07，全部本地 JSON Pointer 引用均可解析。按定义名称比较原始 JSON 对象，共有 275 项同名定义，其中 126 项相同、149 项不同；874 个名称仅在候选版出现，47 个名称仅在锁定版出现。候选版还生成 559 条核心方法元数据。定义数受展开方式和生成器变化影响，方法是否存在应通过注册表及运行验证确认。

- `ConnectParams` 新增 `computerUse`、`includeSessionMessageBodies`、`modelCatalog` 和 `workerRuns` 字段，顶层必填字段列表相同。
- `SessionsListParams` 新增 `rowMode`、`ownerId`、`ifRowsRevision` 等字段。候选版元数据将 `sessions.list` 归到 `operator.read`。两版运行验证均覆盖读取成功及缺少权限的拒绝。
- `RequestFrame` 新增 `expectedProfileId`、`traceparent`，`EventFrame` 新增 `recipientProfileId`。此次未对新增字段执行运行验证。

候选版使用原始 [上游生成器](https://github.com/openclaw/openclaw/blob/e13cf15ece9bbd969b7362927c315362c90c97ff/scripts/protocol-gen.ts)，环境为 Node 24.18.0、tsx 4.23.15 和 typebox 1.4.2。临时检出按同一 tag 补齐 `packages/gateway-protocol`、`packages/normalization-core`、`scripts`、`src/gateway` 和 `src/shared`。其中 `@openclaw/normalization-core` 的构建导出使用同版本 npm 发布包的 `dist/normalization-core`，由临时符号链接提供给检出目录的包导出入口。生成器调用 `node --import ./scripts/tsx.mjs scripts/protocol-gen.ts --out <临时路径>`，执行两次，两个文件逐字节相同。上游检出中的跟踪文件未修改。

本次对照保留原目标版本及评审状态。候选版新增行为应按规格另行验收。

## 核心方法移除后的运行验证

锁定版 [核心方法表](https://github.com/openclaw/openclaw/blob/0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c/src/gateway/methods/core-descriptors.ts)将只读方法 `tasks.list` 归入 `operator.read`。候选版官方生成器输出的核心方法表无此项。两版真实 Gateway 均关闭插件，分别用 `hello.features.methods` 确认方法公告存在与缺失。

[RemovedGatewayMethodFixtureTest.java](fixtures/RemovedGatewayMethodFixtureTest.java)复用相同的临时 Token 类型、协议 4 握手和仅 `operator.read` 的连接权限。通过反射调用 SDK 的内部 RPC 路径，测试分别观察：

| Gateway | 三条维护线结果 |
|---|---|
| 2026.7.1-2 | 公告中含 `tasks.list`，只读调用返回结果。 |
| 2026.11.5 | 公告中缺少 `tasks.list`，同一调用被拒绝。SDK 保留原方法名、`FORBIDDEN` 和服务端权限错误。 |

候选版首次探测按假设期待 `INVALID_REQUEST`，三条线均观测到 `FORBIDDEN`。Gateway 日志确认拒绝发生在权限检查。最终断言使用观测到的错误码，在原权限下复验。两版各 3 项通过，零失败、零跳过。每次只发送一次目标调用。测试随后独立发起 `sessions.list`，验证原会话仍可用。测试记录所有发送的方法名，确认序列只含握手、目标调用和后续显式检查。

该调用没有公开的 typed SDK 封装。证据覆盖核心公告、当前权限下的拒绝和 SDK 内部错误保留。候选版未新增权限或替代方法调用，能力级不可用语义仍待验收。

复核使用 [共用样本的本地启动方法](2026-11-18-live-gateway.md#三条维护线共用的-gateway-测试样本)，将测试文件换为本样本，并将 `SDK_PROBE_TARGET` 分别设为 `locked` 或 `candidate`。运行对应 JDK 的 Maven `-Dtest=RemovedGatewayMethodFixtureTest test`。
