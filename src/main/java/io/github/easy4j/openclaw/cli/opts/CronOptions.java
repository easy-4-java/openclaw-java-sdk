package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.util.OpenClawStrings;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `cron` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class CronOptions implements CliSubArgs {

    /**
     * `Verb` 的有限协议取值集合；枚举常量会转换为 CLI 或 JSON 接受的固定值。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 选择 `run` 协议模式；序列化时使用该固定取值。
         */
        RUN,
        /**
         * 选择 `runs` 协议模式；序列化时使用该固定取值。
         */
        RUNS,
        /**
         * 选择 `add` 协议模式；序列化时使用该固定取值。
         */
        ADD,
        /**
         * 选择 `edit` 协议模式；序列化时使用该固定取值。
         */
        EDIT,
        /**
         * 选择 `list` 协议模式；序列化时使用该固定取值。
         */
        LIST,
        /**
         * 选择 `delete` 协议模式；序列化时使用该固定取值。
         */
        DELETE
    }

    /**
     * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
     */
    private final Verb verb;
    /**
     * 传给 openclaw 子命令 `--job-id` 选项的内容；为 null 时通常省略。
     */
    private final String jobId;
    /**
     * 是否向 openclaw 子命令追加 `--run-due` 开关。
     */
    private final boolean runDue;
    /**
     * 传给 openclaw 子命令 `--runs-id` 选项的内容；为 null 时通常省略。
     */
    private final String runsId;
    /**
     * 传给 openclaw 子命令 `--runs-limit` 选项的内容；为 null 时通常省略。
     */
    private final Integer runsLimit;
    /**
     * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
     */
    private final String name;
    /**
     * 传给 openclaw 子命令 `--cron-expr` 选项的内容；为 null 时通常省略。
     */
    private final String cronExpr;
    /**
     * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
     */
    private final String session;
    /**
     * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
     */
    private final String message;
    /**
     * 传给 openclaw 子命令 `--at` 选项的内容；为 null 时通常省略。
     */
    private final String at;
    /**
     * 传给 openclaw 子命令 `--tz` 选项的内容；为 null 时通常省略。
     */
    private final String tz;
    /**
     * 是否向 openclaw 子命令追加 `--keep-after-run` 开关。
     */
    private final boolean keepAfterRun;
    /**
     * 是否向 openclaw 子命令追加 `--announce` 开关。
     */
    private final boolean announce;
    /**
     * 是否向 openclaw 子命令追加 `--no-deliver` 开关。
     */
    private final boolean noDeliver;
    /**
     * 是否向 openclaw 子命令追加 `--light-context` 开关。
     */
    private final boolean lightContext;
    /**
     * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
     */
    private final String channel;
    /**
     * 传给 openclaw 子命令 `--to` 选项的内容；为 null 时通常省略。
     */
    private final String to;
    /**
     * 传给 openclaw 子命令 `--model` 选项的内容；为 null 时通常省略。
     */
    private final String model;
    /**
     * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
     */
    private final String agent;
    /**
     * 是否向 openclaw 子命令追加 `--clear-agent` 开关。
     */
    private final boolean clearAgent;
    /**
     * 是否向 openclaw 子命令追加 `--best-effort-deliver` 开关。
     */
    private final Boolean bestEffortDeliver;
    /**
     * 是否向 openclaw 子命令追加 `--no-best-effort-deliver` 开关。
     */
    private final Boolean noBestEffortDeliver;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
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
     * 创建空白构建器，供调用方链式设置 `CronOptions` 字段。
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
     * 链式构建器，逐项收集 CronOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 CronOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 传给 openclaw 子命令 `--verb` 选项的内容；为 null 时通常省略。
         */
        private Verb verb = Verb.LIST;
        /**
         * 传给 openclaw 子命令 `--job-id` 选项的内容；为 null 时通常省略。
         */
        private String jobId;
        /**
         * 是否向 openclaw 子命令追加 `--run-due` 开关。
         */
        private boolean runDue;
        /**
         * 传给 openclaw 子命令 `--runs-id` 选项的内容；为 null 时通常省略。
         */
        private String runsId;
        /**
         * 传给 openclaw 子命令 `--runs-limit` 选项的内容；为 null 时通常省略。
         */
        private Integer runsLimit;
        /**
         * 传给 openclaw 子命令 `--name` 选项的内容；为 null 时通常省略。
         */
        private String name;
        /**
         * 传给 openclaw 子命令 `--cron-expr` 选项的内容；为 null 时通常省略。
         */
        private String cronExpr;
        /**
         * 传给 openclaw 子命令 `--session` 选项的内容；为 null 时通常省略。
         */
        private String session;
        /**
         * 传给 openclaw 子命令 `--message` 选项的内容；为 null 时通常省略。
         */
        private String message;
        /**
         * 传给 openclaw 子命令 `--at` 选项的内容；为 null 时通常省略。
         */
        private String at;
        /**
         * 传给 openclaw 子命令 `--tz` 选项的内容；为 null 时通常省略。
         */
        private String tz;
        /**
         * 是否向 openclaw 子命令追加 `--keep-after-run` 开关。
         */
        private boolean keepAfterRun;
        /**
         * 是否向 openclaw 子命令追加 `--announce` 开关。
         */
        private boolean announce;
        /**
         * 是否向 openclaw 子命令追加 `--no-deliver` 开关。
         */
        private boolean noDeliver;
        /**
         * 是否向 openclaw 子命令追加 `--light-context` 开关。
         */
        private boolean lightContext;
        /**
         * 传给 openclaw 子命令 `--channel` 选项的内容；为 null 时通常省略。
         */
        private String channel;
        /**
         * 传给 openclaw 子命令 `--to` 选项的内容；为 null 时通常省略。
         */
        private String to;
        /**
         * 传给 openclaw 子命令 `--model` 选项的内容；为 null 时通常省略。
         */
        private String model;
        /**
         * 传给 openclaw 子命令 `--agent` 选项的内容；为 null 时通常省略。
         */
        private String agent;
        /**
         * 是否向 openclaw 子命令追加 `--clear-agent` 开关。
         */
        private boolean clearAgent;
        /**
         * 是否向 openclaw 子命令追加 `--best-effort-deliver` 开关。
         */
        private Boolean bestEffortDeliver;
        /**
         * 是否向 openclaw 子命令追加 `--no-best-effort-deliver` 开关。
         */
        private Boolean noBestEffortDeliver;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 写入 `--run` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder run(String jobId) {
            this.verb = Verb.RUN;
            this.jobId = jobId;
            return this;
        }

        /**
         * 设置 `--run-due` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param due 是否向命令行追加 `--run-due` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runDue(boolean due) {
            this.runDue = due;
            return this;
        }

        /**
         * 设置 `--runs` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 写入 `--runs` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runs(String jobId) {
            this.verb = Verb.RUNS;
            this.runsId = jobId;
            return this;
        }

        /**
         * 设置 `--runs-limit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param limit 写入 `--runs-limit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder runsLimit(int limit) {
            this.runsLimit = limit;
            return this;
        }

        /**
         * 选择 `add` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder add() {
            this.verb = Verb.ADD;
            return this;
        }

        /**
         * 设置 `--edit` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 写入 `--edit` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder edit(String jobId) {
            this.verb = Verb.EDIT;
            this.jobId = jobId;
            return this;
        }

        /**
         * 选择 `list` 命令动作或布尔开关，并返回当前构建器。
         *
         * @return 当前构建器，便于继续链式配置
         */
        public Builder list() {
            this.verb = Verb.LIST;
            return this;
        }

        /**
         * 设置 `--delete` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param jobId 写入 `--delete` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder delete(String jobId) {
            this.verb = Verb.DELETE;
            this.jobId = jobId;
            return this;
        }

        /**
         * 设置 `--name` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param name 写入 `--name` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * 设置 `--cron-expr` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param expr 写入 `--cron-expr` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder cronExpr(String expr) {
            this.cronExpr = expr;
            return this;
        }

        /**
         * 设置 `--session` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param session 写入 `--session` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder session(String session) {
            this.session = session;
            return this;
        }

        /**
         * 设置 `--message` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param message 消息正文
         * @return 当前构建器，便于继续链式配置
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置 `--at` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param at 写入 `--at` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder at(String at) {
            this.at = at;
            return this;
        }

        /**
         * 设置 `--tz` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tz 写入 `--tz` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder tz(String tz) {
            this.tz = tz;
            return this;
        }

        /**
         * 设置 `--keep-after-run` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param keep 是否向命令行追加 `--keep-after-run` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder keepAfterRun(boolean keep) {
            this.keepAfterRun = keep;
            return this;
        }

        /**
         * 设置 `--announce` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param announce 是否向命令行追加 `--announce` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder announce(boolean announce) {
            this.announce = announce;
            return this;
        }

        /**
         * 设置 `--no-deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noDeliver 是否向命令行追加 `--no-deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noDeliver(boolean noDeliver) {
            this.noDeliver = noDeliver;
            return this;
        }

        /**
         * 设置 `--light-context` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param lightContext 是否向命令行追加 `--light-context` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder lightContext(boolean lightContext) {
            this.lightContext = lightContext;
            return this;
        }

        /**
         * 设置 `--channel` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param channel 写入 `--channel` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 设置 `--to` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param to 写入 `--to` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * 设置 `--model` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param model 模型标识
         * @return 当前构建器，便于继续链式配置
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置 `--agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param agent 写入 `--agent` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        /**
         * 设置 `--clear-agent` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param clear 是否向命令行追加 `--clear-agent` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder clearAgent(boolean clear) {
            this.clearAgent = clear;
            return this;
        }

        /**
         * 设置 `--best-effort-deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param v 是否向命令行追加 `--best-effort-deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder bestEffortDeliver(boolean v) {
            this.bestEffortDeliver = v ? Boolean.TRUE : null;
            return this;
        }

        /**
         * 设置 `--no-best-effort-deliver` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param v 是否向命令行追加 `--no-best-effort-deliver` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noBestEffortDeliver(boolean v) {
            this.noBestEffortDeliver = v ? Boolean.TRUE : null;
            return this;
        }

        /**
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `CronOptions`。
         *
         * @return 按当前字段创建的 CronOptions
         */
        public CronOptions build() {
            return new CronOptions(this);
        }
    }
}
