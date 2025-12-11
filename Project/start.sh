#!/bin/bash

# AIU Trips and Events - Docker Quick Start Script

echo "================================================"
echo "AIU Trips and Events - Docker Setup (Main Project)"
echo "================================================"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker first."
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

# Check if Docker daemon is running
if ! docker info &> /dev/null; then
    echo "Error: Docker daemon is not running. Please start Docker first."
    exit 1
fi

echo "✓ Docker is installed and running"
echo ""

# Check for existing containers
if docker-compose ps | grep -q "Up"; then
    echo "Warning: Some containers are already running."
    read -p "Do you want to restart them? (y/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Stopping existing containers..."
        docker-compose down
    else
        echo "Keeping existing containers. Exiting."
        exit 0
    fi
fi

echo "Starting services..."
echo ""

# Build and start containers
docker-compose up -d --build

# Wait for services to be healthy
echo ""
echo "Waiting for services to start..."
sleep 10

# Check service status
echo ""
echo "Service Status:"
docker-compose ps

echo ""
echo "================================================"
echo "Setup Complete!"
echo "================================================"
echo ""
echo "Access the application:"
echo "  Frontend: http://localhost:3001"
echo "  Backend:  http://localhost:8081"
echo "  Database: localhost:5433"
echo ""
echo "Sample Credentials:"
echo "  Admin:    admin@aiu.edu / admin123"
echo "  Student:  john.doe@aiu.edu / password123"
echo "  Organizer: organizer@aiu.edu / password123"
echo ""
echo "Useful Commands:"
echo "  View logs:    docker-compose logs -f"
echo "  Stop:         docker-compose down"
echo "  Restart:      docker-compose restart"
echo ""
echo "For more information, see DOCKER_SETUP.md"
echo ""
