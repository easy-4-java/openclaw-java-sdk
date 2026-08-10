package io.github.easy4j.openclaw.api.sse;

import io.github.easy4j.openclaw.api.model.ChatChunk;
import io.github.easy4j.openclaw.api.model.ChatChunk.DeltaChoice;
import io.github.easy4j.openclaw.api.model.ChatChunk.DeltaMessage;
import io.github.easy4j.openclaw.api.model.ChatMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Chat Completions 增量累加器，按 choice 与 tool-call 索引拼接文本、函数名和分片参数，生成最终 ChatChunk。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class SseEventAccumulator {

    /**
     * `SseEventAccumulator` 生命周期内保存的 `id` 对应状态。
     */
    private String id;
    /**
     * 模型标识。
     */
    private String model;
    /**
     * `SseEventAccumulator` 生命周期内保存的 `role` 对应状态。
     */
    private String role;
    /**
     * `SseEventAccumulator` 生命周期内保存的 `contentBuilder` 对应状态。
     */
    private final StringBuilder contentBuilder = new StringBuilder();
    /**
     * `SseEventAccumulator` 生命周期内保存的 `toolParts` 对应状态。
     */
    private final Map<Integer, ToolPart> toolParts = new LinkedHashMap<>();
    /**
     * `SseEventAccumulator` 生命周期内保存的 `finishReason` 对应状态。
     */
    private String finishReason;

    /**
     * 按 choice 和 tool-call 索引合并一个增量 ChatChunk，保留已接收片段的顺序。
     *
     * @param chunk 写入 `chunk` 协议字段的内容
     */
    public void merge(ChatChunk chunk) {
        if (chunk.getId() != null) {
            this.id = chunk.getId();
        }
        if (chunk.getModel() != null) {
            this.model = chunk.getModel();
        }
        if (chunk.getChoices() == null || chunk.getChoices().isEmpty()) {
            return;
        }

        DeltaChoice choice = chunk.getChoices().get(0);
        DeltaMessage delta = choice.getDelta();
        if (choice.getFinishReason() != null) {
            this.finishReason = choice.getFinishReason();
        }
        if (delta == null) {
            return;
        }

        if (delta.getRole() != null) {
            this.role = delta.getRole();
        }
        if (delta.getContent() != null) {
            contentBuilder.append(delta.getContent());
        }

        if (delta.getToolCalls() != null) {
            for (int i = 0; i < delta.getToolCalls().size(); i++) {
                ChatMessage.ToolCall tc = delta.getToolCalls().get(i);
                ToolPart tp = toolParts.computeIfAbsent(i, k -> new ToolPart());
                tp.merge(tc);
            }
        }
    }

    /**
     * 读取当前对象保存的 `accumulated` 对应状态，不触发网络或子进程调用。
     *
     * @return 按当前参数创建、查询或解析得到的 ChatChunk
     */
    public ChatChunk getAccumulated() {
        ChatChunk result = new ChatChunk();
        result.setId(id);
        result.setModel(model);

        DeltaMessage delta = new DeltaMessage();
        delta.setRole(role);
        delta.setContent(contentBuilder.length() > 0 ? contentBuilder.toString() : null);

        if (!toolParts.isEmpty()) {
            List<ChatMessage.ToolCall> merged = new ArrayList<>();
            for (ToolPart tp : toolParts.values()) {
                merged.add(tp.build());
            }
            delta.setToolCalls(merged);
        }

        DeltaChoice choice = new DeltaChoice();
        choice.setIndex(0);
        choice.setDelta(delta);
        choice.setFinishReason(finishReason);

        result.setChoices(java.util.Collections.singletonList(choice));
        return result;
    }

    /**
     * 判断 `toolCall` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isToolCall() { return "tool_calls".equals(finishReason); }
    /**
     * 判断 `complete` 对应状态 是否满足协议或生命周期条件。
     *
     * @return 条件成立返回 {@code true}，否则返回 {@code false}
     */
    public boolean isComplete() { return finishReason != null; }
    /**
     * 读取当前对象保存的 `accumulatedContent` 对应状态，不触发网络或子进程调用。
     *
     * @return 服务返回或流式累积得到的文本
     */
    public String getAccumulatedContent() { return contentBuilder.toString(); }

    /**
     * 清空已累积文本、工具调用和完成状态，使累加器可用于新的流。
     */
    public void reset() {
        id = null; model = null; role = null;
        contentBuilder.setLength(0);
        toolParts.clear();
        finishReason = null;
    }

    /**
     * OpenClaw SDK 的 `ToolPart` 类型，封装其公开契约和生命周期边界。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static class ToolPart {
        /**
         * `ToolPart` 生命周期内保存的 `id` 对应状态。
         */
        private String id, type, name;
        /**
         * `ToolPart` 生命周期内保存的 `arguments` 对应状态。
         */
        private final StringBuilder arguments = new StringBuilder();

        void merge(ChatMessage.ToolCall tc) {
            if (tc.getId() != null) {
                this.id = tc.getId();
            }
            if (tc.getType() != null) {
                this.type = tc.getType();
            }
            if (tc.getFunction() != null) {
                if (tc.getFunction().getName() != null) this.name = tc.getFunction().getName();
                if (tc.getFunction().getArguments() != null) this.arguments.append(tc.getFunction().getArguments());
            }
        }

        ChatMessage.ToolCall build() {
            ChatMessage.FunctionCall fn = new ChatMessage.FunctionCall();
            fn.setName(name);
            fn.setArguments(arguments.toString());

            ChatMessage.ToolCall tc = new ChatMessage.ToolCall();
            tc.setId(id);
            tc.setType(type != null ? type : "function");
            tc.setFunction(fn);
            return tc;
        }
    }
}
