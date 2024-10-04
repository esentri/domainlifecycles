# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-06-18

### Added

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
- Added rendering for QueryClients and OutboundServices
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



