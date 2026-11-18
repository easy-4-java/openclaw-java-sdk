# Responses 的 2026.11.5 范围评审

在回环地址分别启动 OpenClaw 2026.7.1-2 和 2026.11.5，启用 `/v1/responses`。Gateway 请求由本地确定性的 OpenAI Chat Completions 模型桩响应。工作区、状态与令牌均为临时测试值。使用 Node 24.18.0，禁用插件、计划任务及候选版远端模型目录刷新。 [候选版 Responses 契约](https://github.com/openclaw/openclaw/blob/v2026.11.5/docs/gateway/openresponses-http-api.md)

| 真实 Gateway 请求 | 2026.7.1-2 | 2026.11.5 |
| --- | --- | --- |
| 正常文本 SSE | 两条文本增量，唯一终态 `response.completed`，随后 `[DONE]` | 相同 |
| 模型桩报告输出长度耗尽 | 事件名为 `response.completed`，终态资源却标记 `failed` | `response.incomplete` 与资源 `incomplete` 一致 |
| 要求客户端工具而模型只生成文本 | `response.failed`，随后 `[DONE]` | 相同 |
| JSON 请求首次设置 `store=false`，再以有效 ID 续接 | 两次均完成 | 两次均完成 |
| 同令牌、同 agent，改动 JSON 的 `user` 后继续使用有效 ID | 请求成功 | 请求成功 |
| 使用有效 ID 但切换显式会话请求头 | 本轮未测 | HTTP 400，模型桩收不到此请求 |
| JSON 与 SSE 分别使用不存在的旧 ID | 两次都返回 HTTP 200，模型桩实际收到调用 | 两次都返回 HTTP 400 `invalid_request_error`，模型桩均未被调用 |
| 模型桩只给出合成客户端工具调用 | 取得 `function_call`，仅提交空的工具输出续接会返回 HTTP 400 | 本模型桩组合得到 HTTP 500 `api_error`，空工具输出的候选版验收未完成 |

候选版应让 SDK 同时保留 SSE 事件名和资源状态。旧版长度耗尽响应说明，仅看事件名会误判成功。续接拒绝时应保留 400，宿主需要显式选择是否携带完整历史重开。JSON `user` 在本次共享令牌模式中不是独立身份约束。候选实现以认证主体、agent 和显式会话请求头查找对应关系。 [候选版续接实现](https://github.com/openclaw/openclaw/blob/v2026.11.5/src/gateway/openresponses-http.ts)

现有三条 Java 发行线的 `OpenClawResponsesClient` JSON 路径将 `stream` 透传后再按 `ResponseResult` 解析。评审范围保留发送前拒绝错误入口和独立 SSE 解码。混合输出、空工具输出、客户端主动取消、终态前断流，以及实际 SDK 流式入口，仍需实施和验收。

运行 [review-responses.py](fixtures/review-responses.py)，位置参数依次为两个已安装的 OpenClaw 包目录。脚本检查版本并在进程退出前清理模型桩和 Gateway。工具桩只返回合成协议帧，未执行实际工具。
