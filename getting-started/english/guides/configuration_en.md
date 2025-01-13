[Getting Started](../index_en.md) / [Configuration](configuration_en.md)

---

# Configuration

DLC offers many options for individualization and configuration. Some specific configurations are mandatory
to make the full range of functions available.
Below you can see all configurations (or Spring beans) that are required for the basic DLC functions.
All beans are sorted according to the respective DLC feature to which they belong.
The guide to the [Features](features_en.md) goes into more detail in many places and refers back to some of them.

---

## Persistence
The Persistence module cares about all interactions between DLC and a relational database
regarding the mapping of aggregates and the corresponding persistence access via repositories.

In addition to the following configuration, examples of implementation can be found [here](../features/persistence_en.md).

### Database Driver
First of all, the respective database driver must be integrated in order to use a database.
As DLC uses jOOQ (see below), almost all relational SQL-based databases are supported.
A list of all supported databases can be found 
[here](https://www.jooq.org/doc/latest/manual/reference/supported-rdbms/").

In the following, an internal H2 database is used for the exemplary configurations.

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'com.h2database:h2:2.3.232'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.3.232</version>
    </dependency>
</dependencies>
```
</details>

Additionally, the respective configuration in addition to the required dependency:

<details>
<summary><b>application.properties</b></summary>

```properties
spring.datasource.url=jdbc:h2:./dlc-sample/build/h2-db/test;AUTO_SERVER=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```
</details>

### JOOQ
For simpler and, above all, type-safe JDBC access, the jOOQ framework is used at DLC. 
This facilitates working with SQL-based relational databases and abstracts SQL dialect-specific peculiarities. 
Additionally, the Spring Boot-specific jOOQ dependency is required for this purpose:

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jooq'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jooq</artifactId>
    </dependency>
</dependencies>
```
</details>

With jOOQ, Java classes are usually generated for accessing database objects (e.g. tables and sequences).
To do this, the jOOQ code generator must be integrated into the build management.
Here is an example configuration for an existing database schema:

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
jooq {
    configurations {
        main {
            generationTool {
                jdbc {
                    driver = 'org.h2.Driver'
                    url = "jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE"
                    user = 'sa'
                    password = ''
                }
                generator {
                    database {
                        name = 'org.jooq.meta.h2.H2Database'
                        includes = '.*'
                        inputSchema = "${your_input_schema_name}"
                        recordVersionFields = 'CONCURRENCY_VERSION'
                        forceIntegerTypesOnZeroScaleDecimals = true
                    }
                    generate {
                        generatedAnnotation = false
                        generatedAnnotationType = 'DETECT_FROM_JDK'
                        javaTimeTypes = true
                    }
                    target {
                        packageName = "${your_package_name}"
                    }
                }
            }
        }
    }
}
```
</details>

The configuration for Maven is made in the pom.xml.

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>library.xml</b></summary>

```xml
<plugin>
    <groupId>org.jooq</groupId>
    <artifactId>jooq-codegen-maven</artifactId>
    <version>3.19.16</version>

    <!-- The plugin should hook into the generate goal -->
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>

    <!-- Manage the plugin's dependency. In this example, we'll use a H2 database -->
    <dependencies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.3.232</version>
        </dependency>
    </dependencies>

    <configuration>
        <jdbc>
            <driver>org.h2.Driver</driver>
            <url>jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE</url>
            <user>sa</user>
            <password></password>
        </jdbc>
        <generator>
            <database>
                <name>org.jooq.meta.h2.H2Database</name>
                <includes>.*</includes>
                <inputSchema>${your_input_schema_name}</inputSchema>
                <recordVersionFields>CONCURRENCY_VERSION</recordVersionFields>
                <forceIntegerTypesOnZeroScaleDecimals>true</forceIntegerTypesOnZeroScaleDecimals>
            </database>
            <generate>
                <generatedAnnotation>false</generatedAnnotation>
                <generatedAnnotationType>DETECT_FROM_JDK</generatedAnnotationType>
                <javaTimeTypes>true</javaTimeTypes>
            </generate>
            <target>
                <packageName>${your_package_name}</packageName>
            </target>
        </generator>
    </configuration>
</plugin>
```
</details>

For DLC Persistence, the necessary ```CONCURRENCY_VERSION``` is super important, so that the optimistic locking of 
aggregates and entities will work correctly.

Additional information regarding the configuration of JOOQ can be found
(https://www.jooq.org/doc/latest/manual/code-generation/codegen-configuration/)[here].

### Spring Beans
The following configurations are required specifically for database access with jOOQ:
```DataSourceConnectionProvider```, ```DefaultConfiguration```, ```DefaultDSLContext```.

#### `DataSourceConnectionProvider`
```
@Bean
public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
}
```

#### `DefaultConfiguration`
```
@Bean
public DefaultConfiguration configuration(DataSource dataSource) {
    final var jooqConfig = new DefaultConfiguration();
    jooqConfig.settings().setExecuteWithOptimisticLocking(true);
    jooqConfig.setConnectionProvider(connectionProvider(dataSource));
    jooqConfig.set(SQLDialect.H2);
    return jooqConfig;
}
```

The correct SQLDialect and enabling optimistic locking is important.

#### `DefaultDSLContext`
```
@Bean
public DefaultDSLContext dslContext(DataSource dataSource) {
    return new DefaultDSLContext(configuration(dataSource));
}
```

Further configurations must be made specifically for DLC Persistence:

#### `JooqDomainPersistenceProvider`
The JooqDomainPersistenceProvider enables jOOQ-specific access to all domain objects.

```
@Bean
public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                Set<RecordMapper<?, ?, ?>> customRecordMappers) {
    return new JooqDomainPersistenceProvider(
        JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withCustomRecordMappers(customRecordMappers)
            .withRecordPackage(JOOQ_RECORD_PKG)
            .make());
}
```

Adapted RecordMappers for different mapping behavior between database tables and domain objects can be passed here.
The ```recordPackage``` must specify the package of the classes created by the jOOQ generator for accessing database 
objects.

#### `EntityIdentityProvider`
The EntityIdentityProvider makes it possible for new entities or AggregateRoots to be introduced into the application 
from outside (e.g. via a REST controller) and for new IDs to be retrieved from the corresponding database sequences 
or other ID providers for new instances.
Only used in conjunction with ```DlcJacksonModule```, see below.

```
@Bean
EntityIdentityProvider identityProvider(DSLContext dslContext) {
    return new JooqEntityIdentityProvider(dslContext);
}
```

#### `SpringPersistenceEventPublisher`
Optional, required to publish DLC persistence events via the Spring Event Bus.
DLC persistence events have information about changes to aggregates after writing operations in the repository.

```
@Bean
public SpringPersistenceEventPublisher springPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
return new SpringPersistenceEventPublisher(applicationEventPublisher);
}
```

---


## Domain-Object-Builders
Domain object builders are required internally by DLC, for example to create instances of domain objects during 
object-relational mapping or JSON de-serialization. In addition to the following configuration, implementation examples 
can be found [here](../features/domainobject_builders_en.md).

### Spring-Beans
The following Spring beans must be configured and provided in a ```@Configuration``` class as ```@Bean```:

#### `DomainObjectBuilderProvider`
The DomainObjectBuilderProvider is required to work with inner-Builders or the Lombok-Builders (Lombok ```@Builder``` annotation).

```
@Bean
DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
    return new InnerClassDomainObjectBuilderProvider();
}
```

---


## JSON-Mapping
In addition to the following configuration, implementation examples can be found [here](../features/json_mapping_en.md).

### Spring-Beans
The following Spring beans must be configured and provided in a ```@Configuration``` class as ```@Bean```:

#### `DlcJacksonModule`
Required for DLC-Jackson integration, i.e. for JSON de-serialization.

```
@Bean
DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                        DomainObjectBuilderProvider domainObjectBuilderProvider,
                                        EntityIdentityProvider entityIdentityProvider
) {
    DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
    return module;
}
```

## Domain-Events
In addition to the following configuration, examples of implementation can be found [here](../features/domain_events_en.md).

### Spring-Beans
The following Spring beans must be configured and provided in a ```@Configuration``` class as ```@Bean```:

#### `ChannelRoutingConfiguration`
Provides a configuration for channel routing.

```
@Bean
public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
    var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
    var router = new DomainEventTypeBasedRouter(List.of(channel));
    router.defineDefaultChannel("default");
    return new ChannelRoutingConfiguration(router);
}
```
This routing defines how domain events are technically processed. In this case, domain events are delivered in-memory 
and transaction-bound from DLC to corresponding listeners. As an alternative to the in-memory variant, these domain 
events could also be delivered via an external event bus (e.g. via JMS). Transaction-bound means that, depending on the
configuration, the publication of the domain events always takes place via a trigger transaction depending on the 
configuration ```beforeCommit``` or ```afterCommit```.

#### `ServiceProvider`
Provides a provider for all required ```ServiceKind``` objects. This is required for the event delivery defined in the route.

```
public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
    return new Services(serviceKinds);
}
```

---

## Open-API-Extension
In addition to the following configuration, examples of implementation can be found [here](../features/open_api_extension_en.md).

### Spring-Beans
The following Spring beans must be configured and provided in a ```@Configuration``` class as ```@Bean```:

#### `DlcOpenApiCustomizer`
Activates the DLC/OpenAPI integration.

```
@Bean
public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
    return new DlcOpenApiCustomizer(springDocConfigProperties);
}
```

---

|         **Build-Management**          |           **Run DLC**            |
|:-------------------------------------:|:--------------------------------:|
| [<< Previous](build_management_en.md) | [Next >>](run_application_en.md) |

---

**EN** / [DE](../../german/guides/configuration_de.md)
