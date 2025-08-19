#!/bin/bash
set -euo pipefail

COMPOSE_FILE="/home/ubuntu/app/docker-compose.prod.yml"

echo ">>> Stopping and removing existing containers..."
docker-compose -f $COMPOSE_FILE down

echo ">>> Stopping and removing existing volumes..."
docker-compose -f $COMPOSE_FILE down -v

echo ">>> Building fresh images..."
docker-compose -f $COMPOSE_FILE build --no-cache

echo ">>> Starting containers in detached mode..."
docker-compose -f $COMPOSE_FILE up -d

echo ">>> Deployment complete."
