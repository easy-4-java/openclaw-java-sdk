package io.github.easy4j.openclaw.api;

/**
 * OpenClaw Gateway 的端点路径、HTTP 请求头、SSE 标记和协议固定值定义。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class OpenClawConstants {

    private OpenClawConstants() {}

    // ==================== HTTP Endpoints ====================

    /**
     * Gateway {@code /v1/chat/completions} 端点路径。
     */
    public static final String ENDPOINT_CHAT_COMPLETIONS = "/v1/chat/completions";

    /**
     * Gateway {@code /v1/models} 端点路径。
     */
    public static final String ENDPOINT_MODELS = "/v1/models";

    /**
     * Gateway {@code /v1/embeddings} 端点路径。
     */
    public static final String ENDPOINT_EMBEDDINGS = "/v1/embeddings";

    /**
     * Gateway {@code /v1/responses} 端点路径。
     */
    public static final String ENDPOINT_RESPONSES = "/v1/responses";

    /**
     * Gateway {@code /tools/invoke} 端点路径。
     */
    public static final String ENDPOINT_TOOLS_INVOKE = "/tools/invoke";

    /**
     * Gateway {@code /hooks/agent} 端点路径。
     */
    public static final String ENDPOINT_HOOKS_AGENT = "/hooks/agent";

    /**
     * Gateway {@code /hooks/wake} 端点路径。
     */
    public static final String ENDPOINT_HOOKS_WAKE = "/hooks/wake";

    // ==================== Message Roles ====================

    /**
     * 聊天消息 role 字段使用的 {@code system} 角色值。
     */
    public static final String ROLE_SYSTEM = "system";

    /**
     * 聊天消息 role 字段使用的 {@code user} 角色值。
     */
    public static final String ROLE_USER = "user";

    /**
     * 聊天消息 role 字段使用的 {@code assistant} 角色值。
     */
    public static final String ROLE_ASSISTANT = "assistant";

    /**
     * 聊天消息 role 字段使用的 {@code tool} 角色值。
     */
    public static final String ROLE_TOOL = "tool";

    // ==================== Object Types ====================

    /**
     * OpenAI 兼容响应 object 字段表示 chat completion 资源时使用的值。
     */
    public static final String OBJECT_CHAT_COMPLETION = "chat.completion";

    /**
     * OpenAI 兼容响应 object 字段表示 chat completion chunk 资源时使用的值。
     */
    public static final String OBJECT_CHAT_COMPLETION_CHUNK = "chat.completion.chunk";

    /**
     * OpenAI 兼容响应 object 字段表示 embedding 资源时使用的值。
     */
    public static final String OBJECT_EMBEDDING = "embedding";

    /**
     * OpenAI 兼容响应 object 字段表示 list 资源时使用的值。
     */
    public static final String OBJECT_LIST = "list";

    /**
     * OpenAI 兼容响应 object 字段表示 model 资源时使用的值。
     */
    public static final String OBJECT_MODEL = "model";

    /**
     * OpenAI 兼容响应 object 字段表示 response 资源时使用的值。
     */
    public static final String OBJECT_RESPONSE = "response";

    // ==================== Finish Reasons ====================

    /**
     * finish_reason 表示服务端正常停止生成时使用的协议值。
     */
    public static final String FINISH_REASON_STOP = "stop";

    /**
     * finish_reason 表示服务端等待调用工具时使用的协议值。
     */
    public static final String FINISH_REASON_TOOL_CALLS = "tool_calls";

    /**
     * finish_reason 表示生成达到长度上限时使用的协议值。
     */
    public static final String FINISH_REASON_LENGTH = "length";

    /**
     * finish_reason 表示工具调用失败时使用的协议值。
     */
    public static final String FINISH_REASON_TOOL_ERROR = "tool_error";

    // ==================== Request Headers ====================

    /**
     * HTTP 请求头 {@code x-openclaw-model} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_MODEL = "x-openclaw-model";

    /**
     * HTTP 请求头 {@code x-openclaw-agent-id} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_AGENT_ID = "x-openclaw-agent-id";

    /**
     * HTTP 请求头 {@code x-openclaw-session-key} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_SESSION_KEY = "x-openclaw-session-key";

    /**
     * HTTP 请求头 {@code x-openclaw-message-channel} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_MESSAGE_CHANNEL = "x-openclaw-message-channel";

    /**
     * HTTP 请求头 {@code x-openclaw-scopes} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_SCOPES = "x-openclaw-scopes";

    /**
     * HTTP 请求头 {@code x-openclaw-token} 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_TOKEN = "x-openclaw-token";

    // ==================== WS Message Types ====================

    /**
     * Gateway WebSocket 帧 type 字段表示 event 帧时使用的值。
     */
    public static final String WS_TYPE_EVENT = "event";

    /**
     * Gateway WebSocket 帧 type 字段表示 req 帧时使用的值。
     */
    public static final String WS_TYPE_REQ = "req";

    /**
     * Gateway WebSocket 帧 type 字段表示 res 帧时使用的值。
     */
    public static final String WS_TYPE_RES = "res";

    // ==================== WS Events ====================

    /**
     * Gateway WebSocket event 字段表示 chat 事件时使用的值。
     */
    public static final String WS_EVENT_CHAT = "chat";

    /**
     * Gateway WebSocket event 字段表示 heartbeat 事件时使用的值。
     */
    public static final String WS_EVENT_HEARTBEAT = "heartbeat";

    // ==================== Input Item Types ====================

    /**
     * Responses API 输入项 type 字段表示 message 时使用的值。
     */
    public static final String INPUT_TYPE_MESSAGE = "message";

    /**
     * Responses API 输入项 type 字段表示 function call output 时使用的值。
     */
    public static final String INPUT_TYPE_FUNCTION_CALL_OUTPUT = "function_call_output";

    /**
     * Responses API 输入项 type 字段表示 image 时使用的值。
     */
    public static final String INPUT_TYPE_IMAGE = "input_image";

    /**
     * Responses API 输入项 type 字段表示 file 时使用的值。
     */
    public static final String INPUT_TYPE_FILE = "input_file";

    // ==================== Tool Types ====================

    /**
     * 工具定义 type 字段表示 function 工具时使用的值。
     */
    public static final String TOOL_TYPE_FUNCTION = "function";

    // ==================== Response Events ====================

    /**
     * Responses SSE 流报告 created 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_CREATED = "response.created";

    /**
     * Responses SSE 流报告 in progress 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_IN_PROGRESS = "response.in_progress";

    /**
     * Responses SSE 流报告 output item added 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_ADDED = "response.output_item.added";

    /**
     * Responses SSE 流报告 content part added 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_CONTENT_PART_ADDED = "response.content_part.added";

    /**
     * Responses SSE 流报告 output text delta 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DELTA = "response.output_text.delta";

    /**
     * Responses SSE 流报告 output text done 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DONE = "response.output_text.done";

    /**
     * Responses SSE 流报告 content part done 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_CONTENT_PART_DONE = "response.content_part.done";

    /**
     * Responses SSE 流报告 output item done 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_DONE = "response.output_item.done";

    /**
     * Responses SSE 流报告 completed 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_COMPLETED = "response.completed";

    /**
     * Responses SSE 流报告 failed 阶段时使用的事件名。
     */
    public static final String RESPONSE_EVENT_FAILED = "response.failed";

    // ==================== Tool Choice ====================

    /**
     * tool_choice 设置为 auto 策略时使用的协议值。
     */
    public static final String TOOL_CHOICE_AUTO = "auto";

    /**
     * tool_choice 设置为 none 策略时使用的协议值。
     */
    public static final String TOOL_CHOICE_NONE = "none";

    /**
     * tool_choice 设置为 required 策略时使用的协议值。
     */
    public static final String TOOL_CHOICE_REQUIRED = "required";

    // ==================== Misc ====================

    /**
     * 标记 SSE 数据流正常结束的 data 字段值。
     */
    public static final String SSE_DONE = "[DONE]";

    /**
     * 未显式指定时采用的立即唤醒模式。
     */
    public static final String DEFAULT_WAKE_MODE = "now";

    /**
     * PATH 查找时使用的默认 OpenClaw CLI 可执行文件名。
     */
    public static final String CLI_EXECUTABLE = "openclaw";

    /**
     * Gateway 挑战超时调度线程的名称。
     */
    public static final String WS_THREAD_NAME = "openclaw-ws-challenge";

    // ==================== Agent Model Prefixes ====================

    /**
     * 识别 OpenClaw 智能体目标时接受的裸前缀。
     */
    public static final String AGENT_PREFIX_OPENCLAW = "openclaw";

    /**
     * 识别斜杠形式 OpenClaw 智能体目标时使用的前缀。
     */
    public static final String AGENT_PREFIX_OPENCLAW_SLASH = "openclaw/";

    /**
     * 识别冒号形式 OpenClaw 智能体目标时使用的前缀。
     */
    public static final String AGENT_PREFIX_OPENCLAW_COLON = "openclaw:";

    /**
     * 识别通用 agent 冒号形式目标时使用的前缀。
     */
    public static final String AGENT_PREFIX_AGENT_COLON = "agent:";

    /**
     * 调用方未指定智能体时使用的兼容默认标识。
     */
    public static final String AGENT_DEFAULT = "openclaw";

    /**
     * 调用方未指定智能体时使用的稳定会话标识。
     */
    public static final String AGENT_DEFAULT_STABLE = "openclaw/default";
}
