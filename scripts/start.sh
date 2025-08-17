#!/bin/bash
set -euo pipefail

APP_NAME="bass"
NEW_COLOR="green"
OLD_COLOR="blue"

# Check which container is currently running
if docker ps -q --filter "name=${APP_NAME}-green" | grep -q .; then
  NEW_COLOR="blue"
  OLD_COLOR="green"
fi

echo ">>> [ApplicationStart] Starting new ${NEW_COLOR} container..."
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml up -d ${APP_NAME}-${NEW_COLOR}

echo ">>> [ApplicationStart] Waiting for health check..."
until curl -f http://localhost:$( [ "$NEW_COLOR" == "blue" ] && echo 8081 || echo 8082 )/actuator/health > /dev/null 2>&1; do
  echo "Waiting for ${NEW_COLOR} container..."
  sleep 5
done

echo ">>> [ApplicationStart] Switch ALB target group to ${NEW_COLOR}"
# Here you need AWS CLI command to register new target group and deregister old one
# Example (replace with actual target group ARNs):
# aws elbv2 register-targets --target-group-arn <NEW_TG_ARN> --targets Id=<EC2_ID>,Port=<PORT>
# aws elbv2 deregister-targets --target-group-arn <OLD_TG_ARN> --targets Id=<EC2_ID>,Port=<PORT>

echo ">>> [ApplicationStart] Stopping old ${OLD_COLOR} container..."
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml stop ${APP_NAME}-${OLD_COLOR} || true
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml rm -f ${APP_NAME}-${OLD_COLOR} || true
