# 锁定版 Gateway 协议 schema（2026-11-18）

`v2026.7.1-2` 对应提交 `0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c`。该版本将 `@openclaw/gateway-protocol` 定义为私有工作区包，npm 注册表没有同版本制品。使用该提交的官方生成器和原始协议定义生成了 Draft-07 JSON Schema。[上游包定义](https://github.com/openclaw/openclaw/blob/0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c/packages/gateway-protocol/package.json) · [上游生成器](https://github.com/openclaw/openclaw/blob/0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c/scripts/protocol-gen.ts)

| 检查 | 结果 |
|---|---|
| 生成环境 | Node 24.18.0、tsx 4.22.4、typebox 1.3.3 |
| 产物 | `dist/protocol.schema.json`，714085 字节，322 项定义 |
| SHA-256 | `0c5758a9750691b6fb070c6344297bce6ddeba89afd715e45531a9508a168699` |
| 可复现性 | 连续执行两次生成器，结果逐字节一致。所有本地 definition 引用均可解析。 |
| ConnectParams | `gateway-client/backend` 通过校验。默认分支的 `openclaw-java-sdk/operator` 在两个枚举字段上均被拒绝。 |

## 复现

将以下命令放在独立临时目录中执行。安装只用于运行锁定版生成器的依赖，源码与 SDK 工作区保持原状。

```bash
git clone --depth 1 --filter=blob:none --sparse --branch v2026.7.1-2 https://github.com/openclaw/openclaw.git source
git -C source sparse-checkout set --no-cone /packages/gateway-protocol/ /scripts/protocol-gen.ts /package.json
test "$(git -C source rev-parse HEAD)" = 0790d9f593ad30c940ed93b5872a8cf6d6f3cf8c
npm install --prefix tooling --ignore-scripts --no-audit --no-fund tsx@4.22.4 typebox@1.3.3
ln -s "$(pwd)/tooling/node_modules" source/node_modules
(cd source && node --import tsx scripts/protocol-gen.ts)
shasum -a 256 source/dist/protocol.schema.json
```

使用锁定版 Gateway 安装包中的 Ajv 校验 `ConnectParams`。失败路径与此前运行中的 Gateway 拒绝结果一致。离线校验只覆盖字段结构和枚举，认证、服务器协商协议版本以及授权结果见 [真实 Gateway 基线验证](2026-11-18-live-gateway.md)。本记录不改变待评审的目标版本。
