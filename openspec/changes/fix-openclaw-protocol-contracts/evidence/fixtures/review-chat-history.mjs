import assert from 'node:assert/strict';
import { spawn } from 'node:child_process';
import { randomBytes } from 'node:crypto';
import { once } from 'node:events';
import { mkdtemp, readFile, rm, writeFile } from 'node:fs/promises';
import net from 'node:net';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { setTimeout as delay } from 'node:timers/promises';

const sessionKey = 'agent:main:history-review';
const scopes = ['operator.read', 'operator.write', 'operator.admin'];

async function probe(base, token, version) {
  const socket = new WebSocket(base.replace('http:', 'ws:'));
  let challenge;
  const challengeReady = new Promise(resolve => { challenge = resolve; });
  const pending = new Map();
  let id = 0;
  socket.addEventListener('message', event => {
    const frame = JSON.parse(event.data);
    if (frame.event === 'connect.challenge') challenge();
    if (frame.type === 'res') pending.get(frame.id)?.(frame);
  });
  function rpc(method, params) {
    return new Promise((resolve, reject) => {
      const key = String(++id);
      const timeout = setTimeout(() => {
        pending.delete(key);
        reject(new Error('RPC timeout: ' + method));
      }, 8000);
      pending.set(key, frame => {
        clearTimeout(timeout);
        pending.delete(key);
        resolve(frame);
      });
      socket.send(JSON.stringify({ type: 'req', id: key, method, params }));
    });
  }
  async function ok(method, params) {
    const response = await rpc(method, params);
    assert.equal(response.ok, true, method + ': ' + JSON.stringify(response.error));
    return response.payload;
  }
  try {
    await Promise.race([challengeReady, delay(8000, null, { ref: false }).then(() => { throw new Error('challenge timeout'); })]);
    await ok('connect', {
      minProtocol: 4, maxProtocol: 4,
      client: { id: 'gateway-client', mode: 'backend', version: 'history-review', platform: 'node' },
      role: 'operator', scopes, auth: { token },
    });
    const created = await ok('sessions.create', { key: sessionKey });
    assert.equal(created.runStarted, false);
    for (let n = 1; n <= 4; ++n) {
      await ok('chat.inject', { sessionKey, message: 'history-review-' + n });
    }
    const tail = await ok('chat.history', { sessionKey, limit: 2 });
    const ids = result => result.messages.map(message => {
      const id = message.__openclaw?.id ?? message.messageId ?? message.id;
      assert.equal(typeof id, 'string', 'message identity is missing');
      return id;
    });
    const tailIds = ids(tail);
    assert.equal(tail.sessionId, created.sessionId);
    assert.equal(tailIds.length, 2);
    assert.equal(tail.sessionInfo.hasActiveRun, false);
    assert.deepEqual(tail.sessionInfo.activeRunIds, []);
    if (version === '2026.7.1-2') {
      for (const key of ['olderCursor', 'newerCursor', 'deltaCursor', 'nextOffset']) {
        assert.equal(Object.hasOwn(tail, key), false, key + ' must be absent on the locked release');
      }
      const cursor = await rpc('chat.history', { sessionKey, cursor: 'history-fixture-cursor' });
      assert.equal(cursor.ok, false);
      assert.equal(cursor.error.code, 'INVALID_REQUEST');
      console.log(version, 'tail and idle state available; cursor absent and cursor input rejected');
      return;
    }
    assert.equal(tail.hasMore, true);
    assert.equal(typeof tail.olderCursor, 'string');
    assert.equal(typeof tail.deltaCursor, 'string');
    assert.equal(typeof tail.nextOffset, 'number');
    const older = await ok('chat.history', { sessionKey, cursor: tail.olderCursor, limit: 2 });
    const olderIds = ids(older);
    assert.equal(olderIds.length, 2);
    assert.equal(older.sessionId, tail.sessionId);
    assert.equal(new Set([...tailIds, ...olderIds]).size, 4);
    assert.equal(Object.hasOwn(older, 'olderCursor'), false);
    assert.equal(typeof older.newerCursor, 'string');
    const newer = await ok('chat.history', { sessionKey, cursor: older.newerCursor, limit: 2 });
    assert.deepEqual(ids(newer), tailIds);
    assert.equal(Object.hasOwn(newer, 'newerCursor'), false);

    const unchanged = await ok('chat.history', { sessionKey, cursor: tail.deltaCursor });
    assert.equal(unchanged.kind, 'delta');
    assert.deepEqual(unchanged.messages, []);
    assert.equal(unchanged.sessionInfo.sessionId, tail.sessionId);
    assert.equal(typeof unchanged.deltaCursor, 'string');

    await ok('chat.inject', { sessionKey, message: 'history-review-5' });
    const delta = await ok('chat.history', { sessionKey, cursor: unchanged.deltaCursor });
    assert.equal(delta.kind, 'delta');
    const deltaIds = ids(delta);
    assert.equal(deltaIds.length, 1);
    assert.equal(new Set([...tailIds, ...olderIds, ...deltaIds]).size, 5);
    assert.equal(typeof delta.deltaCursor, 'string');

    await ok('sessions.reset', { key: sessionKey });
    const stale = await ok('chat.history', { sessionKey, cursor: delta.deltaCursor });
    assert.equal(stale.kind, 'reset');
    assert.equal(Object.hasOwn(stale, 'messages'), false);
    const fresh = await ok('chat.history', { sessionKey, limit: 2 });
    assert.equal(fresh.messages.length, 1);
    assert.equal(fresh.messages[0].role, 'system');
    assert.equal(fresh.messages[0].__openclaw.kind, 'reset');
    assert.equal(new Set([...tailIds, ...olderIds, ...deltaIds, ...ids(fresh)]).size, 6);
    console.log(version, 'directional pages, empty and one-message deltas, and reset verified');
  } finally {
    socket.close();
  }
}

async function release(runtime, version) {
  const { version: actual } = JSON.parse(await readFile(path.join(runtime, 'package.json')));
  assert.equal(actual, version);
  const work = await mkdtemp(path.join(tmpdir(), 'openclaw-history-review-'));
  const reserved = net.createServer();
  reserved.listen(0, '127.0.0.1');
  await once(reserved, 'listening');
  const port = reserved.address().port;
  reserved.close();
  await once(reserved, 'close');
  const base = 'http://127.0.0.1:' + port;
  const config = path.join(work, 'gateway.json');
  const token = randomBytes(24).toString('hex');
  await writeFile(config, JSON.stringify({
    gateway: { mode: 'local', bind: 'loopback', port, auth: { mode: 'token', allowTailscale: false },
      controlUi: { enabled: false } },
    agents: { defaults: { workspace: path.join(work, 'workspace'), skipBootstrap: true } },
    discovery: { mdns: { mode: 'off' } }, cron: { enabled: false }, plugins: { enabled: false },
  }));
  const child = spawn(process.execPath, [path.join(runtime, 'openclaw.mjs'), 'gateway', 'run'], {
    cwd: work,
    env: { PATH: process.env.PATH, OPENCLAW_HOME: work, OPENCLAW_CONFIG_PATH: config,
      OPENCLAW_STATE_DIR: path.join(work, 'state'), OPENCLAW_GATEWAY_TOKEN: token, OPENCLAW_DISABLE_BONJOUR: '1' },
    stdio: ['ignore', 'ignore', 'ignore'],
  });
  try {
    for (let attempt = 0; attempt < 100; ++attempt) {
      assert.equal(child.exitCode, null, 'Gateway exited before readiness');
      const ready = await fetch(base + '/readyz', { signal: AbortSignal.timeout(1000) }).catch(() => null);
      if (ready?.ok) {
        await ready.body.cancel();
        await probe(base, token, version);
        return;
      }
      await delay(250);
    }
    throw new Error('Gateway did not become ready');
  } finally {
    if (child.exitCode === null && child.signalCode === null) {
      const stopped = once(child, 'exit');
      child.kill();
      await stopped;
    }
    await rm(work, { recursive: true });
  }
}

if (process.argv.length !== 4) {
  throw new Error('usage: node review-history.mjs LOCKED_PACKAGE_DIR CANDIDATE_PACKAGE_DIR');
}
for (let index = 0; index < 2; ++index) {
  await release(path.resolve(process.argv[index + 2]), ['2026.7.1-2', '2026.11.5'][index]);
}
