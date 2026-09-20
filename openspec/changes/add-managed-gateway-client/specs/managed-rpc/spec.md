# 受管理的公共 RPC — Spec Delta

## Purpose

为 openclaw-java-sdk 的受管理的公共 RPC定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: RPC-01 公开的同步和异步请求契约
SDK SHALL 提供受管理的通用 RPC 调用，公开方法标识、参数、结果类型、deadline 与取消语义；类型化业务方法 SHALL 复用一致的关联和错误规则。

#### Scenario: RPC-01-S1 响应乱序
- **GIVEN** 两个请求有不同 ID
- **WHEN** 响应逆序到达
- **THEN** 各自 Future 仅由匹配 ID 的响应完成

#### Scenario: RPC-01-S2 未认证调用
- **GIVEN** 连接尚未完成握手
- **WHEN** 调用需要认证的业务方法
- **THEN** 得到明确状态错误，不发送未授权业务帧

### Requirement: RPC-02 所有失败路径释放等待者
请求成功、序列化失败、发送失败、deadline、中断、取消与断线时，SDK SHALL 终止等待并清理相关注册；本地取消 MUST NOT 被描述为已经取消远端工作。

#### Scenario: RPC-02-S1 发送之前失败
- **GIVEN** 请求已登记但发送抛出异常
- **WHEN** 失败处理完成
- **THEN** Future 失败且 pending 数量恢复，不留下定时器或注册

#### Scenario: RPC-02-S2 deadline 到期
- **GIVEN** 远端是否执行未知且无响应
- **WHEN** 本地等待到期
- **THEN** 以等待超时或结果不确定结束，之后迟到响应不能复活已结束请求

### Requirement: RPC-03 能力预检不是安全授权
SDK SHALL 使用版本清单与服务端能力信息进行预检，保留明确受版本约束的未广播方法调用途径；发现列表缺省 MUST NOT 等价于绝对不存在，预检通过 MUST NOT 等价于被授权。

#### Scenario: RPC-03-S1 已知不可用
- **GIVEN** 版本清单明确目标版本不支持某方法
- **WHEN** 调用类型化入口
- **THEN** 在发送前返回不可用原因

#### Scenario: RPC-03-S2 未广播方法
- **GIVEN** 经评审的版本清单允许调用某未广播方法
- **WHEN** 宿主显式调用且服务端随后拒绝权限
- **THEN** 保留真实拒绝结果，不尝试提权或绕过

## Sources

[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
