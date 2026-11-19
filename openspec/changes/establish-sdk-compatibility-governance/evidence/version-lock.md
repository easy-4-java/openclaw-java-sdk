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

已用锁定提交的官方生成器产出 [机器可读协议 schema](2026-11-18-locked-gateway-schema.md)。该版本的协议包属于上游私有工作区。运行时行为见 [真实 Gateway 基线验证](2026-11-18-live-gateway.md)。G0 任务 3.1 已执行，版本锁定和 G1 编码仍待人审。
