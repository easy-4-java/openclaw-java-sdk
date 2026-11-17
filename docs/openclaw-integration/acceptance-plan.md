# 验收计划与证据模板

本文件定义未来验收要求，不记录已通过的产品测试。本轮仅执行文档内部检查。

## 1. 验证层级

| 层级 | 内容 | 可支持的结论 |
|---|---|---|
| L0 文档结构 | 四类产物、格式、ID、来源、依赖、任务引用 | 文档内部一致；不证明产品行为 |
| L1 契约样本 | 固定版本的请求/响应/事件黄金样本 | 序列化、解析与本地状态规则 |
| L2 模拟故障 | MockWebServer、受控 WS 端点、虚拟时钟、测试子进程 | 并发、顺序、关闭和错误路径 |
| L3 分支构建 | JDK 8、17、21 对应分支和真实依赖源 | 各自构建及实际执行的测试结果 |
| L4 真实集成 | 固定版本 Gateway、受控账户/工作目录、明确授权 | 指定版本、场景、权限配置的真实兼容 |
| L5 发布审查 | API、依赖、扫描、许可证、资源与文档 | 有边界的发布准入，不是绝对无漏洞保证 |

L1/L2 的模拟成功不能替代 L4。静态找不到关闭路径可以作为风险线索，但不自动断言已经证明堆泄漏。

## 2. 最低核心场景

Responses：JSON 入口误用流式、多个输出项、工具参数增量、空工具输出续接、completed/incomplete/failed、EOF、取消、格式错误。WS：追加、替换、快照对账、未知 runId、双流交错、终态重复、会话 reset 后旧事件。认证：challenge/计时器竞争、有/无 deviceToken、可信与不可信引导、轮换、scope 拒绝。恢复：断线前后写入结果未知、订阅/快照交错、错误回调、资源释放、用户关闭后零重连。

控制面：agent 受理与 pending 等待、远端取消未确认、并发审批已决、配置 hash 冲突、保存/应用分离、Cron 按准确 runId 查询。运营面：目录不等于权限、工具业务 ok=false、扩展发布部分成功、目标账户隔离、Node 执行状态未知、用量缺省、产物下载过期与跨域。可选适配：终端附着不重跑、语音输出取消、MCP/ACP 初始化、双向管道、stderr 分离、进程退出、CLI JSON 模式和 Windows/POSIX 参数。

## 3. 资源检查的可测口径

在受控环境执行 1000 次请求/订阅生命周期（成功、失败、取消、超时各不少于 200 次，其余用于断线和回调错误）。每轮结束后等待有界清理预算（测试基线建议 5 秒，若需调整必须记录理由），确认 pending 请求、活动订阅和定时任务注册归零；关闭后自有 executor 终止、HTTP ResponseBody 关闭，共享连接仍可供第二客户端使用。

这不是对堆大小强行断言恒定。若出现持续增长，应采集堆转储、线程转储、对象引用与文件描述符证据，并在稳定负载和 GC 条件下定位。不得通过关掉断言或增大无限队列来“修复”检查。

## 4. 安全与错误语义

必须确认：不以 dryRun 当作无副作用；不以共享 Gateway Token 的 scope 头当作租户隔离；不自动批准；不自动读取宿主凭据或降级权限；不记录 Token、签名、短期下载地址或原始敏感业务内容。取消/等待超时/结果不确定/远端失败分开；工具、配置、插件等分阶段结果不被压成一个布尔成功。

真实写操作测试只能在获准的隔离环境运行，使用临时命名空间和明确清理步骤。文档任务不会授权访问真实客户、发送业务消息或修改生产配置。

## 5. 证据记录字段

```yaml
change_id: fix-openclaw-protocol-contracts
requirement_id: CHAT-01
scenario_id: CHAT-01-S1
branch: feature/2.0.x
sdk_commit: null
openclaw_version: null
openclaw_commit: null
protocol_version: null
jdk_version: null
maven_version: null
auth_mode: null
test_command: null
executed_tests: 0
result: NOT_RUN
evidence_path: null
known_limitations: []
```

null 明确表示尚未运行，不得提交为 PASS。PASS 记录须填精确值；缺环境填 BLOCKED，未跑填 NOT_RUN，失败填 FAIL。测试基线版本改变需要重新执行。

## 6. 官方 OpenSpec 校验命令（待实际运行）

先在可联网、受信任环境安装并记录官方 OpenSpec 的确定版本，再从仓库根目录运行：

```bash
openspec --version
openspec list --json
openspec validate --all --strict --no-interactive --json
openspec status --change fix-openclaw-protocol-contracts --json
```

不使用 latest 作为可复现 CI 的版本锁。不在本轮执行 apply、archive 或产品代码生成。官方 CLI 结构通过也不等于 128 个产品验收场景已经执行。
