"""Compare the two locked OpenClaw releases on disposable loopback Gateways."""
import json
import os
import secrets
import socket
import subprocess
import sys
import tempfile
import time
from pathlib import Path
from urllib.error import HTTPError
from urllib.request import ProxyHandler, Request, build_opener

SAFE_REQUEST = {"tool": "sessions_list", "args": {"limit": 1}, "sessionKey": "main"}
URL_OPENER = build_opener(ProxyHandler({}))


def request(base, body, token=None, scopes=None):
    headers = {"Content-Type": "application/json"}
    if token is not None:
        headers["Authorization"] = "Bearer " + token
    if scopes is not None:
        headers["x-openclaw-scopes"] = scopes
    call = Request(base + "/tools/invoke", json.dumps(body).encode(), headers)
    try:
        with URL_OPENER.open(call, timeout=8) as response:
            return response.status, json.load(response)
    except HTTPError as failure:
        with failure:
            return failure.code, json.load(failure)


def release(runtime, version):
    assert json.loads((runtime / "package.json").read_bytes())["version"] == version
    with tempfile.TemporaryDirectory(prefix="openclaw-tools-review-") as tmp:
        state = Path(tmp)
        token = secrets.token_hex(24)
        with socket.socket() as selected:
            selected.bind(("127.0.0.1", 0))
            port = selected.getsockname()[1]
        base = f"http://127.0.0.1:{port}"
        config = state / "config.json"
        config.write_text(json.dumps({
            "gateway": {"mode": "local", "bind": "loopback", "port": port,
                        "auth": {"mode": "token", "allowTailscale": False},
                        "controlUi": {"enabled": False}},
            "agents": {"defaults": {"workspace": str(state / "workspace"), "skipBootstrap": True}},
            "discovery": {"mdns": {"mode": "off"}},
            "cron": {"enabled": False}, "plugins": {"enabled": False},
        }))
        env = {
            "PATH": os.environ["PATH"], "OPENCLAW_HOME": tmp,
            "OPENCLAW_CONFIG_PATH": str(config), "OPENCLAW_STATE_DIR": str(state / "state"),
            "OPENCLAW_GATEWAY_TOKEN": token, "OPENCLAW_DISABLE_BONJOUR": "1",
        }
        log_path = state / "gateway.log"
        with log_path.open("w") as log:
            process = subprocess.Popen(["node", str(runtime / "openclaw.mjs"), "gateway", "run"],
                                       cwd=state, env=env, stdout=log, stderr=log)
            try:
                for _ in range(100):
                    if process.poll() is not None:
                        raise RuntimeError("Gateway exited: " + log_path.read_text()[-2000:])
                    try:
                        with URL_OPENER.open(base + "/readyz", timeout=1):
                            break
                    except OSError:
                        time.sleep(0.25)
                else:
                    raise RuntimeError("Gateway did not start")
                results = {}
                for label, payload, bearer, scopes in (
                        ("absent-token", SAFE_REQUEST, None, None),
                        ("incorrect-token", SAFE_REQUEST, "fixture-invalid", None),
                        ("shared-token", SAFE_REQUEST, token, None),
                        ("read-header", SAFE_REQUEST, token, "operator.read"),
                        ("dry-run", {**SAFE_REQUEST, "dryRun": True, "idempotencyKey": "fixture-v1"}, token, None),
                        ("missing-tool", {**SAFE_REQUEST, "tool": "scope_review_missing_tool"}, token, None),
                        ("mismatched-agent", {**SAFE_REQUEST, "sessionKey": "agent:main:main",
                                              "agentId": "fixture-other"}, token, None)):
                    code, body = request(base, payload, bearer, scopes)
                    error = body.get("error")
                    results[label] = {"status": code, "ok": body.get("ok"),
                                      "errorType": error.get("type") if isinstance(error, dict) else None,
                                      "hasResult": "result" in body}
                return results
            finally:
                process.terminate()
                try:
                    process.wait(timeout=10)
                except subprocess.TimeoutExpired:
                    process.kill()
                    process.wait()


def main():
    if len(sys.argv) != 3:
        raise SystemExit("usage: review-tools-invoke.py LOCKED_PACKAGE_DIR CANDIDATE_PACKAGE_DIR")
    expected = {
        "absent-token": (401, None, "unauthorized", False),
        "incorrect-token": (401, None, "unauthorized", False),
        "shared-token": (200, True, None, True),
        "read-header": (200, True, None, True),
        "dry-run": (200, True, None, True),
        "missing-tool": (404, False, "not_found", False),
        "mismatched-agent": (400, False, "invalid_request", False),
    }
    for path, version in zip(sys.argv[1:], ("2026.7.1-2", "2026.11.5"), strict=True):
        actual = release(Path(path).resolve(), version)
        for label, result in actual.items():
            observed = tuple(result[key] for key in ("status", "ok", "errorType", "hasResult"))
            if observed != expected[label]:
                raise AssertionError(f"{version} {label}: expected {expected[label]}, observed {observed}")
        print(version, json.dumps(actual, sort_keys=True), flush=True)


if __name__ == "__main__":
    main()
