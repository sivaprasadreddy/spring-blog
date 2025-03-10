# Spring Boot Actuator: Monitoring and Managing Your Applications

## Introduction

Spring Boot Actuator is a sub-project of Spring Boot that adds production-ready features to your applications. It provides HTTP endpoints and JMX beans to help you monitor and manage your application. Actuator is particularly valuable in production environments, where understanding the state and behavior of your application is crucial.

## Getting Started with Actuator

### Adding Actuator to Your Project

To use Spring Boot Actuator, add the following dependency to your project:

```xml
<!-- Maven -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```groovy
// Gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

### Basic Configuration

By default, Spring Boot Actuator exposes only the `/health` and `/info` endpoints over HTTP. To expose all endpoints, add the following to your `application.properties` or `application.yml` file:

```properties
# application.properties
management.endpoints.web.exposure.include=*
```

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

For security reasons, you might want to be more selective in a production environment:

```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

### Securing Actuator Endpoints

Since Actuator endpoints can expose sensitive information, it's important to secure them properly. If you're using Spring Security, you can configure it to protect the Actuator endpoints:

```java
@Configuration
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                .anyRequest().hasRole("ACTUATOR")
                .and()
            .httpBasic();
    }
}
```

## Core Actuator Endpoints

Spring Boot Actuator provides numerous endpoints for monitoring and managing your application. Here are some of the most useful ones:

### Health Endpoint

The `/health` endpoint provides basic health information about your application. It can be customized to include health checks for various components of your application.

```
GET /actuator/health
```

Response:
```json
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP",
            "details": {
                "database": "MySQL",
                "validationQuery": "isValid()"
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 500107862016,
                "free": 328157442048,
                "threshold": 10485760
            }
        }
    }
}
```

### Info Endpoint

The `/info` endpoint displays arbitrary application information. You can customize it by adding properties with the `info.` prefix:

```properties
info.app.name=My Spring Boot Application
info.app.description=A sample Spring Boot application
info.app.version=1.0.0
```

```
GET /actuator/info
```

Response:
```json
{
    "app": {
        "name": "My Spring Boot Application",
        "description": "A sample Spring Boot application",
        "version": "1.0.0"
    }
}
```

### Metrics Endpoint

The `/metrics` endpoint provides metrics information for your application. It can show system metrics, as well as custom metrics that you define.

```
GET /actuator/metrics
```

Response:
```json
{
    "names": [
        "jvm.memory.max",
        "jvm.memory.used",
        "http.server.requests",
        "process.uptime",
        "system.cpu.usage",
        "system.load.average.1m"
    ]
}
```

You can get detailed information about a specific metric:

```
GET /actuator/metrics/http.server.requests
```

Response:
```json
{
    "name": "http.server.requests",
    "description": "Timer of HTTP server requests",
    "baseUnit": "seconds",
    "measurements": [
        {
            "statistic": "COUNT",
            "value": 5
        },
        {
            "statistic": "TOTAL_TIME",
            "value": 0.12345
        },
        {
            "statistic": "MAX",
            "value": 0.05678
        }
    ],
    "availableTags": [
        {
            "tag": "uri",
            "values": [
                "/actuator/health",
                "/api/users"
            ]
        },
        {
            "tag": "status",
            "values": [
                "200",
                "404"
            ]
        }
    ]
}
```

### Environment Endpoint

The `/env` endpoint exposes the current environment properties:

```
GET /actuator/env
```

Response:
```json
{
    "activeProfiles": [
        "dev"
    ],
    "propertySources": [
        {
            "name": "systemProperties",
            "properties": {
                "java.version": {
                    "value": "11.0.12"
                }
            }
        },
        {
            "name": "applicationConfig: [classpath:/application.properties]",
            "properties": {
                "server.port": {
                    "value": "8080"
                }
            }
        }
    ]
}
```

### Loggers Endpoint

The `/loggers` endpoint allows you to view and modify the logging configuration at runtime:

```
GET /actuator/loggers
```

Response:
```json
{
    "levels": [
        "OFF",
        "ERROR",
        "WARN",
        "INFO",
        "DEBUG",
        "TRACE"
    ],
    "loggers": {
        "ROOT": {
            "configuredLevel": "INFO",
            "effectiveLevel": "INFO"
        },
        "com.example": {
            "configuredLevel": "DEBUG",
            "effectiveLevel": "DEBUG"
        }
    }
}
```

You can change the log level for a specific logger:

```
POST /actuator/loggers/com.example
Content-Type: application/json

{
    "configuredLevel": "DEBUG"
}
```

### Threaddump Endpoint

The `/threaddump` endpoint provides a snapshot of the application's threads:

```
GET /actuator/threaddump
```

### Heapdump Endpoint

The `/heapdump` endpoint generates a heap dump file that can be analyzed with tools like VisualVM or Eclipse Memory Analyzer:

```
GET /actuator/heapdump
```

### Shutdown Endpoint

The `/shutdown` endpoint allows you to gracefully shut down your application. This endpoint is disabled by default and should be used with caution:

```properties
management.endpoint.shutdown.enabled=true
```

```
POST /actuator/shutdown
```

## Custom Health Indicators

You can create custom health indicators to provide health information about specific components of your application:

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final DataSource dataSource;
    
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Health.up()
                    .withDetail("database", "MySQL")
                    .withDetail("status", "Connected")
                    .build();
            } else {
                return Health.down()
                    .withDetail("database", "MySQL")
                    .withDetail("status", "Query failed")
                    .build();
            }
        } catch (SQLException e) {
            return Health.down()
                .withDetail("database", "MySQL")
                .withDetail("status", "Connection failed")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

## Custom Metrics

You can create custom metrics to track application-specific data:

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    private final MeterRegistry meterRegistry;
    
    public OrderController(OrderService orderService, MeterRegistry meterRegistry) {
        this.orderService = orderService;
        this.meterRegistry = meterRegistry;
        
        // Register a counter
        Counter orderCounter = Counter.builder("app.orders.created")
            .description("Number of orders created")
            .register(meterRegistry);
        
        // Register a gauge
        Gauge.builder("app.orders.pending", orderService, service -> service.countPendingOrders())
            .description("Number of pending orders")
            .register(meterRegistry);
    }
    
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        Order created = orderService.createOrder(order);
        
        // Increment the counter
        meterRegistry.counter("app.orders.created").increment();
        
        // Record the order amount
        meterRegistry.summary("app.orders.amount").record(order.getAmount().doubleValue());
        
        return created;
    }
}
```

## Actuator with Micrometer and Prometheus

Spring Boot Actuator integrates with Micrometer, which provides a vendor-neutral application metrics facade. Micrometer supports various monitoring systems, including Prometheus, which is a popular choice for monitoring Spring Boot applications.

To use Prometheus with Spring Boot Actuator, add the following dependency:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

This will automatically add a `/actuator/prometheus` endpoint that exposes metrics in a format that Prometheus can scrape.

A typical Prometheus configuration to scrape a Spring Boot application might look like this:

```yaml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

## Spring Boot Admin

Spring Boot Admin is a web application that manages and monitors Spring Boot applications. It provides a nice UI for the Actuator endpoints and additional features like notifications.

To use Spring Boot Admin, you need to set up a Spring Boot Admin server and register your applications as clients.

### Setting Up the Admin Server

```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
    <version>2.5.1</version>
</dependency>
```

```java
@SpringBootApplication
@EnableAdminServer
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
```

### Registering a Client

```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
    <version>2.5.1</version>
</dependency>
```

```properties
spring.boot.admin.client.url=http://localhost:8080
management.endpoints.web.exposure.include=*
```

## Best Practices

1. **Secure your endpoints**: Always secure Actuator endpoints in production environments.
2. **Be selective with exposed endpoints**: Only expose the endpoints you need.
3. **Use custom health indicators**: Create custom health indicators for critical components.
4. **Monitor your metrics**: Set up alerts based on important metrics.
5. **Use Spring Boot Admin**: Consider using Spring Boot Admin for a better monitoring experience.
6. **Configure appropriate logging levels**: Use the loggers endpoint to adjust logging levels as needed.

## Conclusion

Spring Boot Actuator is a powerful tool for monitoring and managing your Spring Boot applications. It provides valuable insights into your application's health, performance, and behavior, which are essential for maintaining a reliable production environment. By leveraging Actuator's endpoints and integrating with monitoring systems like Prometheus, you can ensure that your applications run smoothly and that you're quickly alerted to any issues that arise.