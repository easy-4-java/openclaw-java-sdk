"""Observe Responses HTTP and SSE behavior through a disposable real Gateway."""
import json
import os
import secrets
import select
import socket
import subprocess
import sys
import tempfile
import threading
import time
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.error import HTTPError
from urllib.request import ProxyHandler, Request, build_opener

OPENER = build_opener(ProxyHandler({}))
TOOLS = [{"type": "function", "name": "fixture_result", "parameters": {"type": "object", "properties": {}}}]


class Provider(BaseHTTPRequestHandler):
    def log_message(self, *_args):
        pass

    def do_POST(self):
        body = json.loads(self.rfile.read(int(self.headers["Content-Length"])))
        if self.path != "/v1/chat/completions" or body.get("model") != "local-fixture" or not body.get("stream"):
            self.send_error(400)
            return
        mode = self.server.mode
        self.server.requests += 1
        self.send_response(200)
        self.send_header("Content-Type", "text/event-stream")
        self.end_headers()

        def emit(delta, reason=None):
            out = {"id": "fixture", "object": "chat.completion.chunk", "created": 1,
                   "model": "local-fixture", "choices": [{"index": 0, "delta": delta, "finish_reason": reason}]}
            self.wfile.write(("data: " + json.dumps(out) + "\n\n").encode())
            self.wfile.flush()

        emit({"role": "assistant"})
        if mode == "tool":
            emit({"tool_calls": [{"index": 0, "id": "call_fixture", "type": "function",
                                  "function": {"name": "fixture_result", "arguments": "{}"}}]})
            emit({}, "tool_calls")
        else:
            emit({"content": "Gateway "})
            time.sleep(0.1)
            emit({"content": "fixture"})
            if mode == "hold":
                deadline = time.monotonic() + 8
                while time.monotonic() < deadline and not self.server.finished_hold.is_set():
                    readable, _, _ = select.select([self.connection], [], [], 0.2)
                    if readable and not self.connection.recv(1, socket.MSG_PEEK):
                        self.server.cancelled_connection.set()
                        return
            emit({}, "length" if mode == "length" else "stop")
        usage = {"id": "fixture", "object": "chat.completion.chunk", "created": 1, "model": "local-fixture",
                 "choices": [], "usage": {"prompt_tokens": 8, "completion_tokens": 2, "total_tokens": 10}}
        self.wfile.write(("data: " + json.dumps(usage) + "\n\ndata: [DONE]\n\n").encode())
        self.wfile.flush()


def open_request(base, token, user, mode, provider, session_header=None, **parameters):
    provider.mode = mode
    body = {"model": "openclaw", "user": user, **parameters}
    headers = {"Authorization": "Bearer " + token, "Content-Type": "application/json"}
    if session_header is not None:
        headers["x-openclaw-session-key"] = session_header
    req = Request(base + "/v1/responses", json.dumps(body).encode(), headers)
    try:
        return OPENER.open(req, timeout=25)
    except HTTPError as error:
        return error


def request(*args, **kwargs):
    with open_request(*args, **kwargs) as response:
        if response.headers.get_content_type() != "text/event-stream":
            return response.status, json.load(response)
        frames = []
        event = None
        for raw in response:
            line = raw.decode().rstrip("\r\n")
            if line.startswith("event: "):
                event = line[7:]
            if line.startswith("data: "):
                data = line[6:]
                frames.append(("done", None) if data == "[DONE]" else (event, json.loads(data)))
        return response.status, frames


def probe(base, token, provider, candidate):
    modes = (
        ("normal", {}, "response.completed", "completed", ["Gateway", " fixture"]),
        ("length", {}, "response.incomplete" if candidate else "response.completed",
         "incomplete" if candidate else "failed", ["Gateway", " fixture"]),
        ("no-required-tool", {"tools": TOOLS, "tool_choice": "required"}, "response.failed", "failed", []),
    )
    for mode, parameters, event, state, text in modes:
        code, frames = request(base, token, mode, mode, provider, input="fixture", stream=True, **parameters)
        assert code == 200 and isinstance(frames, list), (mode, code, frames)
        terminal = [(name, (data or {}).get("response", {}).get("status"))
                    for name, data in frames if name in ("response.completed", "response.failed",
                                                         "response.incomplete", "done")]
        assert terminal == [(event, state), ("done", None)], (mode, terminal)
        assert [data.get("delta") for name, data in frames if name == "response.output_text.delta"] == text
        print(mode, event, state, flush=True)

    code, first = request(base, token, "continuation", "normal", provider, input="first", store=False)
    assert code == 200 and first["status"] == "completed"
    response_id = first["id"]
    code, second = request(base, token, "continuation", "normal", provider,
                           input="continue", previous_response_id=response_id)
    assert code == 200 and second["status"] == "completed"

    code, changed_user = request(base, token, "other-user", "normal", provider,
                                 input="continue", previous_response_id=response_id)
    assert code == 200 and changed_user["status"] == "completed"
    if candidate:
        before = provider.requests
        code, scoped = request(base, token, "continuation", "normal", provider,
                               session_header="agent:main:fixture-other", input="continue",
                               previous_response_id=response_id)
        assert code == 400 and scoped["error"]["type"] == "invalid_request_error"
        assert provider.requests == before
        print("explicit session mismatch rejected before model", flush=True)
    print("valid continuation and changed JSON user both accepted", flush=True)

    for stream in (False, True):
        before = provider.requests
        code, unknown = request(base, token, "unknown-" + str(stream), "normal", provider,
                                previous_response_id="resp_fixture_missing", input="fixture", stream=stream)
        if candidate:
            assert code == 400 and unknown["error"]["type"] == "invalid_request_error"
            assert provider.requests == before
        else:
            assert code == 200 and provider.requests > before
        print("unknown continuation", "SSE" if stream else "JSON", code, flush=True)

    code, tool = request(base, token, "tool", "tool", provider, input="fixture", tools=TOOLS)
    if candidate:
        assert code == 500 and tool["status"] == "failed" and tool["error"]["code"] == "api_error"
        print("candidate client tool fixture failed; empty tool continuation remains unverified", flush=True)
    else:
        assert code == 200 and tool["status"] == "incomplete"
        call, = [item for item in tool["output"] if item.get("type") == "function_call"]
        code, continuation = request(base, token, "tool", "normal", provider, previous_response_id=tool["id"],
                                     input=[{"type": "function_call_output", "call_id": call["call_id"], "output": ""}])
        assert code == 400 and continuation["error"]["type"] == "invalid_request_error"
        print("locked release returned a tool call and rejected empty tool output", flush=True)



def verify_disconnect(base, token, provider):
    provider.cancelled_connection = threading.Event()
    provider.finished_hold = threading.Event()
    before = provider.requests
    try:
        with open_request(base, token, "cancel-probe", "hold", provider, input="fixture", stream=True) as response:
            assert response.status == 200
            assert response.headers.get_content_type() == "text/event-stream"
            for raw in response:
                if raw.startswith(b"event: response.output_text.delta"):
                    break
            else:
                raise AssertionError("expected a text delta before disconnect")
        started = time.monotonic()
        assert provider.cancelled_connection.wait(timeout=9), "provider socket remained open after disconnect"
        print(f"downstream disconnect closed provider socket in {time.monotonic() - started:.3f}s", flush=True)
    finally:
        provider.finished_hold.set()
    assert provider.requests == before + 1
    status, next_turn = request(base, token, "cancel-probe", "normal", provider, input="after disconnect")
    assert status == 200 and next_turn["status"] == "completed"
    print("same session accepted another turn", flush=True)


def release(runtime, version):
    assert json.loads((runtime / "package.json").read_bytes())["version"] == version
    with tempfile.TemporaryDirectory(prefix="openclaw-responses-review-") as tmp, \
            ThreadingHTTPServer(("127.0.0.1", 0), Provider) as provider:
        state = Path(tmp)
        provider.mode = "normal"
        provider.requests = 0
        server_thread = threading.Thread(target=provider.serve_forever, daemon=True)
        server_thread.start()
        token = secrets.token_hex(24)
        with socket.socket() as reserved:
            reserved.bind(("127.0.0.1", 0))
            port = reserved.getsockname()[1]
        base = f"http://127.0.0.1:{port}"
        candidate = version == "2026.11.5"
        catalog = {"catalogRefresh": {"enabled": False}} if candidate else {}
        config = state / "gateway.json"
        config.write_text(json.dumps({
            "gateway": {"mode": "local", "bind": "loopback", "port": port,
                        "auth": {"mode": "token", "allowTailscale": False},
                        "http": {"endpoints": {"responses": {"enabled": True}}},
                        "controlUi": {"enabled": False}},
            "agents": {"defaults": {"workspace": str(state / "workspace"), "skipBootstrap": True,
                                    "model": {"primary": "localprobe/local-fixture", "fallbacks": []}}},
            "models": {"mode": "replace", **catalog, "providers": {
                "localprobe": {"baseUrl": f"http://127.0.0.1:{provider.server_port}/v1",
                               "apiKey": "local-fixture-key", "api": "openai-completions",
                               "models": [{"id": "local-fixture", "name": "Local fixture", "reasoning": False,
                                           "input": ["text"], "contextWindow": 32768, "maxTokens": 1024}]}}},
            "discovery": {"mdns": {"mode": "off"}}, "cron": {"enabled": False}, "plugins": {"enabled": False},
        }))
        env = {"PATH": os.environ["PATH"], "OPENCLAW_HOME": tmp, "OPENCLAW_CONFIG_PATH": str(config),
               "OPENCLAW_STATE_DIR": str(state / "state"), "OPENCLAW_GATEWAY_TOKEN": token,
               "OPENCLAW_DISABLE_BONJOUR": "1", "OPENCLAW_NO_AUTO_UPDATE": "1"}
        log_path = state / "gateway.log"
        with log_path.open("w") as log:
            child = subprocess.Popen(["node", str(runtime / "openclaw.mjs"), "gateway", "run"],
                                     cwd=state, env=env, stdout=log, stderr=log)
            try:
                for _ in range(100):
                    if child.poll() is not None:
                        raise RuntimeError("Gateway exited: " + log_path.read_text()[-1500:])
                    try:
                        with OPENER.open(base + "/readyz", timeout=1):
                            break
                    except OSError:
                        time.sleep(0.25)
                else:
                    raise RuntimeError("Gateway did not start")
                print(version, flush=True)
                probe(base, token, provider, candidate)
                verify_disconnect(base, token, provider)
            finally:
                child.terminate()
                try:
                    child.wait(timeout=10)
                except subprocess.TimeoutExpired:
                    child.kill()
                    child.wait()
                provider.shutdown()


if __name__ == "__main__":
    if len(sys.argv) != 3:
        raise SystemExit("usage: review-responses.py LOCKED_PACKAGE_DIR CANDIDATE_PACKAGE_DIR")
    for directory, version in zip(sys.argv[1:], ("2026.7.1-2", "2026.11.5"), strict=True):
        release(Path(directory).resolve(), version)
