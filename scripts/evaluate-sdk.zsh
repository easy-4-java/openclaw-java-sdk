#!/bin/zsh
set -uo pipefail

repo_root=${0:a:h:h}
package_root=src/main/java/io/github/easy4j/openclaw
result_dir=$repo_root/target/benchmark
failed=0

for branch in feature/1.0.x feature/2.0.x feature/3.0.x; do
  print "STATIC openclaw $branch"
  git -C "$repo_root" cat-file -e \
    "${branch}:${package_root}/api/OpenClawSseClient.java" || failed=1
  git -C "$repo_root" cat-file -e \
    "${branch}:${package_root}/api/sse/SseSubscription.java" || failed=1
  if ! git -C "$repo_root" grep -q -E \
      'public[[:space:]]+OpenClawSseClient[[:space:]]+sse\(\)' \
      "$branch" -- "$package_root/OpenClawClient.java"; then
    print "FAIL missing OpenClawClient.sse(): $branch"
    failed=1
  fi
  if git -C "$repo_root" grep -n -E \
      'eventClient|[[:space:]]events\(\)|public[[:space:]]+void[[:space:]]+stop\(\)|subscribeSessionStream\(|Thread\.sleep|\.execute\(\)|Netty event-loop' \
      "$branch" -- "$package_root/api/*HttpClient.java" \
      "$package_root/api/*ChatClient.java" "$package_root/api/*SseClient.java"; then
    print "FAIL legacy/blocking SSE implementation remains: $branch"
    failed=1
  fi
done

if [[ "$(git -C "$repo_root" branch --show-current)" != feature/3.0.x ]]; then
  print 'FAIL feature/3.0.x must be checked out for latest verification'
  failed=1
else
  JAVA_HOME=${JAVA_HOME:-$(/usr/libexec/java_home -v 21)}
  PATH="$JAVA_HOME/bin:$PATH" mvn -q -f "$repo_root/pom.xml" \
    -Dtest=OpenClawSseApiShapeTest,OpenClawNonBlockingConcurrencyTest test || failed=1
fi

python3 "$repo_root/scripts/verify-benchmark-results.py" \
  --sdk openclaw \
  --results "$result_dir/benchmark-results.csv" \
  --branches "$result_dir/branch-verification.tsv" || failed=1

if (( failed != 0 )); then
  print 'OPENCLAW_SDK_EVALUATOR_FAIL'
  exit 1
fi
print 'OPENCLAW_SDK_EVALUATOR_PASS'
