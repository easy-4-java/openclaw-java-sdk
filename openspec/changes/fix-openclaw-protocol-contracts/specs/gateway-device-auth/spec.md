# 设备握手与凭据生命周期 — Spec Delta

## Purpose

为 openclaw-java-sdk 的设备握手与凭据生命周期定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: AUTH-01 握手结果完整且向前兼容
SDK SHALL 读取目标契约定义的 protocol、server、features、snapshot、policy、auth；设备 Token 缺省 SHALL 与认证失败区分，未知可选字段 SHALL 不破坏已知字段解析。

#### Scenario: AUTH-01-S1 签发设备凭据
- **GIVEN** 可信连接成功并返回 deviceToken 与 deviceTokens
- **WHEN** 解析 hello-ok
- **THEN** 保留 role、有效 scopes 和各凭据关联，向调用者提供完整结果

#### Scenario: AUTH-01-S2 不签发设备凭据
- **GIVEN** 允许的认证路径成功但不返回 deviceToken
- **WHEN** 解析 hello-ok
- **THEN** 连接仍可成功，不覆盖宿主已有合法凭据为空值

### Requirement: AUTH-02 凭据持久化由宿主控制
SDK SHALL 支持宿主控制的签名与凭据存储策略；持久凭据 SHALL 按 Gateway 身份、设备、角色分区并脱敏；不可信传输上的引导结果 MUST NOT 自动持久化，临时 bootstrap 密钥 MUST NOT 写入日志。

#### Scenario: AUTH-02-S1 可信轮换
- **GIVEN** 宿主配置了安全存储且 wss 握手签发新 Token
- **WHEN** 保存凭据
- **THEN** 原子更新对应分区，不改其他 Gateway 或角色的凭据

#### Scenario: AUTH-02-S2 不可信引导
- **GIVEN** 非可信远程明文传输返回引导凭据
- **WHEN** 触发自动保存路径
- **THEN** 拒绝持久化并报告原因，日志不出现密钥、签名或 Token

### Requirement: AUTH-03 握手和权限失败不自动提权
单次连接尝试 SHALL 只发送一个有效 connect 握手；设备身份要求、nonce 和权限 SHALL 服从目标版本；认证拒绝 MUST NOT 自动改用高权限共享密钥或删除授权要求。

#### Scenario: AUTH-03-S1 挑战竞争
- **GIVEN** 挑战事件与等待计时器同时触发
- **WHEN** SDK 发起握手
- **THEN** 只有一个请求关联和一次终态通知，旧挑战不能复用

#### Scenario: AUTH-03-S2 权限升级要求
- **GIVEN** 服务端拒绝请求并要求重新配对
- **WHEN** 连接失败
- **THEN** 保留结构化错误并停止自动提权；由宿主显式处理配对

## Sources

[O05 Gateway 握手与设备凭据](https://docs.openclaw.ai/gateway/protocol/handshake)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
