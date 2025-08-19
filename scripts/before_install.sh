#!/bin/bash
set -euo pipefail

DEPLOY_DIR="/home/ubuntu/app"

echo ">>> [BeforeInstall] Preparing environment..."

# Update system
echo ">>> Updating system packages..."
sudo apt-get update -y && sudo apt-get upgrade -y

# Install AWS CLI v2 if missing or old version
echo ">>> Installing AWS CLI v2 if missing..."
if ! command -v aws &> /dev/null || [[ $(aws --version 2>&1) != *aws-cli/2* ]]; then
  sudo apt-get update -y && sudo apt-get upgrade -y
  sudo apt-get remove -y awscli || true
  curl "https://awscli.amazonaws.com/awscli-exe-linux-$(uname -m).zip" -o "awscliv2.zip"
  unzip -o awscliv2.zip
  sudo ./aws/install
  aws --version
  rm -rf awscliv2.zip aws
else
  echo "AWS CLI v2 already installed."
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
