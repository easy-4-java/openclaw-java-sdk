# 文档检查结果

检查对象：OpenSpec 文档包 0.1.0-draft。本页先保留首次文档编制的检查记录，再列出后续 G0 证据和当前校验；压缩报告只对应首次检查。

## 实际执行

首次编制时使用文档结构检查器，检查必需产物、schema/metadata、proposal 与 capability 的一一对应、Purpose 长度、Requirement/Scenario 格式、规范关键词、任务验收条件、初始任务状态、全局 ID 唯一性、场景到任务的引用、依赖无环、相对链接和代码围栏。

结果：内部结构检查通过，0 项失败；精确检查数量以 validation-report/ 中保存的完整报告为准。

这不是官方 OpenSpec CLI 输出，也不是产品测试通过数。

| 交付项目 | 数量 |
|---|---:|
| OpenSpec changes | 6 |
| Capability specs | 21 |
| Requirements | 64 |
| Scenarios | 128 |
| 实施任务 | 97 |
| 已勾选 G0 准入证据任务 | 5 |
| 待完成任务 | 92 |

## 后续验证与剩余门禁

首次编制时官方 OpenSpec CLI 校验为 NOT_RUN；后续 G0 证据提交使用 OpenSpec 1.8.0 strict 校验 6/6 通过。2026-11-18 使用 OpenSpec 1.14.1 对本分支执行 `openspec validate --all --strict --no-interactive --json`，6/6 changes 有效，0 个 issues。

后续 G0 证据还记录了三分支 CodeGraph、Maven 构建和测试，详情及当时的 SDK SHA 见 `openspec/changes/establish-sdk-compatibility-governance/evidence/2026-09-20-precursor-evidence.md`。这些历史结果未覆盖本次之后的源码；真实 Gateway 集成、漏洞扫描、资源/堆分析仍为 NOT_RUN。本次文档 PR 不包含 SDK 实现。

## 阶段结论

文档结构检查和当前 OpenSpec strict 校验通过，内容保持 DRAFT / REVIEW_REQUIRED。目标 Gateway 版本已有记录但待人审确认；G0 剩余任务、真实 Gateway 验证及全部 SDK 功能验收仍未完成。评审先从 G0 兼容治理和 G1 现有协议修正开始。

详细逐项输出见 [完整报告与恢复说明](validation-report/README.md)。
