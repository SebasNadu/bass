#!/bin/bash
set -euo pipefail

APP_NAME="bass"
AWS_REGION="ap-northeast-2"

# Export region so AWS CLI uses it
export AWS_DEFAULT_REGION=$AWS_REGION

NEW_COLOR="green"
OLD_COLOR="blue"

# Check which container is currently running
if docker ps -q --filter "name=${APP_NAME}-green" | grep -q .; then
  NEW_COLOR="blue"
  OLD_COLOR="green"
fi

echo ">>> [ApplicationStart] Starting new ${NEW_COLOR} container..."
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml up -d ${APP_NAME}-${NEW_COLOR}

# Determine ports based on color
if [ "$NEW_COLOR" == "blue" ]; then
    NEW_PORT=8081
    OLD_PORT=8082
else
    NEW_PORT=8082
    OLD_PORT=8081
fi

# Wait for health check
timeout=60
elapsed=0
until curl -f http://localhost:$NEW_PORT/actuator/health > /dev/null 2>&1; do
  echo "Waiting for ${NEW_COLOR} container to be healthy..."
  sleep 5
  elapsed=$((elapsed + 5))
  if [ $elapsed -ge $timeout ]; then
    echo "Health check failed for ${NEW_COLOR} container after $timeout seconds."
    exit 1
  fi
done

echo ">>> [ApplicationStart] Fetching Target Group ARNs..."
TG_BLUE=$(aws elbv2 describe-target-groups --region $AWS_REGION --names tg-bass-blue --query "TargetGroups[0].TargetGroupArn" --output text)
TG_GREEN=$(aws elbv2 describe-target-groups --region $AWS_REGION --names tg-bass-green --query "TargetGroups[0].TargetGroupArn" --output text)

NEW_TG=$( [ "$NEW_COLOR" == "blue" ] && echo $TG_BLUE || echo $TG_GREEN )
OLD_TG=$( [ "$OLD_COLOR" == "blue" ] && echo $TG_BLUE || echo $TG_GREEN )

# Get current instance ID
INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

echo ">>> [ApplicationStart] Switching ALB target group to ${NEW_COLOR}..."
aws elbv2 register-targets --region $AWS_REGION --target-group-arn $NEW_TG --targets Id=$INSTANCE_ID,Port=$NEW_PORT
aws elbv2 deregister-targets --region $AWS_REGION --target-group-arn $OLD_TG --targets Id=$INSTANCE_ID,Port=$OLD_PORT

echo ">>> [ApplicationStart] Stopping old ${OLD_COLOR} container..."
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml stop ${APP_NAME}-${OLD_COLOR} || true
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml rm -f ${APP_NAME}-${OLD_COLOR} || true

echo ">>> [ApplicationStart] Deployment of ${NEW_COLOR} complete."
