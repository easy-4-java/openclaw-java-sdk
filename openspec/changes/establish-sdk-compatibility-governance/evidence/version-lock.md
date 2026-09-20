# 版本锁定记录（G0 任务 1.2）

锁定日期：2026-09-20。锁定人：hiwepy（经 AI 会话采集，待人审确认）。

## 目标 OpenClaw 版本

| 项 | 值 | 来源 |
|---|---|---|
| 版本 | `2026.7.1-2` | `openclaw --version` 实测输出 |
| 提交 | `0790d9f` | 同上（版本串内嵌短提交） |
| 构建来源 | npm 全局安装 | `~/.local/bin/openclaw` → `../lib/node_modules/openclaw/openclaw.mjs`（node 启动器） |
| Node 运行时 | v24.18.0（nvm） | `node --version` |

## 协议来源

- Gateway 协议：docs.openclaw.ai `/gateway/protocol/*`（protocol、auth、handshake、rpc-methods、rpc-session-control、transport、versioning）。
- CLI 命令树：docs.openclaw.ai `/cli`（含 infer=capability 别名、policy/voicecall/file-transfer 为可选插件等备注）。

## 声明

锁定仅覆盖本机可获得的最新发布版；未取得官方对应版本的协议 schema 机器可读制品，
协议字段以文档 + 真实 Gateway 观察（任务 3.1，未执行）为准。人审通过前 G1 编码保持阻断。
