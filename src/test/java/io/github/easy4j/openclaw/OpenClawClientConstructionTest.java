package io.github.easy4j.openclaw;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link OpenClawClient} 构造器契约测试。
 *
 * <p>覆盖：</p>
 * <ul>
 *     <li>自动 ObjectMapper / OkHttpClient 的便捷构造器</li>
 *     <li>{@code enabled=false} 真正短路：HTTP/CLI 子客户端字段为 null</li>
 *     <li>{@code requireNonNull} 校验：传 null mapper / httpClient 抛 NPE</li>
 *     <li>组合配置构造器</li>
 * </ul>
 */
class OpenClawClientConstructionTest {

    /**
     * 双配置 + 自动 mapper/client：HTTP 与 CLI 都启用。
     * 注：{@code gatewayBaseUrl} 与 {@code openclaw} 可执行必须存在，否则启动自检会失败；
     * 这里把 {@code startupCheckEnabled} 置为 false 跳过实际探测。
     */
    @Test
    void dualConfig_autoMapperClient_enablesBothSubsystems() {
        OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();
        http.setStartupCheckEnabled(false);
        OpenClawCliConfig cli = new OpenClawCliConfig();
        cli.setStartupCheckEnabled(false);

        try (OpenClawClient client = new OpenClawClient(http, cli)) {
            assertTrue(client.isHttpEnabled());
            assertTrue(client.isCliEnabled());
            assertNotNull(client.chat());
            assertNotNull(client.embeddings());
            assertNotNull(client.responses());
            assertNotNull(client.toolsInvoke());
            assertNotNull(client.ws());
            assertNotNull(client.cli());
        }
    }

    /**
     * {@code httpConfig.enabled=false} 时 HTTP 子系统所有客户端字段为 null，
     * 但 CLI 仍可用。
     */
    @Test
    void httpDisabled_subClientsAreNull_cliStillUsable() {
        OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();
        http.setEnabled(false);
        OpenClawCliConfig cli = new OpenClawCliConfig();
        cli.setStartupCheckEnabled(false);

        try (OpenClawClient client = new OpenClawClient(http, cli)) {
            assertFalse(client.isHttpEnabled());
            assertTrue(client.isCliEnabled());
            assertNull(client.chat());
            assertNull(client.embeddings());
            assertNull(client.responses());
            assertNull(client.toolsInvoke());
            assertNull(client.ws());
            assertNotNull(client.cli());
        }
    }

    /**
     * {@code cliConfig.enabled=false} 时 CLI 字段为 null，HTTP 子系统仍可用。
     */
    @Test
    void cliDisabled_cliIsNull_httpStillUsable() {
        OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();
        http.setStartupCheckEnabled(false);
        OpenClawCliConfig cli = new OpenClawCliConfig();
        cli.setEnabled(false);

        try (OpenClawClient client = new OpenClawClient(http, cli)) {
            assertTrue(client.isHttpEnabled());
            assertFalse(client.isCliEnabled());
            assertNotNull(client.chat());
            assertNull(client.cli());
        }
    }

    /**
     * 强制注入 null mapper 时必须抛 NPE（{@code Objects.requireNonNull} 校验）。
     */
    @Test
    void requireNonNull_nullObjectMapper_throws() {
        OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();
        OpenClawCliConfig cli = new OpenClawCliConfig();
        assertThrows(NullPointerException.class,
                () -> new OpenClawClient(http, cli, null, new OkHttpClient()));
    }

    /**
     * 强制注入 null httpClient 时必须抛 NPE。
     */
    @Test
    void requireNonNull_nullHttpClient_throws() {
        OpenClawHttpClientConfig http = new OpenClawHttpClientConfig();
        OpenClawCliConfig cli = new OpenClawCliConfig();
        assertThrows(NullPointerException.class,
                () -> new OpenClawClient(http, cli, new JsonMapper(), null));
    }

    /**
     * 组合配置构造器（自动 mapper/client）等价于拆分为双配置。
     */
    @Test
    void combinedConfig_autoMapperClient_enablesBothSubsystems() {
        OpenClawClientConfig config = new OpenClawClientConfig();
        config.getHttp().setStartupCheckEnabled(false);
        config.getCli().setStartupCheckEnabled(false);

        try (OpenClawClient client = new OpenClawClient(config)) {
            assertTrue(client.isHttpEnabled());
            assertTrue(client.isCliEnabled());
        }
    }

    /**
     * 强制注入 + 组合配置：mapper/client 非 null 时正常构造。
     */
    @Test
    void combinedConfig_injectedMapperClient_enablesBothSubsystems() {
        OpenClawClientConfig config = new OpenClawClientConfig();
        config.getHttp().setStartupCheckEnabled(false);
        config.getCli().setStartupCheckEnabled(false);

        try (OpenClawClient client = new OpenClawClient(config, new JsonMapper(), new OkHttpClient())) {
            assertTrue(client.isHttpEnabled());
            assertTrue(client.isCliEnabled());
        }
    }
}
