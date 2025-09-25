#!/bin/bash

echo "Starting Library System API..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker first."
    exit 1
fi

# Build and start the services
echo "Building and starting services with Docker Compose..."
docker-compose up --build -d

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Check if services are running
echo "Checking service status..."
docker-compose ps

echo ""
echo "🚀 Library System API is now running!"
echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
echo "🔍 API Endpoints: http://localhost:8080/api-docs"
echo "💾 MongoDB: localhost:27017"
echo "⚡ Redis: localhost:6379"
echo ""
echo "To stop the services, run: docker-compose down"
