# 会话历史的 2026.11.5 范围评审

使用 OpenClaw 2026.7.1-2、2026.11.5 和 Node 24.18.0。两个 Gateway 分别使用临时工作区、状态与令牌，只监听回环地址。用 Gateway RPC 创建测试会话并写入四条合成消息，再以两条消息为一页读取。测试只使用临时会话，未启动模型推理。 [候选版本会话控制契约](https://github.com/openclaw/openclaw/blob/v2026.11.5/docs/gateway/protocol/rpc-session-control.md)

| 场景 | 2026.7.1-2 | 2026.11.5 |
| --- | --- | --- |
| 读取最新两条 | 返回物理会话 ID、两个消息 ID 及已知空闲状态。响应无游标和 `nextOffset`。 | 同样返回两条和空闲状态，另有 `hasMore=true`、`nextOffset`、`olderCursor` 和 `deltaCursor`。 |
| 继续分页 | 提交 `cursor` 返回 `INVALID_REQUEST`。 | 使用 `olderCursor` 得到前两条且没有更旧游标。使用该页的 `newerCursor` 回到原消息 ID，末页没有更新游标。 |
| 使用 `deltaCursor` | 请求字段不受支持。 | 无变化时得到 `kind=delta` 和空消息列表。增加一条后，续读只返回新增消息。 |
| 重置临时会话，续读旧游标 | 本轮覆盖旧版对游标字段的拒绝。 | 旧游标得到 `kind=reset`。重新读取普通尾页得到一条 `system` 重置记录，原消息 ID 不再出现在尾页。 |

三条 Java 发行线 `3dc46a0`、`9293a8c`、`34b003b` 的 `ChatHistoryParams` 已有 `offset`，缺少 `cursor`。三线的 `ChatHistoryResult` 均缺少 `kind`、续页游标及 `sessionInfo`。本轮建议保留 HIST-01 的服务端续页信息、HIST-02 的状态缺省语义和 HIST-03 的重置后重读。受过滤消息和空页影响的续页、并发刷新与迟到事件仍需独立验证。SDK 实施以及目标版本审批保持待完成。

用 Node 24.18.0 运行 [review-chat-history.mjs](fixtures/review-chat-history.mjs)，位置参数依次为已安装的 2026.7.1-2 和 2026.11.5 包目录。脚本验证版本、启动各自 Gateway，经 WebSocket 实际执行请求并断言响应，最后清理临时状态。
