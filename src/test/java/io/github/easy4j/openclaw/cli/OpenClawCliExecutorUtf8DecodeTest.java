/*
 * Copyright (c) 2018-present, easy-4-java (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.easy4j.openclaw.cli;

import io.github.easy4j.openclaw.OpenClawCliConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CLI 子进程输出的字符集解码回归测试：执行器必须以 UTF-8 显式解码子进程输出，
 * 而不是依赖平台默认字符集（C locale / GBK 环境下中文与 Emoji 会损坏）。
 *
 * <p>子进程用 POSIX printf 的八进制转义（{@code \344\275\240\345\245\275}）输出
 * “你好”的原始 UTF-8 字节。注意：Java 源码里的反斜杠必须双写，否则
 * {@code \344} 会在编译期被当作八进制转义吃掉。</p>
 *
 * @since 1.0.0
 */
class OpenClawCliExecutorUtf8DecodeTest {

    @Test
    void shouldDecodeUtf8OutputRegardlessOfPlatformCharset() {
        OpenClawCliConfig config = new OpenClawCliConfig();
        config.setExecutable("/bin/sh");
        config.setTimeout(10);
        OpenClawCliExecutor executor = new OpenClawCliExecutor(config);

        OpenClawCliResult result = executor.execute(OpenClawCliRequest.builder()
                .arguments("-c", "printf '\\344\\275\\240\\345\\245\\275'")
                .build());

        assertEquals("你好", result.getStdout());
    }
}
