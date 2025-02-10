#!/bin/bash

echo "Checking args to build or destroy the Docker image..."
if [ $# -ne  1 ]; then
  echo "Usage: $0 <build|destroy>"
  exit 1
fi

IMAGE_NAME="msvc-products"
IMAGE_TAG="latest"

if [ "$1" == "build" ]; then
  echo "Building the Docker image for the Eureka Server..."
  if docker build -t "$IMAGE_NAME:$IMAGE_TAG" .; then
    echo "Docker image built successfully."
  else
    echo "Failed to build the Docker image."
  fi
elif [ "$1" == "destroy" ]; then
  echo "Destroying the Docker image for the Eureka Server..."
  if docker rmi "$IMAGE_NAME:$IMAGE_TAG"; then
    echo "Docker image destroyed successfully."
  else
    echo "Failed to destroy the Docker image."
  fi
else
  echo "Invalid argument. Please use 'build' or 'destroy'."
  exit 1
fi