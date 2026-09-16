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
 * openclaw 通用子命令选项：覆盖文档中存在子命令树但旗标集合庞大的命令
 * （browser / infer / wiki 等），以「一级子命令 + 自由参数」形态透传，
 * 完整子命令清单见 openclaw 官方 CLI 参考。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SubcommandOptions implements CliSubArgs {

    /** 一级子命令名称（如 {@code status}、{@code navigate}）；为空时仅透传附加参数。 */
    private final String sub;

    /** 按序追加的附加参数。 */
    private final List<String> arguments;

    private SubcommandOptions(Builder builder) {
        this.sub = builder.sub;
        this.arguments = OpenClawLists.copyOf(builder.arguments);
    }

    /**
     * 返回空选项，用于没有附加参数的裸子命令调用。
     *
     * @return 不包含任何参数的选项对象
     */
    public static SubcommandOptions bare() {
        return builder().build();
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
     * 按「子命令在前、附加参数在后」的顺序生成参数列表。
     *
     * @return 有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> args = new ArrayList<>();
        if (sub != null && !sub.isEmpty()) {
            args.add(sub);
        }
        args.addAll(arguments);
        return OpenClawLists.copyOf(args);
    }

    /**
     * {@link SubcommandOptions} 的构建器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        private String sub;
        private final List<String> arguments = new ArrayList<>();

        private Builder() {
        }

        /**
         * 设置一级子命令名称。
         *
         * @param value 子命令名称
         * @return 当前构建器
         */
        public Builder sub(String value) {
            this.sub = value;
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
         * 追加多个附加参数。
         *
         * @param values 参数值
         * @return 当前构建器
         */
        public Builder arguments(String... values) {
            for (String value : values) {
                this.arguments.add(value);
            }
            return this;
        }

        /**
         * 构建不可变选项对象。
         *
         * @return 选项实例
         */
        public SubcommandOptions build() {
            return new SubcommandOptions(this);
        }
    }
}
