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
 * openclaw {@code dns} 子命令的类型化选项：文档化动作为 {@code setup}。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class DnsOptions implements CliSubArgs {

    /** 是否追加 {@code setup} 子命令；{@code false} 时仅执行裸 {@code dns}。 */
    private final boolean setup;

    private DnsOptions(Builder builder) {
        this.setup = builder.setup;
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
     * 按旗标状态生成参数列表。
     *
     * @return 有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> args = new ArrayList<>();
        if (setup) {
            args.add("setup");
        }
        return OpenClawLists.copyOf(args);
    }

    /**
     * {@link DnsOptions} 的构建器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        private boolean setup;

        private Builder() {
        }

        /**
         * 设置是否追加 {@code setup} 子命令。
         *
         * @param value {@code true} 追加 {@code setup}
         * @return 当前构建器
         */
        public Builder setup(boolean value) {
            this.setup = value;
            return this;
        }

        /**
         * 构建不可变选项对象。
         *
         * @return 选项实例
         */
        public DnsOptions build() {
            return new DnsOptions(this);
        }
    }
}
