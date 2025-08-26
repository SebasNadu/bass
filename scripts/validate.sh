#!/bin/bash
set -euo pipefail

echo ">>> [ValidateService] Checking app health..."

PORT=80
for i in {1..10}; do
  STATUS=$(curl -s http://localhost:${PORT}/actuator/health | jq -r '.status' || echo "DOWN")
  if [ "$STATUS" == "UP" ]; then
    echo ">>> [ValidateService] App is healthy"
    exit 0
  fi
  echo "Waiting for app to be UP... ($i/10)"
  sleep 5
done
echo "App health check failed"
exit 1
