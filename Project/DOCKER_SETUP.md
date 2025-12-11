# Docker Setup Guide - Main Project

This guide explains how to run the AIU Trips and Events application (Main Project) using Docker Compose.

## Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+
- At least 4GB of available RAM

## Quick Start

1. **Start all services:**
   ```bash
   ./start.sh
   ```

2. **Access the application:**
   - Frontend: http://localhost:3001
   - Backend API: http://localhost:8081
   - PostgreSQL: localhost:5433

3. **Stop all services:**
   ```bash
   docker-compose down
   ```

## Services

### Frontend (Next.js)
- **Port:** 3001
- **Container:** aiu-trips-frontend-main
- **Tech Stack:** Next.js 15, React 19, TypeScript, Tailwind CSS

### Backend (Spring Boot)
- **Port:** 8081
- **Container:** aiu-trips-backend-main
- **Tech Stack:** Spring Boot 3.2, Java 17, JPA/Hibernate

### Database (PostgreSQL)
- **Port:** 5433 (mapped to container port 5432)
- **Container:** aiu-trips-db-main
- **Database:** tripsdb
- **Username:** aiu_user
- **Password:** aiu_password

## Note: Different Ports

This main project uses different ports than the PM3 project to avoid conflicts:
- **Frontend:** Port 3001 (PM3 uses 3000)
- **Backend:** Port 8081 (PM3 uses 8080)
- **Database:** Port 5433 (PM3 uses 5432)

Both projects can run simultaneously!

## Sample Credentials

The database is automatically seeded with sample data on first run:

### Admin Account
- Email: `admin@aiu.edu`
- Password: `admin123`

### Student Accounts
- Email: `john.doe@aiu.edu` - Password: `password123`
- Email: `jane.smith@aiu.edu` - Password: `password123`
- Email: `mike.johnson@aiu.edu` - Password: `password123`
- Email: `sarah.williams@aiu.edu` - Password: `password123`

### Organizer Account
- Email: `organizer@aiu.edu`
- Password: `password123`

## Sample Data

The seeder creates:
- 5 users (1 admin, 3 students, 1 organizer)
- 6 events/trips (AI conference, mountain hiking, career fair, beach getaway, web workshop, cultural festival)
- 3 bookings with confirmed status
- 3 feedback entries with ratings
- 5 welcome notifications

## Commands

### Start Services
```bash
# Start all services in background
docker-compose up -d

# Start and view logs
docker-compose up

# Start specific service
docker-compose up -d backend
```

### Stop Services
```bash
# Stop all services
docker-compose down

# Stop and remove volumes (deletes database data)
docker-compose down -v
```

### View Logs
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres

# Follow logs in real-time
docker-compose logs -f backend
```

### Rebuild Services
```bash
# Rebuild all services
docker-compose build

# Rebuild specific service
docker-compose build backend

# Rebuild and restart
docker-compose up -d --build
```

### Database Management
```bash
# Access PostgreSQL CLI
docker-compose exec postgres psql -U aiu_user -d tripsdb

# Backup database
docker-compose exec postgres pg_dump -U aiu_user tripsdb > backup.sql

# Restore database
docker-compose exec -T postgres psql -U aiu_user -d tripsdb < backup.sql
```

## Development Workflow

### Making Code Changes

**Backend:**
1. Make changes to Java code
2. Rebuild: `docker-compose build backend`
3. Restart: `docker-compose up -d backend`

**Frontend:**
1. Make changes to TypeScript/React code
2. Rebuild: `docker-compose build frontend`
3. Restart: `docker-compose up -d frontend`

### Hot Reload (Development Mode)

For development with hot reload, you can run services outside Docker:

**Backend:**
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Frontend:**
```bash
cd frontend
npm run dev
```

## Running Both Projects Simultaneously

You can run both the main project and the PM3 project at the same time:

**Main Project (this one):**
- Frontend: http://localhost:3001
- Backend: http://localhost:8081
- Database: localhost:5433

**PM3 Project:**
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Database: localhost:5432

To start both:
```bash
# Terminal 1 - Main Project
cd Project
./start.sh

# Terminal 2 - PM3 Project
cd Milestones/PM_3/Project_without_DP_UML
./start.sh
```

## Troubleshooting

### Port Already in Use
If you get port conflicts:
```bash
# Check ports
sudo lsof -i :3001
sudo lsof -i :8081
sudo lsof -i :5433

# Kill process if needed
sudo kill -9 <PID>
```

### Database Connection Issues
```bash
# Check database status
docker-compose ps postgres

# View database logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

### Build Failures
```bash
# Clean build
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Reset Everything
To start fresh with a clean database:
```bash
docker-compose down -v
docker-compose up -d
```

## Environment Variables

You can override default values using environment variables:

Create a `.env` file in the project root:
```env
# Database
POSTGRES_DB=tripsdb
POSTGRES_USER=aiu_user
POSTGRES_PASSWORD=aiu_password

# Backend
JWT_SECRET=your-custom-secret-key
JWT_EXPIRATION=86400000

# Frontend
NEXT_PUBLIC_API_URL=http://localhost:8081
```

## Production Deployment

For production deployment:

1. **Change default passwords** in `docker-compose.yml`
2. **Update JWT secret** to a strong random string
3. **Configure CORS** for your domain
4. **Enable HTTPS** using a reverse proxy (nginx/traefik)
5. **Set up database backups**
6. **Configure resource limits** in docker-compose.yml
7. **Use production profiles:** `SPRING_PROFILES_ACTIVE=prod`

## Health Checks

The services include health checks:
- PostgreSQL: Checks database readiness
- Backend: Depends on healthy database
- Frontend: Depends on backend

View health status:
```bash
docker-compose ps
```

## Volumes

- `postgres_data_main`: Persists database data across container restarts

## Network

All services run on the `aiu-network-main` bridge network, allowing them to communicate using service names.

## Support

For issues or questions:
1. Check the logs: `docker-compose logs`
2. Verify all services are running: `docker-compose ps`
3. Review this documentation
4. Check the main project README
