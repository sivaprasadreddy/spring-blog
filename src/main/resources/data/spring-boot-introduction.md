# Introduction to Spring Boot

## What is Spring Boot?

Spring Boot is an opinionated framework that simplifies the development of Spring applications. Created by Pivotal (now part of VMware), Spring Boot aims to make it easy to create stand-alone, production-grade Spring-based applications with minimal configuration. It takes an opinionated view of the Spring platform and third-party libraries, allowing developers to get started with minimum fuss.

## Key Features of Spring Boot

### Standalone Applications

Spring Boot allows you to create applications that can be run as standalone JAR files with an embedded server. This eliminates the need for external application servers and simplifies deployment.

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

With this simple main class, you can package your entire application into a single executable JAR file that includes an embedded web server (Tomcat, Jetty, or Undertow).

### Opinionated Defaults

Spring Boot provides sensible defaults for configuration based on the dependencies you have added. This "convention over configuration" approach reduces the need for boilerplate code and XML configuration.

### Auto-Configuration

One of Spring Boot's most powerful features is auto-configuration. Spring Boot automatically configures your application based on the dependencies you have added to your project.

For example, if you include `spring-boot-starter-data-jpa` in your dependencies, Spring Boot will:
- Configure a DataSource (if a database driver is present)
- Set up an EntityManagerFactory
- Create a JPA Transaction Manager
- Configure Spring Data JPA repositories

All of this happens without you having to write a single line of configuration code.

### Spring Boot Starters

Spring Boot provides a set of starter dependencies that simplify your build configuration. Each starter serves as a one-stop shop for all the Spring technologies you need for a specific type of application.

Some common starters include:

- **spring-boot-starter-web**: For building web applications, including RESTful applications using Spring MVC
- **spring-boot-starter-data-jpa**: For using Spring Data JPA with Hibernate
- **spring-boot-starter-security**: For using Spring Security
- **spring-boot-starter-test**: For testing Spring Boot applications
- **spring-boot-starter-actuator**: For monitoring and managing your application

### Embedded Servers

Spring Boot includes support for embedded web servers, including:
- Tomcat (default)
- Jetty
- Undertow

This allows you to create self-contained applications that don't require an external web server.

### Production-Ready Features

Spring Boot includes a number of features that help you monitor and manage your application when it's pushed to production:

- **Health checks**: Verify the health of your application
- **Metrics**: Collect and view metrics about your application
- **Logging**: Configure various logging levels
- **Externalized configuration**: Configure your application outside of your code

These features are provided by Spring Boot Actuator, which you can add to your project with the `spring-boot-starter-actuator` dependency.

## Getting Started with Spring Boot

### Setting Up a Spring Boot Project

The easiest way to create a Spring Boot project is to use Spring Initializr (https://start.spring.io/). This web-based tool allows you to:
1. Select your build tool (Maven or Gradle)
2. Choose your language (Java, Kotlin, or Groovy)
3. Select your Spring Boot version
4. Add dependencies for the features you need
5. Generate and download a project template

Alternatively, you can use the Spring Boot CLI, your IDE (most modern Java IDEs have Spring Boot support), or set up a project manually.

### Basic Project Structure

A typical Spring Boot project has the following structure:

```
my-application/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/myapplication/
│   │   │       ├── MyApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── model/
│   │   └── resources/
│   │       ├── application.properties (or application.yml)
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/example/myapplication/
│               └── MyApplicationTests.java
├── pom.xml (or build.gradle)
└── README.md
```

### Configuration

Spring Boot applications can be configured using:

1. **application.properties** or **application.yml** files:

```properties
# application.properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
```

```yaml
# application.yml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: password
```

2. **Environment variables**:

```
export SERVER_PORT=8080
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/mydb
```

3. **Command-line arguments**:

```
java -jar myapplication.jar --server.port=8080
```

Spring Boot follows a specific order of precedence for these configuration sources, allowing for flexible configuration across different environments.

## Building a Simple REST API

Let's create a simple REST API with Spring Boot:

```java
// Entity class
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    
    // Constructors, getters, setters
}

// Repository interface
public interface ProductRepository extends JpaRepository<Product, Long> {
}

// Service class
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    // Other methods
}

// Controller class
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
    
    // Other endpoints
}
```

## Testing Spring Boot Applications

Spring Boot provides excellent support for testing:

```java
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    
    @MockBean
    private ProductRepository productRepository;
    
    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
            new Product(1L, "Product 1", new BigDecimal("10.00")),
            new Product(2L, "Product 2", new BigDecimal("20.00"))
        );
        when(productRepository.findAll()).thenReturn(products);
        
        // Act
        List<Product> result = productService.getAllProducts();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
    }
}

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @Test
    void testGetAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
            new Product(1L, "Product 1", new BigDecimal("10.00")),
            new Product(2L, "Product 2", new BigDecimal("20.00"))
        );
        when(productService.getAllProducts()).thenReturn(products);
        
        // Act & Assert
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("Product 1")));
    }
}
```

## Deploying Spring Boot Applications

Spring Boot applications can be deployed in various ways:

1. **JAR Deployment**: Package your application as a JAR and run it directly:
   ```
   java -jar myapplication.jar
   ```

2. **WAR Deployment**: Package your application as a WAR and deploy it to an external application server.

3. **Docker Deployment**: Create a Docker image for your application:
   ```dockerfile
   FROM openjdk:11-jre-slim
   COPY target/myapplication.jar app.jar
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

4. **Cloud Deployment**: Deploy to cloud platforms like AWS, Azure, or Google Cloud, often using platform-specific services or Kubernetes.

## Spring Boot Ecosystem

Spring Boot integrates well with the broader Spring ecosystem:

- **Spring Cloud**: For building cloud-native applications
- **Spring Security**: For securing your applications
- **Spring Data**: For data access
- **Spring Batch**: For batch processing
- **Spring Integration**: For enterprise integration patterns

## Conclusion

Spring Boot has revolutionized Spring application development by providing a streamlined, convention-over-configuration approach. Its auto-configuration, starter dependencies, and production-ready features make it an excellent choice for building modern Java applications.

By eliminating boilerplate code and configuration, Spring Boot allows developers to focus on business logic rather than infrastructure concerns. Whether you're building a simple microservice or a complex enterprise application, Spring Boot provides the tools and conventions you need to be productive.