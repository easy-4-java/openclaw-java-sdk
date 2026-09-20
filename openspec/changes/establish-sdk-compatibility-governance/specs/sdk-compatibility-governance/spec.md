# 兼容性与证据治理 — Spec Delta

## Purpose

为 openclaw-java-sdk 的兼容性与证据治理定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: GOV-01 发布能力必须有版本化证据
SDK 的能力声明 SHALL 绑定 SDK 分支与提交、OpenClaw 精确版本或提交、协议版本和验证层级；文档阅读、字段存在、CLI 透传均 MUST NOT 被标记为真实集成通过。

#### Scenario: GOV-01-S1 真实通过可追溯
- **GIVEN** 已锁定目标 Gateway 构建且场景已执行
- **WHEN** 维护者发布兼容矩阵
- **THEN** 矩阵记录双方提交、运行命令、环境、测试结果与证据文件位置

#### Scenario: GOV-01-S2 缺少运行证据
- **GIVEN** 只有当前在线文档及源码静态审查
- **WHEN** 生成发布说明
- **THEN** 状态保持未验证，禁止宣称生产就绪或全量协议支持

### Requirement: GOV-02 三分支语义一致而非字节相同
同一已批准能力的三条版本线 SHALL 使用相同请求与响应语义、错误分类、权限边界和验收样本；语言及依赖适配差异 SHALL 单独登记，未经评审 MUST NOT 形成隐藏功能分叉。

#### Scenario: GOV-02-S1 依赖适配差异
- **GIVEN** 1.x、2.x、3.x 使用不同 JDK 或 JSON 库
- **WHEN** 同一协议样本在各分支执行
- **THEN** 序列化、流重建和终态结果一致，获准差异可追溯

#### Scenario: GOV-02-S2 单分支缺测
- **GIVEN** 某次修复只通过 3.x 的验收
- **WHEN** 准备宣称三个版本都支持
- **THEN** 其余分支保持未验证或明确不支持，不用 cherry-pick 成功代替测试

### Requirement: GOV-03 提案与生效规范分离
未评审或未实现的行为 SHALL 保留为变更提案；实现任务 MUST 保持未完成状态直至对应证据通过；规范归档 SHALL 以本项目规定的人审与验收条件为前提。

#### Scenario: GOV-03-S1 文档评审
- **GIVEN** proposal、design、specs、tasks 已写好但未实现
- **WHEN** 评审者查看变更
- **THEN** 显示草案状态和未完成任务，不把 artifact 齐全解释为功能完成

#### Scenario: GOV-03-S2 提前归档
- **GIVEN** 存在失败或缺失的必需验收证据
- **WHEN** 尝试更新生效规范
- **THEN** 本项目流程阻止归档并列出缺失项；不声称 OpenSpec CLI 自带这些业务审批门禁

### Requirement: GOV-04 版本漂移和移除能力可见
SDK SHALL 为不适用、已移除、实验性或仅某认证模式支持的能力保留明确说明；目标版本改变后 SHALL 重新核对契约，不得以较新的在线文档替代目标版本事实。

#### Scenario: GOV-04-S1 版本升级
- **GIVEN** 候选 Gateway 提交改变方法或事件字段
- **WHEN** 执行兼容复核
- **THEN** 差异进入规范与样本评审，旧证据不自动继承

#### Scenario: GOV-04-S2 已移除调用
- **GIVEN** 上游版本已移除某方法
- **WHEN** 调用该能力
- **THEN** 返回明确的不可用结果且不尝试以更高权限或未评审替代方法绕过

## Sources

[O00 OpenClaw 文档索引](https://docs.openclaw.ai/llms.txt)；[O01 外部应用接入边界](https://docs.openclaw.ai/gateway/external-apps)；[O02 协议版本与客户端恢复边界](https://docs.openclaw.ai/gateway/protocol/versioning)；[S01 OpenSpec 概念与变更目录](https://github.com/Fission-AI/OpenSpec/blob/main/docs/concepts.md)；[S02 OpenSpec spec-driven schema](https://github.com/Fission-AI/OpenSpec/blob/main/schemas/spec-driven/schema.yaml)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
