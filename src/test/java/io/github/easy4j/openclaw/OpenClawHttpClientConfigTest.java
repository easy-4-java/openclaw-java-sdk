package io.github.easy4j.openclaw;

import okhttp3.extension.logging.HttpLogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenClawHttpClientConfigTest {

    @org.junit.jupiter.api.Test
    void debugLoggingIsOptIn() {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        org.junit.jupiter.api.Assertions.assertFalse(config.getDebug().isEnabled());
        org.junit.jupiter.api.Assertions.assertEquals(2_000, config.getDebug().getMaxContentLength());
        org.junit.jupiter.api.Assertions.assertEquals(HttpLogLevel.BASIC,
                config.getDebug().getLevel());

        config.getDebug().setLevel(HttpLogLevel.BODY);
        org.junit.jupiter.api.Assertions.assertEquals(HttpLogLevel.BODY,
                config.getDebug().getLevel());
        config.getDebug().setLevel("HEADERS");
        org.junit.jupiter.api.Assertions.assertEquals(HttpLogLevel.HEADERS,
                config.getDebug().getLevel());
    }

    @Test
    void shouldExposeUnifiedStreamPropertiesAndLegacyAliases() {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        assertEquals(HttpResponseMode.BLOCKING, config.getMode());

        config.setStreamCorePoolSize(7);
        config.setStreamMaxPoolSize(9);
        config.setStreamQueueCapacity(11);
        config.setStreamKeepAliveMillis(13L);

        assertEquals(7, config.getStreamCorePoolSize());
        assertEquals(9, config.getStreamMaxPoolSize());
        assertEquals(11, config.getStreamQueueCapacity());
        assertEquals(13L, config.getStreamKeepAliveMillis());
    }
}
