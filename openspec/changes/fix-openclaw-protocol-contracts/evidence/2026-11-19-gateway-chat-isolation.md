# Gateway 聊天串流隔离测试（2026-11-19）

对应 [CHAT-02-S1 并发隔离](../specs/gateway-chat-events/spec.md#scenario-chat-02-s1-并发隔离)，以及 [GOV-02-S1 三条维护线的终态一致性](../../establish-sdk-compatibility-governance/specs/sdk-compatibility-governance/spec.md#scenario-gov-02-s1-依赖适配差异)。

实测使用官方 Gateway `2026.7.1-2` 与 `2026.11.5`。每次单独起一个 Gateway，绑定回环地址，使用独立配置、状态目录和临时凭据，关闭插件。模型后端为本机 OpenAI 兼容的确定性 SSE 样本，返回三个固定文本片段和正常终止标记。每次执行记录了 3 次本地模型请求，均为 `/v1/chat/completions`，`stream=true`。

| SDK 源码提交 | JDK | 锁定版串行 / 并行 | 候选版串行 / 并行 |
|---|---|---|---|
| `8cfbeb880a6c6ae5390fab0f9fe65e06938907be` | 1.8.0_504 | 通过 / 失败 | 通过 / 失败 |
| `48309d7bc34c4298cdfa1c52db06114b2e7e9f17` | 17.0.21 | 通过 / 失败 | 通过 / 失败 |
| `87c21133aba6364f64ed0edbefab2d821564ede0` | 21.0.13.0.2 | 通过 / 失败 | 通过 / 失败 |

串行对照的 6 项全部通过。并行对照的 6 项均在最终文本断言处失败，所有执行均无跳过。每组都收到终态事件。结果限制为临时测试环境的合成输入，G1 的运行身份隔离和完整终态验收仍待实施。

验证命令为 `mvn -B -ntp -Dtest=GatewayStreamingLiveFixtureTest test`。Java 21 使用 4.0.0-rc-5 Wrapper，Java 8 和 17 使用系统 Maven 3.9.16。每次执行保存了独立的构建结果及模型请求计数。

## 候选补丁复核

旧实现会把未登记的运行事件送给任意活动流。候选补丁只将字符串 `runId` 精确匹配到本客户端登记的流。Gateway 在 RPC 应答里返回替代运行 ID 时仍可正常接收增量和终态。未知和缺失 ID 的帧仍能由通用事件监听器观察。

| 测试基线 | JDK | WebSocket 回归 | Gateway 2026.7.1-2 并发 | Gateway 2026.11.5 并发 | Maven `clean verify` |
| --- | --- | --- | --- | --- | --- |
| `3dc46a0e58200b086efc59029a43aa5c136bf61b` | 8 | 6 通过 | 旧代码失败，补丁通过 | 补丁通过 | 152 通过，3 跳过 |
| `9293a8cd2580435ec076b076d98c7965b77f0049` | 17 | 6 通过 | 旧代码失败，补丁通过 | 补丁通过 | 152 通过，3 跳过 |
| `34b003b5c2c2158e536d3290ef67de7932ab78ef` | 21 | 6 通过 | 旧代码失败，补丁通过 | 补丁通过 | 152 通过，3 跳过 |

三条线的 WebSocket 夹具在修改生产源码前均复现 `foreignunidentifiedmodern-event`，应用补丁后均返回 `modern-event`。真实 Gateway 夹具用本地模型端点将三个请求同步到同一起点。每个 SDK 都通过通用事件监听器确认在当前流结束前有其他运行的增量，再断言自己的增量、终态文本和完成次数。两轮补丁验证均记录 3 个本地模型请求。六项真实测试全部通过，无跳过。旧代码在锁定版 Gateway 的同一夹具上把三条流的文本拼到一起。常规构建的 3 个跳过项来自需要主动启用的 `openclaw.realIntegration` 夹具。

### 复现

1. 在临时目录分别准备上表三个源码提交，命名为 `sdk-1`、`sdk-2`、`sdk-3`。应用[候选补丁](fixtures/strict-run-id-review.patch)后运行 `mvn -Dtest=OpenClawGatewayWsClientIntegrationTest test`。Java 21 使用 `./mvnw`。
2. 运行真实联调前，将 [GatewayStreamingLiveFixtureTest.java](fixtures/GatewayStreamingLiveFixtureTest.java) 复制到三个源码树各自的 `src/test/java/io/github/easy4j/openclaw/ws/`。在临时目录按版本准备官方 npm 构建，分别放在 `locked-runtime/node_modules/openclaw` 和 `runtime/node_modules/openclaw`。macOS 启动器使用 `java_home` 选取 JDK，从同级 `m2-1`、`m2-2`、`m2-3` 的已预热 Maven 缓存离线构建。
3. 将[启动器](fixtures/run-gateway-streaming.py)复制到同一临时目录，分别运行 `python3 run-gateway-streaming.py locked` 和 `python3 run-gateway-streaming.py candidate`。它会启动独立的回环 Gateway 和本地模型夹具，并写出三条版本线的测试日志。

候选补丁和测试证据供评审，SDK 分支仍指向表中的基线。该结果覆盖 CHAT-02-S1。版本确认、人审、会话代际和其他 G1 场景沿用现有验收项。
