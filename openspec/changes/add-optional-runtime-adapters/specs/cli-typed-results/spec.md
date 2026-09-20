# CLI 类型化结果和可选动作 — Spec Delta

## Purpose

为 openclaw-java-sdk 的CLI 类型化结果和可选动作定义可独立验证的外部行为、失败边界和授权约束，使三条 Java 发行线能够依据同一版本化契约实现并接受验收，而不是依据类名或接口存在推断功能已完成。

## ADDED Requirements

### Requirement: CLI-01 命令存在与支持等级分别声明
SDK SHALL 区分类型化命令、自由参数透传、仅可启动进程及已完成协议集成；CLI 可用性检查 SHALL 记录可执行文件和精确版本，不把 --version 成功当成全部命令可用。

#### Scenario: CLI-01-S1 自由参数透传
- **GIVEN** browser 或 infer 通过自由参数运行
- **WHEN** 生成能力清单
- **THEN** 仅标记透传，不标记全量类型化或真实集成通过

#### Scenario: CLI-01-S2 可执行但命令缺失
- **GIVEN** --version 成功但命令在目标版本中不存在
- **WHEN** 调用该命令
- **THEN** 保留不支持结果，不生成虚假的成功 DTO

### Requirement: CLI-02 机器输出与执行状态分开
支持机器输出的命令 SHALL 提供版本化解析与原始输出保留，区分退出失败、超时、取消、输出不合法和业务失败；交互式命令 SHALL 不冒充非交互 JSON API。

#### Scenario: CLI-02-S1 合法机器输出
- **GIVEN** 目标命令支持 JSON 且退出成功
- **WHEN** 解析输出
- **THEN** 同时提供类型化业务结果和执行元数据

#### Scenario: CLI-02-S2 输出不是预期 JSON
- **GIVEN** 命令退出为零但只输出交互提示或未知格式
- **WHEN** 解析结果
- **THEN** 报告解析或模式不兼容，不把空结果当成功

### Requirement: CLI-03 参数与输出受控且跨平台验证
CLI 调用 SHALL 保留字面 argv、UTF-8 语义、输出上限、工作目录和取消语义；浏览器、媒体推理及管理动作 SHALL 经过显式能力与权限策略，不默认经 shell 解释。

#### Scenario: CLI-03-S1 多词中文参数
- **GIVEN** 参数含空格、中文或引号
- **WHEN** 在已声明支持的平台执行
- **THEN** 进程收到原始参数内容，输出按约定解码

#### Scenario: CLI-03-S2 超量输出或交互不退出
- **GIVEN** 命令持续输出或等待输入
- **WHEN** 达到输出或时间上限
- **THEN** 受控终止并保留截断与超时原因，不耗尽内存或占用无限并发

## Sources

[O16 CLI 参考入口](https://docs.openclaw.ai/cli)

本文件是待实施规范，不是运行测试报告；所属 proposal 与 design 定义版本准入和实施边界。
