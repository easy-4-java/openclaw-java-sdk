# 来源登记与证据边界

文档日期：2026-09-20。SDK 提交可以精确定位；上游页面为本轮读取的在线文档，不是已归档的版本化原文，也不是已运行验证的 Gateway 版本。O16 为文档索引指向的 CLI 参考入口，本轮未按全部子命令逐项复核。

## 官方规范来源

| ID | 内容 | 原始来源 |
|---|---|---|
| S01 | OpenSpec 概念与变更目录 | https://github.com/Fission-AI/OpenSpec/blob/main/docs/concepts.md |
| S02 | OpenSpec spec-driven schema | https://github.com/Fission-AI/OpenSpec/blob/main/schemas/spec-driven/schema.yaml |
| S03 | OpenSpec CLI validate 与状态命令 | https://github.com/Fission-AI/OpenSpec/blob/main/docs/cli.md |
| O00 | OpenClaw 文档索引 | https://docs.openclaw.ai/llms.txt |
| O01 | 外部应用接入边界 | https://docs.openclaw.ai/gateway/external-apps |
| O02 | 协议版本与客户端恢复边界 | https://docs.openclaw.ai/gateway/protocol/versioning |
| O03 | Responses HTTP 与 SSE | https://docs.openclaw.ai/gateway/openresponses-http-api |
| O04 | 会话启动、快照及事件语义 | https://docs.openclaw.ai/gateway/protocol/rpc-bootstrap-and-events |
| O05 | Gateway 握手与设备凭据 | https://docs.openclaw.ai/gateway/protocol/handshake |
| O06 | 会话控制与历史 | https://docs.openclaw.ai/gateway/protocol/rpc-session-control |
| O07 | HTTP Tools Invoke 与认证语义 | https://docs.openclaw.ai/gateway/tools-invoke-http-api |
| O08 | 设备、Node、审批与 Cron RPC | https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals |
| O09 | Config RPC 更新与生效 | https://docs.openclaw.ai/gateway/configuration/config-rpc |
| O10 | 工具、模型与 Skills 管理 | https://docs.openclaw.ai/gateway/protocol/operator-methods |
| O11 | Talk、Agent 与产物 RPC | https://docs.openclaw.ai/gateway/protocol/rpc-talk-config-and-agents |
| O12 | 系统、渠道、插件与终端 RPC | https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels |
| O13 | 任务、审计及用量台账 | https://docs.openclaw.ai/gateway/protocol/ledgers |
| O14 | MCP serve | https://docs.openclaw.ai/cli/mcp/serve |
| O15 | ACP bridge | https://docs.openclaw.ai/cli/acp |
| O16 | CLI 参考入口 | https://docs.openclaw.ai/cli |

OpenSpec schema 本轮读取的 Git blob：`688c38a20bd0350bf27003ccdb78215562b1a334`。这标识所读文件，不代表本环境安装了对应 CLI 发布版。

## SDK 提交与源码入口

| 分支 | 精确提交 |
|---|---|
| feature/1.0.x | 8cfbeb880a6c6ae5390fab0f9fe65e06938907be |
| feature/2.0.x | 48309d7bc34c4298cdfa1c52db06114b2e7e9f17 |
| feature/3.0.x | 3687fcb09e75ec4c2ecb522877e9a27c1f1955aa |
| main | c4ddcd3fa86d8fb5352b722813268760c35636b2 |

以 2.x 的固定提交列出共用静态分析入口。三分支差异必须在实施前复核，不能把单个入口的观察扩张为全仓库、全版本安全审计。

- C01：[Responses 普通 JSON 路径](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/api/OpenClawResponsesClient.java)。
- C02：[WS RPC 与聊天事件路由](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/ws/OpenClawGatewayWsClient.java)。
- C03：[HelloOk 响应模型](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/ws/protocol/HelloOk.java)。
- C04：[历史请求与结果](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/ws/protocol/result/ChatHistoryResult.java)。
- C05：[HTTP 工具请求模型](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/api/model/ToolInvokeRequest.java)。
- C06：[CLI 命令门面](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/cli/OpenClawCli.java)。
- C07：[CLI 一次性执行器](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/cli/OpenClawCliExecutor.java)。
- C08：[现有 Chat SSE 订阅](https://github.com/easy-4-java/openclaw-java-sdk/blob/48309d7bc34c4298cdfa1c52db06114b2e7e9f17/src/main/java/io/github/easy4j/openclaw/api/OpenClawSseClient.java)。

C01 和 C02 的相关实现本轮再次读取；其余入口沿用同一会话前轮静态核查，HEAD 本轮再次核对无变化。它们不是运行证据。

## 本轮未执行的事项

未运行 codegraph、Maven 构建、真实 Gateway 集成、漏洞扫描、堆/线程分析或生产就绪检查。尝试在沙箱获取仓库及 OpenSpec CLI 时遇到 DNS/网络解析失败；没有把此前本机 Tunnel 的状态推断为本轮状态。首次文档编制阶段没有修改用户本机，也没有提交 GitHub；本次文档提交保留该阶段的证据边界。

## 实施前必须补全

固定目标 Gateway 的精确版本/提交、可复现构建来源和协议文件；保存脱敏 wire 样本与命令结果；记录测试环境和认证模式。无法核实的上游字段、方法和权限保持待验证，不猜测其在某发行版中的可用性。
