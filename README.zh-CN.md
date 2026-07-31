<a id="readme-top"></a>

<div align="center">

# openclaw-java-sdk

**纯 Java SDK —— 通过五条独立通信通道对接 OpenClaw Gateway**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.easy4j/openclaw-java-sdk)](https://github.com/easy-4-java/openclaw-java-sdk)
[![Java](https://img.shields.io/badge/Java-17-orange)](#3-运行要求与兼容性)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](LICENSE)

[English](./README.md) · [简体中文](./README.zh-CN.md)

[定位](#1-项目定位) · [架构](#4-架构与模块) · [引入依赖](#5-引入依赖) ·
[快速开始](#6-快速开始) · [配置](#8-配置参考) · [测试](#14-构建与测试) ·
[版本](#15-版本线与兼容策略) · [贡献](#19-贡献与许可证)

</div>

---

> **当前版本**：`2.0.x.20260630-SNAPSHOT`<br>
> **JDK 基线**：`17`<br>
> **构建工具**：Maven `3.0+`<br>
> **项目状态**：稳定<br>
> **最后核验**：2026-07-31

## 1. 项目定位

### 1.1 是什么

**openclaw-java-sdk 是一个面向 Java 开发者的纯 Java 库，用于对接 OpenClaw Gateway 的全部外部通信能力。**

| 维度 | 定位 |
|---|---|
| 本质 | 纯 Java SDK（无 Spring 依赖） |
| 消费方 | Java 应用、Spring Boot Starter、微服务 |
| 核心能力 | HTTP Webhook、Chat Completions、Embeddings、Responses、Tools Invoke、WebSocket 控制面、本地 CLI |
| JDK | `17`（feature/2.0.x）；另有 `1.8`（feature/1.0.x）和 `21`（feature/3.0.x） |
| 坐标 | `io.github.easy4j:openclaw-java-sdk:2.0.x.20260630-SNAPSHOT` |

### 1.2 不是什么

- 不是 OpenClaw Gateway 本身，也不是 LLM 推理引擎。
- 不包含 Spring、Servlet、ORM 或其他框架依赖。
- 不承诺未经兼容矩阵验证的 JDK 或框架组合。

### 1.3 典型使用场景

| 场景 | 推荐入口 | 结果 |
|---|---|---|
| 纯 Java 对话 / 流式 | `OpenClawClient.chatCompletion()` | OpenAI 兼容的 Chat Completions 响应 |
| 嵌入向量生成 | `OpenClawClient.createEmbeddings()` | Embeddings 响应 |
| Webhook 触发 Agent | `OpenClawClient.hook()` / `wake()` | HTTP Webhook 调用 |
| 双向实时流式 | `OpenClawClient.connect()` + `chatSend()` | WebSocket 流式回复 |
| 本地 CLI 操作 | `OpenClawClient.cli().version()` | 本地 `openclaw` 子命令执行 |

## 2. 核心能力与状态

| 能力 | 状态 | 入口 | 说明 |
|---|:---:|---|---|
| HTTP Webhook | ✅ | `hook()` / `wake()` | 触发 Agent、注入事件、自定义映射 Webhook |
| Chat Completions | ✅ | `chatCompletion()` / `chatCompletionStream()` | 非流式 + 流式 SSE |
| Models | ✅ | `listModels()` | 获取可用模型/Agent 目标列表 |
| Embeddings | ✅ | `createEmbeddings()` | 创建嵌入向量 |
| Responses | ✅ | `createResponse()` | OpenResponses API（Item-based 输入） |
| Tools Invoke | ✅ | `toolInvoke()` | 直接调用单个工具 |
| WebSocket 控制面 | ✅ | `connect()` / `chatSend()` / `sessionsSend()` | 双向实时通信、流式对话、完整 RPC |
| 本地 CLI | ✅ | `cli().version()` / `agent()` / `gateway()` 等 | 本地 `openclaw` 命令封装（覆盖 40+ 子命令） |
| 启动自检 | ✅ | 构造时自动执行 | HTTP `/v1/models` + CLI `openclaw --version` |

## 3. 运行要求与兼容性

### 3.1 基础要求

| 依赖 | 最低版本 | 推荐版本 | 证据来源 |
|---|---:|---:|---|
| JDK | `17` | `17` | `pom.xml` Enforcer |
| Maven | `3.0` | `3.9+` | Maven Enforcer |

### 3.2 版本兼容矩阵

| 项目版本线 | JDK | 状态 | 维护策略 |
|---|---:|:---:|---|
| `feature/1.0.x` | `1.8` | 🛠️ | 兼容老项目、Spring Boot 2.x starter |
| `feature/2.0.x`（默认分支） | `17` | ✅ | 活跃开发 |
| `feature/3.0.x` | `21` | 🧪 | 新项目、Spring Boot 4.x starter |

### 3.3 依赖边界

- SDK 仅依赖 OkHttp（HTTP 客户端）、Jackson（JSON）、Java-WebSocket（WS）、commons-exec（CLI 子进程）、SLF4J（日志门面）、Lombok。
- **无 Spring 依赖**：Spring Boot 应用请使用配套的 `openclaw-spring-boot-starter`。

## 4. 架构与模块

### 4.1 一眼看懂

```text
[业务应用]
     │ 引入 SDK
     ▼
┌──────────────────────────────────────────────────────────────┐
│  OpenClawClient（门面）                                      │
│  ├── HTTP Webhook  /hooks/*                                  │
│  ├── Chat Completions  /v1/chat/completions                  │
│  ├── Models  /v1/models                                      │
│  ├── Embeddings  /v1/embeddings                              │
│  ├── Responses  /v1/responses                                │
│  ├── Tools Invoke  /tools/invoke                             │
│  ├── WebSocket 控制面（双向 RPC + 流式）                      │
│  └── CLI（本地 openclaw 子进程）                              │
└──────────────────────────────────────────────────────────────┘
     │
     ▼
[OpenClaw Gateway]  ──→  [LLM / Agent / 工具]
```

### 4.2 包结构

| 包 | 职责 |
|---|---|
| `io.github.easy4j.openclaw` | 门面 `OpenClawClient`、配置类 |
| `io.github.easy4j.openclaw.api` | HTTP 子客户端（Chat / Embeddings / Responses / Webhook / Tools Invoke） |
| `io.github.easy4j.openclaw.api.model` | DTO（ChatRequest / ChatResponse / Tools / HookRequest 等） |
| `io.github.easy4j.openclaw.api.sse` | SSE 流式响应（`StreamingChatResponse`、`SseStreamReader`） |
| `io.github.easy4j.openclaw.cli` | 本地 CLI 封装（`OpenClawCli` / `OpenClawCliExecutor`） |
| `io.github.easy4j.openclaw.cli.opts` | CLI 子命令类型化参数（60+ Options 类） |
| `io.github.easy4j.openclaw.cli.availability` | CLI 可用性探测 |
| `io.github.easy4j.openclaw.exception` | 异常层级 |
| `io.github.easy4j.openclaw.util` | 工具类 |
| `io.github.easy4j.openclaw.ws` | WebSocket 客户端 |
| `io.github.easy4j.openclaw.ws.protocol` | WS 帧结构与 RPC params/result |

## 5. 引入依赖

### 5.1 Maven

```xml
<dependency>
  <groupId>io.github.easy4j</groupId>
  <artifactId>openclaw-java-sdk</artifactId>
  <version>2.0.x.20260630-SNAPSHOT</version>
</dependency>
```

### 5.2 仓库配置

```xml
<repository>
  <id>aliyun-snapshot</id>
  <url>https://packages.aliyun.com/maven/repository/2624322-snapshot-3eoov3</url>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

## 6. 快速开始

```java
import io.github.easy4j.openclaw.*;
import io.github.easy4j.openclaw.api.model.*;
import java.util.List;

// 1. 配置
OpenClawClientConfig config = new OpenClawClientConfig();
config.getHttp().setGatewayBaseUrl("http://localhost:18789");
config.getHttp().setGatewayAuthToken("your-gateway-token");

// 2. 创建客户端（构造时自动执行启动自检）
OpenClawClient client = new OpenClawClient(config);

// 3. 调用
ChatResponse resp = client.chatCompletion(
    "openclaw/default", "gpt-4o",
    List.of(ChatMessage.ofUser("你好"))
);
System.out.println(resp.getChoices().get(0).getMessage().getContent());

// 4. 释放
client.close();
```

**预期结果**：构造时 INFO 日志 `OpenClaw HTTP health check passed` + `OpenClaw CLI ready`；调用返回 Agent 回复文本。

## 7. Starter 与自动装配

Spring Boot 应用请使用配套 Starter，无需手动构造 `OpenClawClient`：

```xml
<dependency>
  <groupId>io.github.easy4j</groupId>
  <artifactId>openclaw-spring-boot-starter</artifactId>
  <version>2.7.x.20260630-SNAPSHOT</version>
</dependency>
```

```yaml
openclaw:
  http:
    gateway-base-url: http://localhost:18789
    gateway-auth-token: ${OPENCLAW_GATEWAY_TOKEN}
  cli:
    executable: openclaw
```

Starter 自动装配 `OpenClawClient`、`OkHttpClient`、`ObjectMapper`、`OpenClawCliExecutor` 等 Bean，启动自检由 SDK 构造器统一管理。

## 8. 配置参考

### 8.1 配置对象

| 配置类 | 前缀（Starter） | 职责 |
|---|---|---|
| `OpenClawHttpClientConfig` | `openclaw.http` | Gateway HTTP/WS 相关 |
| `OpenClawCliConfig` | `openclaw.cli` | 本地 CLI 相关 |
| `OpenClawClientConfig` | `openclaw` | 聚合以上两者 |

### 8.2 HTTP 子系统配置（`OpenClawHttpClientConfig`）

| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `enabled` | boolean | `true` | 是否启用 HTTP 子系统 |
| `startupCheckEnabled` | boolean | `true` | 启动时探测 `/v1/models` |
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

### 8.3 CLI 子系统配置（`OpenClawCliConfig`）

| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `enabled` | boolean | `true` | 是否启用 CLI 子系统 |
| `startupCheckEnabled` | boolean | `true` | 启动时探测 `openclaw --version` |
| `failFastOnUnavailable` | boolean | `false` | 探测失败时中断构造 |
| `executable` | String | `openclaw` | 可执行文件名或绝对路径 |
| `timeout` | int | `300` | CLI 命令超时（秒） |
| `probeTimeoutSeconds` | int | `5` | 启动探测超时（秒） |
| `workingDirectory` | String | — | 子进程工作目录 |
| `maxConcurrentExecutions` | int | `0` | 最大并发子进程数（0 = CPU 核心数） |

### 8.4 认证优先级

**Webhook（`/hooks/*`）**：`hooksToken` → 空

**控制面（`/v1/*`、`/tools/*`、WebSocket）**：`gatewayAuthToken` → `gatewayAuthPassword` → `hooksToken` → 空

### 8.5 自定义请求头

通过 `OpenClawHeaders` 构建器设置 `x-openclaw-*` 头：

| 头部 | 常量 | 用途 |
|---|---|---|
| `x-openclaw-model` | `HEADER_X_OPENCLAW_MODEL` | 覆盖后端模型 |
| `x-openclaw-agent-id` | `HEADER_X_OPENCLAW_AGENT_ID` | Agent 覆盖 |
| `x-openclaw-session-key` | `HEADER_X_OPENCLAW_SESSION_KEY` | 显式会话路由 |
| `x-openclaw-message-channel` | `HEADER_X_OPENCLAW_MESSAGE_CHANNEL` | 入口通道上下文 |
| `x-openclaw-scopes` | `HEADER_X_OPENCLAW_SCOPES` | 权限范围声明 |

## 9. 核心用法

### 9.1 Chat Completions（非流式）

```java
ChatResponse resp = client.chatCompletion(
    "openclaw/default", "gpt-4o",
    List.of(ChatMessage.ofUser("你好"))
);
String answer = resp.getChoices().get(0).getMessage().getContent();
```

### 9.2 流式 Chat Completions

```java
ChatRequest req = ChatRequest.builder()
    .agent("openclaw/default")
    .model("gpt-4o")
    .messages(List.of(ChatMessage.ofUser("写一首诗")))
    .build();

StreamingChatResponse stream = client.chatCompletionStream(req);
stream.onDelta(delta -> System.out.print(delta))
      .onComplete(text -> System.out.println("\n完成"))
      .onError(error -> error.printStackTrace());
```

### 9.3 流式工具调用

```java
List<Map<String, Object>> tools = List.of(
    Tools.function("get_weather", "获取天气")
        .param("city", "string", "城市", true)
        .build()
);

ChatRequest req = ChatRequest.builder()
    .agent("openclaw/default")
    .messages(List.of(ChatMessage.ofUser("北京天气")))
    .tools(tools)
    .toolChoice("auto")
    .build();

client.chatCompletionStream(req)
    .onToolCall(toolCalls -> {
        for (ChatMessage.ToolCall tc : toolCalls) {
            System.out.println(tc.getFunction().getName());
        }
    });
```

### 9.4 Embeddings

```java
EmbeddingsResponse resp = client.createEmbeddings(
    EmbeddingsRequest.builder()
        .agent("openclaw/default")
        .model("text-embedding-3-small")
        .input(List.of("hello", "world"))
        .build()
);
```

### 9.5 WebSocket 流式对话

```java
HelloOk hello = client.connect();
client.chatSend("你好", new ChatStreamHandler() {
    @Override public void onDelta(String text) { System.out.print(text); }
    @Override public void onComplete(String fullText) { System.out.println(); }
    @Override public void onError(String error) { System.err.println(error); }
});
```

### 9.6 Webhook

```java
// 一次性调用
client.agentOneShot(InvokeAgentRequest.builder().message("总结任务").build());

// 注入系统事件
client.wake("3点有会议", "now");

// 自定义映射 webhook
client.hook("my-webhook", Map.of("key", "value"));
```

### 9.7 本地 CLI

```java
// openclaw --version
OpenClawCliResult result = client.cli().version();
System.out.println(result.getStdout());

// openclaw gateway health
client.cli().gatewayHealth(
    GatewayRpcOptions.builder().url("ws://127.0.0.1:18789").build()
);
```

### 9.8 `agent` 与 `model` 字段约定

| 字段 | 含义 | 示例 | 走向 |
|---|---|---|---|
| `agent` | Agent 目标路由 | `"openclaw/default"` | HTTP body 的 `model`（用于路由） |
| `model`（非 Agent 路由时） | 后端 LLM | `"gpt-4o"` | `x-openclaw-model` header（用于覆盖） |

### 9.9 会话行为

默认每次请求无状态。复用会话：

```java
// 通过 user 字段派生稳定 session key
client.chatCompletion("openclaw/default", "gpt-4o", "conv:my-id",
    List.of(ChatMessage.ofUser("继续讨论")));
```

## 10. 公共 API 与 SPI

| 类型 | 稳定性 | 用途 |
|---|:---:|---|
| `OpenClawClient` | 稳定 | 门面入口，所有调用的唯一入口 |
| `OpenClawClientConfig` / `OpenClawHttpClientConfig` / `OpenClawCliConfig` | 稳定 | 配置 |
| `OpenClawHeaders.Builder` | 稳定 | 自定义请求头 |
| `Tools` | 稳定 | 工具定义与结果构建 |
| `StreamingChatResponse` | 稳定 | 流式响应回调 |
| `OpenClawCli` | 稳定 | CLI 子命令分发 |
| `OpenClawCliAvailabilityChecker` | 稳定 | CLI 可用性探测 |
| `HealthStatus` | 实验 | Gateway 健康响应（未来切换 `/healthz` 时使用） |

## 11. 启动自检

`OpenClawClient` 主构造器在初始化后按子配置自动执行启动自检：

| 子系统 | 探测方式 | 失败行为 |
|---|---|---|
| HTTP | `GET /v1/models` | `failFast=true` 抛 `IllegalStateException`；`false` 仅 WARN |
| CLI | `openclaw --version` | 同上 |

- `gatewayBaseUrl` 为空时 HTTP 检查自动跳过。
- `enabled=false` 时子系统完全不创建，检查也跳过。
- 可通过 `startupCheckEnabled=false` 或 `failFastOnUnavailable=false` 控制行为。

## 12. 安全与可观测性

### 12.1 安全基线

- Webhook 令牌与控制面令牌分离，不混用。
- `verifySsl=false` 仅建议开发环境。
- 日志使用 SLF4J，敏感字段（令牌）不会出现在日志中。

### 12.2 观测信号

| 信号 | 字段 | 用途 |
|---|---|---|
| 日志 | `OpenClawClient` 内各子客户端 DEBUG 级别日志 | 故障定位 |
| 启动自检 | INFO/WARN 日志 | 启动期健康状态 |

## 13. 项目结构

```text
openclaw-java-sdk/
├── src/main/java/io/github/easy4j/openclaw/
│   ├── OpenClawClient.java            # 门面
│   ├── OpenClawClientConfig.java      # 聚合配置
│   ├── OpenClawHttpClientConfig.java  # HTTP/WS 配置
│   ├── OpenClawCliConfig.java         # CLI 配置
│   ├── api/                           # HTTP 子客户端 + 模型 + SSE
│   ├── cli/                           # CLI 封装 + opts + availability
│   ├── exception/                     # 异常层级
│   ├── util/                          # 工具类
│   └── ws/                            # WebSocket 客户端 + 协议
├── src/test/java/                     # 单元测试（102 个）
├── pom.xml
└── README.md
```

## 14. 构建与测试

### 14.1 常用命令

```bash
# 编译
JAVA_HOME=/path/to/jdk17 mvn clean compile

# 测试
JAVA_HOME=/path/to/jdk17 mvn test

# 打包
JAVA_HOME=/path/to/jdk17 mvn clean package

# 安装到本地仓库
JAVA_HOME=/path/to/jdk17 mvn install -DskipTests

# 发布快照
JAVA_HOME=/path/to/jdk17 mvn clean deploy -DskipTests
```

### 14.2 测试矩阵

| 类型 | 覆盖 | 数量 |
|---|---|---|
| 单元测试 | 配置、CLI 参数、CLI 执行、WS 协议、HTTP 客户端、构造器契约 | 102 |
| 构造器契约测试 | `enabled` 短路、`requireNonNull`、自动 mapper/client | 7 |
| 配置默认值测试 | 三件套默认值 | 2 |

## 15. 版本线与兼容策略

| 分支 | 版本 | JDK | 用途 |
|---|---|---:|---|
| `feature/1.0.x` | `1.0.x.20260630-SNAPSHOT` | `1.8` | 兼容老项目、Spring Boot 2.x starter |
| `feature/2.0.x`（默认） | `2.0.x.20260630-SNAPSHOT` | `17` | 主流线 |
| `feature/3.0.x` | `3.0.x.20260630-SNAPSHOT` | `21` | 新项目 |

## 16. 文档、示例与排障

| 症状 | 诊断 | 解决 |
|---|---|---|
| 构造时 `IllegalStateException` | 启动自检 fail-fast | 检查 Gateway 是否可达 / CLI 是否安装 |
| 构造时 NPE | `enabled=false` 但调用了禁用的子客户端 | 检查 `isHttpEnabled()` / `isCliEnabled()` |
| 认证失败 | 令牌配置错误 | 检查 `gatewayAuthToken` / `hooksToken` |
| `openclaw` not found | CLI 未安装 | 设置 `cli.executable` 为绝对路径或安装 CLI |

## 17. 发布与部署

```bash
# 在对应分支下，使用对应 JDK
mvn clean deploy -DskipTests
```

发布到阿里云 Maven 仓库。详见 `pom.xml` 中 `<distributionManagement>` 配置。

## 18. 参考文档

| 资源 | 内容 |
|---|---|
| [OpenClaw Gateway 文档](https://docs.openclaw.ai) | Gateway 安装、配置、API |
| [OpenAI Chat Completions API](https://docs.openclaw.ai/gateway/openai-http-api) | Chat Completions 对接 |
| [Gateway Protocol](https://docs.openclaw.ai/gateway/protocol) | WebSocket 协议 |
| [CLI Reference](https://docs.openclaw.ai/cli) | CLI 子命令参考 |
| [Webhook 文档](https://docs.openclaw.ai/automation/webhook) | Webhook 自动化 |

## 19. 贡献与许可证

贡献前运行 `mvn clean verify`，说明兼容性、测试、文档和迁移影响。

本项目采用 [Apache License 2.0](LICENSE) 许可证。

---

<div align="center">

[返回顶部](#readme-top) · [问题反馈](https://github.com/easy-4-java/openclaw-java-sdk/issues)

</div>
