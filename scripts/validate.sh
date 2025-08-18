#!/bin/bash
set -euo pipefail

APP_NAME="bass"

echo ">>> [ValidateService] Checking app health..."

# Find which container is running
if docker ps -q --filter "name=${APP_NAME}-blue" | grep -q .; then
  PORT=8081
elif docker ps -q --filter "name=${APP_NAME}-green" | grep -q .; then
  PORT=8082
else
  echo "No running container found!"
  exit 1
fi

# Hit actuator health endpoint
STATUS=$(curl -s http://localhost:${PORT}/actuator/health | jq -r '.status')

if [ "$STATUS" != "UP" ]; then
  echo "App health check failed: $STATUS"
  exit 1
fi

echo ">>> [ValidateService] App is healthy"
