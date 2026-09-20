# Skills 与 Plugins 生命周期 — Spec Delta

## Purpose

为 openclaw-java-sdk 的Skills 与 Plugins 生命周期定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: EXT-01 发现与安装状态区分
SDK SHALL 支持目标契约内的 Skills 与 Plugins 状态、发现、详情和管理动作，区分可发现、已安装、已启用、运行时已加载及不可用。

#### Scenario: EXT-01-S1 已安装未加载
- **GIVEN** 插件安装记录存在但运行时未加载
- **WHEN** 查询状态
- **THEN** 分别呈现磁盘和运行时状态，不把安装成功当作可调用

#### Scenario: EXT-01-S2 安装来源受限
- **GIVEN** 来源不符合宿主或 Gateway 策略
- **WHEN** 请求安装
- **THEN** 返回拒绝结果，不禁用校验或从替代来源暗中下载

### Requirement: EXT-02 包传输有界且可取消
对目标版本提供的分块上传，SDK SHALL 遵守顺序、大小、完整性和终结规则，传输可取消且不得无限缓冲；不支持分块时不得伪造协议。

#### Scenario: EXT-02-S1 受控上传
- **GIVEN** 包满足大小与来源要求
- **WHEN** 完成全部块并提交
- **THEN** 只有服务端确认后才报告包传输完成

#### Scenario: EXT-02-S2 中途失败
- **GIVEN** 上传中断或完整性检查失败
- **WHEN** 执行失败清理
- **THEN** 报告未完成并按目标协议释放暂存，不宣告安装成功

### Requirement: EXT-03 运行时发布按阶段反馈
SDK SHALL 保留启停、更新、重载和卸载的持久化与运行时发布结果；更新通知 SHALL 触发状态查询而非自动重做安装。

#### Scenario: EXT-03-S1 发布变化
- **GIVEN** 收到 plugins.changed 或技能失效事件
- **WHEN** 刷新状态
- **THEN** 读取权威状态并区分新 generation，不无条件重装

#### Scenario: EXT-03-S2 部分阶段失败
- **GIVEN** 持久化已成功但运行时发布失败
- **WHEN** 返回结果
- **THEN** 保留部分完成事实和可用恢复建议，不盲目重新安装

## Sources

[O10 工具、模型与 Skills 管理](https://docs.openclaw.ai/gateway/protocol/operator-methods)；[O12 系统、渠道、插件与终端 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
