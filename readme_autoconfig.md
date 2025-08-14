## DLC Autoconfiguration

---

The DLC framework provides comprehensive autoconfiguration functionality through the `@EnableDlc` annotation.
This significantly simplifies setting up a DLC application by automatically configuring all important features.

### Minimal Configuration

Minimal configuration is required to enable the autoconfiguration:

```java
@SpringBootApplication
@EnableDlc(dlcDomainBasePackages = "com.example.domain")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Advanced Configuration

For more fine granular control, you can also enable/disable single individual features:

```java
@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain",
    enableJooqPersistenceAutoConfig = true,
    enableDomainEventsAutoConfig = true,
    enableSpringWebAutoConfig = true,
    enableJacksonAutoConfig = true,
    enableBuilderAutoConfig = true,
    enableSpringOpenApiAutoConfig = true,
    jooqRecordPackage = "com.example.jooq.tables.records",
    jooqSqlDialect = SQLDialect.POSTGRES
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Configuration Options

The `@EnableDlc` annotation offers the following configuration options:

| Parameter                         | Default   | Description                                   |
|-----------------------------------|-----------|-----------------------------------------------|
| `dlcDomainBasePackages`           | `""`      | **Required**: Base package for domain classes |
| `enableSpringWebAutoConfig`       | `true`    | Enables Spring Web integration                |
| `enableBuilderAutoConfig`         | `true`    | Enables Builder pattern support               |
| `enableJooqPersistenceAutoConfig` | `true`    | Enables JOOQ persistence layer                |
| `enableDomainEventsAutoConfig`    | `false`   | Enables domain events handling                |
| `enableJacksonAutoConfig`         | `true`    | Enables Jackson JSON serialization            |
| `enableSpringOpenApiAutoConfig`   | `true`    | Enables OpenAPI/Swagger integration           |
| `jooqRecordPackage`               | `""`      | Package of generated JOOQ records             |
| `jooqSqlDialect`                  | `DEFAULT` | SQL dialect for JOOQ                          |

### Selective Feature Activation

You can also selectively activate only specific features:

```java
@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain",
    enableSpringWebAutoConfig = false,
    enableBuilderAutoConfig = false,
    enableJooqPersistenceAutoConfig = true,
    enableDomainEventsAutoConfig = false,
    enableJacksonAutoConfig = false,
    enableSpringOpenApiAutoConfig = false,
    jooqRecordPackage = "com.example.jooq",
    jooqSqlDialect = SQLDialect.H2
)
public class MinimalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
}
```

### Important Note

With the `@EnableDlc` annotation, manual initialization of the Domain Mirror is **no longer required**. The autoconfiguration handles this automatically based on the configured `dlcDomainBasePackages`.

### Custom Beans
You can still override any of the provided beans by the DLC autoconfig. Make sure you put your bean definitions
into a separate Spring-Boot configuration class and not in the Spring-Boot application class,
since this could lead to conflicts in the bean creation process at startup.