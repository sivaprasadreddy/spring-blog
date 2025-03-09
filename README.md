# Spring-Blog
A blogging platform using Java and Spring Boot.

## Prerequisites
* JDK 21
* Docker and Docker Compose
* Your favourite IDE (Recommended: [IntelliJ IDEA](https://www.jetbrains.com/idea/))

Install JDK, Maven using [SDKMAN](https://sdkman.io/)

```shell
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk install java 21.0.1-tem
$ sdk install maven
```

**Verify the prerequisites**

```shell
$ java -version
$ docker info
$ docker compose version
```

## Tech Stack:
* Java
* Spring Boot
* Spring JdbcClient
* Spring Security
* PostgreSQL
* Flyway
* Thymeleaf
* JUnit 5
* Testcontainers
* Docker, Docker Compose

## Features
* Home Page: Display a list of posts in createdDate desc order with pagination
* Search Posts
* Post Details View
* Add Comment to a Post
* Admin - Login
* Admin - Create a new Post using Markdown
* Admin - Edit existing Post
* Admin - Delete a Post
* Admin - Delete comment(s)

## How to?

### Run Unit / Integration Tests

```shell
$ ./mvnw verify
```

### Format code
You can format the code automatically using [spotless-maven-plugin](https://github.com/diffplug/spotless/blob/main/plugin-maven/README.md)

```shell
$ ./mvnw spotless:apply // to formatting code automatically
$ ./mvnw spotless:check // to verify the code formatting
```

### Run application locally
We can use Docker Compose or Testcontainers to start the dependent services like database, mail server, etc.

To start the application using docker-compose:

```shell
$ ./mvnw spring-boot:run
```

To start the application using [Testcontainers](https://testcontainers.com/):

```shell
$ ./mvnw spring-boot:test-run
```

The application is accessible at http://localhost:8080/.

## How to contribute?
* If you find this project interesting, fork/clone it, run the application and provide feedback.
* If you find any bugs or have suggestions for improvement, then please file an issue.
* Of course, Pull Requests are most welcome.
