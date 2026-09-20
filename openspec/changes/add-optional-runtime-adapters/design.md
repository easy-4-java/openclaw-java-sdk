# 增加终端、语音、MCP、ACP 与 CLI 可选适配 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

核心 SDK 继续保持无 Spring 强制依赖；可选适配优先逻辑包隔离和显式构造，只有依赖无法兼容三条基线时才引入独立制品。物理模块拆分须另记决策，不能在本轮文档阶段先修改 pom。

终端和语音按服务端会话、调用身份、连接归属及输出流管理。重连附着不重跑命令；采音、终端启动和额外敏感配置读取必须由宿主显式发起，不随连接自动发生。

MCP 与 ACP 独立协商各自协议版本。使用 PersistentStdioSession 逻辑组件处理双向管道、帧关联、通知、取消、stderr、进程退出及关闭；不能复用“等待子进程结束再返回结果”的一次性 executor。先评估能满足 Java 8/17/21 的成熟协议库，不在规范中假定某库已兼容或已安装。

CLI 保留通用 argv 入口；仅为锁定版本中有稳定机器输出的 browser/infer 等动作逐步增加 DTO。自由参数透传仍标为透传。所有执行明确字面参数、工作目录、输出限制、超时、取消与敏感参数脱敏；Windows 与 POSIX 分别验证。

备选方案是强制引入一个全功能协议框架，集成快但可能提高 Java 基线；另一方案是继续透传原始 stdout，无法管理交互协议。本方案将适配器作为可选扩展，不改变核心默认运行行为。

## Technical Approach

宿主显式授权与选择适配器 → 版本协商／进程启动 → 双向会话 → 事件与取消 → 关闭自有资源

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `可选适配包：terminal、talk、tts（最终物理路径须在实施前与项目模块布局核对）`
- `src/main/java/io/github/easy4j/openclaw/cli/support/（独立持久 stdio 会话适配，不复用一次性结果语义）`
- `可选适配包：mcp、acp（不得引入核心模块的必需运行依赖）`
- `src/main/java/io/github/easy4j/openclaw/cli/OpenClawCli.java`
- `src/main/java/io/github/easy4j/openclaw/cli/OpenClawCliExecutor.java`
- `src/main/java/io/github/easy4j/openclaw/cli/OpenClawCliResult.java`
- `src/main/java/io/github/easy4j/openclaw/cli/opts/`

## Risks / Trade-offs

协议库最低 JDK 高于 1.x → 选型门禁与独立制品，不悄悄提高核心基线；长时间交互与输出过量 → 有界缓冲、超时和进程清理验证。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O11 Talk、Agent 与产物 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents)；[O12 系统、渠道、插件与终端 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels)；[O14 MCP serve](https://docs.openclaw.ai/cli/mcp/serve)；[O15 ACP bridge](https://docs.openclaw.ai/cli/acp)；[O16 CLI 参考入口](https://docs.openclaw.ai/cli)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
