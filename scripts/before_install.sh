#!/bin/bash
set -euo pipefail

APP_NAME="bass"
echo ">>> [BeforeInstall] Stopping old containers if running..."

docker ps -q --filter "name=${APP_NAME}-blue" | grep -q . && docker stop ${APP_NAME}-blue || true
docker ps -q --filter "name=${APP_NAME}-green" | grep -q . && docker stop ${APP_NAME}-green || true
docker ps -aq --filter "name=${APP_NAME}-blue" | grep -q . && docker rm ${APP_NAME}-blue || true
docker ps -aq --filter "name=${APP_NAME}-green" | grep -q . && docker rm ${APP_NAME}-green || true

echo ">>> [BeforeInstall] Prepare deploy directory"
sudo chown -R ubuntu:ubuntu /home/ubuntu/app
