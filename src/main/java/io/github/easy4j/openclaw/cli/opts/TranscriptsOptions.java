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
 * openclaw {transcripts} 子命令的类型化选项：动作枚举 + 可选操作数 + 附加参数，
 * 按文档化子命令树生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class TranscriptsOptions implements CliSubArgs {

    /**
     * transcripts 的文档化动作集合。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public enum Verb {
        /**
         * 表示 {@code list} 动作。
         */
        LIST,

        /**
         * 表示 {@code show} 动作。
         */
        SHOW,

        /**
         * 表示 {@code path} 动作。
         */
        PATH,

        /**
         * 未指定动作；生成参数时不包含动词本身。
         */
        NONE;

        /**
         * 返回动作的 CLI 小写拼写。
         *
         * @return CLI 固定参数值；{@code NONE} 返回 {@code null}
         */
        String cliValue() {
            return this == NONE ? null : name().toLowerCase().replace('_', ' ');
        }
    }

    private final Verb verb;
    private final String operand;
    private final List<String> arguments;

    private TranscriptsOptions(Builder builder) {
        this.verb = builder.verb;
        this.operand = builder.operand;
        this.arguments = OpenClawLists.copyOf(builder.arguments);
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
     * 按「动词 → 操作数 → 附加参数」顺序生成参数列表。
     *
     * @return 有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> args = new ArrayList<>();
        if (verb != null && verb.cliValue() != null) {
            args.add(verb.cliValue());
        }
        if (operand != null && !operand.isEmpty()) {
            args.add(operand);
        }
        args.addAll(arguments);
        return OpenClawLists.copyOf(args);
    }

    /**
     * {TranscriptsOptions} 的构建器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        private Verb verb;
        private String operand;
        private final List<String> arguments = new ArrayList<>();

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
         * 设置动词的操作数（如对象 id 或查询串）。
         *
         * @param value 操作数
         * @return 当前构建器
         */
        public Builder operand(String value) {
            this.operand = value;
            return this;
        }

        /**
         * 追加一个附加参数。
         *
         * @param value 参数值
         * @return 当前构建器
         */
        public Builder argument(String value) {
            this.arguments.add(value);
            return this;
        }

        /**
         * 构建不可变选项对象。
         *
         * @return 选项实例
         */
        public TranscriptsOptions build() {
            return new TranscriptsOptions(this);
        }
    }
}
