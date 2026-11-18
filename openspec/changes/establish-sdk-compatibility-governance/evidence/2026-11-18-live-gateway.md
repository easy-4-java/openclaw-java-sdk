# G0 真实 Gateway 基线验证（2026-11-18）

两次 Gateway 启动都只监听回环地址，使用独立状态目录、临时 Token，并关闭插件。SDK 测试仅执行握手和会话列表查询。

| Gateway | SDK 维护线 | 源码 SHA | JDK | 验证 |
|---|---|---|---|---|
| 2026.7.1-2 | feature/1.0.x | `8cfbeb880a6c6ae5390fab0f9fe65e06938907be` | 1.8.0_504 | 3 项通过 |
| 2026.7.1-2 | feature/2.0.x | `48309d7bc34c4298cdfa1c52db06114b2e7e9f17` | 17.0.21 | 3 项通过 |
| 2026.7.1-2 | feature/3.0.x | `87c21133aba6364f64ed0edbefab2d821564ede0` | 21.0.13.0.2 | 3 项通过 |
| 2026.11.5 | feature/1.0.x | `8cfbeb880a6c6ae5390fab0f9fe65e06938907be` | 1.8.0_504 | 3 项通过 |
| 2026.11.5 | feature/2.0.x | `48309d7bc34c4298cdfa1c52db06114b2e7e9f17` | 17.0.21 | 3 项通过 |
| 2026.11.5 | feature/3.0.x | `87c21133aba6364f64ed0edbefab2d821564ede0` | 21.0.13.0.2 | 3 项通过 |

每组验证包括正确 Token 握手并协商协议 4、错误 Token 被 Gateway 拒绝、使用 operator.read 读取会话列表。JDK 8 和 17 通过系统 Maven 执行，JDK 21 使用仓库 Wrapper。

## 追加权限与默认分支验证

同一有效 Token，在三条维护线均可建立 WebSocket 握手。客户端请求空权限集合时，两版 Gateway 均拒绝 `sessions.list`，SDK 收到结构化错误 `missing scope: operator.read`。此前用读取权限执行的正向查询均成功。

默认分支 `ee0474ea597551a613291134858dbc83b815b20e` 使用有效 Token 发起握手，两版 Gateway 均返回 `invalid connect params`。服务端记录显示 `client.id` 校验失败，锁定版本的首次探测还报告了 `client.mode`。旧分支源码声明协议 1。实际握手在元数据校验阶段被拒绝，本次运行未到达协议协商阶段。

追加验证共 8 项，零失败、零跳过。两次默认分支测试以验证拒绝为预期结果。真实模型调用和 G0/G1 范围审批仍待验收。

## 复核方法

使用官方 npm 发布包 `openclaw@2026.7.1-2` 和 `openclaw@2026.11.5`，以 Node 24.18.0 分别启动 `openclaw gateway run`。确认两次 `GET /readyz` 均返回 `ready: true` 后，将 SDK WebSocket URI 指向对应的本地端口。表中的 SHA 对应独立检出目录的源码。

每条维护线使用 `OpenClawGatewayWsClient.connectHandshake()` 检查服务端返回的协议号，并调用 `sessionsList()`。认证失败由 Gateway 的 Token 不匹配记录确认。权限不足由 SDK 的 `OpenClawWsRpcException` 确认，异常保留方法名 `sessions.list` 和服务端的结构化错误。报告省略临时 Token 和连接标识。

锁定版本的基础运行检查满足 G0 任务 3.1。候选版本的结果用于后续版本选择评审。默认分支的兼容缺口计入后续 G1 握手工作。此检查未调用模型，版本与范围审批仍待人审。

## 维护线文档一致性

三条维护线分别比较 POM、Wrapper 配置、中英文 README 与 GitHub Release 说明。两份 README 的 Maven 和 Gradle 示例都使用该维护线 POM 中的快照版本。GitHub Release 说明使用同一版本号的非快照标签，说明原有版本的维护线，未附带 Maven 制品或 Gateway 兼容性测试结果。

| 维护线 | POM 快照版本 | JDK / Wrapper | Jackson databind | 文档提交 | 测试分支 CI |
|---|---|---|---|---|---|
| feature/1.0.x | 1.0.x.20260630-SNAPSHOT | 8 / 3.9.16 | com.fasterxml.jackson.core 2.18.9 | 3dc46a0e58200b086efc59029a43aa5c136bf61b | [成功](https://github.com/easy-4-java/openclaw-java-sdk/actions/runs/44335946637) |
| feature/2.0.x | 2.0.x.20260630-SNAPSHOT | 17 / 3.9.16 | com.fasterxml.jackson.core 2.22.1 | 9293a8cd2580435ec076b076d98c7965b77f0049 | [成功](https://github.com/easy-4-java/openclaw-java-sdk/actions/runs/44335947125) |
| feature/3.0.x | 3.0.x.20260630-SNAPSHOT | 21 / 4.0.0-rc-5 | tools.jackson.core 3.2.1 | 34b003b5c2c2158e536d3290ef67de7932ab78ef | [成功](https://github.com/easy-4-java/openclaw-java-sdk/actions/runs/44335951638) |

三个提交仅修改各自的两份 README，已推送到对应的维护线。六份 README 均通过快照坐标、依赖版本、JDK、Wrapper、Release 标签和验证范围核对。文档明确记录实测的协议 4 握手、Token 认证和会话列表的读取权限检查，并标明 HTTP、SSE、CLI、模型调用及其余 RPC 的真实兼容性待验证。
