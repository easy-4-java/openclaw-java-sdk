package io.github.easy4j.openclaw.api;

/**
 * OpenClaw API constant definitions.
 * <p>
 * Centrally managesvalue,:
 * <ul>
 * <li>HTTP </li>
 * <li>Chat Message </li>
 * <li>Chat Response object</li>
 * <li>Finish Reason completion</li>
 * <li>Request Header </li>
 * <li>Request/Response </li>
 * <li>WS message</li>
 * <li>Input Item </li>
 * <li>Think Level </li>
 * <li>tool call</li>
 * </ul>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 */
public final class OpenClawConstants {

    private OpenClawConstants() {}

    // ==================== HTTP Endpoints ====================

 /** Chat Completions */
    public static final String ENDPOINT_CHAT_COMPLETIONS = "/v1/chat/completions";

 /** Models */
    public static final String ENDPOINT_MODELS = "/v1/models";

 /** Embeddings */
    public static final String ENDPOINT_EMBEDDINGS = "/v1/embeddings";

 /** Responses */
    public static final String ENDPOINT_RESPONSES = "/v1/responses";

 /** Tools Invoke */
    public static final String ENDPOINT_TOOLS_INVOKE = "/tools/invoke";

 /** Webhook: Agent */
    public static final String ENDPOINT_HOOKS_AGENT = "/hooks/agent";

 /** Webhook: Wake */
    public static final String ENDPOINT_HOOKS_WAKE = "/hooks/wake";

    // ==================== Message Roles ====================

 /** message:system */
    public static final String ROLE_SYSTEM = "system";

 /** message: */
    public static final String ROLE_USER = "user";

 /** message: */
    public static final String ROLE_ASSISTANT = "assistant";

 /** message: */
    public static final String ROLE_TOOL = "tool";

    // ==================== Object Types ====================

 /** object:Chat Completion */
    public static final String OBJECT_CHAT_COMPLETION = "chat.completion";

 /** object:Chat Completion Chunk */
    public static final String OBJECT_CHAT_COMPLETION_CHUNK = "chat.completion.chunk";

 /** object:Embedding */
    public static final String OBJECT_EMBEDDING = "embedding";

 /** object:List */
    public static final String OBJECT_LIST = "list";

 /** object:Model */
    public static final String OBJECT_MODEL = "model";

 /** object:Response */
    public static final String OBJECT_RESPONSE = "response";

    // ==================== Finish Reasons ====================

 /** completion:completion */
    public static final String FINISH_REASON_STOP = "stop";

 /** completion:tool call */
    public static final String FINISH_REASON_TOOL_CALLS = "tool_calls";

 /** completion: token */
    public static final String FINISH_REASON_LENGTH = "length";

 /** completion: */
    public static final String FINISH_REASON_TOOL_ERROR = "tool_error";

    // ==================== Request Headers ====================

 /** Header: */
    public static final String HEADER_X_OPENCLAW_MODEL = "x-openclaw-model";

 /** Header: Agent ID */
    public static final String HEADER_X_OPENCLAW_AGENT_ID = "x-openclaw-agent-id";

 /** Header: session Key */
    public static final String HEADER_X_OPENCLAW_SESSION_KEY = "x-openclaw-session-key";

 /** Header: channel */
    public static final String HEADER_X_OPENCLAW_MESSAGE_CHANNEL = "x-openclaw-message-channel";

 /** Header: */
    public static final String HEADER_X_OPENCLAW_SCOPES = "x-openclaw-scopes";

 /** Header: Webhook Token */
    public static final String HEADER_X_OPENCLAW_TOKEN = "x-openclaw-token";

    // ==================== WS Message Types ====================

 /** WS message:event */
    public static final String WS_TYPE_EVENT = "event";

 /** WS message: */
    public static final String WS_TYPE_REQ = "req";

 /** WS message: */
    public static final String WS_TYPE_RES = "res";

    // ==================== WS Events ====================

 /** WS event: */
    public static final String WS_EVENT_CHAT = "chat";

 /** WS event:heartbeat */
    public static final String WS_EVENT_HEARTBEAT = "heartbeat";

    // ==================== Input Item Types ====================

 /** Input Item :message */
    public static final String INPUT_TYPE_MESSAGE = "message";

 /** Input Item : */
    public static final String INPUT_TYPE_FUNCTION_CALL_OUTPUT = "function_call_output";

 /** Input Item : */
    public static final String INPUT_TYPE_IMAGE = "input_image";

 /** Input Item : */
    public static final String INPUT_TYPE_FILE = "input_file";

    // ==================== Tool Types ====================

 /** : */
    public static final String TOOL_TYPE_FUNCTION = "function";

    // ==================== Response Events ====================

 /** Response event: */
    public static final String RESPONSE_EVENT_CREATED = "response.created";

 /** Response event: */
    public static final String RESPONSE_EVENT_IN_PROGRESS = "response.in_progress";

 /** Response event: */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_ADDED = "response.output_item.added";

 /** Response event: */
    public static final String RESPONSE_EVENT_CONTENT_PART_ADDED = "response.content_part.added";

 /** Response event:delta */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DELTA = "response.output_text.delta";

 /** Response event:completion */
    public static final String RESPONSE_EVENT_OUTPUT_TEXT_DONE = "response.output_text.done";

 /** Response event:completion */
    public static final String RESPONSE_EVENT_CONTENT_PART_DONE = "response.content_part.done";

 /** Response event:completion */
    public static final String RESPONSE_EVENT_OUTPUT_ITEM_DONE = "response.output_item.done";

 /** Response event:completion */
    public static final String RESPONSE_EVENT_COMPLETED = "response.completed";

 /** Response event: */
    public static final String RESPONSE_EVENT_FAILED = "response.failed";

    // ==================== Tool Choice ====================

 /** tool choice: */
    public static final String TOOL_CHOICE_AUTO = "auto";

 /** tool choice: */
    public static final String TOOL_CHOICE_NONE = "none";

 /** tool choice: */
    public static final String TOOL_CHOICE_REQUIRED = "required";

    // ==================== Misc ====================

 /** SSE completion */
    public static final String SSE_DONE = "[DONE]";

 /** wake */
    public static final String DEFAULT_WAKE_MODE = "now";

 /** CLI executable name */
    public static final String CLI_EXECUTABLE = "openclaw";

 /** WS thread */
    public static final String WS_THREAD_NAME = "openclaw-ws-challenge";

    // ==================== Agent Model Prefixes ====================

 /** Agent :openclaw */
    public static final String AGENT_PREFIX_OPENCLAW = "openclaw";

 /** Agent :openclaw/ */
    public static final String AGENT_PREFIX_OPENCLAW_SLASH = "openclaw/";

 /** Agent :openclaw: */
    public static final String AGENT_PREFIX_OPENCLAW_COLON = "openclaw:";

 /** Agent :agent: */
    public static final String AGENT_PREFIX_AGENT_COLON = "agent:";

 /** Agent */
    public static final String AGENT_DEFAULT = "openclaw";

 /** Agent  */
    public static final String AGENT_DEFAULT_STABLE = "openclaw/default";
}
