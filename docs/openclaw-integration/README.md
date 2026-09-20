# openclaw-java-sdk · OpenSpec 文档包

版本 0.1.0-draft · 2026-09-20 · **待评审，未实施，未归档**

这是一套仓库内的 OpenSpec 变更草案，不是仅供讨论的差距清单。包含六组 proposal/design/tasks、21 个 capability spec、64 条 Requirement 和 128 个 Scenario；实施任务共 97 项，全部未执行；检查结果见 [文档检查结果](validation-summary.md)。

## 阅读顺序

1. [总体路线与依赖](roadmap.md)。
2. [当前覆盖与风险基线](coverage-baseline.md)及[来源登记](source-register.md)。
3. `openspec/changes/<change-id>/proposal.md` → `specs/*/spec.md` → `design.md` → `tasks.md`。
4. [验收计划](acceptance-plan.md)及[逐场景追踪表](traceability.md)。

## 交付边界

本目录及 openspec/ 以独立文档提交交付；没有修改任何 SDK Java 源码、pom、依赖或现有测试，也没有写入维护者的本机工作区。所有实施任务保持未勾选，人审批准和具体目标 Gateway 版本锁定仍是下一阶段前提。

官方 OpenSpec CLI 在当前环境不可用，尝试获取时因 DNS/网络解析失败而未能安装。因此只报告实际完成的自定义文档结构检查，不声称执行过官方 validate。也没有执行 codegraph、Maven 或真实 Gateway 测试。

## 合入方式

`openspec/` 和 `docs/openclaw-integration/` 按仓库相对路径组织。先在文档专用分支审查再合入；如果目标分支后来已有同名规范或目录，需合并而不是覆盖。本文是迁入文档目录后的交付索引，不覆盖原仓库 README。

本轮没有创建任何生效 capability spec；`openspec/specs/` 只有占位文件。待变更实现、验收和人审完成后，再按项目流程归档。根目录 `openspec/config.yaml` 定义 spec-driven 模式和项目规则。

## 快速核对

查看 `docs/openclaw-integration/baseline.json` 确认 SDK 提交及明确未验证的上游版本。参照 acceptance-plan 中的命令使用官方 OpenSpec CLI 校验；切勿把 artifact 已齐全或状态命令的 isComplete 字段解释为产品实现完成。

## 发布打包说明

原始文档包的 traceability.json 使用 gzip 保存为 .json.gz；validation-report.json 使用 gzip + Base64 分段保存在 [validation-report/](validation-report/README.md)，该目录说明如何恢复原始 JSON。解压后的内容与原包逐字节一致；Markdown 追踪表与检查摘要保留直接可读版本。仓库路径与文件校验值见 [package-manifest.json](package-manifest.json)。此打包调整不改变任何 Requirement、Scenario 或实施任务。
