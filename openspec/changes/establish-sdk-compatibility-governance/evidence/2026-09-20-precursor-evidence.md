# G0 准入证据（2026-09-20 采集）

## 任务 1.2 版本锁定
见 `version-lock.md`。OpenClaw `2026.7.1-2 (0790d9f)`，npm 全局安装，Node v24.18.0。

## 任务 1.3 codegraph 证据（三分支独立检出）
| 分支 | SHA | 执行方式 | 结果 |
|---|---|---|---|
| feature/3.0.x | 3687fcb | codegraph MCP build（主 worktree） | head_matches_build=true，增量 23 文件 / 420 节点 |
| feature/2.0.x | 48309d7 | 同上（切分支后增量） | head_matches_build=true，16 文件 / 275 节点 |
| feature/1.0.x | 8cfbeb8 | 独立 worktree /tmp/wt-oc-1.0.x 全量 | 200 文件 / 2324 节点 / 13840 边 |

Java 支持正常，无执行失败项。命令：`build_or_update_graph_tool`（MCP）/ `codegraph` CLI 等价。

## 任务 3.3 构建与测试证据（每条分支独立）
| 分支 | JDK | Maven | 构建 | 测试 |
|---|---|---|---|---|
| feature/3.0.x | 21 (ms-21.0.12.1) | wrapper Maven 4 (./mvnw) | clean deploy BUILD SUCCESS | 155/155 绿 |
| feature/2.0.x | 17 (corretto-17.0.20.1) | 3.9.16 (mvn) | clean deploy BUILD SUCCESS | 155/155 绿 |
| feature/1.0.x | 8 (corretto-1.8.0_504) | 3.9.16 (mvn) | clean deploy BUILD SUCCESS | 155/155 绿 |

非零执行测试数：0。CI（GitHub Actions）：三分支最新 run 均 success。

## 任务 3.4 官方 OpenSpec CLI 校验
`openspec validate --all --strict --no-interactive --json`（CLI 1.8.0）：
6/6 changes valid（establish-sdk-compatibility-governance、fix-openclaw-protocol-contracts、
add-managed-gateway-client、add-agent-session-approval-control、add-gateway-operations、
add-optional-runtime-adapters），issues 均为空。

## 任务 2.1 能力证据清单
见 `capability-inventory.json`。静态存在 8 项（HTTP 五客户端、CLI、WS core、Spring 装配）、
未封装 4 项（G2-G5 目标能力）；全部 liveVerified=false，无任何 live-pass 标记。

## 阻塞项（人审/环境门禁，未勾选原因）
- 1.1 / 2.3 / 3.5：需人审记录（范围、批准、归档资格），AI 会话不能冒充。
- 2.2：三分支协议 fixture 与获准差异清单，待 1.1 批准后建立。
- 2.4：traceability 逐项映射，依赖 2.1-2.3 产物。
- 3.1：真实 Gateway 健康/认证/授权检查，需启动真实 Gateway（本机 openclaw 2026.7.1-2 可执行，未在本轮启动）。
- 3.2：文档漂移修正，随 G1 实施时逐分支核对。
