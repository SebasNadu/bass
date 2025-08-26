#!/bin/bash
set -euo pipefail

COMPOSE_FILE="/home/ubuntu/app/docker-compose.prod.yml"

echo ">>> Stopping and removing existing Containers..."
docker-compose -f $COMPOSE_FILE down

echo ">>> Building fresh images..."
docker-compose -f $COMPOSE_FILE build

echo ">>> Starting containers in detached mode..."
docker-compose -f $COMPOSE_FILE up -d

echo ">>> Cleaning up unused Docker resources..."
docker system prune -af --volumes

echo ">>> Deployment complete."
