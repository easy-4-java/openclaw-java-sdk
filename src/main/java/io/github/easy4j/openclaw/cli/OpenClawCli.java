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
     * CLI 子进程执行器，统一处理并发限制、超时和输出收集。
     */
    private final OpenClawCliExecutor executor;

    /**
     * 创建 CLI 门面；各子命令在对应方法被调用时才交给执行器运行。
     *
     * @param executor 负责启动并监管本地 CLI 子进程的执行器
     */
    public OpenClawCli(OpenClawCliExecutor executor) {
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    /**
     * 通过执行器运行 {@code openclaw version} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult version() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--version").build());
    }

    /**
     * 通过执行器运行 {@code openclaw help} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult help() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--help").build());
    }

    // --- Gateway & daemon & health ---

    /**
     * 通过执行器运行 {@code openclaw gateway} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gateway(GatewayCommandOptions args) {
        return run("gateway", args);
    }

    /**
     * 通过执行器运行 {@code openclaw gateway-health} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions Gateway RPC 的地址、认证与超时选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayHealth(GatewayRpcOptions rpcOptions) {
        return gateway(GatewayCommandOptions.builder().health(rpcOptions).build());
    }

    /**
     * 通过执行器运行 {@code openclaw gateway-status} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions Gateway RPC 的地址、认证与超时选项
     * @param statusOptions gateway-status 子命令的状态查询选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayStatus(GatewayRpcOptions rpcOptions,
                                           GatewayCliArgv.GatewayStatusOptions statusOptions) {
        return gateway(GatewayCommandOptions.builder().status(rpcOptions, statusOptions).build());
    }

    /**
     * 通过执行器运行 {@code openclaw gateway-probe} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param rpcOptions Gateway RPC 的地址、认证与超时选项
     * @param probeOptions gateway-probe 子命令的探测选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult gatewayProbe(GatewayRpcOptions rpcOptions,
                                          GatewayCliArgv.GatewayProbeOptions probeOptions) {
        return gateway(GatewayCommandOptions.builder().probe(rpcOptions, probeOptions).build());
    }

    /**
     * 通过执行器运行 {@code openclaw daemon} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult daemon(DaemonOptions args) {
        return run("daemon", args);
    }

    /**
     * 通过执行器运行 {@code openclaw health} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult health(HealthCommandOptions args) {
        return run("health", args);
    }

    /**
     * 通过执行器运行 {@code openclaw status} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult status(StatusCommandOptions args) {
        return run("status", args);
    }

    /**
     * 通过执行器运行 {@code openclaw doctor} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult doctor(DoctorOptions args) {
        return run("doctor", args);
    }

    /**
     * 通过执行器运行 {@code openclaw logs} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult logs(LogsOptions args) {
        return run("logs", args);
    }

    // --- Config & setup ---

    /**
     * 通过执行器运行 {@code openclaw config} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult config(ConfigOptions args) {
        return run("config", args);
    }

    /**
     * 通过执行器运行 {@code openclaw configure} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult configure(ConfigureOptions args) {
        return run("configure", args);
    }

    /**
     * 通过执行器运行 {@code openclaw onboard} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult onboard(OnboardOptions args) {
        return run("onboard", args);
    }

    // --- Agents & sessions & skills ---

    /**
     * 通过执行器运行 {@code openclaw agent} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult agent(AgentOptions args) {
        return run("agent", args);
    }

    /**
     * 通过执行器运行 {@code openclaw agents} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult agents(AgentsOptions args) {
        return run("agents", args);
    }

    /**
     * 通过执行器运行 {@code openclaw sessions} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult sessions(SessionsOptions args) {
        return run("sessions", args);
    }

    /**
     * 通过执行器运行 {@code openclaw skills} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult skills(SkillsOptions args) {
        return run("skills", args);
    }

    /**
     * 通过执行器运行 {@code openclaw approvals} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult approvals(ApprovalsOptions args) {
        return run("approvals", args);
    }

    // --- Channels & messaging & nodes ---

    /**
     * 通过执行器运行 {@code openclaw channels} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult channels(ChannelsOptions args) {
        return run("channels", args);
    }

    /**
     * 通过执行器运行 {@code openclaw message} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult message(MessageOptions args) {
        return run("message", args);
    }

    /**
     * 通过执行器运行 {@code openclaw pairing} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult pairing(PairingOptions args) {
        return run("pairing", args);
    }

    /**
     * 通过执行器运行 {@code openclaw qr} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult qr(QrOptions args) {
        return run("qr", args);
    }

    /**
     * 通过执行器运行 {@code openclaw node} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult node(NodeOptions args) {
        return run("node", args);
    }

    /**
     * 通过执行器运行 {@code openclaw nodes} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult nodes(NodesOptions args) {
        return run("nodes", args);
    }

    /**
     * 通过执行器运行 {@code openclaw devices} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult devices(DevicesOptions args) {
        return run("devices", args);
    }

    // --- Browser & MCP & tools ---

    /**
     * 通过执行器运行 {@code openclaw mcp} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult mcp(McpOptions args) {
        return run("mcp", args);
    }

    /**
     * 通过执行器运行 {@code openclaw plugins} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult plugins(PluginsOptions args) {
        return run("plugins", args);
    }

    // --- Automation & webhooks & cron ---

    /**
     * 通过执行器运行 {@code openclaw cron} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult cron(CronOptions args) {
        return run("cron", args);
    }

    /**
     * 通过执行器运行 {@code openclaw hooks} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult hooks(HooksOptions args) {
        return run("hooks", args);
    }

    /**
     * 通过执行器运行 {@code openclaw flows} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult flows(FlowsOptions args) {
        return run("tasks", args);
    }

    // --- Models & security & misc ---

    /**
     * 通过执行器运行 {@code openclaw models} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult models(ModelsOptions args) {
        return run("models", args);
    }

    /**
     * 通过执行器运行 {@code openclaw security} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult security(SecurityOptions args) {
        return run("security", args);
    }

    /**
     * 通过执行器运行 {@code openclaw secrets} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult secrets(SecretsOptions args) {
        return run("secrets", args);
    }

    /**
     * 通过执行器运行 {@code openclaw backup} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult backup(BackupOptions args) {
        return run("backup", args);
    }

    /**
     * 通过执行器运行 {@code openclaw update} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult update(UpdateOptions args) {
        return run("update", args);
    }

    /**
     * 通过执行器运行 {@code openclaw uninstall} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult uninstall(UninstallOptions args) {
        return run("uninstall", args);
    }

    /**
     * 通过执行器运行 {@code openclaw reset} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult reset(ResetOptions args) {
        return run("reset", args);
    }

    /**
     * 通过执行器运行 {@code openclaw completion} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult completion(CompletionOptions args) {
        return run("completion", args);
    }

    /**
     * 通过执行器运行 {@code openclaw tui} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tui(TuiOptions args) {
        return run("tui", args);
    }

    /**
     * 通过执行器运行 {@code openclaw dashboard} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult dashboard(DashboardOptions args) {
        return run("dashboard", args);
    }

    /**
     * 通过执行器运行 {@code openclaw directory} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult directory(DirectoryOptions args) {
        return run("directory", args);
    }

    /**
     * 通过执行器运行 {@code openclaw system} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult system(SystemOptions args) {
        return run("system", args);
    }

    /**
     * 通过执行器运行 {@code openclaw acp} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult acp(AcpOptions args) {
        return run("acp", args);
    }

    /**
     * 通过执行器运行 {@code openclaw chat} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult chat(ChatOptions args) {
        return run("chat", args);
    }

    /**
     * 通过执行器运行 {@code openclaw terminal} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult terminal(TerminalOptions args) {
        return run("terminal", args);
    }

    /**
     * 通过执行器运行 {@code openclaw commitments} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult commitments(CommitmentsOptions args) {
        return run("commitments", args);
    }

    /**
     * 通过执行器运行 {@code openclaw crestodian} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult crestodian(CrestodianOptions args) {
        return run("crestodian", args);
    }

    /**
     * 通过执行器运行 {@code openclaw exec-approvals} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execApprovals(ExecApprovalsOptions args) {
        return run("exec-approvals", args);
    }

    /**
     * 通过执行器运行 {@code openclaw exec-policy} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execPolicy(ExecPolicyOptions args) {
        return run("exec-policy", args);
    }

    /**
     * 通过执行器运行 {@code openclaw migrate} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult migrate(MigrateOptions args) {
        return run("migrate", args);
    }

    /**
     * 通过执行器运行 {@code openclaw proxy} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult proxy(ProxyOptions args) {
        return run("proxy", args);
    }

    /**
     * 通过执行器运行 {@code openclaw worktrees} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult worktrees(WorktreesOptions args) {
        return run("worktrees", args);
    }

    /**
     * 通过执行器运行 {@code openclaw tool} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tool(ToolOptions args) {
        return run("tool", args);
    }

    /**
     * 通过执行器运行 {@code openclaw tools} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param args 要转换为命令行参数的子命令选项
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult tools(ToolsOptions args) {
        return run("tools", args);
    }

    /**
     * 通过执行器运行 {@code openclaw execute} 子命令，不通过 HTTP 或 WebSocket 通道。
     *
     * @param request 要校验、序列化并发送的 {@code OpenClawCliRequest}
     * @return 包含子进程退出码、标准输出和标准错误的执行结果
     */
    public OpenClawCliResult execute(OpenClawCliRequest request) {
        return executor.execute(request);
    }

    /**
     * 按 {@code openclaw <top-level> <sub-arguments...>} 顺序组装并执行子命令。
     *
     * @param topLevel 一级子命令名称
     * @param subArgs 负责生成后续参数的类型化选项
     * @return 包含退出码、标准输出和标准错误的执行结果
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
