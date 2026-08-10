package io.github.easy4j.openclaw.cli.opts;

import io.github.easy4j.openclaw.util.OpenClawLists;
import io.github.easy4j.openclaw.cli.args.CliSubArgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code openclaw proxy}: OpenClaw proxystream.
 * <p>
 * {@code start},{@code run [cmd...]},{@code validate},{@code coverage},{@code sessions},
 * {@code query},{@code blob},{@code purge} subcommand.
 * </p>
 *
 * @see <a href="https://docs.openclaw.ai/cli/proxy">proxy CLI</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public final class ProxyOptions implements CliSubArgs {

 /** subcommand. */
    public enum Mode {
        START,
        RUN,
        VALIDATE,
        COVERAGE,
        SESSIONS,
        QUERY,
        BLOB,
        PURGE
    }

    private final Mode mode;
 /** run: {@code [cmd...]},. */
    private final List<String> runCommand;
 /** start/run:{@code --host} ( {@code 127.0.0.1}). */
    private final String host;
 /** start/run:{@code --port} . */
    private final Integer port;
 /** validate:{@code --json} . */
    private final boolean json;
 /** validate:{@code --proxy-url} proxy URL. */
    private final String proxyUrl;
 /** validate:{@code --proxy-ca-file} HTTPS proxy CA bundle . */
    private final String proxyCaFile;
 /** validate:{@code --allowed-url} URL. */
    private final List<String> allowedUrls;
 /** validate:{@code --denied-url}proxy URL. */
    private final List<String> deniedUrls;
 /** validate:{@code --apns-reachable} APNs HTTP/2 . */
    private final boolean apnsReachable;
 /** validate:{@code --apns-authority} {@code --apns-reachable} APNs authority. */
    private final String apnsAuthority;
 /** validate:{@code --timeout-ms} timeoutmilliseconds. */
    private final Integer timeoutMs;
 /** sessions:{@code --limit} session. */
    private final Integer limit;
 /** query:{@code --preset}(Required). */
    private final String preset;
 /** query:{@code --session} session id. */
    private final String session;
 /** blob:{@code --id}(Required)Blob id. */
    private final String blobId;

    private ProxyOptions(Builder b) {
        this.mode = b.mode;
        this.runCommand = OpenClawLists.copyOf(b.runCommand);
        this.host = b.host;
        this.port = b.port;
        this.json = b.json;
        this.proxyUrl = b.proxyUrl;
        this.proxyCaFile = b.proxyCaFile;
        this.allowedUrls = OpenClawLists.copyOf(b.allowedUrls);
        this.deniedUrls = OpenClawLists.copyOf(b.deniedUrls);
        this.apnsReachable = b.apnsReachable;
        this.apnsAuthority = b.apnsAuthority;
        this.timeoutMs = b.timeoutMs;
        this.limit = b.limit;
        this.preset = b.preset;
        this.session = b.session;
        this.blobId = b.blobId;
    }

    /**
 * @return {@link Builder}( {@link Mode#START})
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<String> toSubcommandArguments() {
        List<String> out = new ArrayList<>();
        if (mode != null) {
            out.add(mode.name().toLowerCase());
        }
        if (runCommand != null && !runCommand.isEmpty()) {
            out.addAll(runCommand);
        }
        OpenClawCliArgv.addIfPresent(out, "--host", host);
        OpenClawCliArgv.addIfNotNull(out, "--port", port);
        OpenClawCliArgv.addFlag(out, "--json", json);
        OpenClawCliArgv.addIfPresent(out, "--proxy-url", proxyUrl);
        OpenClawCliArgv.addIfPresent(out, "--proxy-ca-file", proxyCaFile);
        OpenClawCliArgv.addRepeatable(out, "--allowed-url", allowedUrls);
        OpenClawCliArgv.addRepeatable(out, "--denied-url", deniedUrls);
        OpenClawCliArgv.addFlag(out, "--apns-reachable", apnsReachable);
        OpenClawCliArgv.addIfPresent(out, "--apns-authority", apnsAuthority);
        OpenClawCliArgv.addIfNotNull(out, "--timeout-ms", timeoutMs);
        OpenClawCliArgv.addIfNotNull(out, "--limit", limit);
        OpenClawCliArgv.addIfPresent(out, "--preset", preset);
        OpenClawCliArgv.addIfPresent(out, "--session", session);
        OpenClawCliArgv.addIfPresent(out, "--id", blobId);
        return Collections.unmodifiableList(out);
    }

    /**
 * {@link ProxyOptions} builder.
     */
    public static final class Builder {
        private Mode mode = Mode.START;
        private List<String> runCommand;
        private String host;
        private Integer port;
        private boolean json;
        private String proxyUrl;
        private String proxyCaFile;
        private List<String> allowedUrls;
        private List<String> deniedUrls;
        private boolean apnsReachable;
        private String apnsAuthority;
        private Integer timeoutMs;
        private Integer limit;
        private String preset;
        private String session;
        private String blobId;

 /** {@link Mode}. */
        public Builder mode(Mode mode) { this.mode = mode; return this; }
 /** {@code start} subcommand. */
        public Builder start() { this.mode = Mode.START; return this; }
 /** {@code run [cmd...]} subcommand. */
        public Builder run(List<String> cmd) { this.mode = Mode.RUN; this.runCommand = cmd; return this; }
 /** {@code validate} subcommand. */
        public Builder validate() { this.mode = Mode.VALIDATE; return this; }
 /** {@code coverage} subcommand. */
        public Builder coverage() { this.mode = Mode.COVERAGE; return this; }
 /** {@code sessions} subcommand. */
        public Builder sessions() { this.mode = Mode.SESSIONS; return this; }
 /** {@code query} subcommand. */
        public Builder query() { this.mode = Mode.QUERY; return this; }
 /** {@code blob} subcommand. */
        public Builder blob() { this.mode = Mode.BLOB; return this; }
 /** {@code purge} subcommand. */
        public Builder purge() { this.mode = Mode.PURGE; return this; }
 /** start/run:{@code --host} . */
        public Builder host(String host) { this.host = host; return this; }
 /** start/run:{@code --port} . */
        public Builder port(Integer port) { this.port = port; return this; }
 /** validate:{@code --json} . */
        public Builder json(boolean json) { this.json = json; return this; }
 /** validate:{@code --proxy-url} proxy URL. */
        public Builder proxyUrl(String proxyUrl) { this.proxyUrl = proxyUrl; return this; }
 /** validate:{@code --proxy-ca-file} CA bundle . */
        public Builder proxyCaFile(String proxyCaFile) { this.proxyCaFile = proxyCaFile; return this; }
 /** validate:{@code --allowed-url} URL. */
        public Builder allowedUrls(List<String> urls) { this.allowedUrls = urls; return this; }
 /** validate:{@code --denied-url} URL. */
        public Builder deniedUrls(List<String> urls) { this.deniedUrls = urls; return this; }
 /** validate:{@code --apns-reachable} APNs . */
        public Builder apnsReachable(boolean apnsReachable) { this.apnsReachable = apnsReachable; return this; }
        /** validate：{@code --apns-authority} APNs authority。 */
        public Builder apnsAuthority(String apnsAuthority) { this.apnsAuthority = apnsAuthority; return this; }
 /** validate:{@code --timeout-ms} timeoutmilliseconds. */
        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
 /** sessions:{@code --limit} session. */
        public Builder limit(Integer limit) { this.limit = limit; return this; }
 /** query:{@code --preset}(Required). */
        public Builder preset(String preset) { this.preset = preset; return this; }
 /** query:{@code --session} session id. */
        public Builder session(String session) { this.session = session; return this; }
 /** blob:{@code --id}(Required)Blob id. */
        public Builder blobId(String blobId) { this.blobId = blobId; return this; }

        /**
 * @return {@link ProxyOptions}
         */
        public ProxyOptions build() {
            return new ProxyOptions(this);
        }
    }
}
