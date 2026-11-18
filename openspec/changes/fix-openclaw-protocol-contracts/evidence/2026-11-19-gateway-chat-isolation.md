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
