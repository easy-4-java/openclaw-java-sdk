package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;
import io.github.easy4j.openclaw.cli.opts.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * top-level CLI command facade; {@code io.github.easy4j.openclaw.cli.opts} {@link CliSubArgs} ( {@link AgentOptions}),
 * avoids using {@code String...} .
 * <p>
 * Documentation index:<a href="https://docs.openclaw.ai/cli">CLI Reference</a>.
 * Gateway RPC {@link GatewayCommandOptions.Builder#health(GatewayRpcOptions)} ,
 * {@link #gatewayHealth(GatewayRpcOptions)} .
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli">CLI Reference</a>
 * @see <a href="https://docs.openclaw.ai/gateway/cli-backends">CLI Backends</a>
  *
 * @author [@Loong Wan](https://github.com/loong10k)
  * @since 3.0.0
 */
@Getter
public class OpenClawCli {

    /**
 * underlying executor(usage: {@link OpenClawCliRequest})
     */
    private final OpenClawCliExecutor executor;

    /**
 * @param executor Used for {@code openclaw} executor
     */
    public OpenClawCli(OpenClawCliExecutor executor) {
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    /**
 * Equivalent to {@code openclaw --version}(with documentation {@code -V} / {@code --version} ).
 * <p>example(shell):{@code openclaw --version}</p>
     *
     * @see <a href="https://docs.openclaw.ai/cli">CLI Reference</a>
     */
    public OpenClawCliResult version() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--version").build());
    }

    /**
 * Equivalent to {@code openclaw --help}(help,subcommand {@code &lt;cmd&gt; --help}).
 * <p>example:{@code openclaw --help}</p>
     *
     * @see <a href="https://docs.openclaw.ai/cli">CLI Reference</a>
     */
    public OpenClawCliResult help() {
        return executor.execute(OpenClawCliRequest.builder().arguments("--help").build());
    }

    // --- Gateway & daemon & health ---

    /**
     * {@code openclaw gateway ...}。
 * <p>example:{@code gateway(GatewayCommandOptions.builder.health(GatewayRpcOptions.builder.url("ws://127.0.0.1:18789").build).build)}</p>
     *
 * @param args subcommand and flags,See {@link GatewayCommandOptions}
     * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
     */
    public OpenClawCliResult gateway(GatewayCommandOptions args) {
        return run("gateway", args);
    }

    /**
 * {@code gateway health}( {@link GatewayCommandOptions.Builder#health(GatewayRpcOptions)}).
     *
 * @param rpcOptions with documentation"Query a running Gateway" RPC
     * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
     * @see GatewayCliArgv#health(GatewayRpcOptions)
     */
    public OpenClawCliResult gatewayHealth(GatewayRpcOptions rpcOptions) {
        return gateway(GatewayCommandOptions.builder().health(rpcOptions).build());
    }

    /**
 * {@code gateway status}.
     *
     * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
     * @see GatewayCliArgv#status(GatewayRpcOptions, GatewayCliArgv.GatewayStatusOptions)
     */
    public OpenClawCliResult gatewayStatus(GatewayRpcOptions rpcOptions,
                                           GatewayCliArgv.GatewayStatusOptions statusOptions) {
        return gateway(GatewayCommandOptions.builder().status(rpcOptions, statusOptions).build());
    }

    /**
 * {@code gateway probe}.
     *
     * @see <a href="https://docs.openclaw.ai/cli/gateway">gateway CLI</a>
     * @see GatewayCliArgv#probe(GatewayRpcOptions, GatewayCliArgv.GatewayProbeOptions)
     */
    public OpenClawCliResult gatewayProbe(GatewayRpcOptions rpcOptions,
                                          GatewayCliArgv.GatewayProbeOptions probeOptions) {
        return gateway(GatewayCommandOptions.builder().probe(rpcOptions, probeOptions).build());
    }

    /**
     * {@code openclaw daemon ...}。
     *
 * @param args subcommand and flags,See {@link DaemonOptions}
     * @see <a href="https://docs.openclaw.ai/cli/daemon">daemon CLI</a>
     */
    public OpenClawCliResult daemon(DaemonOptions args) {
        return run("daemon", args);
    }

    /**
 * {@code openclaw health}( {@code gateway health}).
     *
 * @param args subcommand and flags,See {@link HealthCommandOptions}
     * @see <a href="https://docs.openclaw.ai/cli/health">health CLI</a>
     */
    public OpenClawCliResult health(HealthCommandOptions args) {
        return run("health", args);
    }

    /**
 * {@code openclaw status ...}( status,not only gateway subcommand).
     *
 * @param args subcommand and flags,See {@link StatusCommandOptions}
     * @see <a href="https://docs.openclaw.ai/cli/status">status CLI</a>
     */
    public OpenClawCliResult status(StatusCommandOptions args) {
        return run("status", args);
    }

    /**
     * {@code openclaw doctor ...}。
     *
 * @param args subcommand and flags,See {@link DoctorOptions}
     * @see <a href="https://docs.openclaw.ai/cli/doctor">doctor CLI</a>
     */
    public OpenClawCliResult doctor(DoctorOptions args) {
        return run("doctor", args);
    }

    /**
     * {@code openclaw logs ...}。
     *
 * @param args subcommand and flags,See {@link LogsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/logs">logs CLI</a>
     */
    public OpenClawCliResult logs(LogsOptions args) {
        return run("logs", args);
    }

    // --- Config & setup ---

    /**
     * {@code openclaw config ...}。
 * <p>example:{@code config(ConfigOptions.builder.tail("get", "gateway.mode").build)}</p>
     *
 * @param args subcommand and flags,See {@link ConfigOptions}
     * @see <a href="https://docs.openclaw.ai/cli/config">config CLI</a>
     */
    public OpenClawCliResult config(ConfigOptions args) {
        return run("config", args);
    }

    /**
     * {@code openclaw configure ...}。
     *
 * @param args subcommand and flags,See {@link ConfigureOptions}
     * @see <a href="https://docs.openclaw.ai/cli/configure">configure CLI</a>
     */
    public OpenClawCliResult configure(ConfigureOptions args) {
        return run("configure", args);
    }

    /**
     * {@code openclaw onboard ...}。
     *
 * @param args subcommand and flags,See {@link OnboardOptions}
     * @see <a href="https://docs.openclaw.ai/cli/onboard">onboard CLI</a>
     */
    public OpenClawCliResult onboard(OnboardOptions args) {
        return run("onboard", args);
    }

    // --- Agents & sessions & skills ---

    /**
     * {@code openclaw agent ...}。
 * <p> {@link AgentOptions} :{@code --message} Required;{@code --to} / {@code --session-id} / {@code --agent} ;
 * Optional {@link ThinkingLevel},{@link VerboseLevel},{@link AgentOptions.Builder#timeoutSeconds(int)} .</p>
 * <p>example:{@code agent(AgentOptions.builder.agent("ops").message("Summarize logs").build)}</p>
     *
 * @param args subcommand and flags,See {@link AgentOptions}
     * @see <a href="https://docs.openclaw.ai/cli/agent">agent CLI</a>
     */
    public OpenClawCliResult agent(AgentOptions args) {
        return run("agent", args);
    }

    /**
     * {@code openclaw agents ...}。
     *
 * @param args subcommand and flags,See {@link AgentsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/agents">agents CLI</a>
     */
    public OpenClawCliResult agents(AgentsOptions args) {
        return run("agents", args);
    }

    /**
     * {@code openclaw sessions ...}。
     *
 * @param args subcommand and flags,See {@link SessionsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/sessions">sessions CLI</a>
     */
    public OpenClawCliResult sessions(SessionsOptions args) {
        return run("sessions", args);
    }

    /**
     * {@code openclaw skills ...}。
     *
 * @param args subcommand and flags,See {@link SkillsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/skills">skills CLI</a>
     */
    public OpenClawCliResult skills(SkillsOptions args) {
        return run("skills", args);
    }

    /**
     * {@code openclaw approvals ...}。
     *
 * @param args subcommand and flags,See {@link ApprovalsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/approvals">approvals CLI</a>
     */
    public OpenClawCliResult approvals(ApprovalsOptions args) {
        return run("approvals", args);
    }

    // --- Channels & messaging & nodes ---

    /**
     * {@code openclaw channels ...}。
     *
 * @param args subcommand and flags,See {@link ChannelsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/channels">channels CLI</a>
     */
    public OpenClawCliResult channels(ChannelsOptions args) {
        return run("channels", args);
    }

    /**
     * {@code openclaw message ...}。
     *
 * @param args subcommand and flags,See {@link MessageOptions}
     * @see <a href="https://docs.openclaw.ai/cli/message">message CLI</a>
     */
    public OpenClawCliResult message(MessageOptions args) {
        return run("message", args);
    }

    /**
     * {@code openclaw pairing ...}。
     *
 * @param args subcommand and flags,See {@link PairingOptions}
     * @see <a href="https://docs.openclaw.ai/cli/pairing">pairing CLI</a>
     */
    public OpenClawCliResult pairing(PairingOptions args) {
        return run("pairing", args);
    }

    /**
     * {@code openclaw qr ...}。
     *
 * @param args subcommand and flags,See {@link QrOptions}
     * @see <a href="https://docs.openclaw.ai/cli/qr">qr CLI</a>
     */
    public OpenClawCliResult qr(QrOptions args) {
        return run("qr", args);
    }

    /**
     * {@code openclaw node ...}。
     *
 * @param args subcommand and flags,See {@link NodeOptions}
     * @see <a href="https://docs.openclaw.ai/cli/node">node CLI</a>
     */
    public OpenClawCliResult node(NodeOptions args) {
        return run("node", args);
    }

    /**
     * {@code openclaw nodes ...}。
     *
 * @param args subcommand and flags,See {@link NodesOptions}
     * @see <a href="https://docs.openclaw.ai/cli/nodes">nodes CLI</a>
     */
    public OpenClawCliResult nodes(NodesOptions args) {
        return run("nodes", args);
    }

    /**
     * {@code openclaw devices ...}。
     *
 * @param args subcommand and flags,See {@link DevicesOptions}
     * @see <a href="https://docs.openclaw.ai/cli/devices">devices CLI</a>
     */
    public OpenClawCliResult devices(DevicesOptions args) {
        return run("devices", args);
    }

    // --- Browser & MCP & tools ---

    /**
     * {@code openclaw mcp ...}。
     *
 * @param args subcommand and flags,See {@link McpOptions}
     * @see <a href="https://docs.openclaw.ai/cli/mcp">mcp CLI</a>
     */
    public OpenClawCliResult mcp(McpOptions args) {
        return run("mcp", args);
    }

    /**
     * {@code openclaw plugins ...}。
     *
 * @param args subcommand and flags,See {@link PluginsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/plugins">plugins CLI</a>
     */
    public OpenClawCliResult plugins(PluginsOptions args) {
        return run("plugins", args);
    }

    // --- Automation & webhooks & cron ---

    /**
     * {@code openclaw cron ...}。
     *
 * @param args subcommand and flags,See {@link CronOptions}
     * @see <a href="https://docs.openclaw.ai/cli/cron">cron CLI</a>
     */
    public OpenClawCliResult cron(CronOptions args) {
        return run("cron", args);
    }

    /**
     * {@code openclaw hooks ...}。
     *
 * @param args subcommand and flags,See {@link HooksOptions}
     * @see <a href="https://docs.openclaw.ai/cli/hooks">hooks CLI</a>
     */
    public OpenClawCliResult hooks(HooksOptions args) {
        return run("hooks", args);
    }

    /**
 * Task flow subcommand(documentationCorresponds to {@code openclaw tasks flow ...},See {@link FlowsOptions}).
     *
 * @param args subcommand and flags,See {@link FlowsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/flows">flows CLI</a>
     */
    public OpenClawCliResult flows(FlowsOptions args) {
        return run("tasks", args);
    }

    // --- Models & security & misc ---

    /**
     * {@code openclaw models ...}。
     *
 * @param args subcommand and flags,See {@link ModelsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/models">models CLI</a>
     */
    public OpenClawCliResult models(ModelsOptions args) {
        return run("models", args);
    }

    /**
     * {@code openclaw security ...}。
     *
 * @param args subcommand and flags,See {@link SecurityOptions}
     * @see <a href="https://docs.openclaw.ai/cli/security">security CLI</a>
     */
    public OpenClawCliResult security(SecurityOptions args) {
        return run("security", args);
    }

    /**
     * {@code openclaw secrets ...}。
     *
 * @param args subcommand and flags,See {@link SecretsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/secrets">secrets CLI</a>
     */
    public OpenClawCliResult secrets(SecretsOptions args) {
        return run("secrets", args);
    }

    /**
     * {@code openclaw backup ...}。
     *
 * @param args subcommand and flags,See {@link BackupOptions}
     * @see <a href="https://docs.openclaw.ai/cli/backup">backup CLI</a>
     */
    public OpenClawCliResult backup(BackupOptions args) {
        return run("backup", args);
    }

    /**
     * {@code openclaw update ...}。
     *
 * @param args subcommand and flags,See {@link UpdateOptions}
     * @see <a href="https://docs.openclaw.ai/cli/update">update CLI</a>
     */
    public OpenClawCliResult update(UpdateOptions args) {
        return run("update", args);
    }

    /**
     * {@code openclaw uninstall ...}。
     *
 * @param args subcommand and flags,See {@link UninstallOptions}
     * @see <a href="https://docs.openclaw.ai/cli/uninstall">uninstall CLI</a>
     */
    public OpenClawCliResult uninstall(UninstallOptions args) {
        return run("uninstall", args);
    }

    /**
     * {@code openclaw reset ...}。
     *
 * @param args subcommand and flags,See {@link ResetOptions}
     * @see <a href="https://docs.openclaw.ai/cli/reset">reset CLI</a>
     */
    public OpenClawCliResult reset(ResetOptions args) {
        return run("reset", args);
    }

    /**
     * {@code openclaw completion ...}。
     *
 * @param args subcommand and flags,See {@link CompletionOptions}
     * @see <a href="https://docs.openclaw.ai/cli/completion">completion CLI</a>
     */
    public OpenClawCliResult completion(CompletionOptions args) {
        return run("completion", args);
    }

    /**
     * {@code openclaw tui ...}。
     *
 * @param args subcommand and flags,See {@link TuiOptions}
     * @see <a href="https://docs.openclaw.ai/cli/tui">tui CLI</a>
     */
    public OpenClawCliResult tui(TuiOptions args) {
        return run("tui", args);
    }

    /**
     * {@code openclaw dashboard ...}。
     *
 * @param args subcommand and flags,See {@link DashboardOptions}
     * @see <a href="https://docs.openclaw.ai/cli/dashboard">dashboard CLI</a>
     */
    public OpenClawCliResult dashboard(DashboardOptions args) {
        return run("dashboard", args);
    }

    /**
     * {@code openclaw directory ...}。
     *
 * @param args subcommand and flags,See {@link DirectoryOptions}
     * @see <a href="https://docs.openclaw.ai/cli/directory">directory CLI</a>
     */
    public OpenClawCliResult directory(DirectoryOptions args) {
        return run("directory", args);
    }

    /**
     * {@code openclaw system ...}。
     *
 * @param args subcommand and flags,See {@link SystemOptions}
     * @see <a href="https://docs.openclaw.ai/cli/system">system CLI</a>
     */
    public OpenClawCliResult system(SystemOptions args) {
        return run("system", args);
    }

    /**
     * {@code openclaw acp ...}。
     *
 * @param args subcommand and flags,See {@link AcpOptions}
     * @see <a href="https://docs.openclaw.ai/cli/acp">acp CLI</a>
     */
    public OpenClawCliResult acp(AcpOptions args) {
        return run("acp", args);
    }

    /**
 * {@code openclaw chat ...}({@code tui --local} ).
     *
 * @param args subcommand and flags,See {@link ChatOptions}
     * @see <a href="https://docs.openclaw.ai/cli/chat">chat CLI</a>
     */
    public OpenClawCliResult chat(ChatOptions args) {
        return run("chat", args);
    }

    /**
 * {@code openclaw terminal ...}({@code tui --local} ).
     *
 * @param args subcommand and flags,See {@link TerminalOptions}
     * @see <a href="https://docs.openclaw.ai/cli/terminal">terminal CLI</a>
     */
    public OpenClawCliResult terminal(TerminalOptions args) {
        return run("terminal", args);
    }

    /**
     * {@code openclaw commitments ...}。
     *
 * @param args subcommand and flags,See {@link CommitmentsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/commitments">commitments CLI</a>
     */
    public OpenClawCliResult commitments(CommitmentsOptions args) {
        return run("commitments", args);
    }

    /**
     * {@code openclaw crestodian ...}。
     *
 * @param args subcommand and flags,See {@link CrestodianOptions}
     * @see <a href="https://docs.openclaw.ai/cli/crestodian">crestodian CLI</a>
     */
    public OpenClawCliResult crestodian(CrestodianOptions args) {
        return run("crestodian", args);
    }

    /**
 * {@code openclaw exec-approvals ...}( {@code approvals}).
     *
 * @param args subcommand and flags,See {@link ExecApprovalsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/exec-approvals">exec-approvals CLI</a>
     */
    public OpenClawCliResult execApprovals(ExecApprovalsOptions args) {
        return run("exec-approvals", args);
    }

    /**
     * {@code openclaw exec-policy ...}。
     *
 * @param args subcommand and flags,See {@link ExecPolicyOptions}
     * @see <a href="https://docs.openclaw.ai/cli/exec-policy">exec-policy CLI</a>
     */
    public OpenClawCliResult execPolicy(ExecPolicyOptions args) {
        return run("exec-policy", args);
    }

    /**
     * {@code openclaw migrate ...}。
     *
 * @param args subcommand and flags,See {@link MigrateOptions}
     * @see <a href="https://docs.openclaw.ai/cli/migrate">migrate CLI</a>
     */
    public OpenClawCliResult migrate(MigrateOptions args) {
        return run("migrate", args);
    }

    /**
     * {@code openclaw proxy ...}。
     *
 * @param args subcommand and flags,See {@link ProxyOptions}
     * @see <a href="https://docs.openclaw.ai/cli/proxy">proxy CLI</a>
     */
    public OpenClawCliResult proxy(ProxyOptions args) {
        return run("proxy", args);
    }

    /**
     * {@code openclaw worktrees ...}。
     *
 * @param args subcommand and flags,See {@link WorktreesOptions}
     * @see <a href="https://docs.openclaw.ai/cli/worktrees">worktrees CLI</a>
     */
    public OpenClawCliResult worktrees(WorktreesOptions args) {
        return run("worktrees", args);
    }

    /**
 * {@code openclaw tool ...}().
     *
 * @param args subcommand and flags,See {@link ToolOptions}
     * @see <a href="https://docs.openclaw.ai/cli/tool">tool CLI</a>
     */
    public OpenClawCliResult tool(ToolOptions args) {
        return run("tool", args);
    }

    /**
 * {@code openclaw tools ...}(help).
     *
 * @param args subcommand and flags,See {@link ToolsOptions}
     * @see <a href="https://docs.openclaw.ai/cli/tools">tools CLI</a>
     */
    public OpenClawCliResult tools(ToolsOptions args) {
        return run("tools", args);
    }

    /**
 * {@link OpenClawCliRequest}( {@code --dev},{@code --profile} ).
     *
 * @param request (argument list)
     * @see <a href="https://docs.openclaw.ai/cli">CLI Reference</a>
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
