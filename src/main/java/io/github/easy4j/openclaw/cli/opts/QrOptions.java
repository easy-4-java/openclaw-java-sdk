package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * openclaw `qr` 子命令的类型化选项。Builder 记录显式设置项，toSubcommandArguments() 按 CLI 语法生成参数。
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class QrOptions implements CliSubArgs {

    /**
     * 是否向 openclaw 子命令追加 `--remote` 开关。
     */
    private final boolean remote;
    /**
     * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
     */
    private final String url;
    /**
     * 传给 openclaw 子命令 `--public-url` 选项的内容；为 null 时通常省略。
     */
    private final String publicUrl;
    /**
     * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
     */
    private final String token;
    /**
     * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
     */
    private final String password;
    /**
     * 是否向 openclaw 子命令追加 `--setup-code-only` 开关。
     */
    private final boolean setupCodeOnly;
    /**
     * 是否向 openclaw 子命令追加 `--no-ascii` 开关。
     */
    private final boolean noAscii;
    /**
     * 是否向 openclaw 子命令追加 `--json` 开关。
     */
    private final boolean json;
    /**
     * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
     */
    private final List<String> extra;

    /**
 * @param b builder
     */
    private QrOptions(Builder b) {
        this.remote = b.remote;
        this.url = b.url;
        this.publicUrl = b.publicUrl;
        this.token = b.token;
        this.password = b.password;
        this.setupCodeOnly = b.setupCodeOnly;
        this.noAscii = b.noAscii;
        this.json = b.json;
        this.extra = b.extra == null ? OpenClawLists.empty() : OpenClawLists.copyOf(b.extra);
    }

    /**
     * 创建空白构建器，供调用方链式设置 `QrOptions` 字段。
     *
     * @return 新的空白构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 按 openclaw CLI 约定把已设置字段编码为有序参数列表，未设置选项不会输出。
     *
     * @return 可直接传给 Commons Exec 的有序 CLI 参数
     */
    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        OpenClawCliArgv.addFlag(out, "--remote", remote);
        OpenClawCliArgv.addIfPresent(out, "--url", url);
        OpenClawCliArgv.addIfPresent(out, "--public-url", publicUrl);
        OpenClawCliArgv.addIfPresent(out, "--token", token);
        OpenClawCliArgv.addIfPresent(out, "--password", password);
        OpenClawCliArgv.addFlag(out, "--setup-code-only", setupCodeOnly);
        OpenClawCliArgv.addFlag(out, "--no-ascii", noAscii);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addExtra(out, extra);
        return Collections.unmodifiableList(out);
    }

    /**
     * 链式构建器，逐项收集 QrOptions 的字段；build() 会复制当前快照，后续修改不会影响已构造的 QrOptions。
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    public static final class Builder {
        /**
         * 是否向 openclaw 子命令追加 `--remote` 开关。
         */
        private boolean remote;
        /**
         * 传给 openclaw 子命令 `--url` 选项的内容；为 null 时通常省略。
         */
        private String url;
        /**
         * 传给 openclaw 子命令 `--public-url` 选项的内容；为 null 时通常省略。
         */
        private String publicUrl;
        /**
         * 传给 openclaw 子命令 `--token` 选项的内容；为 null 时通常省略。
         */
        private String token;
        /**
         * 传给 openclaw 子命令 `--password` 选项的内容；为 null 时通常省略。
         */
        private String password;
        /**
         * 是否向 openclaw 子命令追加 `--setup-code-only` 开关。
         */
        private boolean setupCodeOnly;
        /**
         * 是否向 openclaw 子命令追加 `--no-ascii` 开关。
         */
        private boolean noAscii;
        /**
         * 是否向 openclaw 子命令追加 `--json` 开关。
         */
        private boolean json;
        /**
         * 传给 openclaw 子命令 `--extra` 选项的内容；为 null 时通常省略。
         */
        private List<String> extra = new ArrayList<>();

        /**
         * 设置 `--remote` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param remote 是否向命令行追加 `--remote` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder remote(boolean remote) {
            this.remote = remote;
            return this;
        }

        /**
         * 设置 `--url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param url 完整目标 URL
         * @return 当前构建器，便于继续链式配置
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 `--public-url` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param publicUrl 写入 `--public-url` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder publicUrl(String publicUrl) {
            this.publicUrl = publicUrl;
            return this;
        }

        /**
         * 设置 `--token` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param token 写入 `--token` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置 `--password` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param password 写入 `--password` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置 `--setup-code-only` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param setupCodeOnly 是否向命令行追加 `--setup-code-only` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder setupCodeOnly(boolean setupCodeOnly) {
            this.setupCodeOnly = setupCodeOnly;
            return this;
        }

        /**
         * 设置 `--no-ascii` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param noAscii 是否向命令行追加 `--no-ascii` 开关
         * @return 当前构建器，便于继续链式配置
         */
        public Builder noAscii(boolean noAscii) {
            this.noAscii = noAscii;
            return this;
        }

        /**
         * 设置 `--json` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param json JSON 文本
         * @return 当前构建器，便于继续链式配置
         */
        public Builder json(boolean json) {
            this.json = json;
            return this;
        }

        /**
         * 设置 `--extra` 命令选项并返回当前构建器；是否输出该选项由参数值决定。
         *
         * @param tokens 写入 `--extra` 选项的内容
         * @return 当前构建器，便于继续链式配置
         */
        public Builder extra(String... tokens) {
            if (tokens != null) {
                Collections.addAll(extra, tokens);
            }
            return this;
        }

        /**
         * 校验并复制当前构建器字段，创建独立的 `QrOptions`。
         *
         * @return 按当前字段创建的 QrOptions
         */
        public QrOptions build() {
            return new QrOptions(this);
        }
    }
}
