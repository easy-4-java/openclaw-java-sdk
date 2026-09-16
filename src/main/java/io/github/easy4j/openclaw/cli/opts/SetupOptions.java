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
 * openclaw {@code setup} 子命令的类型化选项：{@code --baseline} 跳过引导流程，
 * 直接创建基线配置与工作区。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SetupOptions implements CliSubArgs {

    /** 是否以基线模式执行；{@code true} 时追加 {@code --baseline}。 */
    private final boolean baseline;

    private SetupOptions(Builder builder) {
        this.baseline = builder.baseline;
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
        if (baseline) {
            args.add("--baseline");
        }
        return OpenClawLists.copyOf(args);
    }

    /**
     * {@link SetupOptions} 的构建器。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {

        private boolean baseline;

        private Builder() {
        }

        /**
         * 设置是否启用基线模式。
         *
         * @param value {@code true} 追加 {@code --baseline}
         * @return 当前构建器
         */
        public Builder baseline(boolean value) {
            this.baseline = value;
            return this;
        }

        /**
         * 构建不可变选项对象。
         *
         * @return 选项实例
         */
        public SetupOptions build() {
            return new SetupOptions(this);
        }
    }
}
