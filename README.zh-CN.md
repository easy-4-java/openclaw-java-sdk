# openclaw-java-sdk

[English](./README.md) | [简体中文](./README.zh-CN.md)

纯 Java SDK —— 通过 HTTP、SSE、WebSocket 与本地 CLI 等独立通道对接 OpenClaw Gateway

> **当前分支**：`feature/3.0.x`
> **版本**：`3.0.x.x.20260630-SNAPSHOT`
> **JDK 基线**：8
> **项目状态**：稳定（1.0.x 线）。尚未发布 Maven Central；制品通过 Aliyun Maven 仓库与 GitHub Releases 分发。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 能力与状态](#2-features--status)
- [3. 运行要求与兼容性](#3-requirements--compatibility)
- [4. 架构与模块](#4-architecture--modules)
- [5. 引入依赖](#5-installation)
- [6. 快速开始](#6-quick-start)
- [7. 配置](#7-configuration)
- [8. 核心用法](#8-core-usage)
- [9. 测试与构建](#9-testing--build)
- [10. 版本线与分支](#10-versioning--branches)
- [11. 贡献与许可证](#11-contributing--license)

## 1. 项目概述

### 1.1 是什么

**openclaw-java-sdk** 是面向 Java 开发者的纯 Java 库，通过 OpenClaw Gateway 的全部外部通信面进行对接：HTTP Chat Completions / Embeddings / Responses / Webhook / Tools Invoke、用于双向流式的 WebSocket 控制面，以及本地 `openclaw` CLI。

### 1.2 不是什么

- 不是 OpenClaw Gateway 本身，也不是 LLM 推理引擎。
- 无 Spring、Servlet 或 ORM 依赖。
- 不承诺未经兼容矩阵验证的 JDK 或框架组合。

### 1.3 典型使用场景

| 场景 | 推荐入口 | 结果 |
|---|---|---|
| 纯 Java 对话（阻塞） | `client.chatCompletion(...)` | OpenAI 兼容的 Chat Completions 响应 |
| 流式对话 / 工具调用 | `client.chatCompletionStream(request)` | 带增量 / 工具调用 / 完成回调的 SSE 流 |
| 嵌入向量生成 | `client.createEmbeddings(request)` | Embeddings 响应 |
| Webhook 触发 Agent | `client.hook(...)` / `client.wake(...)` | HTTP Webhook 调用 |
| 双向实时对话 | `client.connect()` + `client.chatSend(...)` | WebSocket 流式回复 |
| 本地 CLI 操作 | `client.cli().version()` / `gatewayHealth(...)` | 本地 `openclaw` 子命令执行 |

<a id="2-features--status"></a>
## 2. 能力与状态

| 能力 | 状态 | 说明 |
|---|:---:|---|
| HTTP Webhook | 可用 | `hook()` / `wake()`；自定义映射 Webhook |
| Chat Completions | 可用 | `chatCompletion(...)` 阻塞 + `chatCompletionStream(...)` SSE |
| Models | 可用 | `listModels()` |
| Embeddings | 可用 | `createEmbeddings(request)` |
| Responses | 可用 | `createResponse(request)` |
| Tools Invoke | 可用 | `toolInvoke(request)` / `toolsInvoke()` |
| WebSocket 控制面 | 可用 | `connect()` / `chatSend()` / `sessionsSend()`；帧协议位于 `ws.protocol` |
| 本地 CLI | 可用 | `cli()` 门面对 40+ 个 `openclaw` 子命令的类型化封装 |
| 启动自检 | 可用 | 构造时 HTTP 健康探测 + `openclaw --version` 探测（按子系统开关控制） |

<a id="3-requirements--compatibility"></a>
## 3. 运行要求与兼容性

| 组件 | 版本 | 说明 |
|---|---:|---|
| JDK | 21+ | 1.0.x 线基线 |
| Maven | 3.0+ | Enforcer 下限 |
| OkHttp / okhttp-sse | 4.12.0 | HTTP 与 SSE 传输 |
| Java-WebSocket | — | WebSocket 传输 |
| Jackson databind | 2.17.x | JSON |
| commons-exec | — | CLI 子进程执行 |
| SLF4J | 2.0.18 | 日志门面 |

版本线矩阵：

| 版本线 | 分支 | JDK | 版本模式 | 用途 |
|---|---|---:|---|---|
| 1.0.x | `feature/3.0.x`（当前分支） | 8 | `1.0.x.*` | 存量项目、Boot 2.x Starter 线 |
| 2.0.x | `feature/2.0.x` | 17 | `2.0.x.*` | 主流线（JDK 17） |
| 3.0.x | `feature/3.0.x` | 21 | `3.0.x.*` | 新项目 |

依赖边界：SDK 仅依赖 OkHttp、Jackson、Java-WebSocket、commons-exec 与 SLF4J。**无 Spring 依赖**——Spring Boot 应用请使用配套的 `openclaw-spring-boot-starter`。

<a id="4-architecture--modules"></a>
## 4. 架构与模块

```text
[ 业务应用 ]
        |
        | openclaw-java-sdk
        v
+------------------------------------------+
| OpenClawClient（门面）                    |
|  HTTP   /v1/chat/completions、/v1/models  |
|         /v1/embeddings、/v1/responses、   |
|         /tools/invoke、/hooks/*           |
|  SSE    StreamingChatResponse             |
|  WS     connect / chatSend / sessionsSend |
|  CLI    本地 `openclaw` 子进程             |
+------------------------------------------+
        |
        v
[ OpenClaw Gateway ] -> [ LLM / Agent / 工具 ]
```

包结构：

| 包 | 职责 |
|---|---|
| `io.github.easy4j.openclaw` | 门面 `OpenClawClient` 与配置类 |
| `io.github.easy4j.openclaw.api` | HTTP 子客户端（chat / embeddings / responses / webhook / tools） |
| `io.github.easy4j.openclaw.api.model` | DTO（`ChatRequest`、`ChatResponse`、`Tools`、`HookRequest` 等） |
| `io.github.easy4j.openclaw.api.sse` | SSE 流式（`StreamingChatResponse`、`SseStreamReader`、`SseEventAccumulator`） |
| `io.github.easy4j.openclaw.cli` | CLI 门面（`OpenClawCli` / `OpenClawCliExecutor`） |
| `io.github.easy4j.openclaw.cli.opts` | 60+ 个 CLI 子命令的类型化参数 |
| `io.github.easy4j.openclaw.cli.availability` | CLI 可用性探测 |
| `io.github.easy4j.openclaw.exception` | 异常层级 |
| `io.github.easy4j.openclaw.ws` | WebSocket 客户端与帧协议（params / results） |

<a id="5-installation"></a>
## 5. 引入依赖

Maven：

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>openclaw-java-sdk</artifactId>
    <version>3.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle：

```groovy
implementation 'io.github.easy4j:openclaw-java-sdk:3.0.x.x.20260630-SNAPSHOT'
```

快照版本需要启用对应快照仓库（`pom.xml` 中 `distributionManagement` 指向 Aliyun Maven 仓库）。

<a id="6-quick-start"></a>
## 6. 快速开始

```java
import io.github.easy4j.openclaw.OpenClawClient;
import io.github.easy4j.openclaw.OpenClawClientConfig;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatResponse;
import java.util.List;

// 1. 配置
OpenClawClientConfig config = new OpenClawClientConfig();
config.getHttp().setGatewayBaseUrl("http://localhost:18789");
config.getHttp().setGatewayAuthToken("your-gateway-token");

// 2. 创建客户端（构造时执行启动自检）
OpenClawClient client = new OpenClawClient(config);

// 3. 对话
ChatResponse resp = client.chatCompletion(
        "openclaw/default", "gpt-4o",
        List.of(ChatMessage.ofUser("你好")));
System.out.println(resp.getChoices().get(0).getMessage().getContent());

// 4. 释放
client.close();
```

**预期结果**：默认配置下 HTTP 子系统启用但 `startupCheckEnabled=false`，启动时不阻塞探测；调用返回 Agent 回复文本。启用启动自检后，INFO 日志出现 `OpenClaw HTTP health check passed: ...` / `OpenClaw CLI ready: ...`；探测失败时按配置抛 `IllegalStateException`（fail-fast）或仅输出 WARN（非 fail-fast）。

<a id="7-configuration"></a>
## 7. 配置

配置为对象式（本库无 Spring 配置属性）。三个配置类：

| 配置类 | 职责 |
|---|---|
| `OpenClawClientConfig` | 聚合 `http` + `cli` 子配置 |
| `OpenClawHttpClientConfig` | Gateway HTTP/WS 相关 |
| `OpenClawCliConfig` | 本地 CLI 相关 |

`OpenClawHttpClientConfig` 属性：

| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `enabled` | boolean | `true` | 是否启用 HTTP 子系统 |
| `startupCheckEnabled` | boolean | `false` | 启动时探测 Gateway 健康端点 |
| `failFastOnUnavailable` | boolean | `false` | 探测失败时中断构造 |
| `gatewayBaseUrl` | String | `http://localhost:18789` | Gateway 根地址 |
| `gatewayAuthToken` | String | — | 控制面令牌 |
| `gatewayAuthPassword` | String | — | 控制面密码模式 |
| `hooksToken` | String | — | Webhook 鉴权令牌 |
| `hooksPath` | String | `/hooks` | Webhook 基础路径 |
| `hooksUseXOpenclawTokenHeader` | boolean | `false` | 用 `x-openclaw-token` 头传 Hook 令牌 |
| `verifySsl` | boolean | `true` | 是否校验 HTTPS 证书 |
| `connectTimeoutMillis` | int | `15000` | 连接超时（毫秒） |
| `readTimeoutMillis` | int | `120000` | 读取超时（毫秒） |

`OpenClawCliConfig` 属性：

| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `enabled` | boolean | `true` | 是否启用 CLI 子系统 |
| `startupCheckEnabled` | boolean | `false` | 启动时探测 `openclaw --version` |
| `failFastOnUnavailable` | boolean | `false` | 探测失败时中断构造 |
| `executable` | String | `openclaw` | 可执行文件名或绝对路径 |
| `timeout` | int | `300` | CLI 命令超时（秒） |
| `probeTimeoutSeconds` | int | `5` | 启动探测超时（秒） |
| `workingDirectory` | String | — | 子进程工作目录 |
| `maxConcurrentExecutions` | int | `0` | 最大并发子进程数（0 = CPU 核心数） |

通过 `OpenClawHeaders.Builder` 设置自定义请求头：

| 头部 | 常量 | 用途 |
|---|---|---|
| `x-openclaw-model` | `X_OPENCLAW_MODEL` | 覆盖后端模型 |
| `x-openclaw-agent-id` | `X_OPENCLAW_AGENT_ID` | Agent 覆盖 |
| `x-openclaw-session-key` | `X_OPENCLAW_SESSION_KEY` | 显式会话路由 |
| `x-openclaw-message-channel` | `X_OPENCLAW_MESSAGE_CHANNEL` | 入口通道上下文 |
| `x-openclaw-scopes` | `X_OPENCLAW_SCOPES` | 权限范围声明 |

认证优先级：Webhook（`/hooks/*`）使用 `hooksToken`；控制面（`/v1/*`、`/tools/*`、WebSocket）为 `gatewayAuthToken` → `gatewayAuthPassword` → `hooksToken` → 空。

<a id="8-core-usage"></a>
## 8. 核心用法

### 8.1 流式对话与工具调用

```java
ChatRequest req = ChatRequest.builder()
        .agent("openclaw/default")
        .messages(List.of(ChatMessage.ofUser("北京天气怎么样？")))
        .tools(List.of(Tools.function("get_weather", "获取天气")
                .param("city", "string", "城市名", true).build()))
        .toolChoice("auto")
        .build();

client.chatCompletionStream(req)
        .onDelta(delta -> System.out.print(delta))
        .onToolCall(toolCalls -> toolCalls.forEach(
                tc -> System.out.println(tc.getFunction().getName())))
        .onComplete(text -> System.out.println("\n[done]"))
        .onError(Throwable::printStackTrace);
```

### 8.2 WebSocket 流式对话

```java
HelloOk hello = client.connect();
client.chatSend("你好", new ChatStreamHandler() {
    @Override public void onDelta(String text) { System.out.print(text); }
    @Override public void onComplete(String fullText) { System.out.println(); }
    @Override public void onError(String error) { System.err.println(error); }
});
```

### 8.3 本地 CLI

```java
OpenClawCliResult result = client.cli().version();        // openclaw --version
System.out.println(result.getStdout());

client.cli().gatewayHealth(                                // openclaw gateway health
        GatewayRpcOptions.builder().url("ws://127.0.0.1:18789").build());
```

<a id="9-testing--build"></a>
## 9. 测试与构建

```bash
mvn clean verify
```

- 单元测试覆盖配置默认值、CLI 参数与执行、WS 协议、HTTP 子客户端与构造器契约（`src/test` 下 23 个测试源文件：22 个测试类 + 1 个 mock CLI 辅助类）。
- JaCoCo 在 `verify` 阶段执行 `prepare-agent`、`report` 与 `check`，行覆盖率规则为 **90%**（`haltOnFailure=false`）。
- 发布打包（`mvn -Prelease deploy`）附带 sources 与 javadoc 构件并执行 GPG 签名，对接 Sonatype Central Publishing；普通 `mvn deploy` 按版本后缀路由到 Aliyun Maven 仓库（见 `distributionManagement`）。

<a id="10-versioning--branches"></a>
## 10. 版本线与分支

| 分支 | 版本模式 | JDK | 维护策略 |
|---|---|---|---|
| `feature/1.0.x`（当前分支） | `1.0.x.*` | 8 | 仅接受兼容性修复与 JDK 8 安全的依赖升级 |
| `feature/2.0.x` | `2.0.x.*` | 17 | 主流开发线 |
| `feature/3.0.x` | `3.0.x.*` | 21 | 新项目 |

<a id="11-contributing--license"></a>
## 11. 贡献与许可证

提交 Pull Request 前请执行 `mvn clean verify`，并说明兼容性、测试、文档与迁移影响。本项目采用 [Apache License 2.0](LICENSE) 许可证。
