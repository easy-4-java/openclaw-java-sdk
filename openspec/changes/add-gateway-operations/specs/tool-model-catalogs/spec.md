# 工具与模型目录 — Spec Delta

## Purpose

为 openclaw-java-sdk 的工具与模型目录定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CAT-01 目录与有效工具分开
SDK SHALL 区分所有目录条目、当前会话有效工具及实际可调用状态；展示目录中存在的工具 MUST NOT 等价于拥有执行权限。

#### Scenario: CAT-01-S1 目录含受限工具
- **GIVEN** 工具出现在 catalog 但不在当前 effective 集合
- **WHEN** 用户查看可用工具
- **THEN** 保留来源并显示当前不可用，不提升权限

#### Scenario: CAT-01-S2 会话条件变化
- **GIVEN** 会话或权限已改变
- **WHEN** 重新查询 effective 工具
- **THEN** 返回当前授权下的新结果，不复用其他会话缓存

### Requirement: CAT-02 RPC 成功与工具成功分开
SDK SHALL 为 WS 工具调用保留传输、RPC 与工具业务结果层次及请求身份；业务 ok=false SHALL 不被正常 Future 完成掩盖为操作成功。

#### Scenario: CAT-02-S1 业务拒绝
- **GIVEN** RPC 外层成功、工具结果失败
- **WHEN** 上层检查执行结果
- **THEN** 能读取明确失败、原因与可用输出

#### Scenario: CAT-02-S2 写工具确认未知
- **GIVEN** 工具可能产生副作用但连接中断
- **WHEN** 处理错误
- **THEN** 不给出安全重试结论，也不透明地切换 HTTP 再执行

### Requirement: CAT-03 模型及命令目录按能力刷新
SDK SHALL 读取目标版本支持的模型和命令目录，保留服务端标识及能力限制；缓存失效后 SHALL 重新确认，不能用旧别名或未获授权的模型替换请求。

#### Scenario: CAT-03-S1 目录变化
- **GIVEN** 服务端通知相关目录失效
- **WHEN** 下一次获取目录
- **THEN** 刷新后使用服务端标识并保留元信息

#### Scenario: CAT-03-S2 模型不支持
- **GIVEN** 目标模型或命令不在获准支持范围
- **WHEN** 调用者请求使用
- **THEN** 返回明确不可用原因，不静默选用成本或权限不同的替代项

## Sources

[O10 工具、模型与 Skills 管理](https://docs.openclaw.ai/gateway/protocol/operator-methods)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
