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
    implementation 'io.domainlifecycles:dlc-spring-boot-autoconfig:2.5.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot-autoconfig</artifactId>
    <version>2.5.0</version>
</dependency>
```

Or for an even simpler setup, you can use `dlc-spring-boot-starter`, which is fully compatible with Autoconfig and 
includes all needed dependencies:

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:dlc-spring-boot-starter:2.5.0'
  // Autoconfig is already included
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot-starter</artifactId>
    <version>2.5.0</version>
</dependency>
```

The DLC Spring Boot Starter includes all necessary DLC dependencies for the following features.
Be aware that features requiring external libraries will only be enabled if these libraries are explicitly defined as a dependency:

- DLC jOOQ persistence requires additionally a compatible version of jOOQ 
- DLC Spring Web integration requires additionally Spring Web
- DLC Spring Doc integration requires additionally a compatible version of SpringDoc2
- DLC Domain Events 
    - Domain Events with SpringTransaction support requires additionally a compatible version of spring-tx
    - Domain Events with Jakarta JTA support require additionally  a JTA compatible transaction manager (e.g. Atomikos)
    - Domain Events with Jakarta JMS support require additionally a JMS compatible client (e.g. Active MQ Artemis)
    - Domain Events with Gruelbox support requires additionally a compatible version of Gruelbox
    - Domain Events with ActiveMQ 5 Classic support require additionally the Active MQ Jakarta client library
    - Domain Events Jakarta JTA, Jakarta JMS or Active MQ 5 Classic are not fully supported by AutoConfiguration. Additional configuration beans are required (see test configurations within this [sub project](./../test-domain-events-integration)).
- DLC Jackson integration is enabled by default, but could be disabled, see below

Generally, it is possible to add one of the optional dependencies mentioned above and disable the corresponding DLC autocofiguration (see below).

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

**Activation:** Automatically active when annotation is set.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcDomainAutoConfiguration.class)
```

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
Providing the 'dlcDomainBasePackages' attribute or defining it as a property is mandatory.

**Provided Beans:**
- `DomainMirror` with the bean name `initializedDomain`

### 2. Builder Autoconfig (`DlcBuilderAutoConfiguration`)

**Purpose:** Automatic configuration of DLC Builder Pattern support

**Activation:** Automatically active when `@EnableDlc` annotation is set.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcBuilderAutoConfiguration.class)
```

### 3. Jackson Autoconfig (`DlcJacksonAutoConfiguration`)

**Purpose:** JSON serialization for DLC Domain Objects

**Activation:** Automatically active when `@EnableDlc` annotation is set 
and Jackson is provided on the classpath.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcJacksonAutoConfiguration.class)
```

**Features:**
- Automatic serialization of ValueObjects and Identities
- Custom deserializers for Domain Types
- Integration with Spring Boot's ObjectMapper

### 4. jOOQ Persistence Autoconfig (`DlcJooqPersistenceAutoConfiguration`)

**Purpose:** Automatic configuration of jOOQ-based persistence

**Activation:** Automatically active when `@EnableDlc` annotation is set 
and jOOQ is provided on the classpath.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcJooqPersistenceAutoConfiguration.class)
```

**Configuration:**
```java
@EnableDlc(
    enableJooqPersistenceAutoConfig = true,
    jooqRecordPackage = "com.example.jooq.tables.records",
    jooqSqlDialect = "POSTGRES"
)
```

**Properties:**
```properties
dlc.jooq.recordPackage=com.example.jooq.tables.records
dlc.jooq.sqlDialect=POSTGRES
```

Providing the 'dlcJooqRecordPackage' is mandatory for DLC persistence, 
'dlcJooqSqlDialect' is recommended.

### 5. Domain Events Autoconfig (`DlcDomainEventsAutoConfiguration`)

**Purpose:** Automatic configuration of event handling

**Activation:** Automatically active when `@EnableDlc` annotation is set.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcDomainEventsAutoConfiguration.class)
```

**Supported Event Systems:**
- In memory Domain Events
- JMS (Jakarta Messaging) with additional configuration 

### 6. Gruelbox Domain Events Autoconfig (`DlcGruelboxDomainEventsAutoConfiguration`)

**Purpose:** Automatic configuration of event handling

**Activation:** Automatically active when `@EnableDlc` annotation is set
and Gruelbox is provided on the classpath.

Could be deactivated by:
```java
@EnableDlc(exclude = DlcGruelboxDomainEventsAutoConfiguration.class)
```

**Supported Event Systems:**
- In memory Domain Events
- JMS (Jakarta Messaging) with additional configuration

Enabling 'DlcDomainEventsAutoConfiguration' is recommended for Gruelbox Domain Events.


### 7. Spring Web Autoconfig (`DlcSpringWebAutoConfiguration`)

**Purpose:** REST/Web integration for DLC Domain Objects

**Activation:** Automatically active when `@EnableDlc` annotation is set
and if Spring Web is used (on the classpath).

Could be deactivated by:
```java
@EnableDlc(exclude = DlcGruelboxDomainEventsAutoConfiguration.class)
```

**Features:**
- Automatic conversion of string parameters to Domain Objects
- Parameter converters for ValueObjects and Identities
- `ResponseEntityBuilder` for consistent API responses

### 8. OpenAPI Autoconfig (`DlcSpringOpenApiAutoConfiguration`)

**Purpose:** Automatic OpenAPI/Swagger documentation for DLC Types

**Activation:** Automatically active when `@EnableDlc` annotation is set
and if SpringDoc OpenAPI is provided on the classpath.

Could be deactivated by:
```java
@EnableDlc(exclude = DlcSpringOpenApiAutoConfiguration.class)
```

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

