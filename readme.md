# DomainLifeCycles (DLC)

DomainLifeCycles is a Java based set of libraries for developing business applications following the principles of
`Domain Driven Design` (DDD). The goal is to simplify and accelerate the design and implementation with Java
based tactical design ([DDD and DLC tactical design concepts](concepts/readme.md)),
so that developers can focus on modelling the domain in an appropriate way without being restricted, constrained
or accidentally being influenced by other pure technical frameworks.

## The Opinions Behind DomainLifeCycles (DLC)
DomainLifeCycles (DLC) is an opinionated set of Java libraries for Domain-Driven Design (DDD), focused on helping developers 
express business logic clearly, model rich domain lifecycles, and maintain conceptual clarity 
— all while integrating smoothly with the Java ecosystem. These are the guiding principles behind DLC:

1. <b>Convention over Configuration — with Escape Hatches</b>
   <br>
   DLC follows the principle of convention over configuration to help developers stay focused on the domain model 
   and the business value of the solution. With sensible defaults and ready-to-use structures, developers can move 
   quickly and avoid boilerplate. But DLC also embraces flexibility: every convention can be overridden. 
   Whether it’s custom mappers, naming strategies, or integration details, you’re free to adapt the framework to your 
   specific context — giving you control when you need it without cluttering the common path.

2. <b>Explicit Behavior, Not Hidden Magic For The Business Domain</b>
   <br>
   DLC encourages business logic to be expressed clearly in code, rather than being hidden behind annotations, 
   reflection, or framework magic. Domain behavior, state transitions, and invariants are modeled directly and explicitly 
   — making the system easier to understand, test, and evolve. One specific feature in DLC is that in enables "always valid" 
   domain objects. 

3. <b>Type-Based Domain Modeling</b>
   <br>
   At its core, DLC is type-defined. It promotes modeling with rich, expressive types in an object-oriented way,
   that capture intent and domain semantics. Instead of relying solely on general-purpose Entities or Value Objects, 
   DLC enables you to create types that reflect actual domain concepts and behaviors. DLC also extends the standard DDD 
   building blocks by introducing complementary types that clarify architectural intent and responsibility, such as: 
   ApplicationServices, DomainCommands, ReadModels, QueryHandlers, OutboundServices. These 
   types enhance structure, support separation of concerns, and help articulate the flow of business operations and 
   external communication more effectively.

6. <b>Embrace the Framework, Don’t Fight It</b>
   <br>
   DLC is built to complement DDD, not constrain it. If you're following DDD principles, DLC will feel like a natural 
   extension of your thinking. It encourages modeling behavior explicitly and transparently — 
   instead of pushing you into technical or accidental complexity, that you might know from using other technologies 
   that were not built around DDD concepts. You don’t have to fight the framework to do the right thing.

7. <b>Concept Transparency</b>
   <br>
   Clarity and transparency are central values in DLC. DLC’s type system makes domain rules and behaviors
   explicit in both code and architecture — enhancing maintainability, collaboration, and comprehension. 
   This transparency is further reinforced by DLC’s ability to generate domain diagrams directly from the codebase. 
   These diagrams help teams quickly grasp the domain model, its components, and their relationships — 
   ensuring alignment between implementation and domain understanding, and keeping documentation always up-to-date.

8. <b>Modular and Independent</b>
   <br>
   DLC is designed to be modular. You can adopt only the parts that fit your current architecture or goals. 
   Whether you're introducing only DLC marker interfaces and diagramming features, or you use runtime support features like
   DLC Validations, DLC Persistence or DLC DomainEvents each feature can be used independently. There's no all-or-nothing commitment — just useful tools you can opt into.

9. <b>Integratable by Design</b>
   <br>
   DLC plays well with the broader Java ecosystem. It is designed to integrate seamlessly with standard frameworks and 
   libraries like Spring and Jakarta EE, making it easy to adopt in new or existing projects. 

## Features

DLC provides following core features:

- [`Domain Types`](concepts/readme.md): Implementation of DDD building blocks using DLC interfaces or base
  classes
    * Mark DDD building blocks in the Java code: Make your tactical design more transparent
    * Base for Java typesystem backed meta-data (see below Domain Mirror)

- [`Domain Mirror`](mirror/readme.md): Mirrors the metamodel of the tactical design structures within bounded contexts
    * Provides structural queries and navigation through a bounded contexts structural metamodel as well as simplified
      reflective access on values of instances of a domain's `Aggregates`, `Entities` and `ValueObjects`

- [`Domain Object Builders`](builder/readme.md): Builder pattern support
    * Complements the DDD Factory pattern
    * Optional: Lombok Builder compatibility

- [`JSON Mapping`](jackson-integration/readme.md): [Jackson](https://github.com/FasterXML/jackson) based JSON mapping
    * Auto mapping DDD building blocks form JSON to Java and vice versa
    * Spring compatible
    * MappingCustomizer API to customize mapping logic
    * Optional: DB Sequence based ID provisioning within a JSON Mapper

- [`Open API Extension`](spring-doc-2-integration/readme.md): API documentation DDD
  building blocks based on [Spring Doc Open API](https://springdoc.org/)
    * API documentation consistently to DLCs Jackson AutoMapping features (out-of-the-box)
    * Spring compatible
    * Adds Bean Validation information in API documentation (together with DLC Bean Validation Support)

- [`Persistence`](persistence/readme.md): Simplify object relational mapping for persisted DomainObjects
    * Type-safe queries based on jOOQ
    * Supporting and abstracting many common relational database dialects (via jOOQ)
    * Simplified Aggregate queries (DLC Fetcher)
    * Simplified Aggregate CRUD support (DLC Repositories)
    * Object relational auto mapping
    * Persistence Action Event hooks
    * Full ValueObject support regarding persistence
    * Supports `final` Keywords and Java-Optionals within persisted structures

- [`Domain Events`](domain-events-core/readme.md): Simplifies some technical concerns about publishing and listening to
  DomainEvents
    * Reduce publisher boilerplate code using the static `DomainEvents.publish()` API
    * Reduce listener and event routing boilerplate code by using the `@ListensTo` annotation
    * Optional: Specific support for Spring or JTA based transaction handling
    * Optional: Support of the "transactional outbox" pattern for more reliable publishing of DomainEvents

- [`Validation`](validation-extender/readme.md): Simplifies the implementation of business rules and domain specific invariants
    * DomainAssertion API
    * Optional: Extended Support for Java Bean Validation Annotations within DomainObjects to define
      invariants or method pre- and post-Conditions.
    * Optional: ByteCode extension to simplify the implementation of an
      “Always-Valid-Strategy”

- `Spring Web Integration`:
    * Spring Web Integration to enable
      Identities and “single-valued” ValueObjects being directly used as path or query
      parameters

- [`Domain Diagrammer`](domain-diagrammer/readme.md): Generate text based class diagram for DDD building blocks
    * based on [Nomnoml](https://nomnoml.com/)

- [`Plugins`](dlc-plugins/readme.md): Build management support for Maven and Gradle

## Getting started
You can find our "Getting Started Guide" [here](./getting-started/english/index_en.md)

## DDD and related concepts
For more information about the underlying concepts and ideas, especially regarding tactical DDD and its implementation using DLC, have a look [here](concepts/readme.md).

## DLC Project Setup

### DLC dependencies

DLC provides several JARs which enable the DLC features independently
(they correspond to according sub modules):

| Feature                                                             | Relevant for           | Dependency (groupid:artifactid)              |
|---------------------------------------------------------------------|------------------------|----------------------------------------------|
| Basic domain type interfaces                                        | application developers | io.domainlifecycles:types                    |
| Domain assertions, to express domain specific rules and invariants  | application developers | io.domainlifecycles:assertions               |                         
| Abstract base domain types and utilities                            | application developers | io.domainlifecycles:type-utils               |                       
| Domain type builders                                                | only internally used   | io.domainlifecycles:builder                  |
| General reflection utilities                                        | only internally used   | io.domainlifecycles:reflect                  |
| Interfaces and implementation to access object and class structures | only internally used   | io.domainlifecycles:access                   |
| Domain mirror                                                       | only internally used   | io.domainlifecycles:mirror                   |
| Domain event support                                                | application developers | io.domainlifecycles:domain-events            |  
| Jackson based JSON mapping                                          | application developers | io.domainlifecycles:jackson-integration      |                         
| Service registry                                                    | only internally used   | io.domainlifecycles:service-registry         |
| Persistence interfaces and general persistence management           | only internally used   | io.domainlifecycles:persistence              | 
| jOOQ based implementation for persistence management                | application developers | io.domainlifecycles:jooq-integration         |
| Bean Validation support (javax or jakarta)                          | application developers | io.domainlifecycles:bean-validations         |
| Byte Buddy based auto validation extension                          | application developers | io.domainlifecycles:validation-extender      |
| Spring Doc open API support (Spring Boot 2 compatible)              | application developers | io.domainlifecycles:spring-doc-integration   | 
| Spring Doc 2 open API support (Spring Boot 3 compatible)            | application developers | io.domainlifecycles:spring-doc-2-integration | 
| General Swagger / Open API v3 support                               | only internally used   | io.domainlifecycles:swagger-v3-integration   | 
| Spring Web support (Spring Boot 2 compatible)                       | application developers | io.domainlifecycles:spring-web-integration   | 
| Spring Web support (Spring Boot 3 compatible)                       | application developers | io.domainlifecycles:spring-web-6-integration |
| Nomnoml based domain diagrams                                       | application developers | io.domainlifecycles:domain-diagrammer        | 

To simplify the dependency management using all features in a Spring Boot app using jOOQ for the relational
database persistence management, we provide JARs for a Spring Boot 2 or Spring Boot 3 setup by adding just a single
dependency:

| Application setup                                 | Dependency                                      | 
|---------------------------------------------------|-------------------------------------------------|
| Spring Boot 2 app with all DLC features available | io.domainlifecycles:spring-boot-2-jooq-complete |
| Spring Boot 3 app with all DLC features available | io.domainlifecycles:spring-boot-3-jooq-complete |

Gradle setup for a Spring Boot 3 app:

```Groovy
dependencies{
    implementation 'io.domainlifecycles:spring-boot-3-jooq-complete:2.5.0.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc.rc'
}
```

Maven setup for a Spring Boot 3 app:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>spring-boot-3-jooq-complete</artifactId>
    <version>2.5.0.rc</version>
</dependency>
```

These single dependencies make the target application build mechanism (Maven or Gradle) providing (downloading) the
required DLC modules and external dependencies
for the target applications classpath.

For detailed information on the setup, have a look at the sample project mentioned below.

### Additional runtime dependencies

Depending on the features used, additionally to the DLC dependencies other runtime libraries must be provided in the
target applications classpath.
Here's an overview of the most important external dependencies:

| Feature                                                               | External dependency                                                                            | Supported versions      |
|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------|-------------------------|
| Optional Object Builders Lombok Support                               | org.projectlombok:lombok                                                                       | tested with 1.18.38     |     
| Optional fine grained type resolving in the DLC mirror                | com.github.vladislavsevruk:type-resolver                                                       | tested with 1.0.3       |
| Runtime class loading in the DLC mirror and DLC persistence           | io.github.classgraph:classgraph                                                                | tested with 4.8.163     |
| Validation - Javax Bean Validation 2.0 Support                        | (Bean Validation Provider implementation) e.g.: org.hibernate.validator:hibernate-validator    | tested with 6.2.3.Final |
| Validation - Jakarta Bean Validation 3.0 Support                      | (Bean Validation Provider implementation) e.g.: org.hibernate.validator:hibernate-validator    | tested with 8.0.1.Final |
| Validation extension vi Byte Buddy                                    | net.bytebuddy:byte-buddy                                                                       | tested with 1.15.10     |
| Persistence                                                           | org.jooq:jooq                                                                                  | tested with 3.19.6      |
| JSON Mapping                                                          | com.fasterxml.jackson.core:jackson-core <b>and</b> com.fasterxml.jackson.core:jackson-databind | tested with 2.19.0      |
| Open API Support (Spring Doc 1)                                       | org.springdoc:springdoc-openapi-data-rest <b>and</b> org.springdoc:springdoc-openapi-ui        | tested with 1.8.0       |
| Open API Support (Spring Doc 2)                                       | org.springdoc:springdoc-openapi-starter-webmvc-ui                                              | tested with 2.6.0       |
| Spring Converter Support (needed for Spring-Web, Spring Boot 2)       | org.springframework:spring-core                                                                | tested with 5.3.33      |
| Spring Converter Support (needed for Spring-Web, Spring Boot 3)       | org.springframework:spring-core                                                                | tested with 6.2.7       |
| Domain Events, Spring based Transactional Outbox (Spring Boot 3 only) | org.springframework:spring-jdbc                                                                | tested with 6.2.7       |
| Domain Events, Spring based Transaction Support (Spring Boot 3 only)  | org.springframework:spring-tx                                                                  | tested with 6.2.7       |
| Domain Events, Jakarta JTA Support                                    | (JTA Provider implementation) e.g.: Atomikos com.atomikos:transactions-jta                     | tested with 6.0.0       |
| Domain Events Gruelbox Transactional Outbox                           | com.gruelbox:transactionoutbox-core                                                            | tested with 6.0.553     |
| Domain Events ActiveMq Classic                                        | org.apache.activemq:activemq-client                                                            | tested with 5.18.4      |
| Logging                                                               | (SLF4J Provider) e.g.: ch.qos.logback:logback-classic                                          | tested with 1.5.3       |           

Run `./gradle dependencies` on the main project or any of the submodules to get a complete overview of the dependencies
that must be provided on the target applications runtime classpath.

Newer or even older versions which are not tested might still work! Just try it.

DLC logging is based on SLF4J.
Remember to provide a SLF4J-Provider in your classpath.

### Sample project

A Gradle based sample project that demonstrates all DLC features can be found [here](./sample-project)

