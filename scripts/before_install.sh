#!/bin/bash
set -euo pipefail

APP_NAME="bass"
echo ">>> [BeforeInstall] Preparing environment..."

echo ">>> Updating system packages..."
sudo apt-get update -y && sudo apt-get upgrade -y

echo ">>> Installing AWS CLI if missing..."
if ! command -v aws &> /dev/null; then
  sudo apt-get install -y awscli
else
  echo "AWS CLI already installed."
fi

echo ">>> Installing Docker if missing..."
if ! command -v docker &> /dev/null; then
  sudo apt-get install -y docker.io
  sudo systemctl enable docker
  sudo systemctl start docker
  sudo usermod -aG docker ubuntu
else
  echo "Docker already installed."
fi

echo ">>> Installing Docker Compose if missing..."
if ! command -v docker-compose &> /dev/null; then
  sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
else
  echo "Docker Compose already installed."
fi

echo ">>> [BeforeInstall] Stopping old containers if running..."
docker ps -q --filter "name=${APP_NAME}-blue" | grep -q . && docker stop ${APP_NAME}-blue || true
docker ps -q --filter "name=${APP_NAME}-green" | grep -q . && docker stop ${APP_NAME}-green || true
docker ps -aq --filter "name=${APP_NAME}-blue" | grep -q . && docker rm ${APP_NAME}-blue || true
docker ps -aq --filter "name=${APP_NAME}-green" | grep -q . && docker rm ${APP_NAME}-green || true

echo ">>> [BeforeInstall] Prepare deploy directory"
sudo chown -R ubuntu:ubuntu /home/ubuntu/app
