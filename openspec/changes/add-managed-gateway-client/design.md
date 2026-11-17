# 建立受管理 RPC、连接恢复和事件交付 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

采用组合式 GatewayConnectionManager + GatewayRpcClient + SubscriptionRegistry 的逻辑分层。这些是拟议内部组件名，不是现有 API。兼容保留旧 OpenClawGatewayWsClient 入口；底层 transport 可更换，但业务契约不绑定具体 WebSocket 类。

异步调用为基础，同步调用作为受约束等待的适配层。统一处理序列化、登记、发送、响应、deadline、取消与断线；所有出口都清理 pending 和超时任务。避免在事件交付线程中执行阻塞等待，以免自锁。

状态机采用 NEW → CONNECTING → AUTHENTICATING → READY；故障进入 RECONNECT_WAIT／FAILED，关闭进入 CLOSING → CLOSED。只有显式重连策略可以重新连接；关闭是终结态。重连预算、退避、抖动、服务端 retryAfter、liveness 和连接代际统一管理。

恢复先重建订阅，再读取快照并处理交错事件；使用版本化身份而非简单按 key 覆盖。默认不重放写请求，已发送无确认则产生 OutcomeUnknown 类本地状态。此名为拟议本地分类，不能假装服务端返回这个错误码。

事件交付使用有界队列和回调异常隔离。能力清单只做兼容预检，不作为授权；被上游有意省略的发现项可通过明确版本映射和宿主意图调用，最终以服务端授权为准。

备选方案是在单个 WS 类中不断增加字段和锁，改动少但耦合与测试成本持续上升；完全替换公共类则破坏下游。本方案将新职责内聚为组合组件。

## Technical Approach

领域调用 → 公共异步 RPC → 连接／权限预检 → 请求注册 → transport → 响应或事件分发 → 终态与清理

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `src/main/java/io/github/easy4j/openclaw/ws/OpenClawGatewayWsClient.java`
- `src/main/java/io/github/easy4j/openclaw/OpenClawClient.java`
- `src/main/java/io/github/easy4j/openclaw/exception/（新增本地状态异常或结果类型）`
- `src/main/java/io/github/easy4j/openclaw/ws/（组合式连接管理器与订阅注册表，新增）`
- `src/main/java/io/github/easy4j/openclaw/OpenClawHttpClientConfig.java`
- `src/main/java/io/github/easy4j/openclaw/ws/OpenClawWsListener.java`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/EventFrame.java`

## Risks / Trade-offs

恢复流程引入新的竞态 → 使用虚拟时间、连接代际与确定性事件序列测试；缓存不能保证事件全量恢复 → 明确 gap 并读取权威快照。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)；[O05 Gateway 握手与设备凭据](https://docs.openclaw.ai/gateway/protocol/handshake)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
