# 完整文档结构检查报告（无损分段）

原始报告为 `validation-report.json`，127699 字节。为避免连接器长内容传递被截断，使用 gzip + Base64 并分为 3 段存储；不删改原报告的任何字段、检查项或格式。Markdown 摘要见 [validation-summary.md](../validation-summary.md)。

## 恢复原始 JSON

从仓库根目录运行以下命令，输出到当前目录的 `validation-report.json`：

```bash
python3 - <<'PYCODE'
import base64, gzip, hashlib
from pathlib import Path
parts = Path("docs/openclaw-integration/validation-report")
encoded = "".join(p.read_text().strip() for p in sorted(parts.glob("part-*.b64")))
raw = gzip.decompress(base64.b64decode(encoded, validate=True))
assert hashlib.sha256(raw).hexdigest() == "a689415d7e7c1a28cc50a97179c797adc29080b30f5ec929aa8ed23227a1a598"
Path("validation-report.json").write_bytes(raw)
PYCODE
```

原始 JSON SHA-256：`a689415d7e7c1a28cc50a97179c797adc29080b30f5ec929aa8ed23227a1a598`。
原始报告保留首次文档编制阶段的检查结果；不是官方 OpenSpec CLI 或 SDK 产品测试结果。
