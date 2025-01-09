#!/bin/sh
echo "Initializing localstack"

#Ensure the script will exit if any command fails
set -e

#Variables
TOPIC_NAME="christmas-letters-topic"
QUEUE_NAME="christmas-letters-queue"
TABLE_NAME="christmas_letter"

echo "Creating SNS topic $TOPIC_NAME"
awslocal sns create-topic --name $TOPIC_NAME || true

echo "Creating SQS queue $QUEUE_NAME"
awslocal sqs create-queue --queue-name $QUEUE_NAME || true

echo "Creating DynamoDB Table $TABLE_NAME"
awslocal dynamodb create-table \
  --table-name $TABLE_NAME \
  --attribute-definitions AttributeName=Email,AttributeType=S \
  --key-schema AttributeName=Email,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 || true

echo "Initializing the subscription to the topic"
awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:christmas-letters-topic --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:christmas-letters-queue || true

echo "Resource initialization is complete"