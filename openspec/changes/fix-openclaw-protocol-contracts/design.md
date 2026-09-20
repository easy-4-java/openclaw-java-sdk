# 修正现有 HTTP、SSE 与 WS 协议契约 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

保留已有 HTTP、SSE、WebSocket 与 CLI 分工。Responses 使用独立事件解码与聚合，不将 ChatChunk 直接套用；复用有界读取与取消的传输基础，不复制另一套无界线程池。

聊天聚合以准确运行身份与连接代际为键。追加、替换、完整快照和终态通过明确事件区分；当 runId 未知时隔离诊断。不能采用“找最后一个活动流”的宽松回退。旧监听器通过适配或新增默认方法兼容；替换无法表示为旧追加回调时应使用完整结果／新事件接口并明确迁移，不向旧接口投递会产生错误文本的伪增量。

HelloOk 补齐设备凭据、快照与策略，并通过宿主 CredentialStore 和签名策略处理。Java 8 不假定内置 Ed25519 提供者；保留宿主签名接口，任何可选密码学提供者应独立评审。

历史 DTO 保留可选字段的缺省与 null 语义。Tools Invoke 增补已核实字段，但保留兼容 dryRun 字段并明确其不是安全预演。JSON Responses 入口的 stream=true 改为发送前拒绝，是显式的行为收紧。

备选方案是继续宽容解析并在 README 警告，不能消除错误路由；另一方案是强制所有用户迁移新门面，会扩大破坏面。本方案采用局部修正与加法式 API。

## Technical Approach

请求校验 → 正确传输入口 → 独立解码 → 身份关联／状态聚合 → 明确终态 → finally 清理

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `src/main/java/io/github/easy4j/openclaw/api/OpenClawResponsesClient.java`
- `src/main/java/io/github/easy4j/openclaw/api/OpenClawSseClient.java`
- `src/main/java/io/github/easy4j/openclaw/api/model/ResponseRequest.java`
- `src/main/java/io/github/easy4j/openclaw/api/model/ResponseResult.java`
- `src/main/java/io/github/easy4j/openclaw/api/sse/（新增 Responses 流事件与订阅适配）`
- `src/main/java/io/github/easy4j/openclaw/ws/OpenClawGatewayWsClient.java`
- `src/main/java/io/github/easy4j/openclaw/ws/ChatStreamHandler.java`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/HelloOk.java`
- `src/main/java/io/github/easy4j/openclaw/ws/OpenClawGatewayDeviceIdentity.java`
- `src/main/java/io/github/easy4j/openclaw/OpenClawHttpClientConfig.java`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/params/ChatHistoryParams.java`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/result/ChatHistoryResult.java`
- `src/main/java/io/github/easy4j/openclaw/ws/protocol/result/SessionsListResult.java`
- `src/main/java/io/github/easy4j/openclaw/api/model/ToolInvokeRequest.java`
- `src/main/java/io/github/easy4j/openclaw/api/OpenClawToolInvokeClient.java`

## Risks / Trade-offs

行为收紧影响依赖旧错误行为的用户 → 记录 BREAKING 行为说明并给出迁移示例；新增字段在旧 Gateway 缺省 → 通过旧版本样本验证兼容。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)；[O03 Responses HTTP 与 SSE](https://docs.openclaw.ai/gateway/openresponses-http-api)；[O04 会话启动、快照及事件语义](https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events)；[O05 Gateway 握手与设备凭据](https://docs.openclaw.ai/gateway/protocol/handshake)；[O06 会话控制与历史](https://docs.openclaw.ai/gateway/protocol/rpc-session-control)；[O07 HTTP Tools Invoke 与认证语义](https://docs.openclaw.ai/gateway/tools-invoke-http-api)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
