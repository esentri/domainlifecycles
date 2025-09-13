# DLC Spring Boot Autoconfig

The DLC Spring Boot Autoconfig module provides comprehensive automatic configuration for Spring Boot applications using the Domain Lifecycles (DLC) framework. This module significantly simplifies the integration of various DLC components by providing sensible defaults and a unified configuration strategy.

## Overview

The autoconfig functionality enables developers to use DLC features with minimal manual configuration. By using the `@EnableDlc` annotation, various DLC modules can be automatically activated and configured.

## Getting Started

### Add Dependency

Add the DLC Spring Boot Autoconfig dependency to your `build.gradle` or `pom.xml`:

**Gradle:**
```groovy
dependencies {
    implementation 'io.domainlifecycles:dlc-spring-boot-autoconfig:2.4.0'
}
```


**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot-autoconfig</artifactId>
    <version>2.4.0</version>
</dependency>
```

Or for an even simpler setup, you can use `dlc-spring-boot-starter`, which is fully compatible with Autoconfig and 
includes all needed dependencies:

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:dlc-spring-boot-starter:2.4.0'
  // Autoconfig is already included
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot-starter</artifactId>
    <version>2.4.0</version>
</dependency>
```

### Basic Configuration

Enable DLC in your Spring Boot application using the `@EnableDlc` annotation:

```java
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain"
)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```


## Available Autoconfig Modules

### 1. Domain Autoconfig (`DlcDomainAutoConfiguration`)

**Purpose:** Basic domain configuration with automatic package discovery

**Activation:** Automatically active (cannot be disabled)

**Configuration:**
```java
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain,com.example.shared"
)
```


**Properties:**
```properties
dlc.domain.basePackages=com.example.domain,com.example.shared
```


### 2. Builder Autoconfig (`DlcBuilderAutoConfiguration`)

**Purpose:** Automatic configuration of DLC Builder Pattern support

**Activation:** `enableBuilderAutoConfig = true` (default)

**Provided Beans:**
- `DomainObjectBuilderProvider`
- `InnerClassDomainObjectBuilder`

```java
@EnableDlc(
    enableBuilderAutoConfig = true
)
```


### 3. Jackson Autoconfig (`DlcJacksonAutoConfiguration`)

**Purpose:** JSON serialization for DLC Domain Objects

**Activation:** `enableJacksonAutoConfig = true` (default)

**Features:**
- Automatic serialization of ValueObjects and Identities
- Custom deserializers for Domain Types
- Integration with Spring Boot's ObjectMapper

```java
@EnableDlc(
    enableJacksonAutoConfig = true
)
```


### 4. jOOQ Persistence Autoconfig (`DlcJooqPersistenceAutoConfiguration`)

**Purpose:** Automatic configuration of jOOQ-based persistence

**Activation:** `enableJooqPersistenceAutoConfig = true` (default)

**Configuration:**
```java
@EnableDlc(
    enableJooqPersistenceAutoConfig = true,
    jooqRecordPackage = "com.example.jooq.tables.records",
    jooqSqlDialect = SQLDialect.POSTGRES
)
```


**Properties:**
```properties
dlc.jooq.recordPackage=com.example.jooq.tables.records
dlc.jooq.sqlDialect=POSTGRES
```


**Provided Beans:**
- `JooqDomainPersistenceProvider`
- `EntityIdentityProvider`

### 5. Domain Events Autoconfig (`DlcDomainEventsAutoConfiguration`)

**Purpose:** Automatic configuration of event handling

**Activation:** `enableDomainEventsAutoConfig = false` (default: disabled)

**Supported Event Systems:**
- Spring Events
- JMS (Jakarta Messaging)
- Gruelbox Transaction Outbox

```java
@EnableDlc(
    enableDomainEventsAutoConfig = true
)
```


**Conditional Beans based on Classpath:**
- With Spring TX: `SpringDomainEventPublisher`
- With JMS: `JmsDomainEventPublisher`
- With Gruelbox: `GruelboxDomainEventPublisher`

### 6. Spring Web Autoconfig (`DlcSpringWebAutoConfiguration`)

**Purpose:** REST/Web integration for DLC Domain Objects

**Activation:** `enableSpringWebAutoConfig = true` (default)

**Features:**
- Automatic conversion of string parameters to Domain Objects
- Parameter converters for ValueObjects and Identities
- `ResponseEntityBuilder` for consistent API responses

```java
@EnableDlc(
    enableSpringWebAutoConfig = true
)
```


### 7. OpenAPI Autoconfig (`DlcSpringOpenApiAutoConfiguration`)

**Purpose:** Automatic OpenAPI/Swagger documentation for DLC Types

**Activation:** `enableSpringOpenApiAutoConfig = true` (default)

**Features:**
- Correct schema generation for ValueObjects and Identities
- Integration with SpringDoc OpenAPI
- Custom OpenAPI customizer for DLC Types

```java
@EnableDlc(
    enableSpringOpenApiAutoConfig = true
)
```


## Advanced Configuration

### Properties-based Configuration

Instead of using annotation attributes, you can define properties:

```properties
# Domain Packages
dlc.domain.basePackages=com.example.domain,com.example.shared

# jOOQ Configuration
dlc.jooq.recordPackage=com.example.jooq.tables.records
dlc.jooq.sqlDialect=POSTGRES
```


### Custom RecordMappers

For advanced jOOQ persistence, you can provide custom RecordMappers:

```java
@Component
public class CustomRecordMapper implements RecordMapper<MyEntity, MyEntityRecord, MyEntityId> {
    
    @Override
    public MyEntityRecord from(MyEntity domainObject, DSLContext dslContext) {
        // Custom mapping logic
    }
    
    @Override
    public MyEntity to(MyEntityRecord record, DSLContext dslContext) {
        // Custom mapping logic
    }
}
```
### Important Note

With the `@EnableDlc` annotation, manual initialization of the Domain Mirror is **no longer required**.
The autoconfiguration handles this automatically based on the configured `dlcDomainBasePackages`.

### Custom Beans
You can still override any of the provided beans by the DLC autoconfig. Make sure you put your bean definitions
into a separate Spring-Boot configuration class and not in the Spring-Boot application class,
since this could lead to conflicts in the bean creation process at startup.

## Troubleshooting

### Common Issues

**Problem:** jOOQ Records not found

**Solution:** Execute a new build and make sure you specify the correct record package:
```java
@EnableDlc(
    jooqRecordPackage = "com.example.generated.jooq.tables.records"
)
```


### Debug Information

Enable debug logging for autoconfig:

```properties
logging.level.io.domainlifecycles.autoconfig=DEBUG
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

