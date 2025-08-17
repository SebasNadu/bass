#!/bin/bash
set -euo pipefail

APP_NAME="bass"

echo ">>> [AfterInstall] Setting permissions"
sudo chown -R ubuntu:ubuntu /home/ubuntu/app
chmod +x /home/ubuntu/app/docker-compose.prod.yml
