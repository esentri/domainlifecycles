# NitroX Domain Lifecycles (NitroX DLC)

NitroX DLC is a Java based framework for developing business applications following the principles of 
`Domain Driven Design` (DDD). The goal is to simplify and accelerate a Java based [tactical design](https://thedomaindrivendesign.io/what-is-tactical-design/),
so that developers can focus on modelling the domain in an appropriate way without being restricted, constrained 
or accidentally being influenced by other pure technical frameworks.

NitroX DLC provides features to reduce boilerplate code, especially in adapters implementations to the technical infrastructure.
Another idea is to make tactical patterns transparent within the code using marker interfaces. 
Finally, NitroX DLC supports all kinds of domain isolating architecture styles, which
enforce a clean separation of technical from domain concerns, in order to provide better long term quality characteristics regarding
adaptability, expandability and maintainability as well as a consistently high development.
([Ports & Adapters](https://alistair.cockburn.us/hexagonal-architecture/), 
[Onion Architecture](https://medium.com/the-software-architecture-chronicles/onion-architecture-79529d127f85),
[Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
).

![Added value](documentation/resources/images/added_value.png "NitroX DLC added value")

NitroX DLC should enable developers to focus on the domain model and the true added value of the solution (the business value)
instead of "getting lost" in pure technical concerns. Despite the quick implementation of typical default structures, the developer 
should always be able to customize any part of the solution, if needed (e.g. custom mappers, custom naming conventions,...). 

## Features

NitroX DLC provides following core features:

- [`DLC Domain Types`](./readme_building_blocks.md)): Implementation of DDD building blocks using NitroX interfaces or base classes
    * Mark DDD building blocks in the Java code: Make your tactical design more transparent
    * Base for Java typesystem backed meta-data (see below Domain
      Mirror)

- [`DLC Domain Mirror`](./readme_mirror.md): Mirrors the metamodel of the tactical design structures within bounded contexts 
    * Provides structural queries and navigation through a bounded contexts structural metamodel as well as simplified reflective access
      on values of instances of a domain's `Aggregates`, `Entities`
      and `ValueObjects`

- [`DLC DomainObject Builders`](./readme_builder_pattern.md): Builder pattern support
    * Complements the DDD Factory pattern
    * Optional: Lombok Builder compatibility

- [`DLC JSON Mapping`](./readme_json_mapping.md): [Jackson](https://github.com/FasterXML/jackson) based JSON mapping
    * Auto mapping DDD building blocks form JSON to Java and vice versa
    * Spring compatible
    * MappingCustomizer API to customize mapping logic
    * Optional: DB Sequence based ID provisioning within a JSON Mapper

- [`DLC Open API Extension`](./readme_open_api.md): API documentation DDD
  building blocks based on [Spring Doc Open API](https://springdoc.org/)
    * API documentation consistently to NitroX DLC's Jackson AutoMapping features (out-of-the-box)
    * Spring compatible
    * Adds Bean Validation information in API documentation (together with NitroX Bean Validation Support)

- [`DLC Persistence`](./readme_persistence.md): Simplify object relational mapping for persisted DomainObjects
    * Type-safe queries based on jOOQ
    * Supporting and abstracting many common relational database dialects (via jOOQ)
    * Simplified Aggregate queries (NitroX DLC Fetcher)
    * Simplified Aggregate CRUD support (NitroX DLC Repositories)
    * Object relational auto mapping
    * Persistence Action Event hooks
    * Full ValueObject support regarding persistence
    * Supports `final` Keywords and Java-Optionals within persisted structures

- [`DLC Domain Events`](./readme_domain_events.md): Simplifies some technical concerns about publishing and listening to DomainEvents
    * Reduce publisher boilerplate code using the static `DomainEvents.publish()` API 
    * Reduce listener and event routing boilerplate code by using the `@ListensTo` annotation
    * Optional: Specific support for Spring or JTA based transaction handling
    * Optional: Support of the "transactional outbox" pattern for more reliable publishing of DomainEvents 

- [`DLC Validation`](./readme_validation.md): Simplifies the implementation of business rules and domain specific invariants
    * DomainAssertion API
    * Optional: Extended Support for Java Bean Validation Annotations within DomainObjects to define 
      invariants or method pre- and post-Conditions.
    * Optional: ByteCode extension to simplify the implementation of an
      “Always-Valid-Strategy”

- `DLC Spring Support`: 
    * Spring Web Support to enable
      Identities and “single-valued” ValueObjects being directly used as path or query
      parameters

- [`DLC Domain Diagrammer`](./readme_diagrammer.md): Generate text based class diagram for DDD building blocks
    * based on [Nomnoml](https://nomnoml.com/)


## NitroX DLC Projekt Setup

### NitroX DLC dependencies

NitroX DLC provides several JARs which enable the NitroX DLC core features independently 
(they correspond to according sub modules):

| Feature                                                             | Relevant for           | Dependency (groupid:artifactid) |
|---------------------------------------------------------------------|------------------------|---------------------------------|
| Basic domain type interfaces                                        | application developers | nitrox:dlc-types                |
| Domain assertions, to express domain rules and invariants           | application developers | nitrox:dlc-assertions           |                         
| Abstract base domain types and utilities                            | application developers | nitrox:dlc-type-utils           |                       
| Domain type builders                                                | only internally used   | nitrox:dlc-builder              |
| General reflection utilities                                        | only internally used   | nitrox:dlc-reflect              |
| Interfaces and implementation to access object and class structures | only internally used   | nitrox:dlc-access               |
| Domain mirror                                                       | only internally used   | nitrox:dlc-mirror               |
| Domain Event support                                                | application developers | nitrox:dlc-domain-events        |  
| Jackson based JSON mapping                                          | application developers | nitrox:dlc-jackson              |                         
| Persistence interfaces and general persistence management           | only internally used   | nitrox:dlc-persistence          | 
| jOOQ based implementation for persistence management                | application developers | nitrox:dlc-jooq                 |
| Bean Validation support (javax or jakarta)                          | application developers | nitrox:dlc-bean-validations     |
| Byte Buddy based auto validation extension                          | application developers | nitrox:dlc-validation-extender  |
| Spring Doc open API support (Spring Boot 2 compatible)              | application developers | nitrox:dlc-spring-doc           | 
| Spring Doc 2 open API support (Spring Boot 3 compatible)            | application developers | nitrox:dlc-spring-doc-2         | 
| General Swagger / Open API v3 support                               | only internally used   | nitrox:dlc-swagger-v3           | 
| Spring Web support (Spring Boot 2 compatible)                       | application developers | nitrox:dlc-spring-web           | 
| Spring Web support (Spring Boot 3 compatible)                       | application developers | nitrox:dlc-spring-web-6         |
| Nomnoml based domain diagrams                                       | application developers | nitrox:dlc-domain-diagrammer    | 

To simplify the dependency management using all features in a Spring Boot app using jOOQ for the relational
database persistence management, we provide JARs for a Spring Boot 2 or Spring Boot 3 setup by adding just a single dependency:

| Application setup                                        | Dependency                              | 
|----------------------------------------------------------|-----------------------------------------|
| Spring Boot 2 app with all NitroX DLC features available | nitrox:dlc-spring-boot-2-jooq-complete  |
| Spring Boot 3 app with all NitroX DLC features available | nitrox:dlc-spring-boot-3-jooq-complete  |

Gradle setup for a Spring Boot 3 app:
```Groovy
dependencies{
    implementation 'nitrox:dlc-spring-boot-3-jooq-complete:2.0.0'
}
```

Maven setup for a Spring Boot 3 app:
```XML
<dependency>
    <groupId>nitrox</groupId>
    <artifactId>dlc-spring-boot-3-jooq-complete</artifactId>
    <version>2.0.0</version>
</dependency>
```

Have a look at the sample project mentioned below.

### Additional runtime dependencies

Depending on the features used, additionally to the NitroX dependencies other runtime libraries must be provided.

| Feature                                                               | External dependency                                                                            | Supported versions      |
|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------|-------------------------|
| Object Builders Lombok Support                                        | org.projectlombok:lombok                                                                       | tested with 1.18.30     |     
| Validation - Javax Bean Validation 2.0 Support                        | (Bean Validation Provider implementation) e.g.: org.hibernate.validator:hibernate-validator    | tested with 6.2.3.Final |
| Validation - Jakarta Bean Validation 3.0 Support                      | (Bean Validation Provider implementation) e.g.: org.hibernate.validator:hibernate-validator    | tested with 8.0.1.Final |
| Persistence                                                           | org.jooq:jooq                                                                                  | tested with 3.19.6      |
| JSON Mapping                                                          | com.fasterxml.jackson.core:jackson-core <b>and</b> com.fasterxml.jackson.core:jackson-databind | tested with 2.17.0      |
| Open API Support (Spring Doc 1)                                       | org.springdoc:springdoc-openapi-data-rest <b>and</b> org.springdoc:springdoc-openapi-ui        | tested with 1.8.0       |
| Open API Support (Spring Doc 2)                                       | org.springdoc:springdoc-openapi-starter-webmvc-ui                                              | tested with 2.4.0       |
| Spring Converter Support (needed for Spring-Web, Spring Boot 2)       | org.springframework:spring-core                                                                | tested with 5.3.33      |
| Spring Converter Support (needed for Spring-Web, Spring Boot 3)       | org.springframework:spring-core                                                                | tested with 6.1.5       |
| Domain Events, Spring based Transactional Outbox (Spring Boot 3 only) | org.springframework:spring-jdbc                                                                | tested with 6.1.5       |
| Domain Events, Spring based Transaction Support (Spring Boot 3 only)  | org.springframework:spring-tx                                                                  | tested with 6.1.5       |
| Domain Events, Jakarta JTA Support                                    | (JTA Provider implementation) e.g.: Atomikos com.atomikos:transactions-jta                     | tested with 6.0.0       |
| Logging                                                               | (SLF4J Provider) e.g.: ch.qos.logback:logback-classic                                          | tested with 1.5.3       |           

Newer or even older versions which are not tested might still work! Just try it.

NitroX DLC logging is based on SLF4J.
Remember to provide a SLF4J-Provider in your classpath.

### Sample projects

A Gradle based sample project that demonstrates all NitroX features can be found [here](./dlc-sample)
