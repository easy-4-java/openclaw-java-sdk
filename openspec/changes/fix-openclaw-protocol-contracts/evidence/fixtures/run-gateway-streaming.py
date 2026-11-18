import concurrent.futures
import re
import json
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import threading
import os
from pathlib import Path
import secrets
import signal
import socket
import subprocess
import time
from urllib.request import urlopen

root = Path(__file__).resolve().parent

class LocalModel(BaseHTTPRequestHandler):
    def log_message(self, format, *args):
        pass

    def do_POST(self):
        payload = json.loads(self.rfile.read(int(self.headers["Content-Length"])))
        with (root / (prefix + "provider.log")).open("a") as report:
            report.write(json.dumps({"path": self.path, "model": payload.get("model"),
                                     "stream": payload.get("stream")}) + "\n")
        if self.path != "/v1/chat/completions" or payload.get("model") != "local-fixture" or not payload.get("stream"):
            self.send_error(400)
            return
        streams_started.wait(timeout=25)
        self.send_response(200)
        self.send_header("Content-Type", "text/event-stream; charset=utf-8")
        self.end_headers()
        def chunk(delta, finish=None):
            message = {"id": "local-run", "object": "chat.completion.chunk", "created": 1,
                       "model": "local-fixture", "choices": [{"index": 0, "delta": delta, "finish_reason": finish}]}
            self.wfile.write(("data: " + json.dumps(message, ensure_ascii=False) + "\n\n").encode())
            self.wfile.flush()
        chunk({"role": "assistant"})
        for content in ("Gateway ", "fixture ", "✓"):
            chunk({"content": content})
            time.sleep(0.25)
        chunk({}, "stop")
        self.wfile.write(b"data: [DONE]\n\n")
        self.wfile.flush()

provider = ThreadingHTTPServer(("127.0.0.1", 0), LocalModel)
threading.Thread(target=provider.serve_forever, daemon=True).start()
version = os.sys.argv[1]
workers = int(os.environ.get("FIXTURE_WORKERS", "3"))
streams_started = threading.Barrier(workers)
prefix = f"chat-provenance-{workers}-{version}-"
runtime = root / (("locked-" if version == "locked" else "") + "runtime/node_modules/openclaw")
token = secrets.token_hex(24)
with socket.socket() as reservation:
    reservation.bind(("127.0.0.1", 0))
    port = reservation.getsockname()[1]
http = f"http://127.0.0.1:{port}"
config = root / (prefix + "gateway.json")
config.write_text(json.dumps({
    "gateway": {"mode": "local", "bind": "loopback", "port": port,
                "auth": {"mode": "token", "allowTailscale": False}, "controlUi": {"enabled": False}},
    "agents": {"defaults": {"workspace": str(root / (prefix + "workspace")), "skipBootstrap": True,
                              "model": {"primary": "localprobe/local-fixture", "fallbacks": []}}},
    "models": {"mode": "replace", "providers": {
        "localprobe": {"baseUrl": f"http://127.0.0.1:{provider.server_port}/v1",
                       "apiKey": "local-fixture-key", "api": "openai-completions",
                       "models": [{"id": "local-fixture", "name": "Local fixture", "reasoning": False,
                                   "input": ["text"], "contextWindow": 32768, "maxTokens": 1024}]}
    }},
    "discovery": {"mdns": {"mode": "off"}},
    "cron": {"enabled": False},
    "plugins": {"enabled": False}
}))
isolated_home = root / (prefix + "gateway-home")
isolated_home.mkdir(exist_ok=True)
gateway_env = {"PATH": os.environ["PATH"], "HOME": str(isolated_home),
               "OPENCLAW_CONFIG_PATH": str(config), "OPENCLAW_STATE_DIR": str(root / (prefix + "gateway-state")),
               "OPENCLAW_GATEWAY_TOKEN": token, "OPENCLAW_DISABLE_BONJOUR": "1"}
def verify(args):
    line, jdk, test = args
    java = subprocess.check_output(["/usr/libexec/java_home", "-v", jdk], text=True).strip()
    env = dict(os.environ, JAVA_HOME=java,
               PATH=java + "/bin:" + os.environ["PATH"], SDK_PROBE_URL=f"ws://127.0.0.1:{port}",
               OPENCLAW_GATEWAY_TOKEN=token)
    with (root / f"{prefix}tests-{line}.log").open("w") as log:
        mvn = ["bash", "./mvnw"] if line == 3 else ["mvn"]
        subprocess.run(mvn + ["-version"], env=env, cwd=root / f"sdk-{line}", stdout=log, stderr=log)
        build = subprocess.run(mvn + ["-B", "-ntp", "-o",
                 "-Dmaven.repo.local=" + str(root / f"m2-{line}"),
                 "-Dmaven.repo.local.tail=" + str(Path.home() / ".m2/repository"),
                 "-Dtest=" + test, "test"],
                 env=env, cwd=root / f"sdk-{line}", stdout=log, stderr=log, timeout=120)
    return line, build.returncode

with (root / (prefix + "gateway-live.log")).open("w") as log:
    proc = subprocess.Popen(["node", str(runtime / "openclaw.mjs"), "gateway", "run"], env=gateway_env,
                            stdout=log, stderr=log, start_new_session=True)
    try:
        for attempt in range(100):
            if proc.poll() is not None:
                raise RuntimeError(f"Gateway exited with {proc.returncode}")
            try:
                with urlopen(http + "/readyz", timeout=1) as response:
                    reply = json.load(response)
                (root / (prefix + "ready.json")).write_text(json.dumps(reply))
                print("Gateway ready", flush=True)
                break
            except OSError:
                time.sleep(0.25)
        else:
            raise RuntimeError("Gateway did not become ready")
        runs = []
        with concurrent.futures.ThreadPoolExecutor(max_workers=workers) as executor:
            for line, result in executor.map(verify, tuple((line, jdk, "GatewayStreamingLiveFixtureTest") for line, jdk in ((1, "1.8"), (2, "17"), (3, "21")))):
                print(f"SDK line {line}: Maven exit {result}", flush=True)
                runs.append(result)
        if any(runs):
            raise SystemExit(1)
    finally:
        if proc.poll() is None:
            os.killpg(proc.pid, signal.SIGTERM)
            try:
                proc.wait(timeout=15)
            except subprocess.TimeoutExpired:
                os.killpg(proc.pid, signal.SIGKILL)
                proc.wait()
