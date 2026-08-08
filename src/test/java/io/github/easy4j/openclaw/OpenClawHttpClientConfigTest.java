package io.github.easy4j.openclaw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenClawHttpClientConfigTest {

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
