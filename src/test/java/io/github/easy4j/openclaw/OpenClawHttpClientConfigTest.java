package io.github.easy4j.openclaw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenClawHttpClientConfigTest {

    @Test
    void shouldExposeUnifiedStreamPropertiesAndLegacyAliases() {
        OpenClawHttpClientConfig config = new OpenClawHttpClientConfig();
        assertEquals(HttpResponseMode.BLOCKING, config.getMode());

        config.setSseCorePoolSize(7);
        config.setSseMaxPoolSize(9);
        config.setSseQueueCapacity(11);
        config.setSseKeepAliveMillis(13L);

        assertEquals(7, config.getStreamCorePoolSize());
        assertEquals(9, config.getStreamMaxPoolSize());
        assertEquals(11, config.getStreamQueueCapacity());
        assertEquals(13L, config.getStreamKeepAliveMillis());
    }
}
