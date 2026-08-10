package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;
import io.github.easy4j.openclaw.cli.opts.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 本地 openclaw 命令门面。每个公开方法对应一个 CLI 子命令，并统一委托 OpenClawCliExecutor 返回退出码、标准输出和标准错误。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
public class OpenClawCli {

    /**
     * `OpenClawCli` 生命周期内保存的 `executor` 对应状态。
     */
    private final OpenClawCliExecutor executor;

    /**
     * 按给定配置创建 `OpenClawCli`，构造过程不隐式执行远程业务请求。
     *
     * @param executor 写入 `executor` 协议字段的内容
     */
    public OpenClawCli(OpenClawCliExecutor executor) {
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    /**
     * 通过执行器运行 `openclaw version` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult version() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--version").build());
    }

    /**
     * 通过执行器运行 `openclaw help` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult help() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--help").build());
    }

    // --- Gateway & daemon & health ---

    /**
     * 通过执行器运行 `openclaw gateway` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gateway(GatewayCommandOptions args) {
        return run("gateway", args);
    }

    /**
     * 通过执行器运行 `openclaw gateway-health` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions 写入 `rpcOptions` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayHealth(GatewayRpcOptions rpcOptions) {
        return gateway(GatewayCommandOptions.builder().health(rpcOptions).build());
    }

    /**
     * 通过执行器运行 `openclaw gateway-status` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions 写入 `rpcOptions` 协议字段的内容
     * @param statusOptions 写入 `statusOptions` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayStatus(GatewayRpcOptions rpcOptions,
                                           GatewayCliArgv.GatewayStatusOptions statusOptions) {
        return gateway(GatewayCommandOptions.builder().status(rpcOptions, statusOptions).build());
    }

    /**
     * 通过执行器运行 `openclaw gateway-probe` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions 写入 `rpcOptions` 协议字段的内容
     * @param probeOptions 写入 `probeOptions` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayProbe(GatewayRpcOptions rpcOptions,
                                          GatewayCliArgv.GatewayProbeOptions probeOptions) {
        return gateway(GatewayCommandOptions.builder().probe(rpcOptions, probeOptions).build());
    }

    /**
     * 通过执行器运行 `openclaw daemon` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult daemon(DaemonOptions args) {
        return run("daemon", args);
    }

    /**
     * 通过执行器运行 `openclaw health` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult health(HealthCommandOptions args) {
        return run("health", args);
    }

    /**
     * 通过执行器运行 `openclaw status` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult status(StatusCommandOptions args) {
        return run("status", args);
    }

    /**
     * 通过执行器运行 `openclaw doctor` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult doctor(DoctorOptions args) {
        return run("doctor", args);
    }

    /**
     * 通过执行器运行 `openclaw logs` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult logs(LogsOptions args) {
        return run("logs", args);
    }

    // --- Config & setup ---

    /**
     * 通过执行器运行 `openclaw config` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult config(ConfigOptions args) {
        return run("config", args);
    }

    /**
     * 通过执行器运行 `openclaw configure` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult configure(ConfigureOptions args) {
        return run("configure", args);
    }

    /**
     * 通过执行器运行 `openclaw onboard` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult onboard(OnboardOptions args) {
        return run("onboard", args);
    }

    // --- Agents & sessions & skills ---

    /**
     * 通过执行器运行 `openclaw agent` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult agent(AgentOptions args) {
        return run("agent", args);
    }

    /**
     * 通过执行器运行 `openclaw agents` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult agents(AgentsOptions args) {
        return run("agents", args);
    }

    /**
     * 通过执行器运行 `openclaw sessions` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult sessions(SessionsOptions args) {
        return run("sessions", args);
    }

    /**
     * 通过执行器运行 `openclaw skills` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult skills(SkillsOptions args) {
        return run("skills", args);
    }

    /**
     * 通过执行器运行 `openclaw approvals` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult approvals(ApprovalsOptions args) {
        return run("approvals", args);
    }

    // --- Channels & messaging & nodes ---

    /**
     * 通过执行器运行 `openclaw channels` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult channels(ChannelsOptions args) {
        return run("channels", args);
    }

    /**
     * 通过执行器运行 `openclaw message` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult message(MessageOptions args) {
        return run("message", args);
    }

    /**
     * 通过执行器运行 `openclaw pairing` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult pairing(PairingOptions args) {
        return run("pairing", args);
    }

    /**
     * 通过执行器运行 `openclaw qr` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult qr(QrOptions args) {
        return run("qr", args);
    }

    /**
     * 通过执行器运行 `openclaw node` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult node(NodeOptions args) {
        return run("node", args);
    }

    /**
     * 通过执行器运行 `openclaw nodes` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult nodes(NodesOptions args) {
        return run("nodes", args);
    }

    /**
     * 通过执行器运行 `openclaw devices` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult devices(DevicesOptions args) {
        return run("devices", args);
    }

    // --- Browser & MCP & tools ---

    /**
     * 通过执行器运行 `openclaw mcp` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult mcp(McpOptions args) {
        return run("mcp", args);
    }

    /**
     * 通过执行器运行 `openclaw plugins` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult plugins(PluginsOptions args) {
        return run("plugins", args);
    }

    // --- Automation & webhooks & cron ---

    /**
     * 通过执行器运行 `openclaw cron` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult cron(CronOptions args) {
        return run("cron", args);
    }

    /**
     * 通过执行器运行 `openclaw hooks` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult hooks(HooksOptions args) {
        return run("hooks", args);
    }

    /**
     * 通过执行器运行 `openclaw flows` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult flows(FlowsOptions args) {
        return run("tasks", args);
    }

    // --- Models & security & misc ---

    /**
     * 通过执行器运行 `openclaw models` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult models(ModelsOptions args) {
        return run("models", args);
    }

    /**
     * 通过执行器运行 `openclaw security` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult security(SecurityOptions args) {
        return run("security", args);
    }

    /**
     * 通过执行器运行 `openclaw secrets` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult secrets(SecretsOptions args) {
        return run("secrets", args);
    }

    /**
     * 通过执行器运行 `openclaw backup` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult backup(BackupOptions args) {
        return run("backup", args);
    }

    /**
     * 通过执行器运行 `openclaw update` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult update(UpdateOptions args) {
        return run("update", args);
    }

    /**
     * 通过执行器运行 `openclaw uninstall` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult uninstall(UninstallOptions args) {
        return run("uninstall", args);
    }

    /**
     * 通过执行器运行 `openclaw reset` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult reset(ResetOptions args) {
        return run("reset", args);
    }

    /**
     * 通过执行器运行 `openclaw completion` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult completion(CompletionOptions args) {
        return run("completion", args);
    }

    /**
     * 通过执行器运行 `openclaw tui` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tui(TuiOptions args) {
        return run("tui", args);
    }

    /**
     * 通过执行器运行 `openclaw dashboard` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult dashboard(DashboardOptions args) {
        return run("dashboard", args);
    }

    /**
     * 通过执行器运行 `openclaw directory` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult directory(DirectoryOptions args) {
        return run("directory", args);
    }

    /**
     * 通过执行器运行 `openclaw system` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult system(SystemOptions args) {
        return run("system", args);
    }

    /**
     * 通过执行器运行 `openclaw acp` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult acp(AcpOptions args) {
        return run("acp", args);
    }

    /**
     * 通过执行器运行 `openclaw chat` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult chat(ChatOptions args) {
        return run("chat", args);
    }

    /**
     * 通过执行器运行 `openclaw terminal` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult terminal(TerminalOptions args) {
        return run("terminal", args);
    }

    /**
     * 通过执行器运行 `openclaw commitments` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult commitments(CommitmentsOptions args) {
        return run("commitments", args);
    }

    /**
     * 通过执行器运行 `openclaw crestodian` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult crestodian(CrestodianOptions args) {
        return run("crestodian", args);
    }

    /**
     * 通过执行器运行 `openclaw exec-approvals` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execApprovals(ExecApprovalsOptions args) {
        return run("exec-approvals", args);
    }

    /**
     * 通过执行器运行 `openclaw exec-policy` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execPolicy(ExecPolicyOptions args) {
        return run("exec-policy", args);
    }

    /**
     * 通过执行器运行 `openclaw migrate` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult migrate(MigrateOptions args) {
        return run("migrate", args);
    }

    /**
     * 通过执行器运行 `openclaw proxy` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult proxy(ProxyOptions args) {
        return run("proxy", args);
    }

    /**
     * 通过执行器运行 `openclaw worktrees` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult worktrees(WorktreesOptions args) {
        return run("worktrees", args);
    }

    /**
     * 通过执行器运行 `openclaw tool` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tool(ToolOptions args) {
        return run("tool", args);
    }

    /**
     * 通过执行器运行 `openclaw tools` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 写入 `args` 协议字段的内容
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tools(ToolsOptions args) {
        return run("tools", args);
    }

    /**
     * 通过执行器运行 `openclaw execute` 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param request 请求对象
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execute(OpenClawCliRequest request) {
        return executor.execute(request);
    }

    /**
 * Assembles {@code openclaw &lt;&gt; &lt;sub-arguments...&gt;} .
     */
    private OpenClawCliResult run(String topLevel, CliSubArgs subArgs) {
        Objects.requireNonNull(topLevel, "topLevel");
        Objects.requireNonNull(subArgs, "subArgs");
        List<String> args = new ArrayList<>();
        args.add(topLevel);
        args.addAll(subArgs.toSubcommandArguments());
        return executor.execute(OpenClawCliRequest.builder().arguments(args).build());
    }
}
