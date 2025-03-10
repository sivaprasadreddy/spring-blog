# Docker Compose for Java Applications: Simplifying Multi-Container Deployments

## Introduction

Modern Java applications rarely run in isolation. They typically require databases, caching systems, message brokers, and other services to function properly. Managing these dependencies during development, testing, and production can be challenging. Docker Compose offers an elegant solution to this problem by allowing developers to define and run multi-container Docker applications with ease.

This article explores how Java developers can leverage Docker Compose to streamline their development workflow, simplify testing, and prepare for production deployments.

## What is Docker Compose?

Docker Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application's services, networks, and volumes. Then, with a single command, you create and start all the services from your configuration.

Key benefits of Docker Compose include:

1. **Simplified Configuration**: Define your entire application stack in a single file
2. **Reproducible Environments**: Ensure consistent environments across development, testing, and production
3. **Easy Service Coordination**: Manage service dependencies and networking automatically
4. **Efficient Development Workflow**: Start, stop, and rebuild services with simple commands
5. **Isolated Environments**: Run multiple environments on a single host without conflicts

## Getting Started with Docker Compose

### Installation

Docker Compose is included with Docker Desktop for Windows and Mac. For Linux, you may need to install it separately:

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.15.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

Verify your installation:

```bash
docker-compose --version
```

### Basic Docker Compose File Structure

A `docker-compose.yml` file typically includes:

- **Services**: The containers that make up your application
- **Networks**: How the containers communicate with each other
- **Volumes**: Persistent data storage

Here's a simple example for a Spring Boot application with a PostgreSQL database:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    depends_on:
      - db

  db:
    image: postgres:14
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

### Basic Commands

- Start your application: `docker-compose up`
- Start in detached mode: `docker-compose up -d`
- Stop services: `docker-compose down`
- Stop and remove volumes: `docker-compose down -v`
- View logs: `docker-compose logs`
- Follow logs: `docker-compose logs -f`
- Rebuild services: `docker-compose build`
- Start a specific service: `docker-compose up -d db`

## Docker Compose for Java Development

### Spring Boot with Database

Here's a more detailed example for a Spring Boot application with PostgreSQL:

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: spring-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - db
    networks:
      - spring-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 5

  db:
    image: postgres:14-alpine
    container_name: postgres-db
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./init-scripts:/docker-entrypoint-initdb.d
    networks:
      - spring-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

networks:
  spring-network:
    driver: bridge

volumes:
  postgres-data:
```

### Microservices Architecture

For a microservices architecture, you might have multiple services communicating with each other:

```yaml
version: '3.8'

services:
  user-service:
    build: ./user-service
    ports:
      - "8081:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://user-db:5432/userdb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/
    depends_on:
      - user-db
      - discovery-service
    networks:
      - microservice-network

  product-service:
    build: ./product-service
    ports:
      - "8082:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://product-db:5432/productdb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/
    depends_on:
      - product-db
      - discovery-service
    networks:
      - microservice-network

  order-service:
    build: ./order-service
    ports:
      - "8083:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://order-db:5432/orderdb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/
    depends_on:
      - order-db
      - discovery-service
      - kafka
    networks:
      - microservice-network

  gateway-service:
    build: ./gateway-service
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/
    depends_on:
      - discovery-service
    networks:
      - microservice-network

  discovery-service:
    build: ./discovery-service
    ports:
      - "8761:8761"
    networks:
      - microservice-network

  user-db:
    image: postgres:14-alpine
    environment:
      - POSTGRES_DB=userdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - user-db-data:/var/lib/postgresql/data
    networks:
      - microservice-network

  product-db:
    image: postgres:14-alpine
    environment:
      - POSTGRES_DB=productdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - product-db-data:/var/lib/postgresql/data
    networks:
      - microservice-network

  order-db:
    image: postgres:14-alpine
    environment:
      - POSTGRES_DB=orderdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - order-db-data:/var/lib/postgresql/data
    networks:
      - microservice-network

  kafka:
    image: confluentinc/cp-kafka:7.0.0
    ports:
      - "9092:9092"
    environment:
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
      - KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
    networks:
      - microservice-network

networks:
  microservice-network:
    driver: bridge

volumes:
  user-db-data:
  product-db-data:
  order-db-data:
```

### Full-Stack Development

For full-stack development with a React frontend and Spring Boot backend:

```yaml
version: '3.8'

services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:80"
    depends_on:
      - backend
    networks:
      - app-network

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    depends_on:
      - db
    networks:
      - app-network

  db:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  postgres-data:
```

## Advanced Docker Compose Features

### Environment Variables and .env Files

Use `.env` files to store environment variables:

```
# .env file
POSTGRES_USER=myuser
POSTGRES_PASSWORD=mypassword
POSTGRES_DB=mydb
SPRING_PROFILES_ACTIVE=dev
```

Reference them in your `docker-compose.yml`:

```yaml
services:
  app:
    environment:
      - SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
      - SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
      - SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
```

### Multiple Compose Files for Different Environments

Use multiple compose files to manage different environments:

- `docker-compose.yml`: Base configuration
- `docker-compose.override.yml`: Development overrides (loaded automatically)
- `docker-compose.prod.yml`: Production overrides

```bash
# Development (uses docker-compose.yml and docker-compose.override.yml)
docker-compose up

# Production
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

Example production overrides:

```yaml
# docker-compose.prod.yml
services:
  app:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    deploy:
      replicas: 3
    restart: always

  db:
    volumes:
      - /mnt/persistent-storage:/var/lib/postgresql/data
    restart: always
```

### Health Checks and Dependencies

Ensure services start in the correct order with health checks:

```yaml
services:
  app:
    depends_on:
      db:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 60s

  db:
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
```

### Scaling Services

Scale services with Docker Compose:

```bash
docker-compose up -d --scale app=3
```

For this to work, you need to configure your ports properly:

```yaml
services:
  app:
    ports:
      - "8080-8082:8080"  # Allows scaling up to 3 instances
```

## Development Workflow with Docker Compose

### Local Development with Hot Reload

For a better development experience, mount your source code as a volume and enable hot reloading:

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    volumes:
      - ./src:/app/src  # Mount source code
      - ./build.gradle:/app/build.gradle  # Mount build file
    environment:
      - SPRING_DEVTOOLS_RESTART_ENABLED=true  # Enable hot reload
```

### Debugging with Docker Compose

Enable remote debugging:

```yaml
services:
  app:
    ports:
      - "8080:8080"
      - "5005:5005"  # Debug port
    environment:
      - JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

### Running Tests

Run tests in a separate service:

```yaml
services:
  app:
    # Application configuration

  test:
    build:
      context: .
      dockerfile: Dockerfile.test
    volumes:
      - ./src:/app/src
      - ./build/test-results:/app/build/test-results
    environment:
      - SPRING_PROFILES_ACTIVE=test
      - SPRING_DATASOURCE_URL=jdbc:postgresql://test-db:5432/testdb
    depends_on:
      - test-db

  test-db:
    image: postgres:14-alpine
    environment:
      - POSTGRES_DB=testdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
```

## Integration with CI/CD Pipelines

### GitHub Actions Example

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up Docker Compose
        run: docker-compose -f docker-compose.yml -f docker-compose.test.yml up -d
      
      - name: Run tests
        run: docker-compose -f docker-compose.yml -f docker-compose.test.yml run test
      
      - name: Tear down
        run: docker-compose -f docker-compose.yml -f docker-compose.test.yml down
      
      - name: Build and push