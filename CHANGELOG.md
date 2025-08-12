# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.5.0] - 2025-08-12
- Upgraded all Spring Boot and Spring dependencies to SpringBoot version 3.5 and compatible versions
- Extended default implementation of Entities and ValueObjects companion classes with 
  pure reflective fallbacks for 'equals()', 'hashCode()', 'toString()'. That also enables 
  the corresponding base classes to use 'equals()', 'hashCode()', 'toString()' without having 
  the mirror initialized.

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
- io.domainlifecycles.jackson.api.JacksonMappingCustomizer interface changed due to new mirror integration
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



