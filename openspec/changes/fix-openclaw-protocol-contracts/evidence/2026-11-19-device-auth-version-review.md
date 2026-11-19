# OpenClaw 2026.11.5 设备握手范围复评

G1 的 AUTH-01 仍适用于 2026.11.5。候选版在 hello-ok 中增加了认证方法等元数据，设备 Token 缺省仍是正常成功路径。SDK 需要保留 Gateway 实际返回的认证结果和权限。凭据存储、轮换以及权限变更由后续批准的契约和验证决定。

## 真实 Gateway

使用两个 npm 发布版，在 Node 24.18.0 上分别启动只监听回环地址的 Gateway。每版使用独立配置、状态目录和临时 Token，关闭插件、发现、定时任务、Control UI。使用各版本自带的 GatewayClient，请求 `operator.read`。握手响应只记录字段是否存在。输入 Java 验证的样本用占位值替换凭据和连接 ID。

| Gateway | 无设备身份的 backend 握手 | 设备签名 CLI 握手 | `auth.method` |
| --- | --- | --- | --- |
| `2026.7.1-2` | 成功，无 `deviceToken` | 成功，有 `deviceToken` | 缺省 |
| `2026.11.5` | 成功，无 `deviceToken` | 成功，有 `deviceToken` | 均为 `token` |

四次握手均协商协议 4，返回 `snapshot`、`role=operator` 和 `scopes=[operator.read]`。这组连接全部只涉及 operator 角色，均未出现额外 `deviceTokens`。之前带设备签名的 backend 握手在两版同样成功，均未签发设备 Token。

## Java SDK 基线

将两版各两份脱敏 hello-ok 样本输入 SDK 使用的 Jackson 映射。三个 JDK 各执行 1 项测试，覆盖四份样本，均无失败、错误或跳过。SDK 保留协议、服务端版本、角色和 scopes，映射后的 `HelloOk` 缺少 `snapshot`、`deviceToken`、`issuedAtMs` 和候选版新增的 `auth.method`。

| JDK | SDK 提交 |
| --- | --- |
| 8 | `3dc46a0e58200b086efc59029a43aa5c136bf61b` |
| 17 | `9293a8cd2580435ec076b076d98c7965b77f0049` |
| 21 | `34b003b5c2c2158e536d3290ef67de7932ab78ef` |

候选版生成的 schema 还声明了可选的 `recoveryMigrationAllowed`、`recoveryScope`、`roleAllowsAdmin`、`sessionCap`。本次样本仅用于 AUTH-01 静态比较和真实握手观察。多角色 `deviceTokens`、宿主凭据保存和隔离、轮换与权限升级拒绝待真实验收。G0 版本锁定与 G1 实施范围待维护者批准。[上游协议 schema](https://github.com/openclaw/openclaw/tree/e13cf15ece9bbd969b7362927c315362c90c97ff/packages/gateway-protocol) · [版本及基础 Gateway 验证](../../establish-sdk-compatibility-governance/evidence/2026-11-18-live-gateway.md)
