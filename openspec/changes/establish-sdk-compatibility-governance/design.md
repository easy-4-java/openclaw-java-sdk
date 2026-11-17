# 建立版本与三分支兼容治理 — Technical Design

状态：DRAFT / REVIEW_REQUIRED。下述新类与模块名称为拟议设计，不是仓库已存在能力。

## Context

动机与范围见 proposal.md；固定的 SDK HEAD、静态观察和证据等级见 [coverage-baseline](../../../docs/openclaw-integration/coverage-baseline.md)。当前文档基于公开协议说明和此前源码静态核查，不包含真实 Gateway 验证。在线文档随时间变化；目标版本锁定仍是实施前的阻断条件。

## Goals / Non-Goals

用可观察契约约束新增或修正行为；保留三条 Java 发行线的既有基线与可行的源码兼容。只引入本变更需要的组件，不进行无关重构。SDK 不代替 Gateway 做权限裁定，也不代替宿主存储或授予长期凭据。

## Decisions

采用共同契约、分支独立证据的方式，不把三个分支合并成一条开发线，也不新增统一发行版本。共享 fixture 的期望语义保持一致；Java 8 语法、Jackson 2/3 包名和宿主扩展点的差异记录到兼容清单。

已读取的 SDK HEAD 可精确固定；在线 OpenClaw 文档不等于已测试发布版本。因此先允许文档评审，实施前必须选定可获得的 Gateway 精确版本／提交与构建来源，并核实其协议文件、方法、事件和权限。当前没有选择或测试该版本，状态为 BLOCKED_VERSION_PIN。

不把根 specs 填成未来蓝图。由于仓库尚无本轮能力的生效规范，这些能力首次以 ADDED Requirements 建档；ADDED 指新增规范条目，不断言全部代码此前不存在。归档是未来行为，需经过本项目人审和实施证据核验。

备选方案是复制 README 作为基线，成本低但会继承错误版本说明；另一方案是先全量重写 SDK 再补文档，无法约束实现范围。本方案优先构建可复核的能力矩阵。

## Technical Approach

核对 SDK 提交 → 锁定目标 Gateway → 映射方法/事件/权限 → 共享场景 → 三分支验证 → 精确能力声明

错误类型和状态转换由共享协议层统一；本地超时／取消与远端失败／取消分开。以下源码触点仅用于实施规划，新增路径在接入前需核对实际工作树，不把不存在的文件列为已修改：

- `docs/openclaw-integration/source-register.md`
- `docs/openclaw-integration/coverage-baseline.md`
- `docs/openclaw-integration/acceptance-plan.md`
- `README.md`
- `README.zh-CN.md`
- `.github/workflows/（实施时核对已有文件后扩展）`

## Risks / Trade-offs

文档与运行时漂移 → 将版本锁定作为编码入口门禁；缺少某分支构建环境 → 保留该分支未验证，不复制其他分支通过结果。

所有新状态、上限和取消语义必须进入验收；不得使用修改测试预期、跳过不兼容分支或放宽权限来消除失败。

## Migration Plan

先记录对应旧行为并建立最小失败样本，再实施局部修正或加法 API，最后同步其余分支与文档。按 capability 小批提交，避免跨无关领域的大规模替换。回滚只回退该功能变更和明确可逆状态；配置落盘、已执行工具或已投递消息的外部副作用不宣称自动撤销。根 specs 的更新留到经批准的归档阶段。

## Validation Strategy

需求与场景的逐项映射见 [traceability](../../../docs/openclaw-integration/traceability.md)。每项能力须完成固定样本验证、受控模拟、适用分支构建及真实 Gateway 验收。资源测试至少覆盖成功、失败、取消、deadline、断线和用户回调异常。缺失环境的测试记录 BLOCKED，不记录 PASS。

## Sources

[O00 OpenClaw 文档索引](https://docs.openclaw.ai/llms.txt)；[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)；[S01 OpenSpec 概念与变更目录](https://github.com/Fission-AI/OpenSpec/blob/main/docs/concepts.md)；[S02 OpenSpec spec-driven schema](https://github.com/Fission-AI/OpenSpec/blob/main/schemas/spec-driven/schema.yaml)

上游文档用于需求依据；本 SDK 的方法签名、状态名与组件结构属于本方案设计。每个 wire 字段和方法在实施前还须与锁定版本确认，不能凭字符串拼接猜测。
