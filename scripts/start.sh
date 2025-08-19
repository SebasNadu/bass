#!/bin/bash
set -euo pipefail

APP_NAME="bass"
AWS_REGION="ap-northeast-2"
export AWS_DEFAULT_REGION=$AWS_REGION

NEW_COLOR="green"
OLD_COLOR="blue"

# Check db availability
if ! docker ps -q --filter "name=bass-db-1" | grep -q .; then
  docker-compose -f /home/ubuntu/app/docker-compose.prod.yml up -d bass-db-1
fi

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

# Attempt ALB target group switch only if AWS CLI can access it
if command -v aws &> /dev/null; then
  INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

  # Hardcoded ARNs
  TG_BLUE="arn:aws:elasticloadbalancing:ap-northeast-2:843255971531:targetgroup/tg-bass-blue/c60c755039882121"
  TG_GREEN="arn:aws:elasticloadbalancing:ap-northeast-2:843255971531:targetgroup/tg-bass-green/491545447800a047"

  NEW_TG=$( [ "$NEW_COLOR" == "blue" ] && echo $TG_BLUE || echo $TG_GREEN )
  OLD_TG=$( [ "$OLD_COLOR" == "blue" ] && echo $TG_BLUE || echo $TG_GREEN )

  echo ">>> [ApplicationStart] Switching ALB target group to ${NEW_COLOR}..."
  if ! aws elbv2 register-targets --region $AWS_REGION --target-group-arn $NEW_TG --targets Id=$INSTANCE_ID,Port=$NEW_PORT 2>/dev/null; then
    echo "Warning: Unable to register targets (likely missing IAM permissions). Skipping."
  fi

  if ! aws elbv2 deregister-targets --region $AWS_REGION --target-group-arn $OLD_TG --targets Id=$INSTANCE_ID,Port=$OLD_PORT 2>/dev/null; then
    echo "Warning: Unable to deregister targets (likely missing IAM permissions). Skipping."
  fi
else
  echo ">>> AWS CLI not available, skipping ALB target group switch."
fi

echo ">>> [ApplicationStart] Stopping old ${OLD_COLOR} container..."
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml stop ${APP_NAME}-${OLD_COLOR} || true
docker-compose -f /home/ubuntu/app/docker-compose.prod.yml rm -f ${APP_NAME}-${OLD_COLOR} || true

echo ">>> [ApplicationStart] Deployment of ${NEW_COLOR} complete."
