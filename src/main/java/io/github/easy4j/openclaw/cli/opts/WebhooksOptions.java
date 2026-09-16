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
package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.cli.args.CliSubArgs;
import io.github.easy4j.openclaw.util.OpenClawLists;

import java.util.ArrayList;
import java.util.List;

/**
 * openclaw {@code webhooks} 子命令的类型化选项：文档化动作为
 * {@code gmail setup} 与 {@code gmail run}。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class WebhooksOptions implements CliSubArgs {

    /**
     * 文档化的 webhooks 动作集合。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示 {@code gmail setup} 动作。
         */
        GMAIL_SETUP("gmail", "setup"),
        /**
         * 表示 {@code gmail run} 动作。
         */
        GMAIL_RUN("gmail", "run");

        /** 动作对应的有序 CLI 参数片段。 */
        private final String[] tokens;

        Verb(String... tokens) {
            this.tokens = tokens;
        }

        /**
         * 返回动作的有序 CLI 参数片段。
         *
         * @return 参数片段；{@code null} 动作返回空
         */
        String[] tokens() {
            return tokens;
        }
    }

    private final Verb verb;

    private WebhooksOptions(Builder builder) {
        this.verb = builder.verb;
    }

    /**
     * 返回构建器入口。
     *
     * @return 新的构建器实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 生成参数列表。
     *
     * @return 有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> args = new ArrayList<>();
        if (verb != null) {
            args.addAll(java.util.Arrays.asList(verb.tokens()));
        }
        return OpenClawLists.copyOf(args);
    }

    /**
     * {@link WebhooksOptions} 的构建器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        private Verb verb;

        private Builder() {
        }

        /**
         * 设置动作。
         *
         * @param value 动作枚举
         * @return 当前构建器
         */
        public Builder verb(Verb value) {
            this.verb = value;
            return this;
        }

        /**
         * 构建不可变选项对象。
         *
         * @return 选项实例
         */
        public WebhooksOptions build() {
            return new WebhooksOptions(this);
        }
    }
}
