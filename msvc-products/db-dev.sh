#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <on-off> <num2>"
    exit 1
fi

if [ "$1" == "on" ]; then
  echo "Starting service db in development mode..."
  if docker compose -f docker-compose-dev.yml up -d; then
      echo "Database is up and running."
  else
      echo "Service unavailable. Please check the logs."
      exit 1
  fi
else
  echo "Stopping service db..."
  if docker-compose -f docker-compose-dev.yml down; then
    echo "Database is stopped."
  else
    echo "Service unavailable. Please check the logs."
    exit 1
  fi
fi