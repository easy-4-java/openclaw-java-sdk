# Tools Invoke 的 2026.11.5 范围评审

用已锁定的 OpenClaw 2026.7.1-2 和候选 2026.11.5 各启动一个临时回环 Gateway。Node 版本为 24.18.0。Gateway 使用临时共享令牌、隔离配置和状态目录，禁用插件、计划任务及 mDNS。唯一执行的工具是只读 `sessions_list`，使用空测试工作区。

| 场景 | 2026.7.1-2 | 2026.11.5 |
| --- | --- | --- |
| 缺少令牌 | HTTP 401，`unauthorized` | 相同 |
| 错误令牌 | HTTP 401，`unauthorized` | 相同 |
| 有效共享令牌，读取会话列表 | HTTP 200，`ok=true`，有 `result` | 相同 |
| 有效令牌并声明 `operator.read`，读取会话列表 | HTTP 200，`ok=true`，有 `result` | 相同 |
| `dryRun=true` 并带固定的 `idempotencyKey`，读取会话列表 | HTTP 200，`ok=true`，有 `result` | 相同 |
| 不存在的工具名 | HTTP 404，`ok=false`，`not_found` | 相同 |
| `agentId` 与显式 `sessionKey` 不一致 | HTTP 400，`ok=false`，`invalid_request` | 相同 |

上述两轮共 14 个实际 HTTP 交换。共享令牌的授权范围和 `dryRun` 的保留字段语义以锁定版上游契约为准。只读工具在窄请求头下成功，无法单独证明实际授予了哪些 scopes。带 `dryRun` 的请求仍返回工具结果，候选版官方文档明确该字段当前被忽略。一次带键请求仅证明请求可接受，幂等执行语义仍待独立验证。 [候选版 Tools Invoke 文档](https://github.com/openclaw/openclaw/blob/v2026.11.5/docs/gateway/tools-invoke-http-api.md)

当前 SDK 的 Java 8 `3dc46a0`、Java 17 `9293a8c`、Java 21 `34b003b` 的 `ToolInvokeRequest` 均缺少 `agentId` 和 `idempotencyKey` 字段，`dryRun` 的 Javadoc 仍描述为仅校验。建议候选范围保留 TOOL-01 的显式关联、身份冲突及 TOOL-02 的文档修正和宿主策略。真实代理身份认证、SDK 对工具业务失败的结构化处理、生产网关及实施审批仍待后续验收。

复跑入口为 [review-tools-invoke.py](fixtures/review-tools-invoke.py)。给定两个已安装的 OpenClaw 包目录，依次传入 2026.7.1-2 和 2026.11.5；脚本检查包版本、启动和退出两个隔离 Gateway，并断言各场景的 HTTP 状态及响应字段。
