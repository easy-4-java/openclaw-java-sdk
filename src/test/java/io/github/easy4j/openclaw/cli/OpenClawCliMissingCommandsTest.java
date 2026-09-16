/*
 * Copyright (c) 2018-present, easy-4-java (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;
import io.github.easy4j.openclaw.cli.opts.DnsOptions;
import io.github.easy4j.openclaw.cli.opts.FleetOptions;
import io.github.easy4j.openclaw.cli.opts.MemoryOptions;
import io.github.easy4j.openclaw.cli.opts.PathOptions;
import io.github.easy4j.openclaw.cli.opts.PromosOptions;
import io.github.easy4j.openclaw.cli.opts.SandboxOptions;
import io.github.easy4j.openclaw.cli.opts.SetupOptions;
import io.github.easy4j.openclaw.cli.opts.SubcommandOptions;
import io.github.easy4j.openclaw.cli.opts.TasksOptions;
import io.github.easy4j.openclaw.cli.opts.TranscriptsOptions;
import io.github.easy4j.openclaw.cli.opts.WebhooksOptions;
import io.github.easy4j.openclaw.cli.opts.WorkboardOptions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CLI 参考对齐补充命令的参数装配契约测试：以记录型执行器捕获
 * {@link OpenClawCliRequest}，断言每个新命令的参数序列与文档一致。
 *
 * @since 1.0.0
 */
class OpenClawCliMissingCommandsTest {

    /** 记录最近一次请求参数的桩执行器。 */
    private static final class RecordingExecutor extends OpenClawCliExecutor {

        private List<String> lastArguments = Collections.emptyList();

        RecordingExecutor(OpenClawCliConfig config) {
            super(config);
        }

        @Override
        public OpenClawCliResult execute(OpenClawCliRequest request) {
            this.lastArguments = request.getArguments();
            return new OpenClawCliResult(0, "ok", "");
        }
    }

    private RecordingExecutor executor = new RecordingExecutor(new OpenClawCliConfig());
    private OpenClawCli cli = new OpenClawCli(executor);

    private void assertArgs(String... expected) {
        List<String> actual = new ArrayList<String>(executor.lastArguments);
        List<String> expectedList = new ArrayList<String>();
        Collections.addAll(expectedList, expected);
        assertEquals(expectedList, actual);
    }

    @Test
    void shouldAssemblePassthroughCommandsWithEmptyArguments() {
        cli.attach(CliSubArgs.empty());
        assertArgs("attach");
        cli.audit(CliSubArgs.empty());
        assertArgs("audit");
        cli.claws(CliSubArgs.empty());
        assertArgs("claws");
        cli.connect(CliSubArgs.empty());
        assertArgs("connect");
        cli.docs(CliSubArgs.empty());
        assertArgs("docs");
        cli.policy(CliSubArgs.empty());
        assertArgs("policy");
        cli.resume(CliSubArgs.empty());
        assertArgs("resume");
        cli.triage(CliSubArgs.empty());
        assertArgs("triage");
        cli.voicecall(CliSubArgs.empty());
        assertArgs("voicecall");
        cli.worker(CliSubArgs.empty());
        assertArgs("worker");
        cli.fileTransfer(CliSubArgs.empty());
        assertArgs("file-transfer");
    }

    @Test
    void shouldAssembleDnsSetup() {
        cli.dns(DnsOptions.builder().setup(true).build());
        assertArgs("dns", "setup");
    }

    @Test
    void shouldAssembleFleetVerbs() {
        cli.fleet(FleetOptions.builder().verb(FleetOptions.Verb.RESTART).build());
        assertArgs("fleet", "restart");
        cli.fleet(FleetOptions.builder().verb(FleetOptions.Verb.RM).build());
        assertArgs("fleet", "rm");
    }

    @Test
    void shouldAssembleInferAndBrowserAndWikiSubTrees() {
        cli.infer(SubcommandOptions.builder().sub("model").arguments("run", "flux-pro").build());
        assertArgs("infer", "model", "run", "flux-pro");

        cli.browser(SubcommandOptions.builder().sub("navigate").argument("https://example.com").build());
        assertArgs("browser", "navigate", "https://example.com");

        cli.wiki(SubcommandOptions.builder().sub("status").build());
        assertArgs("wiki", "status");
    }

    @Test
    void shouldAssembleMemoryCommands() {
        cli.memoryStatus();
        assertArgs("memory", "status");
        cli.memoryIndex();
        assertArgs("memory", "index");
        cli.memorySearch("部署清单");
        assertArgs("memory", "search", "部署清单");
    }

    @Test
    void shouldAssemblePathVerbs() {
        cli.path(PathOptions.builder().verb(PathOptions.Verb.RESOLVE).build());
        assertArgs("path", "resolve");
        cli.path(PathOptions.builder().verb(PathOptions.Verb.SET).build());
        assertArgs("path", "set");
    }

    @Test
    void shouldAssemblePromosCommands() {
        cli.promosList();
        assertArgs("promos", "list");
        cli.promosClaim("launch-week");
        assertArgs("promos", "claim", "launch-week");
    }

    @Test
    void shouldAssembleSandboxVerbs() {
        cli.sandbox(SandboxOptions.builder().verb(SandboxOptions.Verb.LIST).build());
        assertArgs("sandbox", "list");
        cli.sandbox(SandboxOptions.builder().verb(SandboxOptions.Verb.RECREATE).build());
        assertArgs("sandbox", "recreate");
        cli.sandbox(SandboxOptions.builder().verb(SandboxOptions.Verb.EXPLAIN).build());
        assertArgs("sandbox", "explain");
    }

    @Test
    void shouldAssembleSetupVariants() {
        cli.setup();
        assertArgs("setup");
        cli.setupBaseline();
        assertArgs("setup", "--baseline");
    }

    @Test
    void shouldAssembleTasksVerbs() {
        cli.tasks(TasksOptions.builder().verb(TasksOptions.Verb.LIST).build());
        assertArgs("tasks", "list");
        cli.tasks(TasksOptions.builder().verb(TasksOptions.Verb.SHOW).operand("t-1").build());
        assertArgs("tasks", "show", "t-1");
        cli.tasks(TasksOptions.builder().verb(TasksOptions.Verb.FLOW).build());
        assertArgs("tasks", "flow");
    }

    @Test
    void shouldAssembleTranscriptsVerbs() {
        cli.transcriptsList();
        assertArgs("transcripts", "list");
        cli.transcriptsShow("tr-7");
        assertArgs("transcripts", "show", "tr-7");
        cli.transcriptsPath();
        assertArgs("transcripts", "path");
    }

    @Test
    void shouldAssembleWebhooksGmailVerbs() {
        cli.webhooksGmailSetup();
        assertArgs("webhooks", "gmail", "setup");
        cli.webhooksGmailRun();
        assertArgs("webhooks", "gmail", "run");
    }

    @Test
    void shouldAssembleWorkboardCommands() {
        cli.workboardList();
        assertArgs("workboard", "list");
        cli.workboardCreate(CliSubArgs.empty());
        assertArgs("workboard", "create");
        cli.workboardShow("wb-3");
        assertArgs("workboard", "show", "wb-3");
        cli.workboardDispatch("wb-3");
        assertArgs("workboard", "dispatch", "wb-3");
    }

    @Test
    void shouldKeepArgumentsInOriginalOrder() {
        cli.infer(SubcommandOptions.builder()
                .sub("tts").arguments("convert", "--voice", "alloy").build());
        assertArgs("infer", "tts", "convert", "--voice", "alloy");
        assertTrue(executor.lastArguments.indexOf("--voice") < executor.lastArguments.indexOf("alloy"));
    }
}
