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
     * Gateway `/v1/chat/completions` 端点路径。
     */
    public static final String ENDPOINT_CHAT_COMPLETIONS = "/v1/chat/completions";

    /**
     * Gateway `/v1/models` 端点路径。
     */
    public static final String ENDPOINT_MODELS = "/v1/models";

    /**
     * Gateway `/v1/embeddings` 端点路径。
     */
    public static final String ENDPOINT_EMBEDDINGS = "/v1/embeddings";

    /**
     * Gateway `/v1/responses` 端点路径。
     */
    public static final String ENDPOINT_RESPONSES = "/v1/responses";

    /**
     * Gateway `/tools/invoke` 端点路径。
     */
    public static final String ENDPOINT_TOOLS_INVOKE = "/tools/invoke";

    /**
     * Gateway `/hooks/agent` 端点路径。
     */
    public static final String ENDPOINT_HOOKS_AGENT = "/hooks/agent";

    /**
     * Gateway `/hooks/wake` 端点路径。
     */
    public static final String ENDPOINT_HOOKS_WAKE = "/hooks/wake";

    // ==================== Message Roles ====================

    /**
     * OpenClaw 协议固定值 {@code "system"}；调用方不应在运行时修改。
     */
    public static final String ROLE_SYSTEM = "system";

    /**
     * OpenClaw 协议固定值 {@code "user"}；调用方不应在运行时修改。
     */
    public static final String ROLE_USER = "user";

    /**
     * OpenClaw 协议固定值 {@code "assistant"}；调用方不应在运行时修改。
     */
    public static final String ROLE_ASSISTANT = "assistant";

    /**
     * OpenClaw 协议固定值 {@code "tool"}；调用方不应在运行时修改。
     */
    public static final String ROLE_TOOL = "tool";

    // ==================== Object Types ====================

    /**
     * OpenClaw 协议固定值 {@code "chat.completion"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_CHAT_COMPLETION = "chat.completion";

    /**
     * OpenClaw 协议固定值 {@code "chat.completion.chunk"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_CHAT_COMPLETION_CHUNK = "chat.completion.chunk";

    /**
     * OpenClaw 协议固定值 {@code "embedding"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_EMBEDDING = "embedding";

    /**
     * OpenClaw 协议固定值 {@code "list"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_LIST = "list";

    /**
     * OpenClaw 协议固定值 {@code "model"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_MODEL = "model";

    /**
     * OpenClaw 协议固定值 {@code "response"}；调用方不应在运行时修改。
     */
    public static final String OBJECT_RESPONSE = "response";

    // ==================== Finish Reasons ====================

    /**
     * OpenClaw 协议固定值 {@code "stop"}；调用方不应在运行时修改。
     */
    public static final String FINISH_REASON_STOP = "stop";

    /**
     * OpenClaw 协议固定值 {@code "tool_calls"}；调用方不应在运行时修改。
     */
    public static final String FINISH_REASON_TOOL_CALLS = "tool_calls";

    /**
     * OpenClaw 协议固定值 {@code "length"}；调用方不应在运行时修改。
     */
    public static final String FINISH_REASON_LENGTH = "length";

    /**
     * OpenClaw 协议固定值 {@code "tool_error"}；调用方不应在运行时修改。
     */
    public static final String FINISH_REASON_TOOL_ERROR = "tool_error";

    // ==================== Request Headers ====================

    /**
     * HTTP 请求头 `x-openclaw-model` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_MODEL = "x-openclaw-model";

    /**
     * HTTP 请求头 `x-openclaw-agent-id` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_AGENT_ID = "x-openclaw-agent-id";

    /**
     * HTTP 请求头 `x-openclaw-session-key` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_SESSION_KEY = "x-openclaw-session-key";

    /**
     * HTTP 请求头 `x-openclaw-message-channel` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_MESSAGE_CHANNEL = "x-openclaw-message-channel";

    /**
     * HTTP 请求头 `x-openclaw-scopes` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_SCOPES = "x-openclaw-scopes";

    /**
     * HTTP 请求头 `x-openclaw-token` 的规范名称。
     */
    public static final String HEADER_X_OPENCLAW_TOKEN = "x-openclaw-token";

    // ==================== WS Message Types ====================

    /**
     * OpenClaw 协议固定值 {@code "event"}；调用方不应在运行时修改。
     */
    public static final String WS_TYPE_EVENT = "event";

    /**
     * OpenClaw 协议固定值 {@code "req"}；调用方不应在运行时修改。
     */
    public static final String WS_TYPE_REQ = "req";

    /**
     * OpenClaw 协议固定值 {@code "res"}；调用方不应在运行时修改。
     */
    public static final String WS_TYPE_RES = "res";

    // ==================== WS Events ====================

    /**
     * OpenClaw 协议固定值 {@code "chat"}；调用方不应在运行时修改。
     */
    public static final String WS_EVENT_CHAT = "chat";

    /**
     * OpenClaw 协议固定值 {@code "heartbeat"}；调用方不应在运行时修改。
     */
    public static final String WS_EVENT_HEARTBEAT = "heartbeat";

    // ==================== Input Item Types ====================

    /**
     * OpenClaw 协议固定值 {@code "message"}；调用方不应在运行时修改。
     */
    public static final String INPUT_TYPE_MESSAGE = "message";

    /**
     * OpenClaw 协议固定值 {@code "function_call_output"}；调用方不应在运行时修改。
     */
    public static final String INPUT_TYPE_FUNCTION_CALL_OUTPUT = "function_call_output";

    /**
     * OpenClaw 协议固定值 {@code "input_image"}；调用方不应在运行时修改。
     */
    public static final String INPUT_TYPE_IMAGE = "input_image";

    /**
     * OpenClaw 协议固定值 {@code "input_file"}；调用方不应在运行时修改。
     */
    public static final String INPUT_TYPE_FILE = "input_file";

    // ==================== Tool Types ====================

    /**
     * OpenClaw 协议固定值 {@code "function"}；调用方不应在运行时修改。
     */
    public static final String TOOL_TYPE_FUNCTION = "function";

    // ==================== Response Events ====================

    /**
     * OpenClaw 协议固定值 {@code "response.created"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_CREATED = "response.created";

    /**
     * OpenClaw 协议固定值 {@code "response.in_progress"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_IN_PROGRESS = "response.in_progress";

    /**
     * OpenClaw 协议固定值 {@code "response.output_item.added"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_ADDED = "response.output_item.added";

    /**
     * OpenClaw 协议固定值 {@code "response.content_part.added"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_CONTENT_PART_ADDED = "response.content_part.added";

    /**
     * OpenClaw 协议固定值 {@code "response.output_text.delta"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DELTA = "response.output_text.delta";

    /**
     * OpenClaw 协议固定值 {@code "response.output_text.done"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DONE = "response.output_text.done";

    /**
     * OpenClaw 协议固定值 {@code "response.content_part.done"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_CONTENT_PART_DONE = "response.content_part.done";

    /**
     * OpenClaw 协议固定值 {@code "response.output_item.done"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_DONE = "response.output_item.done";

    /**
     * OpenClaw 协议固定值 {@code "response.completed"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_COMPLETED = "response.completed";

    /**
     * OpenClaw 协议固定值 {@code "response.failed"}；调用方不应在运行时修改。
     */
    public static final String RESPONSE_EVENT_FAILED = "response.failed";

    // ==================== Tool Choice ====================

    /**
     * OpenClaw 协议固定值 {@code "auto"}；调用方不应在运行时修改。
     */
    public static final String TOOL_CHOICE_AUTO = "auto";

    /**
     * OpenClaw 协议固定值 {@code "none"}；调用方不应在运行时修改。
     */
    public static final String TOOL_CHOICE_NONE = "none";

    /**
     * OpenClaw 协议固定值 {@code "required"}；调用方不应在运行时修改。
     */
    public static final String TOOL_CHOICE_REQUIRED = "required";

    // ==================== Misc ====================

    /**
     * OpenClaw 协议固定值 {@code "[DONE]"}；调用方不应在运行时修改。
     */
    public static final String SSE_DONE = "[DONE]";

    /**
     * OpenClaw 协议固定值 {@code "now"}；调用方不应在运行时修改。
     */
    public static final String DEFAULT_WAKE_MODE = "now";

    /**
     * OpenClaw 协议固定值 {@code "openclaw"}；调用方不应在运行时修改。
     */
    public static final String CLI_EXECUTABLE = "openclaw";

    /**
     * OpenClaw 协议固定值 {@code "openclaw-ws-challenge"}；调用方不应在运行时修改。
     */
    public static final String WS_THREAD_NAME = "openclaw-ws-challenge";

    // ==================== Agent Model Prefixes ====================

    /**
     * OpenClaw 协议固定值 {@code "openclaw"}；调用方不应在运行时修改。
     */
    public static final String AGENT_PREFIX_OPENCLAW = "openclaw";

    /**
     * OpenClaw 协议固定值 {@code "openclaw/"}；调用方不应在运行时修改。
     */
    public static final String AGENT_PREFIX_OPENCLAW_SLASH = "openclaw/";

    /**
     * OpenClaw 协议固定值 {@code "openclaw:"}；调用方不应在运行时修改。
     */
    public static final String AGENT_PREFIX_OPENCLAW_COLON = "openclaw:";

    /**
     * OpenClaw 协议固定值 {@code "agent:"}；调用方不应在运行时修改。
     */
    public static final String AGENT_PREFIX_AGENT_COLON = "agent:";

    /**
     * OpenClaw 协议固定值 {@code "openclaw"}；调用方不应在运行时修改。
     */
    public static final String AGENT_DEFAULT = "openclaw";

    /**
     * OpenClaw 协议固定值 {@code "openclaw/default"}；调用方不应在运行时修改。
     */
    public static final String AGENT_DEFAULT_STABLE = "openclaw/default";
}
