# openclaw-java-sdk · OpenSpec 文档包

版本 0.1.0-draft · 2026-09-20 · **待评审，未实施，未归档**

这是一套仓库内的 OpenSpec 变更草案，不是仅供讨论的差距清单。包含六组 proposal/design/tasks、21 个 capability spec、64 条 Requirement 和 128 个 Scenario；任务共 97 项，其中 6 项 G0 准入证据任务已勾选，91 项待完成。SDK 功能尚未按这些草案实施；检查结果见 [文档检查结果](validation-summary.md)。

## 阅读顺序

1. [总体路线与依赖](roadmap.md)。
2. [当前覆盖与风险基线](coverage-baseline.md)及[来源登记](source-register.md)。
3. `openspec/changes/<change-id>/proposal.md` → `specs/*/spec.md` → `design.md` → `tasks.md`。
4. [验收计划](acceptance-plan.md)及[逐场景追踪表](traceability.md)。

## 交付边界

本目录及 openspec/ 以独立文档提交交付；没有修改任何 SDK Java 源码、pom、依赖或现有测试。G0 已记录目标版本和来源，但该锁定仍待人审确认；G1 编码继续受评审门禁约束。

首次文档编制时仅执行自定义结构检查；后续 G0 证据提交记录了三分支 CodeGraph、Maven 构建与测试，以及 OpenSpec 1.8.0 strict 校验。2026-11-18 使用 OpenSpec 1.14.1 对本分支再次执行 `openspec validate --all --strict --no-interactive --json`，6/6 changes 有效。历史构建证据对应 2026-09-20 的 SDK 提交。2026-11-18 补充了 [真实 Gateway 基线验证](../../openspec/changes/establish-sdk-compatibility-governance/evidence/2026-11-18-live-gateway.md)，覆盖三条维护线并记录默认分支的兼容缺口。

## 合入方式

`openspec/` 和 `docs/openclaw-integration/` 按仓库相对路径组织。先在文档专用分支审查再合入；如果目标分支后来已有同名规范或目录，需合并而不是覆盖。本文是迁入文档目录后的交付索引，不覆盖原仓库 README。

本轮没有创建任何生效 capability spec；`openspec/specs/` 只有占位文件。待变更实现、验收和人审完成后，再按项目流程归档。根目录 `openspec/config.yaml` 定义 spec-driven 模式和项目规则。

## 快速核对

查看 `docs/openclaw-integration/baseline.json` 确认首次文档编制时的 SDK 提交，再阅读 G0 的 `evidence/` 目录了解后续验证。OpenSpec 格式校验通过不代表产品实现完成。

## 发布打包说明

原始文档包的 traceability.json 使用 gzip 保存为 .json.gz；validation-report.json 使用 gzip + Base64 分段保存在 [validation-report/](validation-report/README.md)，该目录说明如何恢复原始 JSON。两份解压后的 JSON 与原包逐字节一致；README 和检查摘要已按后续证据修订，当前文件校验值见 [package-manifest.json](package-manifest.json)。修订不改变任何 Requirement 或 Scenario。
