#!/bin/bash

print_usage() {
    echo "Usage: $0 <on | off>"
    exit 1
}

if [ $# -ne 1 ]; then
    print_usage
fi

COMPOSE_FILE="docker-compose.yml"

start_application() {
    echo "Building Docker container for Spring Boot app"
    if docker compose -f "$COMPOSE_FILE" up; then
        echo "Application started successfully"
    else
        echo "Failed to build application"
        exit 1
    fi
}

stop_application() {
    echo "Stopping container"
    if docker compose -f "$COMPOSE_FILE" down; then
        echo "Container stopped successfully"
    else
        echo "Failed to stop application"
        exit 1
    fi
}

case "$1" in
    on)
        start_application
        ;;
    off)
        stop_application
        ;;
    *)
        print_usage
        ;;
esac