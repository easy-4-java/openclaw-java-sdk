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
| 使用正常文本响应的有效 ID，以合成 `call_id` 提交空字符串工具输出 | JSON 和 SSE 均在调用模型前返回 HTTP 400 | JSON 和 SSE 均只调用模型一次，终态为 `completed` |
| 模型桩只给出合成客户端工具调用 | 取得 `function_call`，仅提交空的工具输出续接会返回 HTTP 400 | 本模型桩组合得到 HTTP 500 `api_error` |
| 模型桩给出文本与工具调用，客户端使用 SSE | 两个输出项，终态事件 `response.completed` 的资源状态为 `incomplete`，保留文本、调用 ID、参数和用量。工具项在 `output_item.done` 带 `status=completed`，最终快照省略该字段 | 本模型桩组合得到 `response.failed`，错误码 `api_error` |

候选版应让 SDK 同时保留 SSE 事件名和资源状态。旧版长度耗尽响应说明，仅看事件名会误判成功。续接拒绝时应保留 400，宿主需要显式选择是否携带完整历史重开。JSON `user` 在本次共享令牌模式中不是独立身份约束。候选实现以认证主体、agent 和显式会话请求头查找对应关系。 [候选版续接实现](https://github.com/openclaw/openclaw/blob/v2026.11.5/src/gateway/openresponses-http.ts)

前表的空字符串对照使用正常文本响应的有效 ID 和合成调用 ID。候选版标记的上游测试也覆盖工具结果续接，其中 agent 运行结果来自 mock。 [候选版空结果续接测试](https://github.com/openclaw/openclaw/blob/v2026.11.5/src/gateway/openresponses-http.test.ts#L1454)

现有三条 Java 发行线的 `OpenClawResponsesClient` JSON 路径将 `stream` 透传后再按 `ResponseResult` 解析。评审范围保留发送前拒绝错误入口和独立 SSE 解码。Java 的混合输出重建、空工具输出序列化、主动取消、终态前断流和流式入口仍需实施和验收。

运行 [review-responses.py](fixtures/review-responses.py)，位置参数依次为两个已安装的 OpenClaw 包目录。脚本检查版本并在进程退出前清理模型桩和 Gateway。工具桩只返回合成协议帧，未执行实际工具。


## 候选版显式工具配置

默认配置的候选版模型请求没有直接列出合成客户端工具。另起一个回环 Gateway，只允许 `fixture_result`，关闭工具发现，并将模型桩的上下文声明设为 262144。该配置仅用于验证客户端显式传入的合成工具。全程没有执行该工具。

- 首次 JSON 请求返回 `function_call`。续接请求重新提供工具声明，并使用原响应 ID、Gateway 返回的 `call_id` 和空字符串结果。真实 Gateway 只调用模型桩一次，模型输入携带相同工具 ID，SSE 以唯一的 `response.completed` 和 `[DONE]` 结束。
- 混合 SSE 请求在两个不同的 `output_index` 结束文本和工具项，终态保留相同项、调用 ID、参数与用量。候选版的最终资源和工具项都携带 `status=completed`。

这两项可作为 RSP-02-S1 与 RSP-03-S1 的 Gateway 输入样本。验收仍需覆盖 Java 协议层及部署所用的工具配置。

## 下游断开与上游连接回收

模型桩在发送文本增量后保持连接，既不发送终态也不自行关闭。客户端收到首个文本增量后关闭 SSE。两版 Gateway 均在桩的 8 秒保持窗口内关闭模型连接，桩通过 TCP EOF 或连接重置确认。每版的被取消请求只进入模型桩一次。同一 JSON user 和 agent 随后的非流式请求成功完成。此处验证 Gateway 对真实断开的处理。Java SDK 主动取消、调用方收到的终态，以及无终态断流仍需实施后验证。

## RSP-01-S2 的 Java 基线

[共享回环夹具](fixtures/ResponsesJsonEntryFixtureTest.java)在三条 Java 发行线各执行一项测试，均零失败、零错误、零跳过。每项先用普通 JSON 请求确认控制组成功，再向同一个 JSON 入口传 `stream=true`。回环服务器实际收到两次 HTTP 请求，其中一次带 `stream=true`。返回 SSE 后客户端才抛出 JSON 解析异常。该结果记录了现状，并验证了请求发出前拦截的验收缺口。

| Java | 发行线验证提交 |
| --- | --- |
| 8 | `3dc46a0e58200b086efc59029a43aa5c136bf61b` |
| 17 | `9293a8cd2580435ec076b076d98c7965b77f0049` |
| 21 | `34b003b5c2c2158e536d3290ef67de7932ab78ef` |

将夹具放入各提交的 `src/test/java/io/github/easy4j/openclaw/`，使用对应 JDK，运行 `mvn -B -ntp -Dtest=ResponsesJsonEntryFixtureTest test`。Java 21 使用 `./mvnw`。
