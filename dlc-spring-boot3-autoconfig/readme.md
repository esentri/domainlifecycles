# DLC Spring Boot 3 Autoconfig

This DLC Spring Boot Autoconfig module provides comprehensive automatic configuration for Spring Boot 3 applications using the Domain Lifecycles (DLC) framework. This module significantly simplifies the integration of various DLC components by providing sensible defaults and a unified configuration strategy.

## Overview

The autoconfig functionality enables developers to use DLC features with minimal manual configuration. By using the `@EnableDlc` annotation, various DLC modules can be automatically activated and configured.

## Getting Started

### Add Dependency

Add the DLC Spring Boot Autoconfig dependency to your `build.gradle` or `pom.xml`:

**Gradle:**
```groovy
dependencies {
    implementation 'io.domainlifecycles:dlc-spring-boot3-autoconfig:3.0.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot3-autoconfig</artifactId>
    <version>3.0.0</version>
</dependency>
```

Or for an even simpler setup, you can use `dlc-spring-boot3-starter`, which is fully compatible with Autoconfig and 
includes all needed dependencies:

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:dlc-spring-boot3-starter:3.0.0'
  // Autoconfig is already included
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot3-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

The DLC Spring Boot Starter includes all necessary DLC dependencies for the following features.
Be aware that features requiring external libraries will only be enabled if these libraries are explicitly defined as a dependency:

- DLC jOOQ persistence requires additionally a compatible version of jOOQ 
- DLC Spring Web integration requires additionally Spring Web
- DLC Spring Doc integration requires additionally a compatible version of SpringDoc
- DLC Domain Events
  - Domain Events by default use the Spring internal event bus (``ApplicationEventPublisher``), transactional support is provided. 
  - Domain Events with SpringTransaction support requires additionally a compatible version of spring-tx
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
Optionally use Spring application properties for configuration:
```properties
dlc.domain.basePackages=com.example.domain,com.example.shared
```
Providing either the 'dlcDomainBasePackages' attribute or defining it as a property is mandatory.

**Provided Beans:**
- `DomainMirror` with the bean name `initializedDomain`

### 2. Builder Autoconfig (`DlcBuilderAutoConfiguration`)

**Purpose:** Automatic configuration of DLC Builder Pattern support

**Activation:** Automatically active when `@EnableDlc` annotation is set.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcBuilderAutoConfiguration.class)
```

More information on [DLC Builders](./../builder/readme.md) 

### 3. Jackson Autoconfig (`DlcJacksonAutoConfiguration`)

**Purpose:** JSON serialization for DLC Domain Objects

**Activation:** Automatically active when `@EnableDlc` annotation is set 
and Jackson is provided on the classpath.
Could be deactivated by:
```java
@EnableDlc(exclude = DlcJacksonAutoConfiguration.class)
```
or regarding Jackson 2
```java
@EnableDlc(exclude = DlcJackson2AutoConfiguration.class)
```

**Features:**
- Automatic de-/serialization of ValueObjects and Identities
- Custom deserializers for Domain Types
- Integration with Jackson's ObjectMapper

More information on [DLC Jackson](./../jackson3-integration/readme.md)

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

More information on [DLC Persistence](./../persistence/readme.md)

### 5. Domain Events Autoconfig 

**Purpose:** Automatic configuration of DomainEvent handling

DLC provides autoconfiguration for the following event systems for DomainEvents:
- In memory (```DlcNoTxInMemoryDomainEventsAutoConfiguration```)
- Spring event bus (```DlcSpringBusDomainEventsAutoConfiguration```)

**Activation:** Automatically active when `@EnableDlc` annotation is set.
By default ```DlcSpringBusDomainEventsAutoConfiguration``` is enabled. 
Disabled by setting config property ```dlc.events.springbus.enabled=false```.

The DLC inmemory event system can be enabled by setting the corresponding config property:
- In memory: ```dlc.events.inmemory.enabled=true```

#### AutoConfig supported Event Systems
- Spring Bus supported Domain Events (DLC autoconfig default):
  Using Spring's internal event bus (passing them to ```ApplicationEventPublisher```).
  Enables transactional handling by Spring means, see [here](../domain-events-spring-bus/readme.md).
  Optionally integrates with Spring Modulith event registry for reliable event handling (no event loss).
- In memory Domain Events: Passing DomainEvents directly to listeners

**Attention**
DLC provides additional ways for handling Domain Events (supporting Jakarta JMS/JTA, ActiveMQ Classic 5, Gruelbox).
Those are not covered by the autoconfig module. But they can be enabled by providing the corresponding beans (see [DLC DomainEvents](./../domain-events-core/readme.md)).

#### Autoconfigured ChannelFactories and Channels
DLC Domain Events supports configuring multiple event channels and routing DomainEvents to different channels.
The primary way to create a channel is creating a ```ChannelFactory```first. 
For each of the supported DomainEvent autoconfigurations a ```ChannelFactory``` is created automatically.
The autoconfiguration also creates only one default ```Channel``` with a routing configuration that routes all Domain Events to the default channel.
So, if on one event system should be used, no additional configuration is required.

But it is possible to activate multiple ChannelFactories at the same time.
In order to use distinct channels, it is required to create specific channels and a corresponding routing configuration: 

```java
@Bean
public ProcessingChannel inMemoryChannel(InMemoryChannelFactory factory){
    return factory.processingChannel("inMemory");
}

@Bean
public PublishingChannel springChannel(SpringApplicationEventsPublishingChannelFactory factory){
    return factory.publishOnlyChannel("springTx");
}

@Bean
public PublishingRouter router(List<PublishingChannel> channels ){
    var router = new DomainEventTypeBasedRouter(channels);
    router.defineDefaultChannel("springTx");
    router.defineExplicitRoute(SpecificEvent.class, "inMemory");
    return router;
}
```

More information on [DLC DomainEvents](./../domain-events-core/readme.md)

More information on [DLC DomainEvents Spring integration](./../domain-events-spring-bus/readme.md)

### 6. Spring Web Autoconfig (`DlcSpringWebAutoConfiguration`)

**Purpose:** REST/Web integration for DLC Domain Objects

**Activation:** Automatically active when `@EnableDlc` annotation is set
and if Spring Web is used (on the classpath).

Could be deactivated by:
```java
@EnableDlc(exclude = DlcSpringWebAutoConfiguration.class)
```

**Features:**
- Automatic conversion of string parameters to Domain Objects
- Parameter converters for ValueObjects and Identities
- `ResponseEntityBuilder` for consistent API responses

### 7. OpenAPI Autoconfig (`DlcSpringOpenApiAutoConfiguration`)

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

More information on [DLC Open API](./../spring-doc2-integration/readme.md)

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
logging.level.io.domainlifecycles.boot3.autoconfig=DEBUG
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

