# 渠道、设备和 Node 控制 — Spec Delta

## Purpose

为 openclaw-java-sdk 的渠道、设备和 Node 控制定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CN-01 渠道操作有明确账户作用域
SDK SHALL 保留渠道、账户、目标和线程身份，区分登录流程、发送受理与投递结果；二维码或临时凭据 SHALL 仅在明确请求的范围内处理。

#### Scenario: CN-01-S1 明确目标投递
- **GIVEN** 存在多个渠道账户
- **WHEN** 指定一个账户和线程发送
- **THEN** 只返回该目标的结果，不广播或换账户重试

#### Scenario: CN-01-S2 登录等待到期
- **GIVEN** web.login 等流程尚未完成
- **WHEN** 本地等待预算到期
- **THEN** 保持未完成或已过期状态，不宣告已登录，临时凭据不进入日志

### Requirement: CN-02 设备配对与凭据轮换受控
SDK SHALL 暴露经授权的设备和 Node 列表、配对决策、Token 轮换与撤销结果；支持范围 SHALL 由目标版本与权限决定，不调用已移除的配对协议。

#### Scenario: CN-02-S1 撤销设备 Token
- **GIVEN** 操作者具有对应权限
- **WHEN** 服务端确认撤销
- **THEN** 保留操作结果并使关联本地认证状态可失效

#### Scenario: CN-02-S2 操作需要额外授权
- **GIVEN** 当前权限不足以配对或轮换
- **WHEN** 请求失败
- **THEN** 返回授权要求，不切换成共享管理员凭据

### Requirement: CN-03 Node 调用与回传按身份关联
SDK SHALL 区分 operator 发起 Node 调用与 node 角色接收执行请求，保留 command、requestId、runId、session 身份及 deadline；Node 回传不得串用另一调用的关联信息。

#### Scenario: CN-03-S1 合法调用回传
- **GIVEN** 已认证 Node 获得允许命令的准确请求
- **WHEN** 回传结果
- **THEN** 使用原请求身份且保留退出、拒绝或超时结果

#### Scenario: CN-03-S2 执行状态未知
- **GIVEN** 请求已经可能运行但连接断开
- **WHEN** 宿主处理失败
- **THEN** 报告不确定，不套用未执行拒绝才可重试的规则重复执行

## Sources

[O12 系统、渠道、插件与终端 RPC](https://docs.openclaw.ai/gateway/protocol/rpc-system-and-channels)；[O08 设备、Node、审批与 Cron RPC](https://docs.openclaw.ai/gateway/protocol/rpc-devices-nodes-and-approvals)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
