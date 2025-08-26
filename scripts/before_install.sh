#!/bin/bash
set -euo pipefail

DEPLOY_DIR="/home/ubuntu/app"

echo ">>> [BeforeInstall] Preparing environment..."

# Update system
echo ">>> Updating system packages..."
sudo apt-get update -y && sudo apt-get upgrade -y

if ! command -v jq &> /dev/null; then
  echo ">>> Installing jq..."
  sudo apt-get install -y jq
else
  echo "jq already installed."
fi

# Install Docker if missing
echo ">>> Installing Docker if missing..."
if ! command -v docker &> /dev/null; then
  sudo apt-get install -y docker.io
  sudo systemctl enable docker
  sudo systemctl start docker
  sudo usermod -aG docker ubuntu
else
  echo "Docker already installed."
fi

# Install Docker Compose if missing
echo ">>> Installing Docker Compose if missing..."
if ! command -v docker-compose &> /dev/null; then
  sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
else
  echo "Docker Compose already installed."
fi

# Prepare deploy directory
echo ">>> [BeforeInstall] Preparing deploy directory..."
sudo mkdir -p $DEPLOY_DIR
sudo chown -R ubuntu:ubuntu $DEPLOY_DIR
