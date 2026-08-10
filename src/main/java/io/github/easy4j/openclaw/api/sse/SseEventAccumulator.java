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
     * 流式聊天响应标识；首次出现后供后续片段复用。
     */
    private String id;
    /**
     * 模型标识。
     */
    private String model;
    /**
     * 流式消息累计得到的聊天角色。
     */
    private String role;
    /**
     * 按事件顺序追加文本增量的缓冲区。
     */
    private final StringBuilder contentBuilder = new StringBuilder();
    /**
     * 按工具调用索引保存的分片状态，用于跨事件拼接参数。
     */
    private final Map<Integer, ToolPart> toolParts = new LinkedHashMap<>();
    /**
     * 服务端报告的流式生成结束原因。
     */
    private String finishReason;

    /**
     * 按 choice 和 tool-call 索引合并一个增量 ChatChunk，保留已接收片段的顺序。
     *
     * @param chunk 本次流式响应片段
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
     * 返回由当前所有 SSE 片段组装出的聊天消息。
     *
     * @return 由当前全部 SSE 片段合并得到的聊天片段
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
     * 判断流是否以工具调用原因结束。
     *
     * @return 累积结果因工具调用结束时返回 {@code true}
     */
    public boolean isToolCall() { return "tool_calls".equals(finishReason); }
    /**
     * 判断服务端是否已给出任意结束原因。
     *
     * @return 已接收到结束原因时返回 {@code true}
     */
    public boolean isComplete() { return finishReason != null; }
    /**
     * 返回按事件顺序拼接的完整文本内容。
     *
     * @return 按接收顺序拼接所有文本增量后的内容
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
     * 跨 SSE 片段暂存单个工具调用标识、名称和参数 JSON 的累加状态。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static class ToolPart {
        /**
         * 工具调用标识；可能跨多个 SSE 片段到达。
         */
        private String id, type, name;
        /**
         * 按 SSE 到达顺序拼接的工具参数 JSON 缓冲区。
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
