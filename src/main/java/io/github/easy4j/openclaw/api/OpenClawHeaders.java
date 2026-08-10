package io.github.easy4j.openclaw.api;

import io.github.easy4j.openclaw.util.OpenClawStrings;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenClaw Gateway HTTP API custom request header constants and builder.
 *
 * <p>Corresponds todocumentation {@code x-openclaw-*} :</p>
 * <ul>
 * <li>{@code x-openclaw-model} - ( {@code openai/gpt-5.4})</li>
 * <li>{@code x-openclaw-agent-id} - agent </li>
 * <li>{@code x-openclaw-session-key} - session</li>
 * <li>{@code x-openclaw-message-channel} - channel</li>
 * <li>{@code x-openclaw-scopes} - </li>
 * </ul>
 *
 * @see <a href="https://docs.openclaw.ai/gateway/openai-http-api#agent-first-model-contract">Agent-first model contract</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class OpenClawHeaders {

 /** Header: */
    public static final String X_OPENCLAW_MODEL = OpenClawConstants.HEADER_X_OPENCLAW_MODEL;

 /** Header: Agent ID */
    public static final String X_OPENCLAW_AGENT_ID = OpenClawConstants.HEADER_X_OPENCLAW_AGENT_ID;

 /** Header: session Key */
    public static final String X_OPENCLAW_SESSION_KEY = OpenClawConstants.HEADER_X_OPENCLAW_SESSION_KEY;

 /** Header: channel */
    public static final String X_OPENCLAW_MESSAGE_CHANNEL = OpenClawConstants.HEADER_X_OPENCLAW_MESSAGE_CHANNEL;

 /** Header: */
    public static final String X_OPENCLAW_SCOPES = OpenClawConstants.HEADER_X_OPENCLAW_SCOPES;

    private OpenClawHeaders() {}

    /**
 * Creates a new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
 * OpenClaw custom request header builder.
     */
    public static final class Builder {
        private String model;
        private String agentId;
        private String sessionKey;
        private String messageChannel;
        private String scopes;

        /**
 * ( {@code openai/gpt-5.4},{@code gpt-5.5}).
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
 * agent .
         */
        public Builder agentId(String agentId) {
            this.agentId = agentId;
            return this;
        }

        /**
 * session.
         */
        public Builder sessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
            return this;
        }

        /**
 * channel( {@code slack},{@code telegram}).
         */
        public Builder messageChannel(String messageChannel) {
            this.messageChannel = messageChannel;
            return this;
        }

        /**
 * ( {@code operator.read,operator.write}).
         */
        public Builder scopes(String scopes) {
            this.scopes = scopes;
            return this;
        }

        /**
 * Map(onlyvalue).
         */
        public Map<String, String> build() {
            Map<String, String> headers = new LinkedHashMap<>();
            if (OpenClawStrings.isNotBlank(model)) {
                headers.put(X_OPENCLAW_MODEL, model);
            }
            if (OpenClawStrings.isNotBlank(agentId)) {
                headers.put(X_OPENCLAW_AGENT_ID, agentId);
            }
            if (OpenClawStrings.isNotBlank(sessionKey)) {
                headers.put(X_OPENCLAW_SESSION_KEY, sessionKey);
            }
            if (OpenClawStrings.isNotBlank(messageChannel)) {
                headers.put(X_OPENCLAW_MESSAGE_CHANNEL, messageChannel);
            }
            if (OpenClawStrings.isNotBlank(scopes)) {
                headers.put(X_OPENCLAW_SCOPES, scopes);
            }
            return Collections.unmodifiableMap(headers);
        }
    }
}
