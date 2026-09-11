# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Added [static analysis](./static-analysis) module, answering which domain methods are called from
  which other domain methods, based on the DomainMirror and a compiled classpath. It carries the
  `StaticAnalyzer` abstraction, the result model and the flow analysis, and depends on nothing but
  the mirror
- Added [static analysis sootup](./static-analysis-sootup) module holding the SootUp based
  `StaticAnalyzer` implementation. Kept as a module of its own so that reading an analysis result -
  for instance to filter a diagram - does not drag a bytecode analysis framework onto the classpath
- The analyzer reads the invoke instructions directly out of the bodies of the mirrored methods
  instead of building a global call graph, so only the inspected bodies are translated and the
  analysis stays confined to the domain
- Resolves lambdas (also nested ones), method references (bound, unbound and static), private
  helper methods, overloads, generic methods with compiler generated bridge methods and inherited
  methods; recursive and mutually recursive calls terminate
- Calls are resolved once per concrete owner: a `this` dispatch inside an inherited body is
  attributed to the overriding subtype, while `super.x()` stays attributed to the base class
- Default methods of mirrored interfaces are analyzed for every implementation not overriding them
- `DomainCalls` holds the immutable result and indexes both directions (`callsFor` and
  `callersOf`). Its nodes combine the concrete owner type with the mirrored method, its edges carry
  the type whose body contained the call and the source line
- `Diagnostic` reports what could not be analyzed - most importantly a mirrored type missing from
  the classpath - so that an empty result can be told apart from an unanalyzable one; `isComplete()`
  summarizes whether the result can be trusted
- `DomainClasspath` derives the classpath to analyze from the mirror, from given types or from a
  classpath string
- Added `FlowAnalyzer`, answering which domain types and methods are reachable from a given method,
  domain event or domain command, by joining the analyzed calls with the event and command
  information of the mirror
- A flow follows four kinds of edges: method calls, dispatch into implementations and overrides,
  published domain events together with the methods listening to them, and the methods processing a
  domain command. Several listeners of one event branch the flow into independent continuations
- Flows are traversed breadth first and stay bounded: every node is expanded at most once, cycles -
  including those closing over a published event - are reported and not followed, and a configurable
  maximum depth reports truncation
- `FlowConfig` allows limiting the depth, switching off event or implementation edges and filtering
  methods (e.g. excluding accessors); a flow renders as an indented tree
- The [domain diagrammer](./domain-diagrammer) optionally takes such an analysis result and can
  reduce a diagram to the classes taking part in one flow, via the new `includeFlowsFrom` trim
  setting - starting at a domain command, a domain event, a single method or a whole type. The
  restriction is combined with the existing trim settings and can only narrow what they allow
- The Gradle and Maven diagram plugins now support flow-based diagram filtering too: the new
  `includeFlowsFrom`, `flowMaxDepth`, `flowFollowEvents`, `flowFollowImplementations` and
  `flowExcludeAccessors` diagram options mirror the diagrammer's flow settings. Whenever
  `includeFlowsFrom` is configured, the plugin runs the SootUp based static analysis on the
  project's compiled classes as part of diagram generation
- Added [static analysis serialization Jackson 3](./static-analysis-serialization-jackson3) and
  [Jackson 2](./static-analysis-serialization-jackson2) modules, JSON (de)serializing a `DomainCalls`
  analysis result so it can be produced once (e.g. in a build step) and consumed elsewhere - for
  instance by an external diagram viewer tool - without re-running the static analysis. A
  `DomainMethod` is written as a compact type/method/parameter-types reference rather than an
  embedded mirror, and resolved back against a `DomainMirror` given at deserialization time
- The Gradle and Maven `domainModelUpload` task/goal now optionally (`runStaticAnalysis`, default
  `true`) also run a static analysis of the compiled domain classes and upload its result
  (`DomainCalls`) alongside the domain model, so a Diagram Viewer can offer flow based diagram
  filtering. `DomainModelUploader` now builds the domain model itself instead of receiving an
  already-serialized JSON string, so the very same `DomainMirror` is reused for both the mirror and
  the static analysis rather than building it twice
- The domain model upload request is now gzip-compressed (`Content-Encoding: gzip`) before being
  sent, since the combined domain model and static analysis JSON can reach the tens of megabytes for
  larger domains; a 10 second connect timeout and a 5 minute overall request timeout were added so an
  unreachable or slow Diagram Viewer fails the build instead of hanging it indefinitely
- `DomainSerializer` (`mirror-serialization-jackson3`/`jackson2`) and `DomainCallsSerializer`
  (`static-analysis-serialization-jackson3`/`jackson2`) gained stream based `serialize`/`deserialize`
  overloads (`OutputStream`/`InputStream`), so a `DomainMirror`/`DomainCalls` can be written to or read
  from a stream directly, without ever holding the complete serialized JSON in memory as a single
  String - a building block towards streaming the domain model upload itself. Both also had Jackson's
  default of closing the given stream once done turned off, to actually honor that contract
- `DomainModelUploader` gained `uploadDomainModelStreaming`, an opt-in alternative to
  `uploadDomainModel` (wired up via the new `streamUpload` option on the Gradle/Maven
  `domainModelUpload` task/goal, default `false`) that streams the gzip-compressed request body
  directly into the HTTP request as it is produced - via a background thread and a bounded
  producer/consumer queue - instead of assembling it completely in memory first. For very large
  domains this trades a flatter memory footprint for the added complexity of a background writer.
  `DomainModelUploaderImpl` also now reuses a single `HttpClient` (previously one was created per
  upload call), avoiding lingering non-daemon client threads

## [3.2.0] - 2026-06-16
- Added support for JMolecules DDD types for DLC mirror, now able to render Domain diagrams using JMolecules marker interfaces or annotations
- Upgraded several libraries, minor version upgrades
- Supporting Gruelbox Transaction Outbox 7.0.707
- Extended Maven and Gradle plugins to support multi-module projects

## [3.1.0] - 2026-03-24
- Added Domain diagram relation stereotypes
- Extended Diagrammer and plugins to hide relation labels or stereotypes
- Gradle Plugin serialize Mirror fixed
- Generally upgraded to Gradle 9.4.0 
- Some smaller readme fixes

## [3.0.0] - 2026-02-27
- Upgraded all Spring Boot and Spring dependencies to SpringBoot version 4.0.x and compatible versions
- Refactored Spring Boot AutoConfiguration to use Spring Boot 4.0.x features
- Added Spring Boot 3 AutoConfiguration for legacy support
- Added integration for Spring Event Bus supported DomainEvents (and Spring Modulith events)
- Added support for Jackson 3.x (mirror serialization, domain event serialization, general DLC Jackson 3 integration)
- Provided fallback support for Jackson 2.x (mirror serialization, domain event serialization, general DLC Jackson 2 integration)
- Refactored Spring Web integration to be independent of Jackson serialization
- Added @DomainEventListener annotation for DomainEvents. @ListensTo annotation is deprecated.
- Fixed jOOQ test class generation
- AutoConfig can load mirror from `META-INF/dlc/mirror.json` without reflection at runtime.
- DLC Build plugins provide support for deserializing mirror to ``META-INF/dlc/mirror.json``

## [2.6.0] - 2025-12-02
- sample-project is not part of the main project anymore, but is now a separate project
- Domain Diagramming documentation extended
- Diagrammer default configuration: 'ApplicationService' is default stereotype, instead of 'Driver'
- KrokiContainer default port changed to 8501 to avoid conflicts with other Kroki containers running on the same machine 
- integrated gradle plugin into sample-project to demonstrate usage of DLC plugin diagram generation 

## [2.5.0] - 2025-10-31
- Upgraded all Spring Boot and Spring dependencies to SpringBoot version 3.5 and compatible versions
- Extended default implementation of Entities and ValueObjects companion classes with 
  pure reflective fallbacks for 'equals()', 'hashCode()', 'toString()'. That also enables 
  the corresponding base classes to use 'equals()', 'hashCode()', 'toString()' without having 
  the mirror initialized.
- Added feature for external comments being rendered in Domain Diagramms.
- Fixed bugs in Domain Diagrams due to missing classes not being rendered in certain inheritance cases.
- Removed deprecated Spring Boot 2 support
- Removed deprecated javax BeanValidation support
- OpenAPI integration now provides OpenAPI 3.1 support
- Bugfix in ValidationDomainClassExtender (was conflicting with other byte code extensions - Jacoco)
- Tested support for Spring Boot 3.5.5
- Added DLC SpringBoot starter and AutoConfiguration for Spring Boot 3.5.5
- Extended Open API configuration Options
- OpenAPI Nullabillity support for Open API 3.0 and 3.1

## [2.4.1] - 2025-06-06
- Added deprecated markers at some classes
- Refactored and extended documentation
- Fixed config setting in Gradle plugin for DomainModel Upload

## [2.4.0] - 2025-05-30
- Refactored and extended Domain diagrammer options for showing inheritance structures
- Adjusted plugin diagram options for showing inheritance structures

## [2.3.0] - 2025-05-27
- Refactored and extended Domain diagrammer with new diagram settings (e.g. options for connection based filtering)
- Added [Maven plugin](./dlc-maven-plugin) providing DLC plugin functions via Maven
- Added [Gradle plugin](./dlc-gradle-plugin) providing DLC plugin functions via Gradle
- Added [DLC plugins](./dlc-plugins) containing the general plugin logic
  (exporting DomainModels, draw Domain diagrams, sending updated DomainModel to DLC Diagram Viewer application, 
  that enables rendering and documenting DomainModels)
- Changed Mirror added DomainMirror interface as the primary mirror containing the complete domain model
- Added completeness check for Domain Mirror ensuring all references to model elements within the domain model can be resolved, 
  when a DomainMirror instance is created either by reflection or by deserialization from a JSON export
- Extended domain diagrammer for filtering options (abstract types and packages) and fixed several exisiting filtering options 

## [2.2.1] - 2025-02-28
- Replaced some critical stream operations in "io.domainlifecycles.persistence.repositoryDomainStructureAwareRepository"

## [2.2.0] - 2025-02-26
- Added shutdown hooks for Gruelbox Domain Event channels, to avoid irritating exceptions for example in tests
- Gruelbox based Domain Event handling now provides distinct outbox entries for each Domain Event Consumer, which
  allows now consumer specific retry behaviour, that can be managed using natural Gruelbox features 
- DLC spring-tx-outbox deprecated for future removal

## [2.1.0] - 2025-02-21
- Introduce version catalog for dependency management
- Replace nu.studer.jooq generator with jOOQ native generator
- Upgrade Flyway Plugin to version 10 and modify usage accordingly
- Replace version in readme.md with version defined in version.properties with `gradle build`
- Upgrade Gruelbox (Domain Events outbox feature) dependency to version 6.0.553
- Renamed "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory" to "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory"
- Renamed "io.domainlifecycles.mirror.api.InitializedDomain" to "io.domainlifecycles.mirror.model.DomainModel"
- An instance of "io.domainlifecycles.mirror.resolver.GenericTypeResolver" can only be applied via "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory" 
- Fixed serialization bugs with "io.domainlifecycles.mirror.model.DomainModel"
- Removed static "io.domainlifecycles.mirror.api.Domain" dependency from Domain Diagrammer

## [2.0.4] - 2025-02-06
- Bytebuddy (ValidationDomainClassExtender) must not try to extend static methods

## [2.0.3] - 2025-01-29
- Bugfix for proper handling of ValueObject duplicates in lists

## [2.0.2] - 2025-01-27

- Fixed deserialization error for the mirror
- Extended diagrammer with background config option 

## [2.0.1] - 2024-11-19

- Fixed misleading error messages in specialized automapping constellations

## [2.0.0] - 2024-11-15

### Added

#### domain-events-core

- First draft of a new module supporting Domain Events, in a way where domain event operations
  and the technical message processing are separated

#### domain-events-activemq-classic-5

- Domain Event support for Active MQ Classic 5

#### domain-events-gruelbox

- Domain Event support for Gruelbox Transactional Outbox

#### domain-events-jakarta-jms

- Domain Event support for Jakarta JMS

#### domain-events-mq

- Domain Event support for Java Messaging (abstract)
- used by domain-events-jakarta-jms and domain-events-activemq-classic-5

#### domain-events-spring-tx

- Spring transaction support for Domain Event handling

#### domain-events-spring-tx-outbox

- Spring based DLC specific outbox implementation (experimental)

#### service-registry

- First draft of a DLC specific service registry, that allows to accessing services in dynamic way at runtime 

#### access

- Completely new module, that replaces class and object functionality from dlc-core
- Added several facades to abstract from direct reflective access to classes and objects
    - io.domainlifecycles.access.DlcAccess as static access provider for the following interfaces
    - io.domainlifecycles.access.classes.ClassProvider
    - io.domainlifecycles.access.object.EnumFactory
    - io.domainlifecycles.access.object.IdentityFactory
    - io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory
    - io.domainlifecycles.access.object.DynamicDomainObjectAccessor

#### builder

- New module, that replaces builder functionality from dlc-core
- Integrated new mirror from dlc-mirror

#### reflect

- New module, that provides simplified reflection access

#### types-utils

- New module, that replaces some dlc-core functionalities (especially base types, mirror based companions, entity
  cloner)

#### types

- New module, that now contains all basic domain type interfaces and annotations
- All Domain interface types moved package (from io.domainlifecycles.domain.api to io.domainlifecycles.domain.types)

### Changed

#### domain-diagrammer

- Little refactorings due to changes in dlc-mirror
- Added rendering for QueryHandlers and OutboundServices
- Added TransitiveDomainTypeFilter option
- Added more configuration options

#### jackson-integration

- Integrated new mirror from dlc-mirror (removed old mirror interface from DlcJacksonModule)
- io.domainlifecycles.jackson3.api.JacksonMappingCustomizer interface changed due to new mirror integration
- Integrated access module

#### jooq-integration

- Integrated new mirror from mirror module (e.g. removed old mirror interface from
  io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration)
- io.domainlifecycles.jooq.imp.matcher.JooqRecordPropertyMatcher replaces
  io.domainlifecycles.jooq.imp.matcher.JooqRecordEntityPropertyMatcher and
  io.domainlifecycles.jooq.imp.matcher.JooqRecordEntityValueObjectMatcher
- Integrated access module

#### mirror

- Replaced reflection utils with reflect module
- Integrated new types from types module (breaking api changes)
- Extended some mirrors with additional meta information (DomainTypeMirror, FieldMirror, ...)
- Added own RuntimeException type io.domainlifecycles.mirror.exception.MirrorException
- Extended domain type visitor with strict "visitTypesOnlyOnce" mode

#### persistence

- Replaced dlc-core dependency with types, type-utils, mirror, access and builder modules (breaking api changes, but
  most of them not relevant for jooq-integration users)
- Added io.domainlifecycles.persistence.mapping.RecordMapper interface to stay independent of
  io.domainlifecycles.persistence.mapping.AbstractRecordMapper
- Added own RuntimeException type io.domainlifecycles.persistence.exception.PersistenceException
- Refactored and unified auto record mapping in new implementation
  io.domainlifecycles.persistence.mapping.AutoRecordMapper
- Removed any internal direct Java reflection usage

#### spring-doc-integration

- Integrated new mirror from mirror module
- Integrated access module

#### spring-doc-2-integration

- Integrated new mirror from mirror module
- Integrated access module

#### spring-web-integration

- Integrated types module

#### spring-web-6-integration

- Integrated types module

#### swagger-v3-integration

- Integrated types module
- Integrated reflect module

#### validation-extender

- Integrated types module

### Removed

#### dlc-core

- removed complete dlc-core module with legacy mirror. Replaced by new modules (access, builder, types, type-utils,
  reflect) and the now fully integrated mirror module

## [1.0.22] - 2023-10-25

### Changed

- last closed source development release



