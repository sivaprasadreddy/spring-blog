# Spring-Blog Project Overview

## Project Description
Spring-Blog is a blogging platform built using Java and Spring Boot. 
It provides a full-featured blogging system with both user-facing and administrative capabilities.

## Purpose
The purpose of this project is to demonstrate a modern Java web application using Spring Boot and 
related technologies. It serves as a reference implementation for building web applications 
with features like content management, user authentication, and database integration.

## Tech Stack
- **Backend**:
  - Java 21
  - Spring Boot
  - Spring JdbcClient
  - Spring Security
  - Flyway (for database migrations)
  
- **Frontend**:
  - Thymeleaf (server-side templating)
  - HTML/CSS/JavaScript
  
- **Database**:
  - PostgreSQL
  
- **Testing**:
  - JUnit 5
  - Testcontainers
  
- **DevOps**:
  - Docker
  - Docker Compose

## Key Features
- **Public Features**:
  - Home page with paginated list of posts
  - Post search functionality
  - Detailed post view
  - Comment submission on posts
  
- **Admin Features**:
  - Secure login system
  - Create new posts with Markdown support
  - Edit existing posts
  - Delete posts
  - Manage comments (including deletion)

## Development Workflow
1. **Setup Environment**:
   - Install JDK 21
   - Install Docker and Docker Compose
   - Clone the repository

2. **Running the Application**:
   - Start with Docker Compose: `./mvnw spring-boot:run`
   - Alternative start with Testcontainers: `./mvnw spring-boot:test-run`
   - Access the application at http://localhost:8080/

3. **Testing**:
   - Run all tests: `./mvnw verify`

4. **Code Quality**:
   - Format code: `./mvnw spotless:apply`
   - Verify formatting: `./mvnw spotless:check`

## Project Structure
The project follows a standard Spring Boot application structure with a focus on clean architecture principles:
- Domain models in the `domain.models` package
- Services in the `domain.services` package
- Repositories in the `domain.data` package
- Controllers in the `web.controllers` package
- Database adapters in the `adapter.jdbc` package

## Contribution Guidelines
- Fork/clone the repository
- Run the application locally and test your changes
- File issues for bugs or improvement suggestions
- Submit pull requests with proposed changes