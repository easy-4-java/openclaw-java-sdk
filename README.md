# openclaw-java-sdk

[English](./README.md) | [简体中文](./README.zh-CN.md)

Pure Java SDK — talks to the OpenClaw Gateway through independent HTTP, SSE, WebSocket and local CLI channels
[简体中文](./README.zh-CN.md)

> **Current branch**: `feature/1.0.x`
> **Version**: `1.0.x.20260630-SNAPSHOT`
> **JDK baseline**: 8
> **Project status**: stable (1.0.x line). Not yet published to Maven Central; artifacts are distributed via the Aliyun Maven repository and GitHub Releases.

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage](#8-core-usage)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

### 1.1 What it is

**openclaw-java-sdk** is a pure Java library for Java developers to integrate with the OpenClaw Gateway over all of its external communication surfaces: HTTP Chat Completions / Embeddings / Responses / Webhook / Tools invoke, a WebSocket control plane for bidirectional streaming, and the local `openclaw` CLI.

### 1.2 What it is not

- Not the OpenClaw Gateway itself, and not an LLM inference engine.
- No Spring, Servlet or ORM dependencies.
- No promises about JDK/framework combinations that are not part of the compatibility matrix.

### 1.3 Typical scenarios

| Scenario | Recommended entry | Result |
|---|---|---|
| Plain Java chat (blocking) | `client.chatCompletion(...)` | OpenAI-compatible Chat Completions response |
| Streaming chat / tool calls | `client.chatCompletionStream(request)` | SSE stream with delta / tool-call / completion callbacks |
| Embedding generation | `client.createEmbeddings(request)` | Embeddings response |
| Webhook-triggered agent | `client.hook(...)` / `client.wake(...)` | HTTP webhook invocation |
| Bidirectional realtime chat | `client.connect()` + `client.chatSend(...)` | WebSocket streaming replies |
| Local CLI operations | `client.cli().version()` / `gatewayHealth(...)` | Local `openclaw` subcommand execution |

<a id="2-features--status"></a>
## 2. Features & Status

| Capability | Status | Notes |
|---|:---:|---|
| HTTP Webhook | Available | `hook()` / `wake()`; custom mapping webhooks |
| Chat Completions | Available | `chatCompletion(...)` blocking + `chatCompletionStream(...)` SSE |
| Models | Available | `listModels()` |
| Embeddings | Available | `createEmbeddings(request)` |
| Responses | Available | `createResponse(request)` |
| Tools invoke | Available | `toolInvoke(request)` / `toolsInvoke()` |
| WebSocket control plane | Available | `connect()` / `chatSend()` / `sessionsSend()`; frame protocol under `ws.protocol` |
| Local CLI | Available | `cli()` facade over 40+ `openclaw` subcommands with typed options |
| Startup self-check | Available | Constructor-time HTTP health probe + `openclaw --version` probe (per-sub-system toggles) |

<a id="3-requirements--compatibility"></a>
## 3. Requirements & Compatibility

| Component | Version | Notes |
|---|---:|---|
| JDK | 8+ | 1.0.x line baseline |
| Maven | 3.0+ | Enforcer minimum |
| OkHttp / okhttp-sse | 4.12.0 | HTTP + SSE transport |
| Java-WebSocket | — | WebSocket transport |
| Jackson databind | 2.17.x | JSON |
| commons-exec | — | CLI subprocess execution |
| SLF4J | 2.0.18 | Logging facade |

Version-line matrix:

| Version line | Branch | JDK | Version pattern | Purpose |
|---|---|---:|---|---|
| 1.0.x | `feature/1.0.x` (this branch) | 8 | `1.0.x.*` | Legacy projects, Boot 2.x starter line |
| 2.0.x | `feature/2.0.x` | 17 | `2.0.x.*` | Main line (JDK 17) |
| 3.0.x | `feature/3.0.x` | 21 | `3.0.x.*` | New projects |

Dependency boundary: the SDK depends only on OkHttp, Jackson, Java-WebSocket, commons-exec and SLF4J. **No Spring dependency** — Spring Boot applications should use the companion `openclaw-spring-boot-starter`.

<a id="4-architecture--modules"></a>
## 4. Architecture & Modules

```text
[ Business Application ]
        |
        | openclaw-java-sdk
        v
+------------------------------------------+
| OpenClawClient (facade)                   |
|  HTTP   /v1/chat/completions, /v1/models  |
|         /v1/embeddings, /v1/responses,    |
|         /tools/invoke, /hooks/*           |
|  SSE    StreamingChatResponse             |
|  WS     connect / chatSend / sessionsSend |
|  CLI    local `openclaw` subprocess       |
+------------------------------------------+
        |
        v
[ OpenClaw Gateway ] -> [ LLM / Agents / Tools ]
```

Package layout:

| Package | Responsibility |
|---|---|
| `io.github.easy4j.openclaw` | Facade `OpenClawClient` + config classes |
| `io.github.easy4j.openclaw.api` | HTTP sub-clients (chat / embeddings / responses / webhook / tools) |
| `io.github.easy4j.openclaw.api.model` | DTOs (`ChatRequest`, `ChatResponse`, `Tools`, `HookRequest`, ...) |
| `io.github.easy4j.openclaw.api.sse` | SSE streaming (`StreamingChatResponse`, `SseStreamReader`, `SseEventAccumulator`) |
| `io.github.easy4j.openclaw.cli` | CLI facade (`OpenClawCli` / `OpenClawCliExecutor`) |
| `io.github.easy4j.openclaw.cli.opts` | Typed options for 60+ CLI subcommands |
| `io.github.easy4j.openclaw.cli.availability` | CLI availability probing |
| `io.github.easy4j.openclaw.exception` | Exception hierarchy |
| `io.github.easy4j.openclaw.ws` | WebSocket client + frame protocol (params / results) |

<a id="5-installation"></a>
## 5. Installation

Maven:

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>openclaw-java-sdk</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
implementation 'io.github.easy4j:openclaw-java-sdk:1.0.x.20260630-SNAPSHOT'
```

Snapshot builds require an enabled snapshot repository (Aliyun Maven snapshot repository per `distributionManagement` in `pom.xml`).

<a id="6-quick-start"></a>
## 6. Quick Start

```java
import io.github.easy4j.openclaw.OpenClawClient;
import io.github.easy4j.openclaw.OpenClawClientConfig;
import io.github.easy4j.openclaw.api.model.ChatMessage;
import io.github.easy4j.openclaw.api.model.ChatResponse;
import java.util.List;

// 1. Configure
OpenClawClientConfig config = new OpenClawClientConfig();
config.getHttp().setGatewayBaseUrl("http://localhost:18789");
config.getHttp().setGatewayAuthToken("your-gateway-token");

// 2. Create the client (constructor runs the startup self-check)
OpenClawClient client = new OpenClawClient(config);

// 3. Chat
ChatResponse resp = client.chatCompletion(
        "openclaw/default", "gpt-4o",
        List.of(ChatMessage.ofUser("Hello")));
System.out.println(resp.getChoices().get(0).getMessage().getContent());

// 4. Release
client.close();
```

**Expected result**: with the default config the HTTP subsystem is enabled but `startupCheckEnabled` is `false`, so no blocking probe runs at startup; the call returns the agent's reply text. When startup checks are enabled, INFO logs `OpenClaw HTTP health check passed: ...` / `OpenClaw CLI ready: ...` appear, and a failing probe either throws `IllegalStateException` (fail-fast) or logs a WARN (non-fail-fast).

<a id="7-configuration"></a>
## 7. Configuration

Configuration is object-based (no Spring properties in this library). Three config classes:

| Config class | Responsibility |
|---|---|
| `OpenClawClientConfig` | Aggregates `http` + `cli` sub-configs |
| `OpenClawHttpClientConfig` | Gateway HTTP/WS settings |
| `OpenClawCliConfig` | Local CLI settings |

`OpenClawHttpClientConfig` properties:

| Property | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `true` | Enable the HTTP sub-system |
| `startupCheckEnabled` | boolean | `false` | Probe the gateway health endpoint at startup |
| `failFastOnUnavailable` | boolean | `false` | Fail construction when the probe fails |
| `gatewayBaseUrl` | String | `http://localhost:18789` | Gateway base URL |
| `gatewayAuthToken` | String | — | Control-plane token |
| `gatewayAuthPassword` | String | — | Control-plane password mode |
| `hooksToken` | String | — | Webhook auth token |
| `hooksPath` | String | `/hooks` | Webhook base path |
| `hooksUseXOpenclawTokenHeader` | boolean | `false` | Send the hook token via `x-openclaw-token` header |
| `verifySsl` | boolean | `true` | Verify HTTPS certificates |
| `connectTimeoutMillis` | int | `15000` | Connect timeout (ms) |
| `readTimeoutMillis` | int | `120000` | Read timeout (ms) |

`OpenClawCliConfig` properties:

| Property | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `true` | Enable the CLI sub-system |
| `startupCheckEnabled` | boolean | `false` | Probe `openclaw --version` at startup |
| `failFastOnUnavailable` | boolean | `false` | Fail construction when the probe fails |
| `executable` | String | `openclaw` | Executable name or absolute path |
| `timeout` | int | `300` | CLI command timeout (seconds) |
| `probeTimeoutSeconds` | int | `5` | Startup probe timeout (seconds) |
| `workingDirectory` | String | — | Subprocess working directory |
| `maxConcurrentExecutions` | int | `0` | Max concurrent subprocesses (0 = CPU cores) |

Custom request headers via `OpenClawHeaders.Builder`:

| Header | Constant | Purpose |
|---|---|---|
| `x-openclaw-model` | `X_OPENCLAW_MODEL` | Override backend model |
| `x-openclaw-agent-id` | `X_OPENCLAW_AGENT_ID` | Override agent |
| `x-openclaw-session-key` | `X_OPENCLAW_SESSION_KEY` | Explicit session routing |
| `x-openclaw-message-channel` | `X_OPENCLAW_MESSAGE_CHANNEL` | Entry channel context |
| `x-openclaw-scopes` | `X_OPENCLAW_SCOPES` | Scope claims |

Auth priority: webhook (`/hooks/*`) uses `hooksToken`; control plane (`/v1/*`, `/tools/*`, WebSocket) uses `gatewayAuthToken` → `gatewayAuthPassword` → `hooksToken` → none.

<a id="8-core-usage"></a>
## 8. Core Usage

### 8.1 Streaming chat with tool calls

```java
ChatRequest req = ChatRequest.builder()
        .agent("openclaw/default")
        .messages(List.of(ChatMessage.ofUser("What's the weather in Beijing?")))
        .tools(List.of(Tools.function("get_weather", "Get weather")
                .param("city", "string", "City name", true).build()))
        .toolChoice("auto")
        .build();

client.chatCompletionStream(req)
        .onDelta(delta -> System.out.print(delta))
        .onToolCall(toolCalls -> toolCalls.forEach(
                tc -> System.out.println(tc.getFunction().getName())))
        .onComplete(text -> System.out.println("\n[done]"))
        .onError(Throwable::printStackTrace);
```

### 8.2 WebSocket streaming conversation

```java
HelloOk hello = client.connect();
client.chatSend("Hello", new ChatStreamHandler() {
    @Override public void onDelta(String text) { System.out.print(text); }
    @Override public void onComplete(String fullText) { System.out.println(); }
    @Override public void onError(String error) { System.err.println(error); }
});
```

### 8.3 Local CLI

```java
OpenClawCliResult result = client.cli().version();        // openclaw --version
System.out.println(result.getStdout());

client.cli().gatewayHealth(                                // openclaw gateway health
        GatewayRpcOptions.builder().url("ws://127.0.0.1:18789").build());
```

<a id="9-testing--build"></a>
## 9. Testing & Build

```bash
mvn clean verify
```

- Unit tests cover config defaults, CLI args/execution, WS protocol, HTTP sub-clients and constructor contracts (23 test sources: 22 test classes + 1 mock CLI helper under `src/test`).
- JaCoCo runs `prepare-agent`, `report` and `check` on the `verify` phase with a **90% line-coverage** rule (`haltOnFailure=false`).
- Release packaging (`mvn -Prelease deploy`) attaches sources and javadoc jars, GPG-signs artifacts and is wired for Sonatype Central Publishing; plain `mvn deploy` routes SNAPSHOT/release artifacts to the Aliyun Maven repository per `distributionManagement`.

<a id="10-versioning--branches"></a>
## 10. Versioning & Branches

| Branch | Version pattern | JDK | Maintenance policy |
|---|---|---|---|
| `feature/1.0.x` (this branch) | `1.0.x.*` | 8 | Compatibility fixes and JDK-8-safe dependency upgrades only |
| `feature/2.0.x` | `2.0.x.*` | 17 | Main development line |
| `feature/3.0.x` | `3.0.x.*` | 21 | New projects |

<a id="11-contributing--license"></a>
## 11. Contributing & License

Run `mvn clean verify` before opening a pull request and describe compatibility, testing, documentation and migration impact. This project is licensed under the [Apache License 2.0](LICENSE).
