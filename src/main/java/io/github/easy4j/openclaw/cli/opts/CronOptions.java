package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw {@code cron} 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CronOptions implements CliSubArgs {

    /**
     * 定义计划任务动作允许的固定取值及其 CLI/JSON 序列化拼写。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示计划任务动作的 {@code run} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RUN,
        /**
         * 表示计划任务动作的 {@code runs} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        RUNS,
        /**
         * 表示计划任务动作的 {@code add} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        ADD,
        /**
         * 表示计划任务动作的 {@code edit} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        EDIT,
        /**
         * 表示计划任务动作的 {@code list} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        LIST,
        /**
         * 表示计划任务动作的 {@code delete} 取值；写入 CLI 或 JSON 时保持该固定拼写。
         */
        DELETE
    }

    /**
     * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
     */
    private final Verb verb;
    /**
     * 目标计划任务标识；未设置时命令行不包含 {@code --job-id}。
     */
    private final String jobId;
    /**
     * 是否向 openclaw 子命令追加 {@code --run-due} 开关。
     */
    private final boolean runDue;
    /**
     * 待查询运行记录的计划任务标识；未设置时命令行不包含 {@code --runs-id}。
     */
    private final String runsId;
    /**
     * 返回计划任务运行记录的数量上限；未设置时命令行不包含 {@code --runs-limit}。
     */
    private final Integer runsLimit;
    /**
     * 目标资源名称；未设置时命令行不包含 {@code --name}。
     */
    private final String name;
    /**
     * 计划任务的 Cron 表达式；未设置时命令行不包含 {@code --cron-expr}。
     */
    private final String cronExpr;
    /**
     * 目标会话标识；未设置时命令行不包含 {@code --session}。
     */
    private final String session;
    /**
     * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
     */
    private final String message;
    /**
     * 计划任务的一次性执行时间；未设置时命令行不包含 {@code --at}。
     */
    private final String at;
    /**
     * Cron 调度使用的时区；未设置时命令行不包含 {@code --tz}。
     */
    private final String tz;
    /**
     * 是否向 openclaw 子命令追加 {@code --keep-after-run} 开关。
     */
    private final boolean keepAfterRun;
    /**
     * 是否向 openclaw 子命令追加 {@code --announce} 开关。
     */
    private final boolean announce;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-deliver} 开关。
     */
    private final boolean noDeliver;
    /**
     * 是否向 openclaw 子命令追加 {@code --light-context} 开关。
     */
    private final boolean lightContext;
    /**
     * 目标消息通道；未设置时命令行不包含 {@code --channel}。
     */
    private final String channel;
    /**
     * 消息投递目标；未设置时命令行不包含 {@code --to}。
     */
    private final String to;
    /**
     * 目标模型标识；未设置时命令行不包含 {@code --model}。
     */
    private final String model;
    /**
     * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
     */
    private final String agent;
    /**
     * 是否向 openclaw 子命令追加 {@code --clear-agent} 开关。
     */
    private final boolean clearAgent;
    /**
     * 是否向 openclaw 子命令追加 {@code --best-effort-deliver} 开关。
     */
    private final Boolean bestEffortDeliver;
    /**
     * 是否向 openclaw 子命令追加 {@code --no-best-effort-deliver} 开关。
     */
    private final Boolean noBestEffortDeliver;
    /**
     * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private CronOptions(Builder b) {
        this.verb = b.verb;
        this.jobId = b.jobId;
        this.runDue = b.runDue;
        this.runsId = b.runsId;
        this.runsLimit = b.runsLimit;
        this.name = b.name;
        this.cronExpr = b.cronExpr;
        this.session = b.session;
        this.message = b.message;
        this.at = b.at;
        this.tz = b.tz;
        this.keepAfterRun = b.keepAfterRun;
        this.announce = b.announce;
        this.noDeliver = b.noDeliver;
        this.lightContext = b.lightContext;
        this.channel = b.channel;
        this.to = b.to;
        this.model = b.model;
        this.agent = b.agent;
        this.clearAgent = b.clearAgent;
        this.bestEffortDeliver = b.bestEffortDeliver;
        this.noBestEffortDeliver = b.noBestEffortDeliver;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 {@code CronOptions} 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        switch (verb) {
            case RUN:
                out.add("run");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                OpenClawCliArgv.addFlag(out, "--due", runDue);
                break;
            case RUNS:
                out.add("runs");
                OpenClawCliArgv.addIfPresent(out, "--id", runsId);
                OpenClawCliArgv.addIfNotNull(out, "--limit", runsLimit);
                break;
            case ADD:
                out.add("add");
                appendAddEditFlags(out, false);
                break;
            case EDIT:
                out.add("edit");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                appendAddEditFlags(out, true);
                break;
            case LIST:
                out.add("list");
                break;
            case DELETE:
                out.add("delete");
                if (jobId != null && OpenClawStrings.isNotBlank(jobId)) {
                    out.add(jobId.trim());
                }
                break;
            default:
                break;
        }
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    private void appendAddEditFlags(List<String> out, boolean edit) {
        OpenClawCliArgv.addIfPresent(out, "--name", name);
        OpenClawCliArgv.addIfPresent(out, "--cron", cronExpr);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--message", message);
        OpenClawCliArgv.addIfPresent(out, "--at", at);
        OpenClawCliArgv.addIfPresent(out, "--tz", tz);
        OpenClawCliArgv.addFlag(out, "--keep-after-run", keepAfterRun);
        OpenClawCliArgv.addFlag(out, "--announce", announce);
        OpenClawCliArgv.addFlag(out, "--light-context", lightContext);
        OpenClawCliArgv.addFlag(out, "--no-deliver", noDeliver);
        OpenClawCliArgv.addIfPresent(out, "--channel", channel);
        OpenClawCliArgv.addIfPresent(out, "--to", to);
        OpenClawCliArgv.addIfPresent(out, "--model", model);
        OpenClawCliArgv.addIfPresent(out, "--agent", agent);
        OpenClawCliArgv.addFlag(out, "--clear-agent", clearAgent);
        if (Boolean.TRUE.equals(bestEffortDeliver)) {
            out.add("--best-effort-deliver");
        }
        if (Boolean.TRUE.equals(noBestEffortDeliver)) {
            out.add("--no-best-effort-deliver");
        }
    }

    /**
     * {@code CronOptions} 的可变构建器；链式方法记录参数，{@code build()} 生成不再受后续修改影响的对象。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 当前选项对象要执行的子命令动作；未设置时命令行不包含 {@code --verb}。
         */
        private Verb verb = Verb.LIST;
        /**
         * 目标计划任务标识；未设置时命令行不包含 {@code --job-id}。
         */
        private String jobId;
        /**
         * 是否向 openclaw 子命令追加 {@code --run-due} 开关。
         */
        private boolean runDue;
        /**
         * 待查询运行记录的计划任务标识；未设置时命令行不包含 {@code --runs-id}。
         */
        private String runsId;
        /**
         * 返回计划任务运行记录的数量上限；未设置时命令行不包含 {@code --runs-limit}。
         */
        private Integer runsLimit;
        /**
         * 目标资源名称；未设置时命令行不包含 {@code --name}。
         */
        private String name;
        /**
         * 计划任务的 Cron 表达式；未设置时命令行不包含 {@code --cron-expr}。
         */
        private String cronExpr;
        /**
         * 目标会话标识；未设置时命令行不包含 {@code --session}。
         */
        private String session;
        /**
         * 待发送的消息正文；未设置时命令行不包含 {@code --message}。
         */
        private String message;
        /**
         * 计划任务的一次性执行时间；未设置时命令行不包含 {@code --at}。
         */
        private String at;
        /**
         * Cron 调度使用的时区；未设置时命令行不包含 {@code --tz}。
         */
        private String tz;
        /**
         * 是否向 openclaw 子命令追加 {@code --keep-after-run} 开关。
         */
        private boolean keepAfterRun;
        /**
         * 是否向 openclaw 子命令追加 {@code --announce} 开关。
         */
        private boolean announce;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-deliver} 开关。
         */
        private boolean noDeliver;
        /**
         * 是否向 openclaw 子命令追加 {@code --light-context} 开关。
         */
        private boolean lightContext;
        /**
         * 目标消息通道；未设置时命令行不包含 {@code --channel}。
         */
        private String channel;
        /**
         * 消息投递目标；未设置时命令行不包含 {@code --to}。
         */
        private String to;
        /**
         * 目标模型标识；未设置时命令行不包含 {@code --model}。
         */
        private String model;
        /**
         * 目标智能体标识；未设置时命令行不包含 {@code --agent}。
         */
        private String agent;
        /**
         * 是否向 openclaw 子命令追加 {@code --clear-agent} 开关。
         */
        private boolean clearAgent;
        /**
         * 是否向 openclaw 子命令追加 {@code --best-effort-deliver} 开关。
         */
        private Boolean bestEffortDeliver;
        /**
         * 是否向 openclaw 子命令追加 {@code --no-best-effort-deliver} 开关。
         */
        private Boolean noBestEffortDeliver;
        /**
         * 附加到 RPC 请求的原始参数；未设置时命令行不包含 {@code --extra}。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 {@code --run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 目标计划任务标识；作为 {@code --run} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run(String jobId) {
            this.verb = Verb.RUN;
            this.jobId = jobId;
            return this;
        }

        /**
         * 设置 {@code --run-due} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param due 是否向命令行追加 {@code --run-due} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runDue(boolean due) {
            this.runDue = due;
            return this;
        }

        /**
         * 设置 {@code --runs} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 目标计划任务标识；作为 {@code --runs} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runs(String jobId) {
            this.verb = Verb.RUNS;
            this.runsId = jobId;
            return this;
        }

        /**
         * 设置 {@code --runs-limit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 返回结果数量上限；作为 {@code --runs-limit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runsLimit(int limit) {
            this.runsLimit = limit;
            return this;
        }

        /**
         * 选择 {@code add} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
         * 设置 {@code --edit} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 目标计划任务标识；作为 {@code --edit} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder edit(String jobId) {
            this.verb = Verb.EDIT;
            this.jobId = jobId;
            return this;
        }

        /**
         * 选择 {@code list} 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 {@code --delete} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 目标计划任务标识；作为 {@code --delete} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder delete(String jobId) {
            this.verb = Verb.DELETE;
            this.jobId = jobId;
            return this;
        }

        /**
         * 设置 {@code --name} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 目标资源名称；作为 {@code --name} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * 设置 {@code --cron-expr} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expr Cron 调度表达式；作为 {@code --cron-expr} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cronExpr(String expr) {
            this.cronExpr = expr;
            return this;
        }

        /**
         * 设置 {@code --session} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 目标会话标识；作为 {@code --session} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) {
            this.session = session;
            return this;
        }

        /**
         * 设置 {@code --message} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置 {@code --at} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param at 计划任务的一次性执行时间；作为 {@code --at} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder at(String at) {
            this.at = at;
            return this;
        }

        /**
         * 设置 {@code --tz} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tz Cron 调度使用的时区；作为 {@code --tz} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tz(String tz) {
            this.tz = tz;
            return this;
        }

        /**
         * 设置 {@code --keep-after-run} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param keep 是否向命令行追加 {@code --keep-after-run} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder keepAfterRun(boolean keep) {
            this.keepAfterRun = keep;
            return this;
        }

        /**
         * 设置 {@code --announce} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param announce 是否向命令行追加 {@code --announce} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder announce(boolean announce) {
            this.announce = announce;
            return this;
        }

        /**
         * 设置 {@code --no-deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noDeliver 是否向命令行追加 {@code --no-deliver} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noDeliver(boolean noDeliver) {
            this.noDeliver = noDeliver;
            return this;
        }

        /**
         * 设置 {@code --light-context} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lightContext 是否向命令行追加 {@code --light-context} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder lightContext(boolean lightContext) {
            this.lightContext = lightContext;
            return this;
        }

        /**
         * 设置 {@code --channel} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 目标消息通道；作为 {@code --channel} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 {@code --to} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param to 消息投递目标；作为 {@code --to} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * 设置 {@code --model} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param model 模型标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置 {@code --agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 目标智能体标识；作为 {@code --agent} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 设置 {@code --clear-agent} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param clear 是否向命令行追加 {@code --clear-agent} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clearAgent(boolean clear) {
            this.clearAgent = clear;
            return this;
        }

        /**
         * 设置 {@code --best-effort-deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param v 是否向命令行追加 {@code --best-effort-deliver} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bestEffortDeliver(boolean v) {
            this.bestEffortDeliver = v ? Boolean.TRUE : null;
            return this;
        }

        /**
         * 设置 {@code --no-best-effort-deliver} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param v 是否向命令行追加 {@code --no-best-effort-deliver} 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noBestEffortDeliver(boolean v) {
            this.noBestEffortDeliver = v ? Boolean.TRUE : null;
            return this;
        }

        /**
         * 设置 {@code --extra} 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 原样追加到生成参数末尾的 CLI 参数列表；作为 {@code --extra} 的参数
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 {@code CronOptions}。
         *
         * @return 按当前字段创建的 CronOptions
         */
        public CronOptions build() {
            return new CronOptions(this);
        }
    }
}
